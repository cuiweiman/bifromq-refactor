// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/fd/PingReq.proto

package com.zachary.bifromq.basecluster.fd.proto;

public final class PingReqProtos {
  private PingReqProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_basecluster_fd_PingReq_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_basecluster_fd_PingReq_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\034basecluster/fd/PingReq.proto\022\016baseclus" +
      "ter.fd\"z\n\007PingReq\022\r\n\005seqNo\030\001 \001(\r\022\n\n\002id\030\002" +
      " \001(\014\022\014\n\004addr\030\003 \001(\t\022\014\n\004port\030\004 \001(\r\022\020\n\010ping" +
      "erId\030\005 \001(\014\022\022\n\npingerAddr\030\006 \001(\t\022\022\n\npinger" +
      "Port\030\007 \001(\rB=\n(com.zachary.bifromq.basecl" +
      "uster.fd.protoB\rPingReqProtosH\001P\001b\006proto" +
      "3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_basecluster_fd_PingReq_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_basecluster_fd_PingReq_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_basecluster_fd_PingReq_descriptor,
        new java.lang.String[] { "SeqNo", "Id", "Addr", "Port", "PingerId", "PingerAddr", "PingerPort", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
