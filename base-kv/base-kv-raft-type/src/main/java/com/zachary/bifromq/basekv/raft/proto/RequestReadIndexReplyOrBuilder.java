// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/raft/RaftMessage.proto

package com.zachary.bifromq.basekv.raft.proto;

public interface RequestReadIndexReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basekv.raft.RequestReadIndexReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint32 id = 1;</code>
   * @return The id.
   */
  int getId();

  /**
   * <code>uint64 readIndex = 2;</code>
   * @return The readIndex.
   */
  long getReadIndex();
}
