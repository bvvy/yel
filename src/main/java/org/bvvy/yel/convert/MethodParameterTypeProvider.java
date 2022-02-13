package org.bvvy.yel.convert;

import java.lang.reflect.Type;

public class MethodParameterTypeProvider implements TypeProvider {


    private final String methodName;
    private final Class<?>[] parameterTypes;
    private final Class<?> declaringClass;
    private final int parameterIndex;
    private final MethodParameter methodParameter;

    public MethodParameterTypeProvider(MethodParameter methodParameter) {
        this.methodName = (methodParameter.getMethod() != null ? methodParameter.getMethod().getName() : null);
        this.parameterTypes = methodParameter.getExecutable().getParameterTypes();
        this.declaringClass = methodParameter.getDeclaringClass();
        this.parameterIndex = methodParameter.getParameterIndex();
        this.methodParameter = methodParameter;
    }

    @Override
    public Type getType() {
        return this.methodParameter.getGenericParameterType();
    }

}
