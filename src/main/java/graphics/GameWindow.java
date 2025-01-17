package main.java.graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow {
    private GraphicsHandler graphicsHandler;
    private boolean windowOpen;
    private boolean lightChosen;

    public GameWindow(GraphicsHandler graphicsHandler) {
        this.graphicsHandler = graphicsHandler;

        JFrame frame = new JFrame("Checkers dev");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(graphicsHandler);
        frame.setSize(800,800);
        frame.setLocationRelativeTo(null); // centered
        frame.setVisible(true);
        this.windowOpen = true;
        this.lightChosen = false;

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

    public boolean lightChosen() {
        System.out.println(lightChosen);
        return lightChosen;
    }

    public boolean isOpen() {
        return windowOpen;
    }
}
