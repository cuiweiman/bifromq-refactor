package com.zachary.bifromq.inbox.client;

import com.zachary.bifromq.baserpc.IRPCClient;
import com.zachary.bifromq.inbox.rpc.proto.CommitReply;
import com.zachary.bifromq.inbox.rpc.proto.CommitRequest;
import com.zachary.bifromq.inbox.rpc.proto.FetchHint;
import com.zachary.bifromq.inbox.rpc.proto.InboxServiceGrpc;
import com.zachary.bifromq.inbox.storage.proto.Fetched;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.zachary.bifromq.inbox.util.PipelineUtil.PIPELINE_ATTR_KEY_CLIENT_INFO;
import static com.zachary.bifromq.inbox.util.PipelineUtil.PIPELINE_ATTR_KEY_INBOX_ID;
import static com.zachary.bifromq.inbox.util.PipelineUtil.PIPELINE_ATTR_KEY_QOS0_LAST_FETCH_SEQ;
import static com.zachary.bifromq.inbox.util.PipelineUtil.PIPELINE_ATTR_KEY_QOS2_LAST_FETCH_SEQ;
import static com.zachary.bifromq.inbox.util.PipelineUtil.encode;

@Slf4j
class InboxReaderPipeline implements IInboxReaderClient.IInboxReader {
    private final IRPCClient.IMessageStream<Fetched, FetchHint> ppln;
    private final CompositeDisposable consumptions = new CompositeDisposable();
    private final ClientInfo clientInfo;
    private final String inboxId;
    private final IRPCClient rpcClient;
    private volatile long lastFetchQoS0Seq = -1;
    private volatile long lastFetchQoS2Seq = -1;

    InboxReaderPipeline(String inboxId, String delivererKey, ClientInfo clientInfo, IRPCClient rpcClient) {
        this.clientInfo = clientInfo;
        this.inboxId = inboxId;
        this.rpcClient = rpcClient;
        Map<String, String> metadata = new HashMap<>() {{
            put(PIPELINE_ATTR_KEY_INBOX_ID, inboxId);
            put(PIPELINE_ATTR_KEY_CLIENT_INFO, encode(clientInfo));
        }};
        ppln = rpcClient.createMessageStream(clientInfo.getTenantId(), null, delivererKey, () -> {
                metadata.put(PIPELINE_ATTR_KEY_QOS0_LAST_FETCH_SEQ, lastFetchQoS0Seq + "");
                metadata.put(PIPELINE_ATTR_KEY_QOS2_LAST_FETCH_SEQ, lastFetchQoS2Seq + "");
                return metadata;
            },
            InboxServiceGrpc.getFetchMethod());
    }

    @Override
    public void fetch(Consumer<Fetched> consumer) {
        consumptions.add(ppln.msg()
            .doOnNext(fetched -> {
                if (fetched.getQos0SeqCount() > 0) {
                    lastFetchQoS0Seq = fetched.getQos0Seq(fetched.getQos0SeqCount() - 1);
                    // commit immediately
                    commit(System.nanoTime(), QoS.AT_MOST_ONCE, lastFetchQoS0Seq);
                }
                if (fetched.getQos2SeqCount() > 0) {
                    lastFetchQoS2Seq = fetched.getQos2Seq(fetched.getQos2SeqCount() - 1);
                }
            })
            .subscribe(consumer::accept));
    }

    @Override
    public void hint(int bufferCapacity) {
        log.trace("Send hint: inboxId={}, capacity={}, client={}", inboxId, bufferCapacity, clientInfo);
        ppln.ack(FetchHint.newBuilder().setCapacity(bufferCapacity).build());
    }

    @Override
    public CompletableFuture<CommitReply> commit(long reqId, QoS qos, long upToSeq) {
        log.trace("Commit: inbox={}, qos={}, seq={}, client={}", inboxId, qos, upToSeq, clientInfo);
        return rpcClient.invoke(clientInfo.getTenantId(), null,
                CommitRequest.newBuilder()
                    .setReqId(reqId)
                    .setQos(qos)
                    .setUpToSeq(upToSeq)
                    .setInboxId(inboxId)
                    .setClientInfo(clientInfo)
                    .build(),
                InboxServiceGrpc.getCommitMethod())
            .exceptionally(e -> {
                log.error("Failed to commit inbox: {}", inboxId, e);
                return CommitReply.newBuilder()
                    .setReqId(reqId)
                    .setResult(CommitReply.Result.ERROR)
                    .build();
            });
    }

    @Override
    public void close() {
        consumptions.dispose();
        ppln.close();
    }
}
