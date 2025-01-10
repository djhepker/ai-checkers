package com.hepker.ai_checkers;

import com.hepker.ai_checkers.engine.GameEngine;
import com.hepker.ai_checkers.engine.GameLoop;

public class Main {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        GameLoop gameLoop = new GameLoop(game);

        //game.printAllCellsUsage();
        //game.printAllPiecesInPlay();
    }
}
