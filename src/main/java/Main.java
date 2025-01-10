package main.java;

import main.java.engine.GameEngine;
import main.java.engine.GameLoop;

public class Main {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        GameLoop gameLoop = new GameLoop(game);

        //game.printAllCellsUsage();
        //game.printAllPiecesInPlay();
        //game.printAllCellsInPlay();
    }
}
