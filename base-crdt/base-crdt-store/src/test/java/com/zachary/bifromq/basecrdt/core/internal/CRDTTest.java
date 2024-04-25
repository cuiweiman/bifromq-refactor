package com.zachary.bifromq.basecrdt.core.internal;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.proto.Replacement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class CRDTTest {
    protected ScheduledExecutorService executor;

    @BeforeMethod
    public void setup() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @AfterMethod
    public void teardown() {
        MoreExecutors.shutdownAndAwaitTermination(executor, 5, TimeUnit.SECONDS);
    }

    protected IReplicaStateLattice newStateLattice(ByteString ownerReplicaId, long historyDurationInMS) {
        return new InMemReplicaStateLattice(ownerReplicaId,
                Duration.ofMillis(historyDurationInMS),
                Duration.ofMillis(200));
    }

    protected void sync(CausalCRDTInflater left, CausalCRDTInflater right) {
        CompletableFuture<Optional<Iterable<Replacement>>> deltaProto =
                left.delta(right.latticeEvents(), right.historyEvents(), 1024);
        if (deltaProto.join().isPresent()) {
            right.join(deltaProto.join().get()).join();
        }
        deltaProto = right.delta(left.latticeEvents(), left.historyEvents(), 1024);
        if (deltaProto.join().isPresent()) {
            left.join(deltaProto.join().get()).join();
        }
    }
}
