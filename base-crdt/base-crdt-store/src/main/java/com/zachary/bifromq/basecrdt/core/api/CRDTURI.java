package com.zachary.bifromq.basecrdt.core.api;

import com.google.common.base.Preconditions;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;

import java.util.regex.Pattern;

public final class CRDTURI {
    private static final Pattern NAME;
    private static final Pattern URI;

    static {
        String namePattern = "[\\w-:.]+";
        StringBuilder uriPattern = new StringBuilder("^(");
        for (CausalCRDTType type : CausalCRDTType.values()) {
            uriPattern.append(type.name());
            uriPattern.append("|");
        }
        uriPattern.deleteCharAt(uriPattern.length() - 1);
        uriPattern.append("):" + namePattern);
        NAME = Pattern.compile(namePattern);
        URI = Pattern.compile(uriPattern.toString());
    }

    public static boolean isValidURI(String uri) {
        return URI.matcher(uri).matches();
    }

    public static String toURI(CausalCRDTType type, String name) {
        Preconditions.checkArgument(NAME.matcher(name).matches(), "Invalid name");
        return type.name() + ":" + name;
    }

    public static CausalCRDTType parseType(String uri) {
        checkURI(uri);
        return CausalCRDTType.valueOf(uri.split(":")[0]);
    }

    public static String parseName(String uri) {
        checkURI(uri);
        return uri.substring(uri.indexOf(":") + 1);
    }

    public static void checkURI(String uri) {
        Preconditions.checkArgument(isValidURI(uri), "Invalid CRDT replica uri format");
    }
}
