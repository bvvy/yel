package org.bvvy.yel.exp.ast;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class Operator extends NodeImpl {

    private final String operatorName;

    public Operator(String payload, int startPos, int endPos, Node ... operands) {
        super(startPos, endPos, operands);
        this.operatorName = payload;
    }

    public Node getLeftOperand() {
        return this.children[0];
    }

    public Node getRightOperand() {
        return this.children[1];
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        return null;
    }

    public boolean equalityCheck(Context context, Object left, Object right) {
        return false;
    }
}
