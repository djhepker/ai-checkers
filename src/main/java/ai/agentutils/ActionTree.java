package main.java.ai.agentutils;

import main.java.game.utils.GameBoardPiece;

public class ActionTree {
    private final GameBoardPiece[][] pieces;
    private final boolean isDusky;

    /*
     * TODO: Determine logic for creating an actionlist
     *  probably need to make the LocationList organized in game updates
     * */
    public ActionTree(GameBoardPiece[][] pieces, boolean isDusky) {
        this.isDusky = isDusky;
        this.pieces = pieces;

        generateTree();
    }

    private void generateTree() {

    }
}
