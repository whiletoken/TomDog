package com.tomdog.leet.practice.primary;

import com.tomdog.leet.struct.ListNode;

public class ListPart {

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnarn7/">...</a>
     */
    public static void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }

}
