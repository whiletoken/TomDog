package bitbucket.neo.collection;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * 集合工具类
 *
 * @author yulewei on 2016/8/30
 */
public class NeoCollectionUtils {

    private final static Logger log = LoggerFactory.getLogger(NeoCollectionUtils.class);

    /**
     * 从bean对象List中筛选出某个属性的List
     * 注1：读取的是属性值而非字段值
     * 注2：类似的功能，也可以直接使用Apache Commons库，示例：
     * CollectionUtils.collect(helloList, TransformerUtils.<Hello, Integer>invokerTransformer("getId"))
     */
    public static <I, O> List<O> collect(Collection<I> inputCollection, String propertyName) {
        List<O> list = new ArrayList<>();
        for (I item : inputCollection) {
            try {
                if (item == null) {
                    continue;
                }
                list.add((O) PropertyUtils.getProperty(item, propertyName));
            } catch (Exception e) {
                log.error("collect exception is ", e);
            }
        }
        return list;
    }

    /**
     * 将List转为Map，键和值对应关系为，1对1
     *
     * @param inputCollection 来源集合
     * @param keyPropertyName 要提取为Map中的Key值的属性名
     */
    public static <K, O> Map<K, O> toMap(Collection<O> inputCollection, String keyPropertyName) {
        Map<K, O> map = new HashMap<>(16);
        for (O item : inputCollection) {
            try {
                if (item == null) {
                    continue;
                }
                K keyValue = (K) PropertyUtils.getProperty(item, keyPropertyName);
                map.put(keyValue, item);
            } catch (Exception e) {
                log.error("toMap exception is ", e);
            }
        }
        return map;
    }

    /**
     * 将List转为Map，键和值对应关系为，1对1
     *
     * @param inputCollection   来源集合
     * @param keyPropertyName   要提取为Map中的Key值的属性名
     * @param valuePropertyName 要提取为Map中的Value值的属性名
     */
    public static <K, O, V> Map<K, V> toMap(Collection<O> inputCollection,
                                            String keyPropertyName, String valuePropertyName) {
        Map<K, V> map = new HashMap<>(16);
        for (O item : inputCollection) {
            try {
                if (item == null) {
                    continue;
                }
                K keyValue = (K) PropertyUtils.getProperty(item, keyPropertyName);
                V valueProperty = (V) PropertyUtils.getProperty(item, valuePropertyName);
                map.put(keyValue, valueProperty);
            } catch (Exception e) {
                log.error("toMap exception is ", e);
            }
        }
        return map;
    }

    /**
     * 将List转为Map，键和值对应关系为，1对N
     */
    public static <K, O> Map<K, List<O>> groupToMap(Collection<O> inputCollection, String keyPropertyName) {
        Map<K, List<O>> map = new HashMap<>(16);
        for (O item : inputCollection) {
            try {
                if (item == null) {
                    continue;
                }
                K keyValue = (K) PropertyUtils.getProperty(item, keyPropertyName);
                List<O> list = map.get(keyValue);
                if (list == null) {
                    list = new ArrayList<O>();
                }
                list.add(item);
                map.put(keyValue, list);
            } catch (Exception e) {
                log.error("groupToMap exception is ", e);
            }
        }
        return map;
    }

    /**
     * 按键的子集，提取子列表
     */
    public static <K, O> List<O> subList(Map<K, O> inputMap, Collection<K> keys) {
        List<O> list = new ArrayList<>();
        for (K key : keys) {
            if (key == null) {
                continue;
            }
            list.add(inputMap.get(key));
        }
        return list;
    }

    public static <T> List<T> group(List<T> collection, int pageNum, int pageSize) {

        if (pageNum < 0 || pageSize < 0) {
            throw new RuntimeException("param error,please check");
        }

        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>();
        }

        if (collection.size() < pageNum * pageNum) {
            return new ArrayList<>();
        }

        List<List<T>> lists = Lists.partition(collection, pageSize);

        return lists.get(pageNum - 1);
    }
}
