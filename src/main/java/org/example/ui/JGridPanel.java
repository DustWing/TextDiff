package org.example.ui;

import org.example.diff.DiffChange;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class JGridPanel extends JPanel {


    private JDiffPanel<String> mJDiffPanel;
    private final GridBagConstraints mGridBagConstraints;

    public JGridPanel() {
        super();

        final JTopPanel jTopPanel = new JTopPanel(this::putJDiffPanel);


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        mGridBagConstraints = new GridBagConstraints();
        mGridBagConstraints.fill = GridBagConstraints.BOTH;
        mGridBagConstraints.weightx = 1;
        mGridBagConstraints.weighty = 0.1;
        mGridBagConstraints.gridx = 0;
        add(jTopPanel);
    }

    private void putJDiffPanel(final List<DiffChange<String>> diffChanges) {

        //do nothing
        if(diffChanges.isEmpty()){
            return;
        }

        if (mJDiffPanel != null)
            remove(mJDiffPanel);

        mJDiffPanel = new JDiffPanel<>(diffChanges);

        mGridBagConstraints.weighty = 0.9;
        mGridBagConstraints.gridx = 1;
        add(mJDiffPanel);
        revalidate();
        repaint();
    }


}
