package com.zachary.bifromq.basekv.localengine;

import com.google.protobuf.ByteString;
import com.google.protobuf.UnsafeByteOperations;

import static com.google.protobuf.UnsafeByteOperations.unsafeWrap;

public class RangeUtil {
    public static boolean isValid(ByteString start, ByteString end) {
        if (start == null || end == null) {
            return true;
        }
        return compare(start, end) <= 0;
    }

    public static boolean inRange(ByteString key, ByteString start, ByteString end) {
        assert isValid(start, end);
        if (start == null && end == null) {
            return true;
        }
        if (end == null) {
            // right open range
            return compare(start, key) <= 0;
        }
        if (start == null) {
            return compare(key, end) < 0;
        }
        return compare(start, key) <= 0 && compare(key, end) < 0;
    }

    public static boolean inRange(ByteString start1, ByteString end1, ByteString start2, ByteString end2) {
        assert isValid(start1, end1) && isValid(start2, end2);

        if (start2 == null && end2 == null) {
            // open-ended range
            return true;
        }
        if (start2 != null && end2 == null) {
            // right open-ended
            if (start1 == null) {
                return ByteString.EMPTY.equals(end1);
            }
            // start2 <= start1
            return compare(start1, start2) >= 0;
        }
        if (start2 == null) {
            // left open-ended
            if (end1 == null) {
                return false;
            }
            // end1 <= end2
            return compare(end1, end2) <= 0;
        }
        // range2 is closed ended
        if (start1 == null && end1 == null) {
            return false;
        } else if (start1 == null) {
            return ByteString.EMPTY.equals(end1);
        } else {
            return compare(start2, start1) <= 0 && compare(end1, end2) <= 0;
        }
    }

    public static ByteString upperBound(ByteString key) {
        return UnsafeByteOperations.unsafeWrap(upperBound(key.toByteArray()));
    }

    public static byte[] upperBound(byte[] key) {
        byte[] upperBound = new byte[key.length];
        System.arraycopy(key, 0, upperBound, 0, key.length);
        int i = key.length;
        while (--i >= 0) {
            byte b = upperBound[i];
            if (compare(new byte[] {b}, new byte[] {(byte) 0xFF}) < 0) {
                upperBound[i]++;
                break;
            }
        }
        return upperBound;
    }

    public static int compare(byte[] a, byte[] b) {
        return compare(unsafeWrap(a), unsafeWrap(b));
    }

    public static int compare(ByteString a, ByteString b) {
        return ByteString.unsignedLexicographicalComparator().compare(a, b);
    }
}
