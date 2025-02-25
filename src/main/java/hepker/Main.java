package main.java.hepker;

import main.java.hepker.engine.GameEngine;
import main.java.hepker.engine.GameLoop;

public class Main {
    public static void main(String[] args) {
        boolean TRAINING_MODE = true;
        int epochs = 10;
        int episode = 0;
        GameLoop gameLoop;

        do {
            gameLoop = new GameLoop(new GameEngine(TRAINING_MODE));
            gameLoop.start();
            gameLoop.awaitCompletion();
        } while (TRAINING_MODE && episode++ < epochs);

        System.out.println("Loop ended. Game finished");
    }
}
