package com.zachary.bifromq.inbox.store.benchmark;

import com.zachary.bifromq.inbox.storage.proto.InboxInsertReply;
import com.zachary.bifromq.type.Message;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;

import static com.zachary.bifromq.type.QoS.AT_MOST_ONCE;

@Slf4j
@State(Scope.Benchmark)
public class QoS0InsertState extends InboxStoreState {
    private static final String tenantId = "testTraffic";

    private static final String topic = "greeting";
    private final Message msg = message(AT_MOST_ONCE, "hello");
    private static final int inboxCount = 100;

    @Override
    void afterSetup() {
        int i = 0;
        while (i < inboxCount) {
            requestCreate(tenantId, i + "", 100, 600, false);
            i++;
        }
    }

    @Override
    void beforeTeardown() {

    }

    public InboxInsertReply insert() {
        return requestInsert(tenantId, ThreadLocalRandom.current()
            .nextInt(0, inboxCount) + "", topic, msg);
    }
}
