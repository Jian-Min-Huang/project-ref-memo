package com.leetcode.algorithms.medium;

/*
Find the kth largest element in an unsorted array. Note that it is the kth largest element in the sorted order, not the kth distinct element.

Example 1:

Input: [3,2,1,5,6,4] and k = 2
Output: 5
Example 2:

Input: [3,2,3,1,2,4,5,5,6] and k = 4
Output: 4
Note:
You may assume k is always valid, 1 ≤ k ≤ array's length.
 */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class KthLargestElementInAnArray {

    // 排序後直接一次計數, 最後再陣列內找到所需的值
//    public int findKthLargest(int[] nums, int k) {
//        int[] kth = new int[nums.length];
//        int idx = 0;
//
//        Arrays.sort(nums);
//
//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i] != kth[idx] && ((i > 0) && (nums[i] == 0))) {
//                kth[idx] = nums[i];
//                idx++;
//            }
//        }
//
//        return kth[kth.length - k];
//    }

    public static void main(String[] args) {
        System.out.println(new KthLargestElementInAnArray().findKthLargest(new int[]{-1, 2, 0}, 1));
    }

    public int findKthLargest(int[] nums, int k) {
        List<Integer> list = new LinkedList<>();
        List<Integer> list2 = new LinkedList<>();
        List<Integer> list3 = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            list.add(nums[i]);
        }

        int pivot = list.get(0);

//        while (true) {
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i) < pivot) {
                    list2.add(list.get(i));
                } else {
                    list3.add(list.get(i));
                }
            }
            if (k > list3.size()) {
                list = list2;

            } else {
                list = list3;
            }
            list2.clear();
            list3.clear();
//        }

        return 0;
    }

}
