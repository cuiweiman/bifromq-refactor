// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/StoreMessage.proto

package com.zachary.bifromq.basekv.proto;

/**
 * Protobuf type {@code basekv.MergeDoneRequest}
 */
public final class MergeDoneRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basekv.MergeDoneRequest)
    MergeDoneRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MergeDoneRequest.newBuilder() to construct.
  private MergeDoneRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MergeDoneRequest() {
    taskId_ = "";
    storeId_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new MergeDoneRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_MergeDoneRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_MergeDoneRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basekv.proto.MergeDoneRequest.class, com.zachary.bifromq.basekv.proto.MergeDoneRequest.Builder.class);
  }

  public static final int TASKID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object taskId_ = "";
  /**
   * <code>string taskId = 1;</code>
   * @return The taskId.
   */
  @java.lang.Override
  public java.lang.String getTaskId() {
    java.lang.Object ref = taskId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      taskId_ = s;
      return s;
    }
  }
  /**
   * <code>string taskId = 1;</code>
   * @return The bytes for taskId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getTaskIdBytes() {
    java.lang.Object ref = taskId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      taskId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int REQID_FIELD_NUMBER = 2;
  private long reqId_ = 0L;
  /**
   * <code>uint64 reqId = 2;</code>
   * @return The reqId.
   */
  @java.lang.Override
  public long getReqId() {
    return reqId_;
  }

  public static final int ID_FIELD_NUMBER = 3;
  private com.zachary.bifromq.basekv.proto.KVRangeId id_;
  /**
   * <pre>
   * merger's id
   * </pre>
   *
   * <code>.basekv.KVRangeId id = 3;</code>
   * @return Whether the id field is set.
   */
  @java.lang.Override
  public boolean hasId() {
    return id_ != null;
  }
  /**
   * <pre>
   * merger's id
   * </pre>
   *
   * <code>.basekv.KVRangeId id = 3;</code>
   * @return The id.
   */
  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.KVRangeId getId() {
    return id_ == null ? com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance() : id_;
  }
  /**
   * <pre>
   * merger's id
   * </pre>
   *
   * <code>.basekv.KVRangeId id = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder getIdOrBuilder() {
    return id_ == null ? com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance() : id_;
  }

  public static final int MERGEEVER_FIELD_NUMBER = 4;
  private long mergeeVer_ = 0L;
  /**
   * <code>uint64 mergeeVer = 4;</code>
   * @return The mergeeVer.
   */
  @java.lang.Override
  public long getMergeeVer() {
    return mergeeVer_;
  }

  public static final int STOREID_FIELD_NUMBER = 5;
  @SuppressWarnings("serial")
  private volatile java.lang.Object storeId_ = "";
  /**
   * <code>string storeId = 5;</code>
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
   * <code>string storeId = 5;</code>
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(taskId_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, taskId_);
    }
    if (reqId_ != 0L) {
      output.writeUInt64(2, reqId_);
    }
    if (id_ != null) {
      output.writeMessage(3, getId());
    }
    if (mergeeVer_ != 0L) {
      output.writeUInt64(4, mergeeVer_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(storeId_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 5, storeId_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(taskId_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, taskId_);
    }
    if (reqId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(2, reqId_);
    }
    if (id_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getId());
    }
    if (mergeeVer_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(4, mergeeVer_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(storeId_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, storeId_);
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
    if (!(obj instanceof com.zachary.bifromq.basekv.proto.MergeDoneRequest)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basekv.proto.MergeDoneRequest other = (com.zachary.bifromq.basekv.proto.MergeDoneRequest) obj;

    if (!getTaskId()
        .equals(other.getTaskId())) return false;
    if (getReqId()
        != other.getReqId()) return false;
    if (hasId() != other.hasId()) return false;
    if (hasId()) {
      if (!getId()
          .equals(other.getId())) return false;
    }
    if (getMergeeVer()
        != other.getMergeeVer()) return false;
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
    hash = (37 * hash) + TASKID_FIELD_NUMBER;
    hash = (53 * hash) + getTaskId().hashCode();
    hash = (37 * hash) + REQID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getReqId());
    if (hasId()) {
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId().hashCode();
    }
    hash = (37 * hash) + MERGEEVER_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getMergeeVer());
    hash = (37 * hash) + STOREID_FIELD_NUMBER;
    hash = (53 * hash) + getStoreId().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basekv.proto.MergeDoneRequest prototype) {
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
   * Protobuf type {@code basekv.MergeDoneRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basekv.MergeDoneRequest)
      com.zachary.bifromq.basekv.proto.MergeDoneRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_MergeDoneRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_MergeDoneRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basekv.proto.MergeDoneRequest.class, com.zachary.bifromq.basekv.proto.MergeDoneRequest.Builder.class);
    }

    // Construct using com.zachary.bifromq.basekv.proto.MergeDoneRequest.newBuilder()
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
      taskId_ = "";
      reqId_ = 0L;
      id_ = null;
      if (idBuilder_ != null) {
        idBuilder_.dispose();
        idBuilder_ = null;
      }
      mergeeVer_ = 0L;
      storeId_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_MergeDoneRequest_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.MergeDoneRequest getDefaultInstanceForType() {
      return com.zachary.bifromq.basekv.proto.MergeDoneRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.MergeDoneRequest build() {
      com.zachary.bifromq.basekv.proto.MergeDoneRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.MergeDoneRequest buildPartial() {
      com.zachary.bifromq.basekv.proto.MergeDoneRequest result = new com.zachary.bifromq.basekv.proto.MergeDoneRequest(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basekv.proto.MergeDoneRequest result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.taskId_ = taskId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.reqId_ = reqId_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.id_ = idBuilder_ == null
            ? id_
            : idBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.mergeeVer_ = mergeeVer_;
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
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
      if (other instanceof com.zachary.bifromq.basekv.proto.MergeDoneRequest) {
        return mergeFrom((com.zachary.bifromq.basekv.proto.MergeDoneRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basekv.proto.MergeDoneRequest other) {
      if (other == com.zachary.bifromq.basekv.proto.MergeDoneRequest.getDefaultInstance()) return this;
      if (!other.getTaskId().isEmpty()) {
        taskId_ = other.taskId_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.getReqId() != 0L) {
        setReqId(other.getReqId());
      }
      if (other.hasId()) {
        mergeId(other.getId());
      }
      if (other.getMergeeVer() != 0L) {
        setMergeeVer(other.getMergeeVer());
      }
      if (!other.getStoreId().isEmpty()) {
        storeId_ = other.storeId_;
        bitField0_ |= 0x00000010;
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
              taskId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 16: {
              reqId_ = input.readUInt64();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 26: {
              input.readMessage(
                  getIdFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 32: {
              mergeeVer_ = input.readUInt64();
              bitField0_ |= 0x00000008;
              break;
            } // case 32
            case 42: {
              storeId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000010;
              break;
            } // case 42
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

    private java.lang.Object taskId_ = "";
    /**
     * <code>string taskId = 1;</code>
     * @return The taskId.
     */
    public java.lang.String getTaskId() {
      java.lang.Object ref = taskId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        taskId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string taskId = 1;</code>
     * @return The bytes for taskId.
     */
    public com.google.protobuf.ByteString
        getTaskIdBytes() {
      java.lang.Object ref = taskId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        taskId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string taskId = 1;</code>
     * @param value The taskId to set.
     * @return This builder for chaining.
     */
    public Builder setTaskId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      taskId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string taskId = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearTaskId() {
      taskId_ = getDefaultInstance().getTaskId();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string taskId = 1;</code>
     * @param value The bytes for taskId to set.
     * @return This builder for chaining.
     */
    public Builder setTaskIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      taskId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private long reqId_ ;
    /**
     * <code>uint64 reqId = 2;</code>
     * @return The reqId.
     */
    @java.lang.Override
    public long getReqId() {
      return reqId_;
    }
    /**
     * <code>uint64 reqId = 2;</code>
     * @param value The reqId to set.
     * @return This builder for chaining.
     */
    public Builder setReqId(long value) {
      
      reqId_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>uint64 reqId = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearReqId() {
      bitField0_ = (bitField0_ & ~0x00000002);
      reqId_ = 0L;
      onChanged();
      return this;
    }

    private com.zachary.bifromq.basekv.proto.KVRangeId id_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basekv.proto.KVRangeId, com.zachary.bifromq.basekv.proto.KVRangeId.Builder, com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder> idBuilder_;
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     * @return Whether the id field is set.
     */
    public boolean hasId() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     * @return The id.
     */
    public com.zachary.bifromq.basekv.proto.KVRangeId getId() {
      if (idBuilder_ == null) {
        return id_ == null ? com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance() : id_;
      } else {
        return idBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     */
    public Builder setId(com.zachary.bifromq.basekv.proto.KVRangeId value) {
      if (idBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        id_ = value;
      } else {
        idBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     */
    public Builder setId(
        com.zachary.bifromq.basekv.proto.KVRangeId.Builder builderForValue) {
      if (idBuilder_ == null) {
        id_ = builderForValue.build();
      } else {
        idBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     */
    public Builder mergeId(com.zachary.bifromq.basekv.proto.KVRangeId value) {
      if (idBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0) &&
          id_ != null &&
          id_ != com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance()) {
          getIdBuilder().mergeFrom(value);
        } else {
          id_ = value;
        }
      } else {
        idBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     */
    public Builder clearId() {
      bitField0_ = (bitField0_ & ~0x00000004);
      id_ = null;
      if (idBuilder_ != null) {
        idBuilder_.dispose();
        idBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVRangeId.Builder getIdBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getIdFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder getIdOrBuilder() {
      if (idBuilder_ != null) {
        return idBuilder_.getMessageOrBuilder();
      } else {
        return id_ == null ?
            com.zachary.bifromq.basekv.proto.KVRangeId.getDefaultInstance() : id_;
      }
    }
    /**
     * <pre>
     * merger's id
     * </pre>
     *
     * <code>.basekv.KVRangeId id = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basekv.proto.KVRangeId, com.zachary.bifromq.basekv.proto.KVRangeId.Builder, com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder> 
        getIdFieldBuilder() {
      if (idBuilder_ == null) {
        idBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basekv.proto.KVRangeId, com.zachary.bifromq.basekv.proto.KVRangeId.Builder, com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder>(
                getId(),
                getParentForChildren(),
                isClean());
        id_ = null;
      }
      return idBuilder_;
    }

    private long mergeeVer_ ;
    /**
     * <code>uint64 mergeeVer = 4;</code>
     * @return The mergeeVer.
     */
    @java.lang.Override
    public long getMergeeVer() {
      return mergeeVer_;
    }
    /**
     * <code>uint64 mergeeVer = 4;</code>
     * @param value The mergeeVer to set.
     * @return This builder for chaining.
     */
    public Builder setMergeeVer(long value) {
      
      mergeeVer_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>uint64 mergeeVer = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearMergeeVer() {
      bitField0_ = (bitField0_ & ~0x00000008);
      mergeeVer_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object storeId_ = "";
    /**
     * <code>string storeId = 5;</code>
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
     * <code>string storeId = 5;</code>
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
     * <code>string storeId = 5;</code>
     * @param value The storeId to set.
     * @return This builder for chaining.
     */
    public Builder setStoreId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      storeId_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    /**
     * <code>string storeId = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearStoreId() {
      storeId_ = getDefaultInstance().getStoreId();
      bitField0_ = (bitField0_ & ~0x00000010);
      onChanged();
      return this;
    }
    /**
     * <code>string storeId = 5;</code>
     * @param value The bytes for storeId to set.
     * @return This builder for chaining.
     */
    public Builder setStoreIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      storeId_ = value;
      bitField0_ |= 0x00000010;
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


    // @@protoc_insertion_point(builder_scope:basekv.MergeDoneRequest)
  }

  // @@protoc_insertion_point(class_scope:basekv.MergeDoneRequest)
  private static final com.zachary.bifromq.basekv.proto.MergeDoneRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basekv.proto.MergeDoneRequest();
  }

  public static com.zachary.bifromq.basekv.proto.MergeDoneRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MergeDoneRequest>
      PARSER = new com.google.protobuf.AbstractParser<MergeDoneRequest>() {
    @java.lang.Override
    public MergeDoneRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<MergeDoneRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MergeDoneRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.MergeDoneRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
