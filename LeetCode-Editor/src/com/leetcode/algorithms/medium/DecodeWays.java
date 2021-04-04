package com.leetcode.algorithms.medium;

/*
A message containing letters from A-Z is being encoded to numbers using the following mapping:

'A' -> 1
'B' -> 2
...
'Z' -> 26
Given a non-empty string containing only digits, determine the total number of ways to decode it.

Example 1:

Input: "12"
Output: 2
Explanation: It could be decoded as "AB" (1 2) or "L" (12).
Example 2:

Input: "226"
Output: 3
Explanation: It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
 */

/**
 * DP, f(i) = f(i-2) + f(i-1)
 */
public class DecodeWays {

    // "102213" = "02213" + "2213"
        // "02213" = 0
        // "2213" = "213" + "13"
            // "213" = "13" + "3"
                // "3" = 1
            // "13" = 2

    public int numDecodings(String s) {
        if (s.length() == 0) return 0;

        int[] f = new int[s.length() + 1];
        f[0] = 1;
        f[1] = s.charAt(0) != '0' ? 1 : 0;
        for (int i = 2; i <= s.length(); i++) {
            if (Integer.parseInt(s.substring(i - 2, i)) >= 10 && Integer.parseInt(s.substring(i - 2, i)) <= 26) {
                f[i] += f[i - 2];
            }
            if (Integer.parseInt(s.substring(i - 1, i)) > 0) {
                f[i] += f[i - 1];
            }
        }

        return f[s.length()];
    }

    public static void main(String[] args) {
        System.out.println(new DecodeWays().numDecodings("255"));
    }

}
