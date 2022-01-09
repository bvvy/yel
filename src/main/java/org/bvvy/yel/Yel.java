package org.bvvy.yel;


import org.bvvy.yel.exp.Expression;
import org.bvvy.yel.parser.ExpressionParser;
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
        Tokenizer tokenizer = new Tokenizer(expression);
        List<Token> tokens = tokenizer.process();
        Context context = new Context(env);
        ExpressionParser expressionParser = new ExpressionParser(tokens);
        Expression exp = expressionParser.parse();
        return exp.getValue(context);
    }


    public Object eval(String expression, Context context) {
        return null;
    }
}
