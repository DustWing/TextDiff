package org.example.ui;

import org.example.diff.DiffChange;
import org.example.diff.DiffType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JLeftTextPane<T> extends JTextPane {

    private final List<DiffChange<T>> mValueList;

    public JLeftTextPane(List<DiffChange<T>> valueList) {
        super();


        this.mValueList = valueList;

        super.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        super.setMargin(new Insets(5, 5, 5, 5));

        init();

    }

    private void init() {


        //In the diff analysis there are multiple delete actions for the same line.
        // if we found a delete action the next delete action for that line should be shown as empty
        for (int i = 0; i < mValueList.size(); i++) {


            final String val;
            if (
                    i > 0
                            && mValueList.get(i - 1).leftLine() == mValueList.get(i).leftLine()
            ) {
                val = "";
            } else {
                val = mValueList.get(i).oldValue().toString();
            }

            if (i == mValueList.size() - 1) {
                appendToPane(val);
            } else {
                appendToPane(val + "\n");
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

    public List<DiffChange<T>> getValueList() {
        return mValueList;
    }
}
