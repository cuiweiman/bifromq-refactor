
package com.zachary.bifromq.basecluster;

import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr;
import com.zachary.bifromq.basecluster.agent.proto.AgentMemberMetadata;
import com.zachary.bifromq.basecluster.agent.proto.AgentMessage;
import com.zachary.bifromq.basecluster.annotation.StoreCfg;
import com.zachary.bifromq.basecluster.annotation.StoreCfgs;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgent;
import com.zachary.bifromq.basecluster.memberlist.agent.IAgentMember;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import io.reactivex.rxjava3.observers.TestObserver;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.protobuf.ByteString.copyFromUtf8;

@Slf4j
@Listeners(AgentHostTestListener.class)
public class AgentHostsTest extends AgentTestTemplate {

    @StoreCfgs(stores = {@StoreCfg(id = "s1", bindPort = 11111)})
    @Test(groups = "integration")
    public void testRegister() {
        IAgent agent = storeMgr.host("s1", "agent1");
        IAgentMember agentMember1 = agent.register("agentNode1");
        agentMember1.metadata(copyFromUtf8("123"));
        IAgentMember agentMember2 = agent.register("agentNode2");
        agentMember2.metadata(copyFromUtf8("456"));
        Map<AgentMemberAddr, AgentMemberMetadata> expected = new HashMap<>() {{
            put(agentMember1.address(), agentMember1.metadata());
            put(agentMember2.address(), agentMember2.metadata());
        }};
        Awaitility.await().until(() -> {
            Map<AgentMemberAddr, AgentMemberMetadata> agentMembers = agent.membership().blockingFirst();
            return expected.equals(agentMembers);
        });
    }

    @StoreCfgs(stores = {@StoreCfg(id = "s1", bindPort = 11111)})
    @Test(groups = "integration")
    public void testUnregister() {
        IAgent agent = storeMgr.host("s1", "agent1");
        IAgentMember agentMember1 = agent.register("agentNode1");
        IAgentMember agentMember2 = agent.register("agentNode2");
        agentMember1.metadata(copyFromUtf8("123"));
        agentMember2.metadata(copyFromUtf8("123"));
        Awaitility.await().until(() -> agent.membership().blockingFirst().size() == 2);

        agent.deregister(agentMember1);
        Awaitility.await().until(() -> {
            Map<AgentMemberAddr, AgentMemberMetadata> agentMembers = agent.membership().blockingFirst();
            if (agentMembers.size() != 1) {
                return false;
            }
            return agentMembers.keySet().stream().findFirst().get().getName().equals("agentNode2");
        });
        agent.deregister(agentMember2);
        Awaitility.await().until(() -> agent.membership().blockingFirst().isEmpty());
    }

    @StoreCfgs(stores = {
            @StoreCfg(id = "s1", bindPort = 11111, isSeed = true),
            @StoreCfg(id = "s2", bindPort = 11112),
            @StoreCfg(id = "s3", bindPort = 11113),
            @StoreCfg(id = "s4", bindPort = 11114),
            @StoreCfg(id = "s5", bindPort = 11115),
    })
    @Test(groups = "integration")
    public void testMultipleAgentHosts() {
        Awaitility.await().until(() -> storeMgr.membership("s1").size() == 5);
        Awaitility.await().until(() -> storeMgr.membership("s2").size() == 5);
        Awaitility.await().until(() -> storeMgr.membership("s3").size() == 5);
        Awaitility.await().until(() -> storeMgr.membership("s4").size() == 5);
        Awaitility.await().until(() -> storeMgr.membership("s5").size() == 5);
    }

