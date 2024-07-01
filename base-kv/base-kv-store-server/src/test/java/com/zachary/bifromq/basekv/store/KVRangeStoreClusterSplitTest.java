package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.Range;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static org.awaitility.Awaitility.await;
import static org.testng.Assert.assertEquals;

@Slf4j
@Listeners(KVRangeStoreTestListener.class)
public class KVRangeStoreClusterSplitTest extends KVRangeStoreClusterTestTemplate {

    @Test(groups = "integration")
    public void splitFromLeaderStore() {
        KVRangeId genesisKVRangeId = cluster.genesisKVRangeId();
        KVRangeSetting genesisKVRangeSettings = cluster.awaitAllKVRangeReady(genesisKVRangeId, 1, 5000);
        cluster.split(genesisKVRangeSettings.leader,
                genesisKVRangeSettings.ver,
                genesisKVRangeId,
                copyFromUtf8("e"))
            .toCompletableFuture().join();
        await().atMost(Duration.ofSeconds(10)).until(() -> cluster.allKVRangeIds().size() == 2);
        for (KVRangeId kvRangeId : cluster.allKVRangeIds()) {
            await().atMost(Duration.ofSeconds(5)).until(() -> {
                KVRangeSetting kvRangeSettings = cluster.kvRangeSetting(kvRangeId);
                return kvRangeSettings.allReplicas.size() == 3;
            });

            KVRangeSetting kvRangeSettings = cluster.kvRangeSetting(kvRangeId);
            assertEquals(kvRangeSettings.ver, genesisKVRangeSettings.ver + 1);
            if (kvRangeId.equals(genesisKVRangeId)) {
                assertEquals(kvRangeSettings.leader, genesisKVRangeSettings.leader);
                assertEquals(kvRangeSettings.range, Range.newBuilder()
                    .setEndKey(ByteString.copyFromUtf8("e"))
                    .build());
            } else {
                assertEquals(kvRangeSettings.range, Range.newBuilder()
                    .setStartKey(ByteString.copyFromUtf8("e"))
                    .build());
            }
        }
    }

    @Test(groups = "integration")
    public void splitFromNonLeaderStore() {
        KVRangeId genesisKVRangeId = cluster.genesisKVRangeId();
        KVRangeSetting genesisKVRangeSettings = cluster.awaitAllKVRangeReady(genesisKVRangeId, 1, 5000);
        String nonLeaderStore = nonLeaderStore(genesisKVRangeSettings);
        cluster.awaitKVRangeReady(nonLeaderStore, genesisKVRangeId);
        cluster.split(nonLeaderStore, genesisKVRangeSettings.ver, genesisKVRangeId, copyFromUtf8("e"))
            .toCompletableFuture().join();
        await().atMost(Duration.ofSeconds(20)).until(() -> cluster.allKVRangeIds().size() == 2);
        for (KVRangeId kvRangeId : cluster.allKVRangeIds()) {
            await().atMost(Duration.ofSeconds(5)).until(() -> {
                KVRangeSetting kvRangeSettings = cluster.kvRangeSetting(kvRangeId);
                return kvRangeSettings.allReplicas.size() == 3;
            });
            KVRangeSetting kvRangeSettings = cluster.kvRangeSetting(kvRangeId);
            assertEquals(kvRangeSettings.ver, genesisKVRangeSettings.ver + 1);
            if (kvRangeId.equals(genesisKVRangeId)) {
                assertEquals(kvRangeSettings.leader, genesisKVRangeSettings.leader);
                assertEquals(kvRangeSettings.range, Range.newBuilder()
                    .setEndKey(ByteString.copyFromUtf8("e"))
                    .build());
            } else {
                assertEquals(kvRangeSettings.range, Range.newBuilder()
                    .setStartKey(ByteString.copyFromUtf8("e"))
                    .build());
            }
        }
    }
}
