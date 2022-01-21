package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.Type;

/**
 * @author bvvy
 * @date 2021/11/29
 */
public class LongLiteral extends Literal {
    public LongLiteral(String payload, int startPos, int endPos, long value) {
        super(payload, startPos, endPos, new TypedValue(value));
        this.exitType = Type.LONG_TYPE;
    }
}
