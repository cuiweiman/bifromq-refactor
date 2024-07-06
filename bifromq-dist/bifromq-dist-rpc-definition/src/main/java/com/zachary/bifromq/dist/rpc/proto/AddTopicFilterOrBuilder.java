// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface AddTopicFilterOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.AddTopicFilter)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.InboxSubInfo&gt; topicFilter = 2;</code>
   */
  int getTopicFilterCount();
  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.InboxSubInfo&gt; topicFilter = 2;</code>
   */
  boolean containsTopicFilter(
      java.lang.String key);
  /**
   * Use {@link #getTopicFilterMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.zachary.bifromq.dist.rpc.proto.InboxSubInfo>
  getTopicFilter();
  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.InboxSubInfo&gt; topicFilter = 2;</code>
   */
  java.util.Map<java.lang.String, com.zachary.bifromq.dist.rpc.proto.InboxSubInfo>
  getTopicFilterMap();
  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.InboxSubInfo&gt; topicFilter = 2;</code>
   */
  /* nullable */
com.zachary.bifromq.dist.rpc.proto.InboxSubInfo getTopicFilterOrDefault(
      java.lang.String key,
      /* nullable */
com.zachary.bifromq.dist.rpc.proto.InboxSubInfo defaultValue);
  /**
   * <pre>
   * key: subInfoKeyUtf8
   * </pre>
   *
   * <code>map&lt;string, .distservice.InboxSubInfo&gt; topicFilter = 2;</code>
   */
  com.zachary.bifromq.dist.rpc.proto.InboxSubInfo getTopicFilterOrThrow(
      java.lang.String key);
}