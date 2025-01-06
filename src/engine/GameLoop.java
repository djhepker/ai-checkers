package engine;

public class GameLoop implements Runnable {
    private final GameEngine game;
    private final long FRAME_TIME = 16;
    private boolean running;

    public GameLoop (GameEngine game) {
        this.game = game;
    }

    @Override
    public void run() {
        while (running && game.windowExists()) {
            long startTime = System.currentTimeMillis();

            //game.updateGame();
            game.repaintWindow();
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            if (elapsedTime < FRAME_TIME) {
                try {
                    // Adjust frame rate
                    Thread.sleep(FRAME_TIME - elapsedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        running = false;
    }
}
