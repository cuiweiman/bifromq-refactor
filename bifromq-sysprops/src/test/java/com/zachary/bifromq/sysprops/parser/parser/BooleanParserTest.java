package com.zachary.bifromq.sysprops.parser.parser;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BooleanParserTest {

    @Test
    public void testParse() {
        assertTrue(BooleanParser.INSTANCE.parse("true"));
        assertTrue(BooleanParser.INSTANCE.parse("True"));
        assertTrue(BooleanParser.INSTANCE.parse("1"));
        assertTrue(BooleanParser.INSTANCE.parse("yes"));

        assertFalse(BooleanParser.INSTANCE.parse("false"));
        assertFalse(BooleanParser.INSTANCE.parse("False"));
        assertFalse(BooleanParser.INSTANCE.parse("0"));
        assertFalse(BooleanParser.INSTANCE.parse("no"));
    }
}