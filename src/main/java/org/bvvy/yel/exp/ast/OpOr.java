package org.bvvy.yel.exp.ast;

public class OpOr extends Operator {
    public OpOr(int startPos, int endPos, Node ... operands) {
        super("or", startPos, endPos, operands);
    }
}
