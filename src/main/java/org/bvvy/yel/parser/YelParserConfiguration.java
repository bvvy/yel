package org.bvvy.yel.parser;

/**
 * @author bvvy
 */
public class YelParserConfiguration {

    private final YelCompilerMode compilerMode;

    public YelParserConfiguration() {
        this(null);
    }

    public YelParserConfiguration(YelCompilerMode compilerMode) {
        this.compilerMode = compilerMode == null ? YelCompilerMode.OFF : compilerMode;
    }


}
