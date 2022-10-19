package org.example.ui;

import org.example.diff.DiffChange;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;

public class JInfoTextPane<T> extends JTextPane {
    private final List<DiffChange<T>> mValueList;

    public JInfoTextPane(List<DiffChange<T>> mValueList) {
        this.mValueList = mValueList;
        init();
    }

    private void init() {

        super.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        super.setMargin(new Insets(5, 5, 5, 5));

        for (int i = 0; i < mValueList.size(); i++) {

            DiffChange<T> tDiffChange = mValueList.get(i);
            int line = i+1;
            if (i == mValueList.size() - 1) {

                switch (tDiffChange.type()) {
                    case EQUAL -> appendToPane(String.valueOf(line));
                    case INSERT -> appendToPane(line + "+");
                    case DELETE -> appendToPane(line + "-");
                }

            } else {

                switch (tDiffChange.type()) {
                    case EQUAL -> appendToPane(line + "\n");
                    case INSERT -> appendToPane(line + "+" + "\n");
                    case DELETE -> appendToPane(line + "-" + "\n");
                }
            }
        }

        setEditable(false);
    }

    private void appendToPane(String msg) {
        try {
            StyledDocument styledDocument = this.getStyledDocument();
            styledDocument.insertString(styledDocument.getLength(), msg, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }


}
