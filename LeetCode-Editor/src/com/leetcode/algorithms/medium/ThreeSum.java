package com.leetcode.algorithms.medium;

/*
Given an array nums of n integers, are there elements a, b, c in nums such that a + b + c = 0? Find all unique triplets in the array which gives the sum of zero.

Note:

The solution set must not contain duplicate triplets.

Example:

Given array nums = [-1, 0, 1, 2, -1, -4],

A solution set is:
[
  [-1, 0, 1],
  [-1, -1, 2]
]
 */

import com.leetcode.algorithms.easy.TwoSum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * fix one, and split it to 2sum problem
 */
public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        List result = new ArrayList<ArrayList>();
        int[] ints = Arrays.copyOfRange(nums, 1, nums.length - 1);
        int[] ints1 = new TwoSum().twoSum(ints, 0);

//        for (int i = 0; i < nums.length; i++) {
//            new TwoSum().twoSum(nums, 0);
//        }

        result.add(ints1);

        return result;
    }

    public static void main(String[] args) {
        new ThreeSum().threeSum(new int [] {-1, 0, 1, 2, -1, -4});
    }

}
