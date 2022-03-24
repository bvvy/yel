package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.NumberUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @author bvvy
 */
public class OpDivide extends Operator {
    public OpDivide(int startPos, int endPos, Node... operand) {
        super("/", startPos, endPos, operand);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
        Object rightOperand = getRightOperand().getValueInternal(state).getValue();
        if (leftOperand instanceof Number && rightOperand instanceof Number) {
            Number leftNumber = (Number) leftOperand;
            Number rightNumber = (Number) rightOperand;
            if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
                BigDecimal leftBigDecimal = NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                int scale = Math.max(leftBigDecimal.scale(), rightBigDecimal.scale());
                this.exitTypeDescriptor = "Ljava/math/BigDecimal";
                return new TypedValue(leftBigDecimal.divide(rightBigDecimal, scale, RoundingMode.HALF_UP));
            } else if (leftNumber instanceof Double || rightNumber instanceof Double) {
                this.exitTypeDescriptor = "D";
                return new TypedValue(leftNumber.doubleValue() / rightNumber.doubleValue());
            } else if (leftNumber instanceof Float || rightNumber instanceof Float) {
                this.exitTypeDescriptor = "F";
                return new TypedValue(leftNumber.floatValue() / rightNumber.floatValue());
            } else if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return new TypedValue(leftBigInteger.divide(rightBigInteger));
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                this.exitTypeDescriptor = "J";
                return new TypedValue(leftNumber.longValue() / rightNumber.longValue());
            } else if (NumberUtils.isIntegerForNumericOp(leftNumber) || NumberUtils.isIntegerForNumericOp(rightNumber)) {
                this.exitTypeDescriptor = "I";
                return new TypedValue(leftNumber.intValue() / rightNumber.intValue());
            } else {
                return new TypedValue(leftNumber.doubleValue() / rightNumber.doubleValue());
            }
        }
        return new TypedValue(state.getOperatorOverloader().divide(leftOperand, rightOperand));
    }

    @Override
    public boolean isCompilable() {
        if (!getLeftOperand().isCompilable()) {
            return false;
        }
        if (this.children.length > 1) {
            if (!getRightOperand().isCompilable()) {
                return false;
            }
        }
        return (this.exitTypeDescriptor != null);
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        Node left = getLeftOperand();
        left.generateCode(mv, cf);
        String leftDesc = left.getExitTypeDescriptor();
        String exitDesc = this.exitTypeDescriptor;
        cf.insertDescriptorConversion(mv, leftDesc, exitDesc);
        if ("Ljava/math/BigDecimal".equals(exitDesc)) {
            mv.visitVarInsn(Opcodes.ASTORE, 3);
        }
        if (this.children.length > 1) {
            cf.enterCompilationScope();
            Node right = getRightOperand();
            right.generateCode(mv, cf);
            String rightDesc = right.getExitTypeDescriptor();
            cf.exitCompilationScope();
            cf.insertDescriptorConversion(mv, rightDesc, exitDesc);
            if ("Ljava/math/BigDecimal".equals(exitDesc)) {
                mv.visitVarInsn(Opcodes.ASTORE, 4);
            }
            switch (exitDesc) {
                case "I":
                    mv.visitInsn(Opcodes.IDIV);
                    break;
                case "J":
                    mv.visitInsn(Opcodes.LDIV);
                    break;
                case "F":
                    mv.visitInsn(Opcodes.FDIV);
                    break;
                case "D":
                    mv.visitInsn(Opcodes.DDIV);
                    break;
                case "Ljava/math/BigDecimal":
                    mv.visitVarInsn(Opcodes.ALOAD, 3);
                    mv.visitVarInsn(Opcodes.ALOAD, 4);
                    mv.visitVarInsn(Opcodes.ALOAD, 3);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/math/BigDecimal", "scale", "()I", false);
                    mv.visitVarInsn(Opcodes.ALOAD, 4);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/math/BigDecimal", "scale", "()I", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/math/RoundingMode", "HALF_UP", "Ljava/math/RoundingMode;");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/math/BigDecimal", "divide", "(Ljava/math/BigDecimal;ILjava/math/RoundingMode;)Ljava/math/BigDecimal;", false);
                    break;
                default:
                    throw new IllegalStateException("unknown exit type");
            }
        }
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
