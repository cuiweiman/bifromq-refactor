// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: baserpc/TrafficDirective.proto

package com.zachary.bifromq.baserpc.proto;

public interface LoadAssignmentOrBuilder extends
    // @@protoc_insertion_point(interface_extends:baserpc.LoadAssignment)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>map&lt;string, uint32&gt; weightedGroup = 1;</code>
   */
  int getWeightedGroupCount();
  /**
   * <code>map&lt;string, uint32&gt; weightedGroup = 1;</code>
   */
  boolean containsWeightedGroup(
      java.lang.String key);
  /**
   * Use {@link #getWeightedGroupMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.Integer>
  getWeightedGroup();
  /**
   * <code>map&lt;string, uint32&gt; weightedGroup = 1;</code>
   */
  java.util.Map<java.lang.String, java.lang.Integer>
  getWeightedGroupMap();
  /**
   * <code>map&lt;string, uint32&gt; weightedGroup = 1;</code>
   */
  int getWeightedGroupOrDefault(
      java.lang.String key,
      int defaultValue);
  /**
   * <code>map&lt;string, uint32&gt; weightedGroup = 1;</code>
   */
  int getWeightedGroupOrThrow(
      java.lang.String key);
}