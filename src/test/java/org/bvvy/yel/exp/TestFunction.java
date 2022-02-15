package org.bvvy.yel.exp;

import org.bvvy.yel.context.GlobalMethodResolver;
import org.bvvy.yel.function.YelFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    }

    public static class Round implements YelFunction {

        private final RoundingMode roundingMode;

        public Round(RoundingMode roundingMode) {
            this.roundingMode = roundingMode;
        }

        public Round() {
            this(RoundingMode.HALF_UP);
        }

        public BigDecimal round(BigDecimal value, Integer scale) {
            return value.setScale(scale, roundingMode);
        }

        public BigDecimal round(BigDecimal value) {
            return round(value, 2);
        }
    }
}
