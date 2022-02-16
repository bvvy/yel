package org.bvvy.yel.convert.converter;

import org.bvvy.yel.convert.TypeDescriptor;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class ConvertersForPair {

    private final Deque<GenericConverter> converters = new ConcurrentLinkedDeque<>();

    public GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
        for (GenericConverter converter : converters) {
            if (!(converter instanceof ConditionalGenericConverter) ||
                    ((ConditionalGenericConverter) converter).matches(sourceType, targetType)) {
                return converter;
            }
        }
        return null;
    }

    public void add(GenericConverter converter) {
        this.converters.addFirst(converter);
    }
}
