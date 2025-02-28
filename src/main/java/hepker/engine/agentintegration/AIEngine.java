package hepker.engine.agentintegration;

import hepker.ai.ai.Agent;
import hepker.ai.utils.AITools;
import hepker.ai.utils.AgentStats;
import hepker.ai.utils.EpisodeCounter;
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

    private final GameState gameType;

    public AIEngine(PieceManager pMgr, boolean playerLight, String gameTypeString) {
        this.IS_DUSKY = playerLight;
        this.pMgr = pMgr;
        this.agents = new ArrayList<>();
        this.gameType = loadGameState(gameTypeString);
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
        Environment env;
        AIDecisionHandler dH;
        AITools tools;

        switch (gameTypeString) {
            case "Agent Vs Player" -> {
                AITools agentZeroToolBox = new AITools(IS_DUSKY);
                env = new Environment(agentZeroToolBox, pMgr);
                dH = new AIDecisionHandler(pMgr, agentZeroToolBox, env);
                agents.add(new AgentRecord(new Agent(), env, dH));
                return GameState.AGENT_VS_PLAYER;
            }
            case "Stochastic Vs Player" -> {
                Agent stochasticAgent = new Agent();
                stochasticAgent.setEpsilon(1.0);
                tools = new AITools(IS_DUSKY);
                env = new Environment(tools, pMgr);
                dH = new AIDecisionHandler(pMgr, tools, env);
                agents.add(new AgentRecord(stochasticAgent, env, dH));
                return GameState.STOCHASTIC_VS_PLAYER;
            }
            case "Agent Vs Stochastic" -> {
                tools = new AITools(IS_DUSKY);
                env = new Environment(tools, pMgr);
                dH = new AIDecisionHandler(pMgr, tools, env);
                agents.add(new AgentRecord(new Agent(), env, dH));

                Agent stochasticAgent = new Agent();
                stochasticAgent.setEpsilon(1.0);
                tools = new AITools(IS_DUSKY);
                env = new Environment(tools, pMgr);
                dH = new AIDecisionHandler(pMgr, tools, env);
                agents.add(new AgentRecord(stochasticAgent, env, dH));

                return GameState.AGENT_VS_STOCHASTIC;
            }
        }
        return null;
    }

    public void finishGame() {
        for (AgentRecord agentRecord : agents) {
            agentRecord.getAgent().finalizeQTableUpdate();
        }
    }

//    private void updateEpisodes() {
//        final String EPISODE_KEY = "EPISODE_COUNT_FILEPATH";
//        EpisodeCounter episodeCounter = new EpisodeCounter(envLoader.get(EPISODE_KEY));
//        try {
//            episodeCounter.processEpisode();
//            logger.info("Successfully updated episode count");
//        } catch (Exception e) {
//            logger.error("Failed to update episode count", e);
//        }
//    }
//
//    private void updateAgentStats(boolean gameWon) {
//        final String STATS_KEY = "AGENT_STATS_FILEPATH";
//        AgentStats agentStatsHandler = new AgentStats(envLoader.get(STATS_KEY));
//        try {
//            agentStatsHandler.processEpisode(gameWon);
//            logger.info("Successfully updated agent stats, gameWon: {}", gameWon);
//        } catch (Exception e) { // Assuming processEpisode might throw a generic Exception
//            logger.error("Failed to update agent stats, gameWon: {}", gameWon, e);
//        }
//    }

    // VALID
    private enum GameState {
        AGENT_VS_PLAYER,
        STOCHASTIC_VS_PLAYER,
        AGENT_VS_STOCHASTIC,
    }
}