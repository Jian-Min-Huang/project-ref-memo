package com.leetcode.algorithms.medium;

/*
Given a singly linked list L: L0→L1→…→Ln-1→Ln,
reorder it to: L0→Ln→L1→Ln-1→L2→Ln-2→…

You may not modify the values in the list's nodes, only nodes itself may be changed.

Example 1:

Given 1->2->3->4, reorder it to 1->4->2->3.
Example 2:

Given 1->2->3->4->5, reorder it to 1->5->2->4->3.
 */

import com.leetcode.algorithms.collections.ListNode;

import java.util.Stack;

/**
 * 1. find center point in list and split it
 * 2. reverse second list or store into stack
 * 3. link first and second list alternative
 */
public class ReorderList {

    /*
     * stack 解
     */
    public void reorderList(ListNode head) {
        if (head == null || head.next == null) return;

        ListNode fastNode = head, slowNode = head;
        while (true) {
            if (fastNode.next == null || fastNode.next.next == null) break;

            fastNode = fastNode.next.next;
            slowNode = slowNode.next;
        }

        ListNode center = null;
        if (fastNode.next == null) {
            // even
            center = slowNode.next;
        } else {
            // odd
            center = slowNode;
        }

        Stack<ListNode> stack = new Stack<>();
        ListNode tail = center.next;
        while (tail != null) {
            stack.push(tail);
            tail = tail.next;
        }

        ListNode now = head;
        ListNode tmp = head;

        while (true) {
            tmp = now.next;
            now.next = stack.pop();
            now.next.next = tmp;
            now = tmp;

            if (stack.empty()) break;
        }
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next = new ListNode(5);

        new ReorderList().reorderList2(head);
    }

    public void reorderList2(ListNode head) {
        if (head == null || head.next == null) return;

        ListNode fastNode = head, slowNode = head;
        while (true) {
            if (fastNode.next == null || fastNode.next.next == null) break;

            fastNode = fastNode.next.next;
            slowNode = slowNode.next;
        }

        ListNode center = null;
        if (fastNode.next == null) {
            // even
            center = slowNode.next;
        } else {
            // odd
            center = slowNode;
        }

        // 後半段顛倒
        // 接起來

        System.out.println();
    }

}
