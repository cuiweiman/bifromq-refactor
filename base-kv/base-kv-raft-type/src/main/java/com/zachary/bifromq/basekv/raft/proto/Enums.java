// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/raft/Enums.proto

package com.zachary.bifromq.basekv.raft.proto;

public final class Enums {
  private Enums() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\027basekv/raft/Enums.proto\022\013basekv.raft*9" +
      "\n\016RaftNodeStatus\022\014\n\010Follower\020\000\022\n\n\006Leader" +
      "\020\001\022\r\n\tCandidate\020\002*F\n\021RaftNodeSyncState\022\013" +
      "\n\007Probing\020\000\022\017\n\013Replicating\020\001\022\023\n\017Snapshot" +
      "Syncing\020\002B+\n%com.zachary.bifromq.basekv." +
      "raft.protoH\001P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
  }

  // @@protoc_insertion_point(outer_class_scope)
}