package com.zachary.bifromq.retain.store;

import com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply;
import com.zachary.bifromq.retain.rpc.proto.RetainCoProcReply;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RetainBehaviorTest extends RetainStoreTest {

    @Test(groups = "integration")
    public void retainFirstMessage() {
        String tenantId = "tenantId";
        String topic = "/a/b/c";
        TopicMessage message = message(topic, "hello");
        RetainCoProcReply reply = requestRetain(tenantId, 10, message);
        assertEquals(reply.getResult(), RetainCoProcReply.Result.RETAINED);

        MatchCoProcReply matchReply = requestMatch(tenantId, topic, 10);
        assertEquals(matchReply.getMessagesCount(), 1);
        assertEquals(matchReply.getMessages(0), message);
    }

    @Test(groups = "integration")
    public void retainFirstMessageWithZeroMaxRetainedTopics() {
        String tenantId = "tenantId";
        String topic = "/a/b/c";
        RetainCoProcReply reply = requestRetain(tenantId, 0, message(topic, "hello"));
        assertEquals(reply.getResult(), RetainCoProcReply.Result.ERROR);

        MatchCoProcReply matchReply = requestMatch(tenantId, topic, 10);
        assertEquals(matchReply.getMessagesCount(), 0);
    }

    @Test(groups = "integration")
    public void retainNewExceedLimit() {
        String tenantId = "tenantId";
        assertEquals(requestRetain(tenantId, 1, message("/a", "hello")).getResult(),
            RetainCoProcReply.Result.RETAINED);

        assertEquals(requestRetain(tenantId, 1, message("/b", "hello")).getResult(),
            RetainCoProcReply.Result.ERROR);

        MatchCoProcReply matchReply = requestMatch(tenantId, "/b", 10);
        assertEquals(matchReply.getMessagesCount(), 0);
    }
}
