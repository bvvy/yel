package org.bvvy.yel.convert;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public interface ConversionService {

    boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);
}
