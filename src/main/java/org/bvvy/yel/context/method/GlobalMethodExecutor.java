package org.bvvy.yel.context.method;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.function.YelFunction;

import java.lang.reflect.Method;

/**
 * @author bvvy
 * @date 2022/2/16
 */
public class GlobalMethodExecutor extends ReflectiveMethodExecutor {

    private final YelFunction yelFunction;

    public GlobalMethodExecutor(YelFunction yelFunction, Method method) {
        super(method);
        this.yelFunction = yelFunction;
    }

    @Override
    public TypedValue execute(Context context, Object target, Object... arguments) {
        return super.execute(context, yelFunction, arguments);
    }
}
