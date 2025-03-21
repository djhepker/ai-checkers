package hepker;

import hepker.ai.Agent;
import hepker.engine.GameLoop;

public class Main {
    public static void main(String[] args) {
        boolean trainingMode = false;
        int epochs = 10;
        int episodeCount = 0;
        GameLoop gameLoop = new GameLoop(trainingMode);
        do {
            gameLoop.start();
            gameLoop.awaitCompletion();
        } while (trainingMode && episodeCount++ < epochs);
        Agent.closeDatabase();
        System.out.println("Loop ended. Game finished");
    }
}