package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.parser.YelCompilerMode;
import org.bvvy.yel.parser.YelExpressionParser;
import org.bvvy.yel.parser.YelParserConfig;
import org.junit.jupiter.api.Test;

/**
 * @author bvvy
 * @date 2022/1/24
 */
public class TestGenerateCode {

    @Test
    public void testGenerateCode() {
        YelParserConfig yelParserConfig = new YelParserConfig(YelCompilerMode.IMMEDIATE);
        YelExpressionParser parser = new YelExpressionParser(yelParserConfig);
        YelExpression ex1 = parser.parse("1 + 2 + 3");
        ex1.getValue(new Context(null));
        ex1.getValue(new Context(null));

    }
}
