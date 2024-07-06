// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/Command.proto

package com.zachary.bifromq.basekv.proto;

/**
 * Protobuf type {@code basekv.Merge}
 */
public final class Merge extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basekv.Merge)
    MergeOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Merge.newBuilder() to construct.
  private Merge(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Merge() {
    storeId_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Merge();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basekv.proto.Command.internal_static_basekv_Merge_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basekv.proto.Command.internal_static_basekv_Merge_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basekv.proto.Merge.class, com.zachary.bifromq.basekv.proto.Merge.Builder.class);
  }

  public static final int MERGEEID_FIELD_NUMBER = 1;
  private com.zachary.bifromq.basekv.proto.KVRangeId mergeeId_;
  /**
   * <code>.basekv.KVRangeId mergeeId = 1;</code>
   * @return Whether the mergeeId field is set.
   */
  @java.lang.Override
  public boolean hasMergeeId() {
    return mergeeId_ != null;
  }
  /**
   * <code>.basekv.KVRangeId mergeeId = 1;</code>
   * @return The mergeeId.
   */
  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.KVRangeId getMergeeId() {
    return mergeeId_ == null ? com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance() : mergeeId_;
  }
  /**
   * <code>.basekv.KVRangeId mergeeId = 1;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder getMergeeIdOrBuilder() {
    return mergeeId_ == null ? com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance() : mergeeId_;
  }

  public static final int MERGEEVER_FIELD_NUMBER = 2;
  private long mergeeVer_ = 0L;
  /**
   * <code>uint64 mergeeVer = 2;</code>
   * @return The mergeeVer.
   */
  @java.lang.Override
  public long getMergeeVer() {
    return mergeeVer_;
  }

  public static final int RANGE_FIELD_NUMBER = 3;
  private com.zachary.bifromq.basekv.proto.Range range_;
  /**
   * <code>.basekv.Range range = 3;</code>
   * @return Whether the range field is set.
   */
  @java.lang.Override
  public boolean hasRange() {
    return range_ != null;
  }
  /**
   * <code>.basekv.Range range = 3;</code>
   * @return The range.
   */
  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.Range getRange() {
    return range_ == null ? com.zachary.bifromq.basekv.proto.Range.getDefaultInstance() : range_;
  }
  /**
   * <code>.basekv.Range range = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.RangeOrBuilder getRangeOrBuilder() {
    return range_ == null ? com.zachary.bifromq.basekv.proto.Range.getDefaultInstance() : range_;
  }

  public static final int STOREID_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private volatile java.lang.Object storeId_ = "";
  /**
   * <code>string storeId = 4;</code>
   * @return The storeId.
   */
  @java.lang.Override
  public java.lang.String getStoreId() {
    java.lang.Object ref = storeId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      storeId_ = s;
      return s;
    }
  }
  /**
   * <code>string storeId = 4;</code>
   * @return The bytes for storeId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getStoreIdBytes() {
    java.lang.Object ref = storeId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      storeId_ = b;
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
    if (mergeeId_ != null) {
      output.writeMessage(1, getMergeeId());
    }
    if (mergeeVer_ != 0L) {
      output.writeUInt64(2, mergeeVer_);
    }
    if (range_ != null) {
      output.writeMessage(3, getRange());
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(storeId_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, storeId_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (mergeeId_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getMergeeId());
    }
    if (mergeeVer_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(2, mergeeVer_);
    }
    if (range_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getRange());
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(storeId_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, storeId_);
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
    if (!(obj instanceof com.zachary.bifromq.basekv.proto.Merge)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basekv.proto.Merge other = (com.zachary.bifromq.basekv.proto.Merge) obj;

    if (hasMergeeId() != other.hasMergeeId()) return false;
    if (hasMergeeId()) {
      if (!getMergeeId()
          .equals(other.getMergeeId())) return false;
    }
    if (getMergeeVer()
        != other.getMergeeVer()) return false;
    if (hasRange() != other.hasRange()) return false;
    if (hasRange()) {
      if (!getRange()
          .equals(other.getRange())) return false;
    }
    if (!getStoreId()
        .equals(other.getStoreId())) return false;
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
    if (hasMergeeId()) {
      hash = (37 * hash) + MERGEEID_FIELD_NUMBER;
      hash = (53 * hash) + getMergeeId().hashCode();
    }
    hash = (37 * hash) + MERGEEVER_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getMergeeVer());
    if (hasRange()) {
      hash = (37 * hash) + RANGE_FIELD_NUMBER;
      hash = (53 * hash) + getRange().hashCode();
    }
    hash = (37 * hash) + STOREID_FIELD_NUMBER;
    hash = (53 * hash) + getStoreId().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.Merge parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basekv.proto.Merge prototype) {
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
   * Protobuf type {@code basekv.Merge}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basekv.Merge)
      com.zachary.bifromq.basekv.proto.MergeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basekv.proto.Command.internal_static_basekv_Merge_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basekv.proto.Command.internal_static_basekv_Merge_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basekv.proto.Merge.class, com.zachary.bifromq.basekv.proto.Merge.Builder.class);
    }

    // Construct using com.zachary.bifromq.basekv.proto.Merge.newBuilder()
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
      mergeeId_ = null;
      if (mergeeIdBuilder_ != null) {
        mergeeIdBuilder_.dispose();
        mergeeIdBuilder_ = null;
      }
      mergeeVer_ = 0L;
      range_ = null;
      if (rangeBuilder_ != null) {
        rangeBuilder_.dispose();
        rangeBuilder_ = null;
      }
      storeId_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basekv.proto.Command.internal_static_basekv_Merge_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.Merge getDefaultInstanceForType() {
      return com.zachary.bifromq.basekv.proto.Merge.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.Merge build() {
      com.zachary.bifromq.basekv.proto.Merge result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.Merge buildPartial() {
      com.zachary.bifromq.basekv.proto.Merge result = new com.zachary.bifromq.basekv.proto.Merge(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basekv.proto.Merge result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.mergeeId_ = mergeeIdBuilder_ == null
            ? mergeeId_
            : mergeeIdBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.mergeeVer_ = mergeeVer_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.range_ = rangeBuilder_ == null
            ? range_
            : rangeBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.storeId_ = storeId_;
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
      if (other instanceof com.zachary.bifromq.basekv.proto.Merge) {
        return mergeFrom((com.zachary.bifromq.basekv.proto.Merge)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basekv.proto.Merge other) {
      if (other == com.zachary.bifromq.basekv.proto.Merge.getDefaultInstance()) return this;
      if (other.hasMergeeId()) {
        mergeMergeeId(other.getMergeeId());
      }
      if (other.getMergeeVer() != 0L) {
        setMergeeVer(other.getMergeeVer());
      }
      if (other.hasRange()) {
        mergeRange(other.getRange());
      }
      if (!other.getStoreId().isEmpty()) {
        storeId_ = other.storeId_;
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
              input.readMessage(
                  getMergeeIdFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 16: {
              mergeeVer_ = input.readUInt64();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 26: {
              input.readMessage(
                  getRangeFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 34: {
              storeId_ = input.readStringRequireUtf8();
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

    private com.zachary.bifromq.basekv.proto.KVRangeId mergeeId_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basekv.proto.KVRangeId, com.zachary.bifromq.basekv.proto.KVRangeId.Builder, com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder> mergeeIdBuilder_;
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     * @return Whether the mergeeId field is set.
     */
    public boolean hasMergeeId() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     * @return The mergeeId.
     */
    public com.zachary.bifromq.basekv.proto.KVRangeId getMergeeId() {
      if (mergeeIdBuilder_ == null) {
        return mergeeId_ == null ? com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance() : mergeeId_;
      } else {
        return mergeeIdBuilder_.getMessage();
      }
    }
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     */
    public Builder setMergeeId(com.zachary.bifromq.basekv.proto.KVRangeId value) {
      if (mergeeIdBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        mergeeId_ = value;
      } else {
        mergeeIdBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     */
    public Builder setMergeeId(
        com.zachary.bifromq.basekv.proto.KVRangeId.Builder builderForValue) {
      if (mergeeIdBuilder_ == null) {
        mergeeId_ = builderForValue.build();
      } else {
        mergeeIdBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     */
    public Builder mergeMergeeId(com.zachary.bifromq.basekv.proto.KVRangeId value) {
      if (mergeeIdBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0) &&
          mergeeId_ != null &&
          mergeeId_ != com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance()) {
          getMergeeIdBuilder().mergeFrom(value);
        } else {
          mergeeId_ = value;
        }
      } else {
        mergeeIdBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     */
    public Builder clearMergeeId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      mergeeId_ = null;
      if (mergeeIdBuilder_ != null) {
        mergeeIdBuilder_.dispose();
        mergeeIdBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVRangeId.Builder getMergeeIdBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getMergeeIdFieldBuilder().getBuilder();
    }
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder getMergeeIdOrBuilder() {
      if (mergeeIdBuilder_ != null) {
        return mergeeIdBuilder_.getMessageOrBuilder();
      } else {
        return mergeeId_ == null ?
            com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance() : mergeeId_;
      }
    }
    /**
     * <code>.basekv.KVRangeId mergeeId = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basekv.proto.KVRangeId, com.zachary.bifromq.basekv.proto.KVRangeId.Builder, com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder> 
        getMergeeIdFieldBuilder() {
      if (mergeeIdBuilder_ == null) {
        mergeeIdBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basekv.proto.KVRangeId, com.zachary.bifromq.basekv.proto.KVRangeId.Builder, com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder>(
                getMergeeId(),
                getParentForChildren(),
                isClean());
        mergeeId_ = null;
      }
      return mergeeIdBuilder_;
    }

    private long mergeeVer_ ;
    /**
     * <code>uint64 mergeeVer = 2;</code>
     * @return The mergeeVer.
     */
    @java.lang.Override
    public long getMergeeVer() {
      return mergeeVer_;
    }
    /**
     * <code>uint64 mergeeVer = 2;</code>
     * @param value The mergeeVer to set.
     * @return This builder for chaining.
     */
    public Builder setMergeeVer(long value) {
      
      mergeeVer_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>uint64 mergeeVer = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearMergeeVer() {
      bitField0_ = (bitField0_ & ~0x00000002);
      mergeeVer_ = 0L;
      onChanged();
      return this;
    }

    private com.zachary.bifromq.basekv.proto.Range range_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basekv.proto.Range, com.zachary.bifromq.basekv.proto.Range.Builder, com.zachary.bifromq.basekv.proto.RangeOrBuilder> rangeBuilder_;
    /**
     * <code>.basekv.Range range = 3;</code>
     * @return Whether the range field is set.
     */
    public boolean hasRange() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>.basekv.Range range = 3;</code>
     * @return The range.
     */
    public com.zachary.bifromq.basekv.proto.Range getRange() {
      if (rangeBuilder_ == null) {
        return range_ == null ? com.zachary.bifromq.basekv.proto.Range.getDefaultInstance() : range_;
      } else {
        return rangeBuilder_.getMessage();
      }
    }
    /**
     * <code>.basekv.Range range = 3;</code>
     */
    public Builder setRange(com.zachary.bifromq.basekv.proto.Range value) {
      if (rangeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        range_ = value;
      } else {
        rangeBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.Range range = 3;</code>
     */
    public Builder setRange(
        com.zachary.bifromq.basekv.proto.Range.Builder builderForValue) {
      if (rangeBuilder_ == null) {
        range_ = builderForValue.build();
      } else {
        rangeBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.Range range = 3;</code>
     */
    public Builder mergeRange(com.zachary.bifromq.basekv.proto.Range value) {
      if (rangeBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0) &&
          range_ != null &&
          range_ != com.zachary.bifromq.basekv.proto.Range.getDefaultInstance()) {
          getRangeBuilder().mergeFrom(value);
        } else {
          range_ = value;
        }
      } else {
        rangeBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.Range range = 3;</code>
     */
    public Builder clearRange() {
      bitField0_ = (bitField0_ & ~0x00000004);
      range_ = null;
      if (rangeBuilder_ != null) {
        rangeBuilder_.dispose();
        rangeBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.Range range = 3;</code>
     */
    public com.zachary.bifromq.basekv.proto.Range.Builder getRangeBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getRangeFieldBuilder().getBuilder();
    }
    /**
     * <code>.basekv.Range range = 3;</code>
     */
    public com.zachary.bifromq.basekv.proto.RangeOrBuilder getRangeOrBuilder() {
      if (rangeBuilder_ != null) {
        return rangeBuilder_.getMessageOrBuilder();
      } else {
        return range_ == null ?
            com.zachary.bifromq.basekv.proto.Range.getDefaultInstance() : range_;
      }
    }
    /**
     * <code>.basekv.Range range = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basekv.proto.Range, com.zachary.bifromq.basekv.proto.Range.Builder, com.zachary.bifromq.basekv.proto.RangeOrBuilder> 
        getRangeFieldBuilder() {
      if (rangeBuilder_ == null) {
        rangeBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basekv.proto.Range, com.zachary.bifromq.basekv.proto.Range.Builder, com.zachary.bifromq.basekv.proto.RangeOrBuilder>(
                getRange(),
                getParentForChildren(),
                isClean());
        range_ = null;
      }
      return rangeBuilder_;
    }

    private java.lang.Object storeId_ = "";
    /**
     * <code>string storeId = 4;</code>
     * @return The storeId.
     */
    public java.lang.String getStoreId() {
      java.lang.Object ref = storeId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        storeId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string storeId = 4;</code>
     * @return The bytes for storeId.
     */
    public com.google.protobuf.ByteString
        getStoreIdBytes() {
      java.lang.Object ref = storeId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        storeId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string storeId = 4;</code>
     * @param value The storeId to set.
     * @return This builder for chaining.
     */
    public Builder setStoreId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      storeId_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>string storeId = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearStoreId() {
      storeId_ = getDefaultInstance().getStoreId();
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    /**
     * <code>string storeId = 4;</code>
     * @param value The bytes for storeId to set.
     * @return This builder for chaining.
     */
    public Builder setStoreIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      storeId_ = value;
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


    // @@protoc_insertion_point(builder_scope:basekv.Merge)
  }

  // @@protoc_insertion_point(class_scope:basekv.Merge)
  private static final com.zachary.bifromq.basekv.proto.Merge DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basekv.proto.Merge();
  }

  public static com.zachary.bifromq.basekv.proto.Merge getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Merge>
      PARSER = new com.google.protobuf.AbstractParser<Merge>() {
    @java.lang.Override
    public Merge parsePartialFrom(
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

  public static com.google.protobuf.Parser<Merge> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Merge> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.Merge getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
