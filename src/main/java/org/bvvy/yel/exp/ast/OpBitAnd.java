package org.bvvy.yel.exp.ast;

/**
 * @author bvvy
 * @date 2022/2/9
 */
public class OpBitAnd extends Operator {
    public OpBitAnd(int startPos, int endPos, Node ... operand) {
        super("&", startPos, endPos, operand);
    }
}
