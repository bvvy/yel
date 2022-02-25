package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exception.YelEvalException;
import org.bvvy.yel.exp.BooleanTypeValue;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.exp.YelMessage;

public class OperatorNot extends NodeImpl {
    public OperatorNot(int startPos, int endPos, Node operand) {
        super(startPos, endPos, operand);
        this.exitTypeDescriptor = "Z";
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Boolean value = (Boolean) this.children[0].getValue(state);
        if (value == null) {
            throw new YelEvalException(YelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES);
        }
        return BooleanTypeValue.of(!value);
    }
}
