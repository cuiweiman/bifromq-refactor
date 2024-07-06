package com.zachary.bifromq.retain.store;

import com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply;
import com.zachary.bifromq.retain.rpc.proto.RetainCoProcReply;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ReplaceBehaviorTest extends RetainStoreTest {

    @Test(groups = "integration")
    public void replaceWithinLimit() {
        String tenantId = "tenantId";
        String topic = "/a";
        TopicMessage message = message(topic, "hello");
        TopicMessage message1 = message(topic, "world");
        assertEquals(requestRetain(tenantId, 1, message).getResult(),
            RetainCoProcReply.Result.RETAINED);

        assertEquals(requestRetain(tenantId, 1, message1).getResult(),
            RetainCoProcReply.Result.RETAINED);

        MatchCoProcReply matchReply = requestMatch(tenantId, topic, 10);
        assertEquals(matchReply.getMessagesCount(), 1);
        assertEquals(matchReply.getMessages(0), message1);

    }

    @Test(groups = "integration")
    public void replaceButExceedLimit() {
        String tenantId = "tenantId";
        assertEquals(requestRetain(tenantId, 2, message("/a", "hello")).getResult(),
            RetainCoProcReply.Result.RETAINED);

        TopicMessage message = message("/b", "world");
        assertEquals(requestRetain(tenantId, 2, message).getResult(),
            RetainCoProcReply.Result.RETAINED);

        // limit now shrink to 1
        assertEquals(requestRetain(tenantId, 1, message("/b", "!!!")).getResult(),
            RetainCoProcReply.Result.ERROR);

        MatchCoProcReply matchReply = requestMatch(tenantId, "/b", 10);
        assertEquals(matchReply.getMessagesCount(), 1);
        assertEquals(matchReply.getMessages(0), message);
    }
}
