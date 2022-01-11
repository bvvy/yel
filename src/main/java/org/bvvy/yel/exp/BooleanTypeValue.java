package org.bvvy.yel.exp;

/**
 * @author bvvy
 */
public class BooleanTypeValue extends TypedValue {
    private static final BooleanTypeValue TRUE = new BooleanTypeValue(true);

    private static final BooleanTypeValue FALSE = new BooleanTypeValue(false);

    public BooleanTypeValue(boolean value) {
        super(value);
    }

    public static BooleanTypeValue of(boolean b) {
        return (b ? TRUE : FALSE);
    }
}
