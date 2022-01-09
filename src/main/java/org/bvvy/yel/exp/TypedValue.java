package org.bvvy.yel.exp;

/**
 * @author bvvy
 */
public class TypedValue {
    public static final TypedValue NULL = new TypedValue(null);

    private final Object value;

    public TypedValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }
}
