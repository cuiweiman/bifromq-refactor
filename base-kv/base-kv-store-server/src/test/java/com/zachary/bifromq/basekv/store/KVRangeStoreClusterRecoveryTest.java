package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.annotation.Cluster;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static com.zachary.bifromq.basekv.raft.proto.RaftNodeStatus.Candidate;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertEquals;

@Slf4j
@Listeners(KVRangeStoreTestListener.class)
public class KVRangeStoreClusterRecoveryTest extends KVRangeStoreClusterTestTemplate {

    @Cluster(initNodes = 2)
    @Test(groups = "integration")
    public void recoveryFromTwoToOne() {
        KVRangeId genesisKVRangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSetting = cluster.awaitAllKVRangeReady(genesisKVRangeId, 2, 5000);
        String leader = rangeSetting.leader;
        List<String> storeIds = cluster.allStoreIds();
        assertEquals(storeIds.size(), 2);
        storeIds.remove(leader);
        cluster.shutdownStore(storeIds.get(0));
        await().ignoreExceptions().until(() -> {
            KVRangeSetting s = cluster.kvRangeSetting(genesisKVRangeId);
            return s != null && cluster.getKVRange(leader, genesisKVRangeId).getRole() == Candidate;
        });

        cluster.recover(leader).toCompletableFuture().join();
        await().until(() -> {
            KVRangeSetting s = cluster.kvRangeSetting(genesisKVRangeId);
            return s != null && s.followers.size() == 0 && s.leader.equals(leader);
        });
    }

    @Cluster(initNodes = 3)
    @Test(groups = "integration")
    public void recoveryFromThreeToOne() {
        KVRangeId genesisKVRangeId = cluster.genesisKVRangeId();
        KVRangeSetting setting = cluster.awaitAllKVRangeReady(genesisKVRangeId, 2, 5000);
        String leader = setting.leader;
        List<String> storeIds = cluster.allStoreIds();
        assertEquals(storeIds.size(), 3);
        storeIds.remove(leader);
        cluster.shutdownStore(storeIds.get(0));
        cluster.shutdownStore(storeIds.get(1));
        log.info("Wait for becoming candidate");
        await().atMost(Duration.ofSeconds(10)).until(() -> {
            KVRangeSetting s = cluster.kvRangeSetting(genesisKVRangeId);
            return s != null && cluster.getKVRange(leader, genesisKVRangeId).getRole() == Candidate;
        });

        cluster.recover(leader).toCompletableFuture().join();
        await().until(() -> {
            KVRangeSetting s = cluster.kvRangeSetting(genesisKVRangeId);
            return s != null &&
                s.followers.size() == 0 &&
                s.leader.equals(leader);
        });
    }
}
