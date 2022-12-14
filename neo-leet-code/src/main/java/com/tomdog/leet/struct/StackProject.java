package com.tomdog.leet.struct;

import java.util.*;

public class StackProject {

    public static Set<String> symbols = new HashSet<>();

    static {
        symbols.add("+");
        symbols.add("-");
        symbols.add("*");
        symbols.add("/");
    }

    /**
     * <a href="https://leetcode.cn/problems/evaluate-reverse-polish-notation/submissions/">...</a>
     */
    public static int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : tokens) {
            if (symbols.contains(token)) {
                int a = stack.pop();
                int b = stack.pop();
                stack.push(calculate(token, a, b));
            } else {
                stack.push(Integer.valueOf(token));
            }
        }
        return stack.pop();
    }

    public static int calculate(String symbol, int a, int b) {
        switch (symbol) {
            case "+":
                return a + b;
            case "-":
                return b - a;
            case "*":
                return a * b;
            case "/":
                return b / a;
            default:
                return 0;
        }
    }

    /**
     * <a href="https://leetcode.cn/problems/valid-parentheses/submissions/">...</a>
     */
    public static boolean isValid(String s) {
        if (s == null) return false;
        if (s.length() == 1) return false;
        Deque<String> stack = new ArrayDeque<>();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            String tempStr = String.valueOf(s.charAt(i));
            if (stack.isEmpty()) {
                stack.push(tempStr);
            } else {
                String str = stack.pop();
                if (!isValidSwitch(str, tempStr)) {
                    stack.push(str);
                    stack.push(tempStr);
                }
            }
        }
        return stack.isEmpty();
    }

    public static boolean isValidSwitch(String str, String tempStr) {
        switch (str) {
            case "(":
                return ")".equals(tempStr);
            case "[":
                return "]".equals(tempStr);
            case "{":
                return "}".equals(tempStr);
            default:
                return false;
        }
    }

    public static void main(String[] args) {
        String[] strings = new String[]{"4", "13", "5", "/", "+"};
        System.out.println(evalRPN(strings));
    }
}
