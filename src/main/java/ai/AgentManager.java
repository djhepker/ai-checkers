package main.java.ai;

import main.java.ai.ai.Agent;
import main.java.ai.ai.StochasticAgent;
import main.java.ai.utils.AITools;
import main.java.game.gameworld.PieceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentManager {

    private static final Logger logger = LoggerFactory.getLogger(AgentManager.class);

    private final boolean IS_DUSKY;

    private PieceManager pMgr;
    private Agent zero;
    private StochasticAgent kane;

    private GameState gameType;

    public AgentManager(PieceManager pMgr, boolean playerLight, String gameTypeString) {
        this.IS_DUSKY = playerLight;
        this.pMgr = pMgr;
        this.gameType = loadGameState(gameTypeString);
    }

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

    public void finishGame(boolean gameWon) {
        if (gameType != GameState.STOCHASTIC_VS_PLAYER) {
            zero.finalizeQTableUpdate(gameWon);
        }
    }

    private GameState loadGameState(String gameTypeString) {
        if (gameTypeString.equals("Agent Vs Player")) {
            this.zero = new Agent(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
            return GameState.AGENT_VS_PLAYER;
        } else if (gameTypeString.equals("Stochastic Vs Player")) {
            this.kane = new StochasticAgent(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
            return GameState.STOCHASTIC_VS_PLAYER;
        } else if (gameTypeString.equals("Agent Vs Stochastic")) {
            this.zero = new Agent(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
            this.kane = new StochasticAgent(pMgr, new AITools(!IS_DUSKY), !IS_DUSKY);
            return GameState.AGENT_VS_STOCHASTIC;
        }
        return null;
    }

    public boolean isStochasticVsAgent() {
        return kane != null && zero != null;
    }

    private enum GameState {
        AGENT_VS_PLAYER,
        STOCHASTIC_VS_PLAYER,
        AGENT_VS_STOCHASTIC,
    }
}
