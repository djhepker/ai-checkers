package main.java.game;

import main.java.engine.GameEngine;
import main.java.engine.GameLoop;

public class Main {
    public static void main(String[] args) {
        boolean TRAINING_MODE = true;
        int maxEpisodes = 200;
        int episode = 0;
        GameLoop gameLoop;

        do {
            gameLoop = new GameLoop(new GameEngine(TRAINING_MODE));
            gameLoop.start();
            gameLoop.awaitCompletion();
        } while (TRAINING_MODE && episode++ < maxEpisodes);

        System.out.println("Loop ended. Game finished");
    }
}
