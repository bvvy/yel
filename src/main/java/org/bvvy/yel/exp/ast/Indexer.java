package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.exp.ValueRef;

import java.util.Map;

public class Indexer extends NodeImpl {
    public Indexer(int startPos, int endPos, Node expr) {
        super(startPos, endPos, expr);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        return getValueRef(state).getValue();
    }

    @Override
    public ValueRef getValueRef(ExpressionState state) {
        TypedValue context = state.getActiveContextObject();
        Object target = context.getValue();
        if (target == null) {
            throw new YelEvaluationException();
        }

        if (target instanceof Map) {

        }
        return null;
    }


}
