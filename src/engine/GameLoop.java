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
        while (game.isOpen() && !Thread.currentThread().isInterrupted()) {
            long startTime = System.nanoTime();
            game.updateGame();
            long elapsedTime = System.nanoTime() - startTime;

            if (elapsedTime < FRAME_TIME) {
                long sleepTimeNanos = FRAME_TIME - elapsedTime;

                try {
                    // Sleep for most of the remaining time
                    Thread.sleep(sleepTimeNanos / 1_000_000, (int) (sleepTimeNanos % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                // Yield for any remaining time to fine-tune accuracy
                while (System.nanoTime() - startTime < FRAME_TIME) {
                    Thread.yield();
                }
            }
        }
        Thread.currentThread().interrupt();
    }


    public void start() {
        new Thread(this).start();
    }
}
