package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;

public class Literal extends NodeImpl {
    private final String originalValue;
    protected TypedValue value;

    public Literal(String originalValue, int startPos, int endPos, TypedValue value) {
        super(startPos, endPos);
        this.originalValue = originalValue;
        this.value = value;
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        return this.value;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public boolean isCompilable() {
        return true;
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        mv.visitLdcInsn(this.value.getValue());
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
