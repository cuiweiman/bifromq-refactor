// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/Type.proto

package com.zachary.bifromq.basekv.proto;

public final class Type {
  private Type() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_KVRangeId_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_KVRangeId_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_Range_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_Range_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_KVPair_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_KVPair_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_NullableValue_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_NullableValue_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021basekv/Type.proto\022\006basekv\"&\n\tKVRangeId" +
      "\022\r\n\005epoch\030\001 \001(\004\022\n\n\002id\030\002 \001(\004\"K\n\005Range\022\025\n\010" +
      "startKey\030\001 \001(\014H\000\210\001\001\022\023\n\006endKey\030\002 \001(\014H\001\210\001\001" +
      "B\013\n\t_startKeyB\t\n\007_endKey\"$\n\006KVPair\022\013\n\003ke" +
      "y\030\001 \001(\014\022\r\n\005value\030\002 \001(\014\"-\n\rNullableValue\022" +
      "\022\n\005value\030\001 \001(\014H\000\210\001\001B\010\n\006_valueB&\n com.zac" +
      "hary.bifromq.basekv.protoH\001P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_basekv_KVRangeId_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_basekv_KVRangeId_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_KVRangeId_descriptor,
        new java.lang.String[] { "Epoch", "Id", });
    internal_static_basekv_Range_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_basekv_Range_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_Range_descriptor,
        new java.lang.String[] { "StartKey", "EndKey", "StartKey", "EndKey", });
    internal_static_basekv_KVPair_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_basekv_KVPair_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_KVPair_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_basekv_NullableValue_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_basekv_NullableValue_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_NullableValue_descriptor,
        new java.lang.String[] { "Value", "Value", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}