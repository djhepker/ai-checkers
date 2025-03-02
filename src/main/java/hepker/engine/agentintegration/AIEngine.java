package hepker.engine.agentintegration;

import hepker.ai.ai.Agent;
import hepker.ai.utils.AITools;
import hepker.ai.utils.AgentStats;
import hepker.ai.utils.EpisodeStatistics;
import hepker.game.gameworld.PieceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for connecting game to AI
 * */
public final class AIEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(AIEngine.class);

    private final boolean isDusky;

    private List<AgentRecord> agents;
    private PieceManager pMgr;

    private int numTurns;

    private boolean pieceCaptured;

    public AIEngine(PieceManager inputPMgr, boolean playerLight, String gameTypeString) {
        this.isDusky = playerLight;
        this.pMgr = inputPMgr;
        this.agents = new ArrayList<>();
        this.numTurns = 0;
        loadGameState(gameTypeString);
        this.pieceCaptured = false;
    }

    public void update() {
        ++numTurns;
        try {
            for (AgentRecord agentRecord : agents) {
                updateAgent(agentRecord.agent(), agentRecord.environment(), agentRecord.decisionHandler());
            }
        } catch (Exception e) {
            LOGGER.error("Agent Manager Exception", e);
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

    private void loadGameState(String gameTypeString) {
        switch (gameTypeString) {
            case "Agent Vs Player" -> {
                generateAgent(false, isDusky);
            }
            case "Stochastic Vs Player" -> {
                generateAgent(true, isDusky);
            }
            case "Agent Vs Stochastic" -> {
                generateAgent(false, true);
                generateAgent(true, false);
            }
            default -> {
            }
        }
    }

    public void finishGame(boolean gameWon) {
        new AgentStats("src/main/resources/data/agentstats").processEpisode(gameWon);
        new EpisodeStatistics("src/main/resources/data/episode").processEpisode(numTurns);
        for (AgentRecord agentRecord : agents) {
            agentRecord.agent().finalizeQTableUpdate();
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