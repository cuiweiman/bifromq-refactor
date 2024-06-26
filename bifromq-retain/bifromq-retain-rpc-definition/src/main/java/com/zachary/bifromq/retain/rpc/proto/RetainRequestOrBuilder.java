// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: retainservice/RetainService.proto

package com.zachary.bifromq.retain.rpc.proto;

public interface RetainRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:retainservice.RetainRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * message id
   * </pre>
   *
   * <code>uint64 reqId = 1;</code>
   * @return The reqId.
   */
  long getReqId();

  /**
   * <code>.commontype.QoS qos = 2;</code>
   * @return The enum numeric value on the wire for qos.
   */
  int getQosValue();
  /**
   * <code>.commontype.QoS qos = 2;</code>
   * @return The qos.
   */
  com.zachary.bifromq.type.QoS getQos();

  /**
   * <code>string topic = 3;</code>
   * @return The topic.
   */
  java.lang.String getTopic();
  /**
   * <code>string topic = 3;</code>
   * @return The bytes for topic.
   */
  com.google.protobuf.ByteString
      getTopicBytes();

  /**
   * <code>uint64 timestamp = 4;</code>
   * @return The timestamp.
   */
  long getTimestamp();

  /**
   * <code>uint64 expireTimestamp = 5;</code>
   * @return The expireTimestamp.
   */
  long getExpireTimestamp();

  /**
   * <code>bytes payload = 6;</code>
   * @return The payload.
   */
  com.google.protobuf.ByteString getPayload();
}
