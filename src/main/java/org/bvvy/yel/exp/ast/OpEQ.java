package org.bvvy.yel.exp.ast;

public class OpEQ extends Operator {
    public OpEQ(int startPos, int endPos, Node ... operand) {
        super("==", startPos, endPos, operand);
    }
}
