package com.zachary.bifromq.basekv.store.util;

public class VerUtil {
    public static long bump(long ver, boolean toOdd) {
        return ver + (ver % 2 == 0 ? (toOdd ? 1 : 2) : (toOdd ? 2 : 1));
    }
}
