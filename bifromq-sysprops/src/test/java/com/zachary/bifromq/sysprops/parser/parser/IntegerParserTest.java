package com.zachary.bifromq.sysprops.parser.parser;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class IntegerParserTest {
    @Test
    public void parse() {
        IntegerParser parser = IntegerParser.POSITIVE;
        assertEquals((int) parser.parse("1"), 1);
    }

    @Test(expectedExceptions = SysPropParseException.class)
    public void parseException() {
        IntegerParser.POSITIVE.parse("0");
    }
}
