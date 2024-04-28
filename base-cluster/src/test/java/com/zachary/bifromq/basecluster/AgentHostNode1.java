
package com.zachary.bifromq.basecluster;

import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgentMember;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AgentHostNode1 {

    public static void main(String[] args) {
        Metrics.addRegistry(new LoggingMeterRegistry());

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        AgentHostOptions opt = AgentHostOptions.builder()
            .autoHealingTimeout(Duration.ofSeconds(300))
            .addr("127.0.0.1")
            .port(3334)
            .build();
        IAgentHost host = IAgentHost.newInstance(opt);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            MoreExecutors.shutdownAndAwaitTermination(executorService, 5, TimeUnit.SECONDS);
            host.shutdown();
        }));
        host.start();
        IAgent agent = host.host("service1");
        IAgentMember agentMember = agent.register("AgentNode1");
        agentMember.metadata(ByteString.copyFromUtf8("Greeting"));
        agent.membership().subscribe(agentNodes -> log.info("Agent[service1] members:\n{}", agentNodes));
        host.membership().subscribe(memberList -> log.info("AgentHosts:\n{}", memberList));
        agentMember.receive()
            .subscribe(msg -> log.info("AgentMessage: {}", msg));
        executorService.scheduleAtFixedRate(() -> {
            agentMember.broadcast(ByteString.copyFromUtf8("hello:" + ThreadLocalRandom.current().nextInt()), true)
                .whenComplete((v, e) -> {
                    if (e != null) {
                        log.error("failed to broadcast", e);
                    }
                }).join();
        }, 3, 10, TimeUnit.SECONDS);
    }
}
