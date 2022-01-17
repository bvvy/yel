package org.bvvy.yel.exp;

import org.bvvy.yel.exp.ast.Node;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bvvy
 */
public class YelCompiler {
    private AtomicInteger suffixId = new AtomicInteger(1);

    public CompiledExpression compile(Node expression) {
        Class<? extends CompiledExpression> clazz = createExpressionClass(expression);
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Class<? extends CompiledExpression> createExpressionClass(Node expressionToCompile) {
        String className = "yel/Ex" + getNextSuffix();
        String contextClass = "org/bvvy/yel/context/Context";

        return null;
    }

    private int getNextSuffix() {
        return this.suffixId.incrementAndGet();
    }
}
