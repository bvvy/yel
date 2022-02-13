package org.bvvy.yel.exp.ast;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.context.MethodExecutor;
import org.bvvy.yel.context.MethodResolver;
import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.exp.TypedValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MethodReference extends NodeImpl {
    private final String name;
    private final boolean nullSafe;
    private MethodExecutor cachedExecutor;

    public MethodReference(boolean nullSafe, String methodName, int startPos, int endPos, Node ... arguments) {
        super(startPos, endPos, arguments);
        this.name = methodName;
        this.nullSafe = nullSafe;
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Context context = state.getContext();
        Object value = state.getActiveContextObject().getValue();
        TypeDescriptor targetType = state.getActiveContextObject().getTypeDescriptor();
        Object[] arguments = getArguments(state);
        TypedValue result = getValueInternal(context, value, targetType, arguments);
        return result;
    }

    private TypedValue getValueInternal(Context context, Object value, TypeDescriptor targetType, Object[] arguments) {
        List<TypeDescriptor> argumentTypes = getArgumentTypes(arguments);
        if (value == null) {
            return TypedValue.NULL;
        }
        MethodExecutor executorToUse = getCachedExecutor(context, value, targetType, arguments);
        if (executorToUse != null) {
            return executorToUse.execute(context, value, arguments);
        }
        executorToUse = findAccessorForMethod(argumentTypes, value, context);
        this.cachedExecutor = executorToUse;
        return executorToUse.execute(context, value, arguments);
    }

    private MethodExecutor findAccessorForMethod(List<TypeDescriptor> argumentTypes, Object targetObject, Context context) {
        List<MethodResolver> methodResolvers = context.getMethodResolvers();
        for (MethodResolver methodResolver : methodResolvers) {
            MethodExecutor methodExecutor = methodResolver.resolve(context, targetObject, this.name, argumentTypes);
            if (methodExecutor != null) {
                return methodExecutor;
            }
        }
        throw new YelEvaluationException();
    }

    private MethodExecutor getCachedExecutor(Context context, Object value, TypeDescriptor targetType, Object[] arguments) {
        return null;
    }

    private List<TypeDescriptor> getArgumentTypes(Object ... arguments) {
        List<TypeDescriptor> descriptors = new ArrayList<>(arguments.length);
        for (Object argument : arguments) {
            descriptors.add(TypeDescriptor.forObject(argument));
        }
        return Collections.unmodifiableList(descriptors);
    }

    public Object[] getArguments(ExpressionState state) {
        Object[] arguments = new Object[getChildCount()];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = children[i].getValueInternal(state).getValue();
        }
        return arguments;
    }


}
