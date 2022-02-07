package org.bvvy.yel.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author bvvy
 */
public class NumberUtils {
    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass) {
        if (targetClass.isInstance(number)) {
            return (T) number;
        } else if (Byte.class == targetClass) {
            return (T) Byte.valueOf(number.byteValue());
        } else if (Short.class == targetClass) {
            return (T) Short.valueOf(number.shortValue());
        } else if (Integer.class == targetClass) {
            return (T) Integer.valueOf(number.intValue());
        } else if (Long.class == targetClass) {
            return (T) Long.valueOf(number.longValue());
        } else if (BigInteger.class == targetClass) {
            if (number instanceof BigDecimal) {
                return (T) ((BigDecimal) number).toBigInteger();
            }
            return (T) BigInteger.valueOf(number.longValue());
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(number.floatValue());
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(number.doubleValue());
        } else if (BigDecimal.class == targetClass) {
            return (T) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException(
                    "Could not convert number [" + number + "] of type [" +
                            "Could not convert number [" + number + "] of type [" +
                            number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]"
            );
        }

    }


    public static boolean isIntegerForNumericOp(Number number) {
        return number instanceof Integer || number instanceof Short || number instanceof Byte;
    }
}
