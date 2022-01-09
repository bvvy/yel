package org.bvvy.yel.exp.ast;

public class OpGT extends Operator {
    public OpGT(int startPos, int endPos, Node ... operand) {
        super(">", startPos, endPos, operand);
    }
}
