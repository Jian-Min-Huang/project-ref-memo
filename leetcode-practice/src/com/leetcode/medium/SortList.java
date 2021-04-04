package com.leetcode.medium;

import com.leetcode.easy.MergeTwoSortedLists;
import com.leetcode.struct.ListNode;

public class SortList {

    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode slow = head, fast = head, firstHalf = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode secondHalf = slow.next;
        slow.next = null;

        ListNode leftList = null, rightList = null;
        if (firstHalf != secondHalf) {
            leftList = sortList(firstHalf);
            rightList = sortList(secondHalf);
        }

        return new MergeTwoSortedLists().mergeTwoLists(leftList, rightList);
    }

    public static void main(String[] args) {
        ListNode l1 = ListNode.newListNode(new int[]{2, 3, 5, 2, 3, 4});

        System.out.println(new SortList().sortList(l1).toArrayList().toString());
    }

}
