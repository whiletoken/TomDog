package com.tomdog.leet.construct.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * 動態規劃
 */
public class DpStruct {

    private final Map<Integer, Integer> map = new HashMap<>();

    int fib(int N) {
        if (N == 1 || N == 2) return 1;
        if (map.containsKey(N)) {
            return map.get(N);
        }
        int result = fib(N - 1) + fib(N - 2);
        map.put(N, result);
        return result;
    }

}
