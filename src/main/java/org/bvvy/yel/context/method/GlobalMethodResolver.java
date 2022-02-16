package org.bvvy.yel.context.method;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.convert.MethodParameter;
import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

public class GlobalMethodResolver implements MethodResolver {

    private final Map<String, YelFunction> globalFunctions = new HashMap<>();
    private final boolean useDistance;

    public GlobalMethodResolver() {
        this.useDistance = true;
    }

    @Override
    public MethodExecutor resolve(Context context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) {

        YelFunction yelFunction = globalFunctions.get(name);
        if (yelFunction == null) {
            throw new YelEvaluationException();
        }
        TypeConverter typeConverter = context.getTypeConverter();
        Class<?> type = yelFunction.getClass();
        List<Method> methods = new ArrayList<>(Arrays.asList(type.getMethods()));
        if (methods.size() > 1) {
            methods.sort((m1, m2) -> {
                int m1pc = m1.getParameterCount();
                int m2pc = m2.getParameterCount();
                if (m1pc == m2pc) {
                    if (!m1.isVarArgs() && m2.isVarArgs()) {
                        return -1;
                    } else if (m1.isVarArgs() && !m2.isVarArgs()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
                return Integer.compare(m1pc, m2pc);
            });
        }
        Set<Method> methodsToIterate = new LinkedHashSet<>(methods);

        Method closeMatch = null;
        int closeMatchDistance = Integer.MAX_VALUE;
        Method matchRequiringConversion = null;
        boolean multipleOptions = false;

        for (Method method : methodsToIterate) {
            if (method.getName().equalsIgnoreCase(type.getName())
                    || method.isAnnotationPresent(GlobalFunction.class)) {
                int paramCount = method.getParameterCount();
                List<TypeDescriptor> paramDescriptors = new ArrayList<>(paramCount);
                for (int i = 0; i < paramCount; i++) {
                    paramDescriptors.add(new TypeDescriptor(new MethodParameter(method, i)));
                }
                ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
                if (method.isVarArgs() && argumentTypes.size() >= (paramCount - 1)) {
                    matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
                } else if (paramCount == argumentTypes.size()) {
                    matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
                }
                if (matchInfo != null) {
                    if (matchInfo.isExactMatch()) {
                        return new GlobalMethodExecutor(yelFunction, method);
                    } else if (matchInfo.isCloseMatch()) {
                        if (this.useDistance) {
                            int matchDistance = ReflectionHelper.getTypeDifferenceWeight(paramDescriptors, argumentTypes);
                            if (closeMatch == null || matchDistance < closeMatchDistance) {
                                closeMatch = method;
                                closeMatchDistance = matchDistance;
                            }
                        } else {
                            if (closeMatch == null) {
                                closeMatch = method;
                            }
                        }
                    } else if (matchInfo.isMatchRequiringConversion()) {
                        if (matchRequiringConversion != null) {
                            multipleOptions = true;
                        }
                        matchRequiringConversion = method;
                    }
                }
            }
        }
        if (closeMatch != null) {
            return new GlobalMethodExecutor(yelFunction, closeMatch);
        } else if (matchRequiringConversion != null) {
            if (multipleOptions) {
                throw new YelEvaluationException();
            }
            return new GlobalMethodExecutor(yelFunction, matchRequiringConversion);
        } else {
            return null;
        }
    }

    public void registerFunction(String name, YelFunction function) {
        if (StringUtils.hasText(name)) {
            this.globalFunctions.put(name, function);
        } else {
            throw new IllegalArgumentException("name can not empty");
        }
    }


}
