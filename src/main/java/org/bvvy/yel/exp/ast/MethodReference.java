package org.bvvy.yel.exp.ast;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.context.method.MethodExecutor;
import org.bvvy.yel.context.method.MethodResolver;
import org.bvvy.yel.exception.YelEvalException;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.convert.TypeDescriptor;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.exp.ValueRef;
import org.bvvy.yel.exp.YelMessage;

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
        throw new YelEvalException(getStartPosition(), YelMessage.METHOD_NOT_FOUND, name);
    }

    @Override
    public ValueRef getValueRef(ExpressionState state) {
        Object[] arguments = getArguments(state);
        if (state.getActiveContextObject().getValue() == null) {
        }
        return new MethodValueRef(state, arguments);
    }

    private MethodExecutor getCachedExecutor(Context context, Object value, TypeDescriptor target, Object[] arguments) {

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


    private class MethodValueRef implements ValueRef {
        private final Object[] arguments;
        private final TypeDescriptor targetType;
        private final Object value;
        private final Context context;

        public MethodValueRef(ExpressionState state, Object[] arguments) {
            this.context = state.getContext();
            this.value = state.getActiveContextObject().getValue();
            this.targetType = state.getActiveContextObject().getTypeDescriptor();
            this.arguments = arguments;
        }

        @Override
        public TypedValue getValue() {
            TypedValue result = MethodReference.this.getValueInternal(this.context, this.value, this.targetType, this.arguments);
            return result;
        }
    }
}
