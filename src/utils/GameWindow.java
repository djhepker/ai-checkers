package utils;

import gameworld.BoardRenderer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow {
    private BoardRenderer boardRenderer;
    private boolean windowOpen;

    public GameWindow(BoardRenderer boardRenderer) {
        this.boardRenderer = boardRenderer;

        JFrame frame = new JFrame("Checkers dev");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(boardRenderer);
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

    public boolean isOpen() {
        return windowOpen;
    }
}
