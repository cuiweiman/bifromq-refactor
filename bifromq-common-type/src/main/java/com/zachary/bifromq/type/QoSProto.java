// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: commontype/QoS.proto

package com.zachary.bifromq.type;

public final class QoSProto {
  private QoSProto() {}
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
      "\n\024commontype/QoS.proto\022\ncommontype*<\n\003Qo" +
      "S\022\020\n\014AT_MOST_ONCE\020\000\022\021\n\rAT_LEAST_ONCE\020\001\022\020" +
      "\n\014EXACTLY_ONCE\020\002B(\n\030com.zachary.bifromq." +
      "typeB\010QoSProtoH\001P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
