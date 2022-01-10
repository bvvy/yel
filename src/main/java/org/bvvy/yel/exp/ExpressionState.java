package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exception.YelEvaluationException;

import java.util.ArrayDeque;
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
            return this.rootObject;
        }
        return contextObject.element();
    }

    public void pushActiveContextObject(TypedValue result) {
        if (this.contextObject == null) {
            this.contextObject = new ArrayDeque<>();
        }
        this.contextObject.push(result);
    }

    public void popActiveContextObject() {
        this.contextObject.pop();
    }

    public TypedValue getRootContextObject() {
        return rootObject;
    }

    public ValueRef index(Object index) {
        throw new YelEvaluationException();
    }
}
