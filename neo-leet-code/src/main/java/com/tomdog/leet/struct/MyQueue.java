package com.tomdog.leet.struct;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <a href="https://leetcode.cn/problems/implement-queue-using-stacks/submissions/">...</a>
 */
public class MyQueue {

    private final Deque<Integer> headDeq;
    private final Deque<Integer> tailDeq;

    private Integer head;

    public MyQueue() {
        headDeq = new ArrayDeque<>();
        tailDeq = new ArrayDeque<>();
    }

    public void push(int x) {
        if (headDeq.isEmpty()) {
            head = x;
        }
        headDeq.push(x);
    }

    public int pop() {
        int size = headDeq.size();
        for (int i = 0; i < size; i++) {
            tailDeq.push(headDeq.pop());
        }
        int num = tailDeq.pop();
        if (!tailDeq.isEmpty()) {
            head = tailDeq.peek();
        } else {
            head = null;
        }
        size = size - 1;
        for (int i = 0; i < size; i++) {
            headDeq.push(tailDeq.pop());
        }
        return num;
    }

    public int peek() {
        return head;
    }

    public boolean empty() {
        return head == null;
    }

    public static void main(String[] args) {
        MyQueue obj = new MyQueue();
        obj.push(1);
        obj.push(2);
        obj.push(3);
        obj.push(4);
        obj.pop();
        obj.push(5);
        int param_3 = obj.peek();
        int param_2 = obj.pop();
        boolean param_4 = obj.empty();
    }

}
