package org.bvvy.yel.convert;

import java.lang.reflect.Method;

public class MethodParameter {

    private final Method executable;
    private final int parameterIndex;
    private int nestingLevel;

    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }

    public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
        this.nestingLevel = nestingLevel;
        this.executable = method;
        this.parameterIndex = parameterIndex;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public Class<?> getContainingClass() {
        return null;
    }

    public Class<?> getDeclaringClass() {
        return null;
    }
}
