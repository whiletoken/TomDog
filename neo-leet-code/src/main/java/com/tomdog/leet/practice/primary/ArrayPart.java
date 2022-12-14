package com.tomdog.leet.practice.primary;

import java.util.*;

public class ArrayPart {

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x2gy9m/">...</a>
     */
    public static int removeDuplicates(int[] nums) {
        int p = 0, q = 1;
        boolean flag = false;
        while (q < nums.length) {
            if (nums[p] == nums[q]) {
                flag = true;
                q++;
            } else if (flag || q > p + 1) {
                nums[p + 1] = nums[q];
                flag = false;
                p++;
                q++;
            } else {
                p++;
                q++;
            }
        }
        return p + 1;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x2zsx1/">...</a>
     */
    public static int maxProfit(int[] prices) {
        int[][] dp = new int[prices.length + 1][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i = 1; i <= prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i - 1]);
            dp[i][1] = Math.max(dp[i - 1][0] - prices[i - 1], dp[i - 1][1]);
        }
        return dp[prices.length][0];
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x2skh7/">...</a>
     */
    public static void rotate(int[] nums, int k) {
        if (k <= 0) {
            return;
        }
        k = k % nums.length;
        int mid = nums.length >> 1;
        for (int i = 0; i < mid; i++) {
            nums[i] = nums[i] ^ nums[nums.length - 1 - i];
            nums[nums.length - 1 - i] = nums[i] ^ nums[nums.length - 1 - i];
            nums[i] = nums[i] ^ nums[nums.length - 1 - i];
        }
        for (int i = 0; i < k >> 1; i++) {
            nums[i] = nums[i] ^ nums[k - 1 - i];
            nums[k - 1 - i] = nums[i] ^ nums[k - 1 - i];
            nums[i] = nums[i] ^ nums[k - 1 - i];
        }
        for (int i = k; i < (nums.length + k) >> 1; i++) {
            nums[i] = nums[i] ^ nums[nums.length - 1 - i + k];
            nums[nums.length - 1 - i + k] = nums[i] ^ nums[nums.length - 1 - i + k];
            nums[i] = nums[i] ^ nums[nums.length - 1 - i + k];
        }
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x248f5/">...</a>
     */
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            } else {
                set.add(num);
            }
        }
        return false;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x2y0c2/">...</a>
     */
    public static int[] intersect(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int p = 0, q = 0;
        List<Integer> list = new LinkedList<>();
        while (p < nums1.length && q < nums2.length) {
            if (nums1[p] == nums2[q]) {
                list.add(nums1[p]);
                p++;
                q++;
            } else if (nums1[p] > nums2[q]) {
                q++;
            } else {
                p++;
            }
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x2cv1c/">...</a>
     */
    public static int[] plusOne(int[] digits) {
        if (digits[digits.length - 1] != 9) {
            digits[digits.length - 1]++;
            return digits;
        }
        int i = digits.length - 1;
        while (i >= 0) {
            if (digits[i] == 9) {
                digits[i] = 0;
            } else {
                digits[i]++;
                break;
            }
            i--;
        }
        if (digits[0] == 0) {
            int[] temp = new int[digits.length + 1];
            temp[0] = 1;
            System.arraycopy(digits, 0, temp, 1, digits.length);
            return temp;
        }
        return digits;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x2ba4i/">...</a>
     */
    public static void moveZeroes(int[] nums) {
        if (nums.length == 1) {
            return;
        }
        int p = 0, q = 1;
        while (q < nums.length) {
            if (nums[p] != 0) {
                p++;
                q++;
            } else if (nums[p] == 0 && nums[q] != 0) {
                nums[p] = nums[p] ^ nums[q];
                nums[q] = nums[p] ^ nums[q];
                nums[p] = nums[p] ^ nums[q];
                p++;
                q++;
            } else {
                q++;
            }
        }
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x2jrse/">...</a>
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] result = new int[2];
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                result[0] = map.get(target - nums[i]);
                result[1] = i;
                return result;
            }
            map.put(nums[i], i);
        }
        return result;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/x2f9gg/">...</a>
     */
    public static boolean isValidSudoku(char[][] board) {
        List<Set<Character>> nineCell = new LinkedList<>();
        List<Set<Character>> lineCell = new LinkedList<>();
        List<Set<Character>> columnCell = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            nineCell.add(new HashSet<>());
            lineCell.add(new HashSet<>());
            columnCell.add(new HashSet<>());
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != '.') {

                    int nineIndex = (i / 3) + (j / 3) * 3;
                    if (nineCell.get(nineIndex).contains(board[i][j])) {
                        return false;
                    }
                    nineCell.get(nineIndex).add(board[i][j]);

                    int lineIndex = j % 9;
                    if (lineCell.get(lineIndex).contains(board[i][j])) {
                        return false;
                    }
                    lineCell.get(lineIndex).add(board[i][j]);

                    int columnIndex = i % 9;
                    if (columnCell.get(columnIndex).contains(board[i][j])) {
                        return false;
                    }
                    columnCell.get(columnIndex).add(board[i][j]);
                }
            }
        }
        return true;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnhhkv/">...</a>
     */
    public static void rotate(int[][] matrix) {
        int length = matrix.length - 1;
        for (int i = length; i >= 0; i--) {
            for (int j = 0; j <= length; j++) {
                int start = length - j;
                int end = length - i;
                if (start < i) {
                    matrix[i][j] = matrix[i][j] ^ matrix[start][end];
                    matrix[start][end] = matrix[i][j] ^ matrix[start][end];
                    matrix[i][j] = matrix[i][j] ^ matrix[start][end];
                }
            }
        }
        int mid = matrix.length >> 1;
        for (int i = 0; i < mid; i++) {
            for (int j = 0; j <= length; j++) {
                matrix[i][j] = matrix[i][j] ^ matrix[length - i][j];
                matrix[length - i][j] = matrix[i][j] ^ matrix[length - i][j];
                matrix[i][j] = matrix[i][j] ^ matrix[length - i][j];
            }
        }
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/array-and-string/yf47s/">...</a>
     */
    public static int pivotIndex(int[] nums) {
        int total = 0, temp = 0;
        for (int num : nums) {
            total += num;
        }
        for (int i = 0; i < nums.length; i++) {
            temp += nums[i];
            if (total == 2 * temp - nums[i]) {
                return i;
            }
        }
        if (temp - nums[0] == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/array-and-string/c5tv3/">...</a>
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals.length == 1) {
            return intervals;
        }
        Map<Integer, Integer> treeMap = new TreeMap<>();
        for (int[] ints : intervals) {
            Integer value = treeMap.get(ints[0]);
            if (value == null || value < ints[1]) {
                treeMap.put(ints[0], ints[1]);
            }
        }
        List<int[]> list = new ArrayList<>();
        int k = 0;
        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
            if (k == 0) {
                int[] temp = new int[]{entry.getKey(), entry.getValue()};
                list.add(temp);
            } else {
                int[] value = list.get(list.size() - 1);
                if (entry.getKey() > value[1]) {
                    int[] temp = new int[]{entry.getKey(), entry.getValue()};
                    list.add(temp);
                } else {
                    int[] temp = new int[]{value[0], Math.max(value[1], entry.getValue())};
                    list.set(list.size() - 1, temp);
                }
            }
            k++;
        }
        return list.toArray(new int[list.size()][2]);
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/array-and-string/cuxq3/">...</a>
     */
//    public static int[] findDiagonalOrder(int[][] mat) {
//        for (int i = 0; i < mat.length; i++) {
//            if (i % 2 == 0) {
//
//            }
//        }
//    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/array-and-string/conm7/">...</a>
     */
    public String longestPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        int start = 0, end = 0;
        boolean[][] flag = new boolean[s.length()][s.length()];
        for (int j = 0; j < s.length(); j++) {
            for (int i = 0; i <= j; i++) {
                boolean b = s.charAt(i) == s.charAt(j);
                if (i == j || (b && flag[i + 1][j - 1]) || (b && i + 1 == j)) {
                    flag[i][j] = true;
                    //判断是否更新
                    if (j - i > end - start) {
                        start = i;
                        end = j;
                    }
                }
            }
        }
        return s.substring(start, end + 1);
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/array-and-string/crmp5/">...</a>
     */
    public static String reverseWords(String s) {
        if (s == null || s.length() == 1) {
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                stringBuilder.append(s.charAt(i));
            } else if ((i + 1) < s.length()) {
                if (s.charAt(i + 1) != ' ') {
                    stringBuilder.append(" ");
                }
            }
        }
        s = stringBuilder.toString();
        if (s.startsWith(" ")) {
            s = s.replaceFirst(" ", "");
        }
        String[] strings = s.split(" ");
        stringBuilder = new StringBuilder();
        for (int i = strings.length - 1; i >= 0; i--) {
            if (i == 0) {
                stringBuilder.append(strings[i]);
            } else {
                stringBuilder.append(strings[i]).append(" ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/array-and-string/c24he/">...</a>
     */
    public static int arrayPairSum(int[] nums) {
        Arrays.sort(nums);
        int pairSum = 0;
        for (int i = 0; i < nums.length; i = i + 2) {
            pairSum += Math.min(nums[i], nums[i + 1]);
        }
        return pairSum;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/array-and-string/cnkjg/">...</a>
     */
    public static int[] twoSum1(int[] numbers, int target) {
        int p = 0, q = numbers.length - 1;
        while (q > p) {
            int sum = numbers[p] + numbers[q];
            if (sum == target) {
                return new int[]{p + 1, q + 1};
            } else if (sum < target) {
                p++;
            } else {
                q--;
            }
        }
        return new int[2];
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/array-and-string/cwuyj/">...</a>
     */
//    public static int removeElement(int[] nums, int val) {
//        int p = 0, q = 1, temp;
//        while (q < nums.length) {
//            if (nums[p] == val && nums[q] != val) {
//                temp = nums[p];
//                nums[p] = nums[q];
//                nums[q] = temp;
//                p++;
//                q++;
//            } else if (nums[p] != val && nums[q] != val) {
//                p++;
//                q++;
//            } else if (nums[p] != val && nums[q] == val) {
//                p++;
//            } else {
//                q++;
//            }
//        }
//    }

    public static void main(String[] args) {
        System.out.println(arrayPairSum(new int[]{1, 4, 3, 2}));
    }

}
