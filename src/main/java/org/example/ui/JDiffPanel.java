package org.example.ui;

import org.example.ui.listeners.NextActionListener;
import org.example.diff.DiffChange;
import org.example.diff.DiffType;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static javax.swing.ScrollPaneConstants.*;

public class JDiffPanel<T> extends JPanel {
    private final JLeftTextPane<T> mJLeftTestPane;
    private final JRightTextPane<T> mJRightTextPane;
    private final JInfoTextPane<T> mJInfoTextPane;
    private final JButton mNextChangeBtn;
    private int mCurrentLine = 0;
    private int mLastChange = 0;// find the last change in diff

    public JDiffPanel(List<DiffChange<T>> diffs) {
        super();

        List<DiffChange<T>> diffPane = prepareForPanes(diffs);
        this.mJLeftTestPane = new JLeftTextPane<>(diffPane);
        this.mJInfoTextPane = new JInfoTextPane<>(diffPane);
        this.mJRightTextPane = new JRightTextPane<>(diffPane);
        this.mNextChangeBtn = new JButton("Next");
        init();
    }

    private void init() {

        for (int i = 0; i < mJLeftTestPane.getValueList().size(); i++) {
            if (!DiffType.EQUAL.equals(mJLeftTestPane.getValueList().get(i).type())) {
                if (mLastChange < i) {
                    mLastChange = i;
                }
            }
        }

        mNextChangeBtn.addActionListener(
                new NextActionListener(mJLeftTestPane, getNextLine())
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
        infoScrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        infoScrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

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

    public Supplier<Integer> getNextLine() {
        return () -> {

            List<DiffChange<T>> valueList = mJLeftTestPane.getValueList();
            int size = valueList.size();

            int offset = mCurrentLine + 1;

            if (offset >= mLastChange) {
                offset = 0;
            }

            for (int i = offset; i < size; i++) {
                if (!DiffType.EQUAL.equals(valueList.get(i).type())) {

                    mCurrentLine = i;

                    break;
                }

            }
            return mCurrentLine;
        };
    }

}
