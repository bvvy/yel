package org.bvvy.yel.parser;

/**
 * @author bvvy
 */
public class YelParserConfig {

    private final YelCompilerMode compilerMode;

    public YelParserConfig() {
        this(null);
    }

    public YelParserConfig(YelCompilerMode compilerMode) {
        this.compilerMode = compilerMode == null ? YelCompilerMode.OFF : compilerMode;
    }

    public YelCompilerMode getCompilerMode() {
        return compilerMode;
    }
}
