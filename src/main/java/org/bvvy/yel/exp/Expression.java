package org.bvvy.yel.exp;

import org.bvvy.yel.exp.ast.Node;
import org.bvvy.yel.context.Context;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class Expression {
    private final Node ast;

    public Expression(Node ast) {
        this.ast = ast;
    }

    public Object getValue(Context context) {
        ExpressionState state = new ExpressionState(context);
        return ast.getValue(state);
    }

    public Node getAst() {
        return ast;
    }
}
