// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/transport/Packet.proto

package com.zachary.bifromq.basecluster.transport.proto;

public interface PacketOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basecluster.transport.Packet)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated bytes messages = 1;</code>
   * @return A list containing the messages.
   */
  java.util.List<com.google.protobuf.ByteString> getMessagesList();
  /**
   * <code>repeated bytes messages = 1;</code>
   * @return The count of messages.
   */
  int getMessagesCount();
  /**
   * <code>repeated bytes messages = 1;</code>
   * @param index The index of the element to return.
   * @return The messages at the given index.
   */
  com.google.protobuf.ByteString getMessages(int index);

  /**
   * <code>string clusterEnv = 2;</code>
   * @return The clusterEnv.
   */
  java.lang.String getClusterEnv();
  /**
   * <code>string clusterEnv = 2;</code>
   * @return The bytes for clusterEnv.
   */
  com.google.protobuf.ByteString
      getClusterEnvBytes();

  /**
   * <code>uint64 hlc = 3;</code>
   * @return The hlc.
   */
  long getHlc();
}
