// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: retainservice/RetainService.proto

package com.zachary.bifromq.retain.rpc.proto;

public interface MatchRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:retainservice.MatchRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint64 reqId = 1;</code>
   * @return The reqId.
   */
  long getReqId();

  /**
   * <code>string topicFilter = 2;</code>
   * @return The topicFilter.
   */
  java.lang.String getTopicFilter();
  /**
   * <code>string topicFilter = 2;</code>
   * @return The bytes for topicFilter.
   */
  com.google.protobuf.ByteString
      getTopicFilterBytes();

  /**
   * <code>uint32 limit = 3;</code>
   * @return The limit.
   */
  int getLimit();
}
