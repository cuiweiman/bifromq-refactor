// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/transport/Packet.proto

package com.zachary.bifromq.basecluster.transport.proto;

public final class PacketProtos {
  private PacketProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basecluster_transport_Packet_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basecluster_transport_Packet_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\"basecluster/transport/Packet.proto\022\025ba" +
      "secluster.transport\";\n\006Packet\022\020\n\010message" +
      "s\030\001 \003(\014\022\022\n\nclusterEnv\030\002 \001(\t\022\013\n\003hlc\030\003 \001(\004" +
      "BC\n/com.zachary.bifromq.basecluster.tran" +
      "sport.protoB\014PacketProtosH\001P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_basecluster_transport_Packet_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_basecluster_transport_Packet_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basecluster_transport_Packet_descriptor,
        new java.lang.String[] { "Messages", "ClusterEnv", "Hlc", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}