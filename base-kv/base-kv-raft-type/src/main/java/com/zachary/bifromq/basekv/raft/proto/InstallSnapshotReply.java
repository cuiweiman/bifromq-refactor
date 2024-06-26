// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/raft/RaftMessage.proto

package com.zachary.bifromq.basekv.raft.proto;

/**
 * Protobuf type {@code basekv.raft.InstallSnapshotReply}
 */
public final class InstallSnapshotReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basekv.raft.InstallSnapshotReply)
    InstallSnapshotReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use InstallSnapshotReply.newBuilder() to construct.
  private InstallSnapshotReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private InstallSnapshotReply() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new InstallSnapshotReply();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basekv.raft.proto.RaftMessageOuterClass.internal_static_basekv_raft_InstallSnapshotReply_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basekv.raft.proto.RaftMessageOuterClass.internal_static_basekv_raft_InstallSnapshotReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply.class, com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply.Builder.class);
  }

  public static final int REJECTED_FIELD_NUMBER = 1;
  private boolean rejected_ = false;
  /**
   * <code>bool rejected = 1;</code>
   * @return The rejected.
   */
  @java.lang.Override
  public boolean getRejected() {
    return rejected_;
  }

  public static final int LASTINDEX_FIELD_NUMBER = 2;
  private long lastIndex_ = 0L;
  /**
   * <pre>
   * last entry index in snapshot
   * </pre>
   *
   * <code>uint64 lastIndex = 2;</code>
   * @return The lastIndex.
   */
  @java.lang.Override
  public long getLastIndex() {
    return lastIndex_;
  }

  public static final int READINDEX_FIELD_NUMBER = 3;
  private long readIndex_ = 0L;
  /**
   * <pre>
   * read index to confirm
   * </pre>
   *
   * <code>uint64 readIndex = 3;</code>
   * @return The readIndex.
   */
  @java.lang.Override
  public long getReadIndex() {
    return readIndex_;
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
    if (rejected_ != false) {
      output.writeBool(1, rejected_);
    }
    if (lastIndex_ != 0L) {
      output.writeUInt64(2, lastIndex_);
    }
    if (readIndex_ != 0L) {
      output.writeUInt64(3, readIndex_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (rejected_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(1, rejected_);
    }
    if (lastIndex_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(2, lastIndex_);
    }
    if (readIndex_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(3, readIndex_);
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
    if (!(obj instanceof com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply other = (com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply) obj;

    if (getRejected()
        != other.getRejected()) return false;
    if (getLastIndex()
        != other.getLastIndex()) return false;
    if (getReadIndex()
        != other.getReadIndex()) return false;
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
    hash = (37 * hash) + REJECTED_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getRejected());
    hash = (37 * hash) + LASTINDEX_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getLastIndex());
    hash = (37 * hash) + READINDEX_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getReadIndex());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply prototype) {
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
   * Protobuf type {@code basekv.raft.InstallSnapshotReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basekv.raft.InstallSnapshotReply)
      com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basekv.raft.proto.RaftMessageOuterClass.internal_static_basekv_raft_InstallSnapshotReply_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basekv.raft.proto.RaftMessageOuterClass.internal_static_basekv_raft_InstallSnapshotReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply.class, com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply.Builder.class);
    }

    // Construct using com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply.newBuilder()
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
      rejected_ = false;
      lastIndex_ = 0L;
      readIndex_ = 0L;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basekv.raft.proto.RaftMessageOuterClass.internal_static_basekv_raft_InstallSnapshotReply_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply getDefaultInstanceForType() {
      return com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply build() {
      com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply buildPartial() {
      com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply result = new com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.rejected_ = rejected_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.lastIndex_ = lastIndex_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.readIndex_ = readIndex_;
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
      if (other instanceof com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply) {
        return mergeFrom((com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply other) {
      if (other == com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply.getDefaultInstance()) return this;
      if (other.getRejected() != false) {
        setRejected(other.getRejected());
      }
      if (other.getLastIndex() != 0L) {
        setLastIndex(other.getLastIndex());
      }
      if (other.getReadIndex() != 0L) {
        setReadIndex(other.getReadIndex());
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
              rejected_ = input.readBool();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              lastIndex_ = input.readUInt64();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              readIndex_ = input.readUInt64();
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

    private boolean rejected_ ;
    /**
     * <code>bool rejected = 1;</code>
     * @return The rejected.
     */
    @java.lang.Override
    public boolean getRejected() {
      return rejected_;
    }
    /**
     * <code>bool rejected = 1;</code>
     * @param value The rejected to set.
     * @return This builder for chaining.
     */
    public Builder setRejected(boolean value) {
      
      rejected_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>bool rejected = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearRejected() {
      bitField0_ = (bitField0_ & ~0x00000001);
      rejected_ = false;
      onChanged();
      return this;
    }

    private long lastIndex_ ;
    /**
     * <pre>
     * last entry index in snapshot
     * </pre>
     *
     * <code>uint64 lastIndex = 2;</code>
     * @return The lastIndex.
     */
    @java.lang.Override
    public long getLastIndex() {
      return lastIndex_;
    }
    /**
     * <pre>
     * last entry index in snapshot
     * </pre>
     *
     * <code>uint64 lastIndex = 2;</code>
     * @param value The lastIndex to set.
     * @return This builder for chaining.
     */
    public Builder setLastIndex(long value) {
      
      lastIndex_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * last entry index in snapshot
     * </pre>
     *
     * <code>uint64 lastIndex = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearLastIndex() {
      bitField0_ = (bitField0_ & ~0x00000002);
      lastIndex_ = 0L;
      onChanged();
      return this;
    }

    private long readIndex_ ;
    /**
     * <pre>
     * read index to confirm
     * </pre>
     *
     * <code>uint64 readIndex = 3;</code>
     * @return The readIndex.
     */
    @java.lang.Override
    public long getReadIndex() {
      return readIndex_;
    }
    /**
     * <pre>
     * read index to confirm
     * </pre>
     *
     * <code>uint64 readIndex = 3;</code>
     * @param value The readIndex to set.
     * @return This builder for chaining.
     */
    public Builder setReadIndex(long value) {
      
      readIndex_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * read index to confirm
     * </pre>
     *
     * <code>uint64 readIndex = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearReadIndex() {
      bitField0_ = (bitField0_ & ~0x00000004);
      readIndex_ = 0L;
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


    // @@protoc_insertion_point(builder_scope:basekv.raft.InstallSnapshotReply)
  }

  // @@protoc_insertion_point(class_scope:basekv.raft.InstallSnapshotReply)
  private static final com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply();
  }

  public static com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<InstallSnapshotReply>
      PARSER = new com.google.protobuf.AbstractParser<InstallSnapshotReply>() {
    @java.lang.Override
    public InstallSnapshotReply parsePartialFrom(
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

  public static com.google.protobuf.Parser<InstallSnapshotReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<InstallSnapshotReply> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basekv.raft.proto.InstallSnapshotReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

