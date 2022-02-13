package org.bvvy.yel.convert;

import org.bvvy.yel.util.ClassUtils;
import org.bvvy.yel.util.ObjectUtils;

import java.lang.annotation.Annotation;
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

    public boolean isAssignableTo(TypeDescriptor expectedArg) {
        return false;
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
        return null;
    }

    private TypeDescriptor getMapValueTypeDescriptor() {
        return null;
    }

    private Object getMapKeyTypeDescriptor() {
        return null;
    }

    private boolean annotationsMatch(TypeDescriptor otherDesc) {
        return false;
    }

    private boolean isMap() {
        return false;
    }

    private boolean isArray() {
        return false;
    }

    private boolean isCollection() {
        return false;
    }

    public Class<?> getObjectType() {
        return ClassUtils.resolvePrimitiveIfNecessary(getType());
    }

}
