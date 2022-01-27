package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.Operation;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.NumberUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.math.BigDecimal;
import java.math.BigInteger;

public class OpPlus extends Operator {
    public OpPlus(int startPos, int endPos, Node... operands) {
        super("+", startPos, endPos, operands);
    }


    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Node leftOp = getLeftOperand();
        if (this.children.length < 2) {
            Object operandOne = leftOp.getValueInternal(state).getValue();
            if (operandOne instanceof Number) {
                if (operandOne instanceof Double) {
                    this.exitTypeDescriptor = "D";
                } else if (operandOne instanceof Float) {
                    this.exitTypeDescriptor = "F";
                } else if (operandOne instanceof Long) {
                    this.exitTypeDescriptor = "J";
                } else if (operandOne instanceof Integer) {
                    this.exitTypeDescriptor = "I";
                } else if (operandOne instanceof BigDecimal) {
                    this.exitTypeDescriptor = "Ljava/math/BigDecimal";
                }
                return new TypedValue(operandOne);
            }
            return state.operate(Operation.ADD, leftOp, null);
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
                return new TypedValue(leftBigDecimal.add(rightBigDecimal));
            } else if (leftNumber instanceof Double || rightNumber instanceof Double) {
                this.exitTypeDescriptor = "D";
                return new TypedValue(leftNumber.doubleValue() + rightNumber.doubleValue());
            } else if (leftNumber instanceof Float || rightNumber instanceof Float) {
                this.exitTypeDescriptor = "F";
                return new TypedValue(leftNumber.floatValue() + rightNumber.floatValue());
            } else if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return new TypedValue(leftBigInteger.add(rightBigInteger));
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                this.exitTypeDescriptor = "J";
                return new TypedValue(leftNumber.longValue() + rightNumber.longValue());
            } else if (NumberUtils.isIntegerForNumericOp(leftNumber) || NumberUtils.isIntegerForNumericOp(rightNumber)) {
                this.exitTypeDescriptor = "I";
                return new TypedValue(leftNumber.intValue() + rightNumber.intValue());
            } else {
                return new TypedValue(leftNumber.doubleValue() + rightNumber.doubleValue());
            }
        }
        if (leftOperand instanceof String && rightOperand instanceof String) {
            this.exitTypeDescriptor = "Ljava/lang/String";
            return new TypedValue((String) leftOperand + rightOperand);
        }
        return state.operate(Operation.ADD, leftOperand, rightOperand);
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
        if ("Ljava/lang/String".equals(this.exitTypeDescriptor)) {
            mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            walk(mv, cf, getLeftOperand());
            walk(mv, cf, getRightOperand());
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        } else if ("Ljava/math/BigDecimal".equals(this.exitTypeDescriptor)) {
            if (this.children.length > 1) {
                Node left = getLeftOperand();
                left.generateCode(mv, cf);
                String leftDesc = left.getExitTypeDescriptor();
                CodeFlow.insertBigDecimalCoercion(mv, leftDesc);
                cf.enterCompilationScope();
                Node right = getRightOperand();
                right.generateCode(mv, cf);
                String rightDesc = right.getExitTypeDescriptor();
                CodeFlow.insertBigDecimalCoercion(mv, rightDesc);
                cf.exitCompilationScope();
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/math/BigDecimal", "add","(Ljava/math/BigDecimal;)Ljava/math/BigDecimal;", false);
                cf.pushDescriptor(this.exitTypeDescriptor);
            }
        } else {
            Node left = getLeftOperand();
            left.generateCode(mv, cf);
            String leftDesc = left.getExitTypeDescriptor();
            String exitDesc = this.exitTypeDescriptor;
            char targetDesc = exitDesc.charAt(0);
            CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, targetDesc);
            if (this.children.length > 1) {
                cf.enterCompilationScope();
                Node right = getRightOperand();
                right.generateCode(mv, cf);
                String rightDesc = right.getExitTypeDescriptor();
                cf.exitCompilationScope();
                CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, targetDesc);
                switch (targetDesc) {
                    case 'I':
                        mv.visitInsn(Opcodes.IADD);
                        break;
                    case 'J':
                        mv.visitInsn(Opcodes.LADD);
                        break;
                    case 'F':
                        mv.visitInsn(Opcodes.FADD);
                        break;
                    case 'D':
                        mv.visitInsn(Opcodes.DADD);
                        break;
                    default:
                        throw new IllegalStateException("unknown exit type");
                }
            }
            cf.pushDescriptor(this.exitTypeDescriptor);
        }
    }

    private void walk(MethodVisitor mv, CodeFlow cf, Node operand) {
        if (operand instanceof OpPlus) {
            OpPlus plus = (OpPlus) operand;
            walk(mv, cf, plus.getLeftOperand());
            walk(mv, cf, plus.getRightOperand());
        } else if (operand != null) {
            cf.enterCompilationScope();
            operand.generateCode(mv, cf);
            if ("Ljava/lang/String".equals(cf.lastDescriptor())) {
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/String");
            }
            cf.exitCompilationScope();
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        }

    }


}
