// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/Type.proto

package com.zachary.bifromq.basekv.proto;

/**
 * Protobuf type {@code basekv.Range}
 */
public final class Range extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basekv.Range)
    RangeOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Range.newBuilder() to construct.
  private Range(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Range() {
    startKey_ = com.google.protobuf.ByteString.EMPTY;
    endKey_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Range();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basekv.proto.Type.internal_static_basekv_Range_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basekv.proto.Type.internal_static_basekv_Range_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basekv.proto.Range.class, com.zachary.bifromq.basekv.proto.Range.Builder.class);
  }

  private int bitField0_;
  public static final int STARTKEY_FIELD_NUMBER = 1;
  private com.google.protobuf.ByteString startKey_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <pre>
   * the inclusive lower bound of the range if present, or it's a left open range
   * </pre>
   *
   * <code>optional bytes startKey = 1;</code>
   * @return Whether the startKey field is set.
   */
  @java.lang.Override
  public boolean hasStartKey() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <pre>
   * the inclusive lower bound of the range if present, or it's a left open range
   * </pre>
   *
   * <code>optional bytes startKey = 1;</code>
   * @return The startKey.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getStartKey() {
    return startKey_;
  }

  public static final int ENDKEY_FIELD_NUMBER = 2;
  private com.google.protobuf.ByteString endKey_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <pre>
   * the exclusive upper bound of the range if present, or it's a right open range
   * </pre>
   *
   * <code>optional bytes endKey = 2;</code>
   * @return Whether the endKey field is set.
   */
  @java.lang.Override
  public boolean hasEndKey() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <pre>
   * the exclusive upper bound of the range if present, or it's a right open range
   * </pre>
   *
   * <code>optional bytes endKey = 2;</code>
   * @return The endKey.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getEndKey() {
    return endKey_;
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
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeBytes(1, startKey_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeBytes(2, endKey_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(1, startKey_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(2, endKey_);
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
    if (!(obj instanceof com.zachary.bifromq.basekv.proto.Range)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basekv.proto.Range other = (com.zachary.bifromq.basekv.proto.Range) obj;

    if (hasStartKey() != other.hasStartKey()) return false;
    if (hasStartKey()) {
      if (!getStartKey()
          .equals(other.getStartKey())) return false;
    }
    if (hasEndKey() != other.hasEndKey()) return false;
    if (hasEndKey()) {
      if (!getEndKey()
          .equals(other.getEndKey())) return false;
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
    if (hasStartKey()) {
      hash = (37 * hash) + STARTKEY_FIELD_NUMBER;
      hash = (53 * hash) + getStartKey().hashCode();
    }
    if (hasEndKey()) {
      hash = (37 * hash) + ENDKEY_FIELD_NUMBER;
      hash = (53 * hash) + getEndKey().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basekv.proto.Range parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.Range parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basekv.proto.Range prototype) {
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
   * Protobuf type {@code basekv.Range}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basekv.Range)
      com.zachary.bifromq.basekv.proto.RangeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basekv.proto.Type.internal_static_basekv_Range_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basekv.proto.Type.internal_static_basekv_Range_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basekv.proto.Range.class, com.zachary.bifromq.basekv.proto.Range.Builder.class);
    }

    // Construct using com.zachary.bifromq.basekv.proto.Range.newBuilder()
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
      startKey_ = com.google.protobuf.ByteString.EMPTY;
      endKey_ = com.google.protobuf.ByteString.EMPTY;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basekv.proto.Type.internal_static_basekv_Range_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.Range getDefaultInstanceForType() {
      return com.zachary.bifromq.basekv.proto.Range.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.Range build() {
      com.zachary.bifromq.basekv.proto.Range result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.Range buildPartial() {
      com.zachary.bifromq.basekv.proto.Range result = new com.zachary.bifromq.basekv.proto.Range(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basekv.proto.Range result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.startKey_ = startKey_;
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.endKey_ = endKey_;
        to_bitField0_ |= 0x00000002;
      }
      result.bitField0_ |= to_bitField0_;
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
      if (other instanceof com.zachary.bifromq.basekv.proto.Range) {
        return mergeFrom((com.zachary.bifromq.basekv.proto.Range)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basekv.proto.Range other) {
      if (other == com.zachary.bifromq.basekv.proto.Range.getDefaultInstance()) return this;
      if (other.hasStartKey()) {
        setStartKey(other.getStartKey());
      }
      if (other.hasEndKey()) {
        setEndKey(other.getEndKey());
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
              startKey_ = input.readBytes();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 18: {
              endKey_ = input.readBytes();
              bitField0_ |= 0x00000002;
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
    private int bitField0_;

    private com.google.protobuf.ByteString startKey_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <pre>
     * the inclusive lower bound of the range if present, or it's a left open range
     * </pre>
     *
     * <code>optional bytes startKey = 1;</code>
     * @return Whether the startKey field is set.
     */
    @java.lang.Override
    public boolean hasStartKey() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <pre>
     * the inclusive lower bound of the range if present, or it's a left open range
     * </pre>
     *
     * <code>optional bytes startKey = 1;</code>
     * @return The startKey.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getStartKey() {
      return startKey_;
    }
    /**
     * <pre>
     * the inclusive lower bound of the range if present, or it's a left open range
     * </pre>
     *
     * <code>optional bytes startKey = 1;</code>
     * @param value The startKey to set.
     * @return This builder for chaining.
     */
    public Builder setStartKey(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      startKey_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the inclusive lower bound of the range if present, or it's a left open range
     * </pre>
     *
     * <code>optional bytes startKey = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearStartKey() {
      bitField0_ = (bitField0_ & ~0x00000001);
      startKey_ = getDefaultInstance().getStartKey();
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString endKey_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <pre>
     * the exclusive upper bound of the range if present, or it's a right open range
     * </pre>
     *
     * <code>optional bytes endKey = 2;</code>
     * @return Whether the endKey field is set.
     */
    @java.lang.Override
    public boolean hasEndKey() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <pre>
     * the exclusive upper bound of the range if present, or it's a right open range
     * </pre>
     *
     * <code>optional bytes endKey = 2;</code>
     * @return The endKey.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getEndKey() {
      return endKey_;
    }
    /**
     * <pre>
     * the exclusive upper bound of the range if present, or it's a right open range
     * </pre>
     *
     * <code>optional bytes endKey = 2;</code>
     * @param value The endKey to set.
     * @return This builder for chaining.
     */
    public Builder setEndKey(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      endKey_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * the exclusive upper bound of the range if present, or it's a right open range
     * </pre>
     *
     * <code>optional bytes endKey = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearEndKey() {
      bitField0_ = (bitField0_ & ~0x00000002);
      endKey_ = getDefaultInstance().getEndKey();
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


    // @@protoc_insertion_point(builder_scope:basekv.Range)
  }

  // @@protoc_insertion_point(class_scope:basekv.Range)
  private static final com.zachary.bifromq.basekv.proto.Range DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basekv.proto.Range();
  }

  public static com.zachary.bifromq.basekv.proto.Range getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Range>
      PARSER = new com.google.protobuf.AbstractParser<Range>() {
    @java.lang.Override
    public Range parsePartialFrom(
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

  public static com.google.protobuf.Parser<Range> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Range> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.Range getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
