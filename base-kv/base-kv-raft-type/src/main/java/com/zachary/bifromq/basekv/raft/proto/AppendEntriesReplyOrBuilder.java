// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/raft/RaftMessage.proto

package com.zachary.bifromq.basekv.raft.proto;

public interface AppendEntriesReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basekv.raft.AppendEntriesReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.basekv.raft.AppendEntriesReply.Accept accept = 1;</code>
   * @return Whether the accept field is set.
   */
  boolean hasAccept();
  /**
   * <code>.basekv.raft.AppendEntriesReply.Accept accept = 1;</code>
   * @return The accept.
   */
  com.zachary.bifromq.basekv.raft.proto.AppendEntriesReply.Accept getAccept();
  /**
   * <code>.basekv.raft.AppendEntriesReply.Accept accept = 1;</code>
   */
  com.zachary.bifromq.basekv.raft.proto.AppendEntriesReply.AcceptOrBuilder getAcceptOrBuilder();

  /**
   * <code>.basekv.raft.AppendEntriesReply.Reject reject = 2;</code>
   * @return Whether the reject field is set.
   */
  boolean hasReject();
  /**
   * <code>.basekv.raft.AppendEntriesReply.Reject reject = 2;</code>
   * @return The reject.
   */
  com.zachary.bifromq.basekv.raft.proto.AppendEntriesReply.Reject getReject();
  /**
   * <code>.basekv.raft.AppendEntriesReply.Reject reject = 2;</code>
   */
  com.zachary.bifromq.basekv.raft.proto.AppendEntriesReply.RejectOrBuilder getRejectOrBuilder();

  /**
   * <pre>
   * read index to confirm
   * </pre>
   *
   * <code>uint64 readIndex = 3;</code>
   * @return The readIndex.
   */
  long getReadIndex();

  public com.zachary.bifromq.basekv.raft.proto.AppendEntriesReply.ResultCase getResultCase();
}
