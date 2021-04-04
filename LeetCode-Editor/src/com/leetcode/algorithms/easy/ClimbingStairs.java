package com.leetcode.algorithms.easy;

/*
You are climbing a stair case. It takes n steps to reach to the top.

Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?

Note: Given n will be a positive integer.

Example 1:

Input: 2
Output: 2
Explanation: There are two ways to climb to the top.
1. 1 step + 1 step
2. 2 steps
Example 2:

Input: 3
Output: 3
Explanation: There are three ways to climb to the top.
1. 1 step + 1 step + 1 step
2. 1 step + 2 steps
3. 2 steps + 1 step
 */

/**
 * DP, f(i) = f(i-2) + f(i-1)
 */
public class ClimbingStairs {

    public int climbStairs(int n) {
        // f(10) = f(9) + f(8)

        // f(1) = 1
        // f(2) = 2

        if (n == 1) return 1;
        if (n == 2) return 2;

        int v1 = 1, v2 = 2, v3 = 0;
        for (int i = 1; i <= n - 2; i++) {
            v3 = v1 + v2;
            v1 = v2;
            v2 = v3;
        }

        return v3;
    }

    public static void main(String[] args) {
        System.out.println(new ClimbingStairs().climbStairs(10));
    }

}
