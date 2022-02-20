package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class OpDec extends Operator {
    private final boolean postfix;

    public OpDec(int startPos, int endPos, boolean postfix, Node ... operands) {
        super("--", startPos, endPos, operands);
        this.postfix = postfix;
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        throw new YelEvaluationException();
    }
}
