package com.zachary.bifromq.basekv.balance.impl;

import com.zachary.bifromq.basekv.balance.command.BalanceCommand;
import com.zachary.bifromq.basekv.balance.command.ChangeConfigCommand;
import com.zachary.bifromq.basekv.balance.command.RecoveryCommand;
import com.zachary.bifromq.basekv.balance.utils.DescriptorUtils;
import com.zachary.bifromq.basekv.proto.KVRangeDescriptor;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;
import com.zachary.bifromq.basekv.proto.LoadHint;
import com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RecoveryBalancerTest {

    private static final String LOCAL_STORE_ID = "localStoreId";

    private RecoveryBalancer balancer;

    @BeforeMethod
    public void setup() {
        balancer = new RecoveryBalancer(LOCAL_STORE_ID, 200L);
    }

    @Test
    public void balanceWithoutUpdate() {
        Optional<BalanceCommand> balance = balancer.balance();
        Assert.assertTrue(balance.isEmpty());
    }

    @Test
    public void rangeNoLeader() {
        KVRangeId id = KVRangeIdUtil.generate();
        List<String> voters = Lists.newArrayList(LOCAL_STORE_ID, "store2", "store3", "store4", "store5");
        List<KVRangeDescriptor> rangeDescriptors =
            DescriptorUtils.generateRangeDesc(id, Sets.newHashSet(voters), Sets.newHashSet());
        KVRangeStoreDescriptor storeDescriptor1 = KVRangeStoreDescriptor.newBuilder()
            .setId(LOCAL_STORE_ID)
            .addRanges(rangeDescriptors.get(0).toBuilder().setRole(RaftNodeStatus.Candidate).setVer(2).build())
            .build();
        KVRangeStoreDescriptor storeDescriptor2 = KVRangeStoreDescriptor.newBuilder()
            .setId("store2")
            .addRanges(rangeDescriptors.get(0).toBuilder().setRole(RaftNodeStatus.Candidate).setVer(3).build())
            .build();
        balancer.update(Sets.newHashSet(storeDescriptor1, storeDescriptor2));
        Optional<BalanceCommand> commandOptional = balancer.balance();
        Assert.assertTrue(commandOptional.isPresent());
        RecoveryCommand recoveryCommand = (RecoveryCommand) commandOptional.get();
        Assert.assertEquals("store2", recoveryCommand.getToStore());
    }

    @Test
    public void rangeNoLeaderAndOneVTemporaryDead() {
        KVRangeId id = KVRangeIdUtil.generate();
        List<String> voters = Lists.newArrayList(LOCAL_STORE_ID, "aaaaa", "store3", "store4", "store5");
        List<KVRangeDescriptor> rangeDescriptors =
            DescriptorUtils.generateRangeDesc(id, Sets.newHashSet(voters), Sets.newHashSet());
        KVRangeStoreDescriptor storeDescriptor1 = KVRangeStoreDescriptor.newBuilder()
            .setId(LOCAL_STORE_ID)
            .addRanges(rangeDescriptors.get(0).toBuilder().setRole(RaftNodeStatus.Candidate).setVer(3).build())
            .build();
        KVRangeStoreDescriptor storeDescriptor2 = KVRangeStoreDescriptor.newBuilder()
            .setId("aaaaa")
            .addRanges(rangeDescriptors.get(0).toBuilder().setRole(RaftNodeStatus.Candidate).setVer(3).build())
            .build();
        balancer.update(Sets.newHashSet(storeDescriptor1, storeDescriptor2));
        // store2 dead temporarily
        balancer.update(Sets.newHashSet(storeDescriptor1));

        Optional<BalanceCommand> commandOptional = balancer.balance();
        Assert.assertTrue(commandOptional.isPresent());
        RecoveryCommand recoveryCommand = (RecoveryCommand) commandOptional.get();
        Assert.assertEquals(LOCAL_STORE_ID, recoveryCommand.getToStore());
    }

    @Test
    public void rangeNoLeaderAndSelfHealing() {
        KVRangeId id = KVRangeIdUtil.generate();
        List<String> voters = Lists.newArrayList(LOCAL_STORE_ID, "store2", "store3", "store4", "store5");
        List<KVRangeDescriptor> rangeDescriptors =
            DescriptorUtils.generateRangeDesc(id, Sets.newHashSet(voters), Sets.newHashSet());
        KVRangeStoreDescriptor storeDescriptor1 = KVRangeStoreDescriptor.newBuilder()
            .setId(LOCAL_STORE_ID)
            .addRanges(rangeDescriptors.get(0).toBuilder().setRole(RaftNodeStatus.Candidate).setVer(2).build())
            .build();
        KVRangeStoreDescriptor storeDescriptor2 = KVRangeStoreDescriptor.newBuilder()
            .setId("store2")
            .addRanges(rangeDescriptors.get(0).toBuilder().setRole(RaftNodeStatus.Candidate).setVer(3).build())
            .build();
        KVRangeStoreDescriptor storeDescriptor3 = KVRangeStoreDescriptor.newBuilder()
            .setId("store3")
            .addRanges(rangeDescriptors.get(0).toBuilder().setRole(RaftNodeStatus.Candidate).setVer(3).build())
            .build();

        balancer.update(Sets.newHashSet(storeDescriptor1, storeDescriptor2, storeDescriptor3));
        Optional<BalanceCommand> commandOptional = balancer.balance();
        Assert.assertTrue(commandOptional.isEmpty());
    }

    @Test
    public void rangeWithThreeVAndTwoAlive() throws InterruptedException {
        KVRangeId id = KVRangeIdUtil.generate();
        List<String> voters = Lists.newArrayList(LOCAL_STORE_ID, "store1", "store2");
        List<String> learners = Lists.newArrayList();
        List<KVRangeDescriptor> rangeDescriptors =
            DescriptorUtils.generateRangeDesc(id, Sets.newHashSet(voters), Sets.newHashSet(learners));
        List<KVRangeStoreDescriptor> storeDescriptors = new ArrayList<>();
        for (int i = 0; i < voters.size(); i++) {
            storeDescriptors.add(KVRangeStoreDescriptor.newBuilder()
                .setId(voters.get(i))
                .addRanges(rangeDescriptors.get(i))
                .build());
        }
        balancer.update(Sets.newHashSet(storeDescriptors));
        // store2 dead
        storeDescriptors.remove(storeDescriptors.size() - 1);
        balancer.update(Sets.newHashSet(storeDescriptors));
        Optional<BalanceCommand> commandOptional = balancer.balance();
        Assert.assertTrue(commandOptional.isEmpty());
        Thread.sleep(250);
        commandOptional = balancer.balance();
        Assert.assertTrue(commandOptional.isPresent());
        ChangeConfigCommand changeConfigCommand = (ChangeConfigCommand) commandOptional.get();
        Assert.assertEquals(2, changeConfigCommand.getVoters().size());
        Assert.assertEquals(0, changeConfigCommand.getLearners().size());
        Set<String> expectedVoters = Sets.newHashSet(voters);
        expectedVoters.remove("store2");
        Assert.assertEquals(expectedVoters, changeConfigCommand.getVoters());
    }

    @Test
    public void rangeWithThreeVAndTwoAliveAndOneStoreSpare() throws InterruptedException {
        KVRangeId id = KVRangeIdUtil.generate();
        List<String> voters = Lists.newArrayList(LOCAL_STORE_ID, "store1", "store2");
        List<String> learners = Lists.newArrayList();
        List<KVRangeDescriptor> rangeDescriptors =
            DescriptorUtils.generateRangeDesc(id, Sets.newHashSet(voters), Sets.newHashSet(learners));
        List<KVRangeStoreDescriptor> storeDescriptors = new ArrayList<>();
        storeDescriptors.add(KVRangeStoreDescriptor.newBuilder()
            .setId("store_spare")
            .build());
        for (int i = 0; i < voters.size(); i++) {
            storeDescriptors.add(KVRangeStoreDescriptor.newBuilder()
                .setId(voters.get(i))
                .addRanges(rangeDescriptors.get(i))
                .build());
        }
        balancer.update(Sets.newHashSet(storeDescriptors));
        storeDescriptors.remove(storeDescriptors.size() - 1);
        balancer.update(Sets.newHashSet(storeDescriptors));
        Thread.sleep(250);
        Optional<BalanceCommand> commandOptional = balancer.balance();
        Assert.assertTrue(commandOptional.isPresent());
        ChangeConfigCommand changeConfigCommand = (ChangeConfigCommand) commandOptional.get();
        Assert.assertEquals(3, changeConfigCommand.getVoters().size());
        Assert.assertEquals(0, changeConfigCommand.getLearners().size());

        Set<String> expectedVoters = Sets.newHashSet(voters);
        expectedVoters.remove("store2");
        expectedVoters.add("store_spare");
        Assert.assertEquals(expectedVoters, changeConfigCommand.getVoters());
    }

    @Test
    public void rangeWithThreeVAndThreeL() throws InterruptedException {
        KVRangeId id = KVRangeIdUtil.generate();
        List<String> voters = Lists.newArrayList(LOCAL_STORE_ID, "store1", "store2");
        List<String> learners = Lists.newArrayList("store3", "store4", "store5");
        List<KVRangeDescriptor> rangeDescriptors =
            DescriptorUtils.generateRangeDesc(id, Sets.newHashSet(voters), Sets.newHashSet(learners));
        Map<String, KVRangeStoreDescriptor> storeDescriptors = new HashMap<>();
        storeDescriptors.put("store_spare_1", KVRangeStoreDescriptor.newBuilder()
            .setId("store_spare_1")
            .build());
        storeDescriptors.put("store_spare_2", KVRangeStoreDescriptor.newBuilder()
            .setId("store_spare_2")
            .addRanges(KVRangeDescriptor.newBuilder()
                .setId(KVRangeIdUtil.generate())
                .setLoadHint(LoadHint.newBuilder().setLoad(1D).build())
            )
            .build());
        for (int i = 0; i < voters.size() + learners.size(); i++) {
            String storeId = i < voters.size() ? voters.get(i) : learners.get(i - voters.size());
            storeDescriptors.put(storeId, KVRangeStoreDescriptor.newBuilder()
                .setId(storeId)
                .addRanges(rangeDescriptors.get(i))
                .build());
        }
        storeDescriptors.remove("store2");
        storeDescriptors.remove("store5");
        balancer.update(Sets.newHashSet(storeDescriptors.values()));
        Optional<BalanceCommand> commandOptional = balancer.balance();
        Assert.assertTrue(commandOptional.isPresent());
        ChangeConfigCommand changeConfigCommand = (ChangeConfigCommand) commandOptional.get();
        Assert.assertEquals(3, changeConfigCommand.getVoters().size());
        Assert.assertEquals(3, changeConfigCommand.getLearners().size());

        Set<String> expectedVoters = Sets.newHashSet(LOCAL_STORE_ID, "store1", "store_spare_1");
        Set<String> expectedLearners = Sets.newHashSet("store3", "store4", "store_spare_2");
        Assert.assertEquals(expectedVoters, changeConfigCommand.getVoters());
        Assert.assertEquals(expectedLearners, changeConfigCommand.getLearners());
    }

}
