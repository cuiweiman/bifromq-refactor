package com.zachary.bifromq.inbox.store;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.store.api.IKVIterator;
import com.zachary.bifromq.basekv.store.api.IKVRangeReader;
import com.zachary.bifromq.basekv.store.api.IKVReader;
import com.zachary.bifromq.basekv.store.api.IKVWriter;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.zachary.bifromq.inbox.storage.proto.CreateParams;
import com.zachary.bifromq.inbox.storage.proto.CreateRequest;
import com.zachary.bifromq.inbox.storage.proto.InboxMetadata;
import com.zachary.bifromq.inbox.storage.proto.InboxServiceRWCoProcInput;
import com.zachary.bifromq.inbox.storage.proto.UpdateRequest;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.google.protobuf.ByteString;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.zachary.bifromq.inbox.util.KeyUtil.scopedInboxId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class InboxCreateTest {
    private KVRangeId id;
    @Mock
    private IKVReader reader;
    @Mock
    private IKVIterator kvIterator;
    @Mock
    private IKVWriter writer;
    private Supplier<IKVRangeReader> rangeReaderProvider = () -> null;
    private IEventCollector eventCollector = event -> {
    };
    private String tenantId = "tenantA";
    private String inboxId = "inboxId";
    private String scopedInboxIdUtf8 = scopedInboxId(tenantId, inboxId).toStringUtf8();
    private ClientInfo clientInfo = ClientInfo.newBuilder()
        .setTenantId(tenantId)
        .putMetadata("agent", "mqtt")
        .putMetadata("protocol", "3.1.1")
        .putMetadata("userId", "testUser")
        .putMetadata("clientId", "testClientId")
        .putMetadata("ip", "127.0.0.1")
        .putMetadata("port", "8888")
        .build();
    private Clock clock = Clock.systemUTC();
    private AutoCloseable closeable;

    @BeforeMethod
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        when(reader.iterator()).thenReturn(kvIterator);
        id = KVRangeIdUtil.generate();
    }

    @AfterMethod
    public void teardown() throws Exception {
        closeable.close();
    }

    @Test
    public void testCreateNewInbox() {
        CreateRequest createRequest = CreateRequest.newBuilder()
            .putInboxes(scopedInboxIdUtf8, CreateParams.newBuilder()
                .setClient(clientInfo)
                .build())
            .build();
        UpdateRequest updateRequest = UpdateRequest.newBuilder()
            .setReqId(System.nanoTime())
            .setCreateInbox(createRequest)
            .build();
        InboxServiceRWCoProcInput coProcInput = InboxServiceRWCoProcInput.newBuilder()
            .setRequest(updateRequest)
            .build();

        when(reader.get(any())).thenReturn(Optional.empty());
        doNothing().when(writer).put(any(), any());

        InboxStoreCoProc coProc = new InboxStoreCoProc(id, rangeReaderProvider, eventCollector,
            clock, Duration.ofMinutes(30));
        coProc.mutate(coProcInput.toByteString(), reader, writer);
        ArgumentCaptor<ByteString> argumentCaptor = ArgumentCaptor.forClass(ByteString.class);
        verify(writer).put(argumentCaptor.capture(), argumentCaptor.capture());
        List<ByteString> args = argumentCaptor.getAllValues();

        try {
            assertEquals(args.size(), 2);
            assertEquals(ByteString.copyFromUtf8(scopedInboxIdUtf8), args.get(0));
            InboxMetadata inboxMetadata = InboxMetadata.parseFrom(args.get(1));
            assertEquals(0, inboxMetadata.getQos0NextSeq());
            assertEquals(0, inboxMetadata.getQos1NextSeq());
            assertEquals(0, inboxMetadata.getQos2NextSeq());
            assertEquals(clientInfo, inboxMetadata.getClient());
        } catch (Exception exception) {
            fail();
        }
    }

    @Test
    public void testCreateExpiredInbox() {
        CreateRequest createRequest = CreateRequest.newBuilder()
            .putInboxes(scopedInboxIdUtf8, CreateParams.newBuilder()
                .setClient(clientInfo)
                .build())
            .build();
        UpdateRequest updateRequest = UpdateRequest.newBuilder()
            .setReqId(System.nanoTime())
            .setCreateInbox(createRequest)
            .build();
        InboxServiceRWCoProcInput coProcInput = InboxServiceRWCoProcInput.newBuilder()
            .setRequest(updateRequest)
            .build();

        when(reader.get(any())).thenReturn(Optional.of(InboxMetadata.newBuilder()
            .setLastFetchTime(clock.millis() - 30 * 1000)
            .setQos0NextSeq(1)
            .setQos1NextSeq(1)
            .setQos2NextSeq(1)
            .setExpireSeconds(1)
            .build().toByteString()));
        doNothing().when(kvIterator).seek(any());
        when(kvIterator.isValid()).thenReturn(false);
        doNothing().when(writer).put(any(), any());

        InboxStoreCoProc coProc = new InboxStoreCoProc(id, rangeReaderProvider, eventCollector,
            Clock.systemUTC(), Duration.ofMinutes(30));
        coProc.mutate(coProcInput.toByteString(), reader, writer);
        ArgumentCaptor<ByteString> argumentCaptor = ArgumentCaptor.forClass(ByteString.class);
        verify(writer).put(argumentCaptor.capture(), argumentCaptor.capture());
        List<ByteString> args = argumentCaptor.getAllValues();

        try {
            assertEquals(args.size(), 2);
            assertEquals(ByteString.copyFromUtf8(scopedInboxIdUtf8), args.get(0));
            InboxMetadata inboxMetadata = InboxMetadata.parseFrom(args.get(1));
            assertEquals(0, inboxMetadata.getQos0NextSeq());
            assertEquals(0, inboxMetadata.getQos1NextSeq());
            assertEquals(0, inboxMetadata.getQos2NextSeq());
            assertEquals(clientInfo, inboxMetadata.getClient());
        } catch (Exception exception) {
            fail();
        }
    }
}
