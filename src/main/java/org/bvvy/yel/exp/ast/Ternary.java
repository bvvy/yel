package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;
import org.bvvy.yel.util.ObjectUtils;

public class Ternary extends NodeImpl {
    public Ternary(int startPos, int endPos, Node ... operands) {
        super(startPos, endPos, operands);
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        Boolean value = (Boolean) children[0].getValue(state);
        TypedValue result = this.children[value ? 1 : 2].getValueInternal(state);
        computeExitTypeDescriptor();
        return result;
    }

    private void computeExitTypeDescriptor() {
        if (this.exitTypeDescriptor == null
                && this.children[1].getExitTypeDescriptor() != null
                && this.children[2].getExitTypeDescriptor() != null) {
            String leftDescriptor = this.children[1].getExitTypeDescriptor();
            String rightDescriptor = this.children[2].getExitTypeDescriptor();
            if (ObjectUtils.nullSafeEquals(leftDescriptor, rightDescriptor)) {
                this.exitTypeDescriptor = leftDescriptor;
            } else {
                this.exitTypeDescriptor = "Ljava/lang/Object";
            }
        }
    }
}
