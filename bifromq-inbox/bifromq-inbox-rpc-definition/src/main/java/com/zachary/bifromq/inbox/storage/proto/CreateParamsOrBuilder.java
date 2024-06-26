// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inboxservice/InboxCoProc.proto

package com.zachary.bifromq.inbox.storage.proto;

public interface CreateParamsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:inboxservice.CreateParams)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint64 expireSeconds = 1;</code>
   * @return The expireSeconds.
   */
  long getExpireSeconds();

  /**
   * <code>uint32 limit = 2;</code>
   * @return The limit.
   */
  int getLimit();

  /**
   * <code>bool dropOldest = 3;</code>
   * @return The dropOldest.
   */
  boolean getDropOldest();

  /**
   * <pre>
   * the owner client
   * </pre>
   *
   * <code>.commontype.ClientInfo client = 4;</code>
   * @return Whether the client field is set.
   */
  boolean hasClient();
  /**
   * <pre>
   * the owner client
   * </pre>
   *
   * <code>.commontype.ClientInfo client = 4;</code>
   * @return The client.
   */
  com.zachary.bifromq.type.ClientInfo getClient();
  /**
   * <pre>
   * the owner client
   * </pre>
   *
   * <code>.commontype.ClientInfo client = 4;</code>
   */
  com.zachary.bifromq.type.ClientInfoOrBuilder getClientOrBuilder();
}
