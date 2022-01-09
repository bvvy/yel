package org.bvvy.yel.exp.ast;

import org.bvvy.yel.exp.ExpressionState;
import org.bvvy.yel.exp.TypedValue;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public abstract class NodeImpl implements Node{

    private int startPos;

    private int endPos;

    protected Node [] children;

    public NodeImpl(int startPos, int endPos) {
        this(startPos, endPos, new Node[0]);
    }

    public NodeImpl(int startPos, int endPos, Node ... children) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.children = children;
    }

    public int getChildCount() {
        return this.children.length;
    }


    @Override
    public int getStartPosition() {
        return startPos;
    }

    @Override
    public int getEndPosition() {
        return endPos;
    }

    @Override
    public TypedValue getValueInternal(ExpressionState state) {
        return null;
    }
}
