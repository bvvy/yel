package org.bvvy.yel.convert.converter;

import org.bvvy.yel.convert.TypeDescriptor;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class NoOpConverter implements GenericConverter {
    private String name;

    public NoOpConverter(String name) {
        this.name = name;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return source;
    }
}
