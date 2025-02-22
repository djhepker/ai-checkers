package main.java.engine;

public class GameLoop implements Runnable {
    private final GameEngine game;
    // 10^9 / FRAME_TIME = Frames Per Second
    private final long FRAME_TIME = 8_333_333;
    private Thread thread;

    public GameLoop(GameEngine game) {
        this.game = game;
    }

    @Override
    public void run() {
        while (!game.gameOver() && !Thread.currentThread().isInterrupted()) {
            long startTime = System.nanoTime();
            game.updateGame();
            long elapsedTime = System.nanoTime() - startTime;
            if (elapsedTime < FRAME_TIME) {
                long sleepTimeNanos = FRAME_TIME - elapsedTime;
                try {

                    Thread.sleep(sleepTimeNanos / 1_000_000, (int) (sleepTimeNanos % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                while (System.nanoTime() - startTime < FRAME_TIME) {
                    Thread.yield();
                }
            }
        }
        Thread.currentThread().interrupt();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void awaitCompletion() {
        try {
            if (thread != null) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
