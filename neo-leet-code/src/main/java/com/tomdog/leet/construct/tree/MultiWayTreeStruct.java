package com.tomdog.leet.construct.tree;

import com.tomdog.leet.struct.MultiWayTree;

public class MultiWayTreeStruct {

    /**
     * 多叉树遍历
     *
     * @param root 多叉树
     */
    public static void traverseForMultiWayTree(MultiWayTree root) {
        for (MultiWayTree child : root.children) {
            System.out.println("val = " + child.val);
        }
    }

    

}
