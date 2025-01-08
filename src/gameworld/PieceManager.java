package gameworld;

import entity.Entity;
import utils.EntityCreator;
import utils.EntityArray;

public class PieceManager {
    private EntityArray pieces;
    private EntityCreator creator;

    public PieceManager(EntityArray pieces, EntityCreator creator) {
        this.pieces = pieces;
        this.creator = creator;
        createBeginningPieces();
    }

    private void createBeginningPieces() {
        int x = 1;
        int y = 0;
        while (y < 3) {
            if (x > 7) {
                x = 0;
                y++;
                if (y % 2 == 0) {
                    x += 1;
                }
            } else {
                pieces.addEntity(creator.createDarkPiece(x, y));
                x += 2;
            }
        }
        y += 2;
        while (y < 8) {
            if (x > 7) {
                x = 0;
                y++;
                if (y % 2 == 0) {
                    x += 1;
                }
            } else {
                pieces.addEntity(creator.createLightPiece(x, y));
                x += 2;
            }
        }
    }

    public Entity getPiece(int x, int y) {
        return pieces.getEntity(x, y);
    }

    public void printNumPieces() {
        System.out.println("The number of pieces: " + pieces.getNumEntities());
    }

    public void printAllPiecesInPlay() {
        pieces.printEntities();
    }
}
