package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.TypedValue;

public class FloatLiteral extends Literal {
    public FloatLiteral(String payload, int startPos, int endPos, float value) {
        super(payload, startPos, endPos, new TypedValue(value));
        this.exitTypeDescriptor = "F";
    }
}
