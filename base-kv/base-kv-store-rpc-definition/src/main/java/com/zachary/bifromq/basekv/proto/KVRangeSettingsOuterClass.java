// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/KVRangeSettings.proto

package com.zachary.bifromq.basekv.proto;

public final class KVRangeSettingsOuterClass {
  private KVRangeSettingsOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_KVRangeSettings_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_KVRangeSettings_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_KVRangeSettings_RepStatusEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_KVRangeSettings_RepStatusEntry_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\034basekv/KVRangeSettings.proto\022\006basekv\032\021" +
      "basekv/Type.proto\032\037basekv/raft/ClusterCo" +
      "nfig.proto\032\027basekv/raft/Enums.proto\"\244\002\n\017" +
      "KVRangeSettings\022\013\n\003ver\030\001 \001(\004\022\035\n\002id\030\002 \001(\013" +
      "2\021.basekv.KVRangeId\022\034\n\005range\030\003 \001(\0132\r.bas" +
      "ekv.Range\022\016\n\006leader\030\004 \001(\t\022*\n\006config\030\005 \001(" +
      "\0132\032.basekv.raft.ClusterConfig\0229\n\trepStat" +
      "us\030\006 \003(\0132&.basekv.KVRangeSettings.RepSta" +
      "tusEntry\032P\n\016RepStatusEntry\022\013\n\003key\030\001 \001(\t\022" +
      "-\n\005value\030\002 \001(\0162\036.basekv.raft.RaftNodeSyn" +
      "cState:\0028\001B&\n com.zachary.bifromq.basekv" +
      ".protoH\001P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.zachary.bifromq.basekv.proto.Type.getDescriptor(),
          com.zachary.bifromq.basekv.raft.proto.ClusterConfigOuterClass.getDescriptor(),
          com.zachary.bifromq.basekv.raft.proto.Enums.getDescriptor(),
        });
    internal_static_basekv_KVRangeSettings_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_basekv_KVRangeSettings_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_KVRangeSettings_descriptor,
        new java.lang.String[] { "Ver", "Id", "Range", "Leader", "Config", "RepStatus", });
    internal_static_basekv_KVRangeSettings_RepStatusEntry_descriptor =
      internal_static_basekv_KVRangeSettings_descriptor.getNestedTypes().get(0);
    internal_static_basekv_KVRangeSettings_RepStatusEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_KVRangeSettings_RepStatusEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    com.zachary.bifromq.basekv.proto.Type.getDescriptor();
    com.zachary.bifromq.basekv.raft.proto.ClusterConfigOuterClass.getDescriptor();
    com.zachary.bifromq.basekv.raft.proto.Enums.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
