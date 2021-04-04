package com.leetcode.algorithms.easy;

import com.leetcode.algorithms.collections.TreeNode;

/*
Given a binary tree, determine if it is height-balanced.

For this problem, a height-balanced binary tree is defined as:

a binary tree in which the depth of the two subtrees of every node never differ by more than 1.

Example 1:

Given the following tree [3,9,20,null,null,15,7]:

    3
   / \
  9  20
    /  \
   15   7
Return true.

Example 2:

Given the following tree [1,2,2,3,3,null,null,4,4]:

       1
      / \
     2   2
    / \
   3   3
  / \
 4   4
Return false.
 */

/**
 *
 */
public class BalancedBinaryTree {

    public boolean isBalanced(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) return true;

        int dL = findDepth(root.left);
        int dR = findDepth(root.right);

        if (Math.abs(dL - dR) <= 1 && isBalanced(root.left) && isBalanced(root.right)) {
            return true;
        } else {
            return false;
        }
    }


    private int findDepth(TreeNode root) {
        if (null == root) {
            return 0;
        }

        int depthL = 1 + findDepth(root.left);
        int depthR = 1 + findDepth(root.right);

        return Math.max(depthL, depthR);
    }

    public static void main(String[] args) {

    }

}
