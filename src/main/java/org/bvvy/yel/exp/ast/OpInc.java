package org.bvvy.yel.exp.ast;

public class OpInc extends Operator {
    private final boolean postfix;

    public OpInc(int startPos, int endPos, boolean postfix, Node ... operands) {
        super("++", startPos, endPos, operands);
        this.postfix = postfix;
    }
}
