// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/messenger/MessengerMessage.proto

package com.zachary.bifromq.basecluster.messenger.proto;

public interface GossipMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basecluster.messenger.GossipMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string messageId = 1;</code>
   * @return The messageId.
   */
  java.lang.String getMessageId();
  /**
   * <code>string messageId = 1;</code>
   * @return The bytes for messageId.
   */
  com.google.protobuf.ByteString
      getMessageIdBytes();

  /**
   * <code>bytes payload = 2;</code>
   * @return The payload.
   */
  com.google.protobuf.ByteString getPayload();
}