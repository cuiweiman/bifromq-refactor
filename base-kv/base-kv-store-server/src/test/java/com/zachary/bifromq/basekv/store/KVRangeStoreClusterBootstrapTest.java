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
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.zachary.bifromq.basekv.Constants.FULL_RANGE;
import static org.awaitility.Awaitility.await;

@Slf4j
@Listeners(KVRangeStoreTestListener.class)
public class KVRangeStoreClusterBootstrapTest extends KVRangeStoreClusterTestTemplate {

    @Cluster(initNodes = 1)
    @Test(groups = "integration")
    public void testBootstrap1StoreCluster() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        await().until(() -> {
            KVRangeSetting rangeSetting = cluster.kvRangeSetting(rangeId);
            if (rangeSetting == null) {
                return false;
            }
            return rangeSetting.allReplicas.size() == 1;
        });
    }

    @Cluster(initNodes = 2)
    @Test(groups = "integration")
    public void testBootstrap2StoreCluster() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        await().until(() -> {
            KVRangeSetting rangeSetting = cluster.kvRangeSetting(rangeId);
            if (rangeSetting == null) {
                return false;
            }
            return rangeSetting.ver >= 2 &&
                FULL_RANGE.equals(rangeSetting.range) &&
                rangeSetting.allReplicas.size() == 2;
        });
    }

    @Test(groups = "integration")
    public void testBootstrap3StoreCluster() {
        KVRangeId rangeId = cluster.genesisKVRangeId();
        await().until(() -> {
            KVRangeSetting rangeSetting = cluster.kvRangeSetting(rangeId);
            if (rangeSetting == null) {
                return false;
            }
            return rangeSetting.ver >= 2 &&
                FULL_RANGE.equals(rangeSetting.range) &&
                rangeSetting.allReplicas.size() == 3;
        });
    }
}
