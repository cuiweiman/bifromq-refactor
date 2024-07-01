package com.zachary.bifromq.retain.utils;

import com.google.protobuf.ByteString;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.zachary.bifromq.retain.utils.TopicUtil.escape;
import static com.zachary.bifromq.retain.utils.TopicUtil.parse;
import static com.google.protobuf.ByteString.copyFromUtf8;
import static com.google.protobuf.UnsafeByteOperations.unsafeWrap;

public class KeyUtil {
    public static ByteString tenantNS(String tenantId) {
        ByteString tenantIdBS = unsafeWrap(tenantId.getBytes(StandardCharsets.UTF_8));
        return toByteString(tenantIdBS.size()).concat(tenantIdBS);
    }

    public static ByteString retainKey(ByteString tenantNS, String topic) {
        return tenantNS.concat(unsafeWrap(new byte[] {(byte) parse(topic, false).size()}))
            .concat(copyFromUtf8(escape(topic)));
    }

    public static ByteString retainKeyPrefix(ByteString tenantNS, List<String> topicFilterLevels) {
        ByteString prefix = ByteString.empty();
        byte leastLevels = 0;
        for (String tfl : topicFilterLevels) {
            if ("+".equals(tfl)) {
                leastLevels++;
                break;
            }
            if ("#".equals(tfl)) {
                break;
            }
            leastLevels++;
            prefix = prefix.concat(copyFromUtf8(tfl));
        }
        return tenantNS.concat(unsafeWrap(new byte[] {leastLevels})).concat(prefix);
    }

    public static boolean isTenantNS(ByteString key) {
        int tenantIdLength = toInt(key.substring(0, Integer.BYTES));
        return key.size() == Integer.BYTES + tenantIdLength;
    }

    public static ByteString parseTenantNS(ByteString key) {
        int tenantIdLength = toInt(key.substring(0, Integer.BYTES));
        return key.substring(0, Integer.BYTES + tenantIdLength);
    }

    public static List<String> parseTopic(ByteString retainKey) {
        int tenantIdLength = toInt(retainKey.substring(0, Integer.BYTES));
        String escapedTopic = retainKey.substring(Integer.BYTES + tenantIdLength + 1).toStringUtf8();
        return parse(escapedTopic, true);
    }

    private static ByteString toByteString(int i) {
        return unsafeWrap(toBytes(i));
    }

    private static byte[] toBytes(int i) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(i).array();
    }

    private static int toInt(ByteString b) {
        assert b.size() == Integer.BYTES;
        ByteBuffer buffer = b.asReadOnlyByteBuffer();
        return buffer.getInt();
    }
}
