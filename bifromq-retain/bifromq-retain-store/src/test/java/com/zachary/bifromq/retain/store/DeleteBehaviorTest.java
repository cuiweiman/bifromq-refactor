package com.zachary.bifromq.retain.store;

import com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply;
import com.zachary.bifromq.retain.rpc.proto.RetainCoProcReply;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DeleteBehaviorTest extends RetainStoreTest {
    @Test(groups = "integration")
    public void deleteFromEmptyRetainSet() {
        String tenantId = "tenantA";
        String topic = "/a/b/c";
        // empty payload signal deletion
        RetainCoProcReply reply = requestRetain(tenantId, 10, message(topic, ""));
        assertEquals(reply.getResult(), RetainCoProcReply.Result.CLEARED);
    }

    @Test(groups = "integration")
    public void deleteNonExisting() {
        String tenantId = "tenantA";
        // empty payload signal deletion
        assertEquals(requestRetain(tenantId, 10, message("/a/b/c", "hello")).getResult(),
            RetainCoProcReply.Result.RETAINED);

        assertEquals(requestRetain(tenantId, 10, message("/a", "")).getResult(),
            RetainCoProcReply.Result.CLEARED);
    }

    @Test(groups = "integration")
    public void deleteNonExpired() {
        String tenantId = "tenantA";
        String topic = "/a/b/c";
        // empty payload signal deletion
        assertEquals(requestRetain(tenantId, 10, message(topic, "hello")).getResult(),
            RetainCoProcReply.Result.RETAINED);

        assertEquals(requestRetain(tenantId, 10, message(topic, "")).getResult(),
            RetainCoProcReply.Result.CLEARED);

        MatchCoProcReply matchReply = requestMatch(tenantId, topic, 10);
        assertEquals(matchReply.getMessagesCount(), 0);
    }
}
