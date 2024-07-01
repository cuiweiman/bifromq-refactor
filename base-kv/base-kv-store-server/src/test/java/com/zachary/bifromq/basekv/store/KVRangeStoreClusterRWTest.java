package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Optional;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static java.util.Collections.emptySet;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertEquals;

@Slf4j
@Listeners(KVRangeStoreTestListener.class)
public class KVRangeStoreClusterRWTest extends KVRangeStoreClusterTestTemplate {
    @Test(groups = "integration")
    public void readFromLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSetting = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);
        for (int i = 0; i < 10; i++) {
            cluster.put(rangeSetting.leader, rangeId, copyFromUtf8("key" + i), copyFromUtf8("value" + i));
            Optional<ByteString> getValue = cluster.get(rangeSetting.leader, rangeId, copyFromUtf8("key" + i));
            assertEquals(getValue.get(), copyFromUtf8("value" + i));
        }
    }

    @Test(groups = "integration")
    public void readFromNonLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSetting = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);
        for (int i = 0; i < 10; i++) {
            cluster.put(rangeSetting.leader, rangeId, copyFromUtf8("key" + i), copyFromUtf8("value" + i));
            Optional<ByteString> getValue = cluster.get(nonLeaderStore(rangeSetting), rangeId, copyFromUtf8("key" + i));
            assertEquals(getValue.get(), copyFromUtf8("value" + i));
        }
    }

    @Test(groups = "integration")
    public void readWhenReplicaRestart() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSetting = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);
        String restartStoreId = nonLeaderStore(rangeSetting);

        log.info("Shutdown store {}", restartStoreId);
        cluster.shutdownStore(restartStoreId);
        for (int i = 0; i < 10; i++) {
            cluster.put(rangeSetting.leader, rangeId, copyFromUtf8("key" + i), copyFromUtf8("value" + i));
            Optional<ByteString> resp = cluster.get(rangeSetting.leader, rangeId, copyFromUtf8("key" + i));
            assertEquals(resp.get(), copyFromUtf8("value" + i));
        }
        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            cluster.changeReplicaConfig(rangeSetting.leader,
                setting.ver,
                rangeId,
                Sets.newHashSet(cluster.allStoreIds()),
                emptySet()
            ).toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.allReplicas.size() == 2 && !setting.allReplicas.contains(restartStoreId);
        });
        log.info("Restart store {}", restartStoreId);
        cluster.startStore(restartStoreId);

        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            cluster.changeReplicaConfig(rangeSetting.leader,
                setting.ver,
                rangeId,
                Sets.newHashSet(cluster.allStoreIds()),
                emptySet()
            ).toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.allReplicas.size() == 3 && setting.allReplicas.contains(restartStoreId);
        });

        cluster.awaitKVRangeReady(restartStoreId, rangeId);
        for (int i = 0; i < 10; i++) {
            Optional<ByteString> resp = cluster.get(restartStoreId, rangeId, copyFromUtf8("key" + i));
            assertEquals(resp.get(), copyFromUtf8("value" + i));
        }
    }

    @Test(groups = "integration")
    public void readWhileAddNewReplica() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSettings = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);

        for (int i = 0; i < 10; i++) {
            cluster.put(rangeSettings.leader, rangeId, copyFromUtf8("key" + i), copyFromUtf8("value" + i));
            Optional<ByteString> resp = cluster.get(rangeSettings.leader, rangeId, copyFromUtf8("key" + i));
            assertEquals(resp.get(), copyFromUtf8("value" + i));
        }
        String storeId = cluster.addStore();
        log.info("Add new store: {}", storeId);
        log.info("Change replica set to: {}", cluster.allStoreIds());
        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            cluster.changeReplicaConfig(rangeSettings.leader,
                setting.ver,
                rangeId,
                Sets.newHashSet(cluster.allStoreIds()),
                Sets.newHashSet()
            ).toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.allReplicas.contains(storeId);
        });
        cluster.awaitKVRangeReady(storeId, rangeId);
        log.info("New store ready");
        for (int i = 0; i < 10; i++) {
            Optional<ByteString> resp = cluster.get(storeId, rangeId, copyFromUtf8("key" + i));
            assertEquals(resp.get(), copyFromUtf8("value" + i));
        }
    }
}
