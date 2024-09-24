package com.zachary.bifromq.dist.worker;

import com.zachary.bifromq.baseenv.EnvProvider;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.dist.entity.NormalMatching;
import com.zachary.bifromq.dist.worker.scheduler.DeliveryRequest;
import com.zachary.bifromq.dist.worker.scheduler.DeliveryScheduler;
import com.zachary.bifromq.dist.worker.scheduler.MessagePackWrapper;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.eventcollector.distservice.DeliverError;
import com.zachary.bifromq.plugin.eventcollector.distservice.DeliverNoInbox;
import com.zachary.bifromq.plugin.eventcollector.distservice.Delivered;
import com.zachary.bifromq.plugin.subbroker.ISubBrokerManager;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.SubInfo;
import com.zachary.bifromq.type.TopicMessagePack;
import lombok.extern.slf4j.Slf4j;
import org.jctools.queues.MpscBlockingConsumerArrayQueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.plugin.eventcollector.ThreadLocalEventPool.getLocal;

@Slf4j
class FanoutExecutorGroup {
    private final IEventCollector eventCollector;
    private final IDistClient distClient;
    private final DeliveryScheduler scheduler;
    private final ExecutorService[] phaseOneExecutorGroup;
    private final ExecutorService[] phaseTwoExecutorGroup;

    FanoutExecutorGroup(ISubBrokerManager subBrokerMgr,
                        DeliveryScheduler scheduler,
                        IEventCollector eventCollector,
                        IDistClient distClient,
                        int groupSize) {
        this.eventCollector = eventCollector;
        this.distClient = distClient;
        this.scheduler = scheduler;
        phaseOneExecutorGroup = new ExecutorService[groupSize];
        phaseTwoExecutorGroup = new ExecutorService[groupSize];
        for (int i = 0; i < groupSize; i++) {
            phaseOneExecutorGroup[i] = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new MpscBlockingConsumerArrayQueue<>(2000),
                EnvProvider.INSTANCE.newThreadFactory("fanout-p1-executor-" + i));
            phaseTwoExecutorGroup[i] = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new MpscBlockingConsumerArrayQueue<>(2000),
                EnvProvider.INSTANCE.newThreadFactory("fanout-p2-executor-" + i));
        }
    }

    public void shutdown() {
        for (ExecutorService executorService : phaseOneExecutorGroup) {
            executorService.shutdown();
        }
        for (ExecutorService executorService : phaseTwoExecutorGroup) {
            executorService.shutdown();
        }
    }

    public void submit(int hash, Map<NormalMatching, Set<ClientInfo>> routeMap, MessagePackWrapper msgPackWrapper,
                       Map<ClientInfo, TopicMessagePack.PublisherPack> senderMsgPackMap) {
        int idx = hash % phaseOneExecutorGroup.length;
        if (idx < 0) {
            idx += phaseOneExecutorGroup.length;
        }
        try {
            phaseOneExecutorGroup[idx].submit(() -> send(routeMap, msgPackWrapper, senderMsgPackMap));
        } catch (RejectedExecutionException ree) {
            log.warn("Message drop due to fan-out queue is full");
        }
    }

    private void send(Map<NormalMatching, Set<ClientInfo>> routeMap,
                      MessagePackWrapper msgPackWrapper,
                      Map<ClientInfo, TopicMessagePack.PublisherPack> senderMsgPackMap) {
        if (routeMap.size() == 1) {
            routeMap.forEach((route, senders) -> send(route, senders, msgPackWrapper, senderMsgPackMap));
        } else {
            List<List<Runnable>> fanoutTasksPerIdx = new ArrayList<>(phaseTwoExecutorGroup.length);
            for (int i = 0; i < phaseTwoExecutorGroup.length; i++) {
                fanoutTasksPerIdx.add(new LinkedList<>());
            }
            routeMap.forEach((route, senders) -> {
                int idx = route.hashCode() % phaseTwoExecutorGroup.length;
                if (idx < 0) {
                    idx += phaseTwoExecutorGroup.length;
                }
                List<Runnable> fanoutTasks = fanoutTasksPerIdx.get(idx);
                fanoutTasks.add(() -> send(route, senders, msgPackWrapper, senderMsgPackMap));
            });
            for (int i = 0; i < phaseTwoExecutorGroup.length; i++) {
                List<Runnable> fanoutTasks = fanoutTasksPerIdx.get(i);
                if (!fanoutTasks.isEmpty()) {
                    try {
                        phaseTwoExecutorGroup[i].submit(() -> fanoutTasks.forEach(Runnable::run));
                    } catch (RejectedExecutionException ree) {
                        log.warn("Message drop due to fan-out queue is full");
                    }
                }
            }
        }
    }

    private void send(NormalMatching route, Set<ClientInfo> senders,
                      MessagePackWrapper msgPackWrapper,
                      Map<ClientInfo, TopicMessagePack.PublisherPack> senderMsgPackMap) {
        if (senders.size() == senderMsgPackMap.size()) {
            send(msgPackWrapper, route);
        } else {
            // ordered share sub
            TopicMessagePack.Builder subMsgPackBuilder = TopicMessagePack.newBuilder()
                .setTopic(msgPackWrapper.messagePack.getTopic());
            senders.forEach(sender -> subMsgPackBuilder.addMessage(senderMsgPackMap.get(sender)));
            send(MessagePackWrapper.wrap(subMsgPackBuilder.build()), route);
        }
    }

    private void send(MessagePackWrapper msgPack, NormalMatching matched) {
        int subBrokerId = matched.subBrokerId;
        String delivererKey = matched.delivererKey;
        SubInfo sub = matched.subInfo;
        DeliveryRequest request = new DeliveryRequest(sub, subBrokerId, delivererKey, msgPack);
        scheduler.schedule(request).whenComplete((result, e) -> {
            if (e != null) {
                eventCollector.report(getLocal(DeliverError.class)
                    .brokerId(subBrokerId)
                    .delivererKey(delivererKey)
                    .subInfo(sub)
                    .messages(msgPack.messagePack));

            } else {
                switch (result) {
                    case OK:
                        eventCollector.report(getLocal(Delivered.class)
                            .brokerId(subBrokerId)
                            .delivererKey(delivererKey)
                            .subInfo(sub)
                            .messages(msgPack.messagePack));
                        break;
                    case NO_INBOX:
                        // unsub as side effect
                        SubInfo subInfo = matched.subInfo;
                        distClient.unsub(System.nanoTime(),
                            subInfo.getTenantId(),
                            subInfo.getTopicFilter(),
                            subInfo.getInboxId(),
                            delivererKey,
                            subBrokerId);
                        eventCollector.report(getLocal(DeliverNoInbox.class)
                            .brokerId(subBrokerId)
                            .delivererKey(delivererKey)
                            .subInfo(sub)
                            .messages(msgPack.messagePack));
                        break;
                }
            }
        });
    }
}
