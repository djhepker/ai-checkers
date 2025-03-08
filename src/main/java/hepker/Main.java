package hepker;

import hepker.ai.ai.Agent;
import hepker.engine.GameEngine;
import hepker.engine.GameLoop;

public class Main {
    public static void main(String[] args) {
        boolean trainingMode = true;
        int epochs = 3;
        int episodeCount = 0;
        GameLoop gameLoop;
        do {
            gameLoop = new GameLoop(new GameEngine(trainingMode));
            gameLoop.start();
            gameLoop.awaitCompletion();
        } while (trainingMode && episodeCount++ < epochs);
        Agent.closeDatabase();
        System.out.println("Loop ended. Game finished");
    }
}