package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.exception.YelEvaluationException;
import org.bvvy.yel.exp.ast.Node;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class YelExpression implements Expression {

    private final Node ast;

    private CompiledExpression compiledAst;

    public YelExpression(Node ast) {
        this.ast = ast;
    }

    public Object getValue(Context context) {
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                return compiledAst.getValue(context.getRootObject().getValue(), context);
            } catch (Exception e) {
                throw new YelEvaluationException();
            }
        }
        ExpressionState state = new ExpressionState(context);
        checkCompile();
        return ast.getValue(state);
    }

    private void checkCompile() {

    }

    public Node getAst() {
        return ast;
    }
}
