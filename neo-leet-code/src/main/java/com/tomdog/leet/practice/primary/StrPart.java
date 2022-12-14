package com.tomdog.leet.practice.primary;

import java.util.*;

public class StrPart {

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnhbqj/">...</a>
     */
    public static void reverseString(char[] s) {
        int mid = s.length >> 1;
        int length = s.length - 1;
        char temp;
        for (int i = 0; i < mid; i++) {
            temp = s[i];
            s[i] = s[length - i];
            s[length - i] = temp;
        }
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnx13t/">...</a>
     */
    public static int reverse(int x) {
        int pre = x < 0 ? -1 : 1;
        x = Math.abs(x);
        if (x < 0) {
            return 0;
        }
        int temp = 0, temp1;
        while (x >= 10) {
            int t = x % 10;
            temp1 = temp;
            temp = 10 * (temp + t);
            if ((temp / 10 - t) != temp1) {
                return 0;
            }
            x = x / 10;
        }
        temp1 = temp;
        temp = (temp + x);
        if ((temp - x) != temp1) {
            return 0;
        }
        return temp * pre;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xn5z8r/">...</a>
     */
    public static int firstUniqChar(String s) {
        if (s.length() == 1) {
            return 0;
        }
        Map<Character, Integer> strMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (strMap.containsKey(s.charAt(i))) {
                strMap.put(s.charAt(i), 2);
            } else {
                strMap.put(s.charAt(i), 1);
            }
        }
        for (int i = 0; i < s.length(); i++) {
            if (strMap.get(s.charAt(i)) == 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xn96us/">...</a>
     */
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        if (s.equals(t)) {
            return true;
        }
        Map<Character, Integer> strMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char str = s.charAt(i);
            if (strMap.containsKey(str)) {
                strMap.put(str, strMap.get(str) + 1);
            } else {
                strMap.put(str, 1);
            }
        }
        for (int i = 0; i < t.length(); i++) {
            char str = t.charAt(i);
            Integer index = strMap.get(str);
            if (index != null) {
                if (index == 1) {
                    strMap.remove(str);
                } else {
                    strMap.put(str, index - 1);
                }
            } else {
                return false;
            }
        }
        return strMap.size() == 0;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xne8id/">...</a>
     */
    public static boolean isPalindrome(String s) {
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if (strSet1.contains(s.charAt(i))) {
                int temp = s.charAt(i) + 32;
                list.add((char) temp);
            }
            if (strSet2.contains(s.charAt(i)) || strSet3.contains(s.charAt(i))) {
                list.add(s.charAt(i));
            }
        }
        int mid = list.size() >> 1;
        for (int i = 0; i < mid; i++) {
            if (list.get(i) != list.get(list.size() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnoilh/">...</a>
     */
    public static int myAtoi(String s) {
        int sign = 1;
        Long temp = null;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                if (temp == null) {
                    continue;
                } else {
                    break;
                }
            }
            if (s.charAt(i) == '-' || s.charAt(i) == '+') {
                if (temp == null) {
                    if (s.charAt(i) == '-') {
                        sign = -1;
                    }
                    temp = 0L;
                    continue;
                } else {
                    break;
                }
            }
            int num = s.charAt(i) - '0';
            if (num > 9 || num < 0) {
                break;
            }
            temp = (temp == null ? 0 : temp) * 10 + num;
            if ((temp - 1) > Integer.MAX_VALUE) {
                break;
            }
        }
        if (temp == null) {
            return 0;
        }
        temp *= sign;
        if (temp >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (temp <= Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return Math.toIntExact(temp);
    }

    static char[] str1 = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    static char[] str2 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static Set<Character> strSet1 = new HashSet<>();
    static Set<Character> strSet2 = new HashSet<>();
    static Set<Character> strSet3 = new HashSet<>();

    static {
        for (char c : str1) {
            strSet1.add(c);
            int temp = c + 32;
            strSet2.add((char) temp);
        }
        for (char c : str2) {
            strSet3.add(c);
        }
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnr003/">...</a>
     */
    public static int strStr(String haystack, String needle) {
        if (needle.length() > haystack.length()) {
            return -1;
        }
        boolean[][] dp = new boolean[needle.length()][haystack.length()];
        int index = -1;
        for (int i = 0; i < needle.length(); i++) {
            for (int j = i; j < haystack.length(); j++) {
                if (needle.charAt(i) == haystack.charAt(j)) {
                    if (i != 0) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = true;
                    }
                } else {
                    dp[i][j] = false;
                    continue;
                }
                if (j >= needle.length() - 1 && dp[needle.length() - 1][j]) {
                    index = j;
                    break;
                }
            }
        }
        if (index == -1) {
            return -1;
        }
        return index - needle.length() + 1;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnpvdm/">...</a>
     */
    public static String countAndSay(int n) {
        if (n == 1) {
            return "1";
        }
        if (n == 2) {
            return "11";
        }
        String value = "11";
        for (int i = 3; i <= n; i++) {
            int p = 0, q = 1;
            StringBuilder stringBuilder = new StringBuilder();
            while (q < value.length()) {
                if (value.charAt(p) != value.charAt(q)) {
                    stringBuilder.append(q - p).append(value.charAt(p));
                    p = q;
                }
                q++;
                if (q == value.length()) {
                    stringBuilder.append(q - p).append(value.charAt(p));
                }
            }
            value = stringBuilder.toString();
        }
        return value;
    }

    /**
     * <a href="https://leetcode.cn/leetbook/read/top-interview-questions-easy/xnmav1/">...</a>
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        for (int i = 0; i < strs.length - 1; i++) {
            strs[i + 1] = longestCommonPrefix(strs[i], strs[i + 1]);
            if (strs[i + 1] == null) {
                return "";
            }
        }
        return strs[strs.length - 1];
    }

    public static String longestCommonPrefix(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return null;
        }
        int p = 0, q = 0;
        while (p < str1.length() && q < str2.length()) {
            if (str1.charAt(p) == str2.charAt(q)) {
                p++;
                q++;
            } else {
                break;
            }
        }
        if (p == 0) {
            return null;
        }
        return str1.substring(0, p);
    }

    public static void main(String[] args) {
        System.out.println(longestCommonPrefix(new String[]{"flower", "flow", "flight"}));
    }

}
