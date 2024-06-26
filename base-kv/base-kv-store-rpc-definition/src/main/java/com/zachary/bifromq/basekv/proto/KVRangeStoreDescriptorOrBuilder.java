// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/Descriptor.proto

package com.zachary.bifromq.basekv.proto;

public interface KVRangeStoreDescriptorOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basekv.KVRangeStoreDescriptor)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string id = 1;</code>
   * @return The id.
   */
  java.lang.String getId();
  /**
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <code>map&lt;string, double&gt; statistics = 2;</code>
   */
  int getStatisticsCount();
  /**
   * <code>map&lt;string, double&gt; statistics = 2;</code>
   */
  boolean containsStatistics(
      java.lang.String key);
  /**
   * Use {@link #getStatisticsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.Double>
  getStatistics();
  /**
   * <code>map&lt;string, double&gt; statistics = 2;</code>
   */
  java.util.Map<java.lang.String, java.lang.Double>
  getStatisticsMap();
  /**
   * <code>map&lt;string, double&gt; statistics = 2;</code>
   */
  double getStatisticsOrDefault(
      java.lang.String key,
      double defaultValue);
  /**
   * <code>map&lt;string, double&gt; statistics = 2;</code>
   */
  double getStatisticsOrThrow(
      java.lang.String key);

  /**
   * <code>repeated .basekv.KVRangeDescriptor ranges = 3;</code>
   */
  java.util.List<com.zachary.bifromq.basekv.proto.KVRangeDescriptor> 
      getRangesList();
  /**
   * <code>repeated .basekv.KVRangeDescriptor ranges = 3;</code>
   */
  com.zachary.bifromq.basekv.proto.KVRangeDescriptor getRanges(int index);
  /**
   * <code>repeated .basekv.KVRangeDescriptor ranges = 3;</code>
   */
  int getRangesCount();
  /**
   * <code>repeated .basekv.KVRangeDescriptor ranges = 3;</code>
   */
  java.util.List<? extends com.zachary.bifromq.basekv.proto.KVRangeDescriptorOrBuilder> 
      getRangesOrBuilderList();
  /**
   * <code>repeated .basekv.KVRangeDescriptor ranges = 3;</code>
   */
  com.zachary.bifromq.basekv.proto.KVRangeDescriptorOrBuilder getRangesOrBuilder(
      int index);

  /**
   * <code>uint64 hlc = 4;</code>
   * @return The hlc.
   */
  long getHlc();
}
