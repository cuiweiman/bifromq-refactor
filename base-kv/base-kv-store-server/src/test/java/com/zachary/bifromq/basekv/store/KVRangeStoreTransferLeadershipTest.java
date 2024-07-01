package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.exception.KVRangeException;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Slf4j
@Listeners(KVRangeStoreTestListener.class)
public class KVRangeStoreTransferLeadershipTest extends KVRangeStoreClusterTestTemplate {

    @Test(groups = "integration")
    public void testRequestTransferLeadershipFromLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        await().until(() -> {
            KVRangeSetting settings = cluster.kvRangeSetting(rangeId);
            return settings != null && settings.ver >= 2;
        });
        KVRangeSetting rangeSettings = cluster.kvRangeSetting(rangeId);
        String oldLeader = rangeSettings.leader;
        String newLeader = nonLeaderStore(rangeSettings);
        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (setting.leader.equals(newLeader)) {
                return true;
            }
            cluster.transferLeader(oldLeader, setting.ver, rangeId, newLeader).toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.leader.equals(newLeader);
        });
    }

    @Test(groups = "integration")
    public void testTransferLeadershipToFakeLeader() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSetting = cluster.kvRangeSetting(rangeId);
        String oldLeader = rangeSetting.leader;

        try {
            cluster.transferLeader(oldLeader, rangeSetting.ver, rangeId, "FakeLeader").toCompletableFuture().join();
            fail();
        } catch (Throwable e) {
            assertTrue(e.getCause() instanceof KVRangeException.BadRequest);
        }
    }

    @Test(groups = "integration")
    public void testRequestTransferLeadershipFromNonLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        await().until(() -> {
            KVRangeSetting settings = cluster.kvRangeSetting(rangeId);
            return settings != null && settings.ver >= 2;
        });

        KVRangeSetting rangeSetting = cluster.kvRangeSetting(rangeId);
        String oldLeader = rangeSetting.leader;
        String newLeader = nonLeaderStore(rangeSetting);
        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (setting.leader.equals(newLeader)) {
                return true;
            }
            cluster.transferLeader(newLeader, setting.ver, rangeId, newLeader).toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.leader.equals(newLeader);
        });
    }

    @Test(groups = "integration")
    public void testTransferLeadershipToSelf() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        await().until(() -> {
            KVRangeSetting settings = cluster.kvRangeSetting(rangeId);
            return settings != null && settings.ver >= 2;
        });
        await().ignoreExceptions().until(() -> {
            KVRangeSetting rangeSettings = cluster.kvRangeSetting(rangeId);
            cluster.transferLeader(rangeSettings.leader, rangeSettings.ver, rangeId, rangeSettings.leader)
                .toCompletableFuture().join();
            KVRangeSetting newRangeSettings = cluster.kvRangeSetting(rangeId);
            return newRangeSettings.leader.equals(rangeSettings.leader);
        });
    }

    @Test(groups = "integration")
    public void testTransferLeadershipConcurrently() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        await().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            return setting != null && setting.ver >= 2;
        });
        KVRangeSetting rangeSettings = cluster.kvRangeSetting(rangeId);
        String oldLeader = rangeSettings.leader;
        String newLeader = nonLeaderStore(rangeSettings);

        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (setting.leader.equals(newLeader)) {
                return true;
            }
            cluster.transferLeader(oldLeader, setting.ver, rangeId, newLeader).toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.leader.equals(newLeader);
        });
        // transfer back
        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (setting.leader.equals(oldLeader)) {
                return true;
            }
            cluster.transferLeader(oldLeader, setting.ver, rangeId, oldLeader).toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.leader.equals(oldLeader);
        });
    }
}
