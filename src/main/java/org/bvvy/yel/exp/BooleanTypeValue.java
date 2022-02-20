package org.bvvy.yel.exp;

/**
 * @author bvvy
 */
public class BooleanTypeValue extends TypedValue {
    public
    static final BooleanTypeValue TRUE = new BooleanTypeValue(true);

    public
    static final BooleanTypeValue FALSE = new BooleanTypeValue(false);

    public BooleanTypeValue(boolean value) {
        super(value);
    }

    public static BooleanTypeValue of(boolean b) {
        return (b ? TRUE : FALSE);
    }
}
