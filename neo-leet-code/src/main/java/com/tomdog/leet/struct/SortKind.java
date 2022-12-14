package com.tomdog.leet.struct;

public class SortKind {

    public void quickSort(int[] nums) {
        // 思路：把一个数组分为左右两段，左段小于右段
        quickSort(nums, 0, nums.length - 1);
    }

    // 原地交换，所以传入交换索引
    private void quickSort(int[] nums, int start, int end) {
        if (start < end) {
            // 分治法：divide
            int pivot = partition(nums, start, end);
            quickSort(nums, 0, pivot - 1);
            quickSort(nums, pivot + 1, end);
        }
    }

    // 分区
    private int partition(int[] nums, int start, int end) {
        // 选取最后一个元素作为基准pivot
        int p = nums[end];
        int i = start;
        // 最后一个值就是基准所以不用比较
        for (int j = start; j < end; j++) {
            if (nums[j] < p) {
                swap(nums, i, j);
                i++;
            }
        }
        // 把基准值换到中间
        swap(nums, i, end);
        return i;
    }

    // 交换两个元素
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }


    public void mergeSort(int[] nums) {
        mergeSort(nums, 0, nums.length);
    }

    private void mergeSort(int[] nums, int start, int end) {
        if (end - start <= 1) {
            return;
        }
        // 分治法：divide 分为两段
        int mid = start + (end - start) / 2;
        mergeSort(nums, start, mid);
        mergeSort(nums, mid, end);
        // 合并两段数据
        merge(nums, start, mid, end);
    }

    private void merge(int[] nums, int start, int mid, int end) {
        int[] temp = new int[end - start];
        // 两边数组合并游标
        int l = start;
        int r = mid;
        int k = 0;
        // 注意不能越界
        while (l < mid && r < end) {
            // 谁小合并谁
            if (nums[l] < nums[r]) {
                temp[k++] = nums[l++];
            } else {
                temp[k++] = nums[r++];
            }
        }
        // 剩余部分合并
        while (l < mid) {
            temp[k++] = nums[l++];
        }
        while (r < end) {
            temp[k++] = nums[r++];
        }
        // 复制到原数组
        System.arraycopy(temp, 0, nums, start, temp.length);
    }

}
