package org.bvvy.yel.exp;

import org.bvvy.yel.Yel;
import org.bvvy.yel.context.Context;
import org.bvvy.yel.context.method.GlobalMethodResolver;
import org.bvvy.yel.function.GlobalFunction;
import org.bvvy.yel.function.YelFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bvvy
 * @date 2022/2/15
 */
public class TestFunction {

    @Test
    public void testFunction() {

        GlobalMethodResolver globalMethodResolver = new GlobalMethodResolver();
        globalMethodResolver.registerFunction("ROUND", new Round());
        globalMethodResolver.registerFunction("ROUNDUP", new Round(RoundingMode.UP));
        globalMethodResolver.registerFunction("ROUNDDOWN", new Round(RoundingMode.DOWN));

        Yel yel = new Yel();
        Map<String, Object> env = new HashMap<>();
        env.put("a", 3.123);
        Context context = new Context(env);

        context.setMethodResolvers(Collections.singletonList(globalMethodResolver));
        Expression expression = yel.parse("ROUNDUP(a)");
        Object value = expression.getValue(context);

        env.put("a", new BigDecimal("3.455"));
        context.setRootObject(env);

        System.out.println(value);
        value = expression.getValue(context);
        System.out.println(value);

    }

    public static class Round implements YelFunction {

        private final RoundingMode roundingMode;

        public Round(RoundingMode roundingMode) {
            this.roundingMode = roundingMode;
        }

        public Round() {
            this(RoundingMode.HALF_UP);
        }

        @GlobalFunction
        public BigDecimal round(BigDecimal value, Integer scale) {
            return value.setScale(scale, roundingMode);
        }

        @GlobalFunction
        public BigDecimal round(BigDecimal value) {
            return round(value, 2);
        }
    }
}
