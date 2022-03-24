package org.bvvy.yel.context.overloader;

public interface OperatorOverloader {

    Object add(Object left, Object right);

    Object minus(Object left, Object right);

    Object mod(Object left, Object right);

    Object multiply(Object left, Object right);

    Object divide(Object left, Object right);
}
