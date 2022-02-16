package org.bvvy.yel.context.accessor;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author bvvy
 * @date 2022/2/16
 */
public class ReflectivePropertyAccessor implements PropertyAccessor {
    private static final Set<Class<?>> ANY_TYPES = Collections.emptySet();

    private Map<PropertyCacheKey, Object> readerCache;

    @Override
    public boolean canRead(Context context, Object target, String name) {
        if (target == null) {
            return false;
        }
        Class<?> type = target instanceof Class ? ((Class<?>) target) : target.getClass();
        if (type.isArray() && name.equals("length")) {
            return true;
        }
        PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
        if (this.readerCache.containsKey(cacheKey)) {
            return true;
        }
        Method method = findGetterForProperty(name, type, target);

        return false;
    }

    private Method findGetterForProperty(String propertyName, Class<?> clazz, Object target) {
        Method method = findGetterForProperty(propertyName, clazz, target instanceof Class);
        if (method == null && target instanceof Class) {
            method = findGetterForProperty(propertyName, target.getClass(), false);
        }
        return method;
    }

    private Method findGetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
        Method method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "get", clazz, mustBeStatic, 0, ANY_TYPES);
        if (method == null) {
            method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "is", clazz, mustBeStatic, 0, ANY_TYPES);
            if (method == null) {
                method = findMethodForProperty(new String[]{propertyName}, "", clazz, mustBeStatic, 0, ANY_TYPES);
            }
        }
        return method;
    }

    private Method findMethodForProperty(String[] methodSuffixes, String prefix, Class<?> clazz,
                                         boolean mustBeStatic, int numberOfParams, Set<Class<?>> requiredReturnTypes) {
        Method[] methods = getSortedMethods(clazz);
        for (String methodSuffix : methodSuffixes) {
            for (Method method : methods) {
                if (isCandidateForProperty(method, clazz) && method.getName().equals(prefix + methodSuffix)
                        && method.getParameterCount() == numberOfParams
                        && (!mustBeStatic) || Modifier.isStatic(method.getModifiers())
                        && (requiredReturnTypes.isEmpty()) || requiredReturnTypes.contains(method.getReturnType())) {
                    return method;
                }
            }
        }
        return null;
    }

    private boolean isCandidateForProperty(Method method, Class<?> clazz) {
        return false;
    }

    private Method[] getSortedMethods(Class<?> clazz) {
        return new Method[0];
    }

    private String[] getPropertyMethodSuffixes(String propertyName) {
        String suffix = getPropertyMethodSuffix(propertyName);
        if (suffix.length() > 0 && Character.isUpperCase(suffix.charAt(0))) {
            return new String[]{suffix};
        }
        return new String[]{suffix, StringUtils.capitalize(suffix)};
    }

    private String getPropertyMethodSuffix(String propertyName) {
        if (propertyName.length() > 1 && Character.isUpperCase(propertyName.charAt(1))) {
            return propertyName;
        }
        return StringUtils.capitalize(propertyName);
    }

    @Override
    public TypedValue read(Context context, Object target, String name) {

        return null;
    }
}
