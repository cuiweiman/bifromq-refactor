package com.zachary.bifromq.dist.server;

import com.zachary.bifromq.baserpc.ResponsePipeline;
import com.zachary.bifromq.basescheduler.exception.DropException;
import com.zachary.bifromq.dist.rpc.proto.DistReply;
import com.zachary.bifromq.dist.rpc.proto.DistRequest;
import com.zachary.bifromq.dist.server.scheduler.DistCall;
import com.zachary.bifromq.dist.server.scheduler.DistCallScheduler;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.eventcollector.distservice.DistError;
import com.zachary.bifromq.plugin.eventcollector.distservice.Disted;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.zachary.bifromq.plugin.eventcollector.ThreadLocalEventPool.getLocal;
import static com.zachary.bifromq.plugin.eventcollector.distservice.DistError.DistErrorCode.DROP_EXCEED_LIMIT;
import static com.zachary.bifromq.plugin.eventcollector.distservice.DistError.DistErrorCode.RPC_FAILURE;
import static com.zachary.bifromq.sysprops.parser.BifroMQSysProp.DIST_WORKER_CALL_QUEUES;

@Slf4j
class DistResponsePipeline extends ResponsePipeline<DistRequest, DistReply> {
    private final IEventCollector eventCollector;
    private final DistCallScheduler distCallScheduler;
    private final LoadingCache<String, RunningAverage> tenantFanouts;
    private final Integer callQueueIdx;

    DistResponsePipeline(DistCallScheduler distCallScheduler,
                         StreamObserver<DistReply> responseObserver,
                         IEventCollector eventCollector,
                         LoadingCache<String, RunningAverage> tenantFanouts) {
        super(responseObserver);
        this.distCallScheduler = distCallScheduler;
        this.eventCollector = eventCollector;
        this.tenantFanouts = tenantFanouts;
        this.callQueueIdx = DistQueueAllocator.allocate();
    }

    @Override
    protected CompletableFuture<DistReply> handleRequest(String tenantId, DistRequest request) {
        return distCallScheduler.schedule(new DistCall(tenantId, request.getMessagesList(),
                callQueueIdx, tenantFanouts.get(tenantId).estimate()))
            .handle((v, e) -> {
                if (e != null) {
                    eventCollector.report(getLocal(DistError.class)
                        .reqId(request.getReqId())
                        .messages(request.getMessagesList())
                        .code(e.getCause() == DropException.EXCEED_LIMIT ? DROP_EXCEED_LIMIT : RPC_FAILURE));
                } else {
                    tenantFanouts.get(tenantId).log(v.values().stream().reduce(0, Integer::sum) / v.size());
                    eventCollector.report(getLocal(Disted.class)
                        .reqId(request.getReqId())
                        .messages(request.getMessagesList())
                        .fanout(v.values().stream().reduce(0, Integer::sum)));
                }
                return DistReply.newBuilder().setReqId(request.getReqId()).build();
            });
    }

    private static class DistQueueAllocator {
        private static final int QUEUE_NUMS = DIST_WORKER_CALL_QUEUES.get();
        private static final AtomicInteger IDX = new AtomicInteger(0);

        public static int allocate() {
            return IDX.getAndIncrement() % QUEUE_NUMS;
        }
    }
}
