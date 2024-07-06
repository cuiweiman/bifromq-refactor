// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/TopicFanout.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface TopicFanoutOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.TopicFanout)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  int getFanoutCount();
  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  boolean containsFanout(
      java.lang.String key);
  /**
   * Use {@link #getFanoutMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.Integer>
  getFanout();
  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  java.util.Map<java.lang.String, java.lang.Integer>
  getFanoutMap();
  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  int getFanoutOrDefault(
      java.lang.String key,
      int defaultValue);
  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  int getFanoutOrThrow(
      java.lang.String key);
}