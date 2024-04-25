
package com.zachary.bifromq.basecrdt.store;

import com.google.protobuf.ByteString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public final class PartialMesh {
    private static class Ring {
        private final ArrayList<ByteString> ring;

        Ring(SortedSet keys) {
            ring = new ArrayList<>(keys);
        }

        int indexOf(ByteString key) {
            return ring.indexOf(key);
        }

        ByteString get(int i) {
            int idx = (i % ring.size() + ring.size()) % ring.size();
            return ring.get(idx);
        }
    }

    public static Set<ByteString> neighbors(SortedSet<ByteString> cluster, ByteString key) {
        return neighbors(cluster, Collections.emptySet(), key);
    }

    public static Set<ByteString> neighbors(SortedSet<ByteString> cluster, Set<ByteString> ignore, ByteString key) {
        Ring ring = new Ring(cluster);
        Set<ByteString> n = new TreeSet<>(ByteString.unsignedLexicographicalComparator());
        assert !ignore.contains(key);
        if (cluster.contains(key)) {
            int index = ring.indexOf(key);
            int offset = 1;
            while (ignore.contains(ring.get(index + offset))) {
                ++offset;
            }
            n.add(ring.get(index + offset));
            offset = 1;
            while (ignore.contains(ring.get(index - offset))) {
                ++offset;
            }
            n.add(ring.get(index - offset));

            // add more adjacent keys based the height of complete binary tree
            int step = step(cluster.size());
            for (int i = 1; i <= step; i++) {
                offset = 2 << (i - 1);
                while (ignore.contains(ring.get(index + offset)) || n.contains(ring.get(index + offset))) {
                    ++offset;
                    if (ring.get(index + offset) == key) {
                        break;
                    }
                }
                n.add(ring.get(index + offset));

                offset = 2 << (i - 1);
                while (ignore.contains(ring.get(index - offset)) || n.contains(ring.get(index - offset))) {
                    ++offset;
                    if (ring.get(index - offset) == key) {
                        break;
                    }
                }
                n.add(ring.get(index - offset));
            }
        }
        n.remove(key);
        return n;
    }

    static int step(int size) {
        return (int) (Math.log10(size) / Math.log10(20));
    }
}