    @StoreCfgs(stores = {
            @StoreCfg(id = "s1", bindPort = 11111, isSeed = true),
            @StoreCfg(id = "s2", bindPort = 22222),
    })
    @Test(groups = "integration")
    public void testAgentNodesInTwoStores() {
        Awaitility.await().until(() -> storeMgr.membership("s1").size() == 2);
        Awaitility.await().until(() -> storeMgr.membership("s2").size() == 2);

        IAgent agentOnS1 = storeMgr.host("s1", "agent1");
        IAgent agentOnS2 = storeMgr.host("s2", "agent1");

        IAgentMember agentMember1 = agentOnS1.register("agentNode1");
        agentMember1.metadata(copyFromUtf8("1"));

        Awaitility.await().until(() -> {
            Map<AgentMemberAddr, AgentMemberMetadata> agentMembers = agentOnS1.membership().blockingFirst();
            log.info("S1: {}", agentMembers);
            return agentMembers.size() == 1;
        });
        Awaitility.await().until(() -> {
            Map<AgentMemberAddr, AgentMemberMetadata> agentMembers = agentOnS2.membership().blockingFirst();
            log.info("S2: {}", agentMembers);
            return agentMembers.size() == 1;
        });

        IAgentMember agentMember2 = agentOnS2.register("agentNode2");
        agentMember2.metadata(copyFromUtf8("2"));

        Awaitility.await().until(() -> agentOnS1.membership().blockingFirst().size() == 2);
        Awaitility.await().until(() -> agentOnS2.membership().blockingFirst().size() == 2);
    }

    @StoreCfgs(stores = {
            @StoreCfg(id = "s1", bindPort = 11111, isSeed = true),
            @StoreCfg(id = "s2", bindPort = 22222),
            @StoreCfg(id = "s3", bindPort = 33333),
    })
    @Test(groups = "integration")
    public void testAgentNodesInThreeStores() {
        Awaitility.await().until(() -> {
            Set<HostEndpoint> hosts_s1 = Sets.newHashSet(storeMgr.getHost("s1").membership().blockingFirst());
            return 3 == hosts_s1.size();
        });
        Awaitility.await().until(() -> {
            Set<HostEndpoint> hosts_s2 = Sets.newHashSet(storeMgr.getHost("s2").membership().blockingFirst());
            return 3 == hosts_s2.size();
        });
        Awaitility.await().until(() -> {
            Set<HostEndpoint> hosts_s3 = Sets.newHashSet(storeMgr.getHost("s3").membership().blockingFirst());
            return 3 == hosts_s3.size();
        });
        IAgent agentOnS1 = storeMgr.host("s1", "agent1");
        IAgent agentOnS2 = storeMgr.host("s2", "agent1");
        IAgent agentOnS3 = storeMgr.host("s3", "agent1");
        IAgentMember agentMember1OnS1 = agentOnS1.register("agentNode1");
        agentMember1OnS1.metadata(copyFromUtf8("1"));

        IAgentMember agentMember11OnS1 = agentOnS1.register("agentNode11");
        agentMember11OnS1.metadata(copyFromUtf8("11"));

        IAgentMember agentMember2OnS2 = agentOnS2.register("agentNode2");
        agentMember2OnS2.metadata(copyFromUtf8("2"));

        IAgentMember agentMember3OnS3 = agentOnS3.register("agentNode3");
        agentMember3OnS3.metadata(copyFromUtf8("3"));

        Awaitility.await().until(() -> agentOnS1.membership().blockingFirst().size() == 4);
        Awaitility.await().until(() -> agentOnS2.membership().blockingFirst().size() == 4);
        Awaitility.await().until(() -> agentOnS3.membership().blockingFirst().size() == 4);

        // unhost agentNode2 from s1
        log.info("Stop hosting agentNode11 from s1");
        agentOnS1.deregister(agentMember11OnS1);
        Awaitility.await().until(() -> agentOnS1.membership().blockingFirst().size() == 3);
        Awaitility.await().until(() -> agentOnS2.membership().blockingFirst().size() == 3);
        Awaitility.await().until(() -> agentOnS3.membership().blockingFirst().size() == 3);

        log.info("Stop hosting agentNode1 from s1");
        // unhost agentNode 1 from s1
        agentOnS1.deregister(agentMember1OnS1);
        Awaitility.await().until(() -> agentOnS2.membership().blockingFirst().size() == 2);
        Awaitility.await().until(() -> agentOnS3.membership().blockingFirst().size() == 2);


        log.info("Re-hosting agentNode1 from s1 with different metadata attached");
        // re-host agentNode1 in s1 with different metadata
        agentOnS1.register("agentNode1");
        agentMember1OnS1 = agentOnS1.register("agentNode1");
        agentMember1OnS1.metadata(copyFromUtf8("abc"));

        Awaitility.await().until(() -> agentOnS1.membership().blockingFirst().size() == 3);
        Awaitility.await().until(() -> agentOnS2.membership().blockingFirst().size() == 3);
        Awaitility.await().until(() -> agentOnS3.membership().blockingFirst().size() == 3);

        // nothing will happen when unregistering agentNode3 from s1, since s3 is registered in s3
        agentOnS1.deregister(agentMember3OnS3);
        Awaitility.await().until(() -> agentOnS1.membership().blockingFirst().size() == 3);
        Awaitility.await().until(() -> agentOnS2.membership().blockingFirst().size() == 3);
        Awaitility.await().until(() -> agentOnS3.membership().blockingFirst().size() == 3);

        // unregister all
        agentOnS1.deregister(agentMember1OnS1);
        agentOnS2.deregister(agentMember2OnS2);
        agentOnS3.deregister(agentMember3OnS3);
        Awaitility.await().until(() -> agentOnS1.membership().blockingFirst().isEmpty());
        Awaitility.await().until(() -> agentOnS2.membership().blockingFirst().isEmpty());
        Awaitility.await().until(() -> agentOnS3.membership().blockingFirst().isEmpty());
    }

