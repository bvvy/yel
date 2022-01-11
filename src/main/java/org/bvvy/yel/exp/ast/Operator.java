package org.bvvy.yel.exp.ast;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class Operator extends NodeImpl {

    private final String operatorName;

    public Operator(String payload, int startPos, int endPos, Node... operands) {
        super(startPos, endPos, operands);
        this.operatorName = payload;
    }

    public Node getLeftOperand() {
        return this.children[0];
    }

    public Node getRightOperand() {
        return this.children[1];
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        return null;
    }

    public boolean equalityCheck(Context context, Object left, Object right) {
        if (left instanceof Number || right instanceof Number) {
            Number leftNumber = (Number) left;
            Number rightNumber = (Number) right;
            if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
                BigDecimal leftBigDecimal = NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                return leftBigDecimal.compareTo(rightBigDecimal) == 0;
            } else if (leftNumber instanceof Double || rightNumber instanceof Double) {
                return leftNumber.doubleValue() == rightNumber.doubleValue();
            } else if (leftNumber instanceof Float || rightNumber instanceof Float) {
                return leftNumber.floatValue() == rightNumber.floatValue();
            } else if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return leftBigInteger.compareTo(rightBigInteger) == 0;
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                return leftNumber.longValue() == rightNumber.longValue();
            } else if (leftNumber instanceof Integer || rightNumber instanceof Integer) {
                return leftNumber.intValue() == rightNumber.intValue();
            } else if (leftNumber instanceof Short || rightNumber instanceof Short) {
                return leftNumber.shortValue() == rightNumber.shortValue();
            } else if (leftNumber instanceof Byte || rightNumber instanceof Byte) {
                return leftNumber.byteValue() == rightNumber.byteValue();
            } else {
                return leftNumber.doubleValue() == rightNumber.doubleValue();
            }
        }
        if (left instanceof CharSequence && right instanceof CharSequence) {
            return left.toString().equals(right.toString());
        }
        if (left instanceof Boolean && right instanceof Boolean) {
            return left.equals(right);
        }
        return Objects.equals(left, right);
    }

}
