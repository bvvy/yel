package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.context.comparator.TypeComparator;
import org.bvvy.yel.exception.YelEvalException;

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

    public TypedValue operate(Operation op, Object left,Object right)  {
            String leftType = (left == null ? "null" : left.getClass().getName());
            String rightType = (right == null? "null" : right.getClass().getName());
            throw new YelEvalException(YelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, op, leftType, rightType);
    }
    public TypeComparator getTypeComparator() {
        return context.getTypeComparator();
    }
}
