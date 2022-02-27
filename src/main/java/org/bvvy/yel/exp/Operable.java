package org.bvvy.yel.exp;

public interface Operable {

    Operable add(Operable operable);

    Operable negate();

    Operable subtract(Operable operable);


    Operable multiply(Operable operable);

    Operable divide(Operable operable);
}
