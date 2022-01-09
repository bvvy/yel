package org.bvvy.yel.exp.ast;

public class Assign extends NodeImpl {
    public Assign(int startPos, int endPos, Node ... operands) {
        super(startPos, endPos, operands);
    }
}
