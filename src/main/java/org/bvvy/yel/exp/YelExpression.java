package org.bvvy.yel.exp;

import org.bvvy.yel.context.Context;
import org.bvvy.yel.context.StandardContext;
import org.bvvy.yel.exception.YelEvalException;
import org.bvvy.yel.exp.ast.Node;
import org.bvvy.yel.parser.YelCompilerMode;
import org.bvvy.yel.parser.YelParserConfig;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class YelExpression implements Expression {

    private final Node ast;

    private final YelParserConfig configuration;

    private CompiledExpression compiledAst;

    private final AtomicInteger interpretedCount = new AtomicInteger();

    private YelCompiler yelCompiler;

    public YelExpression(Node ast, YelParserConfig configuration) {
        this.ast = ast;
        this.configuration = configuration;
        this.yelCompiler = configuration.getYelCompiler();
    }

    @Override
    public Object getValue() {
        return getValue(new StandardContext());
    }

    protected ExpressionState createExpressionState(Context context) {
        return new ExpressionState(context);
    }

    @Override
    public Object getValue(Context context) {
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            try {
                return compiledAst.getValue(context.getRootObject().getValue(), context);
            } catch (Exception e) {
                throw new YelEvalException(e, YelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION);
            }
        }
        ExpressionState state = createExpressionState(context);
        checkCompile();
        return ast.getValue(state);
    }

    private void checkCompile() {
        interpretedCount.incrementAndGet();
        YelCompilerMode compilerMode = this.configuration.getCompilerMode();
        if (compilerMode != YelCompilerMode.OFF) {
            if (compilerMode == YelCompilerMode.IMMEDIATE) {
                if (this.interpretedCount.get() > 1) {
                    compileExpression();
                }
            }
        }
    }

    public boolean compileExpression() {
        CompiledExpression compiledAst = this.compiledAst;
        if (compiledAst != null) {
            return true;
        }
        synchronized (this) {
            if (this.compiledAst != null) {
                return true;
            }
            YelCompiler compiler = this.yelCompiler;
            compiledAst = compiler.compile(this.ast);
            if (compiledAst != null) {
                this.compiledAst = compiledAst;
                return true;
            } else {
                return false;
            }
        }
    }

    public Node getAst() {
        return ast;
    }
}
