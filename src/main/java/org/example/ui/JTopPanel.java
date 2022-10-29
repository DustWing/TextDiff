package org.example.ui;

import org.example.diff.DiffChange;
import org.example.diff.DiffUtil;
import org.example.utils.Either;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.example.utils.FileUtils.readFileAsync;

public class JTopPanel extends JPanel {

    private final JTextField mLeftJTextField;
    private final JButton mLeftChooseBtn;
    private final JTextField mRightJTextField;
    private final JButton mRightChooseBtn;
    private final JButton mCompareBtn;

    private final Consumer<List<DiffChange<String>>> mOnCompare;


    final JFileChooser fc = new JFileChooser();


    public JTopPanel(
            Consumer<List<DiffChange<String>>> onCompare
    ) {
        super();
        this.mOnCompare = onCompare;
        this.mLeftJTextField = new JTextField(40);
        this.mRightJTextField = new JTextField(40);
        this.mCompareBtn = new JButton("Compare");
        this.mLeftChooseBtn = new JButton("Choose File");
        this.mRightChooseBtn = new JButton("Choose File");

        mLeftJTextField.setToolTipText("File 1");
        mRightJTextField.setToolTipText("File 2");

        mLeftJTextField.setText("F:/Documents/workspace/JetBrainsExercise/file1.txt");
        mRightJTextField.setText("F:/Documents/workspace/JetBrainsExercise/file2.txt");

        mLeftChooseBtn.addActionListener(
                e -> chooseFileBtnAction(mLeftJTextField)
        );

        mRightChooseBtn.addActionListener(
                e -> chooseFileBtnAction(mRightJTextField)
        );

        mCompareBtn.addActionListener(
                e -> asyncBtnAction()
        );


        JPanel card1 = new JPanel();
        card1.add(mLeftJTextField);
        card1.add(mLeftChooseBtn);

        JPanel card2 = new JPanel();
        card2.add(mRightJTextField);
        card2.add(mRightChooseBtn);

        JPanel card3 = new JPanel();
        card3.add(mCompareBtn);

        JPanel cards = new JPanel();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        final BoxLayout layout = new BoxLayout(cards, BoxLayout.Y_AXIS);
        cards.setLayout(layout);

        add(cards);
    }


    public void chooseFileBtnAction(final JTextField jTextField) {
        //Handle open button action.

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            jTextField.setText(file.getAbsolutePath());
        }
    }

    private void asyncBtnAction() {

        SwingUtilities.invokeLater(
                this::btnAction
        );

    }

    private void btnAction() {

        final String leftText = mLeftJTextField.getText();

        if (leftText == null || leftText.isBlank()) {
            JOptionPane.showMessageDialog(this, "File 1 required");
            return;
        }

        final String rightText = mRightJTextField.getText();

        if (rightText == null || rightText.isBlank()) {
            JOptionPane.showMessageDialog(this, "File 2 required");
            return;
        }


        final CompletableFuture<List<String>> leftFuture = readFileAsync(leftText)
                .thenApply(this::handleEither)
                .exceptionally(this::handleException);

        final CompletableFuture<List<String>> rightFuture = readFileAsync(rightText)
                .thenApply(this::handleEither)
                .exceptionally(this::handleException);


        final List<String> leftList = leftFuture.join();
        final List<String> rightList = rightFuture.join();

        if(leftList.isEmpty()){
            JOptionPane.showMessageDialog(this, "File 1 is empty");
            return;
        }

        if(rightList.isEmpty()){
            JOptionPane.showMessageDialog(this, "File 2 is empty");
            return;
        }

        try {
            final List<DiffChange<String>> diffs = DiffUtil.createDiff(leftList, rightList);
            mOnCompare.accept(diffs);
        }catch (Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not parse file");
        }


    }


    private List<String> handleEither(Either<Throwable, List<String>> listEither) {

        if (listEither.isLeft()) {
            JOptionPane.showMessageDialog(this, listEither.getLeft());
            return List.of();
        }

        return listEither.getRight();
    }

    private List<String> handleException(Throwable throwable) {
        JOptionPane.showMessageDialog(this, throwable);
        return List.of();
    }
}
