package bitbucket.neo.util;

/**
 * IdUtil
 *
 * @author willian
 **/
public class IdUtil {

    /**
     * 获取16位的UUID
     */
    public static String getShortUuId() {
        return cn.hutool.core.util.IdUtil.fastSimpleUUID();
    }

}
