package org.bvvy.yel.exp.ast;

import org.bvvy.yel.context.PropertyAccessor;
import org.bvvy.yel.context.Context;
import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

import java.util.List;

public class PropertyOrFieldReference extends NodeImpl {
    private final String name;
    private final boolean nullSafe;
    private PropertyAccessor cachedReadAccessor;

    public PropertyOrFieldReference(boolean nullSafe, String propertyOrFieldName, int startPos, int endPos) {
        super(startPos, endPos);
        this.nullSafe = nullSafe;
        this.name = propertyOrFieldName;
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        TypedValue typedValue = getValueInternal(state.getActiveContextObject(), state.getContext());
        return typedValue;
    }

    private TypedValue getValueInternal(TypedValue contextObject, Context context) {
        TypedValue result = readProperty(contextObject, context, this.name);
        return result;
    }

    private TypedValue readProperty(TypedValue contextObject, Context context, String name) {
        Object targetObject = contextObject.getValue();
        if (targetObject == null && this.nullSafe) {
            return TypedValue.NULL;
        }
        List<PropertyAccessor> propertyAccessors = context.getPropertyAccessors();
        for (PropertyAccessor accessor : propertyAccessors) {
            if (accessor.canRead(context, contextObject.getValue(), name)) {
                this.cachedReadAccessor = accessor;
                return accessor.read(context, targetObject, name);
            }

        }
        throw new YelEvaluationException();
    }

    private List<PropertyAccessor> getPropertyAccessorsToTry(Object contextObject, List<PropertyAccessor> propertyAccessors) {
        Class<?> targetType = contextObject != null ? contextObject.getClass() : null;


        return null;
    }


}
