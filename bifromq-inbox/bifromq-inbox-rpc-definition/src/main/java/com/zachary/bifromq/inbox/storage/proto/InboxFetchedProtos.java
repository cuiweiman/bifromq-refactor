// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inboxservice/InboxFetched.proto

package com.zachary.bifromq.inbox.storage.proto;

public final class InboxFetchedProtos {
  private InboxFetchedProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_inboxservice_Fetched_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_inboxservice_Fetched_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\037inboxservice/InboxFetched.proto\022\014inbox" +
      "service\032 inboxservice/InboxMessages.prot" +
      "o\"\234\002\n\007Fetched\022,\n\006result\030\001 \001(\0162\034.inboxser" +
      "vice.Fetched.Result\022\017\n\007qos0Seq\030\002 \003(\004\022+\n\007" +
      "qos0Msg\030\003 \003(\0132\032.inboxservice.InboxMessag" +
      "e\022\017\n\007qos1Seq\030\004 \003(\004\022+\n\007qos1Msg\030\005 \003(\0132\032.in" +
      "boxservice.InboxMessage\022\017\n\007qos2Seq\030\006 \003(\004" +
      "\022+\n\007qos2Msg\030\007 \003(\0132\032.inboxservice.InboxMe" +
      "ssage\")\n\006Result\022\006\n\002OK\020\000\022\014\n\010NO_INBOX\020\001\022\t\n" +
      "\005ERROR\020\002B?\n\'com.zachary.bifromq.inbox.st" +
      "orage.protoB\022InboxFetchedProtosP\001b\006proto" +
      "3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.zachary.bifromq.inbox.storage.proto.InboxMessagesProtos.getDescriptor(),
        });
    internal_static_inboxservice_Fetched_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_inboxservice_Fetched_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_inboxservice_Fetched_descriptor,
        new java.lang.String[] { "Result", "Qos0Seq", "Qos0Msg", "Qos1Seq", "Qos1Msg", "Qos2Seq", "Qos2Msg", });
    com.zachary.bifromq.inbox.storage.proto.InboxMessagesProtos.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
