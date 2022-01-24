package org.bvvy.yel.exp;

import org.bvvy.yel.Yel;
import org.bvvy.yel.config.YelConfig;
import org.bvvy.yel.parser.YelCompilerMode;
import org.bvvy.yel.parser.YelParserConfig;
import org.junit.jupiter.api.Test;

/**
 * @author bvvy
 * @date 2022/1/24
 */
public class TestGenerateCode {

    @Test
    public void testGenerateCode() {
        YelParserConfig yelParserConfig = new YelParserConfig(YelCompilerMode.IMMEDIATE);
        YelConfig yelConfig = new YelConfig(yelParserConfig);
        Yel yel = new Yel(yelConfig);
    }
}
