// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: commontype/TopicMessage.proto

package com.zachary.bifromq.type;

public interface MessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:commontype.Message)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint64 messageId = 1;</code>
   * @return The messageId.
   */
  long getMessageId();

  /**
   * <code>.commontype.QoS pubQoS = 2;</code>
   * @return The enum numeric value on the wire for pubQoS.
   */
  int getPubQoSValue();
  /**
   * <code>.commontype.QoS pubQoS = 2;</code>
   * @return The pubQoS.
   */
  com.zachary.bifromq.type.QoS getPubQoS();

  /**
   * <pre>
   * user payload
   * </pre>
   *
   * <code>bytes payload = 3;</code>
   * @return The payload.
   */
  com.google.protobuf.ByteString getPayload();

  /**
   * <pre>
   * UTC ts
   * </pre>
   *
   * <code>uint64 timestamp = 4;</code>
   * @return The timestamp.
   */
  long getTimestamp();

  /**
   * <pre>
   * UTC ts
   * </pre>
   *
   * <code>uint64 expireTimestamp = 5;</code>
   * @return The expireTimestamp.
   */
  long getExpireTimestamp();
}
