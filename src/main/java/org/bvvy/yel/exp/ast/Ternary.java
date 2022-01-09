package org.bvvy.yel.exp.ast;

public class Ternary extends NodeImpl {
    public Ternary(int startPos, int endPos, Node ... operands) {
        super(startPos, endPos, operands);
    }
}
