// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: mqtt_actions.proto

package com.zachary.bifromq.plugin.authprovider.type;

/**
 * Protobuf type {@code checktypes.UnsubAction}
 */
public final class UnsubAction extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:checktypes.UnsubAction)
    UnsubActionOrBuilder {
private static final long serialVersionUID = 0L;
  // Use UnsubAction.newBuilder() to construct.
  private UnsubAction(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private UnsubAction() {
    topicFilter_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new UnsubAction();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_UnsubAction_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_UnsubAction_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.plugin.authprovider.type.UnsubAction.class, com.zachary.bifromq.plugin.authprovider.type.UnsubAction.Builder.class);
  }

  public static final int TOPICFILTER_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object topicFilter_ = "";
  /**
   * <code>string topicFilter = 1;</code>
   * @return The topicFilter.
   */
  @java.lang.Override
  public java.lang.String getTopicFilter() {
    java.lang.Object ref = topicFilter_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      topicFilter_ = s;
      return s;
    }
  }
  /**
   * <code>string topicFilter = 1;</code>
   * @return The bytes for topicFilter.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getTopicFilterBytes() {
    java.lang.Object ref = topicFilter_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      topicFilter_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(topicFilter_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, topicFilter_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(topicFilter_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, topicFilter_);
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
    if (!(obj instanceof com.zachary.bifromq.plugin.authprovider.type.UnsubAction)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.plugin.authprovider.type.UnsubAction other = (com.zachary.bifromq.plugin.authprovider.type.UnsubAction) obj;

    if (!getTopicFilter()
        .equals(other.getTopicFilter())) return false;
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
    hash = (37 * hash) + TOPICFILTER_FIELD_NUMBER;
    hash = (53 * hash) + getTopicFilter().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.plugin.authprovider.type.UnsubAction prototype) {
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
   * Protobuf type {@code checktypes.UnsubAction}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:checktypes.UnsubAction)
      com.zachary.bifromq.plugin.authprovider.type.UnsubActionOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_UnsubAction_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_UnsubAction_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.plugin.authprovider.type.UnsubAction.class, com.zachary.bifromq.plugin.authprovider.type.UnsubAction.Builder.class);
    }

    // Construct using com.zachary.bifromq.plugin.authprovider.type.UnsubAction.newBuilder()
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
      topicFilter_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.plugin.authprovider.type.CheckTypesProto.internal_static_checktypes_UnsubAction_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.plugin.authprovider.type.UnsubAction getDefaultInstanceForType() {
      return com.zachary.bifromq.plugin.authprovider.type.UnsubAction.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.plugin.authprovider.type.UnsubAction build() {
      com.zachary.bifromq.plugin.authprovider.type.UnsubAction result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.plugin.authprovider.type.UnsubAction buildPartial() {
      com.zachary.bifromq.plugin.authprovider.type.UnsubAction result = new com.zachary.bifromq.plugin.authprovider.type.UnsubAction(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.plugin.authprovider.type.UnsubAction result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.topicFilter_ = topicFilter_;
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
      if (other instanceof com.zachary.bifromq.plugin.authprovider.type.UnsubAction) {
        return mergeFrom((com.zachary.bifromq.plugin.authprovider.type.UnsubAction)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.plugin.authprovider.type.UnsubAction other) {
      if (other == com.zachary.bifromq.plugin.authprovider.type.UnsubAction.getDefaultInstance()) return this;
      if (!other.getTopicFilter().isEmpty()) {
        topicFilter_ = other.topicFilter_;
        bitField0_ |= 0x00000001;
        onChanged();
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
              topicFilter_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
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

    private java.lang.Object topicFilter_ = "";
    /**
     * <code>string topicFilter = 1;</code>
     * @return The topicFilter.
     */
    public java.lang.String getTopicFilter() {
      java.lang.Object ref = topicFilter_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        topicFilter_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string topicFilter = 1;</code>
     * @return The bytes for topicFilter.
     */
    public com.google.protobuf.ByteString
        getTopicFilterBytes() {
      java.lang.Object ref = topicFilter_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        topicFilter_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string topicFilter = 1;</code>
     * @param value The topicFilter to set.
     * @return This builder for chaining.
     */
    public Builder setTopicFilter(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      topicFilter_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string topicFilter = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearTopicFilter() {
      topicFilter_ = getDefaultInstance().getTopicFilter();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string topicFilter = 1;</code>
     * @param value The bytes for topicFilter to set.
     * @return This builder for chaining.
     */
    public Builder setTopicFilterBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      topicFilter_ = value;
      bitField0_ |= 0x00000001;
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


    // @@protoc_insertion_point(builder_scope:checktypes.UnsubAction)
  }

  // @@protoc_insertion_point(class_scope:checktypes.UnsubAction)
  private static final com.zachary.bifromq.plugin.authprovider.type.UnsubAction DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.plugin.authprovider.type.UnsubAction();
  }

  public static com.zachary.bifromq.plugin.authprovider.type.UnsubAction getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<UnsubAction>
      PARSER = new com.google.protobuf.AbstractParser<UnsubAction>() {
    @java.lang.Override
    public UnsubAction parsePartialFrom(
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

  public static com.google.protobuf.Parser<UnsubAction> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<UnsubAction> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.plugin.authprovider.type.UnsubAction getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

