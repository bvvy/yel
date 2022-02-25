package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exception.YelEvalException;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.exp.YelMessage;

public class OpDec extends Operator {
    private final boolean postfix;

    public OpDec(int startPos, int endPos, boolean postfix, Node ... operands) {
        super("--", startPos, endPos, operands);
        this.postfix = postfix;
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        throw new YelEvalException(YelMessage.NOT_ASSIGNABLE);
    }
}
