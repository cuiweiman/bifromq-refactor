// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecrdt/Replica.proto

package com.zachary.bifromq.basecrdt.proto;

public interface ReplicaOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basecrdt.Replica)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   *副本 uri
   * </pre>
   *
   * <code>string uri = 1;</code>
   * @return The uri.
   */
  java.lang.String getUri();
  /**
   * <pre>
   *副本 uri
   * </pre>
   *
   * <code>string uri = 1;</code>
   * @return The bytes for uri.
   */
  com.google.protobuf.ByteString
      getUriBytes();

  /**
   * <pre>
   * 副本 id
   * </pre>
   *
   * <code>bytes id = 2;</code>
   * @return The id.
   */
  com.google.protobuf.ByteString getId();
}
