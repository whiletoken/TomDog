package com.tomdog.leet.struct;

/**
 * 多叉树
 */
public class MultiWayTree {

    public int val;

    public MultiWayTree[] children;

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public MultiWayTree[] getChildren() {
        return children;
    }

    public void setChildren(MultiWayTree[] children) {
        this.children = children;
    }
}
