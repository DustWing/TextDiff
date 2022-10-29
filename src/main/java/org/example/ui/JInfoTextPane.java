package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class JInfoTextPane extends JTextPane {


    private final Style DELETE;
    private final Style INSERT;


    public JInfoTextPane() {

        super.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        super.setMargin(new Insets(5, 5, 5, 5));


        final StyledDocument styledDocument = this.getStyledDocument();
        DELETE = styledDocument.addStyle("DELETE", null);
        StyleConstants.setBackground(DELETE, Color.PINK);

        INSERT = styledDocument.addStyle("INSERT", null);
        StyleConstants.setBackground(INSERT, Color.GREEN);

    }


    public void appendToPane(String msg) {
        appendToPane(msg, null);
    }

    public void appendToPaneInsert(String msg) {
        appendToPane(msg, INSERT);
    }

    public void appendToPaneDel(String msg) {
        appendToPane(msg, DELETE);

    }

    private void appendToPane(String msg, Style style) {
        try {
            StyledDocument styledDocument = this.getStyledDocument();
            styledDocument.insertString(styledDocument.getLength(), msg, style);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }


}
