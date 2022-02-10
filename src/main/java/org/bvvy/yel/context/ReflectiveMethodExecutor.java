package org.bvvy.yel.context;

import org.bvvy.yel.exp.TypedValue;

import java.lang.reflect.Method;

public class ReflectiveMethodExecutor implements MethodExecutor {
    private final Method originalMethod;
    private final Integer varargsPosition;
    private boolean argumentConversionOccurred;

    public ReflectiveMethodExecutor(Method method) {

        this.originalMethod = method;
        if (method.isVarArgs()) {
            this.varargsPosition = method.getParameterCount() - 1;
        } else {
            this.varargsPosition = null;
        }

    }

    @Override
    public TypedValue execute(Context context, Object value, Object... arguments) {
        this.argumentConversionOccurred = ReflectionHelper.convertArguments(
                context.getTypeConverter(), arguments, this.originalMethod, this.varargsPosition
        );
        if (this.originalMethod.isVarArgs()) {
            arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(
                    this.originalMethod.getParameterTypes(), arguments
            );
        }

        return null;
    }
}
