package com.zachary.bifromq.dist.entity;

import com.zachary.bifromq.dist.rpc.proto.MatchRecord;
import com.zachary.bifromq.dist.util.TopicUtil;
import com.google.protobuf.ByteString;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.zachary.bifromq.dist.util.TopicUtil.NUL;
import static com.zachary.bifromq.dist.util.TopicUtil.escape;
import static com.zachary.bifromq.dist.util.TopicUtil.isNormalTopicFilter;
import static com.zachary.bifromq.dist.util.TopicUtil.parseSharedTopic;
import static com.zachary.bifromq.dist.util.TopicUtil.unescape;
import static com.google.protobuf.ByteString.copyFromUtf8;

public class EntityUtil {
    private static final ByteString INFIX_SUBINFO_INFIX = copyFromUtf8("0");
    private static final ByteString INFIX_MATCH_RECORD_INFIX = copyFromUtf8("1");
    private static final ByteString INFIX_UPPERBOUND_INFIX = copyFromUtf8("2");
    private static final ByteString FLAG_NORMAL = copyFromUtf8("0");
    private static final ByteString FLAG_UNORDERD_SHARE = copyFromUtf8("1");
    private static final ByteString FLAG_ORDERD_SHARE = copyFromUtf8("2");

    public static String toQualifiedInboxId(int subBrokerId, String inboxId, String delivererKey) {
        String scoped = subBrokerId + NUL + inboxId + NUL + delivererKey;
        return Base64.getEncoder().encodeToString(scoped.getBytes(StandardCharsets.UTF_8));
    }

    public static int parseSubBroker(String scopedInboxId) {
        scopedInboxId = new String(Base64.getDecoder().decode(scopedInboxId), StandardCharsets.UTF_8);
        int splitIdx = scopedInboxId.indexOf(NUL);
        return Integer.parseInt(scopedInboxId.substring(0, splitIdx));
    }

    public static String parseInboxId(String scopedInboxId) {
        scopedInboxId = new String(Base64.getDecoder().decode(scopedInboxId), StandardCharsets.UTF_8);
        int splitIdx = scopedInboxId.indexOf(NUL);
        return scopedInboxId.substring(splitIdx + 1, scopedInboxId.lastIndexOf(NUL));
    }

    public static ByteString tenantPrefix(String tenantId) {
        return copyFromUtf8(tenantId + NUL);
    }

    public static ByteString tenantUpperBound(String tenantId) {
        return tenantPrefix(tenantId).concat(INFIX_UPPERBOUND_INFIX);
    }

    public static ByteString subInfoKeyPrefix(String tenantId) {
        return tenantPrefix(tenantId).concat(INFIX_SUBINFO_INFIX);
    }

    public static ByteString subInfoKey(String tenantId, String scopedInboxId) {
        return subInfoKeyPrefix(tenantId).concat(copyFromUtf8(scopedInboxId));
    }

    public static boolean isSubInfoKey(ByteString rawKey) {
        String keyStr = rawKey.toStringUtf8();
        int firstSplit = keyStr.indexOf(NUL);
        return keyStr.substring(firstSplit + 1, firstSplit + 2).equals(INFIX_SUBINFO_INFIX.toStringUtf8());
    }

    public static String parseTenantId(ByteString rawKey) {
        String keyStr = rawKey.toStringUtf8();
        int firstSplit = keyStr.indexOf(NUL);
        return keyStr.substring(0, firstSplit);
    }

    public static String parseTenantId(String rawKeyUtf8) {
        int firstSplit = rawKeyUtf8.indexOf(NUL);
        return rawKeyUtf8.substring(0, firstSplit);
    }

    public static Inbox parseInbox(ByteString subInfoKey) {
        String subInfoKeyStr = subInfoKey.toStringUtf8();
        int firstSplit = subInfoKeyStr.indexOf(NUL);
        String scopedInboxId = subInfoKeyStr.substring(firstSplit + 2);
        return new Inbox(scopedInboxId);
    }

    public static Matching parseMatchRecord(ByteString matchRecordKey, ByteString matchRecordValue) {
        // <tenantId><NUL><1><ESCAPED_TOPIC_FILTER><NUL><FLAG><SCOPED_INBOX|SHARE_GROUP>
        String matchRecordKeyStr = matchRecordKey.toStringUtf8();
        int lastSplit = matchRecordKeyStr.lastIndexOf(NUL);
        char flag = matchRecordKeyStr.charAt(lastSplit + 1);
        try {
            MatchRecord matchRecord = MatchRecord.parseFrom(matchRecordValue);
            switch (flag) {
                case '0':
                    assert matchRecord.hasNormal();
                    String scopedInbox = matchRecordKeyStr.substring(lastSplit + 2);
                    return new NormalMatching(matchRecordKey, scopedInbox, matchRecord.getNormal());
                case '1':
                case '2':
                default:
                    assert matchRecord.hasGroup();
                    String group = matchRecordKeyStr.substring(lastSplit + 2);
                    return new GroupMatching(matchRecordKey, group, flag == '2', matchRecord.getGroup().getEntryMap());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse matching record", e);
        }
    }

    public static ByteString matchRecordKeyPrefix(String tenantId) {
        return tenantPrefix(tenantId).concat(INFIX_MATCH_RECORD_INFIX);
    }

    public static ByteString matchRecordKey(String tenantId, String topicFilter, String qInboxId) {
        if (isNormalTopicFilter(topicFilter)) {
            return matchRecordKeyPrefix(tenantId)
                .concat(copyFromUtf8(escape(topicFilter) + NUL))
                .concat(FLAG_NORMAL)
                .concat(copyFromUtf8(qInboxId));
        } else {
            TopicUtil.SharedTopicFilter stf = parseSharedTopic(topicFilter);
            return matchRecordKeyPrefix(tenantId)
                .concat(copyFromUtf8(escape(stf.topicFilter) + NUL))
                .concat(stf.ordered ? FLAG_ORDERD_SHARE : FLAG_UNORDERD_SHARE)
                .concat(copyFromUtf8(stf.shareGroup));
        }
    }

    public static ByteString matchRecordTopicFilterPrefix(String tenantId, String escapedTopicFilter) {
        return matchRecordKeyPrefix(tenantId).concat(copyFromUtf8(escapedTopicFilter + NUL));
    }

    public static String parseTopicFilter(String matchRecordKeyStr) {
        // <tenantId><NUL><1><ESCAPED_TOPIC_FILTER><NUL><FLAG><SCOPED_INBOX|SHARE_GROUP>
        int firstSplit = matchRecordKeyStr.indexOf(NUL);
        int lastSplit = matchRecordKeyStr.lastIndexOf(NUL);
        return unescape(matchRecordKeyStr.substring(firstSplit + 2, lastSplit));
    }
}
