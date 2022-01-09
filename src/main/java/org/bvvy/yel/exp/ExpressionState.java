package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exception.YelEvaluationException;

import java.util.Deque;

/**
 * @author bvvy
 */
public class ExpressionState {
    private Context context;

    private Deque<TypedValue> contextObject;

    private TypedValue rootObject;

    public ExpressionState(Context context) {
        this.context = context;
        this.rootObject = context.getRootObject();
    }

    public Context getContext() {
        return context;
    }

    public TypedValue operate(Operation operation, Object left, Object right) {
        throw new YelEvaluationException();
    }

    public TypedValue getActiveContextObject() {
        if (contextObject == null || contextObject.size() == 0) {
            return  this.rootObject;
        }
        return contextObject.element();
    }
}
