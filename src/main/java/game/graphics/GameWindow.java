package main.java.game.graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow {
    private GraphicsHandler graphicsHandler;
    private boolean windowOpen;
    private boolean lightChosen;
    private JFrame frame;

    public GameWindow(GraphicsHandler graphicsHandler) {
        this.graphicsHandler = graphicsHandler;
        this.lightChosen = false;

        this.frame = new JFrame("Checkers dev");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(graphicsHandler);
        this.frame.setSize(800,800);
        this.frame.setLocationRelativeTo(null); // centered
        this.frame.setVisible(true);
        this.windowOpen = true;

        this.frame.addWindowListener(new WindowAdapter() {
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
        String[] playerOptions = {"Agent Vs Stochastic", "Agent Vs Player", "Stochastic Vs Player"};
        String selectedGameMode = (String) JOptionPane.showInputDialog(
                graphicsHandler,
                "Choose who will be playing",
                "Checkers",
                JOptionPane.QUESTION_MESSAGE,
                null, playerOptions, playerOptions[0]);
        if (selectedGameMode == null) {
            System.out.println("Game Mode Dialog closed.");
            frame.dispose();
            System.exit(0);
        }
        return selectedGameMode;
    }

    public void close() {
        frame.dispose();
    }

    public boolean lightChosen() {
        return lightChosen;
    }

    public boolean isOpen() {
        return windowOpen;
    }
}
