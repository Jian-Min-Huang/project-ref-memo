package com.leetcode.algorithms.medium;

/*
Given an array of non-negative integers, you are initially positioned at the first index of the array.

Each element in the array represents your maximum jump length at that position.

Determine if you are able to reach the last index.

Example 1:

Input: [2,3,1,1,4]
Output: true
Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
Example 2:

Input: [3,2,1,0,4]
Output: false
Explanation: You will always arrive at index 3 no matter what. Its maximum
             jump length is 0, which makes it impossible to reach the last index.
*/

/**
 * Greedy Algorithm, find max(reach, i + nums[i])
 * DP, f[i] = max(f[i-1], nums[i-1]) - 1 // TODO ?
 */
public class JumpGame {

    public boolean canJump(int[] nums) {
        int reach = 0;
            // satisfy
        for (int i = 0; i < nums.length; i++) {
            // i > reach is for 0 case
            if (i > reach || reach >= nums.length - 1) break;

            reach = Math.max(reach, i + nums[i]);
        }

        return reach >= nums.length - 1;
    }

    public static void main(String[] args) {
        System.out.println(new JumpGame().canJump(new int[]{2,5,0,0}));
    }

}
