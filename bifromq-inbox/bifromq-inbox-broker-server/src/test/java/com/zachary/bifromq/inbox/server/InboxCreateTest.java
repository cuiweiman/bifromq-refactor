package com.zachary.bifromq.inbox.server;

import com.zachary.bifromq.inbox.rpc.proto.CreateInboxReply;
import com.zachary.bifromq.inbox.rpc.proto.DeleteInboxReply;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class InboxCreateTest extends InboxServiceTest {

    @Test(groups = "integration")
    public void create() {
        String tenantId = "trafficA";
        String inboxId = "inbox1";
        ClientInfo clientInfo = ClientInfo.newBuilder().setTenantId(tenantId).build();
        long reqId = System.nanoTime();
        assertFalse(inboxReaderClient.has(reqId, inboxId, clientInfo).join());

        CreateInboxReply createInboxReply = inboxReaderClient.create(reqId, inboxId, clientInfo).join();
        assertEquals(createInboxReply.getReqId(), reqId);
        assertEquals(createInboxReply.getResult(), CreateInboxReply.Result.OK);

        assertTrue(inboxReaderClient.has(reqId, inboxId, clientInfo).join());
    }

    @Test(groups = "integration")
    public void delete() {
        String tenantId = "trafficA";
        String inboxId = "inbox1";
        ClientInfo clientInfo = ClientInfo.newBuilder().setTenantId(tenantId).build();
        long reqId = System.nanoTime();

        CreateInboxReply createInboxReply = inboxReaderClient.create(reqId, inboxId, clientInfo).join();
        assertEquals(createInboxReply.getReqId(), reqId);
        assertEquals(createInboxReply.getResult(), CreateInboxReply.Result.OK);

        DeleteInboxReply deleteInboxReply = inboxReaderClient.delete(reqId, inboxId, clientInfo).join();
        assertEquals(deleteInboxReply.getReqId(), reqId);
        assertEquals(deleteInboxReply.getResult(), DeleteInboxReply.Result.OK);

        assertFalse(inboxReaderClient.has(reqId, inboxId, clientInfo).join());
    }
}
