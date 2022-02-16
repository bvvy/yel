package org.bvvy.yel.convert;

import org.bvvy.yel.util.ClassUtils;
import org.bvvy.yel.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bvvy
 * @date 2022/1/13
 */
public class TypeDescriptor {

    private static final Class<?>[] CACHED_COMMON_TYPES = {
            boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class,
            short.class, Short.class, int.class, Integer.class, float.class, Float.class,
            long.class, Long.class, double.class, Double.class, String.class, Object.class
    };
    private static Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap<>(32);

    static {
        for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
            commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
        }
    }

    private final ResolvableType resolvableType;
    private final Class<?> type;

    public TypeDescriptor(MethodParameter methodParameter) {
        this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
        this.type = this.resolvableType.resolve(methodParameter.getNestedParameterType());
        // todo annotation
    }

    public TypeDescriptor(ResolvableType resolvableType, Class<?> type, Annotation[] annotations) {
        this.resolvableType = resolvableType;
        this.type = (type != null ? type : resolvableType.toClass());
        //todo annotations
    }

    public TypeDescriptor(Property property) {
        this.resolvableType = ResolvableType.forMethodParameter(property.getMethodParameter());
        this.type = this.resolvableType.resolve(property.getType());
        // todo annotations
    }

    public TypeDescriptor(Field field) {
        this.resolvableType = ResolvableType.forField(field);
        this.type = this.resolvableType.resolve(field.getType());
        // todo annotations
    }

    public static TypeDescriptor forObject(Object source) {
        return (source != null ? valueOf(source.getClass()) : null);
    }

    public static TypeDescriptor valueOf(Class<?> type) {
        if (type == null) {
            type = Object.class;
        }
        TypeDescriptor desc = commonTypesCache.get(type);
        return (desc != null ? desc : new TypeDescriptor(ResolvableType.forClass(type), null, null));
    }

    public boolean isPrimitive() {
        return getType().isPrimitive();
    }

    public boolean isAssignableTo(TypeDescriptor typeDescriptor) {
        boolean typeAssignable = typeDescriptor.getObjectType().isAssignableFrom(getObjectType());
        if (!typeAssignable) {
            return false;
        }
        if (isArray() && typeDescriptor.isArray()) {
            return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
        } else if (isCollection() && typeDescriptor.isCollection()) {
            return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
        } else if (isMap() && typeDescriptor.isMap()) {
            return isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor())
                    && isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor());
        } else {
            return true;
        }
    }

    private boolean isNestedAssignable(TypeDescriptor nestedTypeDescriptor, TypeDescriptor otherNestedTypeDescriptor) {
        return (nestedTypeDescriptor == null
                || otherNestedTypeDescriptor == null
                || nestedTypeDescriptor.isAssignableTo(otherNestedTypeDescriptor));
    }


    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof TypeDescriptor)) {
            return false;
        }
        TypeDescriptor otherDesc = (TypeDescriptor) other;
        if (getType() != otherDesc.getType()) {
            return false;
        }
        if (!annotationsMatch(otherDesc)) {
            return false;
        }
        if (isCollection() || isArray()) {
            return ObjectUtils.nullSafeEquals(getElementTypeDescriptor(), otherDesc.getElementTypeDescriptor());
        } else if (isMap()) {
            return (ObjectUtils.nullSafeEquals(getMapKeyTypeDescriptor(), otherDesc.getMapKeyTypeDescriptor())
                    && ObjectUtils.nullSafeEquals(getMapValueTypeDescriptor(), otherDesc.getMapValueTypeDescriptor()));
        } else {
            return true;
        }
    }

    public ResolvableType getResolvableType() {
        return resolvableType;
    }

    public TypeDescriptor getElementTypeDescriptor() {
        if (getResolvableType().isArray()) {
            return new TypeDescriptor(getResolvableType().getComponentType(), null, getAnnotations());
        }
        return getRelatedIfResolvable(this, getResolvableType().asCollection().getGeneric(0));
    }

    private TypeDescriptor getRelatedIfResolvable(TypeDescriptor source, ResolvableType type) {
        if (type.resolve() == null) {
            return null;
        }
        return new TypeDescriptor(type, null, source.getAnnotations());
    }

    private Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    private TypeDescriptor getMapValueTypeDescriptor() {
        return null;
    }

    private TypeDescriptor getMapKeyTypeDescriptor() {
        return null;
    }

    private boolean annotationsMatch(TypeDescriptor otherDesc) {
        return true;
    }

    private boolean isMap() {
        return Map.class.isAssignableFrom(getType());
    }

    private boolean isArray() {
        return getType().isArray();
    }

    private boolean isCollection() {
        return Collection.class.isAssignableFrom(getType());
    }

    public Class<?> getObjectType() {
        return ClassUtils.resolvePrimitiveIfNecessary(getType());
    }

    public TypeDescriptor narrow(Object value) {
        if (value == null) {
            return this;
        }
        ResolvableType narrowed = ResolvableType.forType(value.getClass(), getResolvableType());
        return new TypeDescriptor(narrowed, value.getClass(), getAnnotations());
    }

}
