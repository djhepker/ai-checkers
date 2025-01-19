package main.java.ai;

import main.java.game.entity.movement.ActionNode;
import main.java.game.utils.GameBoardPiece;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import static main.java.game.utils.GameBoardPiece.PieceColor.DUSKY;
import static main.java.game.utils.GameBoardPiece.PieceColor.LIGHT;

class AgentActionQueue {
    private final GameBoardPiece[][] pieces;
    private final GameBoardPiece.PieceColor pieceColor;
    private PriorityQueue<ActionNode> actionQueue;

    public AgentActionQueue(GameBoardPiece[][] pieces, boolean isDusky) {
        this.pieces = pieces;
        this.pieceColor = isDusky ? DUSKY : LIGHT;
        this.actionQueue = getQueueOfActions();
    }

    public void printQueue() {
        ActionNode[] printable = getQueueOfActions().toArray(new ActionNode[0]);
        for (ActionNode node : printable) {
            node.printData();
        }
    }

    private PriorityQueue<ActionNode> getQueueOfActions() {
        return Arrays.stream(pieces)
                .flatMap(Arrays::stream)
                .filter(piece -> piece != null && piece.getColor() == pieceColor)
                .flatMap(GameBoardPiece::getMoveListAsStream)
                .collect(Collectors.toCollection(() -> new PriorityQueue<>(
                        (a, b) -> Integer.compare(b.getReward(), a.getReward()))));
    }
}
