package org.bvvy.yel.parser;

import org.bvvy.yel.exp.Expression;

/**
 * @author bvvy
 * @date 2022/2/3
 */
public interface Parser {

    Expression parse(String expression);
}
