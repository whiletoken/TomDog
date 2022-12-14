package com.tomdog.leet.practice.primary;

import java.util.*;

public class MathPart {

    public static class Solution {
        public int[] nums;

        public Solution(int[] nums) {
            this.nums = nums;
        }

        public int[] reset() {
            return this.nums;
        }

        public int[] shuffle() {
            int[] temp = new int[this.nums.length];
            System.arraycopy(nums, 0, temp, 0, nums.length);
            for (int i = temp.length - 1; i >= 0; i--) {
                int random = new Random().nextInt(nums.length);
                int num = temp[i];
                temp[i] = temp[random];
                temp[random] = num;
            }
            return temp;
        }
    }

    public static class MinStack {

        public static class ListNode {
            public int val;

            public int min;

            public ListNode next;
            public ListNode pre;

            public ListNode(int val) {
                this.val = val;
            }
        }

        public ListNode head;
        public ListNode tail;

        public MinStack() {
            this.head = null;
            this.tail = null;
        }

        public void push(int val) {
            if (head == null) {
                head = new ListNode(val);
                head.min = val;
                tail = head;
            } else {
                ListNode listNode = tail;
                tail.next = new ListNode(val);
                tail.next.min = Math.min(tail.min, val);
                tail = tail.next;
                tail.pre = listNode;
            }
        }

        public void pop() {
            if (tail == head) {
                head = null;
                tail = null;
                return;
            }
            tail = tail.pre;
            tail.next = null;
        }

        public int top() {
            return tail.val;
        }

        public int getMin() {
            return tail.min;
        }
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xn1m0i/">...</a>
     */
    public static int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            count += n & 1;
            n = n >>> 1;
        }
        return count;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnyode/">...</a>
     */
    public static int hammingDistance(int x, int y) {
        int count = 0, n = x ^ y;
        while (n != 0) {
            count += n & 1;
            n >>>= 1;
        }
        return count;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnc5vg/">...</a>
     */
    public static int reverseBits(int n) {
        int count = 0;
        while (n != 0) {
            count <<= 1;
            count += n & 1;
            n >>= 1;
        }
        return count;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xncfnv/">...</a>
     */
    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> list = new LinkedList<>();
        List<Integer> integerList = new LinkedList<>();
        integerList.add(1);
        list.add(integerList);
        if (numRows == 1) {
            return list;
        }
        integerList = new LinkedList<>();
        integerList.add(1);
        integerList.add(1);
        list.add(integerList);
        if (numRows == 2) {
            return list;
        }
        for (int i = 3; i <= numRows; i++) {
            List<Integer> integerList1 = new LinkedList<>();
            integerList1.add(1);
            integerList = list.get(list.size() - 1);
            for (int j = 0; j < integerList.size() - 1; j++) {
                integerList1.add(integerList.get(j) + integerList.get(j + 1));
            }
            integerList1.add(1);
            list.add(integerList1);
        }
        return list;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnj4mt/">...</a>
     */
    public static int missingNumber(int[] nums) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i) {
                return i;
            }
        }
        return nums.length;
    }

    public static void main(String[] args) {
        System.out.println(generate(5));
    }

}
