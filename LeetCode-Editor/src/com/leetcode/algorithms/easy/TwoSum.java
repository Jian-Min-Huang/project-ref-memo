package com.leetcode.algorithms.easy;

/*
Given an array of integers, return indices of the two numbers such that they add up to a specific target.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

Example:

Given nums = [2, 7, 11, 15], target = 9,

Because nums[0] + nums[1] = 2 + 7 = 9,
return [0, 1].
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        int len = nums.length;
        int[] result = new int[2];
        List list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            int v = nums[i];
            int id = list.indexOf(target - v);
            if (id >= 0) {
                result[0] = id;
                result[1] = i;
                return result;
            } else {
                list.add(nums[i]);
            }
        }

        return result;
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(new TwoSum().twoSum3(new int[]{3, 3}, 6)));
    }


    public int[] twoSum2(int[] nums, int target) {
        int[] result = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                result[0] = map.get(target - nums[i]);
                result[1] = i;
            } else {
                map.put(nums[i], i);
            }
        }

        return result;
    }

    public int[] twoSum3(int[] nums, int target) {
        int[] nums_bak = Arrays.copyOfRange(nums, 0, nums.length);

        Arrays.sort(nums_bak);

        int left = 0, right = nums.length - 1;
        while (left < right) {
            if (nums_bak[left] + nums_bak[right] < target) {
                left++;
            } else if (nums_bak[left] + nums_bak[right] > target) {
                right--;
            } else {
                break;
            }
        }

        int idx1 = 0, idx2 = 0;
        boolean idx1set = false;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == nums_bak[left] && !idx1set) {
                idx1 = i;
                idx1set = true;
            }
            if (nums[i] == nums_bak[right]) {
                idx2 = i;
            }
        }

        return (idx1 < idx2) ? new int[]{idx1, idx2} : new int[]{idx2, idx1};
    }

}
