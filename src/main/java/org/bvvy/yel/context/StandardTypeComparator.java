package org.bvvy.yel.context;

import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author bvvy
 * @date 2022/1/13
 */
public class StandardTypeComparator implements TypeComparator {
    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object left, Object right) {
        if (left == null) {
            return right == null ? 0 : -1;
        }
        if (right == null) {
            return 1;
        }
        if (left instanceof Number && right instanceof Number) {
            Number leftNumber = (Number) left;
            Number rightNumber = (Number) right;
            if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
                BigDecimal leftBigDecimal = NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                return leftBigDecimal.compareTo(rightBigDecimal);
            } else if (leftNumber instanceof Double || rightNumber instanceof Double) {
                return Double.compare(leftNumber.doubleValue(), rightNumber.doubleValue());
            } else if (leftNumber instanceof Float || rightNumber instanceof Float) {
                return Float.compare(leftNumber.floatValue(), rightNumber.floatValue());
            } else if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return leftBigInteger.compareTo(rightBigInteger);
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                return Long.compare(leftNumber.longValue(), rightNumber.longValue());
            } else if (leftNumber instanceof Integer || rightNumber instanceof Integer) {
                return Integer.compare(leftNumber.intValue(), rightNumber.intValue());
            } else if (leftNumber instanceof Short || rightNumber instanceof Short) {
                return Short.compare(leftNumber.shortValue(), rightNumber.shortValue());
            } else if (leftNumber instanceof Byte || rightNumber instanceof Byte) {
                return Byte.compare(leftNumber.byteValue(), rightNumber.byteValue());
            } else {
                return Double.compare(leftNumber.doubleValue(), rightNumber.doubleValue());
            }
        }
        try {
            if (left instanceof Comparable) {
                return ((Comparable<Object>) left).compareTo(right);
            }
        } catch (Exception e) {
            throw new YelEvaluationException();
        }
        throw new YelEvaluationException();
    }
}
