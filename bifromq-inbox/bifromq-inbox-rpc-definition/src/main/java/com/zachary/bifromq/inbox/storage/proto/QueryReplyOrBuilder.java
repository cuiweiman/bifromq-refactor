// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inboxservice/InboxCoProc.proto

package com.zachary.bifromq.inbox.storage.proto;

public interface QueryReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:inboxservice.QueryReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint64 reqId = 1;</code>
   * @return The reqId.
   */
  long getReqId();

  /**
   * <code>optional .inboxservice.HasReply has = 2;</code>
   * @return Whether the has field is set.
   */
  boolean hasHas();
  /**
   * <code>optional .inboxservice.HasReply has = 2;</code>
   * @return The has.
   */
  com.zachary.bifromq.inbox.storage.proto.HasReply getHas();
  /**
   * <code>optional .inboxservice.HasReply has = 2;</code>
   */
  com.zachary.bifromq.inbox.storage.proto.HasReplyOrBuilder getHasOrBuilder();

  /**
   * <code>optional .inboxservice.InboxFetchReply fetch = 3;</code>
   * @return Whether the fetch field is set.
   */
  boolean hasFetch();
  /**
   * <code>optional .inboxservice.InboxFetchReply fetch = 3;</code>
   * @return The fetch.
   */
  com.zachary.bifromq.inbox.storage.proto.InboxFetchReply getFetch();
  /**
   * <code>optional .inboxservice.InboxFetchReply fetch = 3;</code>
   */
  com.zachary.bifromq.inbox.storage.proto.InboxFetchReplyOrBuilder getFetchOrBuilder();
}
