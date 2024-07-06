package com.zachary.bifromq.retain.store;

import com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply;
import org.testng.annotations.Test;

import static com.google.common.collect.Sets.newHashSet;
import static org.testng.Assert.assertEquals;

public class MatchTest extends RetainStoreTest {

    @Test(groups = "integration")
    public void wildcardTopicFilter() {
        String tenantId = "tenantA";
        TopicMessage message1 = message("/a/b/c", "hello");
        TopicMessage message2 = message("/a/b/", "hello");
        TopicMessage message3 = message("/c/", "hello");
        requestRetain(tenantId, 10, message1);
        requestRetain(tenantId, 10, message2);
        requestRetain(tenantId, 10, message3);

        MatchCoProcReply matchReply = requestMatch(tenantId, "#", 10);
        assertEquals(matchReply.getMessagesCount(), 3);
        assertEquals(newHashSet(matchReply.getMessagesList()), newHashSet(message1, message2, message3));

        matchReply = requestMatch(tenantId, "/a/+", 10);
        assertEquals(matchReply.getMessagesCount(), 0);

        matchReply = requestMatch(tenantId, "/a/+/+", 10);
        assertEquals(matchReply.getMessagesCount(), 2);
        assertEquals(newHashSet(matchReply.getMessagesList()), newHashSet(message1, message2));

        matchReply = requestMatch(tenantId, "/+/b/", 10);
        assertEquals(matchReply.getMessagesCount(), 1);
        assertEquals(newHashSet(matchReply.getMessagesList()), newHashSet(message2));

        matchReply = requestMatch(tenantId, "/+/b/#", 10);
        assertEquals(matchReply.getMessagesCount(), 2);
        assertEquals(newHashSet(matchReply.getMessagesList()), newHashSet(message1, message2));
    }

    @Test(groups = "integration")
    public void matchLimit() {
        String tenantId = "tenantId";
        TopicMessage message1 = message("/a/b/c", "hello");
        TopicMessage message2 = message("/a/b/", "hello");
        TopicMessage message3 = message("/c/", "hello");
        requestRetain(tenantId, 10, message1);
        requestRetain(tenantId, 10, message2);
        requestRetain(tenantId, 10, message3);

        assertEquals(requestMatch(tenantId, "#", 0).getMessagesCount(), 0);
        assertEquals(requestMatch(tenantId, "#", 1).getMessagesCount(), 1);
    }
}
