package org.bvvy.yel.exception;

public class ExpressionException extends RuntimeException {

    private final int position;


    public int getPosition() {
        return position;
    }

    public ExpressionException(String message) {
        super(message);
        this.position = 0;
    }

    public ExpressionException(String message, Throwable cause) {
        super(message, cause);
        this.position = 0;
    }

    public ExpressionException(int position, String message) {
        super(message);
        this.position = position;
    }
}
