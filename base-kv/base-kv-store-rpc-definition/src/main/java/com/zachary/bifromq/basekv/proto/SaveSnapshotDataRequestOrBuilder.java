// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/StoreMessage.proto

package com.zachary.bifromq.basekv.proto;

public interface SaveSnapshotDataRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basekv.SaveSnapshotDataRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string sessionId = 1;</code>
   * @return The sessionId.
   */
  java.lang.String getSessionId();
  /**
   * <code>string sessionId = 1;</code>
   * @return The bytes for sessionId.
   */
  com.google.protobuf.ByteString
      getSessionIdBytes();

  /**
   * <code>uint32 reqId = 2;</code>
   * @return The reqId.
   */
  int getReqId();

  /**
   * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
   * @return The enum numeric value on the wire for flag.
   */
  int getFlagValue();
  /**
   * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
   * @return The flag.
   */
  com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag getFlag();

  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  java.util.List<com.zachary.bifromq.basekv.proto.KVPair> 
      getKvList();
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  com.zachary.bifromq.basekv.proto.KVPair getKv(int index);
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  int getKvCount();
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  java.util.List<? extends com.zachary.bifromq.basekv.proto.KVPairOrBuilder> 
      getKvOrBuilderList();
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  com.zachary.bifromq.basekv.proto.KVPairOrBuilder getKvOrBuilder(
      int index);
}