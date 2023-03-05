package com.tomdog.leet.construct.tree;

import com.tomdog.leet.struct.MultiWayTree;
import com.tomdog.leet.struct.TreeNode;

/**
 * 树的算法基础框架
 */
public class BinaryTreeStruct {

    /**
     * 二叉树遍历
     *
     * @param root 二叉树
     */
    public static void traverseForBinaryTree(TreeNode root) {

        System.out.println("前序遍历 val = " + root.val);

        traverseForBinaryTree(root.left);
        System.out.println("中序遍历 val = " + root.val);

        traverseForBinaryTree(root.right);
        System.out.println("后序遍历 val = " + root.val);
    }



}
