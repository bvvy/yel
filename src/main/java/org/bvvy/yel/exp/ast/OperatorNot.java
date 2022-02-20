package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.exp.BooleanTypeValue;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class OperatorNot extends NodeImpl {
    public OperatorNot(int startPos, int endPos, Node operand) {
        super(startPos, endPos, operand);
        this.exitTypeDescriptor = "Z";
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Boolean value = (Boolean) this.children[0].getValue(state);
        if (value == null) {
            throw new YelEvaluationException();
        }
        return BooleanTypeValue.of(!value);
    }
}
