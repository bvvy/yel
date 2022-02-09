package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.BooleanTypeValue;
import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class OpEQ extends Operator {
    public OpEQ(int startPos, int endPos, Node... operand) {
        this("==", startPos, endPos, operand);
    }

    public OpEQ(String payload, int startPos, int endPos, Node... operand) {
        super(payload, startPos, endPos, operand);
        this.exitTypeDescriptor = "Z";
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Object left = getLeftOperand().getValueInternal(state).getValue();
        Object right = getRightOperand().getValueInternal(state).getValue();
        return BooleanTypeValue.of(state.getTypeComparator().compare(left, right) == 0);
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        this.generateComparisonCode(mv, cf, Opcodes.IFEQ);
    }
}
