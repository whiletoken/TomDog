package com.tomdog.leet.struct;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 单向链表
 */
public class ListNode {

    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        ListNode listNode = this;
        StringBuilder stringBuilder = new StringBuilder("[");
        while (listNode != null) {
            stringBuilder.append(listNode.val);
            if (listNode.next != null) {
                stringBuilder.append(",");
            }
            listNode = listNode.next;
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static ListNode createNode(int[] array) {
        ListNode head = null, temp = null;
        for (int i : array) {
            if (head == null) {
                head = new ListNode(i);
                temp = head;
            } else {
                ListNode listNode = new ListNode(i);
                temp.next = listNode;
                temp = listNode;
            }
        }
        return head;
    }

    public static ListNode circleNode(ListNode listNode, int pos) {
        ListNode p = listNode, temp = null, last = null;
        int i = 0;
        while (p != null) {
            if (i == pos) {
                temp = p;
            }
            if (p.next == null) {
                last = p;
            }
            p = p.next;
            i++;
        }
        last.next = temp;
        return listNode;
    }

    /**
     * 反转链表
     * <a href="https://leetcode.cn/problems/reverse-linked-list/">...</a>
     */
    public static ListNode reverseList(ListNode head) {
        ListNode pre = null, temp, p = head;
        while (p != null) {
            temp = p.next;
            p.next = pre;
            pre = p;
            p = temp;
        }
        return pre;
    }

    /**
     * <a href="https://leetcode.cn/problems/remove-duplicates-from-sorted-list/">...</a>
     */
    public static ListNode deleteDuplicates83(ListNode head) {
        if (head == null) {
            return null;
        }
        Set<Integer> set = new HashSet<>();
        set.add(head.val);
        ListNode p = head;
        while (p.next != null) {
            if (set.contains(p.next.val)) {
                p.next = p.next.next;
            } else {
                set.add(p.next.val);
                p = p.next;
            }
        }
        return head;
    }

    /**
     * <a href="https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/">...</a>
     */
    public static ListNode deleteDuplicates82(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode p1 = new ListNode(-1, head);
        ListNode p = p1;
        Integer n = null;
        while (p.next != null) {
            if (p.next.next != null) {
                if (p.next.val == p.next.next.val) {
                    n = p.next.val;
                    p.next = p.next.next;
                } else {
                    if (n != null && p.next.val == n) {
                        p.next = p.next.next;
                    } else {
                        p = p.next;
                    }
                    n = null;
                }
            } else {
                if (n != null && p.next.val == n) {
                    p.next = null;
                }
                break;
            }
        }
        return p1.next;
    }

    /**
     * <a href="https://leetcode.cn/problems/reverse-linked-list-ii/">...</a>
     */
    public static ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode p = head;
        int i = 1;
        ListNode pre = new ListNode(-1, head);
        ListNode temp = pre;
        ListNode middle = null, temp2, temp3 = null;
        while (p != null) {
            if (i < left) {
                p = p.next;
                temp = temp.next;
                i++;
            }
            if (i >= left && i <= right) {
                if (i == left) {
                    temp3 = p;
                }
                temp2 = p.next;
                p.next = middle;
                middle = p;
                p = temp2;
                i++;
            }
            if (i > right) {
                break;
            }
        }
        temp.next = middle;
        temp3.next = p;
        return pre.next;
    }

