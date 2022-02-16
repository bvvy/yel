package org.bvvy.yel.context.method;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.convert.TypeDescriptor;

import java.util.List;

/**
 * @author bvvy
 * @date 2022/1/14
 */
public interface MethodResolver {

    MethodExecutor resolve(Context context, Object targetObject, String name, List<TypeDescriptor> argumentTypes);
}
