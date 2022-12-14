package com.tomdog.leet.practice.intermediate;

import java.util.*;

public class ArrayPart2 {

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvpj16/">...</a>
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        return new LinkedList<>();
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-medium/xvmy42/">...</a>
     */
    public static void setZeroes(int[][] matrix) {
        Set<Integer> line = new HashSet<>();
        Set<Integer> column = new HashSet<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    line.add(i);
                    column.add(j);
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (line.contains(i) || column.contains(j)) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

}
