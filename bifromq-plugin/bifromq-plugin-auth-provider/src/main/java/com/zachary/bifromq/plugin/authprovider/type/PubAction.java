// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mqtt_actions.proto

package com.zachary.bifromq.plugin.authprovider.type;

/**
 * Protobuf type {@code checktypes.PubAction}
 */
public final class PubAction extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:checktypes.PubAction)
    PubActionOrBuilder {
private static final long serialVersionUID = 0L;
  // Use PubAction.newBuilder() to construct.
  private PubAction(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private PubAction() {
    topic_ = "";
    qos_ = 0;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new PubAction();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_PubAction_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_PubAction_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.plugin.authprovider.type.PubAction.class, com.zachary.bifromq.plugin.authprovider.type.PubAction.Builder.class);
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

  public static final int QOS_FIELD_NUMBER = 2;
  private int qos_ = 0;
  /**
   * <code>.commontype.QoS qos = 2;</code>
   * @return The enum numeric value on the wire for qos.
   */
  @java.lang.Override public int getQosValue() {
    return qos_;
  }
  /**
   * <code>.commontype.QoS qos = 2;</code>
   * @return The qos.
   */
  @java.lang.Override public com.zachary.bifromq.type.QoS getQos() {
    com.zachary.bifromq.type.QoS result = com.zachary.bifromq.type.QoS.forNumber(qos_);
    return result == null ? com.zachary.bifromq.type.QoS.UNRECOGNIZED : result;
  }

  public static final int ISRETAINED_FIELD_NUMBER = 3;
  private boolean isRetained_ = false;
  /**
   * <code>bool isRetained = 3;</code>
   * @return The isRetained.
   */
  @java.lang.Override
  public boolean getIsRetained() {
    return isRetained_;
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
    if (qos_ != com.zachary.bifromq.type.QoS.AT_MOST_ONCE.getNumber()) {
      output.writeEnum(2, qos_);
    }
    if (isRetained_ != false) {
      output.writeBool(3, isRetained_);
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
    if (qos_ != com.zachary.bifromq.type.QoS.AT_MOST_ONCE.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(2, qos_);
    }
    if (isRetained_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(3, isRetained_);
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
    if (!(obj instanceof com.zachary.bifromq.plugin.authprovider.type.PubAction)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.plugin.authprovider.type.PubAction other = (com.zachary.bifromq.plugin.authprovider.type.PubAction) obj;

    if (!getTopic()
        .equals(other.getTopic())) return false;
    if (qos_ != other.qos_) return false;
    if (getIsRetained()
        != other.getIsRetained()) return false;
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
    hash = (37 * hash) + QOS_FIELD_NUMBER;
    hash = (53 * hash) + qos_;
    hash = (37 * hash) + ISRETAINED_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsRetained());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.PubAction parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.plugin.authprovider.type.PubAction prototype) {
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
   * Protobuf type {@code checktypes.PubAction}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:checktypes.PubAction)
      com.zachary.bifromq.plugin.authprovider.type.PubActionOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_PubAction_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_PubAction_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.plugin.authprovider.type.PubAction.class, com.zachary.bifromq.plugin.authprovider.type.PubAction.Builder.class);
    }

    // Construct using com.zachary.bifromq.plugin.authprovider.type.PubAction.newBuilder()
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
      qos_ = 0;
      isRetained_ = false;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_PubAction_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.plugin.authprovider.type.PubAction getDefaultInstanceForType() {
      return com.zachary.bifromq.plugin.authprovider.type.PubAction.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.plugin.authprovider.type.PubAction build() {
      com.zachary.bifromq.plugin.authprovider.type.PubAction result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.plugin.authprovider.type.PubAction buildPartial() {
      com.zachary.bifromq.plugin.authprovider.type.PubAction result = new com.zachary.bifromq.plugin.authprovider.type.PubAction(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.plugin.authprovider.type.PubAction result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.topic_ = topic_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.qos_ = qos_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.isRetained_ = isRetained_;
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
      if (other instanceof com.zachary.bifromq.plugin.authprovider.type.PubAction) {
        return mergeFrom((com.zachary.bifromq.plugin.authprovider.type.PubAction)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.plugin.authprovider.type.PubAction other) {
      if (other == com.zachary.bifromq.plugin.authprovider.type.PubAction.getDefaultInstance()) return this;
      if (!other.getTopic().isEmpty()) {
        topic_ = other.topic_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.qos_ != 0) {
        setQosValue(other.getQosValue());
      }
      if (other.getIsRetained() != false) {
        setIsRetained(other.getIsRetained());
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
            case 16: {
              qos_ = input.readEnum();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              isRetained_ = input.readBool();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
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

    private int qos_ = 0;
    /**
     * <code>.commontype.QoS qos = 2;</code>
     * @return The enum numeric value on the wire for qos.
     */
    @java.lang.Override public int getQosValue() {
      return qos_;
    }
    /**
     * <code>.commontype.QoS qos = 2;</code>
     * @param value The enum numeric value on the wire for qos to set.
     * @return This builder for chaining.
     */
    public Builder setQosValue(int value) {
      qos_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.QoS qos = 2;</code>
     * @return The qos.
     */
    @java.lang.Override
    public com.zachary.bifromq.type.QoS getQos() {
      com.zachary.bifromq.type.QoS result = com.zachary.bifromq.type.QoS.forNumber(qos_);
      return result == null ? com.zachary.bifromq.type.QoS.UNRECOGNIZED : result;
    }
    /**
     * <code>.commontype.QoS qos = 2;</code>
     * @param value The qos to set.
     * @return This builder for chaining.
     */
    public Builder setQos(com.zachary.bifromq.type.QoS value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000002;
      qos_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.QoS qos = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearQos() {
      bitField0_ = (bitField0_ & ~0x00000002);
      qos_ = 0;
      onChanged();
      return this;
    }

    private boolean isRetained_ ;
    /**
     * <code>bool isRetained = 3;</code>
     * @return The isRetained.
     */
    @java.lang.Override
    public boolean getIsRetained() {
      return isRetained_;
    }
    /**
     * <code>bool isRetained = 3;</code>
     * @param value The isRetained to set.
     * @return This builder for chaining.
     */
    public Builder setIsRetained(boolean value) {
      
      isRetained_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>bool isRetained = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearIsRetained() {
      bitField0_ = (bitField0_ & ~0x00000004);
      isRetained_ = false;
      onChanged();
      return this;
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


    // @@protoc_insertion_point(builder_scope:checktypes.PubAction)
  }

  // @@protoc_insertion_point(class_scope:checktypes.PubAction)
  private static final com.zachary.bifromq.plugin.authprovider.type.PubAction DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.plugin.authprovider.type.PubAction();
  }

  public static com.zachary.bifromq.plugin.authprovider.type.PubAction getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<PubAction>
      PARSER = new com.google.protobuf.AbstractParser<PubAction>() {
    @java.lang.Override
    public PubAction parsePartialFrom(
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

  public static com.google.protobuf.Parser<PubAction> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<PubAction> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.plugin.authprovider.type.PubAction getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
