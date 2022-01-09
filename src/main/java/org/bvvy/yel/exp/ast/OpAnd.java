package org.bvvy.yel.exp.ast;

public class OpAnd extends Operator {
    public OpAnd(int startPos, int endPos, Node ... operands) {
        super("and", startPos, endPos, operands);
    }
}
