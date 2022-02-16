package org.bvvy.yel.exp.ast;

import org.bvvy.yel.context.accessor.CompilablePropertyAccessor;
import org.bvvy.yel.context.Context;
import org.bvvy.yel.context.accessor.PropertyAccessor;
import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;

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
        PropertyAccessor accessorToUse = this.cachedReadAccessor;
        if (accessorToUse instanceof CompilablePropertyAccessor) {
            CompilablePropertyAccessor accessor = (CompilablePropertyAccessor) accessorToUse;
            setExitTypeDescriptor(CodeFlow.toDescriptor(accessor.getPropertyType()));
        }
        return typedValue;
    }

    public void setExitTypeDescriptor(String descriptor) {
        this.exitTypeDescriptor = descriptor;
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

    @Override
    public boolean isCompilable() {
        PropertyAccessor accessorToUse = this.cachedReadAccessor;
        return accessorToUse instanceof CompilablePropertyAccessor && ((CompilablePropertyAccessor) accessorToUse).isCompilable();
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        PropertyAccessor accessorToUse = this.cachedReadAccessor;
        if (!(accessorToUse instanceof CompilablePropertyAccessor)) {
            throw new IllegalStateException("property accessor is not compilable: " + accessorToUse);
        }
        if (this.nullSafe) {
            // todo
        }
        ((CompilablePropertyAccessor) accessorToUse).generateCode(this.name, mv, cf);
        cf.pushDescriptor(this.exitTypeDescriptor);

    }
}
