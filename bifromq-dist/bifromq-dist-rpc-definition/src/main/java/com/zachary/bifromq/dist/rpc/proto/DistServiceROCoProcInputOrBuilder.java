// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

public interface DistServiceROCoProcInputOrBuilder extends
    // @@protoc_insertion_point(interface_extends:distservice.DistServiceROCoProcInput)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.distservice.BatchDist dist = 1;</code>
   * @return Whether the dist field is set.
   */
  boolean hasDist();
  /**
   * <code>.distservice.BatchDist dist = 1;</code>
   * @return The dist.
   */
  com.zachary.bifromq.dist.rpc.proto.BatchDist getDist();
  /**
   * <code>.distservice.BatchDist dist = 1;</code>
   */
  com.zachary.bifromq.dist.rpc.proto.BatchDistOrBuilder getDistOrBuilder();

  /**
   * <code>.distservice.GCRequest gcRequest = 2;</code>
   * @return Whether the gcRequest field is set.
   */
  boolean hasGcRequest();
  /**
   * <code>.distservice.GCRequest gcRequest = 2;</code>
   * @return The gcRequest.
   */
  com.zachary.bifromq.dist.rpc.proto.GCRequest getGcRequest();
  /**
   * <code>.distservice.GCRequest gcRequest = 2;</code>
   */
  com.zachary.bifromq.dist.rpc.proto.GCRequestOrBuilder getGcRequestOrBuilder();

  /**
   * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
   * @return Whether the collectMetricsRequest field is set.
   */
  boolean hasCollectMetricsRequest();
  /**
   * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
   * @return The collectMetricsRequest.
   */
  com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest getCollectMetricsRequest();
  /**
   * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
   */
  com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequestOrBuilder getCollectMetricsRequestOrBuilder();

  public com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput.InputCase getInputCase();
}
