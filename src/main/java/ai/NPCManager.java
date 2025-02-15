package main.java.ai;

import main.java.ai.ai.Agent;
import main.java.ai.ai.StochasticNPC;
import main.java.ai.utils.AITools;
import main.java.game.gameworld.PieceManager;

public class NPCManager {
    private final boolean IS_DUSKY;

    private PieceManager pMgr;
    private Agent zero;
    private StochasticNPC kane;

    private GameState gameType;

    public NPCManager(PieceManager pMgr, boolean playerLight, String gameTypeString) {
        this.IS_DUSKY = playerLight;
        this.pMgr = pMgr;
        this.gameType = loadGameState(gameTypeString);
    }

    public void update() {
        if (gameType == GameState.AGENT_VS_PLAYER) {
            zero.update();
        } else if (gameType == GameState.STOCHASTIC_VS_PLAYER) {
            kane.update();
        } else if (gameType == GameState.AGENT_VS_STOCHASTIC) {
            zero.update();
            kane.update();
        }
    }

    public void finishGame() {
        if (gameType != GameState.STOCHASTIC_VS_PLAYER) {
            zero.finalizeQTableUpdate();
        }
    }

    private GameState loadGameState(String gameTypeString) {
        if (gameTypeString.equals("Agent Vs Player")) {
            this.zero = new Agent(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
            System.out.println("Agent Vs Player loaded");
            return GameState.AGENT_VS_PLAYER;
        } else if (gameTypeString.equals("Stochastic Vs Player")) {
            System.out.println("Stochastic Vs Player loaded");
            this.kane = new StochasticNPC(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
            return GameState.STOCHASTIC_VS_PLAYER;
        } else if (gameTypeString.equals("Agent Vs Stochastic")) {
            System.out.println("Agent Vs Stochastic loaded");
            this.zero = new Agent(pMgr, new AITools(IS_DUSKY), IS_DUSKY);
            this.kane = new StochasticNPC(pMgr, new AITools(!IS_DUSKY), !IS_DUSKY);
            return GameState.AGENT_VS_STOCHASTIC;
        }
        return null;
    }

    private enum GameState {
        AGENT_VS_PLAYER,
        STOCHASTIC_VS_PLAYER,
        AGENT_VS_STOCHASTIC,
    }
}
