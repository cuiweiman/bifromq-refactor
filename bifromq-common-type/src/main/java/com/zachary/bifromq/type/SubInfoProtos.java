// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: commontype/SubInfo.proto

package com.zachary.bifromq.type;

public final class SubInfoProtos {
  private SubInfoProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_commontype_SubInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_commontype_SubInfo_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\030commontype/SubInfo.proto\022\ncommontype\032\024" +
      "commontype/QoS.proto\"b\n\007SubInfo\022\020\n\010tenan" +
      "tId\030\001 \001(\t\022\017\n\007inboxId\030\002 \001(\t\022\037\n\006subQoS\030\003 \001" +
      "(\0162\017.commontype.QoS\022\023\n\013topicFilter\030\004 \001(\t" +
      "B+\n\030com.zachary.bifromq.typeB\rSubInfoPro" +
      "tosP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.zachary.bifromq.type.QoSProto.getDescriptor(),
        });
    internal_static_commontype_SubInfo_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_commontype_SubInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_commontype_SubInfo_descriptor,
        new java.lang.String[] { "TenantId", "InboxId", "SubQoS", "TopicFilter", });
    com.zachary.bifromq.type.QoSProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
