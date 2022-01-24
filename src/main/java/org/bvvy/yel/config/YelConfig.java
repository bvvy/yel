package org.bvvy.yel.config;

import org.bvvy.yel.parser.YelParserConfig;

/**
 * @author bvvy
 * @date 2022/1/24
 */
public class YelConfig {

    private final YelParserConfig yelParserConfig;

    public YelConfig() {
        this.yelParserConfig = new YelParserConfig();
    }

    public YelConfig(YelParserConfig yelParserConfig) {
        this.yelParserConfig = yelParserConfig;
    }

    public YelParserConfig getYelParserConfig() {
        return yelParserConfig;
    }
}