    /**
     * <a href="https://leetcode.cn/problems/merge-two-sorted-lists/">...</a>
     */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(0);
        ListNode p = head;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                p.next = l1;
                l1 = l1.next;
            } else {
                p.next = l2;
                l2 = l2.next;
            }
            p = p.next;
        }
        // 连接未处理完节点
        p.next = l1 == null ? l2 : l1;
        return head.next;
    }

    /**
     * <a href="https://leetcode.cn/problems/merge-k-sorted-lists/submissions/">...</a>
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }
        if (lists.length == 2) {
            return mergeTwoLists(lists[0], lists[1]);
        }
        return merge(lists, 0, lists.length - 1);
    }

    public static ListNode merge(ListNode[] lists, int left, int right) {
        if (left == right) {
            return lists[left];
        }
        int middle = (left + right) >>> 1;
        ListNode node1 = merge(lists, left, middle);
        ListNode node2 = merge(lists, middle + 1, right);
        return mergeTwoLists(node1, node2);
    }

    /**
     * <a href="https://leetcode.cn/problems/middle-of-the-linked-list/">...</a>
     */
    public static ListNode middleNode(ListNode head) {
        ListNode p = head;
        ListNode q = head;
        while (q != null && q.next != null) {
            p = p.next;
            q = q.next.next;
        }
        return p;
    }

    /**
     * <a href="https://leetcode.cn/problems/reorder-list/submissions/">...</a>
     */
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;
        // 通过快慢指针找中点
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode p = head, p1 = slow.next, temp, temp1;
        ListNode q = null;
        while (p1 != null) {
            temp1 = p1.next;
            p1.next = q;
            q = p1;
            p1 = temp1;
        }
        slow.next = null;
        boolean flag = true;
        while (p != null && q != null) {
            if (flag) {
                temp = p.next;
                p.next = q;
                p = temp;
                flag = false;
            } else {
                temp = q.next;
                q.next = p;
                q = temp;
                flag = true;
            }
        }
    }

    /**
     * <a href="https://leetcode.cn/problems/palindrome-linked-list/submissions/">...</a>
     */
    public static boolean isPalindrome(ListNode head) {
        if (head == null) return false;
        if (head.next == null) return true;
        // 通过快慢指针找中点
        ListNode slow = head, fast = head.next, pre = null;
        boolean flag = false;
        while (fast.next != null) {
            if (fast.next.next != null) {
                fast = fast.next.next;
            } else {
                fast = fast.next;
                flag = true;
            }
            ListNode temp = slow.next;
            slow.next = pre;
            pre = slow;
            slow = temp;
        }
        ListNode middle = slow;
        if (!flag) {
            middle = slow.next;
        }

        head = new ListNode(slow.val, pre);
        while (middle != null) {
            if (head.val != middle.val) {
                return false;
            }
            head = head.next;
            middle = middle.next;
        }
        return true;
    }

    /**
     * <a href="https://leetcode.cn/problems/linked-list-cycle/submissions/">...</a>
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        ListNode slow = head, fast = head.next;
        while (fast.next != null && fast.next.next != null) {
            if (slow == fast) {
                return true;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return false;
    }

    /**
     * <a href="https://leetcode.cn/problems/linked-list-cycle-ii/submissions/">...</a>
     */
    public static ListNode detectCycle(ListNode head) {
        // 思路：快慢指针，快慢相遇之后，慢指针回到头，快慢指针步调一致一起移动，相遇点即为入环点
        ListNode p = head, q = head;
        while (p != null && q != null && q.next != null) {
            p = p.next;
            q = q.next.next;
            if (p == q) {
                // 指针重新从头开始移动
                ListNode m = head;
                // 比较指针对象（不要比对指针Val值）
                while (m != p) {
                    m = m.next;
                    p = p.next;
                }
                return p;
            }
        }
        return null;
    }

    /**
     * <a href="https://leetcode.cn/problems/remove-nth-node-from-end-of-list/submissions/">...</a>
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) return null;
        if (head.next == null && n == 1) return null;
        ListNode listNode = new ListNode(-1, head);
        ListNode p = listNode, q = listNode;
        while (n >= 0) {
            q = q.next;
            n--;
        }
        while (q != null) {
            p = p.next;
            q = q.next;
        }
        p.next = p.next.next;
        return listNode.next;
    }

    public static void main(String[] args) {
//        int[] array = new int[]{1, 2};
//        int[] array1 = new int[]{1, 3, 4};
//        int[] array2 = new int[]{2, 6};
//        ListNode head = ListNode.createNode(array);

//        ListNode head1 = ListNode.createNode(array1);
//        ListNode head2 = ListNode.createNode(array2);
//        ListNode[] lists = new ListNode[]{head, head1, head2};
        Integer[] array1 = new Integer[]{7, null};
        Integer[] array2 = new Integer[]{13, 0};
        Integer[] array3 = new Integer[]{11, 4};
        Integer[] array4 = new Integer[]{10, 2};
        Integer[] array5 = new Integer[]{1, 0};
        Object[] obj = new Object[]{array1, array2, array3, array4, array5};
        Node node = Node.createNode(obj);

        System.out.println(node);
    }


}
