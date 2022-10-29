package org.example.ui;

import org.example.diff.DiffChange;
import org.example.diff.DiffType;
import org.example.ui.listeners.NextActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.ScrollPaneConstants.*;

public class JDiffPanel<T> extends JPanel {
    private final JLeftTextPane mJLeftTestPane;
    private final JRightTextPane mJRightTextPane;
    private final JInfoTextPane mJInfoTextPane;
    private final JButton mNextChangeBtn;
    private final JButton mPreviousChangeBtn;
    private final List<DiffChange<T>> mValueList;

    private int mCurrentLine = 0;

    private int mFirstChange = 0;
    private int mLastChange = 0;// find the last change in diff
    private boolean inLeft;

    public JDiffPanel(List<DiffChange<T>> diffs) {
        super();
        mValueList = prepareForPanes(diffs);

        this.mJLeftTestPane = new JLeftTextPane();
        this.mJInfoTextPane = new JInfoTextPane();
        this.mJRightTextPane = new JRightTextPane();
        this.mNextChangeBtn = new JButton("Next");
        this.mPreviousChangeBtn = new JButton("Previous");
        init();
    }

    private void init() {
        updateTextPanes();

        mNextChangeBtn.addActionListener(
                new NextActionListener(this::getNextBlock, this::paneToShowDiff)
        );

        mPreviousChangeBtn.addActionListener(
                new NextActionListener(this::getPreviousBlock, this::paneToShowDiff)
        );

        final JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.add(mNextChangeBtn);

        final JNoWrapPanel leftPanel = new JNoWrapPanel(mJLeftTestPane);
        final JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(16);


        final JNoWrapPanel infoPanel = new JNoWrapPanel(mJInfoTextPane);
        final JScrollPane infoScrollPane = new JScrollPane(infoPanel);
        infoScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        infoScrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
        infoScrollPane.getHorizontalScrollBar().setEnabled(false);
        infoScrollPane.getHorizontalScrollBar().setUnitIncrement(0);//enable false still allows scrolling

        final JNoWrapPanel rightPanel = new JNoWrapPanel(mJRightTextPane);
        final JScrollPane rightScroll = new JScrollPane(rightPanel);
        rightScroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
        rightScroll.getVerticalScrollBar().setUnitIncrement(16);


        leftScrollPane.getVerticalScrollBar().addAdjustmentListener(
                e -> {
                    rightScroll.getVerticalScrollBar().setValue(e.getValue());
                    infoScrollPane.getVerticalScrollBar().setValue(e.getValue());
                }
        );


        rightScroll.getVerticalScrollBar().addAdjustmentListener(
                e -> {
                    leftScrollPane.getVerticalScrollBar().setValue(e.getValue());
                    infoScrollPane.getVerticalScrollBar().setValue(e.getValue());
                }
        );


        final JPanel jPanel1 = new JPanel();
        jPanel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        jPanel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        jPanel1.add(rightScroll, c);
        c.weightx = 0.1;
        jPanel1.add(infoScrollPane, c);
        c.weightx = 1;
        jPanel1.add(leftScrollPane, c);


        this.setBorder(
                new TitledBorder(
                        new EtchedBorder(), "Diff Exercise")
        );
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(mPreviousChangeBtn);
        this.add(mNextChangeBtn);
        this.add(jPanel1);

    }


    /**
     * Every Insert action also is marked as a delete action, so we removed the delete action before an insert
     *
     * @param diffs List of diffs
     * @return DiffChange list by removing all deletes that came before an insert
     */
    private List<DiffChange<T>> prepareForPanes(List<DiffChange<T>> diffs) {
        final List<DiffChange<T>> result = new ArrayList<>();

        diffs.forEach(
                diff -> {
                    if (
                            !result.isEmpty()
                                    && DiffType.isDelete(result.get(result.size() - 1).type())
                                    && DiffType.INSERT.equals(diff.type())

                    ) {
                        result.set(result.size() - 1, diff);
                    } else {
                        result.add(diff);
                    }
                }
        );

        return result;
    }


