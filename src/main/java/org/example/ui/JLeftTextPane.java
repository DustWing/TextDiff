package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class JLeftTextPane extends JTextPane {

    private final Style DELETE;

    public JLeftTextPane() {
        super();

        super.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        super.setMargin(new Insets(5, 5, 5, 5));


        final StyledDocument styledDocument = this.getStyledDocument();

        DELETE = styledDocument.addStyle("DELETE", null);
        StyleConstants.setBackground(DELETE, Color.PINK);
    }


    public void appendToPane(String msg) {
        try {
            StyledDocument styledDocument = this.getStyledDocument();
            styledDocument.insertString(styledDocument.getLength(), msg, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void appendToPaneDel(String msg) {
        try {

            if (msg.isBlank()) {
                msg = "\t\n";
            }

            StyledDocument styledDocument = this.getStyledDocument();
            styledDocument.insertString(styledDocument.getLength(), msg, DELETE);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

}
