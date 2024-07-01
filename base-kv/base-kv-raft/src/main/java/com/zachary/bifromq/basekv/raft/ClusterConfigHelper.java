package com.zachary.bifromq.basekv.raft;

import java.util.Set;

public class ClusterConfigHelper {
    public static <T> boolean isIntersect(Set<T> s1, Set<T> s2) {
        Set<T> small = s1.size() > s2.size() ? s2 : s1;
        Set<T> large = s1.size() > s2.size() ? s1 : s2;
        for (T item : small) {
            if (large.contains(item)) {
                return true;
            }
        }
        return false;
    }
}
