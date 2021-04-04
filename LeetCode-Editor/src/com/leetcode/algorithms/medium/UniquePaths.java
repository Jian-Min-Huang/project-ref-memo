package com.leetcode.algorithms.medium;

/*
A robot is located at the top-left corner of a m x n grid (marked 'Start' in the diagram below).

The robot can only move either down or right at any point in time. The robot is trying to reach the bottom-right corner of the grid (marked 'Finish' in the diagram below).

How many possible unique paths are there?

Note: m and n will be at most 100.

Example 1:

Input: m = 3, n = 2
Output: 3
Explanation:
From the top-left corner, there are a total of 3 ways to reach the bottom-right corner:
1. Right -> Right -> Down
2. Right -> Down -> Right
3. Down -> Right -> Right
Example 2:

Input: m = 7, n = 3
Output: 28

m x n = 3 x 2
f(1,1) = 0	f(2,1) = 1	f(3,1) = 1
f(1,2) = 1	f(2,2) = 2	f(3,2) = 3
*/

/**
 * f(2,1) = f(2,0) + f(1,1)
 */
public class UniquePaths {

    public int uniquePaths(int m, int n) {
        int[][] steps = new int[m][n];

        steps[0][0] = 1;
        if (m > 1) steps[1][0] = 1;
        if (n > 1) steps[0][1] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j > 0) {
                    steps[i][j] = steps[i][j - 1];
                } else if (i > 0 && j == 0) {
                    steps[i][j] = steps[i - 1][j];
                } else if (i > 0 && j > 0) {
                    steps[i][j] = steps[i][j - 1] + steps[i - 1][j];
                }
            }
        }

        return steps[m - 1][n - 1];
    }

    public static void main(String[] args) {
        System.out.println(new UniquePaths().uniquePaths(7, 3));
    }

    public int uniquePathsRec(int m, int n) {
        if (m == 1 && n == 1) return 1;
        if (m == 2 && n == 1) return 1;
        if (m == 1 && n == 2) return 1;

        int result = 0;

        if (m == 1 && n > 1) {
            result += uniquePaths(m, n - 1);
        } else if (m > 1 && n == 1) {
            result += uniquePaths(m - 1, n);
        } else {
            result += uniquePaths(m, n - 1) + uniquePaths(m - 1, n);
        }

        return result;
    }

}
