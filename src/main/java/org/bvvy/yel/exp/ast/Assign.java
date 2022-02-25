package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exception.YelEvalException;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.exp.YelMessage;

public class Assign extends NodeImpl {
    public Assign(int startPos, int endPos, Node ... operands) {
        super(startPos, endPos, operands);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        throw new YelEvalException(YelMessage.NOT_ASSIGNABLE);
    }
}
