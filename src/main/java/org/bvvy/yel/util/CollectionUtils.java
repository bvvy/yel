package org.bvvy.yel.util;

import java.util.Collection;
import java.util.List;

/**
 * @author bvvy
 * @date 2022/1/24
 */
public class CollectionUtils {

    public static <T> T lastElement(List<T> list) {
        if (hasElement(list)) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    public static boolean hasElement(Collection<?> list) {
        return list != null && list.size() > 0;
    }

    public static boolean isEmpty(Collection<?> list) {
        return !hasElement(list);
    }
}
