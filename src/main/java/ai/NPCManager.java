package main.java.ai;

import main.java.ai.ai.Agent;
import main.java.ai.ai.StochasticNPC;
import main.java.ai.utils.AITools;
import main.java.game.gameworld.PieceManager;

public class NPCManager {
    private final boolean IS_DUSKY;
    private boolean duskyTurn;

    private AITools toolbox;
    private PieceManager pMgr;
    private Agent zero;
    private StochasticNPC kane;

    private GameState gameType;

    public NPCManager(PieceManager pMgr, boolean playerLight, String gameType) {
        this.IS_DUSKY = playerLight;
        this.pMgr = pMgr;
        this.toolbox = new AITools(IS_DUSKY);
        loadGameState(gameType);
    }

    /*
    * TODO: Add in a timing buffer to slow the game down
    *  Figure out why kane is not updating
    * */
    public void update() {
        if (gameType == GameState.AGENT_VS_PLAYER) {
            zero.update();
        } else if (gameType == GameState.STOCHASTIC_VS_PLAYER) {
            kane.update();
        } else if (gameType == GameState.AGENT_VS_STOCHASTIC) {
            if (duskyTurn) {
                zero.update();
            } else {
                kane.update();
            }
            duskyTurn = !duskyTurn;
        }
    }

    public void finishGame() {
        zero.finalizeQTableUpdate();
    }

    private void loadGameState(String gameType) {
        if (gameType.equals("Agent Vs Player")) {
            this.gameType = GameState.AGENT_VS_PLAYER;
            this.zero = new Agent(pMgr, toolbox, IS_DUSKY);
        } else if (gameType.equals("Stochastic vs Player")) {
            this.gameType = GameState.STOCHASTIC_VS_PLAYER;
            this.kane = new StochasticNPC(pMgr, toolbox, IS_DUSKY);
        } else if (gameType.equals("Agent Vs Stochastic")) {
            this.gameType = GameState.AGENT_VS_STOCHASTIC;
            this.zero = new Agent(pMgr, toolbox, IS_DUSKY);
            this.kane = new StochasticNPC(pMgr, toolbox, !IS_DUSKY);
            duskyTurn = false;
        }
    }

    private enum GameState {
        AGENT_VS_PLAYER,
        STOCHASTIC_VS_PLAYER,
        AGENT_VS_STOCHASTIC,
    }
}
