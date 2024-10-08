// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecrdt/CRDTStoreMessage.proto

package com.zachary.bifromq.basecrdt.store.proto;

/**
 * <pre>
 * 消息报文, oneof 表示 只能是其中的一个字段赋值，后赋值的将覆盖。
 * 如下 两个字段 delta 和 ack，只能赋值其中一个字段
 * </pre>
 *
 * Protobuf type {@code basecrdt.MessagePayload}
 */
public final class MessagePayload extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basecrdt.MessagePayload)
    MessagePayloadOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MessagePayload.newBuilder() to construct.
  private MessagePayload(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MessagePayload() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new MessagePayload();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessageOuterClass.internal_static_basecrdt_MessagePayload_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessageOuterClass.internal_static_basecrdt_MessagePayload_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basecrdt.store.proto.MessagePayload.class, com.zachary.bifromq.basecrdt.store.proto.MessagePayload.Builder.class);
  }

  private int msgTypeCase_ = 0;
  private java.lang.Object msgType_;
  public enum MsgTypeCase
      implements com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    DELTA(1),
    ACK(2),
    MSGTYPE_NOT_SET(0);
    private final int value;
    private MsgTypeCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static MsgTypeCase valueOf(int value) {
      return forNumber(value);
    }

    public static MsgTypeCase forNumber(int value) {
      switch (value) {
        case 1: return DELTA;
        case 2: return ACK;
        case 0: return MSGTYPE_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public MsgTypeCase
  getMsgTypeCase() {
    return MsgTypeCase.forNumber(
        msgTypeCase_);
  }

  public static final int DELTA_FIELD_NUMBER = 1;
  /**
   * <code>.basecrdt.DeltaMessage delta = 1;</code>
   * @return Whether the delta field is set.
   */
  @java.lang.Override
  public boolean hasDelta() {
    return msgTypeCase_ == 1;
  }
  /**
   * <code>.basecrdt.DeltaMessage delta = 1;</code>
   * @return The delta.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.store.proto.DeltaMessage getDelta() {
    if (msgTypeCase_ == 1) {
       return (com.zachary.bifromq.basecrdt.store.proto.DeltaMessage) msgType_;
    }
    return com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.getDefaultInstance();
  }
  /**
   * <code>.basecrdt.DeltaMessage delta = 1;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.store.proto.DeltaMessageOrBuilder getDeltaOrBuilder() {
    if (msgTypeCase_ == 1) {
       return (com.zachary.bifromq.basecrdt.store.proto.DeltaMessage) msgType_;
    }
    return com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.getDefaultInstance();
  }

  public static final int ACK_FIELD_NUMBER = 2;
  /**
   * <code>.basecrdt.AckMessage ack = 2;</code>
   * @return Whether the ack field is set.
   */
  @java.lang.Override
  public boolean hasAck() {
    return msgTypeCase_ == 2;
  }
  /**
   * <code>.basecrdt.AckMessage ack = 2;</code>
   * @return The ack.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.store.proto.AckMessage getAck() {
    if (msgTypeCase_ == 2) {
       return (com.zachary.bifromq.basecrdt.store.proto.AckMessage) msgType_;
    }
    return com.zachary.bifromq.basecrdt.store.proto.AckMessage.getDefaultInstance();
  }
  /**
   * <code>.basecrdt.AckMessage ack = 2;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.store.proto.AckMessageOrBuilder getAckOrBuilder() {
    if (msgTypeCase_ == 2) {
       return (com.zachary.bifromq.basecrdt.store.proto.AckMessage) msgType_;
    }
    return com.zachary.bifromq.basecrdt.store.proto.AckMessage.getDefaultInstance();
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
    if (msgTypeCase_ == 1) {
      output.writeMessage(1, (com.zachary.bifromq.basecrdt.store.proto.DeltaMessage) msgType_);
    }
    if (msgTypeCase_ == 2) {
      output.writeMessage(2, (com.zachary.bifromq.basecrdt.store.proto.AckMessage) msgType_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (msgTypeCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (com.zachary.bifromq.basecrdt.store.proto.DeltaMessage) msgType_);
    }
    if (msgTypeCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (com.zachary.bifromq.basecrdt.store.proto.AckMessage) msgType_);
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
    if (!(obj instanceof com.zachary.bifromq.basecrdt.store.proto.MessagePayload)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basecrdt.store.proto.MessagePayload other = (com.zachary.bifromq.basecrdt.store.proto.MessagePayload) obj;

    if (!getMsgTypeCase().equals(other.getMsgTypeCase())) return false;
    switch (msgTypeCase_) {
      case 1:
        if (!getDelta()
            .equals(other.getDelta())) return false;
        break;
      case 2:
        if (!getAck()
            .equals(other.getAck())) return false;
        break;
      case 0:
      default:
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
    switch (msgTypeCase_) {
      case 1:
        hash = (37 * hash) + DELTA_FIELD_NUMBER;
        hash = (53 * hash) + getDelta().hashCode();
        break;
      case 2:
        hash = (37 * hash) + ACK_FIELD_NUMBER;
        hash = (53 * hash) + getAck().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basecrdt.store.proto.MessagePayload prototype) {
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
   * <pre>
   * 消息报文, oneof 表示 只能是其中的一个字段赋值，后赋值的将覆盖。
   * 如下 两个字段 delta 和 ack，只能赋值其中一个字段
   * </pre>
   *
   * Protobuf type {@code basecrdt.MessagePayload}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basecrdt.MessagePayload)
      com.zachary.bifromq.basecrdt.store.proto.MessagePayloadOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessageOuterClass.internal_static_basecrdt_MessagePayload_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessageOuterClass.internal_static_basecrdt_MessagePayload_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basecrdt.store.proto.MessagePayload.class, com.zachary.bifromq.basecrdt.store.proto.MessagePayload.Builder.class);
    }

    // Construct using com.zachary.bifromq.basecrdt.store.proto.MessagePayload.newBuilder()
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
      if (deltaBuilder_ != null) {
        deltaBuilder_.clear();
      }
      if (ackBuilder_ != null) {
        ackBuilder_.clear();
      }
      msgTypeCase_ = 0;
      msgType_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basecrdt.store.proto.CRDTStoreMessageOuterClass.internal_static_basecrdt_MessagePayload_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecrdt.store.proto.MessagePayload getDefaultInstanceForType() {
      return com.zachary.bifromq.basecrdt.store.proto.MessagePayload.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basecrdt.store.proto.MessagePayload build() {
      com.zachary.bifromq.basecrdt.store.proto.MessagePayload result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecrdt.store.proto.MessagePayload buildPartial() {
      com.zachary.bifromq.basecrdt.store.proto.MessagePayload result = new com.zachary.bifromq.basecrdt.store.proto.MessagePayload(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      buildPartialOneofs(result);
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basecrdt.store.proto.MessagePayload result) {
      int from_bitField0_ = bitField0_;
    }

    private void buildPartialOneofs(com.zachary.bifromq.basecrdt.store.proto.MessagePayload result) {
      result.msgTypeCase_ = msgTypeCase_;
      result.msgType_ = this.msgType_;
      if (msgTypeCase_ == 1 &&
          deltaBuilder_ != null) {
        result.msgType_ = deltaBuilder_.build();
      }
      if (msgTypeCase_ == 2 &&
          ackBuilder_ != null) {
        result.msgType_ = ackBuilder_.build();
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
      if (other instanceof com.zachary.bifromq.basecrdt.store.proto.MessagePayload) {
        return mergeFrom((com.zachary.bifromq.basecrdt.store.proto.MessagePayload)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basecrdt.store.proto.MessagePayload other) {
      if (other == com.zachary.bifromq.basecrdt.store.proto.MessagePayload.getDefaultInstance()) return this;
      switch (other.getMsgTypeCase()) {
        case DELTA: {
          mergeDelta(other.getDelta());
          break;
        }
        case ACK: {
          mergeAck(other.getAck());
          break;
        }
        case MSGTYPE_NOT_SET: {
          break;
        }
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
              input.readMessage(
                  getDeltaFieldBuilder().getBuilder(),
                  extensionRegistry);
              msgTypeCase_ = 1;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getAckFieldBuilder().getBuilder(),
                  extensionRegistry);
              msgTypeCase_ = 2;
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
    private int msgTypeCase_ = 0;
    private java.lang.Object msgType_;
    public MsgTypeCase
        getMsgTypeCase() {
      return MsgTypeCase.forNumber(
          msgTypeCase_);
    }

    public Builder clearMsgType() {
      msgTypeCase_ = 0;
      msgType_ = null;
      onChanged();
      return this;
    }

    private int bitField0_;

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.store.proto.DeltaMessage, com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.Builder, com.zachary.bifromq.basecrdt.store.proto.DeltaMessageOrBuilder> deltaBuilder_;
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     * @return Whether the delta field is set.
     */
    @java.lang.Override
    public boolean hasDelta() {
      return msgTypeCase_ == 1;
    }
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     * @return The delta.
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.store.proto.DeltaMessage getDelta() {
      if (deltaBuilder_ == null) {
        if (msgTypeCase_ == 1) {
          return (com.zachary.bifromq.basecrdt.store.proto.DeltaMessage) msgType_;
        }
        return com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.getDefaultInstance();
      } else {
        if (msgTypeCase_ == 1) {
          return deltaBuilder_.getMessage();
        }
        return com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     */
    public Builder setDelta(com.zachary.bifromq.basecrdt.store.proto.DeltaMessage value) {
      if (deltaBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        msgType_ = value;
        onChanged();
      } else {
        deltaBuilder_.setMessage(value);
      }
      msgTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     */
    public Builder setDelta(
        com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.Builder builderForValue) {
      if (deltaBuilder_ == null) {
        msgType_ = builderForValue.build();
        onChanged();
      } else {
        deltaBuilder_.setMessage(builderForValue.build());
      }
      msgTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     */
    public Builder mergeDelta(com.zachary.bifromq.basecrdt.store.proto.DeltaMessage value) {
      if (deltaBuilder_ == null) {
        if (msgTypeCase_ == 1 &&
            msgType_ != com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.getDefaultInstance()) {
          msgType_ = com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.newBuilder((com.zachary.bifromq.basecrdt.store.proto.DeltaMessage) msgType_)
              .mergeFrom(value).buildPartial();
        } else {
          msgType_ = value;
        }
        onChanged();
      } else {
        if (msgTypeCase_ == 1) {
          deltaBuilder_.mergeFrom(value);
        } else {
          deltaBuilder_.setMessage(value);
        }
      }
      msgTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     */
    public Builder clearDelta() {
      if (deltaBuilder_ == null) {
        if (msgTypeCase_ == 1) {
          msgTypeCase_ = 0;
          msgType_ = null;
          onChanged();
        }
      } else {
        if (msgTypeCase_ == 1) {
          msgTypeCase_ = 0;
          msgType_ = null;
        }
        deltaBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     */
    public com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.Builder getDeltaBuilder() {
      return getDeltaFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.store.proto.DeltaMessageOrBuilder getDeltaOrBuilder() {
      if ((msgTypeCase_ == 1) && (deltaBuilder_ != null)) {
        return deltaBuilder_.getMessageOrBuilder();
      } else {
        if (msgTypeCase_ == 1) {
          return (com.zachary.bifromq.basecrdt.store.proto.DeltaMessage) msgType_;
        }
        return com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.DeltaMessage delta = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.store.proto.DeltaMessage, com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.Builder, com.zachary.bifromq.basecrdt.store.proto.DeltaMessageOrBuilder> 
        getDeltaFieldBuilder() {
      if (deltaBuilder_ == null) {
        if (!(msgTypeCase_ == 1)) {
          msgType_ = com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.getDefaultInstance();
        }
        deltaBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecrdt.store.proto.DeltaMessage, com.zachary.bifromq.basecrdt.store.proto.DeltaMessage.Builder, com.zachary.bifromq.basecrdt.store.proto.DeltaMessageOrBuilder>(
                (com.zachary.bifromq.basecrdt.store.proto.DeltaMessage) msgType_,
                getParentForChildren(),
                isClean());
        msgType_ = null;
      }
      msgTypeCase_ = 1;
      onChanged();
      return deltaBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.store.proto.AckMessage, com.zachary.bifromq.basecrdt.store.proto.AckMessage.Builder, com.zachary.bifromq.basecrdt.store.proto.AckMessageOrBuilder> ackBuilder_;
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     * @return Whether the ack field is set.
     */
    @java.lang.Override
    public boolean hasAck() {
      return msgTypeCase_ == 2;
    }
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     * @return The ack.
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.store.proto.AckMessage getAck() {
      if (ackBuilder_ == null) {
        if (msgTypeCase_ == 2) {
          return (com.zachary.bifromq.basecrdt.store.proto.AckMessage) msgType_;
        }
        return com.zachary.bifromq.basecrdt.store.proto.AckMessage.getDefaultInstance();
      } else {
        if (msgTypeCase_ == 2) {
          return ackBuilder_.getMessage();
        }
        return com.zachary.bifromq.basecrdt.store.proto.AckMessage.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     */
    public Builder setAck(com.zachary.bifromq.basecrdt.store.proto.AckMessage value) {
      if (ackBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        msgType_ = value;
        onChanged();
      } else {
        ackBuilder_.setMessage(value);
      }
      msgTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     */
    public Builder setAck(
        com.zachary.bifromq.basecrdt.store.proto.AckMessage.Builder builderForValue) {
      if (ackBuilder_ == null) {
        msgType_ = builderForValue.build();
        onChanged();
      } else {
        ackBuilder_.setMessage(builderForValue.build());
      }
      msgTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     */
    public Builder mergeAck(com.zachary.bifromq.basecrdt.store.proto.AckMessage value) {
      if (ackBuilder_ == null) {
        if (msgTypeCase_ == 2 &&
            msgType_ != com.zachary.bifromq.basecrdt.store.proto.AckMessage.getDefaultInstance()) {
          msgType_ = com.zachary.bifromq.basecrdt.store.proto.AckMessage.newBuilder((com.zachary.bifromq.basecrdt.store.proto.AckMessage) msgType_)
              .mergeFrom(value).buildPartial();
        } else {
          msgType_ = value;
        }
        onChanged();
      } else {
        if (msgTypeCase_ == 2) {
          ackBuilder_.mergeFrom(value);
        } else {
          ackBuilder_.setMessage(value);
        }
      }
      msgTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     */
    public Builder clearAck() {
      if (ackBuilder_ == null) {
        if (msgTypeCase_ == 2) {
          msgTypeCase_ = 0;
          msgType_ = null;
          onChanged();
        }
      } else {
        if (msgTypeCase_ == 2) {
          msgTypeCase_ = 0;
          msgType_ = null;
        }
        ackBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     */
    public com.zachary.bifromq.basecrdt.store.proto.AckMessage.Builder getAckBuilder() {
      return getAckFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.store.proto.AckMessageOrBuilder getAckOrBuilder() {
      if ((msgTypeCase_ == 2) && (ackBuilder_ != null)) {
        return ackBuilder_.getMessageOrBuilder();
      } else {
        if (msgTypeCase_ == 2) {
          return (com.zachary.bifromq.basecrdt.store.proto.AckMessage) msgType_;
        }
        return com.zachary.bifromq.basecrdt.store.proto.AckMessage.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.AckMessage ack = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.store.proto.AckMessage, com.zachary.bifromq.basecrdt.store.proto.AckMessage.Builder, com.zachary.bifromq.basecrdt.store.proto.AckMessageOrBuilder> 
        getAckFieldBuilder() {
      if (ackBuilder_ == null) {
        if (!(msgTypeCase_ == 2)) {
          msgType_ = com.zachary.bifromq.basecrdt.store.proto.AckMessage.getDefaultInstance();
        }
        ackBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecrdt.store.proto.AckMessage, com.zachary.bifromq.basecrdt.store.proto.AckMessage.Builder, com.zachary.bifromq.basecrdt.store.proto.AckMessageOrBuilder>(
                (com.zachary.bifromq.basecrdt.store.proto.AckMessage) msgType_,
                getParentForChildren(),
                isClean());
        msgType_ = null;
      }
      msgTypeCase_ = 2;
      onChanged();
      return ackBuilder_;
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


    // @@protoc_insertion_point(builder_scope:basecrdt.MessagePayload)
  }

  // @@protoc_insertion_point(class_scope:basecrdt.MessagePayload)
  private static final com.zachary.bifromq.basecrdt.store.proto.MessagePayload DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basecrdt.store.proto.MessagePayload();
  }

  public static com.zachary.bifromq.basecrdt.store.proto.MessagePayload getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MessagePayload>
      PARSER = new com.google.protobuf.AbstractParser<MessagePayload>() {
    @java.lang.Override
    public MessagePayload parsePartialFrom(
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

  public static com.google.protobuf.Parser<MessagePayload> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MessagePayload> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basecrdt.store.proto.MessagePayload getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

