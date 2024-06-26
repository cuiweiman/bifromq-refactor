// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/Type.proto

package com.zachary.bifromq.basekv.proto;

public interface RangeOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basekv.Range)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * the inclusive lower bound of the range if present, or it's a left open range
   * </pre>
   *
   * <code>optional bytes startKey = 1;</code>
   * @return Whether the startKey field is set.
   */
  boolean hasStartKey();
  /**
   * <pre>
   * the inclusive lower bound of the range if present, or it's a left open range
   * </pre>
   *
   * <code>optional bytes startKey = 1;</code>
   * @return The startKey.
   */
  com.google.protobuf.ByteString getStartKey();

  /**
   * <pre>
   * the exclusive upper bound of the range if present, or it's a right open range
   * </pre>
   *
   * <code>optional bytes endKey = 2;</code>
   * @return Whether the endKey field is set.
   */
  boolean hasEndKey();
  /**
   * <pre>
   * the exclusive upper bound of the range if present, or it's a right open range
   * </pre>
   *
   * <code>optional bytes endKey = 2;</code>
   * @return The endKey.
   */
  com.google.protobuf.ByteString getEndKey();
}
