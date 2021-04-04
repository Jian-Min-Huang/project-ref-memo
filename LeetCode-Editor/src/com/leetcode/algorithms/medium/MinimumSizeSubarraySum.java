package com.leetcode.algorithms.medium;

/*
Given an array of n positive integers and a positive integer s, find the minimal length of a contiguous subarray of which the sum ≥ s. If there isn't one, return 0 instead.

Example:

Input: s = 7, nums = [2,3,1,2,4,3]
Output: 2
Explanation: the subarray [4,3] has the minimal length under the problem constraint.
Follow up:
If you have figured out the O(n) solution, try coding another solution of which the time complexity is O(n log n).
 */

/**
 * Array series need two pointers
 */
public class MinimumSizeSubarraySum {

    public int minSubArrayLen(int s, int[] nums) {
        int[] sum = new int[nums.length];
        int minLen = Integer.MAX_VALUE;
        if (nums.length != 0) sum[0] = nums[0];
        for (int i = 1; i < nums.length; i++) sum[i] = sum[i - 1] + nums[i];
        for (int i = 0; i < nums.length; i++) {
            if (sum[i] >= s) {
                minLen = Math.min(minLen, i - binarySearchLastIndexNotBiggerThanTarget(0, i, sum[i] - s, sum));
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    int binarySearchLastIndexNotBiggerThanTarget(int left, int right, int target, int[] sum) {
        while (left <= right) {
            int mid = (left + right) >> 1;
            if (sum[mid] > target) right = mid - 1;
            else left = mid + 1;
        }
        return right;
    }

    public static void main(String[] args) {
        System.out.println(new MinimumSizeSubarraySum().minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}));
    }

//    public int minSubArrayLen2(int s, int[] nums) {
//        int left = 0, right = 0, sum = 0;
//
//
//    }

}
