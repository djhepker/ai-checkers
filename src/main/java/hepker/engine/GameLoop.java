package hepker.engine;

public final class GameLoop implements Runnable {
    private static final long FRAME_TIME = 8_333_333; // 10^9 / FRAME_TIME = Frames Per Second
    private static final long FRAME_DELAY_CONSTANT = 1_000_000;

    private final GameEngine game;

    private Thread thread;

    public GameLoop(GameEngine inputEngine) {
        this.game = inputEngine;
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
                    Thread.sleep(sleepTimeNanos
                            / FRAME_DELAY_CONSTANT,
                            (int) (sleepTimeNanos % FRAME_DELAY_CONSTANT)
                    );
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
