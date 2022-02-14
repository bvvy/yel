package org.bvvy.yel.convert;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class MethodParameter {

    private final Executable executable;
    private final int parameterIndex;
    public Map<Integer, Integer> typeIndexesPerLevel;
    private int nestingLevel;
    private Class<?> containingClass;
    private Type genericParameterType;
    private Class<?> parameterType;

    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }

    public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
        this.nestingLevel = nestingLevel;
        this.executable = method;
        this.parameterIndex = parameterIndex;
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex) {
        this(constructor, parameterIndex, 1);
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
        this.executable = constructor;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
    }



    public static MethodParameter forExecutable(Executable executable, int parameterIndex) {
        if (executable instanceof Method) {
            return new MethodParameter((Method) executable, parameterIndex);
        } else if (executable instanceof Constructor) {
            return new MethodParameter((Constructor<?>) executable, parameterIndex);
        } else {
            throw new IllegalArgumentException("Not a Method/Constructor: " + executable);
        }
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
        if (this.nestingLevel > 1) {
            Type type = getGenericParameterType();
            for (int i = 2; i < this.nestingLevel; i++) {
                if (type instanceof ParameterizedType) {
                    Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                    Integer index = getTypeIndexForLevel(i);
                    type = args[index != null ? index : args.length - 1];
                }
            }
            if (type instanceof Class) {
                return ((Class<?>) type);
            } else if (type instanceof ParameterizedType) {
                Type arg = ((ParameterizedType) type).getRawType();
                if (arg instanceof Class) {
                    return ((Class<?>) arg);
                }
            }
            return Object.class;
        } else {
            return getParameterType();
        }
    }

    private Class<?> getParameterType() {
        Class<?> paramType = this.parameterType;
        if (paramType != null) {
            return paramType;
        }
        if (getContainingClass() != getDeclaringClass()) {
            paramType = ResolvableType.forMethodParameter(this, null, 1).resolve();
        }
        if (paramType == null) {
            paramType = computeParameterType();
        }
        this.parameterType = paramType;
        return paramType;
    }

    private Integer getTypeIndexForLevel(int nestingLevel) {
        return getTypeIndexesPerLevel().get(nestingLevel);
    }

    private Map<Integer, Integer> getTypeIndexesPerLevel() {
        if (this.typeIndexesPerLevel == null) {
            this.typeIndexesPerLevel = new HashMap<>(4);
        }
        return this.typeIndexesPerLevel;
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

    private Class<?> computeParameterType() {
        if (this.parameterIndex < 0) {
            Method method = getMethod();
            if (method == null) {
                return void.class;
            }
            return method.getReturnType();
        }
        return this.executable.getParameterTypes()[this.parameterIndex];
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
