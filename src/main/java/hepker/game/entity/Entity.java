package hepker.game.entity;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
public abstract class Entity {

    private final String name;

    private final GameBoardPiece.PieceColor color;

    private final BufferedImage sprite;

    @Setter
    private int x;

    @Setter
    private int y;

    protected Entity(String inputName, int xCoordinate, int yCoordinate, BufferedImage img, boolean lightPiece) {
        this.name = inputName;
        this.x = xCoordinate;
        this.y = yCoordinate;
        this.sprite = img;
        this.color = lightPiece ? GameBoardPiece.PieceColor.LIGHT : GameBoardPiece.PieceColor.DUSKY;
    }

    /**
     * getter for detecting if this Entity is a light piece. Returns true if so and false if otherwise.
     * When overriding, be sure to still accurately produce this result while adding your own logic, if
     * not calling super.isLight() afterward
     * @return true if this Entity is a light color and false if it is dark
     */
    protected boolean isLight() {
        return color == GameBoardPiece.PieceColor.LIGHT;
    }
}
