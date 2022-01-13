package org.bvvy.yel.exp;

/**
 * @author bvvy
 */
public class TypedValue {
    public static final TypedValue NULL = new TypedValue(null);

    private final Object value;

    private TypeDescriptor typeDescriptor;

    public TypedValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public TypeDescriptor getTypeDescriptor() {
        if (this.typeDescriptor == null && this.value != null) {
            this.typeDescriptor = TypeDescriptor.forObject(this.value);
        }
        return this.typeDescriptor;
    }

}
