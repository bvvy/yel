package org.bvvy.yel.exp;

import java.text.MessageFormat;

public enum YelMessage {
    OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES(1030, "The operator '{0}' is not supported between object of type '{1}' and '{}'"),
    NOT_COMPARABLE(1031, "Cannot compare instances of '{0}' and '{1}'"),
    MULTIPLE_POSSIBLE_FUNCTIONS(1312,"Global function call of '{0}' is ambiguous, supported type conversions allow multiple variants to match" ),
    CANNOT_INDEX_INTO_NULL_VALUE(),
    ARRAY_INDEX_OUT_OF_BOUNDS,
    MULTIPLE_POSSIBLE_METHODS,
    NOT_ASSIGNABLE, METHOD_NOT_FOUND, EXCEPTION_RUNNING_COMPILED_EXPRESSION;

    private final int code;
    private final String message;

    YelMessage() {
        this.code = 1;
        this.message = "err";
    }

    YelMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String formatMessage(Object[] inserts) {
        return "YEL" + this.code +
                "E: " +
                MessageFormat.format(this.message, inserts);
    }
}
