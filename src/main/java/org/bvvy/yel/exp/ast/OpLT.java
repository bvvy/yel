package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.BooleanTypeValue;
import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class OpLT extends Operator {
    public OpLT(int startPos, int endPos, Node... operand) {
        super("<", startPos, endPos, operand);
        this.exitTypeDescriptor = "Z";
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Object left = getLeftOperand().getValueInternal(state).getValue();
        Object right = getRightOperand().getValueInternal(state).getValue();
        return BooleanTypeValue.of(state.getTypeComparator().compare(left, right) < 0);
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        this.generateComparisonCode(mv, cf, Opcodes.IFLT);
    }
}
