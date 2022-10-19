package org.example.ui;

import org.example.diff.DiffChange;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class JRightTextPane<T> extends JTextPane {

    private final List<DiffChange<T>> mValueList;

    private final Style INSERT;
    private final Style DEFAULT;


    public JRightTextPane(List<DiffChange<T>> valueList) {
        super();
        this.mValueList = valueList;

        final StyledDocument styledDocument = this.getStyledDocument();

        INSERT = styledDocument.addStyle("INSERT", null);
        StyleConstants.setBackground(INSERT, Color.GREEN);


        DEFAULT = styledDocument.addStyle("UPDATE", null);
        StyleConstants.setForeground(DEFAULT, Color.BLACK);

        init();

    }

    private void init() {

        super.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        super.setMargin(new Insets(5, 5, 5, 5));


        for (int i = 0; i < mValueList.size(); i++) {

            DiffChange<T> tDiffChange = mValueList.get(i);

            String s = mValueList.get(i).newValue() == null ? "" : mValueList.get(i).newValue().toString();

            switch (tDiffChange.type()) {
                case EQUAL -> addNewLine(s, DEFAULT);
                case INSERT -> addNewLine(s, INSERT);
                case DELETE -> addNewLine("", null);
            }

            if (i == mValueList.size() - 1) {
                try {
                    getDocument().remove(getDocument().getLength() - 1, 1);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        setEditable(false);
    }

    private void addNewLine(String msg, Style style) {
        try {
            StyledDocument styledDocument = this.getStyledDocument();
            styledDocument.insertString(styledDocument.getLength(), msg + "\n", style);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

    }

}
