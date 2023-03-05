package com.tomdog.leet.construct.backtrack;

import java.util.LinkedList;

/**
 * 回溯算法框架
 * 回溯算法就是个N叉树的前后序遍历问题，没有例外
 */
public class BackTrackStruct {


    /* 提取出 N 叉树遍历框架 */
    void backtrack(int[] nums, LinkedList<Integer> track) {
        for (int i = 0; i < nums.length; i++) {
            backtrack(nums, track);
        }
    }

}
