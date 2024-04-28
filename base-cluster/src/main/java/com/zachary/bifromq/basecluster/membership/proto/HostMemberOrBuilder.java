// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/membership/HostMember.proto

package com.zachary.bifromq.basecluster.membership.proto;

public interface HostMemberOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basecluster.membership.HostMember)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 1;</code>
   * @return Whether the endpoint field is set.
   */
  boolean hasEndpoint();
  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 1;</code>
   * @return The endpoint.
   */
  com.zachary.bifromq.basecluster.membership.proto.HostEndpoint getEndpoint();
  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 1;</code>
   */
  com.zachary.bifromq.basecluster.membership.proto.HostEndpointOrBuilder getEndpointOrBuilder();

  /**
   * <pre>
   * incarnation of the node, managed by the node itself
   * </pre>
   *
   * <code>uint32 incarnation = 2;</code>
   * @return The incarnation.
   */
  int getIncarnation();

  /**
   * <pre>
   * the list of agents reside on the host
   * </pre>
   *
   * <code>repeated string agentId = 3;</code>
   * @return A list containing the agentId.
   */
  java.util.List<java.lang.String>
      getAgentIdList();
  /**
   * <pre>
   * the list of agents reside on the host
   * </pre>
   *
   * <code>repeated string agentId = 3;</code>
   * @return The count of agentId.
   */
  int getAgentIdCount();
  /**
   * <pre>
   * the list of agents reside on the host
   * </pre>
   *
   * <code>repeated string agentId = 3;</code>
   * @param index The index of the element to return.
   * @return The agentId at the given index.
   */
  java.lang.String getAgentId(int index);
  /**
   * <pre>
   * the list of agents reside on the host
   * </pre>
   *
   * <code>repeated string agentId = 3;</code>
   * @param index The index of the value to return.
   * @return The bytes of the agentId at the given index.
   */
  com.google.protobuf.ByteString
      getAgentIdBytes(int index);
}