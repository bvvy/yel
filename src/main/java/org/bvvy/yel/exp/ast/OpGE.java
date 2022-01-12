package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.BooleanTypeValue;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class OpGE extends Operator {
    public OpGE(int startPos, int endPos, Node... operand) {
        super(">=", startPos, endPos, operand);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Object left = getLeftOperand().getValueInternal(state).getValue();
        Object right = getRightOperand().getValueInternal(state).getValue();
        if (left instanceof Number && right instanceof Number) {
            Number leftNumber = (Number) left;
            Number rightNumber = (Number) right;
            if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
                BigDecimal leftBigDecimal = NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                return BooleanTypeValue.of(leftBigDecimal.compareTo(rightBigDecimal) == 0);
            } else if (leftNumber instanceof Double || rightNumber instanceof Double) {
                return BooleanTypeValue.of(leftNumber.doubleValue() == rightNumber.doubleValue());
            } else if (leftNumber instanceof Float || rightNumber instanceof Float) {
                return BooleanTypeValue.of(leftNumber.floatValue() == rightNumber.floatValue());
            } else if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return BooleanTypeValue.of(leftBigInteger.compareTo(rightBigInteger) == 0);
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                return BooleanTypeValue.of(leftNumber.longValue() == rightNumber.longValue());
            } else if (leftNumber instanceof Integer || rightNumber instanceof Integer) {
                return BooleanTypeValue.of(leftNumber.intValue() == rightNumber.intValue());
            } else if (leftNumber instanceof Short || rightNumber instanceof Short) {
                return BooleanTypeValue.of(leftNumber.shortValue() == rightNumber.shortValue());
            } else if (leftNumber instanceof Byte || rightNumber instanceof Byte) {
                return BooleanTypeValue.of(leftNumber.byteValue() == rightNumber.byteValue());
            } else {
                return BooleanTypeValue.of(leftNumber.doubleValue() == rightNumber.doubleValue());
            }
        }
        return null;
    }

}
