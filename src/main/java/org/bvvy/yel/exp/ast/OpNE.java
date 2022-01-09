package org.bvvy.yel.exp.ast;

public class OpNE extends Operator {
    public OpNE(int startPos, int endPos, Node ... operand) {
        super("!=", startPos, endPos, operand);
    }
}
