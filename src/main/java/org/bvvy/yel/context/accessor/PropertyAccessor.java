package org.bvvy.yel.context.accessor;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.TypedValue;

/**
 * @author bvvy
 */
public interface PropertyAccessor {
    boolean canRead(Context context, Object target, String name);

    TypedValue read(Context context, Object target, String name);
}
