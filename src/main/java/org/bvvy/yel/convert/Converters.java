package org.bvvy.yel.convert;

import org.bvvy.yel.convert.converter.ConvertersForPair;
import org.bvvy.yel.convert.converter.ConvertiblePair;
import org.bvvy.yel.convert.converter.GenericConverter;
import org.bvvy.yel.util.ClassUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class Converters {

    private final Set<GenericConverter> globalConverters = new HashSet<>();

    private Map<ConvertiblePair, ConvertersForPair> converters = new HashMap<>(256);


    public GenericConverter find(TypeDescriptor sourceType, TypeDescriptor targetType) {
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType.getType());
        List<Class<?>> targetCandidates = getClassHierarchy(targetType.getType());
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                ConvertiblePair convertiblePair = new ConvertiblePair(sourceCandidate, targetCandidate);
                GenericConverter converter = getRegisteredConverter(sourceType, targetType, convertiblePair);
                if (converter != null) {
                    return converter;
                }
            }

        }
        return null;
    }

    private GenericConverter getRegisteredConverter(TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertiblePair) {
        ConvertersForPair convertersForPair = this.converters.get(convertiblePair);
        if (convertersForPair != null) {
            GenericConverter converter = convertersForPair.getConverter(sourceType, targetType);
            if (converter != null) {
                return converter;
            }
        }
        return null;
    }

    private List<Class<?>> getClassHierarchy(Class<?> type) {
        List<Class<?>> hierarchy = new ArrayList<>(20);
        Set<Class<?>> visited = new HashSet<>(20);
        addToClassHierarchy(0, ClassUtils.resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
        boolean array = type.isArray();
        int i = 0;
        while (i < hierarchy.size()) {
            Class<?> candidate = hierarchy.get(i);
            candidate = (array ? candidate.getComponentType() : ClassUtils.resolvePrimitiveIfNecessary(candidate));
            Class<?> superclass = candidate.getSuperclass();
            if (superclass != null && superclass != Object.class && superclass != Enum.class) {
                addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
            }
            addInterfacesToClassHierarchy(candidate, array, hierarchy, visited);
            i++;
        }
        if (Enum.class.isAssignableFrom(type)) {
            addToClassHierarchy(hierarchy.size(), Enum.class, array, hierarchy, visited);
            addToClassHierarchy(hierarchy.size(), Enum.class, false, hierarchy, visited);
            addInterfacesToClassHierarchy(Enum.class, array, hierarchy, visited);
        }
        addToClassHierarchy(hierarchy.size(), Object.class, array, hierarchy, visited);
        addToClassHierarchy(hierarchy.size(), Object.class, false, hierarchy, visited);
        return hierarchy;
    }

    private void addInterfacesToClassHierarchy(Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
        for (Class<?> implementedInterface : type.getInterfaces()) {
            addToClassHierarchy(hierarchy.size(), implementedInterface, asArray, hierarchy, visited);
        }
    }

    private void addToClassHierarchy(int index, Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
        if (asArray) {
            type = Array.newInstance(type, 0).getClass();
        }
        if (visited.add(type)) {
            hierarchy.add(index, type);
        }

    }
}
