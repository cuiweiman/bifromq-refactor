// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: retainservice/RetainCoProc.proto

package com.zachary.bifromq.retain.rpc.proto;

public interface RetainServiceRWCoProcOutputOrBuilder extends
    // @@protoc_insertion_point(interface_extends:retainservice.RetainServiceRWCoProcOutput)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.retainservice.RetainCoProcReply retainReply = 1;</code>
   * @return Whether the retainReply field is set.
   */
  boolean hasRetainReply();
  /**
   * <code>.retainservice.RetainCoProcReply retainReply = 1;</code>
   * @return The retainReply.
   */
  RetainCoProcReply getRetainReply();
  /**
   * <code>.retainservice.RetainCoProcReply retainReply = 1;</code>
   */
  RetainCoProcReplyOrBuilder getRetainReplyOrBuilder();

  /**
   * <code>.retainservice.GCReply gcReply = 2;</code>
   * @return Whether the gcReply field is set.
   */
  boolean hasGcReply();
  /**
   * <code>.retainservice.GCReply gcReply = 2;</code>
   * @return The gcReply.
   */
  GCReply getGcReply();
  /**
   * <code>.retainservice.GCReply gcReply = 2;</code>
   */
  GCReplyOrBuilder getGcReplyOrBuilder();

  public RetainServiceRWCoProcOutput.TypeCase getTypeCase();
}