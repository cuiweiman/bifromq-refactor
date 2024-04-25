package com.zachary.bifromq.sysprops.parser.parser;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DoubleParserTest {

    @Test
    public void parse() {
        DoubleParser parser = DoubleParser.from(0.0, 1.0, false);
        assertEquals(Double.compare(0.0, parser.parse("0.0")), 0.0);
    }

    @Test(expectedExceptions = SysPropParseException.class)
    public void parseFail() {
        DoubleParser parser = DoubleParser.from(0.0, 1.0, true);
        assertEquals(Double.compare(0.0, parser.parse("0.0")), 0.0);
    }

}
