package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypeDescriptor;

import java.util.List;

/**
 * @author bvvy
 * @date 2022/1/14
 */
public interface MethodResolver {

    MethodExecutor resolve(Context context, Object targetObject, String name, List<TypeDescriptor> argumentTypes);
}
