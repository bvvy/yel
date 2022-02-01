package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypeDescriptor;

import java.util.List;

public class GlobalMethodResolver implements MethodResolver {



    @Override
    public MethodExecutor resolve(Context context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) {
        return null;
    }
}
