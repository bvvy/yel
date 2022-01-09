package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.TypedValue;

public class BooleanLiteral extends Literal {
    public BooleanLiteral(String payload, int startPos, int endPos, boolean value) {
        super(payload, startPos, endPos, new TypedValue(value));
    }
}
