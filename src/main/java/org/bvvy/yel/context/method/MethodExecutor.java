package org.bvvy.yel.context.method;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.TypedValue;

/**
 * @author bvvy
 * @date 2022/1/14
 */
public interface MethodExecutor {

    TypedValue execute(Context context, Object value, Object ... arguments);
}
