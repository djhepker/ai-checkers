package main.java.ai;

import main.java.game.utils.GameBoardPiece;

import static main.java.game.utils.GameBoardPiece.PieceColor.DUSKY;
import static main.java.game.utils.GameBoardPiece.PieceColor.LIGHT;

class AIActionList {
    private final GameBoardPiece[][] pieces;
    private final boolean isDusky;
    private ActionNode head;

    /*
     * TODO: Determine logic for creating an actionlist
     *  probably need to make the LocationList organized in game updates
     * */
    public AIActionList(GameBoardPiece[][] pieces, boolean isDusky) {
        this.isDusky = isDusky;
        this.pieces = pieces;
        this.head = null;

        generateOrderedList();
    }

    private void generateOrderedList() {
        GameBoardPiece.PieceColor pieceColor;
        if (isDusky) {
            pieceColor = DUSKY;
        } else {
            pieceColor = LIGHT;
        }

        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (pieces[i][j].getColor() == pieceColor) {

                }
            }
        }
    }

    private void addAction(ActionNode node) {

    }
}
