package gameworld;

import utils.EntityCreator;
import utils.EntityList;

public class PieceManager {
    private EntityList pieces;
    private EntityCreator creator;

    public PieceManager(EntityList pieces, EntityCreator creator) {
        this.pieces = pieces;
        this.creator = creator;
    }

    private void createBeginningPieces() {
        // starting from top left
        // dark starts on 2nd index, so x = 1
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
}