    private void updateTextPanes() {

        boolean foundFirstChange = false;

        for (int i = 0; i < mValueList.size(); i++) {

            DiffChange<T> tDiffChange = mValueList.get(i);

            //LEFT PANE
            //In the diff analysis there are multiple delete actions for the same line.
            // if we found a delete action the next delete action for that line should be shown as empty
            final String val;
            if (i > 0 && mValueList.get(i - 1).leftLine() == tDiffChange.leftLine()) {
                val = "";
            } else {
                val = tDiffChange.oldValue().toString();
            }

            if (i == mValueList.size() - 1) {
                switch (tDiffChange.type()) {
                    case EQUAL, INSERT -> mJLeftTestPane.appendToPane(val);
                    case DELETE -> mJLeftTestPane.appendToPaneDel(val);
                }
            } else {
                switch (tDiffChange.type()) {
                    case EQUAL, INSERT -> mJLeftTestPane.appendToPane(val + "\n");
                    case DELETE -> mJLeftTestPane.appendToPaneDel(val + "\n");
                }
            }


            //MIDDLE PANE
            int line = i + 1;
            if (i == mValueList.size() - 1) {
                switch (tDiffChange.type()) {
                    case EQUAL -> mJInfoTextPane.appendToPane(String.valueOf(line));
                    case INSERT -> mJInfoTextPane.appendToPaneInsert(line + "+");
                    case DELETE -> mJInfoTextPane.appendToPaneDel(line + "-");
                }

            } else {
                switch (tDiffChange.type()) {
                    case EQUAL -> mJInfoTextPane.appendToPane(line + "\n");
                    case INSERT -> mJInfoTextPane.appendToPaneInsert(line + "+" + "\n");
                    case DELETE -> mJInfoTextPane.appendToPaneDel(line + "-" + "\n");
                }
            }


            //RIGHT PANE
            final String newValue = tDiffChange.newValue().toString();
            if (i == mValueList.size() - 1) {
                switch (tDiffChange.type()) {
                    case EQUAL -> mJRightTextPane.appendToPane(newValue);
                    case INSERT -> mJRightTextPane.appendToPaneNew(newValue);
                    case DELETE -> mJRightTextPane.appendToPane("");
                }
            } else {

                switch (tDiffChange.type()) {
                    case EQUAL -> mJRightTextPane.appendToPane(newValue + "\n");
                    case INSERT -> mJRightTextPane.appendToPaneNew(newValue + "\n");
                    case DELETE -> mJRightTextPane.appendToPane("" + "\n");
                }
            }


            //initialize change position
            if (!DiffType.EQUAL.equals(tDiffChange.type())) {

                if (!foundFirstChange) {
                    mFirstChange = i;
                    foundFirstChange = true;
                }

                if (mLastChange < i) {
                    mLastChange = i;
                }
            }

        }

        mJLeftTestPane.setEditable(false);
        mJInfoTextPane.setEditable(false);
        mJRightTextPane.setEditable(false);


    }

    private int getPreviousBlock() {

        List<DiffChange<T>> valueList = mValueList;
        int size = valueList.size();

        int offset = mCurrentLine - 1;

        if (offset <= mFirstChange) {
            offset = size - 1;
        }

        for (int i = offset; i >= 0; i--) {
            DiffType type = valueList.get(i).type();
            if (!DiffType.EQUAL.equals(type)) {

//                //to jump blocks of changes
//                if (i != 0 && type.equals(valueList.get(i - 1).type())) {
//                    continue;
//                }

                mCurrentLine = i;

                //in which pane to show the change
                if (DiffType.INSERT.equals(type)) {
                    inLeft = false;
                } else if (DiffType.DELETE.equals(type)) {
                    inLeft = true;
                }

                break;
            }

        }

        return mCurrentLine;
    }

    private int getNextBlock() {

        List<DiffChange<T>> valueList = mValueList;
        int size = valueList.size();

        int offset = mCurrentLine + 1;

        if (offset >= mLastChange) {
            offset = 0;
        }

        for (int i = offset; i < size; i++) {

            DiffType type = valueList.get(i).type();

            if (!DiffType.EQUAL.equals(type)) {

//                //to jump blocks of changes
//                if (i != 0 && type.equals(valueList.get(i - 1).type())) {
//                    continue;
//                }

                mCurrentLine = i;

                //in which pane to show the change
                if (DiffType.INSERT.equals(type)) {
                    inLeft = false;
                } else if (DiffType.DELETE.equals(type)) {
                    inLeft = true;
                }
                break;
            }

        }
        return mCurrentLine;
    }

    private JTextPane paneToShowDiff() {

        if (inLeft) {
            return mJLeftTestPane;
        } else {
            return mJRightTextPane;
        }
    }

}
