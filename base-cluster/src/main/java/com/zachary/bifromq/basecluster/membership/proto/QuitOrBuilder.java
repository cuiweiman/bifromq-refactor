// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/membership/Quit.proto

package com.zachary.bifromq.basecluster.membership.proto;

public interface QuitOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basecluster.membership.Quit)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * the endpoint who is quitting the cluster
   * </pre>
   *
   * <code>.basecluster.membership.HostEndpoint endpoint = 1;</code>
   * @return Whether the endpoint field is set.
   */
  boolean hasEndpoint();
  /**
   * <pre>
   * the endpoint who is quitting the cluster
   * </pre>
   *
   * <code>.basecluster.membership.HostEndpoint endpoint = 1;</code>
   * @return The endpoint.
   */
  com.zachary.bifromq.basecluster.membership.proto.HostEndpoint getEndpoint();
  /**
   * <pre>
   * the endpoint who is quitting the cluster
   * </pre>
   *
   * <code>.basecluster.membership.HostEndpoint endpoint = 1;</code>
   */
  com.zachary.bifromq.basecluster.membership.proto.HostEndpointOrBuilder getEndpointOrBuilder();

  /**
   * <code>uint32 incarnation = 2;</code>
   * @return The incarnation.
   */
  int getIncarnation();
}
