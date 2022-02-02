package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypedValue;

import java.lang.reflect.Method;

public class ReflectiveMethodExecutor implements MethodExecutor {
    public ReflectiveMethodExecutor(Method method) {

    }

    @Override
    public TypedValue execute(Context context, Object value, Object... arguments) {
        return null;
    }
}
