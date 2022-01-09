package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public interface Node {

    int getStartPosition();

    int getEndPosition();

    default Object getValue(ExpressionState state) {
        return this.getValueInternal(state).getValue();
    }

    TypedValue getValueInternal(ExpressionState state);
}
