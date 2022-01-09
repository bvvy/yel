package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.exp.ValueRef;

public class CompoundExpression extends NodeImpl {
    public CompoundExpression(int startPos, int endPos, Node... expressionComponents) {
        super(startPos, endPos, expressionComponents);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        ValueRef ref = getValueRef(state);
        TypedValue result = ref.getValue();
        return result;
    }

    @Override
    public ValueRef getValueRef(ExpressionState state) {
        if (getChildCount() == 1) {
            return this.children[0].getValueRef(state);
        }
        Node nextNode = this.children[0];
        TypedValue result = nextNode.getValueInternal(state);
        int cc = getChildCount();
        for (int i = 1; i < cc - 1; i++) {
            try {
                state.pushActiveContextObject(result);
                nextNode = this.children[i];
                result = nextNode.getValueInternal(state);
            } finally {
                state.popActiveContextObject();
            }
        }
        try {
            state.pushActiveContextObject(result);
            nextNode = this.children[cc - 1];
            return nextNode.getValueRef(state);
        } finally {
            state.popActiveContextObject();
        }
    }
}
