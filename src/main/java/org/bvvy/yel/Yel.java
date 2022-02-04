package org.bvvy.yel;


import org.bvvy.yel.config.YelConfig;
import org.bvvy.yel.context.Context;
import org.bvvy.yel.exp.YelExpression;
import org.bvvy.yel.parser.YelParser;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class Yel {

    private YelConfig yelConfig;

    public Yel(YelConfig yelConfig) {
        this.yelConfig = yelConfig;
    }

    public Yel() {
        this.yelConfig = new YelConfig();
    }

    public Object eval(String expression) {
        return eval(expression, (Object) null);
    }

    public Object eval(String expression, Object env) {
        Context context = new Context(env);
        return eval(expression, context);
    }


    public Object eval(String expression, Context context) {
        YelParser yelParser = new YelParser(yelConfig.getYelParserConfig());
        YelExpression exp = yelParser.parse(expression);
        return exp.getValue(context);
    }
}
