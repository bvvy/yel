package org.bvvy.yel;


import org.bvvy.yel.config.YelConfig;
import org.bvvy.yel.context.Context;
import org.bvvy.yel.context.StandardContext;
import org.bvvy.yel.exp.Expression;
import org.bvvy.yel.exp.YelExpression;
import org.bvvy.yel.parser.YelParser;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class Yel {

    private YelConfig yelConfig;
    private YelParser yelParser;

    public Yel(YelConfig yelConfig) {
        this.yelConfig = yelConfig;
        this.yelParser = new YelParser(yelConfig.getYelParserConfig());
    }

    public Yel() {
        this.yelConfig = new YelConfig();
        this.yelParser = new YelParser(yelConfig.getYelParserConfig());
    }

    public Object eval(String expression) {
        return eval(expression, (Object) null);
    }

    public Object eval(String expression, Object env) {
        Context context = new StandardContext(env);
        return eval(expression, context);
    }


    public Object eval(String expression, Context context) {
        YelExpression exp = yelParser.parse(expression);
        return exp.getValue(context);
    }

    public Expression parse(String expression) {
        return yelParser.parse(expression);
    }

}
