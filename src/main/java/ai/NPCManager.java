package main.java.ai;

import main.java.ai.utils.AITools;
import main.java.game.gameworld.PieceManager;

public class NPCManager {
    private final boolean isDusky;

    private AITools toolbox;
    private PieceManager pMgr;
    private Agent zero;
    private StochasticNPC kane;

    private GameState gameState;

    public NPCManager(PieceManager pMgr, boolean playerLight, String gameType) {
        this.isDusky = playerLight;
        this.pMgr = pMgr;
        this.toolbox = new AITools(isDusky);
        loadGameState(gameType);
    }

    public void update() {
        if (gameState == GameState.AGENT_VS_PLAYER) {
            zero.update();
        } else if (gameState == GameState.STOCHASTIC_VS_PLAYER) {
            kane.update();
        }
    }

    public void finishGame() {
        zero.finalizeQTableUpdate();
    }

    private void loadGameState(String gameType) {
        if (gameType.equals("Agent Vs Player")) {
            this.gameState = GameState.AGENT_VS_PLAYER;
            this.zero = new Agent(pMgr, toolbox, isDusky);
        } else if (gameType.equals("Stochastic vs Player")) {
            this.gameState = GameState.STOCHASTIC_VS_PLAYER;
            this.kane = new StochasticNPC(pMgr, toolbox, isDusky);
        }
    }

    private enum GameState {
        AGENT_VS_PLAYER,
        STOCHASTIC_VS_PLAYER,
    }
}
