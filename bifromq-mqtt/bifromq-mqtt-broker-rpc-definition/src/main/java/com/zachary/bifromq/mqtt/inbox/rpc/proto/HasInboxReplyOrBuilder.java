// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mqttbroker/MessageReceiver.proto

package com.zachary.bifromq.mqtt.inbox.rpc.proto;

public interface HasInboxReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:mqttbroker.HasInboxReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint64 reqId = 1;</code>
   * @return The reqId.
   */
  long getReqId();

  /**
   * <code>bool result = 2;</code>
   * @return The result.
   */
  boolean getResult();
}