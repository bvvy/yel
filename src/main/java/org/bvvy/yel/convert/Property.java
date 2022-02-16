package org.bvvy.yel.convert;

import java.lang.reflect.Method;

public class Property {
    private final MethodParameter methodParameter;
    private Class<?> objectType;
    private Method readMethod;
    private Method writeMethod;
    private String name;

    public Property(Class<?> objectType, Method readMethod, Method writeMethod) {
        this(objectType, readMethod, writeMethod, null);
    }

    public Property(Class<?> objectType, Method readMethod, Method writeMethod, String name) {
        this.objectType = objectType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.methodParameter = resolveMethodParameter();
        this.name = (name != null ? name : resolveName());
    }

    private MethodParameter resolveMethodParameter() {
        return null;
    }

    private String resolveName() {
        return null;
    }

    public MethodParameter getMethodParameter() {
        return this.methodParameter;
    }

    public Class<?> getType() {
        return this.objectType;
    }
}
