// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface DeleteMatchRecordReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.DeleteMatchRecordReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>map&lt;string, bool&gt; exist = 1;</code>
   */
  int getExistCount();
  /**
   * <code>map&lt;string, bool&gt; exist = 1;</code>
   */
  boolean containsExist(
      java.lang.String key);
  /**
   * Use {@link #getExistMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.Boolean>
  getExist();
  /**
   * <code>map&lt;string, bool&gt; exist = 1;</code>
   */
  java.util.Map<java.lang.String, java.lang.Boolean>
  getExistMap();
  /**
   * <code>map&lt;string, bool&gt; exist = 1;</code>
   */
  boolean getExistOrDefault(
      java.lang.String key,
      boolean defaultValue);
  /**
   * <code>map&lt;string, bool&gt; exist = 1;</code>
   */
  boolean getExistOrThrow(
      java.lang.String key);
}
