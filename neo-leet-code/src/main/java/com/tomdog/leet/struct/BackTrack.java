package com.tomdog.leet.struct;

import java.util.*;

/**
 * 回溯算法
 */
public class BackTrack {

    public static List<List<Integer>> result = null;

    /**
     * <a href="https://leetcode.cn/problems/subsets/">...</a>
     */
    public static List<List<Integer>> subsets(int[] nums) {
        result = new ArrayList<>();
        result.add(new ArrayList<>());
        if (nums == null) {
            return result;
        }
        subsets(nums, new ArrayList<>(), 0);
        return result;
    }

    public static void subsets(int[] nums, List<Integer> list, int index) {
        for (int i = index; i < nums.length; i++) {
            list.add(nums[i]);
            result.add(list);
            subsets(nums, new ArrayList<>(list), i + 1);
            list = new ArrayList<>(list);
            list.remove(list.size() - 1);
        }
    }

    /**
     * <a href="https://leetcode.cn/problems/subsets-ii/submissions/">...</a>
     */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        // 保存中间结果
        List<Integer> subSet = new ArrayList<>();
        // 保存最终结果
        List<List<Integer>> result = new ArrayList<>();
        // 先排序
        Arrays.sort(nums);
        subsetsWithDup(nums, 0, subSet, result);
        return result;
    }

    private static void subsetsWithDup(int[] nums, int pos, List<Integer> subSet, List<List<Integer>> result) {
        // 把临时结果复制出来保存到最终结果
        result.add(new ArrayList<>(subSet));
        for (int i = pos; i < nums.length; i++) {
            // 排序之后，如果再遇到重复元素，则不选择此元素
            if (i != pos && nums[i] == nums[i - 1]) {
                continue;
            }
            // 选择、处理结果、再撤销选择
            subSet.add(nums[i]);
            subsetsWithDup(nums, i + 1, subSet, result);
            subSet.remove(subSet.size() - 1);
        }
    }

    /**
     * <a href="https://leetcode.cn/problems/permutations/submissions/">...</a>
     */
    public static List<List<Integer>> permute(int[] nums) {
        result = new ArrayList<>();
        if (nums == null) {
            return result;
        }
        permute(nums, new ArrayList<>(), 0);
        return result;
    }

    public static void permute(int[] nums, List<Integer> list, int index) {
        if (index + 1 == nums.length) {
            list.add(nums[index]);
            result.add(list);
            return;
        }
        for (int i = index; i < nums.length; i++) {
            list.add(nums[i]);
            int[] temp = Arrays.copyOfRange(nums, 0, nums.length);
            swap(temp, index, i);
            permute(temp, new ArrayList<>(list), index + 1);
            list = new ArrayList<>(list);
            list.remove(list.size() - 1);
        }
    }

    public static int[] swap(int[] nums, int start, int end) {
        int temp = nums[start];
        nums[start] = nums[end];
        nums[end] = temp;
        return nums;
    }

    /**
     * <a href="https://leetcode.cn/problems/permutations-ii/submissions/">...</a>
     */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        result = new ArrayList<>();
        if (nums == null) {
            return result;
        }
        Arrays.sort(nums);
        permuteUnique(nums, new ArrayList<>(), 0);
        return result;
    }

    public static void permuteUnique(int[] nums, List<Integer> list, int index) {
        if (index + 1 == nums.length) {
            list.add(nums[index]);
            result.add(list);
            return;
        }
        for (int i = index; i < nums.length; i++) {
            if (i != index && nums[i] == nums[i - 1]) {
                continue;
            }
            list.add(nums[i]);
            if (index == i) {
                permuteUnique(nums, new ArrayList<>(list), index + 1);
            } else {
                int[] temp = new int[nums.length];

                // 数组前半分复制
                System.arraycopy(nums, 0, temp, 0, index);

                // 数组中间复制
                System.arraycopy(nums, index, temp, index + 1, i - index);

                // 数组后半部分复制
                System.arraycopy(nums, i + 1, temp, i + 1, nums.length - i - 1);
                temp[index] = nums[i];
                permuteUnique(temp, new ArrayList<>(list), index + 1);
            }
            list = new ArrayList<>(list);
            list.remove(list.size() - 1);
        }
    }

    /**
     * <a href="https://leetcode.cn/problems/combination-sum/submissions/">...</a>
     */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        result = new ArrayList<>();
        Arrays.sort(candidates);
        if (candidates[0] > target) {
            return result;
        }
        combinationSum(candidates, target, new ArrayList<>(), 0);
        return result;
    }

    public static void combinationSum(int[] candidates, int target, List<Integer> list, int index) {
        if (target == 0) {
            result.add(list);
            return;
        }
        if (candidates[index] == target) {
            list.add(candidates[index]);
            result.add(list);
            return;
        }
        if (candidates[index] > target) {
            return;
        }
        for (int i = index; i < candidates.length; i++) {
            if (target < candidates[i]) {
                break;
            }
            list.add(candidates[i]);
            int temp = target - candidates[i];
            combinationSum(candidates, temp, new ArrayList<>(list), i);
            list = new ArrayList<>(list);
            list.remove(list.size() - 1);
        }
    }

    // 记录数字到字母的映射
    private final static Map<Character, String> map = new HashMap<>();

    static {
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");
    }

    public static List<String> list;

    public static List<String> letterCombinations(String digits) {
        list = new ArrayList<>();
        if (digits == null || digits.equals("")) {
            return list;
        }
        letterCombinations(digits, new StringBuilder(), 0);
        return list;
    }

    public static void letterCombinations(String digits, StringBuilder stringBuilder, int index) {
        if (index == digits.length()) {
            list.add(stringBuilder.toString());
            return;
        }
        String target = map.get(digits.charAt(index));
        for (int i = 0; i < target.length(); i++) {
            stringBuilder.append(target.charAt(i));
            letterCombinations(digits, new StringBuilder(stringBuilder), index + 1);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(letterCombinations("2"));
    }

}
