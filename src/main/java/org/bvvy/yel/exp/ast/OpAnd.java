package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.BooleanTypeValue;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class OpAnd extends Operator {
    public OpAnd(int startPos, int endPos, Node ... operands) {
        super("and", startPos, endPos, operands);
        this.exitTypeDescriptor = "Z";
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        if (!getBooleanValue(state, getLeftOperand())) {
            return BooleanTypeValue.FALSE;
        }
        return BooleanTypeValue.of(getBooleanValue(state, getRightOperand()));
    }

    private boolean getBooleanValue(ExpressionState state, Node operand) {
        Boolean value = (Boolean) operand.getValue(state);
        return value;
    }
}
