package com.tomdog.leet.practice.primary;

import com.tomdog.leet.struct.TreeNode;

import java.util.*;

public class TreePart {

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xn7ihv/">...</a>
     */
    public static boolean isSymmetric(TreeNode root) {
        if (root == null) return false;
        List<Integer> temp = new ArrayList<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        int pre = 1, mark = 0;
        while (!queue.isEmpty()) {
            pre--;
            TreeNode node = queue.poll();
            temp.add(node.val);
            if (node.val != 101) {
                mark++;
                queue.add(node.left == null ? new TreeNode(101) : node.left);

                mark++;
                queue.add(node.right == null ? new TreeNode(101) : node.right);
            }
            if (pre == 0) {
                pre = mark;
                mark = 0;
                int mid = temp.size() >> 1;
                for (int i = 0; i < mid; i++) {
                    if (!temp.get(i).equals(temp.get(temp.size() - 1 - i))) {
                        return false;
                    }
                }
                temp = new ArrayList<>();
            }
        }
        return true;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xninbt/">...</a>
     */
    public static TreeNode sortedArrayToBST(int[] nums) {
        return sortedArrayToBST(nums, 0, nums.length - 1);
    }

    public static TreeNode sortedArrayToBST(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }
        if (start == end) {
            return new TreeNode(nums[start]);
        }
        if (end - start == 1) {
            TreeNode node1 = new TreeNode(nums[end]);
            node1.left = new TreeNode(nums[start]);
            return node1;
        }
        int mid = (end + 1 + start) >> 1;
        TreeNode treeNode = new TreeNode(nums[mid]);
        treeNode.left = sortedArrayToBST(nums, start, mid - 1);
        treeNode.right = sortedArrayToBST(nums, mid + 1, end);
        return treeNode;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnumcr/">...</a>
     */
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int p = 0, q = 0, i = 0;
        int[] temp = new int[m + n];
        while (p < m && q < n) {
            if (nums1[p] == nums2[q]) {
                temp[i] = nums1[p];
                q++;
            } else if (nums1[p] < nums2[q]) {
                temp[i] = nums1[p];
                p++;
            } else {
                temp[i] = nums2[q];
                q++;
            }
            i++;
        }
        if (p != m || q != n) {
            if (p == m) {
                System.arraycopy(nums2, q, temp, p + q, n - q);
            } else {
                System.arraycopy(nums1, p, temp, p + q, m - p);
            }
        }
        System.arraycopy(temp, 0, nums1, 0, m + n);
    }

    public static void main(String[] args) {
        merge(new int[]{2, 0}, 1, new int[]{1}, 1);
    }

}
