package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.TypedValue;

/**
 * @author bvvy
 * @date 2021/11/29
 */
public class IntLiteral extends Literal {

    public IntLiteral(String payload, int startPos, int endPos, int value) {
        super(payload, startPos, endPos, new TypedValue(value));
    }
}
