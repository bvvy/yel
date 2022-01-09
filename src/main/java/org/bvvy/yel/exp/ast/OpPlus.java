package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.Operation;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class OpPlus extends Operator {
    public OpPlus(int startPos, int endPos, Node... operands) {
        super("+", startPos, endPos, operands);
    }


    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Node leftOp = getLeftOperand();
        if (this.children.length < 2) {
            Object operandOne = leftOp.getValueInternal(state).getValue();
            if (operandOne instanceof Number) {
                return new TypedValue(operandOne);
            }
            return state.operate(Operation.ADD, leftOp, null);
        }
        Object leftOperand = leftOp.getValueInternal(state).getValue();
        Object rightOperand = getRightOperand().getValueInternal(state).getValue();
        if (leftOperand instanceof Number || rightOperand instanceof Number) {
            Number leftNumber = (Number) leftOperand;
            Number rightNumber = (Number) rightOperand;
            if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
                BigDecimal leftBigDecimal = NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                return new TypedValue(leftBigDecimal.add(rightBigDecimal));
            } else if (leftNumber instanceof Double || rightNumber instanceof Double) {
                return new TypedValue(leftNumber.doubleValue() + rightNumber.doubleValue());
            } else if (leftNumber instanceof Float || rightNumber instanceof Float) {
                return new TypedValue(leftNumber.floatValue() + rightNumber.floatValue());
            } else if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return new TypedValue(leftBigInteger.add(rightBigInteger));
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                return new TypedValue(leftNumber.longValue() + rightNumber.longValue());
            } else if (NumberUtils.isIntegerForNumericOp(leftNumber)|| NumberUtils.isIntegerForNumericOp(rightNumber)) {
                return new TypedValue(leftNumber.intValue() + rightNumber.intValue());
            }else {
                return new TypedValue(leftNumber.doubleValue() + rightNumber.doubleValue());
            }

        }
        return state.operate(Operation.ADD, leftOperand, rightOperand);
    }
}
