package hepker.engine.agentintegration;

import hepker.ai.ai.Agent;
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

    private final List<AgentRecord> agents;
    private final PieceManager pMgr;

    private int agentTurnSwitch;
    private int numTurns;

    public AIEngine(PieceManager inputPMgr, boolean playerLight, String gameTypeString) {
        this.agentTurnSwitch = 0;
        this.isDusky = playerLight;
        this.pMgr = inputPMgr;
        this.agents = new ArrayList<>();
        this.numTurns = 0;
        loadGameState(gameTypeString);
    }

    public void update() {
        ++numTurns;
        try {
            updateAgent(agents.get(agentTurnSwitch).agent(), agents.get(agentTurnSwitch).decisionHandler());
        } catch (Exception e) {
            LOGGER.error("Agent Manager Exception", e);
        }
    }

    private void updateAgent(Agent inputAgent, AIDecisionHandler inputDecisionHandler) {
        String stateKey = Environment.getEncodedGameState(pMgr);
        inputAgent.setStateKey(stateKey);
        inputDecisionHandler.updateDecisionContainer();
        int numDecisions = inputDecisionHandler.getNumDecisions();
        if (numDecisions == 0) {
            pMgr.flagGameOver();
            return;
        }

        int actionChoiceInt = inputAgent.getActionInt(numDecisions);
        inputAgent.loadCurrentQ(stateKey, actionChoiceInt);
        inputDecisionHandler.setPreDecisionRewardParameters(actionChoiceInt);
        inputDecisionHandler.movePiece(actionChoiceInt);

        inputDecisionHandler.updateDecisionContainer();
        String stateKeyPrime = Environment.getEncodedGameState(pMgr);
        inputAgent.updateRho(inputDecisionHandler.getDecisionReward());

        inputAgent.update(stateKeyPrime, actionChoiceInt);
    }

    private void loadGameState(String gameTypeString) {
        switch (gameTypeString) {
            case "Agent Vs Player" -> generateAgent(false, isDusky);
            case "Stochastic Vs Player" -> generateAgent(true, isDusky);
            case "Agent Vs Stochastic" -> {
                generateAgent(false, true);
                generateAgent(true, false);
            }
            case "Agent vs Agent" -> {
                generateAgent(false, false);
                generateAgent(false, true);
            }
            default -> {
                throw new IllegalArgumentException("Invalid gameTypeString: " + gameTypeString);
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

    public void flipAgentSwitch() {
        agentTurnSwitch ^= 1;
    }

    private void generateAgent(boolean isStochastic, boolean duskyAgent) {
        Agent zero = new Agent();
        if (isStochastic) {
            zero.setEpsilon(1.0);
        }
        agents.add(new AgentRecord(zero, new AIDecisionHandler(pMgr, duskyAgent)));
    }
}