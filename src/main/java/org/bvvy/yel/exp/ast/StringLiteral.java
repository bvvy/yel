package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.TypedValue;

public class StringLiteral extends Literal {

    public StringLiteral(String payload, int startPos, int endPos, String value) {
        super(payload, startPos, endPos, new TypedValue(value));
    }
}
