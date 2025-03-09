package hepker.engine;

import hepker.engine.agentintegration.AIEngine;
import hepker.utils.EpisodeStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameLoop implements Runnable {
    private static final long FRAME_TIME = 8_333_333; // 10^9 / FRAME_TIME = Frames Per Second
    private static final long FRAME_DELAY_CONSTANT = 1_000_000;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLoop.class);

    private GameEngine game;
    private final boolean isTrainingAgent;

    private Thread thread;

    public GameLoop(boolean isTrainingAgent) {
        EpisodeStatistics.retrieveEpisodeData();
        this.isTrainingAgent = isTrainingAgent;
    }

    @Override
    public void run() {
        game = new GameEngine(isTrainingAgent);
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
        EpisodeStatistics.processEpisode(AIEngine.getNumTurns());
        EpisodeStatistics.updateEpisodeCSV();
        updateGraph();

        Thread.currentThread().interrupt();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
        LOGGER.info("Game loop started");
    }

    public void updateGraph() {
        EpisodeStatistics.displayEpisodeStatistics();
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
