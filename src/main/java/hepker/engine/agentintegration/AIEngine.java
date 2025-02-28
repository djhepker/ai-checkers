package hepker.engine.agentintegration;

import hepker.ai.ai.Agent;
import hepker.ai.utils.AITools;
import hepker.ai.utils.AgentStats;
import hepker.game.gameworld.PieceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for connecting game to AI
 * */
public final class AIEngine {

    private static final Logger logger = LoggerFactory.getLogger(AIEngine.class);

    private final boolean IS_DUSKY;

    private List<AgentRecord> agents;
    private PieceManager pMgr;


    public AIEngine(PieceManager pMgr, boolean playerLight, String gameTypeString) {
        this.IS_DUSKY = playerLight;
        this.pMgr = pMgr;
        this.agents = new ArrayList<>();
        loadGameState(gameTypeString);
    }

    public void update() {
        try {
            for (AgentRecord agentRecord : agents) {
                updateAgent(agentRecord.getAgent(), agentRecord.getEnvironment(), agentRecord.getDecisionHandler());
            }
        } catch (Exception e) {
            logger.error("Agent Manager Exception", e);
        }
    }

    private void updateAgent(Agent inputAgent, Environment inputEnvironment, AIDecisionHandler inputDecisionHandler) {
        String stateKey = inputEnvironment.getEncodedGameState(pMgr);

        inputAgent.setStateKey(stateKey);
        inputDecisionHandler.updateDecisionContainer();

        int numDecisions = inputDecisionHandler.getNumDecisions();
        if (numDecisions == 0) {
            pMgr.flagGameOver();
            return;
        }

        int actionChoiceInt = inputAgent.getActionInt(numDecisions);
        inputAgent.updateCurrentQ(stateKey, actionChoiceInt);
        inputDecisionHandler.setPreDecisionRewardParameters(actionChoiceInt);
        inputDecisionHandler.movePiece(actionChoiceInt);
        inputDecisionHandler.updateDecisionContainer();

        String stateKeyPrime = inputEnvironment.getEncodedGameState(pMgr);
        inputAgent.updateRho(inputDecisionHandler.getDecisionReward());

        inputAgent.update(stateKeyPrime, actionChoiceInt);
    }

    private GameState loadGameState(String gameTypeString) {
        switch (gameTypeString) {
            case "Agent Vs Player" -> {
                generateAgent(false, IS_DUSKY);
                return GameState.AGENT_VS_PLAYER;
            }
            case "Stochastic Vs Player" -> {
                generateAgent(true, IS_DUSKY);
                return GameState.STOCHASTIC_VS_PLAYER;
            }
            case "Agent Vs Stochastic" -> {
                generateAgent(false, true);
                generateAgent(true, false);
                return GameState.AGENT_VS_STOCHASTIC;
            }
        }
        return null;
    }

    public void finishGame(boolean gameWon) {
        new AgentStats("src/main/resources/data/agentstats").processEpisode(gameWon);
        for (AgentRecord agentRecord : agents) {
            agentRecord.getAgent().finalizeQTableUpdate();
        }
    }

    private void generateAgent(boolean isStochastic, boolean duskyAgent) {
        Agent zero = new Agent();
        if (isStochastic) {
            zero.setEpsilon(1.0);
        }
        AITools tools = new AITools(duskyAgent);
        Environment env = new Environment(tools, pMgr);
        agents.add(new AgentRecord(
                zero, env, new AIDecisionHandler(pMgr, tools, env)));
    }

    // VALID
    private enum GameState {
        AGENT_VS_PLAYER,
        STOCHASTIC_VS_PLAYER,
        AGENT_VS_STOCHASTIC,
    }
}