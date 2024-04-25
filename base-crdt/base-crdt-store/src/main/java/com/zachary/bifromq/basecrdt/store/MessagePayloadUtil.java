
package com.zachary.bifromq.basecrdt.store;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zachary.bifromq.basecrdt.core.util.Log;
import com.zachary.bifromq.basecrdt.store.compressor.Compressor;
import com.zachary.bifromq.basecrdt.store.proto.AckMessage;
import com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessage;
import com.zachary.bifromq.basecrdt.store.proto.DeltaMessage;
import com.zachary.bifromq.basecrdt.store.proto.MessagePayload;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MessagePayloadUtil {

    public static ByteString compressToPayload(Compressor compressor, AckMessage ackMessage) {
        return compressor.compress(MessagePayload.newBuilder().setAck(ackMessage).build().toByteString());
    }

    public static ByteString compressToPayload(Compressor compressor, DeltaMessage deltaMessage) {
        return compressor.compress(MessagePayload.newBuilder().setDelta(deltaMessage).build().toByteString());
    }

    public static MessagePayload decompress(Compressor compressor, CRDTStoreMessage crdtStoreMessage) {
        try {
            return MessagePayload.parseFrom(compressor.decompress(crdtStoreMessage.getPayload()));
        } catch (InvalidProtocolBufferException e) {
            Log.error(log, "Can not decompress message payload from message {}", crdtStoreMessage, e);
            throw new RuntimeException(e);
        }
    }

}
