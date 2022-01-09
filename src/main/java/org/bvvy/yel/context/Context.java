package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypedValue;

import java.util.ArrayList;
import java.util.List;

public class Context {

    private final TypedValue rootObject;

    private List<PropertyAccessor> propertyAccessors;

    public Context(Object rootObject) {
        this.rootObject = new TypedValue(rootObject);
        this.propertyAccessors = new ArrayList<>();
        propertyAccessors.add(new MapAccessor());
    }

    public List<PropertyAccessor> getPropertyAccessors() {
        return this.propertyAccessors;
    }

    public TypedValue getRootObject() {
        return this.rootObject;
    }
}
