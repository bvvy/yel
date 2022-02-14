package org.bvvy.yel.function;

/**
 * @author bvvy
 * @date 2022/2/14
 */
public interface Function2<T1, T2, R> extends YelFunction {

    R apply(T1 t1, T2 t2);
}
