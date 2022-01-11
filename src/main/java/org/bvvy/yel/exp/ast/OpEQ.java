package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.BooleanTypeValue;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class OpEQ extends Operator {
    public OpEQ(int startPos, int endPos, Node... operand) {
        super("==", startPos, endPos, operand);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Object left = getLeftOperand().getValueInternal(state).getValue();
        Object right = getRightOperand().getValueInternal(state).getValue();
        return BooleanTypeValue.of(equalityCheck(state.getContext(), left, right));
    }


}
