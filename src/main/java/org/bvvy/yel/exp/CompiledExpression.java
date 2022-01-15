package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;

/**
 * @author bvvy
 */
public abstract class CompiledExpression {

    public abstract Object getValue(Object target, Context context);

}
