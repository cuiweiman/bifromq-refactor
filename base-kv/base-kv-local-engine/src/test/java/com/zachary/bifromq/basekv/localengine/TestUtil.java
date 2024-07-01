package com.zachary.bifromq.basekv.localengine;

import com.google.protobuf.ByteString;
import com.google.protobuf.UnsafeByteOperations;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Slf4j
public class TestUtil {
    public static long toLong(ByteString b) {
        assert b.size() == Long.BYTES;
        ByteBuffer buffer = b.asReadOnlyByteBuffer();
        return buffer.getLong();
    }

    public static ByteString toByteString(long l) {
        return UnsafeByteOperations.unsafeWrap(toBytes(l));
    }

    public static byte[] toBytes(long l) {
        return ByteBuffer.allocate(Long.BYTES).putLong(l).array();
    }

    public static ByteString toByteStringNativeOrder(long l) {
        return UnsafeByteOperations.unsafeWrap(ByteBuffer.allocate(Long.BYTES)
            .order(ByteOrder.nativeOrder())
            .putLong(l).array());
    }


    public static long toLongNativeOrder(ByteString b) {
        assert b.size() == Long.BYTES;
        ByteBuffer buffer = b.asReadOnlyByteBuffer().order(ByteOrder.nativeOrder());
        return buffer.getLong();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Object targetObject, String fieldName) {
        try {
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(targetObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("get field {} from {} failed: {}", targetObject, fieldName, e.getMessage());
        }
        return null;
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
}
