package org.bvvy.yel.exp;

import org.bvvy.yel.Yel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class TestYel {

    @Test
    public void testAdd() {
        Yel yel = new Yel();

        Object result = yel.eval("100 + 100");
        Assertions.assertEquals(result, 200);
        result = yel.eval("+100");
        Assertions.assertEquals(result, 100);

    }

    @Test
    public void testSub() {
        Yel yel = new Yel();
        Object result = yel.eval("100 - 100");
        Assertions.assertEquals(0, result);
        result = yel.eval("-100");
        Assertions.assertEquals(-100, result);
    }

    @Test
    public void testVar() {
        Yel yel = new Yel();
        Map<String, Object> env = new HashMap<>();
        env.put("a", 100);
        Object result = yel.eval("a", env);
        Assertions.assertEquals(100, result);
    }

    @Test
    public void testIndexer() {
        Yel yel = new Yel();
        Map<String, Object> env = new HashMap<>();
        env.put("a", Arrays.asList(100, 200));
        Object result = yel.eval("a[1]", env);
        Assertions.assertEquals(200, result);
    }

    @Test
    public void testMethod() {
        Yel yel = new Yel();
        Object result = yel.eval("say('Lee')", new Tester());
        Assertions.assertEquals("Hi Lee, I am Tester", result);
    }
}
