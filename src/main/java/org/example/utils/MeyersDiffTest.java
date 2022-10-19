package org.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MeyersDiffTest {

    private static final String file1 = "";
    private static final String file2 = "";

//    public static void main(String[] args) {
//        final List<String> strings1 = List.of("a", "b", "c");
//        final List<String> strings2 = List.of("a", "b", "d");
////        final List<String> strings1 = readFile(file1);
////        final List<String> strings2 = readFile(file2);
//
//        System.out.println(diff(strings1, strings2, 0, 0));
//
////        System.out.println(1 % 5);
//
//
//    }

    public static <E> List<String> diff(List<E> left, List<E> right, int i, int j) {

        int N = left.size();
        int M = right.size();
        int L = N + M;
        int Z = 2 * Math.min(N, M) + 2;

        if (N > 0 && M > 0) {
            int w = N - M;
            int[] g = new int[Z];
            int[] p = new int[Z];

            final int offset = (L % 2 == 0 ? L : L + 1) / 2;

            for (int h = 0; h < offset; h++) {

                for (int r = 0; r < 2; r++) {

                    int o;
                    int m;
                    int[] c;
                    int[] d;
                    if (r == 0) {
                        o = 1;
                        m = 1;
                        c = g;
                        d = p;
                    } else {
                        o = 0;
                        m = -1;
                        c = p;
                        d = g;
                    }

                    int from = -(h - 2 * Math.max(0, h - M));
                    int to = h - 2 * Math.max(0, h - N) + 1;
                    for (int k = from; k < to; k += 2) {

                        int a;
                        if (k == -h || k != h && c[mod((k - 1), Z)] < c[mod((k + 1), Z)]) {
                            a = c[mod((k + 1), Z)];
                        } else {
                            a = c[mod((k - 1), Z)] + 1;
                        }

                        int b = a - k;
                        int s = a;
                        int t = b;


                        while (
                                a < N && b < M &&
                                        left.get((1 - o) * N + m * a + (o - 1))
                                                .equals(right.get((1 - o) * M + m * b + (o - 1)))
                        ) {
                            ++a;
                            ++b;
                        }

                        int kZMod = mod(k, Z);
                        c[kZMod] = a;
                        int z = -(k - w);
                        int zZMod = mod(z, Z);
                        if (L % 2 == o && z >= -(h - o) && z <= h - o && c[kZMod] + d[zZMod] >= N) {

                            int D;
                            int x;
                            int y;
                            int u;
                            int v;
                            if (o == 1) {
                                D = 2 * h - 1;
                                x = s;
                                y = t;
                                u = a;
                                v = b;
                            } else {
                                D = 2 * h;
                                x = N - a;
                                y = M - b;
                                u = N - s;
                                v = M - t;
                            }

                            if (D > 1 || (x != u && y != v)) {
                                return Stream.concat(
                                        diff(left.subList(0, x), right.subList(0, y), i, j).stream(),
                                        diff(left.subList(u, N), right.subList(v, M), i + u, j + v).stream()
                                ).toList();
                            } else if (M > N) {
                                return diff(List.of(), right.subList(N, M), i + N, j + N);
                            } else if (M < N) {
                                return diff(left.subList(M, N), List.of(), i + M, j + M);
                            } else {
                                return List.of();
                            }

                        }

                    }

                }

            }
        } else if (N > 0) {
            List<String> result = new ArrayList<>(N);
            for (int n = 0; n < N; n++) {
                result.add("delete position:" + (i + n));
            }
            return result;
        } else {
            List<String> result = new ArrayList<>(M);
            for (int n = 0; n < M; n++) {
                result.add("insert position old: %s position_new: %s".formatted(i, j + n));
            }
            return result;
        }

        return List.of();
    }

    private static int mod(int value, int mod) {
        if (value < 0) {
            return mod + value;
        }
        return value % mod;
    }
}
