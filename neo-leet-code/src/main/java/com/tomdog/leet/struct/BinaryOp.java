package com.tomdog.leet.struct;

public class BinaryOp {

    /**
     * <a href="https://leetcode.cn/problems/single-number/">...</a>
     */
    public static int singleNumber(int[] nums) {
        int start = nums[0];
        for (int i = 1; i < nums.length; i++) {
            start = start ^ nums[i];
        }
        return start;
    }

    public static void main(String[] args) {
        int a = 1, b = 2;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println(a);
    }

}
