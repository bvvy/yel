package org.bvvy.yel.convert;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public class MethodParameter {

    private final Method executable;
    private final int parameterIndex;
    public Map<Integer, Integer> typeIndexesPerLevel;
    private int nestingLevel;
    private Class<?> containingClass;

    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }

    public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
        this.nestingLevel = nestingLevel;
        this.executable = method;
        this.parameterIndex = parameterIndex;
    }

    public static MethodParameter forExecutable(Executable executable, int i) {
        return null;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public Class<?> getContainingClass() {
        Class<?> containingClass = this.containingClass;
        return (containingClass != null ? containingClass : getDeclaringClass());
    }

    public Class<?> getDeclaringClass() {
        return null;
    }

    public Class<?> getNestedParameterType() {
        return this.executable.getDeclaringClass();
    }

    private Type getGenericParameterType() {
        return null;
    }
}
