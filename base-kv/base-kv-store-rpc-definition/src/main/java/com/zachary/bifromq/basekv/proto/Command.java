// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/Command.proto

package com.zachary.bifromq.basekv.proto;

public final class Command {
  private Command() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_ChangeConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_ChangeConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_TransferLeadership_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_TransferLeadership_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_SplitRange_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_SplitRange_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_PrepareMergeWith_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_PrepareMergeWith_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_CancelMerging_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_CancelMerging_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_PrepareMergeTo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_PrepareMergeTo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_Merge_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_Merge_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_MergeDone_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_MergeDone_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_Delete_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_Delete_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_Put_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_Put_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basekv_KVRangeCommand_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basekv_KVRangeCommand_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\024basekv/Command.proto\022\006basekv\032\021basekv/T" +
      "ype.proto\032\037basekv/raft/ClusterConfig.pro" +
      "to\"0\n\014ChangeConfig\022\016\n\006voters\030\001 \003(\t\022\020\n\010le" +
      "arners\030\002 \003(\t\"\'\n\022TransferLeadership\022\021\n\tne" +
      "wLeader\030\001 \001(\t\"@\n\nSplitRange\022\020\n\010splitKey\030" +
      "\001 \001(\014\022 \n\005newId\030\002 \001(\0132\021.basekv.KVRangeId\"" +
      "7\n\020PrepareMergeWith\022#\n\010mergeeId\030\001 \001(\0132\021." +
      "basekv.KVRangeId\"\017\n\rCancelMerging\"\222\001\n\016Pr" +
      "epareMergeTo\022#\n\010mergerId\030\001 \001(\0132\021.basekv." +
      "KVRangeId\022\021\n\tmergerVer\030\002 \001(\004\022*\n\006config\030\003" +
      " \001(\0132\032.basekv.raft.ClusterConfig\022\034\n\005rang" +
      "e\030\004 \001(\0132\r.basekv.Range\"n\n\005Merge\022#\n\010merge" +
      "eId\030\001 \001(\0132\021.basekv.KVRangeId\022\021\n\tmergeeVe" +
      "r\030\002 \001(\004\022\034\n\005range\030\003 \001(\0132\r.basekv.Range\022\017\n" +
      "\007storeId\030\004 \001(\t\"\034\n\tMergeDone\022\017\n\007storeId\030\001" +
      " \001(\t\"\025\n\006Delete\022\013\n\003key\030\001 \001(\014\"!\n\003Put\022\013\n\003ke" +
      "y\030\001 \001(\014\022\r\n\005value\030\002 \001(\014\"\200\004\n\016KVRangeComman" +
      "d\022\016\n\006taskId\030\001 \001(\t\022\013\n\003ver\030\002 \001(\004\022,\n\014change" +
      "Config\030\004 \001(\0132\024.basekv.ChangeConfigH\000\0228\n\022" +
      "transferLeadership\030\005 \001(\0132\032.basekv.Transf" +
      "erLeadershipH\000\022(\n\nsplitRange\030\006 \001(\0132\022.bas" +
      "ekv.SplitRangeH\000\0224\n\020prepareMergeWith\030\007 \001" +
      "(\0132\030.basekv.PrepareMergeWithH\000\022.\n\rcancel" +
      "Merging\030\010 \001(\0132\025.basekv.CancelMergingH\000\0220" +
      "\n\016prepareMergeTo\030\t \001(\0132\026.basekv.PrepareM" +
      "ergeToH\000\022\036\n\005merge\030\n \001(\0132\r.basekv.MergeH\000" +
      "\022&\n\tmergeDone\030\013 \001(\0132\021.basekv.MergeDoneH\000" +
      "\022 \n\006delete\030\014 \001(\0132\016.basekv.DeleteH\000\022\032\n\003pu" +
      "t\030\r \001(\0132\013.basekv.PutH\000\022\022\n\010rwCoProc\030\016 \001(\014" +
      "H\000B\r\n\013CommandTypeB&\n com.zachary.bifromq" +
      ".basekv.protoH\001P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.zachary.bifromq.basekv.proto.Type.getDescriptor(),
          com.zachary.bifromq.basekv.raft.proto.ClusterConfigOuterClass.getDescriptor(),
        });
    internal_static_basekv_ChangeConfig_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_basekv_ChangeConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_ChangeConfig_descriptor,
        new java.lang.String[] { "Voters", "Learners", });
    internal_static_basekv_TransferLeadership_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_basekv_TransferLeadership_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_TransferLeadership_descriptor,
        new java.lang.String[] { "NewLeader", });
    internal_static_basekv_SplitRange_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_basekv_SplitRange_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_SplitRange_descriptor,
        new java.lang.String[] { "SplitKey", "NewId", });
    internal_static_basekv_PrepareMergeWith_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_basekv_PrepareMergeWith_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_PrepareMergeWith_descriptor,
        new java.lang.String[] { "MergeeId", });
    internal_static_basekv_CancelMerging_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_basekv_CancelMerging_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_CancelMerging_descriptor,
        new java.lang.String[] { });
    internal_static_basekv_PrepareMergeTo_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_basekv_PrepareMergeTo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_PrepareMergeTo_descriptor,
        new java.lang.String[] { "MergerId", "MergerVer", "Config", "Range", });
    internal_static_basekv_Merge_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_basekv_Merge_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_Merge_descriptor,
        new java.lang.String[] { "MergeeId", "MergeeVer", "Range", "StoreId", });
    internal_static_basekv_MergeDone_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_basekv_MergeDone_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_MergeDone_descriptor,
        new java.lang.String[] { "StoreId", });
    internal_static_basekv_Delete_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_basekv_Delete_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_Delete_descriptor,
        new java.lang.String[] { "Key", });
    internal_static_basekv_Put_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_basekv_Put_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_Put_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_basekv_KVRangeCommand_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_basekv_KVRangeCommand_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basekv_KVRangeCommand_descriptor,
        new java.lang.String[] { "TaskId", "Ver", "ChangeConfig", "TransferLeadership", "SplitRange", "PrepareMergeWith", "CancelMerging", "PrepareMergeTo", "Merge", "MergeDone", "Delete", "Put", "RwCoProc", "CommandType", });
    com.zachary.bifromq.basekv.proto.Type.getDescriptor();
    com.zachary.bifromq.basekv.raft.proto.ClusterConfigOuterClass.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
