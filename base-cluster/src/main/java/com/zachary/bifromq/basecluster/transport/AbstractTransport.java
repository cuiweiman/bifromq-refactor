package com.zachary.bifromq.basecluster.transport;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecluster.transport.proto.Packet;
import com.zachary.bifromq.basehlc.HLC;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 11:09
 */
@AllArgsConstructor
public abstract class AbstractTransport implements ITransport {

    private final String sharedToken;

    private final Subject<PacketEnvelope> publisher = PublishSubject.create();
    private final AtomicBoolean hasStopped = new AtomicBoolean();

    @Override
    public abstract InetSocketAddress bindAddress();

    @Override
    public final CompletableFuture<Void> send(List<ByteString> data, InetSocketAddress recipient) {
        checkState();
        // piggyback local base hlc
        Packet.Builder builder = Packet.newBuilder().setHlc(HLC.INST.get()).addAllMessages(data);
        if (!Strings.isNullOrEmpty(sharedToken)) {
            builder.setClusterEnv(sharedToken);
        }
        Packet packet = builder.build();
        return doSend(packet, recipient);
    }

    @Override
    public final Observable<PacketEnvelope> receive() {
        checkState();
        return publisher;
    }

    @Override
    public final CompletableFuture<Void> shutdown() {
        /*
         * 关闭
         */
        if (hasStopped.compareAndSet(false, true)) {
            CompletableFuture<Void> onDone = new CompletableFuture<>();
            // Completable.concatArrayDelayError 执行多个 CompletableSource，所有 CompletableSource 执行结束后，才抛出执行异常的错误
            // Completable.fromAction 创建 CompletableSource 实例 最简单的方法
            Completable.concatArrayDelayError(doShutdown(), Completable.fromAction(publisher::onComplete))
                    .onErrorComplete()
                    .subscribe(() -> onDone.complete(null));
            return onDone;
        }
        return CompletableFuture.completedFuture(null);
    }

    protected void doReceive(Packet packet, InetSocketAddress sender, InetSocketAddress recipient) {
        if (!Strings.isNullOrEmpty(sharedToken)
                && !Strings.isNullOrEmpty(packet.getClusterEnv())
                && !sharedToken.equals(packet.getClusterEnv())) {
            return;
        }
        synchronized (publisher) {
            // update local base-hlc before deliver to app logic
            // 在 传递到应用程序逻辑之前 更新本地 base-hlc
            HLC.INST.update(packet.getHlc());
            publisher.onNext(new PacketEnvelope(packet.getMessagesList(), recipient, sender));
        }
    }

    protected abstract CompletableFuture<Void> doSend(Packet packet, InetSocketAddress recipient);

    protected abstract Completable doShutdown();

    private void checkState() {
        // 校验 hasStopped 的值是否为 TRUE-已停止
        Preconditions.checkState(!hasStopped.get(), "Has stopped");
    }
}
