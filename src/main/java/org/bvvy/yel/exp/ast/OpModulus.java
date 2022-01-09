package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.Operation;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author bvvy
 */
public class OpModulus extends Operator {
    public OpModulus(int startPos, int endPos, Node ... operand) {
        super("%", startPos, endPos, operand);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
        Object rightOperand = getRightOperand().getValueInternal(state).getValue();
        if (leftOperand instanceof Number || rightOperand instanceof Number) {
            Number leftNumber = (Number) leftOperand;
            Number rightNumber = (Number) rightOperand;
            if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
                BigDecimal leftBigDecimal = NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                return new TypedValue(leftBigDecimal.remainder(rightBigDecimal));
            } else if (leftNumber instanceof Double || rightNumber instanceof Double) {
                return new TypedValue(leftNumber.doubleValue() % rightNumber.doubleValue());
            } else if (leftNumber instanceof Float || rightNumber instanceof Float) {
                return new TypedValue(leftNumber.floatValue() % rightNumber.floatValue());
            } else if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return new TypedValue(leftBigInteger.remainder(rightBigInteger));
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                return new TypedValue(leftNumber.longValue() % rightNumber.longValue());
            } else if (NumberUtils.isIntegerForNumericOp(leftNumber) || NumberUtils.isIntegerForNumericOp(rightNumber)) {
                return new TypedValue(leftNumber.intValue() % rightNumber.intValue());
            } else {
                return new TypedValue(leftNumber.doubleValue() % rightNumber.doubleValue());
            }
        }
        return state.operate(Operation.MODULES, leftOperand, rightOperand);
    }
}
