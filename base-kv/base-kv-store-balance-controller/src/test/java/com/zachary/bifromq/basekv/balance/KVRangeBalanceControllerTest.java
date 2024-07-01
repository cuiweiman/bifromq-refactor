package com.zachary.bifromq.basekv.balance;

import com.zachary.bifromq.basekv.balance.option.KVRangeBalanceControllerOptions;
import com.zachary.bifromq.basekv.balance.utils.DescriptorUtils;
import com.zachary.bifromq.basekv.client.IBaseKVStoreClient;
import com.zachary.bifromq.basekv.proto.KVRangeDescriptor;
import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.proto.KVRangeStoreDescriptor;
import com.zachary.bifromq.basekv.store.proto.ChangeReplicaConfigReply;
import com.zachary.bifromq.basekv.store.proto.ReplyCode;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KVRangeBalanceControllerTest {

    private static final String LOCAL_STORE_ID = "localStoreId";

    @Mock
    private IBaseKVStoreClient storeClient;

    private final PublishSubject<Set<KVRangeStoreDescriptor>> storeDescSubject = PublishSubject.create();

    private KVRangeBalanceController KVRangeBalanceController;
    private AutoCloseable closeable;
    @BeforeMethod
    public void setup() throws IOException {
        closeable = MockitoAnnotations.openMocks(this);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        File optFile =
            new File(KVRangeBalanceControllerTest.class.getClassLoader().getResource("controller.yml").getPath());
        KVRangeBalanceControllerOptions
            KVRangeBalanceControllerOptions = mapper.readValue(optFile, KVRangeBalanceControllerOptions.class);
        KVRangeBalanceController = new KVRangeBalanceController(
            storeClient,
            KVRangeBalanceControllerOptions,
            Executors.newScheduledThreadPool(1)
        );
        when(storeClient.describe()).thenReturn(storeDescSubject);
    }

    @AfterMethod
    public void clear() throws Exception {
        KVRangeBalanceController.stop();
        closeable.close();
    }

    @Test
    public void testWithoutStoreDescriptors() throws InterruptedException {
        KVRangeBalanceController.start(LOCAL_STORE_ID);
        Thread.sleep(1200);
        verify(storeClient, times(0)).changeReplicaConfig(anyString(), any());
    }

    @Test
    public void testWithStoreDescriptorsUpdate() throws InterruptedException {
        KVRangeBalanceController.start(LOCAL_STORE_ID);
        Thread.sleep(1200);
        verify(storeClient, times(0)).changeReplicaConfig(anyString(), any());
        KVRangeId id = KVRangeIdUtil.generate();
        List<String> voters = Lists.newArrayList(LOCAL_STORE_ID, "store1");
        List<String> learners = Lists.newArrayList();
        List<KVRangeDescriptor> rangeDescriptors =
            DescriptorUtils.generateRangeDesc(id, Sets.newHashSet(voters), Sets.newHashSet(learners));
        Set<KVRangeStoreDescriptor> storeDescriptors = new HashSet<>();
        for (int i = 0; i < voters.size(); i++) {
            storeDescriptors.add(KVRangeStoreDescriptor.newBuilder()
                .setId(voters.get(i))
                .addRanges(rangeDescriptors.get(i))
                .build());
        }
        storeDescSubject.onNext(storeDescriptors);
        Thread.sleep(1200);
        verify(storeClient, times(0)).changeReplicaConfig(anyString(), any());
        // New store
        storeDescriptors = Sets.newHashSet(storeDescriptors);
        storeDescriptors.add(
            KVRangeStoreDescriptor.newBuilder()
                .setId("store2")
                .build()
        );
        when(storeClient.changeReplicaConfig(anyString(), any())).thenReturn(CompletableFuture.completedFuture(
            ChangeReplicaConfigReply.newBuilder()
                .setCode(ReplyCode.Ok)
                .build()
        ));
        storeDescSubject.onNext(storeDescriptors);
        Thread.sleep(1200);
        verify(storeClient, times(1)).changeReplicaConfig(anyString(), any());
    }

    @Test
    public void testWithCommandRunFail() throws InterruptedException {
        KVRangeBalanceController.start(LOCAL_STORE_ID);
        KVRangeId id = KVRangeIdUtil.generate();
        List<String> voters = Lists.newArrayList(LOCAL_STORE_ID, "store1");
        List<String> learners = Lists.newArrayList();
        List<KVRangeDescriptor> rangeDescriptors =
            DescriptorUtils.generateRangeDesc(id, Sets.newHashSet(voters), Sets.newHashSet(learners));
        Set<KVRangeStoreDescriptor> storeDescriptors = new HashSet<>();
        for (int i = 0; i < voters.size(); i++) {
            storeDescriptors.add(KVRangeStoreDescriptor.newBuilder()
                .setId(voters.get(i))
                .addRanges(rangeDescriptors.get(i))
                .build());
        }
        storeDescSubject.onNext(storeDescriptors);

        when(storeClient.changeReplicaConfig(anyString(), any())).thenReturn(
            CompletableFuture.completedFuture(
                ChangeReplicaConfigReply.newBuilder()
                    .setCode(ReplyCode.TryLater)
                    .build()
            ),
            CompletableFuture.completedFuture(
                ChangeReplicaConfigReply.newBuilder()
                    .setCode(ReplyCode.Ok)
                    .build()
            )
        );
        // New store
        storeDescriptors = Sets.newHashSet(storeDescriptors);
        storeDescriptors.add(
            KVRangeStoreDescriptor.newBuilder()
                .setId("store2")
                .build()
        );
        storeDescSubject.onNext(storeDescriptors);
        // run and failed once, will try after 1000ms interval
        Thread.sleep(2500);
        // run again and succeed, and will not run duplicate command
        verify(storeClient, times(2)).changeReplicaConfig(anyString(), any());
        Thread.sleep(1200);
        verify(storeClient, times(2)).changeReplicaConfig(anyString(), any());
    }

}
