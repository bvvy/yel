package org.bvvy.yel.context;

import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.function.YelFunction;
import org.bvvy.yel.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalMethodResolver implements MethodResolver {

    private final Map<String, MethodExecutor> methodExecutors = new HashMap<>();

    @Override
    public MethodExecutor resolve(Context context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) {

        return methodExecutors.get(name);
    }

    public void registerFunction(String name, YelFunction function) {
        if (StringUtils.hasText(name)) {
            MethodExecutor executor = parse(function);
            methodExecutors.put(name, executor);
        } else {
            throw new IllegalArgumentException("name can not empty");
        }
    }

    private MethodExecutor parse(YelFunction function) {

        System.out.println(Arrays.toString(function.getClass().getMethods()));

        return null;
    }

}
