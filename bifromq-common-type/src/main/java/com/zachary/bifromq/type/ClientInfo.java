// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: commontype/ClientInfo.proto

package com.zachary.bifromq.type;

/**
 * Protobuf type {@code commontype.ClientInfo}
 */
public final class ClientInfo extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:commontype.ClientInfo)
    ClientInfoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ClientInfo.newBuilder() to construct.
  private ClientInfo(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ClientInfo() {
    tenantId_ = "";
    type_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ClientInfo();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.type.ClientInfoProto.internal_static_commontype_ClientInfo_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  @java.lang.Override
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 3:
        return internalGetMetadata();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.type.ClientInfoProto.internal_static_commontype_ClientInfo_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.type.ClientInfo.class, com.zachary.bifromq.type.ClientInfo.Builder.class);
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

  public static final int TYPE_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile java.lang.Object type_ = "";
  /**
   * <pre>
   * the type of the calling client could be external user client or internal service
   * </pre>
   *
   * <code>string type = 2;</code>
   * @return The type.
   */
  @java.lang.Override
  public java.lang.String getType() {
    java.lang.Object ref = type_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      type_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * the type of the calling client could be external user client or internal service
   * </pre>
   *
   * <code>string type = 2;</code>
   * @return The bytes for type.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getTypeBytes() {
    java.lang.Object ref = type_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      type_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int METADATA_FIELD_NUMBER = 3;
  private static final class MetadataDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        java.lang.String, java.lang.String> defaultEntry =
            com.google.protobuf.MapEntry
            .<java.lang.String, java.lang.String>newDefaultInstance(
                com.zachary.bifromq.type.ClientInfoProto.internal_static_commontype_ClientInfo_MetadataEntry_descriptor, 
                com.google.protobuf.WireFormat.FieldType.STRING,
                "",
                com.google.protobuf.WireFormat.FieldType.STRING,
                "");
  }
  @SuppressWarnings("serial")
  private com.google.protobuf.MapField<
      java.lang.String, java.lang.String> metadata_;
  private com.google.protobuf.MapField<java.lang.String, java.lang.String>
  internalGetMetadata() {
    if (metadata_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          MetadataDefaultEntryHolder.defaultEntry);
    }
    return metadata_;
  }
  public int getMetadataCount() {
    return internalGetMetadata().getMap().size();
  }
  /**
   * <pre>
   * the metadata of the client
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 3;</code>
   */
  @java.lang.Override
  public boolean containsMetadata(
      java.lang.String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    return internalGetMetadata().getMap().containsKey(key);
  }
  /**
   * Use {@link #getMetadataMap()} instead.
   */
  @java.lang.Override
  @java.lang.Deprecated
  public java.util.Map<java.lang.String, java.lang.String> getMetadata() {
    return getMetadataMap();
  }
  /**
   * <pre>
   * the metadata of the client
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 3;</code>
   */
  @java.lang.Override
  public java.util.Map<java.lang.String, java.lang.String> getMetadataMap() {
    return internalGetMetadata().getMap();
  }
  /**
   * <pre>
   * the metadata of the client
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 3;</code>
   */
  @java.lang.Override
  public /* nullable */
java.lang.String getMetadataOrDefault(
      java.lang.String key,
      /* nullable */
java.lang.String defaultValue) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetMetadata().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <pre>
   * the metadata of the client
   * </pre>
   *
   * <code>map&lt;string, string&gt; metadata = 3;</code>
   */
  @java.lang.Override
  public java.lang.String getMetadataOrThrow(
      java.lang.String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<java.lang.String, java.lang.String> map =
        internalGetMetadata().getMap();
    if (!map.containsKey(key)) {
      throw new java.lang.IllegalArgumentException();
    }
    return map.get(key);
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(type_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, type_);
    }
    com.google.protobuf.GeneratedMessageV3
      .serializeStringMapTo(
        output,
        internalGetMetadata(),
        MetadataDefaultEntryHolder.defaultEntry,
        3);
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(type_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, type_);
    }
    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry
         : internalGetMetadata().getMap().entrySet()) {
      com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
      metadata__ = MetadataDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, metadata__);
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
    if (!(obj instanceof com.zachary.bifromq.type.ClientInfo)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.type.ClientInfo other = (com.zachary.bifromq.type.ClientInfo) obj;

    if (!getTenantId()
        .equals(other.getTenantId())) return false;
    if (!getType()
        .equals(other.getType())) return false;
    if (!internalGetMetadata().equals(
        other.internalGetMetadata())) return false;
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
    hash = (37 * hash) + TYPE_FIELD_NUMBER;
    hash = (53 * hash) + getType().hashCode();
    if (!internalGetMetadata().getMap().isEmpty()) {
      hash = (37 * hash) + METADATA_FIELD_NUMBER;
      hash = (53 * hash) + internalGetMetadata().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.type.ClientInfo parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.type.ClientInfo parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.ClientInfo parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.type.ClientInfo parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.type.ClientInfo prototype) {
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
   * Protobuf type {@code commontype.ClientInfo}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:commontype.ClientInfo)
      com.zachary.bifromq.type.ClientInfoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.type.ClientInfoProto.internal_static_commontype_ClientInfo_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 3:
          return internalGetMetadata();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMutableMapField(
        int number) {
      switch (number) {
        case 3:
          return internalGetMutableMetadata();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.type.ClientInfoProto.internal_static_commontype_ClientInfo_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.type.ClientInfo.class, com.zachary.bifromq.type.ClientInfo.Builder.class);
    }

    // Construct using com.zachary.bifromq.type.ClientInfo.newBuilder()
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
      type_ = "";
      internalGetMutableMetadata().clear();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.type.ClientInfoProto.internal_static_commontype_ClientInfo_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.type.ClientInfo getDefaultInstanceForType() {
      return com.zachary.bifromq.type.ClientInfo.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.type.ClientInfo build() {
      com.zachary.bifromq.type.ClientInfo result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.type.ClientInfo buildPartial() {
      com.zachary.bifromq.type.ClientInfo result = new com.zachary.bifromq.type.ClientInfo(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.type.ClientInfo result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.tenantId_ = tenantId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.type_ = type_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.metadata_ = internalGetMetadata();
        result.metadata_.makeImmutable();
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
      if (other instanceof com.zachary.bifromq.type.ClientInfo) {
        return mergeFrom((com.zachary.bifromq.type.ClientInfo)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.type.ClientInfo other) {
      if (other == com.zachary.bifromq.type.ClientInfo.getDefaultInstance()) return this;
      if (!other.getTenantId().isEmpty()) {
        tenantId_ = other.tenantId_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (!other.getType().isEmpty()) {
        type_ = other.type_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      internalGetMutableMetadata().mergeFrom(
          other.internalGetMetadata());
      bitField0_ |= 0x00000004;
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
              type_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              com.google.protobuf.MapEntry<java.lang.String, java.lang.String>
              metadata__ = input.readMessage(
                  MetadataDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
              internalGetMutableMetadata().getMutableMap().put(
                  metadata__.getKey(), metadata__.getValue());
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

    private java.lang.Object type_ = "";
    /**
     * <pre>
     * the type of the calling client could be external user client or internal service
     * </pre>
     *
     * <code>string type = 2;</code>
     * @return The type.
     */
    public java.lang.String getType() {
      java.lang.Object ref = type_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        type_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * the type of the calling client could be external user client or internal service
     * </pre>
     *
     * <code>string type = 2;</code>
     * @return The bytes for type.
     */
    public com.google.protobuf.ByteString
        getTypeBytes() {
      java.lang.Object ref = type_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        type_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * the type of the calling client could be external user client or internal service
     * </pre>
     *
     * <code>string type = 2;</code>
     * @param value The type to set.
     * @return This builder for chaining.
     */
    public Builder setType(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      type_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the type of the calling client could be external user client or internal service
     * </pre>
     *
     * <code>string type = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearType() {
      type_ = getDefaultInstance().getType();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the type of the calling client could be external user client or internal service
     * </pre>
     *
     * <code>string type = 2;</code>
     * @param value The bytes for type to set.
     * @return This builder for chaining.
     */
    public Builder setTypeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      type_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private com.google.protobuf.MapField<
        java.lang.String, java.lang.String> metadata_;
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
        internalGetMetadata() {
      if (metadata_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            MetadataDefaultEntryHolder.defaultEntry);
      }
      return metadata_;
    }
    private com.google.protobuf.MapField<java.lang.String, java.lang.String>
        internalGetMutableMetadata() {
      if (metadata_ == null) {
        metadata_ = com.google.protobuf.MapField.newMapField(
            MetadataDefaultEntryHolder.defaultEntry);
      }
      if (!metadata_.isMutable()) {
        metadata_ = metadata_.copy();
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return metadata_;
    }
    public int getMetadataCount() {
      return internalGetMetadata().getMap().size();
    }
    /**
     * <pre>
     * the metadata of the client
     * </pre>
     *
     * <code>map&lt;string, string&gt; metadata = 3;</code>
     */
    @java.lang.Override
    public boolean containsMetadata(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      return internalGetMetadata().getMap().containsKey(key);
    }
    /**
     * Use {@link #getMetadataMap()} instead.
     */
    @java.lang.Override
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String> getMetadata() {
      return getMetadataMap();
    }
    /**
     * <pre>
     * the metadata of the client
     * </pre>
     *
     * <code>map&lt;string, string&gt; metadata = 3;</code>
     */
    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getMetadataMap() {
      return internalGetMetadata().getMap();
    }
    /**
     * <pre>
     * the metadata of the client
     * </pre>
     *
     * <code>map&lt;string, string&gt; metadata = 3;</code>
     */
    @java.lang.Override
    public /* nullable */
java.lang.String getMetadataOrDefault(
        java.lang.String key,
        /* nullable */
java.lang.String defaultValue) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetMetadata().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <pre>
     * the metadata of the client
     * </pre>
     *
     * <code>map&lt;string, string&gt; metadata = 3;</code>
     */
    @java.lang.Override
    public java.lang.String getMetadataOrThrow(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<java.lang.String, java.lang.String> map =
          internalGetMetadata().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }
    public Builder clearMetadata() {
      bitField0_ = (bitField0_ & ~0x00000004);
      internalGetMutableMetadata().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <pre>
     * the metadata of the client
     * </pre>
     *
     * <code>map&lt;string, string&gt; metadata = 3;</code>
     */
    public Builder removeMetadata(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      internalGetMutableMetadata().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.String>
        getMutableMetadata() {
      bitField0_ |= 0x00000004;
      return internalGetMutableMetadata().getMutableMap();
    }
    /**
     * <pre>
     * the metadata of the client
     * </pre>
     *
     * <code>map&lt;string, string&gt; metadata = 3;</code>
     */
    public Builder putMetadata(
        java.lang.String key,
        java.lang.String value) {
      if (key == null) { throw new NullPointerException("map key"); }
      if (value == null) { throw new NullPointerException("map value"); }
      internalGetMutableMetadata().getMutableMap()
          .put(key, value);
      bitField0_ |= 0x00000004;
      return this;
    }
    /**
     * <pre>
     * the metadata of the client
     * </pre>
     *
     * <code>map&lt;string, string&gt; metadata = 3;</code>
     */
    public Builder putAllMetadata(
        java.util.Map<java.lang.String, java.lang.String> values) {
      internalGetMutableMetadata().getMutableMap()
          .putAll(values);
      bitField0_ |= 0x00000004;
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


    // @@protoc_insertion_point(builder_scope:commontype.ClientInfo)
  }

  // @@protoc_insertion_point(class_scope:commontype.ClientInfo)
  private static final com.zachary.bifromq.type.ClientInfo DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.type.ClientInfo();
  }

  public static com.zachary.bifromq.type.ClientInfo getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ClientInfo>
      PARSER = new com.google.protobuf.AbstractParser<ClientInfo>() {
    @java.lang.Override
    public ClientInfo parsePartialFrom(
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

  public static com.google.protobuf.Parser<ClientInfo> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ClientInfo> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.type.ClientInfo getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

