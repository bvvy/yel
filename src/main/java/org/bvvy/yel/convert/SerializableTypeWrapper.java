package org.bvvy.yel.convert;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bvvy
 * @date 2022/2/8
 */
public class SerializableTypeWrapper {

    public static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = {
            GenericArrayType.class, TypeVariable.class, WildcardType.class, ParameterizedType.class
    };
    private static Map<Type, Type> cache = new HashMap<>();

    public static Type forTypeProvider(TypeProvider provider) {
        Type providedType = provider.getType();
        if (providedType == null || providedType instanceof Serializable) {
            return providedType;
        }
        Type cached = cache.get(providedType);
        if (cached != null) {
            return cached;
        }
        for (Class<?> type : SUPPORTED_SERIALIZABLE_TYPES) {
            if (type.isInstance(providedType)) {
                ClassLoader classLoader = provider.getClass().getClassLoader();
                Class<?> []interfaces = new Class<?>[]{type, Serializable.class};
            }
        }
        throw new IllegalArgumentException("Unsupported Type class: " + providedType.getClass().getName());
    }
}
