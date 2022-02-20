package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.BooleanTypeValue;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class OpOr extends Operator {
    public OpOr(int startPos, int endPos, Node ... operands) {
        super("or", startPos, endPos, operands);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        if (getBooleanValue(state, getLeftOperand())) {
            return BooleanTypeValue.TRUE;
        }
        return BooleanTypeValue.of(getBooleanValue(state, getRightOperand()));
    }

    private boolean getBooleanValue(ExpressionState state, Node operand) {
        Boolean value = (Boolean) operand.getValue(state);
        return value;
    }
}
