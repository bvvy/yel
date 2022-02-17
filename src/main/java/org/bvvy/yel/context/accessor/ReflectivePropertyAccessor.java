package org.bvvy.yel.context.accessor;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.convert.Property;
import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.exception.AccessException;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.ClassUtils;
import org.bvvy.yel.util.ReflectionUtils;
import org.bvvy.yel.util.StringUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author bvvy
 * @date 2022/2/16
 */
public class ReflectivePropertyAccessor implements PropertyAccessor {
    private static final Set<Class<?>> ANY_TYPES = Collections.emptySet();

    private Map<PropertyCacheKey, InvokerPair> readerCache = new HashMap<>(64);
    private Map<Class<?>, Method[]> sortedMethodsCache = new HashMap<>(64);
    private Map<PropertyCacheKey, TypeDescriptor> typeDescriptorCache = new HashMap<>(64);
    private InvokerPair lastReadInvokerPair;

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
        if (method != null) {
            Property property = new Property(type, method, null);
            TypeDescriptor typeDescriptor = new TypeDescriptor(property);
            method = ClassUtils.getInterfaceMethodIfPossible(method);
            this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
            this.typeDescriptorCache.put(cacheKey, typeDescriptor);
            return true;
        } else {
            Field field = findField(name, type, target);
            if (field != null) {
                TypeDescriptor typeDescriptor = new TypeDescriptor(field);
                this.readerCache.put(cacheKey, new InvokerPair(field, typeDescriptor));
                this.typeDescriptorCache.put(cacheKey, typeDescriptor);
                return true;
            }
        }
        return false;
    }

    private Field findField(String name, Class<?> clazz, Object target) {
        Field field = findField(name, clazz, target instanceof Class);
        if (field == null || target instanceof Class) {
            field = findField(name, target.getClass(), false);
        }
        return field;
    }

    private Field findField(String name, Class<?> clazz, boolean mustBeStatic) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.getName().equals(name)
                    && (!mustBeStatic || Modifier.isStatic(field.getModifiers()))) {
                return field;
            }
        }
        if (clazz.getSuperclass() != null) {
            Field field = findField(name, clazz.getSuperclass(), mustBeStatic);
            if (field != null) {
                return field;
            }
        }
        for (Class<?> implementedInterface : clazz.getInterfaces()) {
            Field field = findField(name, implementedInterface, mustBeStatic);
            if (field != null) {
                return field;
            }
        }
        return null;
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
        return true;
    }

    private Method[] getSortedMethods(Class<?> clazz) {
        return this.sortedMethodsCache.computeIfAbsent(clazz, key -> {
            Method[] methods = key.getMethods();
            Arrays.sort(methods, (o1, o2) -> o1.isBridge() == o2.isBridge() ? 0 : o1.isBridge() ? 1 : -1);
            return methods;
        });
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
        Class<?> type = target instanceof Class ? ((Class<?>) target) : target.getClass();
        if (type.isArray() && name.equals("length")) {
            if (target instanceof Class) {
                throw new AccessException("Cannot access length on array class itself");
            }
            return new TypedValue(Array.getLength(target));
        }
        PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
        InvokerPair invoker = this.readerCache.get(cacheKey);
        this.lastReadInvokerPair = invoker;
        if (invoker == null || invoker.member instanceof Method) {
            Method method = invoker != null ? ((Method) invoker.member) : null;
            if (method == null) {
                method = findGetterForProperty(name, type, target);
                if (method != null) {
                    Property property = new Property(type, method, null);
                    TypeDescriptor typeDescriptor = new TypeDescriptor(property);
                    method = ClassUtils.getInterfaceMethodIfPossible(method);
                    invoker = new InvokerPair(method, typeDescriptor);
                    this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
                    this.lastReadInvokerPair = invoker;
                }
            }
            if (method != null) {
                try {
                    ReflectionUtils.makeAccessible(method);
                    Object value = method.invoke(target);
                    return new TypedValue(value, invoker.typeDescriptor.narrow(value));
                } catch (Exception e) {
                    throw new AccessException("Unable to access property '" + name + "' through getter method", e);
                }
            }
        }
        if (invoker == null || invoker.member instanceof Field) {
            Field field = invoker != null ? ((Field) invoker.member) : null;
            if (field == null) {
                field = findField(name, type, target);
                if (field != null) {
                    invoker = new InvokerPair(field, new TypeDescriptor(field));
                    this.lastReadInvokerPair = invoker;
                    this.readerCache.put(cacheKey, invoker);
                }
            }
            if (field != null) {
                try {
                    ReflectionUtils.makeAccessible(field);
                    Object value = field.get(target);
                    return new TypedValue(value, invoker.typeDescriptor.narrow(value));
                } catch (IllegalAccessException e) {
                    throw new AccessException("Unable to access field '" + name + "'", e);
                }
            }
        }
        throw new AccessException("Neigher getter method nor field found for property '" + name + "'");
    }

    private static class InvokerPair {
        private Member member;
        private TypeDescriptor typeDescriptor;

        public InvokerPair(Member member, TypeDescriptor typeDescriptor) {
            this.member = member;
            this.typeDescriptor = typeDescriptor;
        }
    }

    private static class PropertyCacheKey {
        private final Class<?> clazz;
        private final String name;
        private final boolean targetIsClass;

        public PropertyCacheKey(Class<?> clazz, String name, boolean targetIsClass) {

            this.clazz = clazz;
            this.name = name;
            this.targetIsClass = targetIsClass;
        }


    }

}
