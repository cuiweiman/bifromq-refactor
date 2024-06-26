// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface InboxSubInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.InboxSubInfo)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * key: Original TopicFilter
   * </pre>
   *
   * <code>map&lt;string, .commontype.QoS&gt; topicFilters = 1;</code>
   */
  int getTopicFiltersCount();
  /**
   * <pre>
   * key: Original TopicFilter
   * </pre>
   *
   * <code>map&lt;string, .commontype.QoS&gt; topicFilters = 1;</code>
   */
  boolean containsTopicFilters(
      java.lang.String key);
  /**
   * Use {@link #getTopicFiltersMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.zachary.bifromq.type.QoS>
  getTopicFilters();
  /**
   * <pre>
   * key: Original TopicFilter
   * </pre>
   *
   * <code>map&lt;string, .commontype.QoS&gt; topicFilters = 1;</code>
   */
  java.util.Map<java.lang.String, com.zachary.bifromq.type.QoS>
  getTopicFiltersMap();
  /**
   * <pre>
   * key: Original TopicFilter
   * </pre>
   *
   * <code>map&lt;string, .commontype.QoS&gt; topicFilters = 1;</code>
   */
  /* nullable */
com.zachary.bifromq.type.QoS getTopicFiltersOrDefault(
      java.lang.String key,
      /* nullable */
com.zachary.bifromq.type.QoS         defaultValue);
  /**
   * <pre>
   * key: Original TopicFilter
   * </pre>
   *
   * <code>map&lt;string, .commontype.QoS&gt; topicFilters = 1;</code>
   */
  com.zachary.bifromq.type.QoS getTopicFiltersOrThrow(
      java.lang.String key);
  /**
   * Use {@link #getTopicFiltersValueMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.Integer>
  getTopicFiltersValue();
  /**
   * <pre>
   * key: Original TopicFilter
   * </pre>
   *
   * <code>map&lt;string, .commontype.QoS&gt; topicFilters = 1;</code>
   */
  java.util.Map<java.lang.String, java.lang.Integer>
  getTopicFiltersValueMap();
  /**
   * <pre>
   * key: Original TopicFilter
   * </pre>
   *
   * <code>map&lt;string, .commontype.QoS&gt; topicFilters = 1;</code>
   */
  int getTopicFiltersValueOrDefault(
      java.lang.String key,
      int defaultValue);
  /**
   * <pre>
   * key: Original TopicFilter
   * </pre>
   *
   * <code>map&lt;string, .commontype.QoS&gt; topicFilters = 1;</code>
   */
  int getTopicFiltersValueOrThrow(
      java.lang.String key);
}
