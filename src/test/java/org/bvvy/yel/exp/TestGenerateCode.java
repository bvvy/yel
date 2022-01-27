package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.parser.YelCompilerMode;
import org.bvvy.yel.parser.YelExpressionParser;
import org.bvvy.yel.parser.YelParserConfig;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        yelParserConfig.setUseBigDecimalForFloat(true);
        YelExpressionParser parser = new YelExpressionParser(yelParserConfig);
        Map<String, Object> env = new HashMap<>();
        Context context = new Context(env);
        YelExpression ex1 = parser.parse("1.1 + 1.1");
        LocalDateTime start = LocalDateTime.now();
        System.out.println(ex1.getValue(context));
        System.out.println(ex1.getValue(context));
        System.out.println(start.until(LocalDateTime.now(), ChronoUnit.MILLIS));

    }
}
