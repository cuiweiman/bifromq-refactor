package com.zachary.bifromq.plugin.manager;

import java.util.HashSet;
import java.util.Set;

public class ProvidedPackages {
    private static final Set<String> PACKAGES = new HashSet<>();

    static {
        PACKAGES.add("com.zachary.bifromq.");
        PACKAGES.add("io.micrometer.core");
        PACKAGES.add("com.google.protobuf");
        PACKAGES.add("org.slf4j.");
    }

    public static boolean isProvided(String className) {
        return PACKAGES.stream().anyMatch(className::startsWith);
    }
}
