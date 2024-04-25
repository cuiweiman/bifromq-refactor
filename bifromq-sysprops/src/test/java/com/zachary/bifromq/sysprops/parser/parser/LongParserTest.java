package com.zachary.bifromq.sysprops.parser.parser;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class LongParserTest {
    @Test
    public void parse() {
        LongParser parser = LongParser.POSITIVE;
        assertTrue(parser.parse("1") == 1);
    }

    @Test(expectedExceptions = SysPropParseException.class)
    public void parseException() {
        LongParser.POSITIVE.parse("0");
    }
}
