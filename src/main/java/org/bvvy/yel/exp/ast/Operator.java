package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class Operator extends NodeImpl {

    private final String operatorName;

    public Operator(String payload, int startPos, int endPos, Node... operands) {
        super(startPos, endPos, operands);
        this.operatorName = payload;
    }

    public Node getLeftOperand() {
        return this.children[0];
    }

    public Node getRightOperand() {
        return this.children[1];
    }


    protected void generateComparisonCode(MethodVisitor mv, CodeFlow cf, int opcode) {
        Label endOfIf = new Label();
        Label elseTarget = new Label();

        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/bvvy/yel/context/Context", "getTypeComparator", "()Lorg/bvvy/yel/context/comparator/TypeComparator;", false);
        Node left = getLeftOperand();
        cf.enterCompilationScope();
        left.generateCode(mv, cf);
        cf.exitCompilationScope();
        cf.insertDescriptorConversion(mv, left.getExitTypeDescriptor(), "Ljava/lang/Comparable");
        Node right = getRightOperand();
        cf.enterCompilationScope();
        right.generateCode(mv, cf);
        cf.exitCompilationScope();
        cf.insertDescriptorConversion(mv, right.getExitTypeDescriptor(), "Ljava/lang/Comparable");
        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "org/bvvy/yel/context/comparator/TypeComparator", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", true);
        mv.visitJumpInsn(opcode, elseTarget);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitJumpInsn(Opcodes.GOTO, endOfIf);
        mv.visitLabel(elseTarget);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitLabel(endOfIf);
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
