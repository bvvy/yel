package org.bvvy.yel.context;

import org.bvvy.yel.convert.MethodParameter;
import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.exception.YelEvaluationException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ReflectiveMethodResolver implements MethodResolver {

    private final boolean useDistance;

    public ReflectiveMethodResolver() {
        this.useDistance = true;
    }

    @Override
    public MethodExecutor resolve(Context context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) {
        TypeConverter typeConverter = context.getTypeConverter();
        Class<?> type = targetObject instanceof Class ? (Class<?>) targetObject : targetObject.getClass();
        List<Method> methods = new ArrayList<>(getMethods(type, targetObject));
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
            if (method.getName().equals(name)) {
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
                        return new ReflectiveMethodExecutor(method);
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
            return new ReflectiveMethodExecutor(closeMatch);
        } else if (matchRequiringConversion != null) {
            if (multipleOptions) {
                throw new YelEvaluationException();
            }
            return new ReflectiveMethodExecutor(matchRequiringConversion);
        } else {
            return null;
        }
    }

    private Set<Method> getMethods(Class<?> type, Object targetObject) {
        if (targetObject instanceof Class) {
            Set<Method> result = new LinkedHashSet<>();
            Method[] methods = type.getMethods();
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers())) {
                    result.add(method);
                }
            }
            Collections.addAll(result, Class.class.getMethods());
            return result;
        } else {
            Method[] methods = type.getMethods();
            return new LinkedHashSet<>(Arrays.asList(methods));
        }
    }


}
