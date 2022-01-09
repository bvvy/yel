package org.bvvy.yel.exp.ast;

public class OpGE extends Operator {
    public OpGE(int startPos, int endPos, Node ... operand) {
        super(">=", startPos, endPos, operand);
    }
}
