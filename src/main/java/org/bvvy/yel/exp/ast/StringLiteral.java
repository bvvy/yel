package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.CodeFlow;
import org.bvvy.yel.exp.TypedValue;
import org.objectweb.asm.MethodVisitor;

public class StringLiteral extends Literal {

    public StringLiteral(String payload, int startPos, int endPos, String value) {
        super(payload, startPos, endPos, new TypedValue(value.substring(1, value.length() - 1)));
        this.exitTypeDescriptor = "Ljava/lang/String";
    }

    @Override
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        mv.visitLdcInsn(this.value.getValue());
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
