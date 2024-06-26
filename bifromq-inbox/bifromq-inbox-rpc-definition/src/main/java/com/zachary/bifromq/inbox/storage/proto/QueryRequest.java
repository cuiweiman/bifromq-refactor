// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: inboxservice/InboxCoProc.proto

package com.zachary.bifromq.inbox.storage.proto;

/**
 * Protobuf type {@code inboxservice.QueryRequest}
 */
public final class QueryRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:inboxservice.QueryRequest)
    QueryRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use QueryRequest.newBuilder() to construct.
  private QueryRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private QueryRequest() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new QueryRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_QueryRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_QueryRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.inbox.storage.proto.QueryRequest.class, com.zachary.bifromq.inbox.storage.proto.QueryRequest.Builder.class);
  }

  private int bitField0_;
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

  public static final int HAS_FIELD_NUMBER = 2;
  private com.zachary.bifromq.inbox.storage.proto.HasRequest has_;
  /**
   * <code>optional .inboxservice.HasRequest has = 2;</code>
   * @return Whether the has field is set.
   */
  @java.lang.Override
  public boolean hasHas() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional .inboxservice.HasRequest has = 2;</code>
   * @return The has.
   */
  @java.lang.Override
  public com.zachary.bifromq.inbox.storage.proto.HasRequest getHas() {
    return has_ == null ? com.zachary.bifromq.inbox.storage.proto.HasRequest.getDefaultInstance() : has_;
  }
  /**
   * <code>optional .inboxservice.HasRequest has = 2;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.inbox.storage.proto.HasRequestOrBuilder getHasOrBuilder() {
    return has_ == null ? com.zachary.bifromq.inbox.storage.proto.HasRequest.getDefaultInstance() : has_;
  }

  public static final int FETCH_FIELD_NUMBER = 3;
  private com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest fetch_;
  /**
   * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
   * @return Whether the fetch field is set.
   */
  @java.lang.Override
  public boolean hasFetch() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
   * @return The fetch.
   */
  @java.lang.Override
  public com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest getFetch() {
    return fetch_ == null ? com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.getDefaultInstance() : fetch_;
  }
  /**
   * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.inbox.storage.proto.InboxFetchRequestOrBuilder getFetchOrBuilder() {
    return fetch_ == null ? com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.getDefaultInstance() : fetch_;
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
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(2, getHas());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeMessage(3, getFetch());
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
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getHas());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getFetch());
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
    if (!(obj instanceof com.zachary.bifromq.inbox.storage.proto.QueryRequest)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.inbox.storage.proto.QueryRequest other = (com.zachary.bifromq.inbox.storage.proto.QueryRequest) obj;

    if (getReqId()
        != other.getReqId()) return false;
    if (hasHas() != other.hasHas()) return false;
    if (hasHas()) {
      if (!getHas()
          .equals(other.getHas())) return false;
    }
    if (hasFetch() != other.hasFetch()) return false;
    if (hasFetch()) {
      if (!getFetch()
          .equals(other.getFetch())) return false;
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
    hash = (37 * hash) + REQID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getReqId());
    if (hasHas()) {
      hash = (37 * hash) + HAS_FIELD_NUMBER;
      hash = (53 * hash) + getHas().hashCode();
    }
    if (hasFetch()) {
      hash = (37 * hash) + FETCH_FIELD_NUMBER;
      hash = (53 * hash) + getFetch().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.inbox.storage.proto.QueryRequest prototype) {
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
   * Protobuf type {@code inboxservice.QueryRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:inboxservice.QueryRequest)
      com.zachary.bifromq.inbox.storage.proto.QueryRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_QueryRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_QueryRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.inbox.storage.proto.QueryRequest.class, com.zachary.bifromq.inbox.storage.proto.QueryRequest.Builder.class);
    }

    // Construct using com.zachary.bifromq.inbox.storage.proto.QueryRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getHasFieldBuilder();
        getFetchFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      reqId_ = 0L;
      has_ = null;
      if (hasBuilder_ != null) {
        hasBuilder_.dispose();
        hasBuilder_ = null;
      }
      fetch_ = null;
      if (fetchBuilder_ != null) {
        fetchBuilder_.dispose();
        fetchBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.inbox.storage.proto.InboxCoProcProtos.internal_static_inboxservice_QueryRequest_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.inbox.storage.proto.QueryRequest getDefaultInstanceForType() {
      return com.zachary.bifromq.inbox.storage.proto.QueryRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.inbox.storage.proto.QueryRequest build() {
      com.zachary.bifromq.inbox.storage.proto.QueryRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.inbox.storage.proto.QueryRequest buildPartial() {
      com.zachary.bifromq.inbox.storage.proto.QueryRequest result = new com.zachary.bifromq.inbox.storage.proto.QueryRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.inbox.storage.proto.QueryRequest result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.reqId_ = reqId_;
      }
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.has_ = hasBuilder_ == null
            ? has_
            : hasBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.fetch_ = fetchBuilder_ == null
            ? fetch_
            : fetchBuilder_.build();
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
      if (other instanceof com.zachary.bifromq.inbox.storage.proto.QueryRequest) {
        return mergeFrom((com.zachary.bifromq.inbox.storage.proto.QueryRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.inbox.storage.proto.QueryRequest other) {
      if (other == com.zachary.bifromq.inbox.storage.proto.QueryRequest.getDefaultInstance()) return this;
      if (other.getReqId() != 0L) {
        setReqId(other.getReqId());
      }
      if (other.hasHas()) {
        mergeHas(other.getHas());
      }
      if (other.hasFetch()) {
        mergeFetch(other.getFetch());
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
              input.readMessage(
                  getHasFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              input.readMessage(
                  getFetchFieldBuilder().getBuilder(),
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

    private com.zachary.bifromq.inbox.storage.proto.HasRequest has_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.inbox.storage.proto.HasRequest, com.zachary.bifromq.inbox.storage.proto.HasRequest.Builder, com.zachary.bifromq.inbox.storage.proto.HasRequestOrBuilder> hasBuilder_;
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     * @return Whether the has field is set.
     */
    public boolean hasHas() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     * @return The has.
     */
    public com.zachary.bifromq.inbox.storage.proto.HasRequest getHas() {
      if (hasBuilder_ == null) {
        return has_ == null ? com.zachary.bifromq.inbox.storage.proto.HasRequest.getDefaultInstance() : has_;
      } else {
        return hasBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     */
    public Builder setHas(com.zachary.bifromq.inbox.storage.proto.HasRequest value) {
      if (hasBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        has_ = value;
      } else {
        hasBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     */
    public Builder setHas(
        com.zachary.bifromq.inbox.storage.proto.HasRequest.Builder builderForValue) {
      if (hasBuilder_ == null) {
        has_ = builderForValue.build();
      } else {
        hasBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     */
    public Builder mergeHas(com.zachary.bifromq.inbox.storage.proto.HasRequest value) {
      if (hasBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0) &&
          has_ != null &&
          has_ != com.zachary.bifromq.inbox.storage.proto.HasRequest.getDefaultInstance()) {
          getHasBuilder().mergeFrom(value);
        } else {
          has_ = value;
        }
      } else {
        hasBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     */
    public Builder clearHas() {
      bitField0_ = (bitField0_ & ~0x00000002);
      has_ = null;
      if (hasBuilder_ != null) {
        hasBuilder_.dispose();
        hasBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     */
    public com.zachary.bifromq.inbox.storage.proto.HasRequest.Builder getHasBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getHasFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     */
    public com.zachary.bifromq.inbox.storage.proto.HasRequestOrBuilder getHasOrBuilder() {
      if (hasBuilder_ != null) {
        return hasBuilder_.getMessageOrBuilder();
      } else {
        return has_ == null ?
            com.zachary.bifromq.inbox.storage.proto.HasRequest.getDefaultInstance() : has_;
      }
    }
    /**
     * <code>optional .inboxservice.HasRequest has = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.inbox.storage.proto.HasRequest, com.zachary.bifromq.inbox.storage.proto.HasRequest.Builder, com.zachary.bifromq.inbox.storage.proto.HasRequestOrBuilder> 
        getHasFieldBuilder() {
      if (hasBuilder_ == null) {
        hasBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.inbox.storage.proto.HasRequest, com.zachary.bifromq.inbox.storage.proto.HasRequest.Builder, com.zachary.bifromq.inbox.storage.proto.HasRequestOrBuilder>(
                getHas(),
                getParentForChildren(),
                isClean());
        has_ = null;
      }
      return hasBuilder_;
    }

    private com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest fetch_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest, com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.Builder, com.zachary.bifromq.inbox.storage.proto.InboxFetchRequestOrBuilder> fetchBuilder_;
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     * @return Whether the fetch field is set.
     */
    public boolean hasFetch() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     * @return The fetch.
     */
    public com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest getFetch() {
      if (fetchBuilder_ == null) {
        return fetch_ == null ? com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.getDefaultInstance() : fetch_;
      } else {
        return fetchBuilder_.getMessage();
      }
    }
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     */
    public Builder setFetch(com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest value) {
      if (fetchBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        fetch_ = value;
      } else {
        fetchBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     */
    public Builder setFetch(
        com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.Builder builderForValue) {
      if (fetchBuilder_ == null) {
        fetch_ = builderForValue.build();
      } else {
        fetchBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     */
    public Builder mergeFetch(com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest value) {
      if (fetchBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0) &&
          fetch_ != null &&
          fetch_ != com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.getDefaultInstance()) {
          getFetchBuilder().mergeFrom(value);
        } else {
          fetch_ = value;
        }
      } else {
        fetchBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     */
    public Builder clearFetch() {
      bitField0_ = (bitField0_ & ~0x00000004);
      fetch_ = null;
      if (fetchBuilder_ != null) {
        fetchBuilder_.dispose();
        fetchBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     */
    public com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.Builder getFetchBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getFetchFieldBuilder().getBuilder();
    }
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     */
    public com.zachary.bifromq.inbox.storage.proto.InboxFetchRequestOrBuilder getFetchOrBuilder() {
      if (fetchBuilder_ != null) {
        return fetchBuilder_.getMessageOrBuilder();
      } else {
        return fetch_ == null ?
            com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.getDefaultInstance() : fetch_;
      }
    }
    /**
     * <code>optional .inboxservice.InboxFetchRequest fetch = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest, com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.Builder, com.zachary.bifromq.inbox.storage.proto.InboxFetchRequestOrBuilder> 
        getFetchFieldBuilder() {
      if (fetchBuilder_ == null) {
        fetchBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest, com.zachary.bifromq.inbox.storage.proto.InboxFetchRequest.Builder, com.zachary.bifromq.inbox.storage.proto.InboxFetchRequestOrBuilder>(
                getFetch(),
                getParentForChildren(),
                isClean());
        fetch_ = null;
      }
      return fetchBuilder_;
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


    // @@protoc_insertion_point(builder_scope:inboxservice.QueryRequest)
  }

  // @@protoc_insertion_point(class_scope:inboxservice.QueryRequest)
  private static final com.zachary.bifromq.inbox.storage.proto.QueryRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.inbox.storage.proto.QueryRequest();
  }

  public static com.zachary.bifromq.inbox.storage.proto.QueryRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<QueryRequest>
      PARSER = new com.google.protobuf.AbstractParser<QueryRequest>() {
    @java.lang.Override
    public QueryRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<QueryRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<QueryRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.inbox.storage.proto.QueryRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

