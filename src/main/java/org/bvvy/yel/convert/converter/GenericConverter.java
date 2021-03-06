package org.bvvy.yel.convert.converter;

import org.bvvy.yel.convert.TypeDescriptor;

import java.util.Set;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public interface GenericConverter {

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

    Set<ConvertiblePair> getConvertibleTypes();
}
