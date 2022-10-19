package org.example.ui.listeners;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.function.Supplier;

public class NextActionListener implements ActionListener {


    private final JTextPane jTextPane;
    private final Supplier<Integer> lineSupplier;

    public NextActionListener(JTextPane jTextPane, Supplier<Integer> lineSupplier) {
        this.jTextPane = jTextPane;
        this.lineSupplier = lineSupplier;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        // Focus the text area, otherwise the highlighting won't show up
        jTextPane.requestFocusInWindow();

        int totalCharacters = jTextPane.getDocument().getLength();
        int lineCount = 0;
        int offset = 0;
        int rowStart = 0;
        int rowEnd = 0;
        int stopLine = lineSupplier.get();
        try {
            while (offset < totalCharacters) {

                offset = Utilities.getRowEnd(jTextPane, offset);

                if (lineCount == stopLine) {
                    rowStart = Utilities.getRowStart(jTextPane, offset);
                    rowEnd = Utilities.getRowEnd(jTextPane, offset);
                    break;
                }
                lineCount++;
                offset++;
            }
        } catch (BadLocationException ex) {
            throw new RuntimeException("Offset " + offset, ex);
        }

        try {
            final Rectangle2D rectangle2D = jTextPane.modelToView2D(rowStart);
            jTextPane.scrollRectToVisible(rectangle2D.getBounds());
            jTextPane.setCaretPosition(rowStart);
            jTextPane.moveCaretPosition(rowEnd);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }


}
