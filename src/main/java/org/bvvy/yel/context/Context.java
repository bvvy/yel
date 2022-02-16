package org.bvvy.yel.context;

import org.bvvy.yel.context.accessor.MapAccessor;
import org.bvvy.yel.context.accessor.PropertyAccessor;
import org.bvvy.yel.context.comparator.StandardTypeComparator;
import org.bvvy.yel.context.comparator.TypeComparator;
import org.bvvy.yel.context.method.MethodResolver;
import org.bvvy.yel.context.method.ReflectiveMethodResolver;
import org.bvvy.yel.context.method.StandardTypeConverter;
import org.bvvy.yel.context.method.TypeConverter;
import org.bvvy.yel.exp.TypedValue;

import java.util.ArrayList;
import java.util.List;

public class Context {

    private TypedValue rootObject;

    private List<PropertyAccessor> propertyAccessors;

    private TypeComparator typeComparator = new StandardTypeComparator();

    private List<MethodResolver> methodResolvers;

    private TypeConverter typeConverter;

    public Context(Object rootObject) {
        this.rootObject = new TypedValue(rootObject);
    }

    public Context() {
        this.rootObject = TypedValue.NULL;
    }

    public void setMethodResolvers(List<MethodResolver> methodResolvers) {
        this.methodResolvers = methodResolvers;
    }

    public void setRootObject(Object rootObject) {
        this.rootObject = rootObject == null ? TypedValue.NULL : new TypedValue(rootObject);
    }

    public List<PropertyAccessor> getPropertyAccessors() {
        if (this.propertyAccessors == null) {
            this.propertyAccessors = new ArrayList<>();
            propertyAccessors.add(new MapAccessor());
        }
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
