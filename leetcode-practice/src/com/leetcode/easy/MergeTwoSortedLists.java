package com.leetcode.easy;

import com.leetcode.struct.ListNode;

public class MergeTwoSortedLists {

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        ListNode dummyHead = new ListNode(-1);
        ListNode ptr = dummyHead;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                ptr.next = l1;
                l1 = l1.next;
            } else {
                ptr.next = l2;
                l2 = l2.next;
            }
            ptr = ptr.next;
        }

        if (l1 != null) {
            ptr.next = l1;
        }
        if (l2 != null) {
            ptr.next = l2;
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = ListNode.newListNode(new int[]{2, 3, 4});
        ListNode l2 = ListNode.newListNode(new int[]{1, 2, 3});

        System.out.println(new MergeTwoSortedLists().mergeTwoLists(l1, l2).toArrayList().toString());
    }

}
