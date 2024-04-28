// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/agent/AgentMember.proto

package com.zachary.bifromq.basecluster.agent.proto;

/**
 * Protobuf type {@code basecluster.agent.AgentMemberAddr}
 */
public final class AgentMemberAddr extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basecluster.agent.AgentMemberAddr)
    AgentMemberAddrOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AgentMemberAddr.newBuilder() to construct.
  private AgentMemberAddr(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AgentMemberAddr() {
    name_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new AgentMemberAddr();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basecluster.agent.proto.AgentMemberOuterClass.internal_static_basecluster_agent_AgentMemberAddr_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basecluster.agent.proto.AgentMemberOuterClass.internal_static_basecluster_agent_AgentMemberAddr_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.class, com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.Builder.class);
  }

  public static final int NAME_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object name_ = "";
  /**
   * <pre>
   * the name of the agent member, it's allowed to have same name registered in different hosts
   * </pre>
   *
   * <code>string name = 1;</code>
   * @return The name.
   */
  @java.lang.Override
  public java.lang.String getName() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      name_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * the name of the agent member, it's allowed to have same name registered in different hosts
   * </pre>
   *
   * <code>string name = 1;</code>
   * @return The bytes for name.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getNameBytes() {
    java.lang.Object ref = name_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      name_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ENDPOINT_FIELD_NUMBER = 2;
  private com.zachary.bifromq.basecluster.membership.proto.HostEndpoint endpoint_;
  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
   * @return Whether the endpoint field is set.
   */
  @java.lang.Override
  public boolean hasEndpoint() {
    return endpoint_ != null;
  }
  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
   * @return The endpoint.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.membership.proto.HostEndpoint getEndpoint() {
    return endpoint_ == null ? com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.getDefaultInstance() : endpoint_;
  }
  /**
   * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.membership.proto.HostEndpointOrBuilder getEndpointOrBuilder() {
    return endpoint_ == null ? com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.getDefaultInstance() : endpoint_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, name_);
    }
    if (endpoint_ != null) {
      output.writeMessage(2, getEndpoint());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(name_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, name_);
    }
    if (endpoint_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getEndpoint());
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr other = (com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr) obj;

    if (!getName()
        .equals(other.getName())) return false;
    if (hasEndpoint() != other.hasEndpoint()) return false;
    if (hasEndpoint()) {
      if (!getEndpoint()
          .equals(other.getEndpoint())) return false;
    }
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + NAME_FIELD_NUMBER;
    hash = (53 * hash) + getName().hashCode();
    if (hasEndpoint()) {
      hash = (37 * hash) + ENDPOINT_FIELD_NUMBER;
      hash = (53 * hash) + getEndpoint().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code basecluster.agent.AgentMemberAddr}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basecluster.agent.AgentMemberAddr)
      com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddrOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basecluster.agent.proto.AgentMemberOuterClass.internal_static_basecluster_agent_AgentMemberAddr_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basecluster.agent.proto.AgentMemberOuterClass.internal_static_basecluster_agent_AgentMemberAddr_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.class, com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.Builder.class);
    }

    // Construct using com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      name_ = "";
      endpoint_ = null;
      if (endpointBuilder_ != null) {
        endpointBuilder_.dispose();
        endpointBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basecluster.agent.proto.AgentMemberOuterClass.internal_static_basecluster_agent_AgentMemberAddr_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr getDefaultInstanceForType() {
      return com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr build() {
      com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr buildPartial() {
      com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr result = new com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.name_ = name_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.endpoint_ = endpointBuilder_ == null
            ? endpoint_
            : endpointBuilder_.build();
      }
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr) {
        return mergeFrom((com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr other) {
      if (other == com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.getDefaultInstance()) return this;
      if (!other.getName().isEmpty()) {
        name_ = other.name_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.hasEndpoint()) {
        mergeEndpoint(other.getEndpoint());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              name_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getEndpointFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.lang.Object name_ = "";
    /**
     * <pre>
     * the name of the agent member, it's allowed to have same name registered in different hosts
     * </pre>
     *
     * <code>string name = 1;</code>
     * @return The name.
     */
    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        name_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * the name of the agent member, it's allowed to have same name registered in different hosts
     * </pre>
     *
     * <code>string name = 1;</code>
     * @return The bytes for name.
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * the name of the agent member, it's allowed to have same name registered in different hosts
     * </pre>
     *
     * <code>string name = 1;</code>
     * @param value The name to set.
     * @return This builder for chaining.
     */
    public Builder setName(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      name_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the name of the agent member, it's allowed to have same name registered in different hosts
     * </pre>
     *
     * <code>string name = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearName() {
      name_ = getDefaultInstance().getName();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the name of the agent member, it's allowed to have same name registered in different hosts
     * </pre>
     *
     * <code>string name = 1;</code>
     * @param value The bytes for name to set.
     * @return This builder for chaining.
     */
    public Builder setNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      name_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private com.zachary.bifromq.basecluster.membership.proto.HostEndpoint endpoint_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.membership.proto.HostEndpoint, com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.Builder, com.zachary.bifromq.basecluster.membership.proto.HostEndpointOrBuilder> endpointBuilder_;
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     * @return Whether the endpoint field is set.
     */
    public boolean hasEndpoint() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     * @return The endpoint.
     */
    public com.zachary.bifromq.basecluster.membership.proto.HostEndpoint getEndpoint() {
      if (endpointBuilder_ == null) {
        return endpoint_ == null ? com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.getDefaultInstance() : endpoint_;
      } else {
        return endpointBuilder_.getMessage();
      }
    }
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     */
    public Builder setEndpoint(com.zachary.bifromq.basecluster.membership.proto.HostEndpoint value) {
      if (endpointBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        endpoint_ = value;
      } else {
        endpointBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     */
    public Builder setEndpoint(
        com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.Builder builderForValue) {
      if (endpointBuilder_ == null) {
        endpoint_ = builderForValue.build();
      } else {
        endpointBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     */
    public Builder mergeEndpoint(com.zachary.bifromq.basecluster.membership.proto.HostEndpoint value) {
      if (endpointBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0) &&
          endpoint_ != null &&
          endpoint_ != com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.getDefaultInstance()) {
          getEndpointBuilder().mergeFrom(value);
        } else {
          endpoint_ = value;
        }
      } else {
        endpointBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     */
    public Builder clearEndpoint() {
      bitField0_ = (bitField0_ & ~0x00000002);
      endpoint_ = null;
      if (endpointBuilder_ != null) {
        endpointBuilder_.dispose();
        endpointBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     */
    public com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.Builder getEndpointBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getEndpointFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     */
    public com.zachary.bifromq.basecluster.membership.proto.HostEndpointOrBuilder getEndpointOrBuilder() {
      if (endpointBuilder_ != null) {
        return endpointBuilder_.getMessageOrBuilder();
      } else {
        return endpoint_ == null ?
            com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.getDefaultInstance() : endpoint_;
      }
    }
    /**
     * <code>.basecluster.membership.HostEndpoint endpoint = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.membership.proto.HostEndpoint, com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.Builder, com.zachary.bifromq.basecluster.membership.proto.HostEndpointOrBuilder> 
        getEndpointFieldBuilder() {
      if (endpointBuilder_ == null) {
        endpointBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecluster.membership.proto.HostEndpoint, com.zachary.bifromq.basecluster.membership.proto.HostEndpoint.Builder, com.zachary.bifromq.basecluster.membership.proto.HostEndpointOrBuilder>(
                getEndpoint(),
                getParentForChildren(),
                isClean());
        endpoint_ = null;
      }
      return endpointBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:basecluster.agent.AgentMemberAddr)
  }

  // @@protoc_insertion_point(class_scope:basecluster.agent.AgentMemberAddr)
  private static final com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr();
  }

  public static com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AgentMemberAddr>
      PARSER = new com.google.protobuf.AbstractParser<AgentMemberAddr>() {
    @java.lang.Override
    public AgentMemberAddr parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<AgentMemberAddr> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AgentMemberAddr> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
