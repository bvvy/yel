package org.bvvy.yel.exp.ast;

public class OpDec extends Operator {
    private final boolean postfix;

    public OpDec(int startPos, int endPos, boolean postfix, Node ... operands) {
        super("--", startPos, endPos, operands);
        this.postfix = postfix;
    }
}
