package com.zachary.bifromq.basekv;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Slf4j
public class TestUtil {

    public static <T extends Throwable> void assertThrows(Class<T> tClass, Runnable runnable) {
        try {
            runnable.run();
            fail();
        } catch (Throwable e) {
            assertTrue(tClass.isInstance(e));
        }
    }

    public static void deleteDir(String dir) {
        try {
            Files.walk(Paths.get(dir))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        } catch (IOException e) {
            log.error("Failed to delete db root dir", e);
        }
    }

    public static boolean isDevEnv() {
        String osName = System.getProperty("os.name");
        return osName != null && osName.startsWith("Mac");
    }
}
