package org.bvvy.yel.convert.converter;

/**
 * @author bvvy
 * @date 2022/2/8
 */
public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
