package hepker;

import hepker.engine.GameEngine;
import hepker.engine.GameLoop;

public class Main {
    public static void main(String[] args) {
        boolean TRAINING_MODE = false;
        int epochs = 0;
        int episodeCount = 0;
        GameLoop gameLoop;

        do {
            gameLoop = new GameLoop(new GameEngine(TRAINING_MODE));
            gameLoop.start();
            gameLoop.awaitCompletion();
        } while (TRAINING_MODE && episodeCount++ < epochs);

        System.out.println("Loop ended. Game finished");
    }
}
