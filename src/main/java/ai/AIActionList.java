package main.java.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.utils.GameBoardPiece;

import java.util.ArrayDeque;
import java.util.Deque;

import static main.java.game.utils.GameBoardPiece.PieceColor.DUSKY;
import static main.java.game.utils.GameBoardPiece.PieceColor.LIGHT;

class AIActionList {
    private final GameBoardPiece[][] pieces;
    private final boolean isDusky;
    private final GameBoardPiece.PieceColor pieceColor;
    private Deque<ActionNode> taskStack;
    private ActionNode head;

    /*
     * TODO: Determine logic for creating an actionlist
     *  probably need to make the LocationList organized in game updates
     * */
    public AIActionList(GameBoardPiece[][] pieces, boolean isDusky) {
        this.isDusky = isDusky;
        this.pieces = pieces;
        this.head = null;
        this.taskStack = new ArrayDeque<>();
        this.pieceColor = isDusky ? DUSKY : LIGHT;
        fillTaskStack();
        generateOrderedList();
    }

    private void fillTaskStack() {
        for (GameBoardPiece[] row : pieces) {
            for (GameBoardPiece piece : row) {
                if (piece != null && piece.getColor() == pieceColor) {
                    ActionNode cursor = piece.getMoveListPointer();
                    while (cursor != null) {
                        taskStack.push(cursor);
                        cursor = cursor.getNext();
                    }
                }
            }
        }
    }

    private void generateOrderedList() {

    }

    private void addActionNode(ActionNode inputNode) {
        ActionNode cursor = inputNode;
        if (head == null) {
            head = inputNode;
        } else if (true) {

        }
    }
}
