package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

public class Literal extends NodeImpl {
    private final String originalValue;
    protected TypedValue value;

    public Literal(String originalValue, int startPos, int endPos, TypedValue value) {
        super(startPos, endPos);
        this.originalValue = originalValue;
        this.value = value;
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        return this.value;
    }
}
