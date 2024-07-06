// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/raft/Voting.proto

package com.zachary.bifromq.basekv.raft.proto;

public interface VotingOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basekv.raft.Voting)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * voting term
   * </pre>
   *
   * <code>uint64 term = 1;</code>
   * @return The term.
   */
  long getTerm();

  /**
   * <pre>
   * the voted peer
   * </pre>
   *
   * <code>string for = 2;</code>
   * @return The for.
   */
  java.lang.String getFor();
  /**
   * <pre>
   * the voted peer
   * </pre>
   *
   * <code>string for = 2;</code>
   * @return The bytes for for.
   */
  com.google.protobuf.ByteString
      getForBytes();
}