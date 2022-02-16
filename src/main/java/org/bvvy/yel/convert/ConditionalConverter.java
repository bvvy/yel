package org.bvvy.yel.convert;

/**
 * @author bvvy
 * @date 2022/2/16
 */
public interface ConditionalConverter {

    boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
}
