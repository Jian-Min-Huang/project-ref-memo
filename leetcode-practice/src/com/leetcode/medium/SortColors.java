package com.leetcode.medium;

import java.util.Arrays;

public class SortColors {

    public void sortColors(int[] nums) {
        int red = 0, blue = nums.length - 1;

        for (int i = 0; i <= blue; i++) {
            if (nums[i] == 0) {
                int temp = nums[red];
                nums[red++] = nums[i];
                nums[i] = temp;
            } else if (nums[i] == 2) {
                int temp = nums[blue];
                nums[blue--] = nums[i];
                nums[i] = temp;
                i--; // the key point is here, i-- last so it can be process i again
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 0, 2};
        new SortColors().sortColors(nums);

        System.out.println(Arrays.toString(nums));
    }

}
