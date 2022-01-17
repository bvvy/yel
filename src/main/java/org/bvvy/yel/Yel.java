package org.bvvy.yel;


import org.bvvy.yel.exp.YelExpression;
import org.bvvy.yel.parser.YelExpressionParser;
import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.token.Token;
import org.bvvy.yel.exp.token.Tokenizer;

import java.util.List;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class Yel {


    public Object eval(String expression) {
        return eval(expression, (Object) null);
    }

    public Object eval(String expression, Object env) {
        Context context = new Context(env);
        return eval(expression, context);
    }


    public Object eval(String expression, Context context) {
        YelExpressionParser yelExpressionParser = new YelExpressionParser();
        YelExpression exp = yelExpressionParser.parse(expression);
        return exp.getValue(context);
    }
}
