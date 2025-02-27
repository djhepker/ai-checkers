package hepker.engine.agentintegration;

import hepker.ai.ai.Agent;
import hepker.ai.ai.StochasticAgent;
import hepker.ai.utils.AITools;
import hepker.game.gameworld.PieceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for connecting game to AI
 * */
public class AIEngine {

    private static final Logger logger = LoggerFactory.getLogger(AIEngine.class);

    private final boolean IS_DUSKY;

    private final PieceManager pMgr;
    private Agent zero;
    private StochasticAgent kane;

    private GameState gameType;

    public AIEngine(PieceManager pMgr, boolean playerLight, String gameTypeString) {
        this.IS_DUSKY = playerLight;
        this.pMgr = pMgr;
        this.gameType = loadGameState(gameTypeString);
    }

    // TODO: Accommodate line 47
    public void update() {
        try {

            if (gameType == GameState.AGENT_VS_PLAYER) {
                zero.update();

            } else if (gameType == GameState.STOCHASTIC_VS_PLAYER) {
                kane.update();

            } else if (gameType == GameState.AGENT_VS_STOCHASTIC) {
                zero.update();
                kane.update();
            }

        } catch (Exception e) {
            logger.error("Agent Manager Exception", e);
        }
    }

    // TODO: Untangle pMgr and dusky boolean from Agent
    private GameState loadGameState(String gameTypeString) {
        switch (gameTypeString) {
            case "Agent Vs Player" -> {
                this.zero = new Agent(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
                return GameState.AGENT_VS_PLAYER;
            }
            case "Stochastic Vs Player" -> {
                this.kane = new StochasticAgent(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
                return GameState.STOCHASTIC_VS_PLAYER;
            }
            case "Agent Vs Stochastic" -> {
                this.zero = new Agent(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
                this.kane = new StochasticAgent(pMgr, new AITools(!IS_DUSKY), !IS_DUSKY);
                return GameState.AGENT_VS_STOCHASTIC;
            }
        }
        return null;
    }

    // VALID
    public void finishGame(boolean gameWon) {
        if (gameType != GameState.STOCHASTIC_VS_PLAYER) {
            zero.finalizeQTableUpdate(gameWon);
        }
    }

    // VALID
    private enum GameState {
        AGENT_VS_PLAYER,
        STOCHASTIC_VS_PLAYER,
        AGENT_VS_STOCHASTIC,
    }
}