    @StoreCfgs(stores = {@StoreCfg(id = "s1", bindPort = 11111, isSeed = true)})
    @Test(groups = "integration")
    public void testRefreshRoute() {
        IAgent agent = storeMgr.host("s1", "agent1");
        IAgentMember agentMember1 = agent.register("agentNode1");
        agentMember1.metadata(copyFromUtf8("1"));
        IAgentMember agentMember2 = agent.register("agentNode2");
        agentMember2.metadata(copyFromUtf8("2"));
        Awaitility.await().until(() -> agent.membership().blockingFirst().size() == 2);

        agent.deregister(agentMember1);
        Awaitility.await().until(() -> {
            try {
                agentMember1.send(AgentMemberAddr.newBuilder()
                                .setName("agentNode2")
                                .setEndpoint(storeMgr.endpoint("s1"))
                                .build(),
                        ByteString.EMPTY, true).join();
                return false;
            } catch (Exception e) {
                Assert.assertEquals(IllegalStateException.class, e.getClass());
                return true;
            }
        });
    }

    @StoreCfgs(stores = {
            @StoreCfg(id = "s1", bindPort = 11111, isSeed = true),
            @StoreCfg(id = "s2", bindPort = 22222),
    })
    @Test(groups = "integration")
    public void testMulticast() {
        String sender = "sender";
        String receiverGroup = "receiverGroup";
        IAgent agentOnS1 = storeMgr.host("s1", "agent1");
        IAgent agentOnS2 = storeMgr.host("s2", "agent1");
        IAgentMember agentMember1 = agentOnS1.register(sender);
        agentMember1.metadata(copyFromUtf8("1"));

        IAgentMember receiverOnS1 = agentOnS1.register(receiverGroup);
        receiverOnS1.metadata(copyFromUtf8("2"));

        IAgentMember receiverOnS2 = agentOnS2.register(receiverGroup);
        receiverOnS2.metadata(copyFromUtf8("2"));

        Awaitility.await().until(() -> agentOnS1.membership().blockingFirst().size() == 3);
        Awaitility.await().until(() -> agentOnS2.membership().blockingFirst().size() == 3);

        TestObserver<AgentMessage> testObserver1 = new TestObserver<>();
        TestObserver<AgentMessage> testObserver2 = new TestObserver<>();
        receiverOnS1.receive().subscribe(testObserver1);
        receiverOnS2.receive().subscribe(testObserver2);
        agentMember1.multicast(receiverGroup, ByteString.copyFromUtf8("hello"), true);

        testObserver1.awaitCount(1);
        testObserver2.awaitCount(1);
        testObserver1.assertValue(testObserver2.values().get(0));
    }
}
