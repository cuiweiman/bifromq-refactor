
package com.zachary.bifromq.basecluster;

import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgentMember;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.time.Duration;

@Slf4j
public class AgentHostNode3 {
    public static void main(String[] args) {
        Metrics.addRegistry(new LoggingMeterRegistry());

        AgentHostOptions opt = new AgentHostOptions()
            .autoHealingTimeout(Duration.ofSeconds(300))
            .addr("127.0.0.1")
            .port(5556);
        IAgentHost host = IAgentHost.newInstance(opt);
        // comment out following line to simulate crash and restart
        Runtime.getRuntime().addShutdownHook(new Thread(() -> host.shutdown()));
        host.start();

        host.join(Sets.newHashSet(new InetSocketAddress("127.0.0.1", 3334)));
        IAgent agent = host.host("service1");
        IAgentMember agentMember = agent.register("AgentNode3");
        agentMember.metadata(ByteString.copyFromUtf8("My lord"));
        agent.membership().subscribe(agentNodes -> log.info("Agent[service1] members:\n{}", agentNodes));
        host.membership().subscribe(memberList -> log.info("AgentHosts:\n{}", memberList));
        agentMember.receive().subscribe(msg -> log.info("AgentMessage: {}", msg));
    }
}
