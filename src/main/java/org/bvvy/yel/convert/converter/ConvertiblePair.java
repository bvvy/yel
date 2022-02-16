package org.bvvy.yel.convert.converter;

import java.util.Objects;

/**
 * @author bvvy
 * @date 2022/2/7
 */
public class ConvertiblePair {
    private Class<?> sourceType;
    private Class<?> targetType;

    public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    public Class<?> getSourceType() {
        return sourceType;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvertiblePair that = (ConvertiblePair) o;
        return sourceType.equals(that.sourceType) && targetType.equals(that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceType, targetType);
    }

    @Override
    public String toString() {
        return this.sourceType.getName() + " -> " + this.targetType.getName();
    }

}
