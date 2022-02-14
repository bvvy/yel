package org.bvvy.yel.context;

import org.bvvy.yel.convert.MethodParameter;
import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.exception.AccessException;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.ClassUtils;

import java.lang.reflect.Method;

public class ReflectiveMethodExecutor implements MethodExecutor {
    private final Method originalMethod;
    private final Integer varargsPosition;
    private final Method methodToInvoke;
    private boolean argumentConversionOccurred;

    public ReflectiveMethodExecutor(Method method) {
        this.originalMethod = method;
        this.methodToInvoke = ClassUtils.getInterfaceMethodIfPossible(method);
        if (method.isVarArgs()) {
            this.varargsPosition = method.getParameterCount() - 1;
        } else {
            this.varargsPosition = null;
        }

    }

    @Override
    public TypedValue execute(Context context, Object target, Object... arguments) {
        try {
            this.argumentConversionOccurred = ReflectionHelper.convertArguments(
                    context.getTypeConverter(), arguments, this.originalMethod, this.varargsPosition
            );
            if (this.originalMethod.isVarArgs()) {
                arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(
                        this.originalMethod.getParameterTypes(), arguments
                );
            }
            Object value = this.methodToInvoke.invoke(target, arguments);
            return new TypedValue(value, new TypeDescriptor(new MethodParameter(this.originalMethod, -1)).narrow(value));
        } catch (Exception e) {
            throw new AccessException("Problem invoking method: " + this.methodToInvoke, e);
        }
    }
}
