package engine;

public class GameLoop implements Runnable {
    private final GameEngine game;
    private final long FRAME_TIME = 16_666_667;

    public GameLoop(GameEngine game) {
        this.game = game;
        start();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && game.windowExists()) {
            long startTime = System.nanoTime();
            game.updateGame();

            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;

            if (elapsedTime < FRAME_TIME) {
                try {
                    long sleepTime = (FRAME_TIME - elapsedTime) / 1_000_000;
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public void start() {
        new Thread(this).start();
    }
}
