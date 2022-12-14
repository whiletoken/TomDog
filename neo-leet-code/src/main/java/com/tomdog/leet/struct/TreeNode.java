package com.tomdog.leet.struct;

import java.util.*;

public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {
    }

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public static TreeNode createTreeNode(String data) {
        if (data.equals("[]")) return null;
        data = data.substring(1, data.length() - 1);
        String[] split = data.split(",");
        int len = len = split.length;
        TreeNode[] treeNodes = new TreeNode[len];
        data = data.substring(1, data.length() - 1);
        for (int i = 0; i < len; i++) {
            if (!split[i].equals("null")) {
                treeNodes[i] = new TreeNode(Integer.parseInt(split[i]));
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

    /**
     * <a href="https://leetcode.cn/problems/binary-tree-preorder-traversal/submissions/">...</a>
     */
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list = new LinkedList<>();
        preorderTraversal(root, list);
        return list;
    }

    public static void preorderTraversal(TreeNode treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        list.add(treeNode.val);
        preorderTraversal(treeNode.left, list);
        preorderTraversal(treeNode.right, list);
    }

    /**
     * <a href="https://leetcode.cn/problems/binary-tree-inorder-traversal/submissions/">...</a>
     */
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new LinkedList<>();
        inorderTraversal(root, list);
        return list;
    }

    public static void inorderTraversal(TreeNode treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        inorderTraversal(treeNode.left, list);
        list.add(treeNode.val);
        inorderTraversal(treeNode.right, list);
    }

    /**
     * <a href="https://leetcode.cn/problems/binary-tree-postorder-traversal/submissions/">...</a>
     */
    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new LinkedList<>();
        postorderTraversal(root, list);
        return list;
    }

    public static void postorderTraversal(TreeNode treeNode, List<Integer> list) {
        if (treeNode == null) {
            return;
        }
        postorderTraversal(treeNode.left, list);
        postorderTraversal(treeNode.right, list);
        list.add(treeNode.val);
    }

    /**
     * <a href="https://leetcode.cn/problems/binary-tree-paths/submissions/">...</a>
     */
    public static List<String> binaryTreePaths(TreeNode root) {
        StringBuilder path = new StringBuilder();
        List<String> paths = new LinkedList<>();
        dfs(root, path, paths);
        return paths;
    }

    public static void dfs(TreeNode p, StringBuilder path, List<String> paths) {
        if (p == null) {
            return;
        }
        path.append(p.val);
        // 当前节点是叶子节点
        if (p.left == null && p.right == null) {
            // 把路径加入结果
            paths.add(path.toString());
        } else {
            path.append("->");
            // 这里需要复制创建新的StringBuilder对象
            dfs(p.left, new StringBuilder(path), paths);
            dfs(p.right, new StringBuilder(path), paths);
        }
    }

    /**
     * <a href="https://leetcode.cn/problems/validate-binary-search-tree/submissions/">...</a>
     */
    public boolean isValidBST(TreeNode root) {
        return divideAndConquer(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean divideAndConquer(TreeNode p, long min, long max) {
        if (p == null) return true;
        // 返回条件
        if (p.val <= min || max <= p.val) {
            return false;
        }
        // 分治(Divide)
        boolean left = divideAndConquer(p.left, min, p.val);
        if (!left) {
            return false;
        }
        return divideAndConquer(p.right, p.val, max);
    }

    /**
     * <a href="https://leetcode.cn/problems/maximum-depth-of-binary-tree/">...</a>
     */
    public int maxDepth(TreeNode root) {
        // 返回条件处理
        if (root == null) {
            return 0;
        }
        // divide：分左右子树分别计算
        // conquer：合并左右子树结果，即取二者中的最大值加一
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    /**
     * <a href="https://leetcode.cn/problems/balanced-binary-tree/">...</a>
     */
    public static boolean isBalanced(TreeNode root) {
        return maxDepth1(root) >= 0;
    }

    private static int maxDepth1(TreeNode p) {
        if (p == null) {
            return 0;
        }
        int left = maxDepth1(p.left);
        int right = maxDepth1(p.right);
        if (left < 0 || right < 0 || Math.abs(left - right) > 1) {
            return -1;
        } else {
            return Math.max(left, right) + 1;
        }
    }

    public static String bfs(TreeNode root) {
        StringBuilder stringBuilder = new StringBuilder();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll(); // Java 的 pop 写作 poll()
            stringBuilder.append(node.val).append("->");
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * <a href="https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/">...</a>
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new LinkedList<>();
        if (root == null) return result;
        List<Integer> temp = new LinkedList<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        int pre = 1, mark = 0;
        while (!queue.isEmpty()) {
            pre--;
            TreeNode node = queue.poll(); // Java 的 pop 写作 poll()
            temp.add(node.val);
            if (node.left != null) {
                mark++;
                queue.add(node.left);
            }
            if (node.right != null) {
                mark++;
                queue.add(node.right);
            }
            if (pre == 0) {
                pre = mark;
                mark = 0;
                result.add(temp);
                temp = new LinkedList<>();
            }
        }
        for (int i = 0; i < result.size(); i++) {
            if (i % 2 != 0) {
                Collections.reverse(result.get(i));
            }
        }
        return result;
    }

    public static int maxSum;

    public static int maxPathSum(TreeNode root) {
        maxSum = -10000;
        return Math.max(maxPath(root), maxSum);
    }

    public static int maxPath(TreeNode root) {
        if (root == null) return -10000;
        int left = maxPath(root.left);
        int right = maxPath(root.right);

        int sum3 = left + right + root.val;
        int maxSide = Math.max(left, right);
        int maxNum = Math.max(maxSide, root.val);

        if (sum3 > maxNum) {
            maxSum = Math.max(sum3, maxSum);
            return sum3 - Math.min(left, right);
        }
        int sum2 = root.val + maxSide;
        if (sum2 > maxNum) {
            maxSum = Math.max(sum3, maxSum);
            return sum2;
        } else {
            maxSum = Math.max(maxNum, maxSum);
            if (maxNum == root.val) {
                return maxNum;
            } else {
                return sum2;
            }
        }

    }

    public static int swap(int a, int b) {
        return Math.max(a, b);
    }

    /**
     * <a href="https://leetcode.cn/problems/insert-into-a-binary-search-tree/">...</a>
     */
    public static TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        if (root.val > val) {
            root.left = insertIntoBST(root.left, val);
        } else {
            root.right = insertIntoBST(root.right, val);
        }
        return root;
    }


    /**
     * <a href="https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/submissions/">...</a>
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // check
        if (root == null) {
            return null;
        }
        // 相等 直接返回root节点即可
        if (root == p || root == q) {
            return root;
        }
        // Divide
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        // Conquer
        // 左右两边都不为空，则根节点为祖先
        if (left != null && right != null) {
            return root;
        } else if (left != null) {
            return left;
        } else {
            return right;
        }
    }

    public static void main(String[] args) {
//        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, null, 8};
        TreeNode treeNode = createTreeNode("[7,null,7,null,4]");
        System.out.println(maxPathSum(treeNode));
    }

}
