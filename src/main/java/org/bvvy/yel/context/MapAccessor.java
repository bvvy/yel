package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypedValue;

import java.util.Map;

/**
 * @author bvvy
 */
public class MapAccessor implements PropertyAccessor {
    @Override
    public boolean canRead(Context context, Object target, String name) {
        return (target instanceof Map && ((Map<?, ?>) target).containsKey(name));
    }

    @Override
    public TypedValue read(Context context, Object target, String name) {
        Map<?, ?> map = (Map<?, ?>) target;
        Object value = map.get(name);
        return new TypedValue(value);
    }
}
