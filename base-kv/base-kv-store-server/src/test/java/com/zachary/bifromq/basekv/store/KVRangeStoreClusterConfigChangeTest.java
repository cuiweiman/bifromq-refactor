/*
 * Copyright (c) 2023. Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basekv.KVRangeSetting;
import com.zachary.bifromq.basekv.annotation.Cluster;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static org.awaitility.Awaitility.await;

@Slf4j
@Listeners(KVRangeStoreTestListener.class)
public class KVRangeStoreClusterConfigChangeTest extends KVRangeStoreClusterTestTemplate {
    @Test(groups = "integration")
    public void removeNonLeaderReplicaFromNonLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting setting = await().until(() -> cluster.kvRangeSetting(rangeId), Objects::nonNull);
        String leaderStore = setting.leader;
        String remainStore = nonLeaderStore(setting);

        await().ignoreExceptions().until(() -> {
            KVRangeSetting newSetting = cluster.kvRangeSetting(rangeId);
            if (newSetting.allReplicas.size() == 2) {
                return true;
            }
            cluster.changeReplicaConfig(remainStore, newSetting.ver, rangeId, Sets.newHashSet(leaderStore, remainStore),
                emptySet()).toCompletableFuture().join();
            newSetting = cluster.kvRangeSetting(rangeId);
            return newSetting.allReplicas.size() == 2;
        });
    }

    @Test(groups = "integration")
    public void removeNonLeaderReplicaFromHostingStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting setting = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);
        String leaderStore = setting.leader;
        String remainStore = nonLeaderStore(setting);
        String removedStore = setting.followers
            .stream()
            .filter(storeId -> !storeId.equals(remainStore))
            .collect(Collectors.joining());

        log.info("Remove replica[{}]", removedStore);
        await().ignoreExceptions().until(() -> {
            KVRangeSetting newSetting = cluster.kvRangeSetting(rangeId);
            if (newSetting.allReplicas.size() == 2 &&
                !newSetting.allReplicas.contains(removedStore)) {
                return true;
            }
            cluster.changeReplicaConfig(remainStore, newSetting.ver, rangeId, Sets.newHashSet(leaderStore, remainStore),
                emptySet()).toCompletableFuture().join();
            newSetting = cluster.kvRangeSetting(rangeId);
            return
                newSetting.allReplicas.size() == 2 &&
                    !newSetting.allReplicas.contains(removedStore);
        });
    }

    @Test(groups = "integration")
    public void removeLeaderReplicaFromLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSettings = await().until(() -> cluster.kvRangeSetting(rangeId), Objects::nonNull);
        String leaderStore = rangeSettings.leader;
        List<String> remainStores = rangeSettings.followers;
        log.info("Remove: {}, remain: {}", leaderStore, remainStores);

        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (remainStores.containsAll(setting.allReplicas)) {
                return true;
            }
            cluster.changeReplicaConfig(leaderStore, setting.ver, rangeId, Sets.newHashSet(remainStores), emptySet())
                .toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return remainStores.containsAll(setting.allReplicas);
        });
    }

    @Test(groups = "integration")
    public void removeFailedReplica() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSettings = cluster.kvRangeSetting(rangeId);
        String leaderStore = rangeSettings.leader;
        String failureStore = nonLeaderStore(rangeSettings);
        log.info("shutdown store {}", failureStore);
        cluster.shutdownStore(failureStore);

        List<String> remainStores = Lists.newArrayList(rangeSettings.allReplicas);
        remainStores.remove(failureStore);
        log.info("Remain: {}", remainStores);
        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (remainStores.containsAll(setting.allReplicas)) {
                return true;
            }
            cluster.changeReplicaConfig(leaderStore, setting.ver, rangeId, Sets.newHashSet(remainStores), emptySet())
                .toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return remainStores.containsAll(setting.allReplicas);
        });
    }

    @Test(groups = "integration")
    public void removeNonLeaderReplicaFromLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSettings = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);
        String leaderStore = rangeSettings.leader;
        String remainStore = nonLeaderStore(rangeSettings);

        await().ignoreExceptions().until(() -> {
            KVRangeSetting newSetting = cluster.kvRangeSetting(rangeId);
            if (newSetting.allReplicas.size() == 2 &&
                newSetting.allReplicas.contains(leaderStore) &&
                newSetting.allReplicas.contains(remainStore)) {
                return true;
            }
            cluster.changeReplicaConfig(remainStore, newSetting.ver, rangeId, Sets.newHashSet(leaderStore, remainStore),
                emptySet()).toCompletableFuture().join();
            newSetting = cluster.kvRangeSetting(rangeId);
            return newSetting.allReplicas.size() == 2 &&
                newSetting.allReplicas.contains(leaderStore) &&
                newSetting.allReplicas.contains(remainStore);
        });
    }

    @Test(groups = "integration")
    public void removeLeaderReplicaFromNonLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSettings = cluster.kvRangeSetting(rangeId);
        String leaderStore = rangeSettings.leader;
        List<String> remainStores = Lists.newArrayList(rangeSettings.allReplicas);
        remainStores.remove(leaderStore);
        log.info("Remain: {}", remainStores);

        await().ignoreExceptions().until(() -> {
            KVRangeSetting newSetting = cluster.kvRangeSetting(rangeId);
            if (newSetting.allReplicas.size() == 2 &&
                !newSetting.allReplicas.contains(leaderStore)) {
                return true;
            }
            cluster.changeReplicaConfig(remainStores.get(0), newSetting.ver, rangeId, Sets.newHashSet(remainStores),
                emptySet()).toCompletableFuture().join();
            newSetting = cluster.kvRangeSetting(rangeId);
            return newSetting.allReplicas.size() == 2 &&
                !newSetting.allReplicas.contains(leaderStore);
        });
    }

    @Cluster(initNodes = 1)
    @Test(groups = "integration")
    public void addReplicaFromLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        String newStore = cluster.addStore();
        log.info("add replica {}", newStore);

        await().atMost(Duration.ofSeconds(60)).ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (setting.allReplicas.size() == 2) {
                return true;
            }
            cluster.changeReplicaConfig(setting.leader, setting.ver, rangeId, Sets.newHashSet(setting.leader, newStore),
                emptySet()).toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.allReplicas.size() == 2;
        });
    }

    @Cluster(initNodes = 2)
    @Test(groups = "integration")
    public void addReplicaFromNonLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSettings = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);
        String newStore = cluster.addStore();
        Set<String> newReplicas = Sets.newHashSet(rangeSettings.allReplicas);
        newReplicas.add(newStore);

        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (setting.allReplicas.size() == 3 && setting.allReplicas.contains(newStore)) {
                return true;
            }
            cluster.changeReplicaConfig(nonLeaderStore(setting), setting.ver, rangeId, newReplicas, emptySet())
                .toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return setting.allReplicas.size() == 3 && setting.allReplicas.contains(newStore);
        });
    }

    @Test(groups = "integration")
    public void jointChangeReplicasFromLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSettings = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);
        String newStore1 = cluster.addStore();
        String newStore2 = cluster.addStore();
        String newStore3 = cluster.addStore();
        Set<String> newReplicas = Sets.newHashSet(newStore1, newStore2, newStore3);
        await().ignoreExceptions().until(() -> {
            KVRangeSetting setting = cluster.kvRangeSetting(rangeId);
            if (newReplicas.containsAll(setting.allReplicas)) {
                return true;
            }
            cluster.changeReplicaConfig(setting.leader, setting.ver, rangeId, newReplicas, emptySet())
                .toCompletableFuture().join();
            setting = cluster.kvRangeSetting(rangeId);
            return newReplicas.containsAll(setting.allReplicas);
        });
    }

    @Test(groups = "integration")
    public void jointChangeReplicasFromNonLeaderStore() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        KVRangeSetting rangeSettings = cluster.awaitAllKVRangeReady(rangeId, 1, 5000);
        String newStore1 = cluster.addStore();
        String newStore2 = cluster.addStore();
        String newStore3 = cluster.addStore();
        Set<String> newReplicas = Sets.newHashSet(newStore1, newStore2, newStore3);

        log.info("Current config: {}", rangeSettings.allReplicas);
        await().ignoreExceptions().forever().until(() -> {
            KVRangeSetting newSettings = cluster.kvRangeSetting(rangeId);
            if (newReplicas.containsAll(newSettings.allReplicas)) {
                return true;
            }
            log.info("Joint-Config change to {}", newReplicas);
            cluster.changeReplicaConfig(nonLeaderStore(rangeSettings), newSettings.ver, rangeId, newReplicas,
                emptySet()).toCompletableFuture().join();
            newSettings = cluster.kvRangeSetting(rangeId);
            return newReplicas.contains(newSettings.allReplicas);
        });
    }
}
