package org.bvvy.yel.convert.converter;

/**
 * @author bvvy
 * @date 2022/2/8
 */
public interface Converter<S, T> {

    T convert(S source);
}
