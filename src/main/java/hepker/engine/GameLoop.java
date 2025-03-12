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
    private final boolean trainingMode;

    private Thread thread;

    public GameLoop(boolean isTrainingAgent) {
        EpisodeStatistics.retrieveEpisodeData();
        this.trainingMode = isTrainingAgent;
    }

    @Override
    public void run() {
        game = new GameEngine(trainingMode);
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
        //handleTurnCountVisualsAndData();
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
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOGGER.info("Game loop finished");
    }

    private void handleTurnCountVisualsAndData() {
        EpisodeStatistics.processEpisode(AIEngine.getNumTurns());
        AIEngine.setNumTurns(0);
        EpisodeStatistics.updateEpisodeCSV();
        updateGraph();
    }

    private void updateGraph() {
        EpisodeStatistics.displayEpisodeStatistics();
    }
}
