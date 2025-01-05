
import engine.GameEngine;

public class Main {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();

        while (game.windowExists()) {
            game.repaintWindow();
        }
    }
}
