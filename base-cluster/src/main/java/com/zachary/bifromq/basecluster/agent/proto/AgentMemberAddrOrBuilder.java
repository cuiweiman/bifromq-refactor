// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/agent/AgentMember.proto

package com.zachary.bifromq.basecluster.agent.proto;

public interface AgentMemberAddrOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basecluster.agent.AgentMemberAddr)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * the name of the agent member, it's allowed to have same name registered in different hosts
   * </pre>
   *
   * <code>string name = 1;</code>
   * @return The name.
   */
  java.lang.String getName();
  /**
   * <pre>
   * the name of the agent member, it's allowed to have same name registered in different hosts
   * </pre>
   *
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString
      getNameBytes();

  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
   * @return Whether the endpoint field is set.
   */
  boolean hasEndpoint();
  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
   * @return The endpoint.
   */
  com.zachary.bifromq.basecluster.membership.proto.HostEndpoint getEndpoint();
  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
   */
  com.zachary.bifromq.basecluster.membership.proto.HostEndpointOrBuilder getEndpointOrBuilder();
}