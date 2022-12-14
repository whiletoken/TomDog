package bitbucket.neo.collection.map;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BitMap，将数据分成商和余数，商作为数组下标，余数作为二进制的一部分进行或运算
 * <p>
 * 处理常见的内存不足，大数据对比问题
 *
 * @author willian
 * @date 2020-03-11 18:49
 **/

public class BitMap {

    public byte[] bitArr;

    private static final byte MASK = 3;

    /**
     * 1 -> 1000 = 8，左移 = * 2^n，右移 = / 2^n
     */
    private static final int MAX_NUM = (1 << MASK) - 1;

    BitMap(int num) {
        bitArr = new byte[getIndex(num) + 1];
    }

    /**
     * 以8位为一组进行分组，byte[1] 代表第一组八个数字
     * 商数
     *
     * @param num num
     * @return int
     */
    private int getIndex(int num) {
        return num >> MASK;
    }

    /**
     * 取余
     *
     * @param num num
     * @return int
     */
    private int getPosition(int num) {
        return num & MAX_NUM;
    }

    /**
     * 数组下标 + bit 下标
     *
     * @param num num
     */
    public void add(int num) {
        bitArr[getIndex(num)] |= (1 << getPosition(num));
    }

    public boolean contain(int num) {
        return (bitArr[getIndex(num)] & (1 << getPosition(num))) != 0;
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {

        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>(16);
        map.put(1, 1);

        Field entrySetField = HashMap.class.getDeclaredField("entrySet");
        entrySetField.setAccessible(true);

        Object entrySet = entrySetField.get(map);
        System.out.println("entrySet = " + entrySet);

        System.out.println("map.toString() = " + map.toString());

        entrySet = entrySetField.get(map);
        System.out.println("entrySet = " + entrySet);

        System.out.println("113 = " + 113);

    }

}
