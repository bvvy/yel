package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author bvvy
 * @date 2021/11/29
 */
public class IntLiteral extends Literal {

    public IntLiteral(String payload, int startPos, int endPos, int value) {
        super(payload, startPos, endPos, new TypedValue(value));
        this.exitTypeDescriptor = "I";
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        Integer intValue = (Integer) this.value.getValue();
        if (intValue == -1) {
            mv.visitInsn(Opcodes.ICONST_M1);
        } else if (intValue > 0 && intValue < 6) {
            mv.visitInsn(Opcodes.ICONST_0 + intValue);
        } else {
            mv.visitLdcInsn(intValue);
        }
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
