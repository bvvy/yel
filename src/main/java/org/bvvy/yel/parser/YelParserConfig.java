package org.bvvy.yel.parser;

import org.bvvy.yel.exp.YelCompiler;

/**
 * @author bvvy
 */
public class YelParserConfig {

    private final YelCompilerMode compilerMode;
    private boolean useBigDecimalForFloat;
    private YelCompiler yelCompiler;

    public YelParserConfig() {
        this(null);
    }

    public YelParserConfig(YelCompilerMode compilerMode) {
        this.compilerMode = compilerMode == null ? YelCompilerMode.OFF : compilerMode;
        this.yelCompiler = new YelCompiler();
    }

    public void setUseBigDecimalForFloat(boolean useBigDecimalForFloat) {
        this.useBigDecimalForFloat = useBigDecimalForFloat;
    }

    public boolean isUseBigDecimalForFloat() {
        return useBigDecimalForFloat;
    }

    public YelCompilerMode getCompilerMode() {
        return compilerMode;
    }

    public YelCompiler getYelCompiler() {
        return this.yelCompiler;
    }
}
