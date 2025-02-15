package main.java.game;

import main.java.engine.GameEngine;
import main.java.engine.GameLoop;

public class Main {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        GameLoop gameLoop = new GameLoop(game);

        gameLoop.start();
        gameLoop.awaitCompletion();

        System.out.println("Loop ended. Game finished");
    }
}
