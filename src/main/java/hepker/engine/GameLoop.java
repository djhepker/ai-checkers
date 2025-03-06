package hepker.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameLoop implements Runnable {
    private static final long FRAME_TIME = 8_333_333; // 10^9 / FRAME_TIME = Frames Per Second
    private static final long FRAME_DELAY_CONSTANT = 1_000_000;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLoop.class);

    private final GameEngine game;

    private Thread thread;

    public GameLoop(GameEngine inputEngine) {
        this.game = inputEngine;
    }

    /*
    * TODO Make graphing calls outside of the loop thread; SwingUtilities cannot
    *   be assigned inside of the delegated game thread because they run asynchronously
    *   MAY need to use executor service
    *   MAY put graphics on its own thread
    * */
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
                    LOGGER.error("Thread interrupted in GameLoop", e);
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
        LOGGER.info("Game loop started");
    }

    public void awaitCompletion() {
        try {
            if (thread != null) {
                thread.join();
                LOGGER.info("Game loop awaiting completion");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
