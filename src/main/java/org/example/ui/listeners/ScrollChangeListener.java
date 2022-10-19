package org.example.ui.listeners;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ScrollChangeListener implements ChangeListener {

    private final JTextPane jTextPane;

    public ScrollChangeListener(JTextPane jTextPane) {
        this.jTextPane = jTextPane;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        final JTextPane textPane = jTextPane;

        if (textPane.getText().length() == 0) {
            //do nothing
            return;
        }

        final JViewport viewport = (JViewport) e.getSource();
        final Rectangle viewRect = viewport.getViewRect();

        Point p = viewRect.getLocation();
        int startIndex = textPane.viewToModel2D(p);

        p.x += viewRect.width;
        p.y += viewRect.height;
        int endIndex = textPane.viewToModel2D(p);

//                    if (endIndex - startIndex > 0) {
//
//                        try {
//                            System.out.println(
//                                    textPane.getText(
//                                            startIndex,
//                                            (endIndex - startIndex)
//                                    )
//                            );
//                        } catch (BadLocationException ex) {
//                            throw new RuntimeException("Scroll error: Start: " + startIndex + ", End: " + endIndex, ex);
//                        }
//
//                    }

    }
}
