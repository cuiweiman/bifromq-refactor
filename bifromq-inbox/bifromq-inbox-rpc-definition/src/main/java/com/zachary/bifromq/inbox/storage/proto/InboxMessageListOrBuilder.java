// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inboxservice/InboxMessages.proto

package com.zachary.bifromq.inbox.storage.proto;

public interface InboxMessageListOrBuilder extends
    // @@protoc_insertion_point(interface_extends:inboxservice.InboxMessageList)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .inboxservice.InboxMessage message = 1;</code>
   */
  java.util.List<com.zachary.bifromq.inbox.storage.proto.InboxMessage> 
      getMessageList();
  /**
   * <code>repeated .inboxservice.InboxMessage message = 1;</code>
   */
  com.zachary.bifromq.inbox.storage.proto.InboxMessage getMessage(int index);
  /**
   * <code>repeated .inboxservice.InboxMessage message = 1;</code>
   */
  int getMessageCount();
  /**
   * <code>repeated .inboxservice.InboxMessage message = 1;</code>
   */
  java.util.List<? extends com.zachary.bifromq.inbox.storage.proto.InboxMessageOrBuilder> 
      getMessageOrBuilderList();
  /**
   * <code>repeated .inboxservice.InboxMessage message = 1;</code>
   */
  com.zachary.bifromq.inbox.storage.proto.InboxMessageOrBuilder getMessageOrBuilder(
      int index);
}
