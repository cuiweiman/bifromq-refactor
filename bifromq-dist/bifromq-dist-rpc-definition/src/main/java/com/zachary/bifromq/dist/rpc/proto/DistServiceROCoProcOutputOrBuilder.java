// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface DistServiceROCoProcOutputOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.DistServiceROCoProcOutput)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.distservice.BatchDistReply distReply = 1;</code>
   * @return Whether the distReply field is set.
   */
  boolean hasDistReply();
  /**
   * <code>.distservice.BatchDistReply distReply = 1;</code>
   * @return The distReply.
   */
  com.zachary.bifromq.dist.rpc.proto.BatchDistReply getDistReply();
  /**
   * <code>.distservice.BatchDistReply distReply = 1;</code>
   */
  com.zachary.bifromq.dist.rpc.proto.BatchDistReplyOrBuilder getDistReplyOrBuilder();

  /**
   * <code>.distservice.GCReply gcReply = 2;</code>
   * @return Whether the gcReply field is set.
   */
  boolean hasGcReply();
  /**
   * <code>.distservice.GCReply gcReply = 2;</code>
   * @return The gcReply.
   */
  com.zachary.bifromq.dist.rpc.proto.GCReply getGcReply();
  /**
   * <code>.distservice.GCReply gcReply = 2;</code>
   */
  com.zachary.bifromq.dist.rpc.proto.GCReplyOrBuilder getGcReplyOrBuilder();

  /**
   * <code>.distservice.CollectMetricsReply collectMetricsReply = 3;</code>
   * @return Whether the collectMetricsReply field is set.
   */
  boolean hasCollectMetricsReply();
  /**
   * <code>.distservice.CollectMetricsReply collectMetricsReply = 3;</code>
   * @return The collectMetricsReply.
   */
  com.zachary.bifromq.dist.rpc.proto.CollectMetricsReply getCollectMetricsReply();
  /**
   * <code>.distservice.CollectMetricsReply collectMetricsReply = 3;</code>
   */
  com.zachary.bifromq.dist.rpc.proto.CollectMetricsReplyOrBuilder getCollectMetricsReplyOrBuilder();

  public com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcOutput.OutputCase getOutputCase();
}