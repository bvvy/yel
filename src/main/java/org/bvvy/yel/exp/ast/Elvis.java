package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class Elvis extends NodeImpl {
    public Elvis(int startPos, int endPos, Node ... operands) {
        super(startPos, endPos, operands);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        TypedValue value = this.children[0].getValueInternal(state);
        if (value.getValue() != null && !"".equals(value.getValue())) {
            return value;
        } else {
            TypedValue result = this.children[1].getValueInternal(state);
            return result;
        }
    }
}
