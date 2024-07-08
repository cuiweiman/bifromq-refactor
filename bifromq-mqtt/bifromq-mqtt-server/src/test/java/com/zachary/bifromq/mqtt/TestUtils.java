package com.zachary.bifromq.mqtt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestUtils {
    interface ToThrow {
        void run() throws Throwable;
    }

    protected static <T extends Throwable> T expectThrow(ToThrow test) {
        try {
            test.run();
            throw new RuntimeException("Failed");
        } catch (Throwable r) {
            return (T) r;
        }
    }
}
