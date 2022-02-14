package org.bvvy.yel.exp;

import org.bvvy.yel.convert.TypeDescriptor;

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

    public TypedValue(Object value, TypeDescriptor typeDescriptor) {
        this.value = value;
        this.typeDescriptor = typeDescriptor;
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
