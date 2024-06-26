// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/messenger/MessengerMessage.proto

package com.zachary.bifromq.basecluster.messenger.proto;

/**
 * <pre>
 **
 *oneof 表示
 *MessengerMessage 类型只能是 DirectMessage 和 GossipMessage 中的 其中一种
 * </pre>
 *
 * Protobuf type {@code basecluster.messenger.MessengerMessage}
 */
public final class MessengerMessage extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basecluster.messenger.MessengerMessage)
    MessengerMessageOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MessengerMessage.newBuilder() to construct.
  private MessengerMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MessengerMessage() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new MessengerMessage();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basecluster.messenger.proto.MessengerMessageProtos.internal_static_basecluster_messenger_MessengerMessage_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basecluster.messenger.proto.MessengerMessageProtos.internal_static_basecluster_messenger_MessengerMessage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage.class, com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage.Builder.class);
  }

  private int messengerMessageTypeCase_ = 0;
  private java.lang.Object messengerMessageType_;
  public enum MessengerMessageTypeCase
      implements com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    DIRECT(1),
    GOSSIP(2),
    MESSENGERMESSAGETYPE_NOT_SET(0);
    private final int value;
    private MessengerMessageTypeCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static MessengerMessageTypeCase valueOf(int value) {
      return forNumber(value);
    }

    public static MessengerMessageTypeCase forNumber(int value) {
      switch (value) {
        case 1: return DIRECT;
        case 2: return GOSSIP;
        case 0: return MESSENGERMESSAGETYPE_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public MessengerMessageTypeCase
  getMessengerMessageTypeCase() {
    return MessengerMessageTypeCase.forNumber(
        messengerMessageTypeCase_);
  }

  public static final int DIRECT_FIELD_NUMBER = 1;
  /**
   * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
   * @return Whether the direct field is set.
   */
  @java.lang.Override
  public boolean hasDirect() {
    return messengerMessageTypeCase_ == 1;
  }
  /**
   * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
   * @return The direct.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.messenger.proto.DirectMessage getDirect() {
    if (messengerMessageTypeCase_ == 1) {
       return (com.zachary.bifromq.basecluster.messenger.proto.DirectMessage) messengerMessageType_;
    }
    return com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.getDefaultInstance();
  }
  /**
   * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.messenger.proto.DirectMessageOrBuilder getDirectOrBuilder() {
    if (messengerMessageTypeCase_ == 1) {
       return (com.zachary.bifromq.basecluster.messenger.proto.DirectMessage) messengerMessageType_;
    }
    return com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.getDefaultInstance();
  }

  public static final int GOSSIP_FIELD_NUMBER = 2;
  /**
   * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
   * @return Whether the gossip field is set.
   */
  @java.lang.Override
  public boolean hasGossip() {
    return messengerMessageTypeCase_ == 2;
  }
  /**
   * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
   * @return The gossip.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.messenger.proto.GossipMessage getGossip() {
    if (messengerMessageTypeCase_ == 2) {
       return (com.zachary.bifromq.basecluster.messenger.proto.GossipMessage) messengerMessageType_;
    }
    return com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.getDefaultInstance();
  }
  /**
   * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecluster.messenger.proto.GossipMessageOrBuilder getGossipOrBuilder() {
    if (messengerMessageTypeCase_ == 2) {
       return (com.zachary.bifromq.basecluster.messenger.proto.GossipMessage) messengerMessageType_;
    }
    return com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.getDefaultInstance();
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
    if (messengerMessageTypeCase_ == 1) {
      output.writeMessage(1, (com.zachary.bifromq.basecluster.messenger.proto.DirectMessage) messengerMessageType_);
    }
    if (messengerMessageTypeCase_ == 2) {
      output.writeMessage(2, (com.zachary.bifromq.basecluster.messenger.proto.GossipMessage) messengerMessageType_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (messengerMessageTypeCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (com.zachary.bifromq.basecluster.messenger.proto.DirectMessage) messengerMessageType_);
    }
    if (messengerMessageTypeCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (com.zachary.bifromq.basecluster.messenger.proto.GossipMessage) messengerMessageType_);
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
    if (!(obj instanceof com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage other = (com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage) obj;

    if (!getMessengerMessageTypeCase().equals(other.getMessengerMessageTypeCase())) return false;
    switch (messengerMessageTypeCase_) {
      case 1:
        if (!getDirect()
            .equals(other.getDirect())) return false;
        break;
      case 2:
        if (!getGossip()
            .equals(other.getGossip())) return false;
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
    switch (messengerMessageTypeCase_) {
      case 1:
        hash = (37 * hash) + DIRECT_FIELD_NUMBER;
        hash = (53 * hash) + getDirect().hashCode();
        break;
      case 2:
        hash = (37 * hash) + GOSSIP_FIELD_NUMBER;
        hash = (53 * hash) + getGossip().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage prototype) {
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
   **
   *oneof 表示
   *MessengerMessage 类型只能是 DirectMessage 和 GossipMessage 中的 其中一种
   * </pre>
   *
   * Protobuf type {@code basecluster.messenger.MessengerMessage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basecluster.messenger.MessengerMessage)
      com.zachary.bifromq.basecluster.messenger.proto.MessengerMessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basecluster.messenger.proto.MessengerMessageProtos.internal_static_basecluster_messenger_MessengerMessage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basecluster.messenger.proto.MessengerMessageProtos.internal_static_basecluster_messenger_MessengerMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage.class, com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage.Builder.class);
    }

    // Construct using com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage.newBuilder()
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
      if (directBuilder_ != null) {
        directBuilder_.clear();
      }
      if (gossipBuilder_ != null) {
        gossipBuilder_.clear();
      }
      messengerMessageTypeCase_ = 0;
      messengerMessageType_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basecluster.messenger.proto.MessengerMessageProtos.internal_static_basecluster_messenger_MessengerMessage_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage getDefaultInstanceForType() {
      return com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage build() {
      com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage buildPartial() {
      com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage result = new com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      buildPartialOneofs(result);
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage result) {
      int from_bitField0_ = bitField0_;
    }

    private void buildPartialOneofs(com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage result) {
      result.messengerMessageTypeCase_ = messengerMessageTypeCase_;
      result.messengerMessageType_ = this.messengerMessageType_;
      if (messengerMessageTypeCase_ == 1 &&
          directBuilder_ != null) {
        result.messengerMessageType_ = directBuilder_.build();
      }
      if (messengerMessageTypeCase_ == 2 &&
          gossipBuilder_ != null) {
        result.messengerMessageType_ = gossipBuilder_.build();
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
      if (other instanceof com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage) {
        return mergeFrom((com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage other) {
      if (other == com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage.getDefaultInstance()) return this;
      switch (other.getMessengerMessageTypeCase()) {
        case DIRECT: {
          mergeDirect(other.getDirect());
          break;
        }
        case GOSSIP: {
          mergeGossip(other.getGossip());
          break;
        }
        case MESSENGERMESSAGETYPE_NOT_SET: {
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
                  getDirectFieldBuilder().getBuilder(),
                  extensionRegistry);
              messengerMessageTypeCase_ = 1;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getGossipFieldBuilder().getBuilder(),
                  extensionRegistry);
              messengerMessageTypeCase_ = 2;
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
    private int messengerMessageTypeCase_ = 0;
    private java.lang.Object messengerMessageType_;
    public MessengerMessageTypeCase
        getMessengerMessageTypeCase() {
      return MessengerMessageTypeCase.forNumber(
          messengerMessageTypeCase_);
    }

    public Builder clearMessengerMessageType() {
      messengerMessageTypeCase_ = 0;
      messengerMessageType_ = null;
      onChanged();
      return this;
    }

    private int bitField0_;

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.messenger.proto.DirectMessage, com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.Builder, com.zachary.bifromq.basecluster.messenger.proto.DirectMessageOrBuilder> directBuilder_;
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     * @return Whether the direct field is set.
     */
    @java.lang.Override
    public boolean hasDirect() {
      return messengerMessageTypeCase_ == 1;
    }
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     * @return The direct.
     */
    @java.lang.Override
    public com.zachary.bifromq.basecluster.messenger.proto.DirectMessage getDirect() {
      if (directBuilder_ == null) {
        if (messengerMessageTypeCase_ == 1) {
          return (com.zachary.bifromq.basecluster.messenger.proto.DirectMessage) messengerMessageType_;
        }
        return com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.getDefaultInstance();
      } else {
        if (messengerMessageTypeCase_ == 1) {
          return directBuilder_.getMessage();
        }
        return com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.getDefaultInstance();
      }
    }
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     */
    public Builder setDirect(com.zachary.bifromq.basecluster.messenger.proto.DirectMessage value) {
      if (directBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        messengerMessageType_ = value;
        onChanged();
      } else {
        directBuilder_.setMessage(value);
      }
      messengerMessageTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     */
    public Builder setDirect(
        com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.Builder builderForValue) {
      if (directBuilder_ == null) {
        messengerMessageType_ = builderForValue.build();
        onChanged();
      } else {
        directBuilder_.setMessage(builderForValue.build());
      }
      messengerMessageTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     */
    public Builder mergeDirect(com.zachary.bifromq.basecluster.messenger.proto.DirectMessage value) {
      if (directBuilder_ == null) {
        if (messengerMessageTypeCase_ == 1 &&
            messengerMessageType_ != com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.getDefaultInstance()) {
          messengerMessageType_ = com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.newBuilder((com.zachary.bifromq.basecluster.messenger.proto.DirectMessage) messengerMessageType_)
              .mergeFrom(value).buildPartial();
        } else {
          messengerMessageType_ = value;
        }
        onChanged();
      } else {
        if (messengerMessageTypeCase_ == 1) {
          directBuilder_.mergeFrom(value);
        } else {
          directBuilder_.setMessage(value);
        }
      }
      messengerMessageTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     */
    public Builder clearDirect() {
      if (directBuilder_ == null) {
        if (messengerMessageTypeCase_ == 1) {
          messengerMessageTypeCase_ = 0;
          messengerMessageType_ = null;
          onChanged();
        }
      } else {
        if (messengerMessageTypeCase_ == 1) {
          messengerMessageTypeCase_ = 0;
          messengerMessageType_ = null;
        }
        directBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     */
    public com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.Builder getDirectBuilder() {
      return getDirectFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.basecluster.messenger.proto.DirectMessageOrBuilder getDirectOrBuilder() {
      if ((messengerMessageTypeCase_ == 1) && (directBuilder_ != null)) {
        return directBuilder_.getMessageOrBuilder();
      } else {
        if (messengerMessageTypeCase_ == 1) {
          return (com.zachary.bifromq.basecluster.messenger.proto.DirectMessage) messengerMessageType_;
        }
        return com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.getDefaultInstance();
      }
    }
    /**
     * <code>.basecluster.messenger.DirectMessage direct = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.messenger.proto.DirectMessage, com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.Builder, com.zachary.bifromq.basecluster.messenger.proto.DirectMessageOrBuilder> 
        getDirectFieldBuilder() {
      if (directBuilder_ == null) {
        if (!(messengerMessageTypeCase_ == 1)) {
          messengerMessageType_ = com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.getDefaultInstance();
        }
        directBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecluster.messenger.proto.DirectMessage, com.zachary.bifromq.basecluster.messenger.proto.DirectMessage.Builder, com.zachary.bifromq.basecluster.messenger.proto.DirectMessageOrBuilder>(
                (com.zachary.bifromq.basecluster.messenger.proto.DirectMessage) messengerMessageType_,
                getParentForChildren(),
                isClean());
        messengerMessageType_ = null;
      }
      messengerMessageTypeCase_ = 1;
      onChanged();
      return directBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.messenger.proto.GossipMessage, com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.Builder, com.zachary.bifromq.basecluster.messenger.proto.GossipMessageOrBuilder> gossipBuilder_;
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     * @return Whether the gossip field is set.
     */
    @java.lang.Override
    public boolean hasGossip() {
      return messengerMessageTypeCase_ == 2;
    }
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     * @return The gossip.
     */
    @java.lang.Override
    public com.zachary.bifromq.basecluster.messenger.proto.GossipMessage getGossip() {
      if (gossipBuilder_ == null) {
        if (messengerMessageTypeCase_ == 2) {
          return (com.zachary.bifromq.basecluster.messenger.proto.GossipMessage) messengerMessageType_;
        }
        return com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.getDefaultInstance();
      } else {
        if (messengerMessageTypeCase_ == 2) {
          return gossipBuilder_.getMessage();
        }
        return com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.getDefaultInstance();
      }
    }
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     */
    public Builder setGossip(com.zachary.bifromq.basecluster.messenger.proto.GossipMessage value) {
      if (gossipBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        messengerMessageType_ = value;
        onChanged();
      } else {
        gossipBuilder_.setMessage(value);
      }
      messengerMessageTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     */
    public Builder setGossip(
        com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.Builder builderForValue) {
      if (gossipBuilder_ == null) {
        messengerMessageType_ = builderForValue.build();
        onChanged();
      } else {
        gossipBuilder_.setMessage(builderForValue.build());
      }
      messengerMessageTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     */
    public Builder mergeGossip(com.zachary.bifromq.basecluster.messenger.proto.GossipMessage value) {
      if (gossipBuilder_ == null) {
        if (messengerMessageTypeCase_ == 2 &&
            messengerMessageType_ != com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.getDefaultInstance()) {
          messengerMessageType_ = com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.newBuilder((com.zachary.bifromq.basecluster.messenger.proto.GossipMessage) messengerMessageType_)
              .mergeFrom(value).buildPartial();
        } else {
          messengerMessageType_ = value;
        }
        onChanged();
      } else {
        if (messengerMessageTypeCase_ == 2) {
          gossipBuilder_.mergeFrom(value);
        } else {
          gossipBuilder_.setMessage(value);
        }
      }
      messengerMessageTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     */
    public Builder clearGossip() {
      if (gossipBuilder_ == null) {
        if (messengerMessageTypeCase_ == 2) {
          messengerMessageTypeCase_ = 0;
          messengerMessageType_ = null;
          onChanged();
        }
      } else {
        if (messengerMessageTypeCase_ == 2) {
          messengerMessageTypeCase_ = 0;
          messengerMessageType_ = null;
        }
        gossipBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     */
    public com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.Builder getGossipBuilder() {
      return getGossipFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.basecluster.messenger.proto.GossipMessageOrBuilder getGossipOrBuilder() {
      if ((messengerMessageTypeCase_ == 2) && (gossipBuilder_ != null)) {
        return gossipBuilder_.getMessageOrBuilder();
      } else {
        if (messengerMessageTypeCase_ == 2) {
          return (com.zachary.bifromq.basecluster.messenger.proto.GossipMessage) messengerMessageType_;
        }
        return com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.getDefaultInstance();
      }
    }
    /**
     * <code>.basecluster.messenger.GossipMessage gossip = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecluster.messenger.proto.GossipMessage, com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.Builder, com.zachary.bifromq.basecluster.messenger.proto.GossipMessageOrBuilder> 
        getGossipFieldBuilder() {
      if (gossipBuilder_ == null) {
        if (!(messengerMessageTypeCase_ == 2)) {
          messengerMessageType_ = com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.getDefaultInstance();
        }
        gossipBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecluster.messenger.proto.GossipMessage, com.zachary.bifromq.basecluster.messenger.proto.GossipMessage.Builder, com.zachary.bifromq.basecluster.messenger.proto.GossipMessageOrBuilder>(
                (com.zachary.bifromq.basecluster.messenger.proto.GossipMessage) messengerMessageType_,
                getParentForChildren(),
                isClean());
        messengerMessageType_ = null;
      }
      messengerMessageTypeCase_ = 2;
      onChanged();
      return gossipBuilder_;
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


    // @@protoc_insertion_point(builder_scope:basecluster.messenger.MessengerMessage)
  }

  // @@protoc_insertion_point(class_scope:basecluster.messenger.MessengerMessage)
  private static final com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage();
  }

  public static com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MessengerMessage>
      PARSER = new com.google.protobuf.AbstractParser<MessengerMessage>() {
    @java.lang.Override
    public MessengerMessage parsePartialFrom(
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

  public static com.google.protobuf.Parser<MessengerMessage> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MessengerMessage> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basecluster.messenger.proto.MessengerMessage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

