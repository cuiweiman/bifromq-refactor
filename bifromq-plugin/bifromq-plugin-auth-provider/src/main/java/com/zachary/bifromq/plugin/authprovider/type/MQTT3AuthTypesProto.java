// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mqtt3_auth_types.proto

package com.zachary.bifromq.plugin.authprovider.type;

public final class MQTT3AuthTypesProto {
  private MQTT3AuthTypesProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mqtt3authtypes_MQTT3AuthData_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mqtt3authtypes_MQTT3AuthData_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mqtt3authtypes_Ok_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mqtt3authtypes_Ok_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mqtt3authtypes_Reject_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mqtt3authtypes_Reject_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mqtt3authtypes_MQTT3AuthResult_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mqtt3authtypes_MQTT3AuthResult_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\026mqtt3_auth_types.proto\022\016mqtt3authtypes" +
      "\"\344\001\n\rMQTT3AuthData\022\020\n\010isMQIsdp\030\001 \001(\010\022\025\n\010" +
      "username\030\002 \001(\tH\000\210\001\001\022\025\n\010password\030\003 \001(\014H\001\210" +
      "\001\001\022\021\n\004cert\030\004 \001(\014H\002\210\001\001\022\025\n\010clientId\030\005 \001(\tH" +
      "\003\210\001\001\022\022\n\nremoteAddr\030\006 \001(\t\022\022\n\nremotePort\030\007" +
      " \001(\r\022\021\n\tchannelId\030\010 \001(\tB\013\n\t_usernameB\013\n\t" +
      "_passwordB\007\n\005_certB\013\n\t_clientId\"&\n\002Ok\022\020\n" +
      "\010tenantId\030\001 \001(\t\022\016\n\006userId\030\002 \001(\t\"\206\001\n\006Reje" +
      "ct\022)\n\004code\030\001 \001(\0162\033.mqtt3authtypes.Reject" +
      ".Code\022\023\n\006reason\030\002 \001(\tH\000\210\001\001\"1\n\004Code\022\013\n\007Ba" +
      "dPass\020\000\022\021\n\rNotAuthorized\020\001\022\t\n\005Error\020\002B\t\n" +
      "\007_reason\"e\n\017MQTT3AuthResult\022 \n\002ok\030\001 \001(\0132" +
      "\022.mqtt3authtypes.OkH\000\022(\n\006reject\030\002 \001(\0132\026." +
      "mqtt3authtypes.RejectH\000B\006\n\004TypeBG\n,com.z" +
      "achary.bifromq.plugin.authprovider.typeB" +
      "\023MQTT3AuthTypesProtoH\001P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_mqtt3authtypes_MQTT3AuthData_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_mqtt3authtypes_MQTT3AuthData_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mqtt3authtypes_MQTT3AuthData_descriptor,
        new java.lang.String[] { "IsMQIsdp", "Username", "Password", "Cert", "ClientId", "RemoteAddr", "RemotePort", "ChannelId", "Username", "Password", "Cert", "ClientId", });
    internal_static_mqtt3authtypes_Ok_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_mqtt3authtypes_Ok_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mqtt3authtypes_Ok_descriptor,
        new java.lang.String[] { "TenantId", "UserId", });
    internal_static_mqtt3authtypes_Reject_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_mqtt3authtypes_Reject_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mqtt3authtypes_Reject_descriptor,
        new java.lang.String[] { "Code", "Reason", "Reason", });
    internal_static_mqtt3authtypes_MQTT3AuthResult_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_mqtt3authtypes_MQTT3AuthResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mqtt3authtypes_MQTT3AuthResult_descriptor,
        new java.lang.String[] { "Ok", "Reject", "Type", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
