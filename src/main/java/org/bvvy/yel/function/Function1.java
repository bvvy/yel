package org.bvvy.yel.function;

/**
 * @author bvvy
 * @date 2022/2/14
 */
public interface Function1<T, R> extends YelFunction {
    R apply(T t);


}
