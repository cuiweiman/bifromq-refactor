// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: commontype/SubInfo.proto

package com.zachary.bifromq.type;

/**
 * Protobuf type {@code commontype.SubInfo}
 */
public final class SubInfo extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:commontype.SubInfo)
    SubInfoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SubInfo.newBuilder() to construct.
  private SubInfo(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SubInfo() {
    tenantId_ = "";
    inboxId_ = "";
    subQoS_ = 0;
    topicFilter_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new SubInfo();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.type.SubInfoProtos.internal_static_commontype_SubInfo_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.type.SubInfoProtos.internal_static_commontype_SubInfo_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.type.SubInfo.class, com.zachary.bifromq.type.SubInfo.Builder.class);
  }

  public static final int TENANTID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object tenantId_ = "";
  /**
   * <code>string tenantId = 1;</code>
   * @return The tenantId.
   */
  @java.lang.Override
  public java.lang.String getTenantId() {
    java.lang.Object ref = tenantId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      tenantId_ = s;
      return s;
    }
  }
  /**
   * <code>string tenantId = 1;</code>
   * @return The bytes for tenantId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getTenantIdBytes() {
    java.lang.Object ref = tenantId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      tenantId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int INBOXID_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile java.lang.Object inboxId_ = "";
  /**
   * <code>string inboxId = 2;</code>
   * @return The inboxId.
   */
  @java.lang.Override
  public java.lang.String getInboxId() {
    java.lang.Object ref = inboxId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      inboxId_ = s;
      return s;
    }
  }
  /**
   * <code>string inboxId = 2;</code>
   * @return The bytes for inboxId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getInboxIdBytes() {
    java.lang.Object ref = inboxId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      inboxId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SUBQOS_FIELD_NUMBER = 3;
  private int subQoS_ = 0;
  /**
   * <code>.commontype.QoS subQoS = 3;</code>
   * @return The enum numeric value on the wire for subQoS.
   */
  @java.lang.Override public int getSubQoSValue() {
    return subQoS_;
  }
  /**
   * <code>.commontype.QoS subQoS = 3;</code>
   * @return The subQoS.
   */
  @java.lang.Override public com.zachary.bifromq.type.QoS getSubQoS() {
    com.zachary.bifromq.type.QoS result = com.zachary.bifromq.type.QoS.forNumber(subQoS_);
    return result == null ? com.zachary.bifromq.type.QoS.UNRECOGNIZED : result;
  }

  public static final int TOPICFILTER_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private volatile java.lang.Object topicFilter_ = "";
  /**
   * <code>string topicFilter = 4;</code>
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
   * <code>string topicFilter = 4;</code>
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(tenantId_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, tenantId_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(inboxId_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, inboxId_);
    }
    if (subQoS_ != com.zachary.bifromq.type.QoS.AT_MOST_ONCE.getNumber()) {
      output.writeEnum(3, subQoS_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(topicFilter_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, topicFilter_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(tenantId_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, tenantId_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(inboxId_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, inboxId_);
    }
    if (subQoS_ != com.zachary.bifromq.type.QoS.AT_MOST_ONCE.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(3, subQoS_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(topicFilter_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, topicFilter_);
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
    if (!(obj instanceof com.zachary.bifromq.type.SubInfo)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.type.SubInfo other = (com.zachary.bifromq.type.SubInfo) obj;

    if (!getTenantId()
        .equals(other.getTenantId())) return false;
    if (!getInboxId()
        .equals(other.getInboxId())) return false;
    if (subQoS_ != other.subQoS_) return false;
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
    hash = (37 * hash) + TENANTID_FIELD_NUMBER;
    hash = (53 * hash) + getTenantId().hashCode();
    hash = (37 * hash) + INBOXID_FIELD_NUMBER;
    hash = (53 * hash) + getInboxId().hashCode();
    hash = (37 * hash) + SUBQOS_FIELD_NUMBER;
    hash = (53 * hash) + subQoS_;
    hash = (37 * hash) + TOPICFILTER_FIELD_NUMBER;
    hash = (53 * hash) + getTopicFilter().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.type.SubInfo parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.type.SubInfo parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.SubInfo parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.SubInfo parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.type.SubInfo prototype) {
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
   * Protobuf type {@code commontype.SubInfo}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:commontype.SubInfo)
      com.zachary.bifromq.type.SubInfoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.type.SubInfoProtos.internal_static_commontype_SubInfo_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.type.SubInfoProtos.internal_static_commontype_SubInfo_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.type.SubInfo.class, com.zachary.bifromq.type.SubInfo.Builder.class);
    }

    // Construct using com.zachary.bifromq.type.SubInfo.newBuilder()
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
      tenantId_ = "";
      inboxId_ = "";
      subQoS_ = 0;
      topicFilter_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.type.SubInfoProtos.internal_static_commontype_SubInfo_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.type.SubInfo getDefaultInstanceForType() {
      return com.zachary.bifromq.type.SubInfo.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.type.SubInfo build() {
      com.zachary.bifromq.type.SubInfo result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.type.SubInfo buildPartial() {
      com.zachary.bifromq.type.SubInfo result = new com.zachary.bifromq.type.SubInfo(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.type.SubInfo result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.tenantId_ = tenantId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.inboxId_ = inboxId_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.subQoS_ = subQoS_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
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
      if (other instanceof com.zachary.bifromq.type.SubInfo) {
        return mergeFrom((com.zachary.bifromq.type.SubInfo)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.type.SubInfo other) {
      if (other == com.zachary.bifromq.type.SubInfo.getDefaultInstance()) return this;
      if (!other.getTenantId().isEmpty()) {
        tenantId_ = other.tenantId_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (!other.getInboxId().isEmpty()) {
        inboxId_ = other.inboxId_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      if (other.subQoS_ != 0) {
        setSubQoSValue(other.getSubQoSValue());
      }
      if (!other.getTopicFilter().isEmpty()) {
        topicFilter_ = other.topicFilter_;
        bitField0_ |= 0x00000008;
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
              tenantId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              inboxId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 24: {
              subQoS_ = input.readEnum();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 34: {
              topicFilter_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000008;
              break;
            } // case 34
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

    private java.lang.Object tenantId_ = "";
    /**
     * <code>string tenantId = 1;</code>
     * @return The tenantId.
     */
    public java.lang.String getTenantId() {
      java.lang.Object ref = tenantId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        tenantId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string tenantId = 1;</code>
     * @return The bytes for tenantId.
     */
    public com.google.protobuf.ByteString
        getTenantIdBytes() {
      java.lang.Object ref = tenantId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        tenantId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string tenantId = 1;</code>
     * @param value The tenantId to set.
     * @return This builder for chaining.
     */
    public Builder setTenantId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      tenantId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string tenantId = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearTenantId() {
      tenantId_ = getDefaultInstance().getTenantId();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string tenantId = 1;</code>
     * @param value The bytes for tenantId to set.
     * @return This builder for chaining.
     */
    public Builder setTenantIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      tenantId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private java.lang.Object inboxId_ = "";
    /**
     * <code>string inboxId = 2;</code>
     * @return The inboxId.
     */
    public java.lang.String getInboxId() {
      java.lang.Object ref = inboxId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        inboxId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string inboxId = 2;</code>
     * @return The bytes for inboxId.
     */
    public com.google.protobuf.ByteString
        getInboxIdBytes() {
      java.lang.Object ref = inboxId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        inboxId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string inboxId = 2;</code>
     * @param value The inboxId to set.
     * @return This builder for chaining.
     */
    public Builder setInboxId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      inboxId_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>string inboxId = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearInboxId() {
      inboxId_ = getDefaultInstance().getInboxId();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <code>string inboxId = 2;</code>
     * @param value The bytes for inboxId to set.
     * @return This builder for chaining.
     */
    public Builder setInboxIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      inboxId_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private int subQoS_ = 0;
    /**
     * <code>.commontype.QoS subQoS = 3;</code>
     * @return The enum numeric value on the wire for subQoS.
     */
    @java.lang.Override public int getSubQoSValue() {
      return subQoS_;
    }
    /**
     * <code>.commontype.QoS subQoS = 3;</code>
     * @param value The enum numeric value on the wire for subQoS to set.
     * @return This builder for chaining.
     */
    public Builder setSubQoSValue(int value) {
      subQoS_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.QoS subQoS = 3;</code>
     * @return The subQoS.
     */
    @java.lang.Override
    public com.zachary.bifromq.type.QoS getSubQoS() {
      com.zachary.bifromq.type.QoS result = com.zachary.bifromq.type.QoS.forNumber(subQoS_);
      return result == null ? com.zachary.bifromq.type.QoS.UNRECOGNIZED : result;
    }
    /**
     * <code>.commontype.QoS subQoS = 3;</code>
     * @param value The subQoS to set.
     * @return This builder for chaining.
     */
    public Builder setSubQoS(com.zachary.bifromq.type.QoS value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000004;
      subQoS_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.commontype.QoS subQoS = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearSubQoS() {
      bitField0_ = (bitField0_ & ~0x00000004);
      subQoS_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object topicFilter_ = "";
    /**
     * <code>string topicFilter = 4;</code>
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
     * <code>string topicFilter = 4;</code>
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
     * <code>string topicFilter = 4;</code>
     * @param value The topicFilter to set.
     * @return This builder for chaining.
     */
    public Builder setTopicFilter(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      topicFilter_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>string topicFilter = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearTopicFilter() {
      topicFilter_ = getDefaultInstance().getTopicFilter();
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    /**
     * <code>string topicFilter = 4;</code>
     * @param value The bytes for topicFilter to set.
     * @return This builder for chaining.
     */
    public Builder setTopicFilterBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      topicFilter_ = value;
      bitField0_ |= 0x00000008;
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


    // @@protoc_insertion_point(builder_scope:commontype.SubInfo)
  }

  // @@protoc_insertion_point(class_scope:commontype.SubInfo)
  private static final com.zachary.bifromq.type.SubInfo DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.type.SubInfo();
  }

  public static com.zachary.bifromq.type.SubInfo getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SubInfo>
      PARSER = new com.google.protobuf.AbstractParser<SubInfo>() {
    @java.lang.Override
    public SubInfo parsePartialFrom(
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

  public static com.google.protobuf.Parser<SubInfo> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SubInfo> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.type.SubInfo getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

