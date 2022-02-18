package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;

/**
 * @author bvvy
 */
public interface Expression {

    Object getValue(Context context);

    Object getValue();
}
