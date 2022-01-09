package org.bvvy.yel.exp.ast;

public class CompoundExpression extends NodeImpl {
    public CompoundExpression(int startPos, int endPos, Node... expressionComponents) {
        super(startPos, endPos, expressionComponents);
    }
}
