package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.TypedValue;

import java.math.BigDecimal;

/**
 * @author bvvy
 */
public class DecimalLiteral extends Literal {
    public DecimalLiteral(String payload, int startPos, int endPos, BigDecimal value) {
        super(payload, startPos, endPos, new TypedValue(value));
        this.exitTypeDescriptor = "Ljava/math/BigDecimal"
    }
}
