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
    public void testBitOperator() {
        Yel yel = new Yel();
        Object result = yel.eval("1 & 1");
        Assertions.assertEquals(1, result);
        result = yel.eval("1 | 1");
        Assertions.assertEquals(1, result);
        result = yel.eval("1 ^ 1");
        Assertions.assertEquals(0, result);
        result = yel.eval("3 ^ 3 & 2 | 2");
        Assertions.assertEquals(1, result);
    }


    @Test
    public void testMethod() {
        Yel yel = new Yel();
        Tester tester = new Tester();
        Object result = yel.eval("say('Lee')", tester);
        Assertions.assertEquals("Hi Lee, I am Tester", result);
        result = yel.eval("say(10)", tester);
        Assertions.assertEquals("I am Tester; I am 10 years old", result);
        result = yel.eval("say(10.0)", tester);
        Assertions.assertEquals("I am Tester; I am 10 years old", result);

    }

    @Test
    public void testProperty() {
        Yel yel = new Yel();
        Tester tester = new Tester();
        Object result = yel.eval("name", tester);
        System.out.println(result);
    }

    @Test
    public void testAssignFrom() {

        System.out.println(Number.class.isAssignableFrom(Integer.class));
    }
}
