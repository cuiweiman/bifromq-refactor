package com.zachary.bifromq.basekv.store;

import com.zachary.bifromq.basecrdt.core.api.IMVReg;
import com.zachary.bifromq.basecrdt.core.api.IORMap;
import com.zachary.bifromq.basecrdt.core.api.IORMap.ORMapKey;
import com.zachary.bifromq.basecrdt.core.api.enums.CausalCRDTType;
import com.zachary.bifromq.basecrdt.core.api.operation.ORMapOperation;
import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basehlc.HLC;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.zachary.bifromq.basekv.store.CRDTUtil.storeDescriptorMapCRDTURI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KVRangeStoreDescriptorReporterTest {

    @Mock
    private ICRDTService crdtService;
    @Mock
    private IORMap storeDescriptorMap;
    @Mock
    private IMVReg localMVReg;
    @Mock
    private IMVReg remoteMVReg;

    private KVRangeStoreDescriptorReporter storeDescriptorReporter;
    private Subject<Long> inflationSubject = PublishSubject.create();
    private String localStoreId = "localStoreId";
    private String remoteStoreId = "remoteStoreId";
    private KVRangeStoreDescriptor storeDescriptor;
    private KVRangeStoreDescriptor remoteStoreDescriptor;
    private AutoCloseable closeable;
    @BeforeMethod
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        String uri = storeDescriptorMapCRDTURI("testCluster");
        when(crdtService.get(uri)).thenReturn(Optional.of(storeDescriptorMap));
        storeDescriptorReporter = new KVRangeStoreDescriptorReporter("testCluster", crdtService, 200L);
        storeDescriptor = KVRangeStoreDescriptor.newBuilder()
            .setId(localStoreId)
            .setHlc(HLC.INST.get())
            .build();
        remoteStoreDescriptor = KVRangeStoreDescriptor.newBuilder()
            .setId(remoteStoreId)
            .setHlc(HLC.INST.get())
            .build();
        when(storeDescriptorMap.inflation()).thenReturn(inflationSubject);
        storeDescriptorReporter.start();
    }

    @AfterMethod
    public void close() throws Exception {
        when(storeDescriptorMap.execute(any(ORMapOperation.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(crdtService.stopHosting(anyString())).thenReturn(CompletableFuture.completedFuture(null));
        storeDescriptorReporter.stop();
        closeable.close();
    }

    @Test
    public void testReportWithStoreDescriptorNotExist() {
        when(storeDescriptorMap.getMVReg(ByteString.copyFromUtf8(localStoreId))).thenReturn(localMVReg);
        when(localMVReg.read()).thenReturn(Collections.emptyIterator());
        storeDescriptorReporter.report(storeDescriptor);
        verify(storeDescriptorMap, times(1)).execute(any(ORMapOperation.ORMapUpdate.class));

    }

    @Test
    public void testReportWithStoreDescriptorExist() {
        when(storeDescriptorMap.getMVReg(ByteString.copyFromUtf8(localStoreId))).thenReturn(localMVReg);
        when(localMVReg.read()).thenReturn(Lists.<ByteString>newArrayList(storeDescriptor.toByteString()).iterator());
        storeDescriptorReporter.report(storeDescriptor);
        verify(storeDescriptorMap, times(0)).execute(any(ORMapOperation.ORMapUpdate.class));
    }

    @Test
    public void testRemoveRemoteDescriptorNotAlive() throws InterruptedException {
        when(storeDescriptorMap.getMVReg(ByteString.copyFromUtf8(localStoreId))).thenReturn(localMVReg);
        when(storeDescriptorMap.getMVReg(ByteString.copyFromUtf8(remoteStoreId))).thenReturn(remoteMVReg);
        when(localMVReg.read())
            .thenReturn(Lists.<ByteString>newArrayList(storeDescriptor.toByteString()).iterator())
            .thenReturn(Lists.<ByteString>newArrayList(storeDescriptor.toByteString()).iterator());
        when(remoteMVReg.read()).thenReturn(
            Lists.<ByteString>newArrayList(remoteStoreDescriptor.toByteString()).iterator());
        ORMapKey localKey = new ORMapKey() {
            @Override
            public ByteString key() {
                return ByteString.copyFromUtf8(localStoreId);
            }

            @Override
            public CausalCRDTType valueType() {
                return CausalCRDTType.mvreg;
            }
        };
        ORMapKey remoteKey = new ORMapKey() {
            @Override
            public ByteString key() {
                return ByteString.copyFromUtf8(remoteStoreId);
            }

            @Override
            public CausalCRDTType valueType() {
                return CausalCRDTType.mvreg;
            }
        };
        when(storeDescriptorMap.keys()).thenReturn(Sets.newHashSet(localKey, remoteKey).iterator());
        storeDescriptorReporter.report(storeDescriptor);
        Thread.sleep(400);
        inflationSubject.onNext(System.currentTimeMillis());
        verify(storeDescriptorMap, times(1)).execute(any(ORMapOperation.ORMapRemove.class));
    }

}
