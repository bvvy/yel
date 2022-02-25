package org.bvvy.yel.context;

import org.bvvy.yel.context.accessor.PropertyAccessor;
import org.bvvy.yel.context.comparator.TypeComparator;
import org.bvvy.yel.context.method.MethodResolver;
import org.bvvy.yel.context.method.TypeConverter;
import org.bvvy.yel.exp.TypedValue;

import java.util.List;

public interface Context {


    List<PropertyAccessor> getPropertyAccessors();

    TypedValue getRootObject();

    TypeComparator getTypeComparator();

    List<MethodResolver> getMethodResolvers();

    TypeConverter getTypeConverter();
}
