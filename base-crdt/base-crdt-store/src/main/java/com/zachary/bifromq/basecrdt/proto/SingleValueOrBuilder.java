// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecrdt/Dot.proto

package com.zachary.bifromq.basecrdt.proto;

public interface SingleValueOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basecrdt.SingleValue)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bytes replicaId = 1;</code>
   * @return The replicaId.
   */
  com.google.protobuf.ByteString getReplicaId();

  /**
   * <code>uint64 ver = 2;</code>
   * @return The ver.
   */
  long getVer();

  /**
   * <code>bytes value = 3;</code>
   * @return The value.
   */
  com.google.protobuf.ByteString getValue();
}