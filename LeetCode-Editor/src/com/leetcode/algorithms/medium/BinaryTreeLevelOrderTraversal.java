package com.leetcode.algorithms.medium;

/*
Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).

For example:
Given binary tree [3,9,20,null,null,15,7],
    3
   / \
  9  20
    /  \
   15   7
return its level order traversal as:
[
  [3],
  [9,20],
  [15,7]
]
 */

import com.leetcode.algorithms.collections.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class BinaryTreeLevelOrderTraversal {

    public List<List<Integer>> levelOrder(TreeNode root) {
        if (null == root) return Collections.emptyList();

        List<List<Integer>> list = new ArrayList<>();
        list.add(Arrays.asList(root.val));

        List<TreeNode> nodes = Arrays.asList(root);
        while (nodes.size() > 0) {
            List<Integer> integers = new ArrayList<>();

            List<TreeNode> nodes_n = new ArrayList<>();
            nodes.forEach(node -> {
                nodes_n.addAll(helper(node, integers));
            });

            if (nodes_n.size() > 0) list.add(integers);

            nodes = nodes_n;
        }

        return list;
    }

    private List<TreeNode> helper(TreeNode node, List<Integer> list) {
        List<TreeNode> rtnList = new ArrayList<>();

        if (null != node.left) {
            list.add(node.left.val);
            rtnList.add(node.left);
        }
        if (null != node.right) {
            list.add(node.right.val);
            rtnList.add(node.right);
        }

        return rtnList;
    }

    public static void main(String[] args) {
        // [3,9,20,null,null,15,7]

        TreeNode root = new TreeNode(3);
        TreeNode node1 = new TreeNode(9);
        TreeNode node2 = new TreeNode(20);
        TreeNode node3 = new TreeNode(15);
        TreeNode node4 = new TreeNode(7);

        root.left = node1;
        root.right = node2;
        root.right.left = node3;
        root.right.right = node4;

        System.out.println(new BinaryTreeLevelOrderTraversal().levelOrder(root));
    }

}
