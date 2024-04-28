// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/agent/AgentMessage.proto

package com.zachary.bifromq.basecluster.agent.proto;

/**
 * Protobuf type {@code basecluster.agent.AgentMessageEnvelope}
 */
public final class AgentMessageEnvelope extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basecluster.agent.AgentMessageEnvelope)
    AgentMessageEnvelopeOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AgentMessageEnvelope.newBuilder() to construct.
  private AgentMessageEnvelope(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AgentMessageEnvelope() {
    agentId_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new AgentMessageEnvelope();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basecluster.agent.proto.AgentMessageProtos.internal_static_basecluster_agent_AgentMessageEnvelope_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basecluster.agent.proto.AgentMessageProtos.internal_static_basecluster_agent_AgentMessageEnvelope_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope.class, com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope.Builder.class);
  }

  public static final int AGENTID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object agentId_ = "";
  /**
   * <code>string agentId = 1;</code>
   * @return The agentId.
   */
  @java.lang.Override
  public java.lang.String getAgentId() {
    java.lang.Object ref = agentId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      agentId_ = s;
      return s;
    }
  }
  /**
   * <code>string agentId = 1;</code>
   * @return The bytes for agentId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getAgentIdBytes() {
    java.lang.Object ref = agentId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      agentId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int RECEIVER_FIELD_NUMBER = 2;
  private com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr receiver_;
  /**
   * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
   * @return Whether the receiver field is set.
   */
  @java.lang.Override
  public boolean hasReceiver() {
    return receiver_ != null;
  }
  /**
   * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
   * @return The receiver.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr getReceiver() {
    return receiver_ == null ? com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.getDefaultInstance() : receiver_;
  }
  /**
   * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddrOrBuilder getReceiverOrBuilder() {
    return receiver_ == null ? com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.getDefaultInstance() : receiver_;
  }

  public static final int MESSAGE_FIELD_NUMBER = 3;
  private com.zachary.bifromq.basecluster.agent.proto.AgentMessage message_;
  /**
   * <code>.basecluster.agent.AgentMessage message = 3;</code>
   * @return Whether the message field is set.
   */
  @java.lang.Override
  public boolean hasMessage() {
    return message_ != null;
  }
  /**
   * <code>.basecluster.agent.AgentMessage message = 3;</code>
   * @return The message.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.agent.proto.AgentMessage getMessage() {
    return message_ == null ? com.zachary.bifromq.basecluster.agent.proto.AgentMessage.getDefaultInstance() : message_;
  }
  /**
   * <code>.basecluster.agent.AgentMessage message = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.agent.proto.AgentMessageOrBuilder getMessageOrBuilder() {
    return message_ == null ? com.zachary.bifromq.basecluster.agent.proto.AgentMessage.getDefaultInstance() : message_;
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(agentId_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, agentId_);
    }
    if (receiver_ != null) {
      output.writeMessage(2, getReceiver());
    }
    if (message_ != null) {
      output.writeMessage(3, getMessage());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(agentId_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, agentId_);
    }
    if (receiver_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getReceiver());
    }
    if (message_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getMessage());
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
    if (!(obj instanceof com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope other = (com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope) obj;

    if (!getAgentId()
        .equals(other.getAgentId())) return false;
    if (hasReceiver() != other.hasReceiver()) return false;
    if (hasReceiver()) {
      if (!getReceiver()
          .equals(other.getReceiver())) return false;
    }
    if (hasMessage() != other.hasMessage()) return false;
    if (hasMessage()) {
      if (!getMessage()
          .equals(other.getMessage())) return false;
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
    hash = (37 * hash) + AGENTID_FIELD_NUMBER;
    hash = (53 * hash) + getAgentId().hashCode();
    if (hasReceiver()) {
      hash = (37 * hash) + RECEIVER_FIELD_NUMBER;
      hash = (53 * hash) + getReceiver().hashCode();
    }
    if (hasMessage()) {
      hash = (37 * hash) + MESSAGE_FIELD_NUMBER;
      hash = (53 * hash) + getMessage().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope prototype) {
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
   * Protobuf type {@code basecluster.agent.AgentMessageEnvelope}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basecluster.agent.AgentMessageEnvelope)
      com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelopeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basecluster.agent.proto.AgentMessageProtos.internal_static_basecluster_agent_AgentMessageEnvelope_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basecluster.agent.proto.AgentMessageProtos.internal_static_basecluster_agent_AgentMessageEnvelope_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope.class, com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope.Builder.class);
    }

    // Construct using com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope.newBuilder()
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
      agentId_ = "";
      receiver_ = null;
      if (receiverBuilder_ != null) {
        receiverBuilder_.dispose();
        receiverBuilder_ = null;
      }
      message_ = null;
      if (messageBuilder_ != null) {
        messageBuilder_.dispose();
        messageBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basecluster.agent.proto.AgentMessageProtos.internal_static_basecluster_agent_AgentMessageEnvelope_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope getDefaultInstanceForType() {
      return com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope build() {
      com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope buildPartial() {
      com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope result = new com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.agentId_ = agentId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.receiver_ = receiverBuilder_ == null
            ? receiver_
            : receiverBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.message_ = messageBuilder_ == null
            ? message_
            : messageBuilder_.build();
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
      if (other instanceof com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope) {
        return mergeFrom((com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope other) {
      if (other == com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope.getDefaultInstance()) return this;
      if (!other.getAgentId().isEmpty()) {
        agentId_ = other.agentId_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.hasReceiver()) {
        mergeReceiver(other.getReceiver());
      }
      if (other.hasMessage()) {
        mergeMessage(other.getMessage());
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
              agentId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getReceiverFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              input.readMessage(
                  getMessageFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000004;
              break;
            } // case 26
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

    private java.lang.Object agentId_ = "";
    /**
     * <code>string agentId = 1;</code>
     * @return The agentId.
     */
    public java.lang.String getAgentId() {
      java.lang.Object ref = agentId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        agentId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string agentId = 1;</code>
     * @return The bytes for agentId.
     */
    public com.google.protobuf.ByteString
        getAgentIdBytes() {
      java.lang.Object ref = agentId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        agentId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string agentId = 1;</code>
     * @param value The agentId to set.
     * @return This builder for chaining.
     */
    public Builder setAgentId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      agentId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string agentId = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearAgentId() {
      agentId_ = getDefaultInstance().getAgentId();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string agentId = 1;</code>
     * @param value The bytes for agentId to set.
     * @return This builder for chaining.
     */
    public Builder setAgentIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      agentId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr receiver_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr, com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.Builder, com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddrOrBuilder> receiverBuilder_;
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     * @return Whether the receiver field is set.
     */
    public boolean hasReceiver() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     * @return The receiver.
     */
    public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr getReceiver() {
      if (receiverBuilder_ == null) {
        return receiver_ == null ? com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.getDefaultInstance() : receiver_;
      } else {
        return receiverBuilder_.getMessage();
      }
    }
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     */
    public Builder setReceiver(com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr value) {
      if (receiverBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        receiver_ = value;
      } else {
        receiverBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     */
    public Builder setReceiver(
        com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.Builder builderForValue) {
      if (receiverBuilder_ == null) {
        receiver_ = builderForValue.build();
      } else {
        receiverBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     */
    public Builder mergeReceiver(com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr value) {
      if (receiverBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0) &&
          receiver_ != null &&
          receiver_ != com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.getDefaultInstance()) {
          getReceiverBuilder().mergeFrom(value);
        } else {
          receiver_ = value;
        }
      } else {
        receiverBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     */
    public Builder clearReceiver() {
      bitField0_ = (bitField0_ & ~0x00000002);
      receiver_ = null;
      if (receiverBuilder_ != null) {
        receiverBuilder_.dispose();
        receiverBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     */
    public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.Builder getReceiverBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getReceiverFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     */
    public com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddrOrBuilder getReceiverOrBuilder() {
      if (receiverBuilder_ != null) {
        return receiverBuilder_.getMessageOrBuilder();
      } else {
        return receiver_ == null ?
            com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.getDefaultInstance() : receiver_;
      }
    }
    /**
     * <code>.basecluster.agent.AgentMemberAddr receiver = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr, com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.Builder, com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddrOrBuilder> 
        getReceiverFieldBuilder() {
      if (receiverBuilder_ == null) {
        receiverBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr, com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddr.Builder, com.zachary.bifromq.basecluster.agent.proto.AgentMemberAddrOrBuilder>(
                getReceiver(),
                getParentForChildren(),
                isClean());
        receiver_ = null;
      }
      return receiverBuilder_;
    }

    private com.zachary.bifromq.basecluster.agent.proto.AgentMessage message_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.agent.proto.AgentMessage, com.zachary.bifromq.basecluster.agent.proto.AgentMessage.Builder, com.zachary.bifromq.basecluster.agent.proto.AgentMessageOrBuilder> messageBuilder_;
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     * @return Whether the message field is set.
     */
    public boolean hasMessage() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     * @return The message.
     */
    public com.zachary.bifromq.basecluster.agent.proto.AgentMessage getMessage() {
      if (messageBuilder_ == null) {
        return message_ == null ? com.zachary.bifromq.basecluster.agent.proto.AgentMessage.getDefaultInstance() : message_;
      } else {
        return messageBuilder_.getMessage();
      }
    }
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     */
    public Builder setMessage(com.zachary.bifromq.basecluster.agent.proto.AgentMessage value) {
      if (messageBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        message_ = value;
      } else {
        messageBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     */
    public Builder setMessage(
        com.zachary.bifromq.basecluster.agent.proto.AgentMessage.Builder builderForValue) {
      if (messageBuilder_ == null) {
        message_ = builderForValue.build();
      } else {
        messageBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     */
    public Builder mergeMessage(com.zachary.bifromq.basecluster.agent.proto.AgentMessage value) {
      if (messageBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0) &&
          message_ != null &&
          message_ != com.zachary.bifromq.basecluster.agent.proto.AgentMessage.getDefaultInstance()) {
          getMessageBuilder().mergeFrom(value);
        } else {
          message_ = value;
        }
      } else {
        messageBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     */
    public Builder clearMessage() {
      bitField0_ = (bitField0_ & ~0x00000004);
      message_ = null;
      if (messageBuilder_ != null) {
        messageBuilder_.dispose();
        messageBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     */
    public com.zachary.bifromq.basecluster.agent.proto.AgentMessage.Builder getMessageBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getMessageFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     */
    public com.zachary.bifromq.basecluster.agent.proto.AgentMessageOrBuilder getMessageOrBuilder() {
      if (messageBuilder_ != null) {
        return messageBuilder_.getMessageOrBuilder();
      } else {
        return message_ == null ?
            com.zachary.bifromq.basecluster.agent.proto.AgentMessage.getDefaultInstance() : message_;
      }
    }
    /**
     * <code>.basecluster.agent.AgentMessage message = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.agent.proto.AgentMessage, com.zachary.bifromq.basecluster.agent.proto.AgentMessage.Builder, com.zachary.bifromq.basecluster.agent.proto.AgentMessageOrBuilder> 
        getMessageFieldBuilder() {
      if (messageBuilder_ == null) {
        messageBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecluster.agent.proto.AgentMessage, com.zachary.bifromq.basecluster.agent.proto.AgentMessage.Builder, com.zachary.bifromq.basecluster.agent.proto.AgentMessageOrBuilder>(
                getMessage(),
                getParentForChildren(),
                isClean());
        message_ = null;
      }
      return messageBuilder_;
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


    // @@protoc_insertion_point(builder_scope:basecluster.agent.AgentMessageEnvelope)
  }

  // @@protoc_insertion_point(class_scope:basecluster.agent.AgentMessageEnvelope)
  private static final com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope();
  }

  public static com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AgentMessageEnvelope>
      PARSER = new com.google.protobuf.AbstractParser<AgentMessageEnvelope>() {
    @java.lang.Override
    public AgentMessageEnvelope parsePartialFrom(
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

  public static com.google.protobuf.Parser<AgentMessageEnvelope> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AgentMessageEnvelope> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basecluster.agent.proto.AgentMessageEnvelope getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

