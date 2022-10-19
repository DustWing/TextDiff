package org.example.ui;

import javax.swing.*;

public class JNoWrapPanel extends JPanel {

    private final JTextPane textPane;


    public JNoWrapPanel(JTextPane textPane) {
        super();
        this.textPane = textPane;

        //to force JTextPane to not wrap text
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(textPane);
    }

    public JTextPane getTextPane() {
        return textPane;
    }
}
