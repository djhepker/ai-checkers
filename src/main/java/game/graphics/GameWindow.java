package main.java.game.graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow {
    private GraphicsHandler graphicsHandler;
    private boolean windowOpen;
    private boolean lightChosen;

    public GameWindow(GraphicsHandler graphicsHandler) {
        this.graphicsHandler = graphicsHandler;
        this.lightChosen = false;

        JFrame frame = new JFrame("Checkers dev");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(graphicsHandler);
        frame.setSize(800,800);
        frame.setLocationRelativeTo(null); // centered
        frame.setVisible(true);
        this.windowOpen = true;

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                windowOpen = false;
            }
        });
    }

    public void showPopUpColorDialog() {
        try {
            String[] colors = {"White", "Black"};
            String selectedColor = (String) JOptionPane.showInputDialog(
                    graphicsHandler, "Choose your battle color", "Checkers", JOptionPane.QUESTION_MESSAGE,
                    null, colors, colors[0]);
            lightChosen = selectedColor.equals("White");
        } catch (Exception e) {
            System.out.println("Color dialog was closed");
            System.exit(0);
        }
    }

    public String showGameModeDialog() {
        try {
            String[] playerOptions = {"Agent Vs Stochastic", "Agent Vs Player", "Stochastic Vs Player"};
            String selectedGameMode = (String) JOptionPane.showInputDialog(
                    graphicsHandler,
                    "Choose who will be playing",
                    "Checkers",
                    JOptionPane.QUESTION_MESSAGE,
                    null, playerOptions, playerOptions[0]);
            return selectedGameMode;
        } catch (Exception e) {
            System.out.println("Game Mode Dialog closed.");
            System.exit(0);
        }
        return null;
    }

    public boolean lightChosen() {
        return lightChosen;
    }

    public boolean isOpen() {
        return windowOpen;
    }
}
