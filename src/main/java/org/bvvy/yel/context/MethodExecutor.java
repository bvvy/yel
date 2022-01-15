package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypedValue;

/**
 * @author bvvy
 * @date 2022/1/14
 */
public interface MethodExecutor {

    TypedValue execute(Context context, Object value, Object ... arguments);
}
