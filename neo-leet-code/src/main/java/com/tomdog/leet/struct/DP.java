package com.tomdog.leet.struct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 动态规划
 */
public class DP {

    /**
     * <a href="https://leetcode.cn/problems/triangle/">...</a>
     * f(i,j) = min(f(i-1,j-1),f(i-1,j)) + list[i][j]
     */
    public static int minimumTotal(List<List<Integer>> triangle) {
        int size = triangle.size();
        int maxDeep = size - 1;
        int minNum = Integer.MAX_VALUE;
        for (int i = 0; i < triangle.get(maxDeep).size(); i++) {
            minNum = Math.min(minimumTotal(triangle, maxDeep, i, new Integer[size][size]), minNum);
        }
        return minNum;
    }

    public static int minimumTotal(List<List<Integer>> triangle, int i, int j, Integer[][] saves) {
        if (saves[i][j] != null) {
            return saves[i][j];
        }
        if (i == 0) {
            saves[0][0] = triangle.get(0).get(0);
            return saves[0][0];
        }
        int temp = triangle.get(i).get(j), minNum;
        if (j == 0) {
            minNum = minimumTotal(triangle, i - 1, 0, saves) + temp;
            saves[i][j] = minNum;
            return minNum;
        }
        if (j == triangle.get(i).size() - 1) {
            minNum = minimumTotal(triangle, i - 1, j - 1, saves) + temp;
            saves[i][j] = minNum;
            return minNum;
        }
        minNum = Math.min(minimumTotal(triangle, i - 1, j - 1, saves), minimumTotal(triangle, i - 1, j, saves)) + temp;
        saves[i][j] = minNum;
        return minNum;
    }

    /**
     * <a href="https://leetcode.cn/problems/minimum-path-sum/">...</a>
     * f(i,j) = min(f(i-1,j),f(i,j-1)) + list[i][j]
     */
    public static int minPathSum(int[][] grid) {
        int length_i = grid.length - 1;
        int length_j = grid[length_i].length - 1;
        return minPathSum(grid, length_i, length_j, new Integer[length_i + 1][length_j + 1]);
    }

    public static int minPathSum(int[][] grid, int i, int j, Integer[][] saves) {
        if (saves[i][j] != null) {
            return saves[i][j];
        }
        if (i == 0 && j == 0) {
            saves[i][j] = grid[0][0];
            return grid[0][0];
        }
        if (i == 0) {
            saves[i][j] = minPathSum(grid, 0, j - 1, saves) + grid[i][j];
            return saves[i][j];
        }
        if (j == 0) {
            saves[i][j] = minPathSum(grid, i - 1, 0, saves) + grid[i][j];
            return saves[i][j];
        }
        saves[i][j] = Math.min(minPathSum(grid, i - 1, j, saves), minPathSum(grid, i, j - 1, saves)) + grid[i][j];
        return saves[i][j];
    }

    /**
     * <a href="https://leetcode.cn/problems/unique-paths/">...</a>
     * f(i,j) = f(i-1,j) + f(i,j-1)
     */
    public static int uniquePaths(int m, int n) {
        return uniquePaths(m - 1, n - 1, new Integer[m][n]);
    }

