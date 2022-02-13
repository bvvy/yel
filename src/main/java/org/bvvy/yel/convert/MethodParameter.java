package org.bvvy.yel.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public class MethodParameter {

    private final Executable executable;
    private final int parameterIndex;
    public Map<Integer, Integer> typeIndexesPerLevel;
    private int nestingLevel;
    private Class<?> containingClass;
    private Type genericParameterType;

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
        return this.executable.getDeclaringClass();
    }

    public Class<?> getNestedParameterType() {
        return this.executable.getDeclaringClass();
    }

    public Type getGenericParameterType() {
        Type paramType = this.genericParameterType;
        if (paramType == null) {
            if (this.parameterIndex < 0) {
                Method method = getMethod();
                paramType = (method != null ? method.getGenericReturnType() : void.class);
            } else {
                Type[] genericParameterTypes = this.executable.getGenericParameterTypes();
                int index = this.parameterIndex;
                if (this.executable instanceof Constructor) {
                    index = this.parameterIndex - 1;
                }
                paramType = (index >= 0 && index < genericParameterTypes.length ? genericParameterTypes[index] : computeParameterType());
            }
            this.genericParameterType = paramType;
        }
        return paramType;
    }

    private Type computeParameterType() {
        return null;
    }

    public Method getMethod() {
        return (this.executable instanceof Method ? ((Method) this.executable) : null);
    }

    public Executable getExecutable() {
        return this.executable;
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }
}
