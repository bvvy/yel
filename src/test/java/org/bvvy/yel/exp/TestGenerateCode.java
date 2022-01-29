package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.parser.YelCompilerMode;
import org.bvvy.yel.parser.YelExpressionParser;
import org.bvvy.yel.parser.YelParserConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bvvy
 * @date 2022/1/24
 */
public class TestGenerateCode {

    @Test
    public void testGenerateCodeOperator() {
        YelParserConfig yelParserConfig = new YelParserConfig(YelCompilerMode.IMMEDIATE);
        yelParserConfig.setUseBigDecimalForFloat(true);
        YelExpressionParser parser = new YelExpressionParser(yelParserConfig);
        Map<String, Object> env = new HashMap<>();
        Context context = new Context(env);
        YelExpression ex1 = parser.parse("1.1 + 1.1 + 1");
        Object v1 = ex1.getValue(context);
        Object v2 = ex1.getValue(context);
        Assertions.assertEquals(v1, v2);
        ex1 = parser.parse("1 + 1.1 - 1");
        v1 = ex1.getValue(context);
        v2 = ex1.getValue(context);
        Assertions.assertEquals(v1, v2);

        ex1 = parser.parse("1 * 1.1");
        v1 = ex1.getValue(context);
        v2 = ex1.getValue(context);
        Assertions.assertEquals(v1, v2);


        ex1 = parser.parse("3.1 % 2");
        v1 = ex1.getValue(context);
        v2 = ex1.getValue(context);
        Assertions.assertEquals(v1, v2);

        ex1 = parser.parse("3.1 / 2");
        v1 = ex1.getValue(context);
        v2 = ex1.getValue(context);
        Assertions.assertEquals(v1, v2);

    }

    @Test
    public void testForCompare() {

        YelParserConfig yelParserConfig = new YelParserConfig(YelCompilerMode.IMMEDIATE);
        yelParserConfig.setUseBigDecimalForFloat(true);
        YelExpressionParser parser = new YelExpressionParser(yelParserConfig);
        Map<String, Object> env = new HashMap<>();
        Context context = new Context(env);
        YelExpression ex1 = parser.parse("2 <= 2");
        Object v1 = ex1.getValue(context);
        Object v2 = ex1.getValue(context);
        Object v3 = ex1.getValue(context);
        Assertions.assertEquals(v1, v2);
        Assertions.assertEquals(v2, v3);

        ex1 = parser.parse("2 < 2");
        v1 = ex1.getValue(context);
        System.out.println(v1);
        v2 = ex1.getValue(context);
        System.out.println(v2);
        v3 = ex1.getValue(context);
        System.out.println(v3);
        Assertions.assertEquals(v1, v2);
        Assertions.assertEquals(v2, v3);


        ex1 = parser.parse("2 > 2");
        v1 = ex1.getValue(context);
        System.out.println(v1);
        v2 = ex1.getValue(context);
        System.out.println(v2);
        v3 = ex1.getValue(context);
        System.out.println(v3);
        Assertions.assertEquals(v1, v2);
        Assertions.assertEquals(v2, v3);

        ex1 = parser.parse("2 == 2");
        v1 = ex1.getValue(context);
        System.out.println(v1);
        v2 = ex1.getValue(context);
        System.out.println(v2);
        v3 = ex1.getValue(context);
        System.out.println(v3);
        Assertions.assertEquals(v1, v2);
        Assertions.assertEquals(v2, v3);


        ex1 = parser.parse("2.5 != 2");
        v1 = ex1.getValue(context);
        System.out.println(v1);
        v2 = ex1.getValue(context);
        System.out.println(v2);
        v3 = ex1.getValue(context);
        System.out.println(v3);
        Assertions.assertEquals(v1, v2);
        Assertions.assertEquals(v2, v3);
    }

    @Test
    public void testForCompareEnv() {

        YelParserConfig yelParserConfig = new YelParserConfig(YelCompilerMode.IMMEDIATE);
        yelParserConfig.setUseBigDecimalForFloat(false);
        YelExpressionParser parser = new YelExpressionParser(yelParserConfig);
        Map<String, Object> env = new HashMap<>();
        env.put("a", 1);
        env.put("b", 1);
        Context context = new Context(env);
        YelExpression ex1 = parser.parse("a == b");
        Object v1 = ex1.getValue(context);
        System.out.println(v1);
        Object v2 = ex1.getValue(context);
        System.out.println(v2);
        Object v3 = ex1.getValue(context);
        System.out.println(v3);
        Assertions.assertEquals(v1, v2);
        Assertions.assertEquals(v2, v3);
    }

}
