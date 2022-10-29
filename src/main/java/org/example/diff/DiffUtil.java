package org.example.diff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiffUtil {


    public static <T> List<DiffChange<T>> createDiff(List<T> left, List<T> right) {

        if (left.isEmpty() || right.isEmpty()) {
            return List.of();
        }

        int[][] matrix = lcs(left, right);


        List<DiffChange<T>> diffChanges = new ArrayList<>();
        fillChanges(
                diffChanges,
                matrix,
                left,
                right,
                left.size(),
                right.size()

        );
        return diffChanges;
    }

    public static <T> int[][] lcs(List<T> left, List<T> right) {

        final int[][] matrix = new int[left.size() + 1][right.size() + 1];

        for (int i = 0; i <= left.size(); i++) {

            for (int j = 0; j <= right.size(); j++) {

                if (i == 0 || j == 0) {
                    matrix[i][j] = 0;
                } else if (left.get(i - 1).equals(right.get(j - 1))) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(
                            matrix[i - 1][j], matrix[i][j - 1]
                    );
                }
            }
        }

        return matrix;
    }

    private static <T> void fillChanges(
            final List<DiffChange<T>> diffChanges,
            final int[][] matrix,
            final List<T> left,
            final List<T> right,
            int i,
            int j
    ) {

        while (i != 0 || j != 0) {

            if (i == 0) {
                diffChanges.add(
                        new DiffChange<>(i, j, DiffType.INSERT, right.get(j - 1), left.get(i))
                );
                j -= 1;
            } else if (j == 0) {
                diffChanges.add(
                        new DiffChange<>(i, j, DiffType.DELETE, right.get(j), left.get(i - 1))
                );
                i -= 1;
            } else if (left.get(i - 1).equals(right.get(j - 1))) {
                diffChanges.add(
                        new DiffChange<>(i, j, DiffType.EQUAL, right.get(j - 1), left.get(i - 1))
                );
                i -= 1;
                j -= 1;
            } else if (matrix[i - 1][j] <= matrix[i][j - 1]) {

                diffChanges.add(
                        new DiffChange<>(i, j, DiffType.INSERT, right.get(j - 1), left.get(i - 1))
                );
                j -= 1;
            } else {
                diffChanges.add(
                        new DiffChange<>(i, j + 1, DiffType.DELETE, right.get(j), left.get(i - 1))
                );
                i -= 1;
            }
        }

        //reverse list since we start from bottom
        Collections.reverse(diffChanges);

    }

}
