package org.bvvy.yel.convert.converter;

import org.bvvy.yel.convert.TypeDescriptor;

import java.util.Objects;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class ConverterCacheKey {
    private final TypeDescriptor sourceType;
    private final TypeDescriptor targetType;

    public ConverterCacheKey(TypeDescriptor sourceType, TypeDescriptor targetType) {

        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConverterCacheKey that = (ConverterCacheKey) o;
        return Objects.equals(sourceType, that.sourceType) && Objects.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceType, targetType);
    }

    @Override
    public String toString() {
        return "ConverterCacheKey{" +
                "sourceType=" + sourceType +
                ", targetType=" + targetType +
                '}';
    }
}
