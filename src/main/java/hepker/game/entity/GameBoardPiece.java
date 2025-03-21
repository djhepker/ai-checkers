package hepker.game.entity;

import hepker.game.entity.movement.ActionNode;
import hepker.game.gameworld.PieceManager;

import java.awt.image.BufferedImage;
import java.util.stream.Stream;

public interface GameBoardPiece {

    enum PieceColor {
        LIGHT,
        DUSKY
    }

    boolean isReadyForPromotion();

    Stream<ActionNode> getMoveListAsStream();

    boolean isLight();

    void update(PieceManager pMgr);

    void generateLegalMoves(PieceManager pMgr);

    ActionNode getMoveListPointer();

    String getName();

    BufferedImage getSprite();

    void setX(int x);

    void setY(int y);

    int getX();

    int getY();

    short getPieceValue();

    PieceColor getColor();
}