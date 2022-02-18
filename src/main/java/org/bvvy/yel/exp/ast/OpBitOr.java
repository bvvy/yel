package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.NumberUtils;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.math.BigInteger;

/**
 * @author bvvy
 * @date 2022/2/9
 */
public class OpBitOr extends Operator {
    public OpBitOr(int startPos, int endPos, Node... operand) {
        super("|", startPos, endPos, operand);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Object left = getLeftOperand().getValueInternal(state).getValue();
        Object right = getRightOperand().getValueInternal(state).getValue();
        if (left instanceof Number && right instanceof Number) {
            Number leftNumber = (Number) left;
            Number rightNumber = (Number) right;
            if (leftNumber instanceof BigInteger || rightNumber instanceof BigInteger) {
                BigInteger leftBigInteger = NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return new TypedValue(leftBigInteger.or(rightBigInteger));
            } else if (leftNumber instanceof Long || rightNumber instanceof Long) {
                this.exitTypeDescriptor = "J";
                return new TypedValue(leftNumber.longValue() | rightNumber.longValue());
            } else if (NumberUtils.isIntegerForNumericOp(leftNumber) || NumberUtils.isIntegerForNumericOp(rightNumber)) {
                this.exitTypeDescriptor = "I";
                return new TypedValue(leftNumber.intValue() | rightNumber.intValue());
            } else {
                throw new YelEvaluationException();
            }
        } else {
            throw new YelEvaluationException();
        }
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
                    mv.visitInsn(Opcodes.IOR);
                    break;
                case "J":
                    mv.visitInsn(Opcodes.LOR);
                    break;
                default:
                    throw new IllegalStateException("unknown exit type");
            }
        }
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
