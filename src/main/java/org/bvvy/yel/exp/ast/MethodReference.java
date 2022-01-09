package org.bvvy.yel.exp.ast;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class MethodReference extends NodeImpl {
    private final String name;
    private final boolean nullSafe;

    public MethodReference(boolean nullSafe, String methodName, int startPos, int endPos, Node ... arguments) {
        super(startPos, endPos, arguments);
        this.name = methodName;
        this.nullSafe = nullSafe;
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Context context = state.getContext();
        Object[] arguments = getArguments(state);
        return null;
    }

    public Object[] getArguments(ExpressionState state) {
        Object[] arguments = new Object[getChildCount()];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = children[i].getValueInternal(state);
        }
        return arguments;
    }


}
