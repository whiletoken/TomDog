package com.tomdog.reward.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 原神工具
 */
public class GenShinUtil {

    private static final Random RANDOM = new Random();

    private static final Character[] CHARACTERS = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static final List<Character> CHAR_LIST;

    static {
        CHAR_LIST = Arrays.asList(CHARACTERS);
    }

    private GenShinUtil() {
    }

    /**
     * 随机数
     */
    public static String getRandomFromArray(Character[] array, int count) {
        List<Character> list;
        if (array == null) {
            list = CHAR_LIST;
        } else {
            list = Arrays.asList(array);
        }
        StringBuilder re = new StringBuilder();
        List<Character> arrList = new ArrayList<>(list);
        for (int i = 0; i < count; i++) {
            int t = RANDOM.nextInt(arrList.size());
            re.append(arrList.get(t));
            arrList.remove(t);
        }
        return re.toString();
    }

}