    public static int uniquePaths(int i, int j, Integer[][] saves) {
        if (saves[i][j] != null) {
            return saves[i][j];
        }
        if (i == 0) {
            saves[i][j] = 1;
            return saves[i][j];
        }
        if (j == 0) {
            saves[i][j] = 1;
            return saves[i][j];
        }
        saves[i][j] = uniquePaths(i - 1, j, saves) + uniquePaths(i, j - 1, saves);
        return saves[i][j];
    }

    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[m - 1].length;
        return uniquePathsWithObstacles(obstacleGrid, m - 1, n - 1, new Integer[m][n]);
    }

    /**
     * <a href="https://leetcode.cn/problems/unique-paths-ii/submissions/">...</a>
     */
    public static int uniquePathsWithObstacles(int[][] obstacleGrid, int i, int j, Integer[][] saves) {
        if (obstacleGrid[i][j] == 1) {
            return 0;
        }
        if (i == 0 && j == 0) {
            saves[i][j] = 1;
            return saves[i][j];
        }
        if (saves[i][j] != null) {
            return saves[i][j];
        }
        if (i == 0) {
            saves[i][j] = uniquePathsWithObstacles(obstacleGrid, 0, j - 1, saves);
            return saves[i][j];
        }
        if (j == 0) {
            saves[i][j] = uniquePathsWithObstacles(obstacleGrid, i - 1, 0, saves);
            return saves[i][j];
        }
        saves[i][j] = uniquePathsWithObstacles(obstacleGrid, i - 1, j, saves) + uniquePathsWithObstacles(obstacleGrid, i, j - 1, saves);
        return saves[i][j];
    }

    /**
     * <a href="https://leetcode.cn/problems/climbing-stairs/">...</a>
     * f(n) = f(n-1) + f(n-2)
     */
    public static int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        int num1 = 1, num = 2;
        while (n > 2) {
            num = num1 + num;
            num1 = num - num1;
            n--;
        }
        return num;
    }

    /**
     * <a href="https://leetcode.cn/problems/longest-increasing-subsequence/">...</a>
     */
    public static int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            dp[i] = 1;
        }
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j] && dp[i] <= dp[j]) {
                    dp[i] = dp[j] + 1;
                }
            }
        }
        int maxNum = 0;
        for (int n : dp) {
            maxNum = Math.max(maxNum, n);
        }
        return maxNum;
    }

    /**
     * <a href="https://leetcode.cn/problems/word-break/">...</a>
     * abc
     * a
     * ab
     * b
     * abc
     * bc
     * c
     */
    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordDictSet = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordDictSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    /**
     * <a href="https://leetcode.cn/problems/longest-common-subsequence/">...</a>
     */
    public static int longestCommonSubsequence(String text1, String text2) {
        // dp[i][j] a前i个和b前j个字符最长公共子序列
        // dp[m+1][n+1]
        //   ' a d c e
        // ' 0 0 0 0 0
        // a 0 1 1 1 1
        // c 0 1 1 2 2
        int[][] dp = new int[text1.length() + 1][text2.length() + 1];
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                // 相等取左上元素+1，否则取左或上的较大值
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[text1.length()][text2.length()];
    }

    /**
     * <a href="https://leetcode.cn/problems/coin-change/">...</a>
     * 1、5、11
     * f(i) = min(f(i-1),f(i-5),f(i-11)) + 1
     */
    public static int coinChange(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (coins[0] > amount) {
            return -1;
        }
        int[][] dp = new int[amount + 1][2];
        dp[0][0] = 0;
        for (int i = 1; i <= amount; i++) {
            dp[i][0] = Integer.MAX_VALUE;
            for (int coin : coins) {
                if (i - coin >= 0) {
                    if (dp[i - coin][0] == -1) {
                        dp[i][0] = -1;
                        continue;
                    }
                    int temp = dp[i][0];
                    dp[i][0] = Math.min(temp, dp[i - coin][0] + 1);
                    if (dp[i][0] != temp) {
                        dp[i][1] = coin;
                    }
                }
            }
            if (dp[i][0] == Integer.MAX_VALUE) {
                dp[i][0] = -1;
            }
            System.out.printf("i = %s and dp = %s%n", i, dp[i][0]);
        }
        // 输出最小硬币数的组合
        if (dp[amount][0] != -1) {
            List<Integer> list = new ArrayList<>();
            int amountTemp = amount;
            int temp = dp[amount][0];
            int coinTemp = dp[amount][1];
            list.add(coinTemp);
            while (temp > 0) {
                amountTemp = amountTemp - coinTemp;
                coinTemp = dp[amountTemp][1];
                if (coinTemp == 0) {
                    break;
                }
                list.add(coinTemp);
                temp--;
            }
            System.out.println(list);
        }
        return dp[amount][0];
    }

    /**
     * <a href="https://leetcode.cn/problems/coin-change-2/">...</a>
     */
    public static int change(int amount, int[] coins) {
        // 状态 dp[i]表示金额为i时，组合的方法数
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        // 先遍历物品再遍历背包
        for (int n : coins) {
            for (int i = n; i <= amount; i++) {
                dp[i] = dp[i] + dp[i - n];
                System.out.printf("i = %s and dp = %s%n", i, dp[i]);
            }
        }
        return dp[amount];
    }

    /**
     * <a href="https://leetcode.cn/problems/edit-distance/">...</a>
     */
    public static int minDistance(String word1, String word2) {
        // dp[i][j] 表示a字符串的前i个字符编辑为b字符串的前j个字符最少需要多少次操作
        // dp[i][j] = OR(dp[i-1][j-1]，a[i]==b[j],min(dp[i-1][j],dp[i][j-1],dp[i-1][j-1])+1)
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i <= word2.length(); i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
                // 相等则不需要操作
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 否则取删除、插入、替换最小操作次数的值+1
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        return dp[word1.length()][word2.length()];
    }

    /**
     * <a href="https://leetcode.cn/problems/partition-equal-subset-sum/">...</a>
     */
    public static boolean canPartition(int[] nums) {
        return false;
    }

    public static void main(String[] args) {
        System.out.println(change(5, new int[]{1, 2, 5}));
    }

}
