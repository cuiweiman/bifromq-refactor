package com.zachary.bifromq.basecluster.memberlist.agent;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessage;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;
import com.zachary.bifromq.basecrdt.core.api.operation.ORMapOperation;
import com.zachary.bifromq.basehlc.HLC;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType.mvreg;


class AgentMember implements IAgentMember {
    private final AgentMemberAddr localAddr;
    private final IORMap agentCRDT;
    private final IAgentMessenger messenger;
    private final Scheduler scheduler;
    private final Supplier<Set<AgentMemberAddr>> memberAddresses;
    private final PublishSubject<AgentMessage> agentMessageSubject = PublishSubject.create();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ReadWriteLock destroyLock = new ReentrantReadWriteLock();
    private volatile boolean destroy = false;
    private volatile AgentMemberMetadata metadata;

    AgentMember(AgentMemberAddr memberAddr,
                IORMap agentCRDT,
                IAgentMessenger messenger,
                Scheduler scheduler,
                Supplier<Set<AgentMemberAddr>> memberAddresses) {
        this.localAddr = memberAddr;
        this.agentCRDT = agentCRDT;
        this.messenger = messenger;
        this.scheduler = scheduler;
        this.memberAddresses = memberAddresses;
        metadata = AgentMemberMetadata.newBuilder().setHlc(HLC.INST.get()).build();
        updateCRDT();
        disposables.add(agentCRDT.inflation()
                .observeOn(scheduler)
                .subscribe(this::updateCRDT));
        disposables.add(messenger.receive()
                .filter(msg -> msg.getReceiver().equals(localAddr))
                .map(msg -> msg.getMessage())
                .observeOn(scheduler)
                .subscribe(agentMessageSubject::onNext));
    }

    @Override
    public AgentMemberMetadata metadata() {
        return metadata;
    }

    @Override
    public void metadata(ByteString value) {
        skipRunWhenDestroyed(() -> {
            if (!metadata.getValue().equals(value)) {
                metadata = AgentMemberMetadata.newBuilder().setValue(value).setHlc(HLC.INST.get()).build();
                updateCRDT();
            }
        });
    }

    @Override
    public AgentMemberAddr address() {
        return localAddr;
    }

    @Override
    public CompletableFuture<Void> broadcast(ByteString message, boolean reliable) {
        return throwsWhenDestroyed(() -> {
            AgentMessage agentMessage = AgentMessage.newBuilder().setSender(localAddr).setPayload(message).build();
            CompletableFuture<Void>[] sendFutures = memberAddresses.get().stream()
                    .map(memberAddr -> messenger.send(agentMessage, memberAddr, reliable))
                    .toArray(CompletableFuture[]::new);
            return CompletableFuture.allOf(sendFutures).exceptionally(e -> null);
        });
    }

    @Override
    public CompletableFuture<Void> send(AgentMemberAddr targetMemberAddr, ByteString message, boolean reliable) {
        return throwsWhenDestroyed(() -> {
            if (memberAddresses.get().contains(targetMemberAddr)) {
                AgentMessage agentMessage = AgentMessage.newBuilder().setSender(localAddr).setPayload(message).build();
                return messenger.send(agentMessage, targetMemberAddr, reliable);
            }
            return CompletableFuture.failedFuture(new UnknownHostException("Target not found"));
        });
    }

    @Override
    public CompletableFuture<Void> multicast(String targetMemberName, ByteString message, boolean reliable) {
        return throwsWhenDestroyed(() -> {
            Set<AgentMemberAddr> targetAddrs = memberAddresses.get().stream()
                    .filter(memberAddr -> memberAddr.getName().equals(targetMemberName))
                    .collect(Collectors.toSet());
            AgentMessage agentMessage = AgentMessage.newBuilder()
                    .setSender(localAddr)
                    .setPayload(message)
                    .build();
            return CompletableFuture.allOf(targetAddrs.stream()
                    .map(targetAddr -> messenger.send(agentMessage, targetAddr, reliable))
                    .toArray(CompletableFuture[]::new));
        });
    }

    private void updateCRDT(long ts) {
        skipRunWhenDestroyed(() -> {
            Optional<AgentMemberMetadata> metaOnCRDT = CRDTUtil.getAgentMemberMetadata(agentCRDT, localAddr);
            if (!metaOnCRDT.isPresent() || !metaOnCRDT.get().equals(metadata)) {
                updateCRDT();
            }
        });
    }

    private void updateCRDT() {
        skipRunWhenDestroyed(() -> agentCRDT.execute(ORMapOperation.update(localAddr.toByteString())
                .with(MVRegOperation.write(metadata.toByteString()))));
    }

    @Override
    public Observable<AgentMessage> receive() {
        return agentMessageSubject;
    }

    private void skipRunWhenDestroyed(Runnable runnable) {
        Lock readLock = destroyLock.readLock();
        try {
            readLock.lock();
            if (destroy) {
                return;
            }
            runnable.run();
        } finally {
            readLock.unlock();
        }
    }

    private CompletableFuture<Void> throwsWhenDestroyed(Supplier<CompletableFuture<Void>> callable) {
        Lock readLock = destroyLock.readLock();
        try {
            readLock.lock();
            if (destroy) {
                throw new IllegalStateException("Agent member has been deregistered");
            }
            return callable.get();
        } finally {
            readLock.unlock();
        }
    }

    CompletableFuture<Void> destroy() {
        Lock writeLock = destroyLock.writeLock();
        try {
            writeLock.lock();
            if (destroy) {
                return CompletableFuture.completedFuture(null);
            }
            return agentCRDT.execute(ORMapOperation.remove(localAddr.toByteString()).of(mvreg))
                    .whenComplete((v, e) -> {
                        disposables.dispose();
                        agentMessageSubject.onComplete();
                        destroy = true;
                    });
        } finally {
            writeLock.unlock();
        }
    }
}
