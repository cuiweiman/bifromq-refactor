package com.zachary.bifromq.basekv.balance;

import com.zachary.bifromq.basekv.balance.command.BalanceCommand;
import com.zachary.bifromq.basekv.balance.command.ChangeConfigCommand;
import com.zachary.bifromq.basekv.balance.command.MergeCommand;
import com.zachary.bifromq.basekv.balance.command.SplitCommand;
import com.zachary.bifromq.basekv.balance.command.TransferLeadershipCommand;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply;
import com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigRequest;
import com.zachary.bifromq.basekv.store.proto.KVRangeMergeReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeMergeRequest;
import com.zachary.bifromq.basekv.store.proto.KVRangeSplitReply;
import com.zachary.bifromq.basekv.store.proto.KVRangeSplitRequest;
import com.zachary.bifromq.basekv.store.proto.RecoverRequest;
import com.zachary.bifromq.basekv.store.proto.ReplyCode;
import com.zachary.bifromq.basekv.store.proto.TransferLeadershipReply;
import com.zachary.bifromq.basekv.store.proto.TransferLeadershipRequest;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CommandRunner {

    private final Cache<KVRangeId, Long> historyCommandCache;
    private final IBaseKVStoreClient kvStoreClient;

    public enum Result {
        Succeed,
        Failed
    }

    public CommandRunner(IBaseKVStoreClient kvStoreClient) {
        this.kvStoreClient = kvStoreClient;
        this.historyCommandCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();
    }

    public CompletableFuture<Result> run(BalanceCommand command) {
        if (command.getExpectedVer() != null) {
            Long historyCommand = historyCommandCache.getIfPresent(command.getKvRangeId());
            if (historyCommand != null && historyCommand >= command.getExpectedVer()) {
                log.warn("Command version is duplicated with prev one: {}", command);
                return CompletableFuture.completedFuture(Result.Failed);
            }
        }
        log.debug("Send balanceCommand[{}]", command);
        switch (command.type()) {
            case CHANGE_CONFIG:
                ChangeReplicaConfigRequest changeConfigRequest = ChangeReplicaConfigRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setKvRangeId(command.getKvRangeId())
                    .setVer(command.getExpectedVer())
                    .addAllNewVoters(((ChangeConfigCommand) command).getVoters())
                    .addAllNewLearners(((ChangeConfigCommand) command).getLearners())
                    .build();
                return handleStoreReplyCode(command,
                    kvStoreClient.changeReplicaConfig(command.getToStore(), changeConfigRequest)
                        .thenApply(ChangeReplicaConfigReply::getCode)
                );
            case MERGE:
                KVRangeMergeRequest rangeMergeRequest = KVRangeMergeRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setVer(command.getExpectedVer())
                    .setMergerId(command.getKvRangeId())
                    .setMergeeId(((MergeCommand) command).getMergeeId())
                    .build();
                return handleStoreReplyCode(command,
                    kvStoreClient.mergeRanges(command.getToStore(), rangeMergeRequest)
                        .thenApply(KVRangeMergeReply::getCode));
            case SPLIT:
                KVRangeSplitRequest kvRangeSplitRequest = KVRangeSplitRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setKvRangeId(command.getKvRangeId())
                    .setVer(command.getExpectedVer())
                    .setSplitKey(((SplitCommand) command).getSplitKey())
                    .build();
                return handleStoreReplyCode(command,
                    kvStoreClient.splitRange(command.getToStore(), kvRangeSplitRequest)
                        .thenApply(KVRangeSplitReply::getCode));
            case TRANSFER_LEADERSHIP:
                TransferLeadershipRequest transferLeadershipRequest = TransferLeadershipRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .setKvRangeId(command.getKvRangeId())
                    .setVer(command.getExpectedVer())
                    .setNewLeaderStore(((TransferLeadershipCommand) command).getNewLeaderStore())
                    .build();
                return handleStoreReplyCode(command,
                    kvStoreClient.transferLeadership(command.getToStore(), transferLeadershipRequest)
                        .thenApply(TransferLeadershipReply::getCode));
            case RECOVERY:
                RecoverRequest recoverRequest = RecoverRequest.newBuilder()
                    .setReqId(System.nanoTime())
                    .build();
                return kvStoreClient.recover(command.getToStore(), recoverRequest)
                    .handle((r, e) -> {
                        if (e != null) {
                            log.error("Unexpected error when recover, req: {}", recoverRequest, e);
                        }
                        return Result.Succeed;
                    });
            default:
                return CompletableFuture.completedFuture(null);
        }
    }

    private CompletableFuture<Result> handleStoreReplyCode(BalanceCommand command,
                                                           CompletableFuture<ReplyCode> storeReply) {
        CompletableFuture<Result> onDone = new CompletableFuture<>();
        storeReply.whenComplete((code, e) -> {
            if (e != null) {
                log.error("Unexpected error when run command: {}", command, e);
                onDone.complete(Result.Failed);
                return;
            }
            switch (code) {
                case Ok:
                    if (command.getExpectedVer() != null) {
                        historyCommandCache.put(command.getKvRangeId(), command.getExpectedVer());
                    }
                    onDone.complete(Result.Succeed);
                    break;
                case BadRequest:
                case BadVersion:
                case TryLater:
                case InternalError:
                    log.warn("Failed with reply: {}, command: {}", code, command);
                    onDone.complete(Result.Failed);
                    break;
                default:
                    onDone.complete(Result.Failed);
            }
        });
        return onDone;
    }
}
