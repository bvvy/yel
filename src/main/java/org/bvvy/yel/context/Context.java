package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypedValue;

import java.util.ArrayList;
import java.util.List;

public class Context {

    private final TypedValue rootObject;

    private List<PropertyAccessor> propertyAccessors;

    private TypeComparator typeComparator = new StandardTypeComparator();

    private List<MethodResolver> methodResolvers;

    private TypeConverter typeConverter;

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

    public TypeComparator getTypeComparator() {
        return typeComparator;
    }

    public List<MethodResolver> getMethodResolvers() {
        return initMethodResolvers();
    }

    private List<MethodResolver> initMethodResolvers() {
        List<MethodResolver> resolvers = this.methodResolvers;
        if (resolvers == null) {
            resolvers = new ArrayList<>(1);
            resolvers.add(new ReflectiveMethodResolver());
            this.methodResolvers = resolvers;
        }
        return resolvers;
    }

    public TypeConverter getTypeConverter() {
        if (this.typeConverter == null) {
            this.typeConverter = new StandardTypeConverter();
        }
        return typeConverter;
    }
}
