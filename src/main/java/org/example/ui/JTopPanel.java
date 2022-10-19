package org.example.ui;

import org.example.diff.DiffChange;
import org.example.diff.DiffUtil;

import javax.swing.*;
import java.util.List;
import java.util.function.Consumer;

import static org.example.utils.FileUtils.readFile;

public class JTopPanel extends JPanel {

    private final JTextField mLeftJTextField;
    private final JTextField mRightJTextField;
    private final JButton mCompareBtn;

    private final Consumer<List<DiffChange<String>>> mOnCompare;


    public JTopPanel(Consumer<List<DiffChange<String>>> onCompare) {
        super();
        this.mOnCompare = onCompare;
        this.mLeftJTextField = new JTextField(40);
        this.mRightJTextField = new JTextField(40);
        this.mCompareBtn = new JButton("Compare");

        mLeftJTextField.setToolTipText("File 1");
        mRightJTextField.setToolTipText("File 2");

        mLeftJTextField.setText("F:/Documents/workspace/JetBrainsExercise/file1.txt");
        mRightJTextField.setText("F:/Documents/workspace/JetBrainsExercise/file2.txt");


        add(mLeftJTextField);
        add(mRightJTextField);
        add(mCompareBtn);

        mCompareBtn.addActionListener(
                e -> btnAction()
        );
    }

    private void btnAction() {
        final String leftText = mLeftJTextField.getText();

        if (leftText == null || leftText.isBlank()) {
            JOptionPane.showMessageDialog(this, "leftText required");
            return;
        }

        final String rightText = mRightJTextField.getText();

        if (rightText == null || rightText.isBlank()) {
            JOptionPane.showMessageDialog(this, "rightText required");
            return;
        }

        final List<String> leftList = readFile(leftText)
                .apply(
                        exception -> {
                            JOptionPane.showMessageDialog(this, exception);
                            return List.of();
                        },
                        strings -> strings
                );

        final List<String> rightList = readFile(rightText)
                .apply(
                        exception -> {
                            JOptionPane.showMessageDialog(this, exception);
                            return List.of();
                        },
                        strings -> strings
                );


        final List<DiffChange<String>> diffs = DiffUtil.createDiff(leftList, rightList);
        mOnCompare.accept(diffs);
    }
}
