package org.bvvy.yel.exp.ast;

public class OperatorPower extends NodeImpl {
    public OperatorPower(int startPos, int endPos,Node ... operand) {
        super(startPos, endPos, operand);
    }
}
