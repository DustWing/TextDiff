package org.example;

import org.example.ui.JMainPanel;

import javax.swing.*;
import java.awt.*;

public class Application {


    public static void main(String[] args) {


        //Set OS UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(() -> {
            final JMainPanel jMainPanel = new JMainPanel();
            final JFrame frame = new JFrame();
            frame.add(jMainPanel);
            frame.pack();
            frame.setSize(1080, 920);
            frame.setMinimumSize(new Dimension(640,480));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setTitle("Text Diff Exercise");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });

    }

}
