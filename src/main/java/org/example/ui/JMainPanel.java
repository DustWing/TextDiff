package org.example.ui;

import org.example.diff.DiffChange;

import javax.swing.*;
import java.util.List;

public class JMainPanel extends JPanel {


    private JDiffPanel<String> mJDiffPanel;

    public JMainPanel() {
        super();

        final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        final JTopPanel jTopPanel = new JTopPanel(this::putJDiffPanel);

        add(jTopPanel);
    }

    private void putJDiffPanel(final List<DiffChange<String>> diffChanges) {

        //do nothing
        if (diffChanges.isEmpty()) {
            return;
        }

        if (mJDiffPanel != null)
            remove(mJDiffPanel);

        mJDiffPanel = new JDiffPanel<>(diffChanges);
        add(mJDiffPanel);
        revalidate();
        repaint();
    }


}
