package com.tomdog.leet.struct;

public class BinarySearch {

    /**
     * <a href="https://leetcode-cn.com/problems/binary-search/">...</a>
     */
    public static int search(int[] nums, int target) {
        return search(nums, target, 0, nums.length - 1);
    }

    public static int search(int[] nums, int target, int start, int end) {
        if (nums[start] > target || nums[end] < target) {
            return -1;
        }
        int midIndex = (end + start) >> 1;
        int midNum = nums[midIndex];
        if (midNum == target) {
            return midIndex;
        } else if (midNum > target) {
            return search(nums, target, start, midIndex);
        } else {
            return search(nums, target, midIndex + 1, end);
        }
    }

    /**
     * <a href="https://leetcode-cn.com/problems/search-insert-position/">...</a>
     */
    public static int searchInsert(int[] nums, int target) {
        return searchInsert(nums, target, 0, nums.length - 1);
    }

    public static int searchInsert(int[] nums, int target, int start, int end) {
        if (nums[start] > target) {
            return start;
        }
        if (nums[end] < target) {
            return end + 1;
        }
        int midIndex = (end + start) >> 1;
        int midNum = nums[midIndex];
        if (midNum == target) {
            return midIndex;
        } else if (midNum > target) {
            return searchInsert(nums, target, start, midIndex);
        } else {
            return searchInsert(nums, target, midIndex + 1, end);
        }
    }

    /**
     * <a href="https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array-ii/">...</a>
     */
    public static int findMin(int[] nums) {
        if (nums[nums.length - 1] > nums[0]) {
            return nums[0];
        }
        return findMin(nums, 0, nums.length - 1);
    }

    public static int findMin(int[] nums, int start, int end) {
        if (start == end) return nums[start];
        int midIndex = (end + start) >> 1;
        int midNum = nums[midIndex];
        int left, right;
        if (midNum < nums[start]) {
            left = findMin(nums, start, midIndex);
            return left;
        }
        if (midNum > nums[end]) {
            right = findMin(nums, midIndex + 1, end);
            return right;
        }
        left = findMin(nums, start, midIndex);
        right = findMin(nums, midIndex + 1, end);
        return Math.min(left, right);
    }

    /**
     * <a href="https://leetcode.cn/problems/search-in-rotated-sorted-array-ii/submissions/">...</a>
     */
    public static boolean search2(int[] nums, int target) {
        return search2(nums, target, 0, nums.length - 1) != -1;
    }

    public static int search2(int[] nums, int target, int start, int end) {
        if (start == end) {
            if (nums[start] != target) {
                return -1;
            } else {
                return start;
            }
        }
        int midIndex = (end + start) >> 1;
        int midNum = nums[midIndex];
        if (midNum == target) {
            return midIndex;
        }
        int left = -1, right = -1;
        // 左邊數組存在旋轉
        if (midNum < nums[start]) {
            if (midNum < target && target <= nums[end]) {
                left = search2(nums, target, midIndex + 1, end);
            } else {
                right = search2(nums, target, start, midIndex);
            }
        } else if (midNum > nums[end]) {
            // 右邊數組存在旋轉
            if (midNum >= target && target >= nums[0]) {
                left = search2(nums, target, start, midIndex);
            } else {
                right = search2(nums, target, midIndex + 1, end);
            }
        } else {
            left = search2(nums, target, midIndex + 1, end);
            right = search2(nums, target, start, midIndex);
        }
        return left != -1 ? left : right;
    }

    /**
     * <a href="https://leetcode-cn.com/problems/search-in-rotated-sorted-array/">...</a>
     */
    public static int search3(int[] nums, int target) {
        return search3(nums, target, 0, nums.length - 1);
    }

    public static int search3(int[] nums, int target, int start, int end) {
        if (start == end) {
            if (nums[start] != target) {
                return -1;
            } else {
                return start;
            }
        }
        int midIndex = (end + start) >> 1;
        int midNum = nums[midIndex];
        if (midNum == target) {
            return midIndex;
        }
        // 左邊數組存在旋轉
        if (midNum < nums[start]) {
            if (midNum < target && target <= nums[end]) {
                return search3(nums, target, midIndex + 1, end);
            } else {
                return search3(nums, target, start, midIndex);
            }
        } else if (midNum > nums[end]) {
            // 右邊數組存在旋轉
            if (midNum >= target && target >= nums[0]) {
                return search3(nums, target, start, midIndex);
            } else {
                return search3(nums, target, midIndex + 1, end);
            }
        } else {
            // 顺序数组
            if (target > nums[end] || target < nums[start]) {
                return -1;
            }
            if (target > midNum) {
                return search3(nums, target, midIndex + 1, end);
            } else {
                return search3(nums, target, start, midIndex);
            }
        }
    }

    public static void main(String[] args) {
        int[] array = new int[]{1, 0, 1, 1, 1};
        System.out.println(search2(array, 0));
    }

}
