package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.TypedValue;

public class NullLiteral extends Literal {
    public NullLiteral(int startPos, int endPos) {
        super(null, startPos, endPos, TypedValue.NULL);
    }
}
