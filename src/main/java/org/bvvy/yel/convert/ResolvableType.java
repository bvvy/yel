package org.bvvy.yel.convert;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

public class ResolvableType {


    private static final ResolvableType NONE = new ResolvableType(EmptyType.INSTANCE, null, null, 0);
    private static final ResolvableType[] EMPTY_TYPES_ARRAY = new ResolvableType[0];
    private Object hash;
    private Type type;
    private TypeProvider typeProvider;
    private VariableResolver variableResolver;
    private ResolvableType componentType;

    private ResolvableType[] interfaces;

    private Class<?> resolved;
    private ResolvableType[] generics;

    public ResolvableType(Type instance, Object o, Object o1, int i) {

    }

    public ResolvableType(Class<?> clazz) {
        this.resolved = (clazz != null ? clazz : Object.class);
        this.type = this.resolved;
    }

    public ResolvableType(Type type, TypeProvider typeProvider, VariableResolver variableResolver, ResolvableType componentType) {
        this.type = type;
        this.typeProvider = typeProvider;
        this.variableResolver = variableResolver;
        this.componentType = componentType;
        this.hash = null;
        this.resolved = resolveClass();
    }

    private Class<?> resolveClass() {
        if (this.type == EmptyType.INSTANCE) {
            return null;
        }
        if (this.type instanceof Class) {
            return (Class<?>) this.type;
        }
        if (this.type instanceof GenericArrayType) {
            Class<?> resolvedComponent = getComponentType().resolve();
            return (resolvedComponent != null ? Array.newInstance(resolvedComponent, 0).getClass() : null);
        }
        return resolveType().resolve();
    }

    private ResolvableType resolveType() {
        return null;
    }

    private ResolvableType getComponentType() {
        return null;
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

    public static ResolvableType forClass(Class<?> clazz) {
        return new ResolvableType(clazz);
    }

    public static ResolvableType forType(Type type, TypeProvider typeProvider, VariableResolver variableResolver) {
        if (type == null && typeProvider == null) {
            type = SerializableTypeWrapper.forTypeProvider(typeProvider);
        }
        if (type == null) {
            return NONE;
        }

        if (type instanceof Class) {
            return new ResolvableType(type, typeProvider, variableResolver, (ResolvableType) null);
        }
        return null;
    }

    public static ResolvableType forType(Class<?> type) {
        return null;
    }

    public static ResolvableType forType(Type type, ResolvableType owner) {
        VariableResolver variableResolver = null;
        if (owner != null) {
            variableResolver = owner.asVariableResolver();
        }
        return forType(type, variableResolver);
    }

    public static ResolvableType forType(Type type, VariableResolver variableResolver) {
        return forType(type, null, variableResolver);
    }

    public ResolvableType getNested(int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
        return null;
    }

    public VariableResolver asVariableResolver() {
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

    public ResolvableType[] getInterfaces() {
        Class<?> resolved = resolve();
        if (resolved == null) {
            return EMPTY_TYPES_ARRAY;
        }
        ResolvableType[] interfaces = this.interfaces;
        if (interfaces == null) {
            Type[] genericIfcs = resolved.getGenericInterfaces();
            interfaces = new ResolvableType[genericIfcs.length];
            for (int i = 0; i < genericIfcs.length; i++) {
                interfaces[i] = forType(genericIfcs[i], this);
            }
        }
        return new ResolvableType[0];
    }

    public Class<?> resolve() {
        return this.resolved;
    }

    public Class<?> resolve(Object nestedParameterType) {
        return null;
    }

    public Class<?> toClass() {
        return resolve(Object.class);
    }

    public ResolvableType[] getGenerics() {
        if (this == NONE) {
            return EMPTY_TYPES_ARRAY;
        }
        ResolvableType[] generics = this.generics;
        if (generics == null) {
            if (this.type instanceof Class) {
                Type[] typeParams = ((Class<?>) this.type).getTypeParameters();
                generics = new ResolvableType[typeParams.length];
                for (int i = 0; i < generics.length; i++) {
                    generics[i] = ResolvableType.forType(typeParams[i], this);
                }
            }
        }
        return new ResolvableType[0];
    }

    interface VariableResolver extends Serializable {
        Object getSource();

        ResolvableType resolveVariable(TypeVariable<?> variable);
    }

    static class EmptyType implements Type, Serializable {

        static final Type INSTANCE = new EmptyType();

        Object readResolve() {
            return INSTANCE;
        }
    }
}
