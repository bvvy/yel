package org.bvvy.yel.exp.ast;

public class OpLE extends Operator {
    public OpLE(int startPos, int endPos, Node ... operand) {
        super("<=", startPos, endPos, operand);
    }
}
