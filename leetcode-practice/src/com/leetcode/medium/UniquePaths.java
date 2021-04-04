package com.leetcode.medium;

public class UniquePaths {

    public int uniquePaths(int m, int n) {
        int[][] value = new int[m][n];
        value[0][0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) continue;

                if (i == 0 || j == 0) {
                    value[i][j] = 1;
                } else {
                    value[i][j] = value[i][j - 1] + value[i - 1][j];
                }
            }
        }

        return value[m - 1][n - 1];
    }

    public static void main(String[] args) {
        System.out.println(new UniquePaths().uniquePaths(3, 2));
    }

}
