package com.leetcode.struct;

import java.util.ArrayList;
import java.util.List;

public class ListNode {

    public int val;
    public ListNode next;

    public ListNode(int x) {
        val = x;
    }

    public static ListNode newListNode(int... list) {
        if (list.length == 0) throw new RuntimeException("Length Must Greater Than Zero");

        ListNode dummyNode = new ListNode(-1);
        ListNode ptr = dummyNode;
        for (int i = 0; i < list.length; i++) {
            ListNode listNode = new ListNode(list[i]);
            ptr.next = listNode;
            ptr = ptr.next;
        }
        return dummyNode.next;
    }

    public List<Integer> toArrayList() {
        ListNode ptr = this;
        List<Integer> list = new ArrayList<>();
        while (ptr != null) {
            list.add(ptr.val);
            ptr = ptr.next;
        }

        return list;
    }

}
