package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.NumberUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.math.BigDecimal;
import java.math.BigInteger;

public class OpMinus extends Operator {
    public OpMinus(int startPos, int endPos, Node... operands) {
        super("-", startPos, endPos, operands);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Node leftOp = getLeftOperand();
        if (this.children.length < 2) {
            Object operand = leftOp.getValueInternal(state).getValue();
            if (operand instanceof Number) {
                if (operand instanceof BigDecimal) {
                    this.exitTypeDescriptor = "Ljava/math/BigDecimal";
                    return new TypedValue(((BigDecimal) operand).negate());
                } else if (operand instanceof Double) {
                    this.exitTypeDescriptor = "D";
                    return new TypedValue(-(Double) operand);
                } else if (operand instanceof Float) {
                    this.exitTypeDescriptor = "F";
                    return new TypedValue(-(Float) operand);
                } else if (operand instanceof BigInteger) {
                    return new TypedValue(((BigInteger) operand).negate());
                } else if (operand instanceof Long) {
                    this.exitTypeDescriptor = "J";
                    return new TypedValue(-(Long) operand);
                } else if (operand instanceof Integer) {
                    this.exitTypeDescriptor = "I";
                    return new TypedValue(-(Integer) operand);
                } else if (operand instanceof Short) {
                    return new TypedValue(-(Short) operand);
                } else if (operand instanceof Byte) {
                    return new TypedValue(-(Byte) operand);
                } else {
                    return new TypedValue(-((Number) operand).doubleValue());
                }
            }
            return new TypedValue(state.getOperatorOverloader().minus(operand, null));
        }
        Object leftOperand = leftOp.getValueInternal(state).getValue();
        Object rightOperand = getRightOperand().getValueInternal(state).getValue();
        if (leftOperand instanceof Number && rightOperand instanceof Number) {
            Number leftNumber = (Number) leftOperand;
            Number rightNumber = (Number) rightOperand;
            if (leftNumber instanceof BigDecimal || rightNumber instanceof BigDecimal) {
                BigDecimal leftBigDecimal = NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                this.exitTypeDescriptor = "Ljava/math/BigDecimal";
                return new TypedValue(leftBigDecimal.subtract(rightBigDecimal));
            } else if (leftNumber instanceof Double || rightNumber instanceof Double) {
                this.exitTypeDescriptor = "D";
                return new TypedValue(leftNumber.doubleValue() - rightNumber.doubleValue());
            } else if (leftNumber instanceof Float || rightNumber instanceof Float) {
                this.exitTypeDescriptor = "F";
                return new TypedValue(leftNumber.floatValue() - rightNumber.floatValue());
            } else if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return new TypedValue(leftBigInteger.subtract(rightBigInteger));
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                this.exitTypeDescriptor = "J";
                return new TypedValue(leftNumber.longValue() - rightNumber.longValue());
            } else if (NumberUtils.isIntegerForNumericOp(leftNumber) || NumberUtils.isIntegerForNumericOp(rightNumber)) {
                this.exitTypeDescriptor = "I";
                return new TypedValue(leftNumber.intValue() - rightNumber.intValue());
            } else {
                return new TypedValue(leftNumber.doubleValue() - rightNumber.doubleValue());
            }
        }
        return new TypedValue(state.getOperatorOverloader().minus(leftOperand, rightOperand));
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
        if (this.children.length > 1) {
            cf.enterCompilationScope();
            Node right = getRightOperand();
            right.generateCode(mv, cf);
            String rightDesc = right.getExitTypeDescriptor();
            cf.exitCompilationScope();
            cf.insertDescriptorConversion(mv, rightDesc, exitDesc);
            switch (exitDesc) {
                case "I":
                    mv.visitInsn(Opcodes.ISUB);
                    break;
                case "J":
                    mv.visitInsn(Opcodes.LSUB);
                    break;
                case "F":
                    mv.visitInsn(Opcodes.FSUB);
                    break;
                case "D":
                    mv.visitInsn(Opcodes.DSUB);
                    break;
                case "Ljava/math/BigDecimal":
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/math/BigDecimal", "subtract", "(Ljava/math/BigDecimal;)Ljava/math/BigDecimal;", false);
                    break;
                default:
                    throw new IllegalStateException("unknown exit type");
            }
        } else {
            switch (exitDesc) {
                case "I":
                    mv.visitInsn(Opcodes.INEG);
                    break;
                case "J":
                    mv.visitInsn(Opcodes.LNEG);
                    break;
                case "F":
                    mv.visitInsn(Opcodes.FNEG);
                    break;
                case "D":
                    mv.visitInsn(Opcodes.DNEG);
                    break;
                case "Ljava/math/BigDecimal":
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/math/BigDecimal", "negate", "()Ljava/math/BigDecimal;", false);
                    break;
                default:
                    throw new IllegalStateException("unknown exit type");
            }
        }
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
