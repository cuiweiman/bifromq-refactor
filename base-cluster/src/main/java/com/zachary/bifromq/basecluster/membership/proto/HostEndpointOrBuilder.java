// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/membership/HostMember.proto

package com.zachary.bifromq.basecluster.membership.proto;

public interface HostEndpointOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basecluster.membership.HostEndpoint)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * the id of the host member which is identical to the replica id of local HostMemberListCRDT
   * </pre>
   *
   * <code>bytes id = 1;</code>
   * @return The id.
   */
  com.google.protobuf.ByteString getId();

  /**
   * <pre>
   * the network address of the host
   * </pre>
   *
   * <code>string address = 3;</code>
   * @return The address.
   */
  java.lang.String getAddress();
  /**
   * <pre>
   * the network address of the host
   * </pre>
   *
   * <code>string address = 3;</code>
   * @return The bytes for address.
   */
  com.google.protobuf.ByteString
      getAddressBytes();

  /**
   * <pre>
   * bind network port of the host
   * </pre>
   *
   * <code>uint32 port = 4;</code>
   * @return The port.
   */
  int getPort();
}
