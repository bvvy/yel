package org.bvvy.yel.exp.ast;

public class OpLT extends Operator {
    public OpLT(int startPos, int endPos, Node ... operand) {
        super("<", startPos, endPos, operand);
    }
}
