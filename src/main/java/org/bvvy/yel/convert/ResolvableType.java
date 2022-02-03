package org.bvvy.yel.convert;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

public class ResolvableType {

    public static final ResolvableType NONE = new ResolvableType(EmptyType.INSTANCE, null, null,0);
    private static final ResolvableType[] EMPTY_TYPES_ARRAY = new ResolvableType[0];

    private ResolvableType[] interfaces;

    private Class<?>  resolved;

    public ResolvableType(Type instance, Object o, Object o1, int i) {

    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter) {
        return forMethodParameter(methodParameter, (Type) null);
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter, Type targetType) {
        return forMethodParameter(methodParameter, targetType, methodParameter.getNestingLevel());
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter, Type targetType, int nestingLevel) {
        return forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
    }

    private ResolvableType getNested(int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
        return null;
    }

    private static ResolvableType forType(Type targetType, MethodParameterTypeProvider methodParameterTypeProvider, Object asVariableResolver) {
        return null;
    }

    private Object asVariableResolver() {
        return null;
    }

    public ResolvableType as(Class<?> type) {
        if (this == NONE) {
            return NONE;
        }
        Class<?> resolved = resolve();
        if (resolved == null || resolved == type) {
            return this;
        }
        for (ResolvableType interfaceType : getInterfaces()) {
        }
        return null;
    }

    private ResolvableType[] getInterfaces() {
        Class<?> resolved = resolve();
        if (resolved == null) {
            return EMPTY_TYPES_ARRAY;
        }
        ResolvableType [] interfaces = this.interfaces;
        if (interfaces == null) {
            Type[] genericIfcs = resolved.getGenericInterfaces();
            interfaces = new ResolvableType[genericIfcs.length];
            for (int i = 0; i < genericIfcs.length; i++) {
                interfaces[i] = forType(genericIfcs[i], this);
            }
        }
        return new ResolvableType[0];
    }

    private ResolvableType forType(Type genericIfc, ResolvableType resolvableType) {
        return null;
    }

    public static ResolvableType forType(Class<?> type) {
        return null;
    }

    private Class<?> resolve() {
        return this.resolved;
    }

    public Class<?> resolve(Object nestedParameterType) {
        return null;
    }

    static class EmptyType implements Type, Serializable {

        static final Type INSTANCE = new EmptyType();

        Object readResolve() {
            return INSTANCE;
        }
    }
}
