package org.bvvy.yel.context.overloader;

import org.bvvy.yel.exception.YelEvalException;
import org.bvvy.yel.exp.YelMessage;

public class DefaultOperatorOverloader implements OperatorOverloader {
    @Override
    public Object add(Object left, Object right) {
        throw new YelEvalException(YelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, "+", left, right);
    }

    @Override
    public Object minus(Object left, Object right) {
        throw new YelEvalException(YelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, "-", left, right);
    }

    @Override
    public Object mod(Object left, Object right) {
        throw new YelEvalException(YelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, "%", left, right);
    }

    @Override
    public Object multiply(Object left, Object right) {
        throw new YelEvalException(YelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, "*", left, right);
    }

    @Override
    public Object divide(Object left, Object right) {
        throw new YelEvalException(YelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, "/", left, right);
    }

}
