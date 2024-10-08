// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: retainservice/RetainService.proto

package com.zachary.bifromq.retain.rpc.proto;

/**
 * Protobuf type {@code retainservice.MatchRequest}
 */
public final class MatchRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:retainservice.MatchRequest)
    MatchRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MatchRequest.newBuilder() to construct.
  private MatchRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MatchRequest() {
    topicFilter_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new MatchRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.retain.rpc.proto.RetainServiceProtos.internal_static_retainservice_MatchRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.retain.rpc.proto.RetainServiceProtos.internal_static_retainservice_MatchRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.retain.rpc.proto.MatchRequest.class, com.zachary.bifromq.retain.rpc.proto.MatchRequest.Builder.class);
  }

  public static final int REQID_FIELD_NUMBER = 1;
  private long reqId_ = 0L;
  /**
   * <code>uint64 reqId = 1;</code>
   * @return The reqId.
   */
  @java.lang.Override
  public long getReqId() {
    return reqId_;
  }

  public static final int TOPICFILTER_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile java.lang.Object topicFilter_ = "";
  /**
   * <code>string topicFilter = 2;</code>
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
   * <code>string topicFilter = 2;</code>
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

  public static final int LIMIT_FIELD_NUMBER = 3;
  private int limit_ = 0;
  /**
   * <code>uint32 limit = 3;</code>
   * @return The limit.
   */
  @java.lang.Override
  public int getLimit() {
    return limit_;
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
    if (reqId_ != 0L) {
      output.writeUInt64(1, reqId_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(topicFilter_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, topicFilter_);
    }
    if (limit_ != 0) {
      output.writeUInt32(3, limit_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (reqId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(1, reqId_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(topicFilter_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, topicFilter_);
    }
    if (limit_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(3, limit_);
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
    if (!(obj instanceof com.zachary.bifromq.retain.rpc.proto.MatchRequest)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.retain.rpc.proto.MatchRequest other = (com.zachary.bifromq.retain.rpc.proto.MatchRequest) obj;

    if (getReqId()
        != other.getReqId()) return false;
    if (!getTopicFilter()
        .equals(other.getTopicFilter())) return false;
    if (getLimit()
        != other.getLimit()) return false;
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
    hash = (37 * hash) + REQID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getReqId());
    hash = (37 * hash) + TOPICFILTER_FIELD_NUMBER;
    hash = (53 * hash) + getTopicFilter().hashCode();
    hash = (37 * hash) + LIMIT_FIELD_NUMBER;
    hash = (53 * hash) + getLimit();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.retain.rpc.proto.MatchRequest prototype) {
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
   * Protobuf type {@code retainservice.MatchRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:retainservice.MatchRequest)
      com.zachary.bifromq.retain.rpc.proto.MatchRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.retain.rpc.proto.RetainServiceProtos.internal_static_retainservice_MatchRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.retain.rpc.proto.RetainServiceProtos.internal_static_retainservice_MatchRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.retain.rpc.proto.MatchRequest.class, com.zachary.bifromq.retain.rpc.proto.MatchRequest.Builder.class);
    }

    // Construct using com.zachary.bifromq.retain.rpc.proto.MatchRequest.newBuilder()
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
      reqId_ = 0L;
      topicFilter_ = "";
      limit_ = 0;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.retain.rpc.proto.RetainServiceProtos.internal_static_retainservice_MatchRequest_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.MatchRequest getDefaultInstanceForType() {
      return com.zachary.bifromq.retain.rpc.proto.MatchRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.MatchRequest build() {
      com.zachary.bifromq.retain.rpc.proto.MatchRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.MatchRequest buildPartial() {
      com.zachary.bifromq.retain.rpc.proto.MatchRequest result = new com.zachary.bifromq.retain.rpc.proto.MatchRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.retain.rpc.proto.MatchRequest result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.reqId_ = reqId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.topicFilter_ = topicFilter_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.limit_ = limit_;
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
      if (other instanceof com.zachary.bifromq.retain.rpc.proto.MatchRequest) {
        return mergeFrom((com.zachary.bifromq.retain.rpc.proto.MatchRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.retain.rpc.proto.MatchRequest other) {
      if (other == com.zachary.bifromq.retain.rpc.proto.MatchRequest.getDefaultInstance()) return this;
      if (other.getReqId() != 0L) {
        setReqId(other.getReqId());
      }
      if (!other.getTopicFilter().isEmpty()) {
        topicFilter_ = other.topicFilter_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      if (other.getLimit() != 0) {
        setLimit(other.getLimit());
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
              reqId_ = input.readUInt64();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 18: {
              topicFilter_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 24: {
              limit_ = input.readUInt32();
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

    private long reqId_ ;
    /**
     * <code>uint64 reqId = 1;</code>
     * @return The reqId.
     */
    @java.lang.Override
    public long getReqId() {
      return reqId_;
    }
    /**
     * <code>uint64 reqId = 1;</code>
     * @param value The reqId to set.
     * @return This builder for chaining.
     */
    public Builder setReqId(long value) {
      
      reqId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>uint64 reqId = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearReqId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      reqId_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object topicFilter_ = "";
    /**
     * <code>string topicFilter = 2;</code>
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
     * <code>string topicFilter = 2;</code>
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
     * <code>string topicFilter = 2;</code>
     * @param value The topicFilter to set.
     * @return This builder for chaining.
     */
    public Builder setTopicFilter(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      topicFilter_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>string topicFilter = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearTopicFilter() {
      topicFilter_ = getDefaultInstance().getTopicFilter();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <code>string topicFilter = 2;</code>
     * @param value The bytes for topicFilter to set.
     * @return This builder for chaining.
     */
    public Builder setTopicFilterBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      topicFilter_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private int limit_ ;
    /**
     * <code>uint32 limit = 3;</code>
     * @return The limit.
     */
    @java.lang.Override
    public int getLimit() {
      return limit_;
    }
    /**
     * <code>uint32 limit = 3;</code>
     * @param value The limit to set.
     * @return This builder for chaining.
     */
    public Builder setLimit(int value) {
      
      limit_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>uint32 limit = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearLimit() {
      bitField0_ = (bitField0_ & ~0x00000004);
      limit_ = 0;
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


    // @@protoc_insertion_point(builder_scope:retainservice.MatchRequest)
  }

  // @@protoc_insertion_point(class_scope:retainservice.MatchRequest)
  private static final com.zachary.bifromq.retain.rpc.proto.MatchRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.retain.rpc.proto.MatchRequest();
  }

  public static com.zachary.bifromq.retain.rpc.proto.MatchRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MatchRequest>
      PARSER = new com.google.protobuf.AbstractParser<MatchRequest>() {
    @java.lang.Override
    public MatchRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<MatchRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MatchRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.retain.rpc.proto.MatchRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

