package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class NullLiteral extends Literal {
    public NullLiteral(int startPos, int endPos) {
        super(null, startPos, endPos, TypedValue.NULL);
        this.exitTypeDescriptor = "Ljava/lang/Object";
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        mv.visitInsn(Opcodes.ACONST_NULL);
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
