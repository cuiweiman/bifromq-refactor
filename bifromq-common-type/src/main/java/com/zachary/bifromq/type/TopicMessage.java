// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: commontype/TopicMessage.proto

package com.zachary.bifromq.type;

/**
 * Protobuf type {@code commontype.TopicMessage}
 */
public final class TopicMessage extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:commontype.TopicMessage)
    TopicMessageOrBuilder {
private static final long serialVersionUID = 0L;
  // Use TopicMessage.newBuilder() to construct.
  private TopicMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private TopicMessage() {
    topic_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new TopicMessage();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.type.TopicMessageProtos.internal_static_commontype_TopicMessage_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.type.TopicMessageProtos.internal_static_commontype_TopicMessage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.type.TopicMessage.class, com.zachary.bifromq.type.TopicMessage.Builder.class);
  }

  public static final int TOPIC_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object topic_ = "";
  /**
   * <code>string topic = 1;</code>
   * @return The topic.
   */
  @java.lang.Override
  public java.lang.String getTopic() {
    java.lang.Object ref = topic_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      topic_ = s;
      return s;
    }
  }
  /**
   * <code>string topic = 1;</code>
   * @return The bytes for topic.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getTopicBytes() {
    java.lang.Object ref = topic_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      topic_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int MESSAGE_FIELD_NUMBER = 2;
  private com.zachary.bifromq.type.Message message_;
  /**
   * <code>.commontype.Message message = 2;</code>
   * @return Whether the message field is set.
   */
  @java.lang.Override
  public boolean hasMessage() {
    return message_ != null;
  }
  /**
   * <code>.commontype.Message message = 2;</code>
   * @return The message.
   */
  @java.lang.Override
  public com.zachary.bifromq.type.Message getMessage() {
    return message_ == null ? com.zachary.bifromq.type.Message.getDefaultInstance() : message_;
  }
  /**
   * <code>.commontype.Message message = 2;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.type.MessageOrBuilder getMessageOrBuilder() {
    return message_ == null ? com.zachary.bifromq.type.Message.getDefaultInstance() : message_;
  }

  public static final int PUBLISHER_FIELD_NUMBER = 3;
  private com.zachary.bifromq.type.ClientInfo publisher_;
  /**
   * <code>.commontype.ClientInfo publisher = 3;</code>
   * @return Whether the publisher field is set.
   */
  @java.lang.Override
  public boolean hasPublisher() {
    return publisher_ != null;
  }
  /**
   * <code>.commontype.ClientInfo publisher = 3;</code>
   * @return The publisher.
   */
  @java.lang.Override
  public com.zachary.bifromq.type.ClientInfo getPublisher() {
    return publisher_ == null ? com.zachary.bifromq.type.ClientInfo.getDefaultInstance() : publisher_;
  }
  /**
   * <code>.commontype.ClientInfo publisher = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.type.ClientInfoOrBuilder getPublisherOrBuilder() {
    return publisher_ == null ? com.zachary.bifromq.type.ClientInfo.getDefaultInstance() : publisher_;
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(topic_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, topic_);
    }
    if (message_ != null) {
      output.writeMessage(2, getMessage());
    }
    if (publisher_ != null) {
      output.writeMessage(3, getPublisher());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(topic_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, topic_);
    }
    if (message_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getMessage());
    }
    if (publisher_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getPublisher());
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
    if (!(obj instanceof com.zachary.bifromq.type.TopicMessage)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.type.TopicMessage other = (com.zachary.bifromq.type.TopicMessage) obj;

    if (!getTopic()
        .equals(other.getTopic())) return false;
    if (hasMessage() != other.hasMessage()) return false;
    if (hasMessage()) {
      if (!getMessage()
          .equals(other.getMessage())) return false;
    }
    if (hasPublisher() != other.hasPublisher()) return false;
    if (hasPublisher()) {
      if (!getPublisher()
          .equals(other.getPublisher())) return false;
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
    hash = (37 * hash) + TOPIC_FIELD_NUMBER;
    hash = (53 * hash) + getTopic().hashCode();
    if (hasMessage()) {
      hash = (37 * hash) + MESSAGE_FIELD_NUMBER;
      hash = (53 * hash) + getMessage().hashCode();
    }
    if (hasPublisher()) {
      hash = (37 * hash) + PUBLISHER_FIELD_NUMBER;
      hash = (53 * hash) + getPublisher().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.type.TopicMessage parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.type.TopicMessage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.TopicMessage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.TopicMessage parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.type.TopicMessage prototype) {
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
   * Protobuf type {@code commontype.TopicMessage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:commontype.TopicMessage)
      com.zachary.bifromq.type.TopicMessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.type.TopicMessageProtos.internal_static_commontype_TopicMessage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.type.TopicMessageProtos.internal_static_commontype_TopicMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.type.TopicMessage.class, com.zachary.bifromq.type.TopicMessage.Builder.class);
    }

    // Construct using com.zachary.bifromq.type.TopicMessage.newBuilder()
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
      topic_ = "";
      message_ = null;
      if (messageBuilder_ != null) {
        messageBuilder_.dispose();
        messageBuilder_ = null;
      }
      publisher_ = null;
      if (publisherBuilder_ != null) {
        publisherBuilder_.dispose();
        publisherBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.type.TopicMessageProtos.internal_static_commontype_TopicMessage_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.type.TopicMessage getDefaultInstanceForType() {
      return com.zachary.bifromq.type.TopicMessage.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.type.TopicMessage build() {
      com.zachary.bifromq.type.TopicMessage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.type.TopicMessage buildPartial() {
      com.zachary.bifromq.type.TopicMessage result = new com.zachary.bifromq.type.TopicMessage(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.type.TopicMessage result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.topic_ = topic_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.message_ = messageBuilder_ == null
            ? message_
            : messageBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.publisher_ = publisherBuilder_ == null
            ? publisher_
            : publisherBuilder_.build();
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
      if (other instanceof com.zachary.bifromq.type.TopicMessage) {
        return mergeFrom((com.zachary.bifromq.type.TopicMessage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.type.TopicMessage other) {
      if (other == com.zachary.bifromq.type.TopicMessage.getDefaultInstance()) return this;
      if (!other.getTopic().isEmpty()) {
        topic_ = other.topic_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.hasMessage()) {
        mergeMessage(other.getMessage());
      }
      if (other.hasPublisher()) {
        mergePublisher(other.getPublisher());
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
              topic_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getMessageFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              input.readMessage(
                  getPublisherFieldBuilder().getBuilder(),
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

    private java.lang.Object topic_ = "";
    /**
     * <code>string topic = 1;</code>
     * @return The topic.
     */
    public java.lang.String getTopic() {
      java.lang.Object ref = topic_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        topic_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string topic = 1;</code>
     * @return The bytes for topic.
     */
    public com.google.protobuf.ByteString
        getTopicBytes() {
      java.lang.Object ref = topic_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        topic_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string topic = 1;</code>
     * @param value The topic to set.
     * @return This builder for chaining.
     */
    public Builder setTopic(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      topic_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string topic = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearTopic() {
      topic_ = getDefaultInstance().getTopic();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string topic = 1;</code>
     * @param value The bytes for topic to set.
     * @return This builder for chaining.
     */
    public Builder setTopicBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      topic_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private com.zachary.bifromq.type.Message message_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.type.Message, com.zachary.bifromq.type.Message.Builder, com.zachary.bifromq.type.MessageOrBuilder> messageBuilder_;
    /**
     * <code>.commontype.Message message = 2;</code>
     * @return Whether the message field is set.
     */
    public boolean hasMessage() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>.commontype.Message message = 2;</code>
     * @return The message.
     */
    public com.zachary.bifromq.type.Message getMessage() {
      if (messageBuilder_ == null) {
        return message_ == null ? com.zachary.bifromq.type.Message.getDefaultInstance() : message_;
      } else {
        return messageBuilder_.getMessage();
      }
    }
    /**
     * <code>.commontype.Message message = 2;</code>
     */
    public Builder setMessage(com.zachary.bifromq.type.Message value) {
      if (messageBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        message_ = value;
      } else {
        messageBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.Message message = 2;</code>
     */
    public Builder setMessage(
        com.zachary.bifromq.type.Message.Builder builderForValue) {
      if (messageBuilder_ == null) {
        message_ = builderForValue.build();
      } else {
        messageBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.Message message = 2;</code>
     */
    public Builder mergeMessage(com.zachary.bifromq.type.Message value) {
      if (messageBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0) &&
          message_ != null &&
          message_ != com.zachary.bifromq.type.Message.getDefaultInstance()) {
          getMessageBuilder().mergeFrom(value);
        } else {
          message_ = value;
        }
      } else {
        messageBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.Message message = 2;</code>
     */
    public Builder clearMessage() {
      bitField0_ = (bitField0_ & ~0x00000002);
      message_ = null;
      if (messageBuilder_ != null) {
        messageBuilder_.dispose();
        messageBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.Message message = 2;</code>
     */
    public com.zachary.bifromq.type.Message.Builder getMessageBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getMessageFieldBuilder().getBuilder();
    }
    /**
     * <code>.commontype.Message message = 2;</code>
     */
    public com.zachary.bifromq.type.MessageOrBuilder getMessageOrBuilder() {
      if (messageBuilder_ != null) {
        return messageBuilder_.getMessageOrBuilder();
      } else {
        return message_ == null ?
            com.zachary.bifromq.type.Message.getDefaultInstance() : message_;
      }
    }
    /**
     * <code>.commontype.Message message = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.type.Message, com.zachary.bifromq.type.Message.Builder, com.zachary.bifromq.type.MessageOrBuilder> 
        getMessageFieldBuilder() {
      if (messageBuilder_ == null) {
        messageBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.type.Message, com.zachary.bifromq.type.Message.Builder, com.zachary.bifromq.type.MessageOrBuilder>(
                getMessage(),
                getParentForChildren(),
                isClean());
        message_ = null;
      }
      return messageBuilder_;
    }

    private com.zachary.bifromq.type.ClientInfo publisher_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.type.ClientInfo, com.zachary.bifromq.type.ClientInfo.Builder, com.zachary.bifromq.type.ClientInfoOrBuilder> publisherBuilder_;
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     * @return Whether the publisher field is set.
     */
    public boolean hasPublisher() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     * @return The publisher.
     */
    public com.zachary.bifromq.type.ClientInfo getPublisher() {
      if (publisherBuilder_ == null) {
        return publisher_ == null ? com.zachary.bifromq.type.ClientInfo.getDefaultInstance() : publisher_;
      } else {
        return publisherBuilder_.getMessage();
      }
    }
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     */
    public Builder setPublisher(com.zachary.bifromq.type.ClientInfo value) {
      if (publisherBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        publisher_ = value;
      } else {
        publisherBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     */
    public Builder setPublisher(
        com.zachary.bifromq.type.ClientInfo.Builder builderForValue) {
      if (publisherBuilder_ == null) {
        publisher_ = builderForValue.build();
      } else {
        publisherBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     */
    public Builder mergePublisher(com.zachary.bifromq.type.ClientInfo value) {
      if (publisherBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0) &&
          publisher_ != null &&
          publisher_ != com.zachary.bifromq.type.ClientInfo.getDefaultInstance()) {
          getPublisherBuilder().mergeFrom(value);
        } else {
          publisher_ = value;
        }
      } else {
        publisherBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     */
    public Builder clearPublisher() {
      bitField0_ = (bitField0_ & ~0x00000004);
      publisher_ = null;
      if (publisherBuilder_ != null) {
        publisherBuilder_.dispose();
        publisherBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     */
    public com.zachary.bifromq.type.ClientInfo.Builder getPublisherBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getPublisherFieldBuilder().getBuilder();
    }
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     */
    public com.zachary.bifromq.type.ClientInfoOrBuilder getPublisherOrBuilder() {
      if (publisherBuilder_ != null) {
        return publisherBuilder_.getMessageOrBuilder();
      } else {
        return publisher_ == null ?
            com.zachary.bifromq.type.ClientInfo.getDefaultInstance() : publisher_;
      }
    }
    /**
     * <code>.commontype.ClientInfo publisher = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.type.ClientInfo, com.zachary.bifromq.type.ClientInfo.Builder, com.zachary.bifromq.type.ClientInfoOrBuilder> 
        getPublisherFieldBuilder() {
      if (publisherBuilder_ == null) {
        publisherBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.type.ClientInfo, com.zachary.bifromq.type.ClientInfo.Builder, com.zachary.bifromq.type.ClientInfoOrBuilder>(
                getPublisher(),
                getParentForChildren(),
                isClean());
        publisher_ = null;
      }
      return publisherBuilder_;
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


    // @@protoc_insertion_point(builder_scope:commontype.TopicMessage)
  }

  // @@protoc_insertion_point(class_scope:commontype.TopicMessage)
  private static final com.zachary.bifromq.type.TopicMessage DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.type.TopicMessage();
  }

  public static com.zachary.bifromq.type.TopicMessage getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<TopicMessage>
      PARSER = new com.google.protobuf.AbstractParser<TopicMessage>() {
    @java.lang.Override
    public TopicMessage parsePartialFrom(
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

  public static com.google.protobuf.Parser<TopicMessage> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<TopicMessage> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.type.TopicMessage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

