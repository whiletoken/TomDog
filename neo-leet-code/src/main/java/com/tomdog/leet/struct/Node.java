package com.tomdog.leet.struct;

public class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }

    public Node(int val, Node random) {
        this.val = val;
        this.next = null;
        this.random = random;
    }

    public static Node createNode(Object[] objects) {
        if (objects == null || objects.length == 0) return null;
        Integer[] array1 = (Integer[]) objects[0];
        Node node = new Node(array1[0], array1[1] == null ? null : new Node(array1[1]));
        Node p = node;
        for (int i = 1; i < objects.length; i++) {
            Integer[] array = (Integer[]) objects[i];
            p.next = new Node(array[0], new Node(array[1]));
            p = p.next;
        }
        return node;
    }

    @Override
    public String toString() {
        Node node = this;
        StringBuilder stringBuilder = new StringBuilder("[");
        while (node != null) {
            stringBuilder.append("[").append(node.val).append(",").append(node.random == null ? "null" : node.random.val).append("]");
            if (node.next != null) {
                stringBuilder.append(",");
            }
            node = node.next;
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static Node copyRandomList(Node head) {
        return head;
    }

}
