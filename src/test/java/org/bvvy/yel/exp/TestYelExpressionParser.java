package org.bvvy.yel.exp;

import org.bvvy.yel.exp.ast.*;
import org.bvvy.yel.parser.YelExpressionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestYelExpressionParser {

    public Node parse(String expression) {
        YelExpressionParser yelExpressionParser = new YelExpressionParser();
        YelExpression yeExpr = yelExpressionParser.parse(expression);
        return yeExpr.getAst();
    }


    @Test
    public void testIntLiteral() {
        Node node = parse("1");
        Assertions.assertEquals(node.getClass(), IntLiteral.class);
    }

    @Test
    public void testDecimalLiteral() {
        Node node = parse("1.0");
        Assertions.assertEquals(node.getClass(), RealLiteral.class);
    }

    @Test
    public void testString() {
        Node singleQuotedNode = parse("'1.0'");
        Node doubleQuotedNode = parse("\"1.0\"");
        Assertions.assertEquals(singleQuotedNode.getClass(), StringLiteral.class);
        Assertions.assertEquals(doubleQuotedNode.getClass(), StringLiteral.class);
    }

    @Test
    public void testSum() {
        Node plus = parse("1+1");
        Assertions.assertEquals(plus.getClass(), OpPlus.class);
        Node minus = parse("1-1");
        Assertions.assertEquals(minus.getClass(), OpMinus.class);
    }

    @Test
    public void testRelation() {
        Node node = parse("1 > 1");
        Assertions.assertEquals(OpGT.class, node.getClass());

        node = parse("1 < 1");
        Assertions.assertEquals(OpLT.class, node.getClass());

        node = parse("1 == 1");
        Assertions.assertEquals(OpEQ.class, node.getClass());

        node = parse("1 >= 1");
        Assertions.assertEquals(OpGE.class, node.getClass());

        node = parse("1 <= 1");
        Assertions.assertEquals(OpLE.class, node.getClass());

        node = parse("1 != 1");
        Assertions.assertEquals(OpNE.class, node.getClass());
    }

    @Test
    public void testAndOr() {
        Node node = parse("1 && 1");
        Assertions.assertEquals(OpAnd.class, node.getClass());

        node = parse("1 || 1");
        Assertions.assertEquals(OpOr.class, node.getClass());

    }

    @Test
    public void testBit() {
        Node node = parse("1 & 1");
        Assertions.assertEquals(OpBitAnd.class, node.getClass());
        node = parse("1 | 1");
        Assertions.assertEquals(OpBitOr.class, node.getClass());
        node = parse("1 ^ 1");
        Assertions.assertEquals(OpBitXor.class, node.getClass());
    }

    @Test
    public void testAssign() {

        Node node = parse("a=1");
        Assertions.assertEquals(Assign.class, node.getClass());

        node = parse("a ?: 1");
        Assertions.assertEquals(Elvis.class, node.getClass());

        node = parse("a ? 1 : 2");
        Assertions.assertEquals(Ternary.class, node.getClass());


    }


    @Test
    public void testIndex() {

        Node node = parse("a[1]");
        Assertions.assertEquals(CompoundExpression.class, node.getClass());

    }

    @Test
    public void testMethod() {
        Node node = parse("test(1)");
        Assertions.assertEquals(MethodReference.class, node.getClass());
    }

    @Test
    public void testVar() {
        Node node = parse("a");
        Assertions.assertEquals(PropertyOrFieldReference.class, node.getClass());
    }
}
