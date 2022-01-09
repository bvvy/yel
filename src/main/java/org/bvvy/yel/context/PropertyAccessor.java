package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypedValue;

/**
 * @author bvvy
 */
public interface PropertyAccessor {
    boolean canRead(Context context, Object value, String name);

    TypedValue read(Context context, Object target, String name);
}
