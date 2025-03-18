package hepker;

import hepker.ai.Agent;
import hepker.engine.GameLoop;

public class Main {
    private static GameLoop gameLoop;

    public static void main(String[] args) {
        boolean trainingMode = true;
        int epochs = 10000;
        int numSessions = 4;
        gameLoop = new GameLoop(trainingMode);
        do {
            runLoopWithParameters(epochs, trainingMode);
        } while (--numSessions > 0);
        Agent.closeDatabase();
        System.out.println("Loop ended. Game finished");
    }

    private static void runLoopWithParameters(int epochs, boolean trainingMode) {
        int episodeCount = 0;
        do {
            gameLoop.start();
            gameLoop.awaitCompletion();
        } while (trainingMode && episodeCount++ < epochs);
        gameLoop.trainingProgress();
    }
}