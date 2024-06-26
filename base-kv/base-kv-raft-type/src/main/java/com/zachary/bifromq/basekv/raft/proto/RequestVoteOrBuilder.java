// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/raft/RaftMessage.proto

package com.zachary.bifromq.basekv.raft.proto;

public interface RequestVoteOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basekv.raft.RequestVote)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string candidateId = 1;</code>
   * @return The candidateId.
   */
  java.lang.String getCandidateId();
  /**
   * <code>string candidateId = 1;</code>
   * @return The bytes for candidateId.
   */
  com.google.protobuf.ByteString
      getCandidateIdBytes();

  /**
   * <code>uint64 lastLogTerm = 2;</code>
   * @return The lastLogTerm.
   */
  long getLastLogTerm();

  /**
   * <code>uint64 lastLogIndex = 3;</code>
   * @return The lastLogIndex.
   */
  long getLastLogIndex();

  /**
   * <code>bool leaderTransfer = 4;</code>
   * @return The leaderTransfer.
   */
  boolean getLeaderTransfer();
}
