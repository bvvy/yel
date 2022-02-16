package org.bvvy.yel.convert.converter;

import org.bvvy.yel.convert.TypeDescriptor;

/**
 * @author bvvy
 * @date 2022/2/16
 */
public interface ConditionalGenericConverter extends GenericConverter {

    boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
}
