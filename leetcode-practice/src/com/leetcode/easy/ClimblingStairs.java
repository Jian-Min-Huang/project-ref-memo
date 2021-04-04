package com.leetcode.easy;

public class ClimblingStairs {

    public int climbStairs(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;

        int fn = 0, fn_1 = 2, fn_2=1;
        for (int i = 3; i <= n; i++) {
            fn = fn_2 + fn_1;

            fn_2 = fn_1;
            fn_1 = fn;
        }

        return fn;
    }

    public static void main(String[] args) {
        System.out.println(new ClimblingStairs().climbStairs(3));
    }

}
