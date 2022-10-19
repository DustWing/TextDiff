package org.example;

import org.example.ui.JGridPanel;

import javax.swing.*;

public class Application {


    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> {
            final JGridPanel jGridPanel = new JGridPanel();
            final JFrame frame = new JFrame();
            frame.add(jGridPanel);
            frame.pack();
            frame.setSize(1080, 920);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });

    }

}
