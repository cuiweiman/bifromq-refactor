// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface TopicFilterListOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.TopicFilterList)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated string topicFilter = 1;</code>
   * @return A list containing the topicFilter.
   */
  java.util.List<java.lang.String>
      getTopicFilterList();
  /**
   * <code>repeated string topicFilter = 1;</code>
   * @return The count of topicFilter.
   */
  int getTopicFilterCount();
  /**
   * <code>repeated string topicFilter = 1;</code>
   * @param index The index of the element to return.
   * @return The topicFilter at the given index.
   */
  java.lang.String getTopicFilter(int index);
  /**
   * <code>repeated string topicFilter = 1;</code>
   * @param index The index of the value to return.
   * @return The bytes of the topicFilter at the given index.
   */
  com.google.protobuf.ByteString
      getTopicFilterBytes(int index);
}