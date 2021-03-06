package org.bvvy.yel.convert.support;

import org.bvvy.yel.convert.ConditionalConverter;
import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.convert.converter.Converter;
import org.bvvy.yel.convert.converter.ConverterFactory;
import org.bvvy.yel.util.NumberUtils;

/**
 * @author bvvy
 * @date 2022/2/8
 */
public class NumberToNumberConverterFactory implements ConverterFactory<Number, Number>, ConditionalConverter {
    @Override
    public <T extends Number> Converter<Number, T> getConverter(Class<T> targetType) {
        return new NumberToNumber<>(targetType);
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return !sourceType.equals(targetType);
    }

    private static final class NumberToNumber<T extends Number> implements Converter<Number, T> {

        private final Class<T> targetType;

        public NumberToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(Number source) {
            return NumberUtils.convertNumberToTargetClass(source, this.targetType);
        }
    }
}
