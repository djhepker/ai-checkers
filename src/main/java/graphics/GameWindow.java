package main.java.graphics;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow {
    private GraphicsHandler graphicsHandler;
    private boolean windowOpen;

    public GameWindow(GraphicsHandler graphicsHandler) {
        this.graphicsHandler = graphicsHandler;

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
        SwingUtilities.invokeLater(() -> showPopUpColorDialog());
    }

    private void showPopUpColorDialog() {
        String[] colors = {"White", "Black"};
        String selectedColor = (String) JOptionPane.showInputDialog(
                graphicsHandler, "Choose color", "Checkers", JOptionPane.QUESTION_MESSAGE,
                null, colors, colors[0]);
        if (selectedColor != null) {
            // potential start logic, could just use window open
        }
    }

    public boolean isOpen() {
        return windowOpen;
    }
}
