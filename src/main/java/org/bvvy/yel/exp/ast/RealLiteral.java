package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.TypedValue;

public class RealLiteral extends Literal {
    public RealLiteral(String payload, int startPos, int endPos, double value) {
        super(payload, startPos, endPos, new TypedValue(value));
        this.exitTypeDescriptor = "D";
    }
}
