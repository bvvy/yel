package org.bvvy.yel.convert;

import java.lang.reflect.Type;

public class ResolvableType {

    public static ResolvableType forMethodParameter(MethodParameter methodParameter) {
        return forMethodParameter(methodParameter, (Type) null);
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter, Type targetType) {
        return forMethodParameter(methodParameter, targetType, methodParameter.getNestingLevel());
    }

    public static ResolvableType forMethodParameter(MethodParameter methodParameter, Type targetType, int nestingLevel) {
        ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
        return null;
    }

    public ResolvableType as(Class<?> declaringClass) {
        return null;
    }

    public static ResolvableType forType(Class<?> containingClass) {
        return null;

    }

}
