package org.bvvy.yel.parser;

/**
 * @author bvvy
 */
public class YelParserConfig {

    private final YelCompilerMode compilerMode;
    private boolean useBigDecimalForFloat;

    public YelParserConfig() {
        this(null);
    }

    public YelParserConfig(YelCompilerMode compilerMode) {
        this.compilerMode = compilerMode == null ? YelCompilerMode.OFF : compilerMode;
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
}
