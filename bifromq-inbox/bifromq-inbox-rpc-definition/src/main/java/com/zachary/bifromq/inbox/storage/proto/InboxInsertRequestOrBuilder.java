// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inboxservice/InboxCoProc.proto

package com.zachary.bifromq.inbox.storage.proto;

public interface InboxInsertRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:inboxservice.InboxInsertRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .inboxservice.MessagePack subMsgPack = 1;</code>
   */
  java.util.List<com.zachary.bifromq.inbox.storage.proto.MessagePack> 
      getSubMsgPackList();
  /**
   * <code>repeated .inboxservice.MessagePack subMsgPack = 1;</code>
   */
  com.zachary.bifromq.inbox.storage.proto.MessagePack getSubMsgPack(int index);
  /**
   * <code>repeated .inboxservice.MessagePack subMsgPack = 1;</code>
   */
  int getSubMsgPackCount();
  /**
   * <code>repeated .inboxservice.MessagePack subMsgPack = 1;</code>
   */
  java.util.List<? extends com.zachary.bifromq.inbox.storage.proto.MessagePackOrBuilder> 
      getSubMsgPackOrBuilderList();
  /**
   * <code>repeated .inboxservice.MessagePack subMsgPack = 1;</code>
   */
  com.zachary.bifromq.inbox.storage.proto.MessagePackOrBuilder getSubMsgPackOrBuilder(
      int index);
}
