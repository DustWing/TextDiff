package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class JRightTextPane extends JTextPane {


    private final Style INSERT;


    public JRightTextPane() {
        super();
        super.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        super.setMargin(new Insets(5, 5, 5, 5));


        final StyledDocument styledDocument = this.getStyledDocument();

        INSERT = styledDocument.addStyle("INSERT", null);
        StyleConstants.setBackground(INSERT, Color.GREEN);


    }

    public void appendToPane(String msg) {
        appendToPane(msg, null);
    }

    public void appendToPaneNew(String msg) {
        if (msg.isBlank()) {
            msg = "\t\n";
        }
        appendToPane(msg, INSERT);
    }

    private void appendToPane(String msg, Style style) {
        try {
            StyledDocument styledDocument = this.getStyledDocument();
            styledDocument.insertString(styledDocument.getLength(), msg , style);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

}
