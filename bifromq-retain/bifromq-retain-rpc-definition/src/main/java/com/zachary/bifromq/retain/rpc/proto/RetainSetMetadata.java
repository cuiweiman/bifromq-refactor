// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: retainservice/RetainCoProc.proto

package com.zachary.bifromq.retain.rpc.proto;

/**
 * Protobuf type {@code retainservice.RetainSetMetadata}
 */
public final class RetainSetMetadata extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:retainservice.RetainSetMetadata)
    RetainSetMetadataOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RetainSetMetadata.newBuilder() to construct.
  private RetainSetMetadata(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RetainSetMetadata() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new RetainSetMetadata();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_RetainSetMetadata_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_RetainSetMetadata_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata.class, com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata.Builder.class);
  }

  public static final int COUNT_FIELD_NUMBER = 1;
  private int count_ = 0;
  /**
   * <code>uint32 count = 1;</code>
   * @return The count.
   */
  @java.lang.Override
  public int getCount() {
    return count_;
  }

  public static final int ESTEXPIRE_FIELD_NUMBER = 2;
  private long estExpire_ = 0L;
  /**
   * <code>uint64 estExpire = 2;</code>
   * @return The estExpire.
   */
  @java.lang.Override
  public long getEstExpire() {
    return estExpire_;
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
    if (count_ != 0) {
      output.writeUInt32(1, count_);
    }
    if (estExpire_ != 0L) {
      output.writeUInt64(2, estExpire_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (count_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(1, count_);
    }
    if (estExpire_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(2, estExpire_);
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
    if (!(obj instanceof com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata other = (com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata) obj;

    if (getCount()
        != other.getCount()) return false;
    if (getEstExpire()
        != other.getEstExpire()) return false;
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
    hash = (37 * hash) + COUNT_FIELD_NUMBER;
    hash = (53 * hash) + getCount();
    hash = (37 * hash) + ESTEXPIRE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getEstExpire());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata prototype) {
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
   * Protobuf type {@code retainservice.RetainSetMetadata}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:retainservice.RetainSetMetadata)
      com.zachary.bifromq.retain.rpc.proto.RetainSetMetadataOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_RetainSetMetadata_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_RetainSetMetadata_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata.class, com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata.Builder.class);
    }

    // Construct using com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata.newBuilder()
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
      count_ = 0;
      estExpire_ = 0L;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_RetainSetMetadata_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata getDefaultInstanceForType() {
      return com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata build() {
      com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata buildPartial() {
      com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata result = new com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.count_ = count_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.estExpire_ = estExpire_;
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
      if (other instanceof com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata) {
        return mergeFrom((com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata other) {
      if (other == com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata.getDefaultInstance()) return this;
      if (other.getCount() != 0) {
        setCount(other.getCount());
      }
      if (other.getEstExpire() != 0L) {
        setEstExpire(other.getEstExpire());
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
              count_ = input.readUInt32();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              estExpire_ = input.readUInt64();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
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

    private int count_ ;
    /**
     * <code>uint32 count = 1;</code>
     * @return The count.
     */
    @java.lang.Override
    public int getCount() {
      return count_;
    }
    /**
     * <code>uint32 count = 1;</code>
     * @param value The count to set.
     * @return This builder for chaining.
     */
    public Builder setCount(int value) {
      
      count_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>uint32 count = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearCount() {
      bitField0_ = (bitField0_ & ~0x00000001);
      count_ = 0;
      onChanged();
      return this;
    }

    private long estExpire_ ;
    /**
     * <code>uint64 estExpire = 2;</code>
     * @return The estExpire.
     */
    @java.lang.Override
    public long getEstExpire() {
      return estExpire_;
    }
    /**
     * <code>uint64 estExpire = 2;</code>
     * @param value The estExpire to set.
     * @return This builder for chaining.
     */
    public Builder setEstExpire(long value) {
      
      estExpire_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>uint64 estExpire = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearEstExpire() {
      bitField0_ = (bitField0_ & ~0x00000002);
      estExpire_ = 0L;
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


    // @@protoc_insertion_point(builder_scope:retainservice.RetainSetMetadata)
  }

  // @@protoc_insertion_point(class_scope:retainservice.RetainSetMetadata)
  private static final com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata();
  }

  public static com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RetainSetMetadata>
      PARSER = new com.google.protobuf.AbstractParser<RetainSetMetadata>() {
    @java.lang.Override
    public RetainSetMetadata parsePartialFrom(
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

  public static com.google.protobuf.Parser<RetainSetMetadata> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RetainSetMetadata> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.retain.rpc.proto.RetainSetMetadata getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

