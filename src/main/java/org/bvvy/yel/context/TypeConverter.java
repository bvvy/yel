package org.bvvy.yel.context;

import org.bvvy.yel.convert.TypeDescriptor;

public interface TypeConverter {
    boolean canConvert(TypeDescriptor suppliedArg, TypeDescriptor expectedArg);

    Object convertValue(Object value, TypeDescriptor sourceType, TypeDescriptor targetType);
}
