package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.exp.ValueRef;
import org.objectweb.asm.MethodVisitor;

public interface Node {

    int getStartPosition();

    int getEndPosition();

    default Object getValue(ExpressionState state) {
        return this.getValueInternal(state).getValue();
    }

    TypedValue getValueInternal(ExpressionState state);

    ValueRef getValueRef(ExpressionState state);

    void generateCode(MethodVisitor mv, CodeFlow cf);

    String getExitDescriptor();
}
