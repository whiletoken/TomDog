package bitbucket.neo.collection.map;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * least recently used 最近最常访问
 * LFU Least Frequently Used 最近最少访问
 *
 * @author arthas
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 缓存上限
     */
    private static final int MAX_ENTRIES = 3;

    /**
     * afterNodeAccess()
     * 当一个节点被访问时，如果 accessOrder 为 true，则会将该节点移到链表尾部。
     * 也就是说指定为 LRU 顺序之后，在每次访问一个节点时，会将这个节点移到链表尾部，保证链表尾部是最近访问的节点，那么链表首部就是最近最久未使用的节点。
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_ENTRIES;
    }

    LRUCache() {
        super(MAX_ENTRIES, 0.75f, true);
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>();
        cache.put(1, "a");
        cache.put(2, "b");
        cache.put(3, "c");
        cache.get(1);
        cache.put(4, "d");
        System.out.println(cache.keySet());
    }
}
