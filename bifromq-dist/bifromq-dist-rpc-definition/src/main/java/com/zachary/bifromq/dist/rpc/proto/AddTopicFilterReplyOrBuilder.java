// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface AddTopicFilterReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.AddTopicFilterReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.AddTopicFilterReply.Results&gt; result = 2;</code>
   */
  int getResultCount();
  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.AddTopicFilterReply.Results&gt; result = 2;</code>
   */
  boolean containsResult(
      java.lang.String key);
  /**
   * Use {@link #getResultMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.zachary.bifromq.dist.rpc.proto.AddTopicFilterReply.Results>
  getResult();
  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.AddTopicFilterReply.Results&gt; result = 2;</code>
   */
  java.util.Map<java.lang.String, com.zachary.bifromq.dist.rpc.proto.AddTopicFilterReply.Results>
  getResultMap();
  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.AddTopicFilterReply.Results&gt; result = 2;</code>
   */
  /* nullable */
com.zachary.bifromq.dist.rpc.proto.AddTopicFilterReply.Results getResultOrDefault(
      java.lang.String key,
      /* nullable */
com.zachary.bifromq.dist.rpc.proto.AddTopicFilterReply.Results defaultValue);
  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.AddTopicFilterReply.Results&gt; result = 2;</code>
   */
  com.zachary.bifromq.dist.rpc.proto.AddTopicFilterReply.Results getResultOrThrow(
      java.lang.String key);
}
