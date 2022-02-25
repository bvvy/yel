package org.bvvy.yel.exception;

import org.bvvy.yel.exp.YelMessage;

/**
 * @author bvvy
 */
public class YelEvalException extends ExpressionException {

    private final YelMessage message;
    private final Object[] inserts;

    public YelEvalException(YelMessage message, Object... inserts) {
        super(message.formatMessage(inserts));
        this.message = message;
        this.inserts = inserts;
    }

    public YelEvalException(Exception e, YelMessage message, Object ... inserts) {
        super(message.formatMessage(inserts), e);
        this.message = message;
        this.inserts = inserts;
    }

    public YelEvalException(int position, YelMessage message, Object ... inserts) {
        super(position, message.formatMessage(inserts));
        this.message = message;
        this.inserts = inserts;
    }
}
