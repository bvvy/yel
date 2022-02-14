package org.bvvy.yel.exception;

/**
 * @author bvvy
 * @date 2022/2/14
 */
public class AccessException extends RuntimeException {

    public AccessException(String message) {
        super(message);
    }

    public AccessException(String message, Exception cause) {
        super(message, cause);
    }
}
