package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.math.BigDecimal;

/**
 * @author bvvy
 */
public class DecimalLiteral extends Literal {
    public DecimalLiteral(String payload, int startPos, int endPos, BigDecimal value) {
        super(payload, startPos, endPos, new TypedValue(value));
        this.exitTypeDescriptor = "Ljava/math/BigDecimal";
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        mv.visitTypeInsn(Opcodes.NEW, "java/math/BigDecimal");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn(this.originalValue);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(Ljava/lang/String;)V", false);
    }
}
