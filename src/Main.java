
import engine.GameEngine;

public class Main {
    public static void main(String[] args) {
        final int TARGET_FPS = 60;
        final long FRAME_TIME = 1000 / TARGET_FPS;

        GameEngine game = new GameEngine();
        //game.printAllCellsUsage();
        //game.printAllPiecesInPlay();

        while (game.windowExists()) {
            long startTime = System.currentTimeMillis();

            game.repaintWindow();
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            if (elapsedTime < FRAME_TIME) {
                try {
                    Thread.sleep(FRAME_TIME - elapsedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
