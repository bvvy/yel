package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.parser.YelCompilerMode;
import org.bvvy.yel.parser.YelExpressionParser;
import org.bvvy.yel.parser.YelParserConfig;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bvvy
 * @date 2022/1/24
 */
public class TestGenerateCode {

    @Test
    public void testGenerateCode() {
        YelParserConfig yelParserConfig = new YelParserConfig(YelCompilerMode.IMMEDIATE);
//        yelParserConfig.setUseBigDecimalForFloat(true);
        YelExpressionParser parser = new YelExpressionParser(yelParserConfig);
        Map<String, Object> env = new HashMap<>();
        env.put("a", new BigDecimal("1.1"));
        env.put("b", 2);
        YelExpression ex1 = parser.parse("a + 1.1f");
        Context context = new Context(env);
        System.out.println(ex1.getValue(context));
        System.out.println(ex1.getValue(context));

    }
}
