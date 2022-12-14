package com.tomdog.leet.practice.tree;

import com.tomdog.leet.struct.TreeNode;

import java.util.*;

public class TreePart2 {

    /**
     * <a href="https://leetcode.cn/leetbook/read/data-structure-binary-tree/xeywh5/">...</a>
     */
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new LinkedList<>();
        //加个边界条件判断
        if (root == null) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);//压栈
        while (!stack.empty()) {
            TreeNode t1 = stack.pop();//出栈
            res.add(t1.val);
            if (t1.right != null) {
                stack.push(t1.right);
            }
            if (t1.left != null) {
                stack.push(t1.left);
            }
        }
        return res;
    }

    public void preorderTraversal(List<Integer> list, TreeNode root) {
        if (root == null) {
            return;
        }
        list.add(root.val);
        preorderTraversal(list, root.left);
        preorderTraversal(list, root.right);
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/data-structure-binary-tree/xo566j/">...</a>
     */
    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) {
            return false;
        }
        return hasPathSum(0, root, targetSum);
    }

    public static boolean hasPathSum(int num, TreeNode root, int targetSum) {
        if (root == null) {
            return num == targetSum;
        }
        num += root.val;
        if (root.left != null && hasPathSum(num, root.left, targetSum)) {
            return true;
        }
        if (root.right != null && hasPathSum(num, root.right, targetSum)) {
            return true;
        }
        if (root.left == null && root.right == null) {
            return num == targetSum;
        }
        return false;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/data-structure-binary-tree/xo98qt/">...</a>
     */
    public static TreeNode buildTree(int[] inorder, int[] postorder) {
        int len = inorder.length;
        if (len == 0) {
            return null;
        }
        map = new HashMap<>(inorder.length);
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return buildTree(postorder, 0, 0, len - 1);
    }

    public static TreeNode buildTree(int[] postorder, int head1, int head2, int tail2) {
        if (head2 > tail2) return null;

        int val = postorder[tail2];
        TreeNode root = new TreeNode(val);
        if (head2 == tail2) return root;

        int mid = map.get(val) - head1;

        root.left = buildTree(postorder, head1, head2, head2 + mid - 1);
        root.right = buildTree(postorder, head1 + mid + 1, head2 + mid, tail2 - 1);

        return root;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/data-structure-binary-tree/xoei3r/">...</a>
     */
    public static TreeNode buildTree2(int[] preorder, int[] inorder) {
        int len = inorder.length;
        if (len == 0) {
            return null;
        }
        map = new HashMap<>(len);
        for (int i = 0; i < len; i++) {
            map.put(inorder[i], i);
        }
        return buildTree2(preorder, 0, len - 1, 0, len - 1);
    }

    public static Map<Integer, Integer> map = null;

    public static TreeNode buildTree2(int[] preorder, int head1, int tail1, int head2, int tail2) {
        if (head2 > tail2) return null;

        int val = preorder[head2];
        TreeNode root = new TreeNode(val);
        if (head2 == tail2) return root;

        int mid = map.get(val) - head1;

        root.left = buildTree2(preorder, head1, head1 + mid - 1, head2 + 1, head2 + mid);
        root.right = buildTree2(preorder, head1 + mid + 1, tail1, head2 + mid + 1, tail2);

        return root;
    }

    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }

        public static Node createTreeNode(String data) {
            if (data.equals("[]")) return null;
            data = data.substring(1, data.length() - 1);
            String[] split = data.split(",");
            int len = len = split.length;
            Node[] treeNodes = new Node[len];
            data = data.substring(1, data.length() - 1);
            for (int i = 0; i < len; i++) {
                if (!split[i].equals("null")) {
                    treeNodes[i] = new Node(Integer.parseInt(split[i]));
                }
            }
            for (int i = 0; i < len; i++) {
                if (treeNodes[i] != null) {
                    int leftIndex = i * 2 + 1;
                    if (leftIndex < len) {
                        treeNodes[i].left = treeNodes[leftIndex];
                    }
                    int rightIndex = leftIndex + 1;
                    if (rightIndex < len) {
                        treeNodes[i].right = treeNodes[rightIndex];
                    }
                }
            }
            return treeNodes[0];
        }
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/data-structure-binary-tree/xoo0ts/">...</a>
     */
    public static Node connect(Node root) {
        if (root == null) {
            return null;
        }

        //cur我们可以把它看做是每一层的链表
        Node cur = root;
        while (cur != null) {
            //遍历当前层的时候，为了方便操作在下一
            //层前面添加一个哑结点（注意这里是访问
            //当前层的节点，然后把下一层的节点串起来）
            Node dummy = new Node(0);
            //pre表示访下一层节点的前一个节点
            Node pre = dummy;
            //然后开始遍历当前层的链表
            while (cur != null) {
                if (cur.left != null) {
                    //如果当前节点的左子节点不为空，就让pre节点
                    //的next指向他，也就是把它串起来
                    pre.next = cur.left;
                    //然后再更新pre
                    pre = pre.next;
                }
                //同理参照左子树
                if (cur.right != null) {
                    pre.next = cur.right;
                    pre = pre.next;
                }
                //继续访问这一行的下一个节点
                cur = cur.next;
            }
            //把下一层串联成一个链表之后，让他赋值给cur，
            //后续继续循环，直到cur为空为止
            cur = dummy.next;
        }
        return root;
    }

    public static class Codec {

        public String serialize(TreeNode root) {
            //边界判断，如果为空就返回一个字符串"#"
            if (root == null)
                return "#";
            //创建一个队列
            Queue<TreeNode> queue = new LinkedList<>();
            StringBuilder res = new StringBuilder();
            //把根节点加入到队列中
            queue.add(root);
            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                if (node == null) {
                    res.append("#,");
                    continue;
                }
                res.append(node.val).append(",");
                queue.add(node.left);
                queue.add(node.right);
            }
            return res.toString();
        }

        public TreeNode deserialize(String data) {
            if ("#".equals(data)) {
                return null;
            }
            String[] values = data.split(",");
            Queue<TreeNode> queue = new LinkedList<>();
            TreeNode root = new TreeNode(Integer.parseInt(values[0]));
            queue.add(root);
            for (int i = 1; i < values.length; i++) {
                TreeNode treeNode = queue.poll();
                if (!"#".equals(values[i])) {
                    treeNode.left = new TreeNode(Integer.parseInt(values[i]));
                    queue.add(treeNode.left);
                }
                if (!"#".equals(values[++i])) {
                    treeNode.right = new TreeNode(Integer.parseInt(values[i]));
                    queue.add(treeNode.right);
                }
            }
            return root;
        }
    }

    public static void main(String[] args) {
//        TreeNode treeNode = TreeNode.createTreeNode("[9,3,15,20,7]");
//        hasPathSum(treeNode, 22);
        Node node = Node.createTreeNode("[3,9,20,null,null,15,7]");
        System.out.println(connect(node));
    }

}
