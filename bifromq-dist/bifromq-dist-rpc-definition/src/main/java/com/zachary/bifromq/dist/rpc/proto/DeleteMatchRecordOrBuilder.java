// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface DeleteMatchRecordOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.DeleteMatchRecord)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * normal matchRecordKey utf8
   * </pre>
   *
   * <code>repeated string matchRecordKey = 1;</code>
   * @return A list containing the matchRecordKey.
   */
  java.util.List<java.lang.String>
      getMatchRecordKeyList();
  /**
   * <pre>
   * normal matchRecordKey utf8
   * </pre>
   *
   * <code>repeated string matchRecordKey = 1;</code>
   * @return The count of matchRecordKey.
   */
  int getMatchRecordKeyCount();
  /**
   * <pre>
   * normal matchRecordKey utf8
   * </pre>
   *
   * <code>repeated string matchRecordKey = 1;</code>
   * @param index The index of the element to return.
   * @return The matchRecordKey at the given index.
   */
  java.lang.String getMatchRecordKey(int index);
  /**
   * <pre>
   * normal matchRecordKey utf8
   * </pre>
   *
   * <code>repeated string matchRecordKey = 1;</code>
   * @param index The index of the value to return.
   * @return The bytes of the matchRecordKey at the given index.
   */
  com.google.protobuf.ByteString
      getMatchRecordKeyBytes(int index);
}
