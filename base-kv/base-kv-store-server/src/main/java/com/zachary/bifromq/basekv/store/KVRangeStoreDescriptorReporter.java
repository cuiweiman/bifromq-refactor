package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basecrdt.core.api.IORMap;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.MVRegOperation;
import com.zachary.bifromq.basecrdt.core.api.operation.ORMapOperation;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basehlc.HLC;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.zachary.bifromq.basekv.store.CRDTUtil.getDescriptorFromCRDT;
import static com.zachary.bifromq.basekv.store.CRDTUtil.storeDescriptorMapCRDTURI;

@Slf4j
public class KVRangeStoreDescriptorReporter implements IKVRangeStoreDescriptorReporter {
    private static final long WAIT_CLEANUP_SPREAD_MILLIS = 1000L;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final ICRDTService crdtService;
    private final String clusterId;
    private final IORMap storeDescriptorMap;
    private final long deadStoreCleanupInMillis;

    private KVRangeStoreDescriptor localStoreDescriptor;

    public KVRangeStoreDescriptorReporter(@NonNull String clusterId,
                                          @NonNull ICRDTService crdtService,
                                          @NonNull Long deadStoreCleanupInMillis) {
        this.clusterId = clusterId;
        this.crdtService = crdtService;
        this.deadStoreCleanupInMillis = deadStoreCleanupInMillis;
        String uri = storeDescriptorMapCRDTURI(clusterId);
        crdtService.host(uri);
        storeDescriptorMap = (IORMap) crdtService.get(uri).get();
    }

    @Override
    public void start() {
        disposables.add(storeDescriptorMap.inflation().subscribe(t -> checkAndHealStoreDescriptorList()));
    }

    @Override
    public void stop() {
        disposables.dispose();
        if (localStoreDescriptor != null) {
            removeStoreDescriptor(localStoreDescriptor.getId()).join();
            try {
                // ICausalCRDT could supply a way to wait localJoin spread to remote replicas
                Thread.sleep(WAIT_CLEANUP_SPREAD_MILLIS);
            } catch (InterruptedException e) {
                log.warn("Interrupted when waiting localStoreDescriptor cleanup spread", e);
            }
        }
        crdtService.stopHosting(storeDescriptorMapCRDTURI(clusterId)).join();
    }

    @Override
    public void report(KVRangeStoreDescriptor storeDescriptor) {
        localStoreDescriptor = storeDescriptor.toBuilder().build();
        log.debug("Report store descriptor\n{}", localStoreDescriptor);
        ByteString ormapKey = ByteString.copyFromUtf8(localStoreDescriptor.getId());
        Optional<KVRangeStoreDescriptor> settingsOnCRDT = getDescriptorFromCRDT(storeDescriptorMap, ormapKey);
        if (settingsOnCRDT.isEmpty() || !settingsOnCRDT.get().equals(localStoreDescriptor)) {
            storeDescriptorMap.execute(ORMapOperation
                .update(ormapKey)
                .with(MVRegOperation.write(localStoreDescriptor.toByteString())));
        }
    }

    private void checkAndHealStoreDescriptorList() {
        if (localStoreDescriptor == null) {
            return;
        }
        Map<ByteString, KVRangeStoreDescriptor> storedDescriptors = new HashMap<>();
        storeDescriptorMap.keys().forEachRemaining(ormapKey ->
            getDescriptorFromCRDT(storeDescriptorMap, ormapKey.key())
                .ifPresent(descriptor -> storedDescriptors.put(ormapKey.key(), descriptor)));
        ByteString ormapKey = ByteString.copyFromUtf8(localStoreDescriptor.getId());
        // check if some store is not alive anymore
        storedDescriptors.remove(ormapKey);
        for (KVRangeStoreDescriptor storeDescriptor : storedDescriptors.values()) {
            long elapsed = HLC.INST.getPhysical() - HLC.INST.getPhysical(storeDescriptor.getHlc());
            if (elapsed > deadStoreCleanupInMillis) {
                log.debug("store[{}] is not alive, remove its storeDescriptor", storeDescriptor.getId());
                removeStoreDescriptor(storeDescriptor.getId());
            }
        }
    }

    private CompletableFuture<Void> removeStoreDescriptor(String storeId) {
        return storeDescriptorMap.execute(ORMapOperation
            .remove(ByteString.copyFromUtf8(storeId))
            .of(CausalCRDTType.mvreg)
        );
    }

}
