// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inboxservice/InboxCoProc.proto

package com.zachary.bifromq.inbox.storage.proto;

/**
 * Protobuf type {@code inboxservice.CreateParams}
 */
public final class CreateParams extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:inboxservice.CreateParams)
    CreateParamsOrBuilder {
private static final long serialVersionUID = 0L;
  // Use CreateParams.newBuilder() to construct.
  private CreateParams(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private CreateParams() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new CreateParams();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_CreateParams_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_CreateParams_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.inbox.storage.proto.CreateParams.class, com.zachary.bifromq.inbox.storage.proto.CreateParams.Builder.class);
  }

  public static final int EXPIRESECONDS_FIELD_NUMBER = 1;
  private long expireSeconds_ = 0L;
  /**
   * <code>uint64 expireSeconds = 1;</code>
   * @return The expireSeconds.
   */
  @java.lang.Override
  public long getExpireSeconds() {
    return expireSeconds_;
  }

  public static final int LIMIT_FIELD_NUMBER = 2;
  private int limit_ = 0;
  /**
   * <code>uint32 limit = 2;</code>
   * @return The limit.
   */
  @java.lang.Override
  public int getLimit() {
    return limit_;
  }

  public static final int DROPOLDEST_FIELD_NUMBER = 3;
  private boolean dropOldest_ = false;
  /**
   * <code>bool dropOldest = 3;</code>
   * @return The dropOldest.
   */
  @java.lang.Override
  public boolean getDropOldest() {
    return dropOldest_;
  }

  public static final int CLIENT_FIELD_NUMBER = 4;
  private com.zachary.bifromq.type.ClientInfo client_;
  /**
   * <pre>
   * the owner client
   * </pre>
   *
   * <code>.commontype.ClientInfo client = 4;</code>
   * @return Whether the client field is set.
   */
  @java.lang.Override
  public boolean hasClient() {
    return client_ != null;
  }
  /**
   * <pre>
   * the owner client
   * </pre>
   *
   * <code>.commontype.ClientInfo client = 4;</code>
   * @return The client.
   */
  @java.lang.Override
  public com.zachary.bifromq.type.ClientInfo getClient() {
    return client_ == null ? com.zachary.bifromq.type.ClientInfo.getDefaultInstance() : client_;
  }
  /**
   * <pre>
   * the owner client
   * </pre>
   *
   * <code>.commontype.ClientInfo client = 4;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.type.ClientInfoOrBuilder getClientOrBuilder() {
    return client_ == null ? com.zachary.bifromq.type.ClientInfo.getDefaultInstance() : client_;
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
    if (expireSeconds_ != 0L) {
      output.writeUInt64(1, expireSeconds_);
    }
    if (limit_ != 0) {
      output.writeUInt32(2, limit_);
    }
    if (dropOldest_ != false) {
      output.writeBool(3, dropOldest_);
    }
    if (client_ != null) {
      output.writeMessage(4, getClient());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (expireSeconds_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(1, expireSeconds_);
    }
    if (limit_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(2, limit_);
    }
    if (dropOldest_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(3, dropOldest_);
    }
    if (client_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, getClient());
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
    if (!(obj instanceof com.zachary.bifromq.inbox.storage.proto.CreateParams)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.inbox.storage.proto.CreateParams other = (com.zachary.bifromq.inbox.storage.proto.CreateParams) obj;

    if (getExpireSeconds()
        != other.getExpireSeconds()) return false;
    if (getLimit()
        != other.getLimit()) return false;
    if (getDropOldest()
        != other.getDropOldest()) return false;
    if (hasClient() != other.hasClient()) return false;
    if (hasClient()) {
      if (!getClient()
          .equals(other.getClient())) return false;
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
    hash = (37 * hash) + EXPIRESECONDS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getExpireSeconds());
    hash = (37 * hash) + LIMIT_FIELD_NUMBER;
    hash = (53 * hash) + getLimit();
    hash = (37 * hash) + DROPOLDEST_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getDropOldest());
    if (hasClient()) {
      hash = (37 * hash) + CLIENT_FIELD_NUMBER;
      hash = (53 * hash) + getClient().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.inbox.storage.proto.CreateParams parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.inbox.storage.proto.CreateParams prototype) {
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
   * Protobuf type {@code inboxservice.CreateParams}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:inboxservice.CreateParams)
      com.zachary.bifromq.inbox.storage.proto.CreateParamsOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_CreateParams_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_CreateParams_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.inbox.storage.proto.CreateParams.class, com.zachary.bifromq.inbox.storage.proto.CreateParams.Builder.class);
    }

    // Construct using com.zachary.bifromq.inbox.storage.proto.CreateParams.newBuilder()
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
      expireSeconds_ = 0L;
      limit_ = 0;
      dropOldest_ = false;
      client_ = null;
      if (clientBuilder_ != null) {
        clientBuilder_.dispose();
        clientBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_CreateParams_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.inbox.storage.proto.CreateParams getDefaultInstanceForType() {
      return com.zachary.bifromq.inbox.storage.proto.CreateParams.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.inbox.storage.proto.CreateParams build() {
      com.zachary.bifromq.inbox.storage.proto.CreateParams result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.inbox.storage.proto.CreateParams buildPartial() {
      com.zachary.bifromq.inbox.storage.proto.CreateParams result = new com.zachary.bifromq.inbox.storage.proto.CreateParams(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.inbox.storage.proto.CreateParams result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.expireSeconds_ = expireSeconds_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.limit_ = limit_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.dropOldest_ = dropOldest_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.client_ = clientBuilder_ == null
            ? client_
            : clientBuilder_.build();
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
      if (other instanceof com.zachary.bifromq.inbox.storage.proto.CreateParams) {
        return mergeFrom((com.zachary.bifromq.inbox.storage.proto.CreateParams)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.inbox.storage.proto.CreateParams other) {
      if (other == com.zachary.bifromq.inbox.storage.proto.CreateParams.getDefaultInstance()) return this;
      if (other.getExpireSeconds() != 0L) {
        setExpireSeconds(other.getExpireSeconds());
      }
      if (other.getLimit() != 0) {
        setLimit(other.getLimit());
      }
      if (other.getDropOldest() != false) {
        setDropOldest(other.getDropOldest());
      }
      if (other.hasClient()) {
        mergeClient(other.getClient());
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
            case 8: {
              expireSeconds_ = input.readUInt64();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              limit_ = input.readUInt32();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              dropOldest_ = input.readBool();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 34: {
              input.readMessage(
                  getClientFieldBuilder().getBuilder(),
                  extensionRegistry);
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

    private long expireSeconds_ ;
    /**
     * <code>uint64 expireSeconds = 1;</code>
     * @return The expireSeconds.
     */
    @java.lang.Override
    public long getExpireSeconds() {
      return expireSeconds_;
    }
    /**
     * <code>uint64 expireSeconds = 1;</code>
     * @param value The expireSeconds to set.
     * @return This builder for chaining.
     */
    public Builder setExpireSeconds(long value) {
      
      expireSeconds_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>uint64 expireSeconds = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearExpireSeconds() {
      bitField0_ = (bitField0_ & ~0x00000001);
      expireSeconds_ = 0L;
      onChanged();
      return this;
    }

    private int limit_ ;
    /**
     * <code>uint32 limit = 2;</code>
     * @return The limit.
     */
    @java.lang.Override
    public int getLimit() {
      return limit_;
    }
    /**
     * <code>uint32 limit = 2;</code>
     * @param value The limit to set.
     * @return This builder for chaining.
     */
    public Builder setLimit(int value) {
      
      limit_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>uint32 limit = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearLimit() {
      bitField0_ = (bitField0_ & ~0x00000002);
      limit_ = 0;
      onChanged();
      return this;
    }

    private boolean dropOldest_ ;
    /**
     * <code>bool dropOldest = 3;</code>
     * @return The dropOldest.
     */
    @java.lang.Override
    public boolean getDropOldest() {
      return dropOldest_;
    }
    /**
     * <code>bool dropOldest = 3;</code>
     * @param value The dropOldest to set.
     * @return This builder for chaining.
     */
    public Builder setDropOldest(boolean value) {
      
      dropOldest_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>bool dropOldest = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearDropOldest() {
      bitField0_ = (bitField0_ & ~0x00000004);
      dropOldest_ = false;
      onChanged();
      return this;
    }

    private com.zachary.bifromq.type.ClientInfo client_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.type.ClientInfo, com.zachary.bifromq.type.ClientInfo.Builder, com.zachary.bifromq.type.ClientInfoOrBuilder> clientBuilder_;
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     * @return Whether the client field is set.
     */
    public boolean hasClient() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     * @return The client.
     */
    public com.zachary.bifromq.type.ClientInfo getClient() {
      if (clientBuilder_ == null) {
        return client_ == null ? com.zachary.bifromq.type.ClientInfo.getDefaultInstance() : client_;
      } else {
        return clientBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     */
    public Builder setClient(com.zachary.bifromq.type.ClientInfo value) {
      if (clientBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        client_ = value;
      } else {
        clientBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     */
    public Builder setClient(
        com.zachary.bifromq.type.ClientInfo.Builder builderForValue) {
      if (clientBuilder_ == null) {
        client_ = builderForValue.build();
      } else {
        clientBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     */
    public Builder mergeClient(com.zachary.bifromq.type.ClientInfo value) {
      if (clientBuilder_ == null) {
        if (((bitField0_ & 0x00000008) != 0) &&
          client_ != null &&
          client_ != com.zachary.bifromq.type.ClientInfo.getDefaultInstance()) {
          getClientBuilder().mergeFrom(value);
        } else {
          client_ = value;
        }
      } else {
        clientBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     */
    public Builder clearClient() {
      bitField0_ = (bitField0_ & ~0x00000008);
      client_ = null;
      if (clientBuilder_ != null) {
        clientBuilder_.dispose();
        clientBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     */
    public com.zachary.bifromq.type.ClientInfo.Builder getClientBuilder() {
      bitField0_ |= 0x00000008;
      onChanged();
      return getClientFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     */
    public com.zachary.bifromq.type.ClientInfoOrBuilder getClientOrBuilder() {
      if (clientBuilder_ != null) {
        return clientBuilder_.getMessageOrBuilder();
      } else {
        return client_ == null ?
            com.zachary.bifromq.type.ClientInfo.getDefaultInstance() : client_;
      }
    }
    /**
     * <pre>
     * the owner client
     * </pre>
     *
     * <code>.commontype.ClientInfo client = 4;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.type.ClientInfo, com.zachary.bifromq.type.ClientInfo.Builder, com.zachary.bifromq.type.ClientInfoOrBuilder> 
        getClientFieldBuilder() {
      if (clientBuilder_ == null) {
        clientBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.type.ClientInfo, com.zachary.bifromq.type.ClientInfo.Builder, com.zachary.bifromq.type.ClientInfoOrBuilder>(
                getClient(),
                getParentForChildren(),
                isClean());
        client_ = null;
      }
      return clientBuilder_;
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


    // @@protoc_insertion_point(builder_scope:inboxservice.CreateParams)
  }

  // @@protoc_insertion_point(class_scope:inboxservice.CreateParams)
  private static final com.zachary.bifromq.inbox.storage.proto.CreateParams DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.inbox.storage.proto.CreateParams();
  }

  public static com.zachary.bifromq.inbox.storage.proto.CreateParams getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<CreateParams>
      PARSER = new com.google.protobuf.AbstractParser<CreateParams>() {
    @java.lang.Override
    public CreateParams parsePartialFrom(
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

  public static com.google.protobuf.Parser<CreateParams> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<CreateParams> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.inbox.storage.proto.CreateParams getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

