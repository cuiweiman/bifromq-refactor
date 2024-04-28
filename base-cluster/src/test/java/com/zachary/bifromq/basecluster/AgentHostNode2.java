package com.zachary.bifromq.basecluster;

import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgentMember;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AgentHostNode2 {
    public static void main(String[] args) {
        Metrics.addRegistry(new LoggingMeterRegistry());

        AgentHostOptions opt = new AgentHostOptions()
            .autoHealingTimeout(Duration.ofSeconds(300))
            .addr("127.0.0.1")
            .port(4445);
        IAgentHost host = IAgentHost.newInstance(opt);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            host.shutdown();
        }));
        host.start();

        host.join(Sets.newHashSet(new InetSocketAddress("127.0.0.1", 3334)));
        IAgent agent = host.host("service1");
        IAgentMember agentMember = agent.register("AgentNode2");
        agentMember.metadata(ByteString.copyFromUtf8("Hello"));

        agent.membership().subscribe(agentNodes -> log.info("Agent[service1] members:\n{}", agentNodes));

        host.membership().subscribe(memberList -> log.info("AgentHosts:\n{}", memberList));

        Observable.timer(1, TimeUnit.SECONDS)
            .subscribe(t -> {
                IAgentMember agentMember22 = agent.register("AgentNode22");
                agentMember22.metadata(ByteString.copyFromUtf8("World"));
                agentMember22.receive()
                    .subscribe(msg -> log.info("AgentMessage: {}", msg));
            });

        agentMember.receive().subscribe(msg -> log.info("AgentMessage: {}", msg));
    }
}
