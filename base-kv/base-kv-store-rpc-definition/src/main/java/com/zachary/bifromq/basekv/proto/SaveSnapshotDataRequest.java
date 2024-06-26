// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/StoreMessage.proto

package com.zachary.bifromq.basekv.proto;

/**
 * Protobuf type {@code basekv.SaveSnapshotDataRequest}
 */
public final class SaveSnapshotDataRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basekv.SaveSnapshotDataRequest)
    SaveSnapshotDataRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use SaveSnapshotDataRequest.newBuilder() to construct.
  private SaveSnapshotDataRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private SaveSnapshotDataRequest() {
    sessionId_ = "";
    flag_ = 0;
    kv_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new SaveSnapshotDataRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_SaveSnapshotDataRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_SaveSnapshotDataRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.class, com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Builder.class);
  }

  /**
   * Protobuf enum {@code basekv.SaveSnapshotDataRequest.Flag}
   */
  public enum Flag
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>More = 0;</code>
     */
    More(0),
    /**
     * <code>End = 1;</code>
     */
    End(1),
    /**
     * <code>Error = 2;</code>
     */
    Error(2),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>More = 0;</code>
     */
    public static final int More_VALUE = 0;
    /**
     * <code>End = 1;</code>
     */
    public static final int End_VALUE = 1;
    /**
     * <code>Error = 2;</code>
     */
    public static final int Error_VALUE = 2;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static Flag valueOf(int value) {
      return forNumber(value);
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     */
    public static Flag forNumber(int value) {
      switch (value) {
        case 0: return More;
        case 1: return End;
        case 2: return Error;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Flag>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        Flag> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Flag>() {
            public Flag findValueByNumber(int number) {
              return Flag.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalStateException(
            "Can't get the descriptor of an unrecognized enum value.");
      }
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.getDescriptor().getEnumTypes().get(0);
    }

    private static final Flag[] VALUES = values();

    public static Flag valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private Flag(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:basekv.SaveSnapshotDataRequest.Flag)
  }

  public static final int SESSIONID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object sessionId_ = "";
  /**
   * <code>string sessionId = 1;</code>
   * @return The sessionId.
   */
  @java.lang.Override
  public java.lang.String getSessionId() {
    java.lang.Object ref = sessionId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      sessionId_ = s;
      return s;
    }
  }
  /**
   * <code>string sessionId = 1;</code>
   * @return The bytes for sessionId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getSessionIdBytes() {
    java.lang.Object ref = sessionId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      sessionId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int REQID_FIELD_NUMBER = 2;
  private int reqId_ = 0;
  /**
   * <code>uint32 reqId = 2;</code>
   * @return The reqId.
   */
  @java.lang.Override
  public int getReqId() {
    return reqId_;
  }

  public static final int FLAG_FIELD_NUMBER = 3;
  private int flag_ = 0;
  /**
   * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
   * @return The enum numeric value on the wire for flag.
   */
  @java.lang.Override public int getFlagValue() {
    return flag_;
  }
  /**
   * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
   * @return The flag.
   */
  @java.lang.Override public com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag getFlag() {
    com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag result = com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag.forNumber(flag_);
    return result == null ? com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag.UNRECOGNIZED : result;
  }

  public static final int KV_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private java.util.List<com.zachary.bifromq.basekv.proto.KVPair> kv_;
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  @java.lang.Override
  public java.util.List<com.zachary.bifromq.basekv.proto.KVPair> getKvList() {
    return kv_;
  }
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.zachary.bifromq.basekv.proto.KVPairOrBuilder> 
      getKvOrBuilderList() {
    return kv_;
  }
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  @java.lang.Override
  public int getKvCount() {
    return kv_.size();
  }
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.KVPair getKv(int index) {
    return kv_.get(index);
  }
  /**
   * <code>repeated .basekv.KVPair kv = 4;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.KVPairOrBuilder getKvOrBuilder(
      int index) {
    return kv_.get(index);
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(sessionId_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, sessionId_);
    }
    if (reqId_ != 0) {
      output.writeUInt32(2, reqId_);
    }
    if (flag_ != com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag.More.getNumber()) {
      output.writeEnum(3, flag_);
    }
    for (int i = 0; i < kv_.size(); i++) {
      output.writeMessage(4, kv_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(sessionId_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, sessionId_);
    }
    if (reqId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(2, reqId_);
    }
    if (flag_ != com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag.More.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(3, flag_);
    }
    for (int i = 0; i < kv_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, kv_.get(i));
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
    if (!(obj instanceof com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest other = (com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest) obj;

    if (!getSessionId()
        .equals(other.getSessionId())) return false;
    if (getReqId()
        != other.getReqId()) return false;
    if (flag_ != other.flag_) return false;
    if (!getKvList()
        .equals(other.getKvList())) return false;
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
    hash = (37 * hash) + SESSIONID_FIELD_NUMBER;
    hash = (53 * hash) + getSessionId().hashCode();
    hash = (37 * hash) + REQID_FIELD_NUMBER;
    hash = (53 * hash) + getReqId();
    hash = (37 * hash) + FLAG_FIELD_NUMBER;
    hash = (53 * hash) + flag_;
    if (getKvCount() > 0) {
      hash = (37 * hash) + KV_FIELD_NUMBER;
      hash = (53 * hash) + getKvList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest prototype) {
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
   * Protobuf type {@code basekv.SaveSnapshotDataRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basekv.SaveSnapshotDataRequest)
      com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_SaveSnapshotDataRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_SaveSnapshotDataRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.class, com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Builder.class);
    }

    // Construct using com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.newBuilder()
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
      sessionId_ = "";
      reqId_ = 0;
      flag_ = 0;
      if (kvBuilder_ == null) {
        kv_ = java.util.Collections.emptyList();
      } else {
        kv_ = null;
        kvBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000008);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basekv.proto.StoreMessageOuterClass.internal_static_basekv_SaveSnapshotDataRequest_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest getDefaultInstanceForType() {
      return com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest build() {
      com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest buildPartial() {
      com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest result = new com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest result) {
      if (kvBuilder_ == null) {
        if (((bitField0_ & 0x00000008) != 0)) {
          kv_ = java.util.Collections.unmodifiableList(kv_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.kv_ = kv_;
      } else {
        result.kv_ = kvBuilder_.build();
      }
    }

    private void buildPartial0(com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.sessionId_ = sessionId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.reqId_ = reqId_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.flag_ = flag_;
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
      if (other instanceof com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest) {
        return mergeFrom((com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest other) {
      if (other == com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.getDefaultInstance()) return this;
      if (!other.getSessionId().isEmpty()) {
        sessionId_ = other.sessionId_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.getReqId() != 0) {
        setReqId(other.getReqId());
      }
      if (other.flag_ != 0) {
        setFlagValue(other.getFlagValue());
      }
      if (kvBuilder_ == null) {
        if (!other.kv_.isEmpty()) {
          if (kv_.isEmpty()) {
            kv_ = other.kv_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensureKvIsMutable();
            kv_.addAll(other.kv_);
          }
          onChanged();
        }
      } else {
        if (!other.kv_.isEmpty()) {
          if (kvBuilder_.isEmpty()) {
            kvBuilder_.dispose();
            kvBuilder_ = null;
            kv_ = other.kv_;
            bitField0_ = (bitField0_ & ~0x00000008);
            kvBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getKvFieldBuilder() : null;
          } else {
            kvBuilder_.addAllMessages(other.kv_);
          }
        }
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
              sessionId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 16: {
              reqId_ = input.readUInt32();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              flag_ = input.readEnum();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 34: {
              com.zachary.bifromq.basekv.proto.KVPair m =
                  input.readMessage(
                      com.zachary.bifromq.basekv.proto.KVPair.parser(),
                      extensionRegistry);
              if (kvBuilder_ == null) {
                ensureKvIsMutable();
                kv_.add(m);
              } else {
                kvBuilder_.addMessage(m);
              }
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

    private java.lang.Object sessionId_ = "";
    /**
     * <code>string sessionId = 1;</code>
     * @return The sessionId.
     */
    public java.lang.String getSessionId() {
      java.lang.Object ref = sessionId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        sessionId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string sessionId = 1;</code>
     * @return The bytes for sessionId.
     */
    public com.google.protobuf.ByteString
        getSessionIdBytes() {
      java.lang.Object ref = sessionId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        sessionId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string sessionId = 1;</code>
     * @param value The sessionId to set.
     * @return This builder for chaining.
     */
    public Builder setSessionId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      sessionId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string sessionId = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearSessionId() {
      sessionId_ = getDefaultInstance().getSessionId();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string sessionId = 1;</code>
     * @param value The bytes for sessionId to set.
     * @return This builder for chaining.
     */
    public Builder setSessionIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      sessionId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private int reqId_ ;
    /**
     * <code>uint32 reqId = 2;</code>
     * @return The reqId.
     */
    @java.lang.Override
    public int getReqId() {
      return reqId_;
    }
    /**
     * <code>uint32 reqId = 2;</code>
     * @param value The reqId to set.
     * @return This builder for chaining.
     */
    public Builder setReqId(int value) {
      
      reqId_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>uint32 reqId = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearReqId() {
      bitField0_ = (bitField0_ & ~0x00000002);
      reqId_ = 0;
      onChanged();
      return this;
    }

    private int flag_ = 0;
    /**
     * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
     * @return The enum numeric value on the wire for flag.
     */
    @java.lang.Override public int getFlagValue() {
      return flag_;
    }
    /**
     * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
     * @param value The enum numeric value on the wire for flag to set.
     * @return This builder for chaining.
     */
    public Builder setFlagValue(int value) {
      flag_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
     * @return The flag.
     */
    @java.lang.Override
    public com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag getFlag() {
      com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag result = com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag.forNumber(flag_);
      return result == null ? com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag.UNRECOGNIZED : result;
    }
    /**
     * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
     * @param value The flag to set.
     * @return This builder for chaining.
     */
    public Builder setFlag(com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest.Flag value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000004;
      flag_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.basekv.SaveSnapshotDataRequest.Flag flag = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearFlag() {
      bitField0_ = (bitField0_ & ~0x00000004);
      flag_ = 0;
      onChanged();
      return this;
    }

    private java.util.List<com.zachary.bifromq.basekv.proto.KVPair> kv_ =
      java.util.Collections.emptyList();
    private void ensureKvIsMutable() {
      if (!((bitField0_ & 0x00000008) != 0)) {
        kv_ = new java.util.ArrayList<com.zachary.bifromq.basekv.proto.KVPair>(kv_);
        bitField0_ |= 0x00000008;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.zachary.bifromq.basekv.proto.KVPair, com.zachary.bifromq.basekv.proto.KVPair.Builder, com.zachary.bifromq.basekv.proto.KVPairOrBuilder> kvBuilder_;

    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public java.util.List<com.zachary.bifromq.basekv.proto.KVPair> getKvList() {
      if (kvBuilder_ == null) {
        return java.util.Collections.unmodifiableList(kv_);
      } else {
        return kvBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public int getKvCount() {
      if (kvBuilder_ == null) {
        return kv_.size();
      } else {
        return kvBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVPair getKv(int index) {
      if (kvBuilder_ == null) {
        return kv_.get(index);
      } else {
        return kvBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder setKv(
        int index, com.zachary.bifromq.basekv.proto.KVPair value) {
      if (kvBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKvIsMutable();
        kv_.set(index, value);
        onChanged();
      } else {
        kvBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder setKv(
        int index, com.zachary.bifromq.basekv.proto.KVPair.Builder builderForValue) {
      if (kvBuilder_ == null) {
        ensureKvIsMutable();
        kv_.set(index, builderForValue.build());
        onChanged();
      } else {
        kvBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder addKv(com.zachary.bifromq.basekv.proto.KVPair value) {
      if (kvBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKvIsMutable();
        kv_.add(value);
        onChanged();
      } else {
        kvBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder addKv(
        int index, com.zachary.bifromq.basekv.proto.KVPair value) {
      if (kvBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureKvIsMutable();
        kv_.add(index, value);
        onChanged();
      } else {
        kvBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder addKv(
        com.zachary.bifromq.basekv.proto.KVPair.Builder builderForValue) {
      if (kvBuilder_ == null) {
        ensureKvIsMutable();
        kv_.add(builderForValue.build());
        onChanged();
      } else {
        kvBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder addKv(
        int index, com.zachary.bifromq.basekv.proto.KVPair.Builder builderForValue) {
      if (kvBuilder_ == null) {
        ensureKvIsMutable();
        kv_.add(index, builderForValue.build());
        onChanged();
      } else {
        kvBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder addAllKv(
        java.lang.Iterable<? extends com.zachary.bifromq.basekv.proto.KVPair> values) {
      if (kvBuilder_ == null) {
        ensureKvIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, kv_);
        onChanged();
      } else {
        kvBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder clearKv() {
      if (kvBuilder_ == null) {
        kv_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
      } else {
        kvBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public Builder removeKv(int index) {
      if (kvBuilder_ == null) {
        ensureKvIsMutable();
        kv_.remove(index);
        onChanged();
      } else {
        kvBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVPair.Builder getKvBuilder(
        int index) {
      return getKvFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVPairOrBuilder getKvOrBuilder(
        int index) {
      if (kvBuilder_ == null) {
        return kv_.get(index);  } else {
        return kvBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public java.util.List<? extends com.zachary.bifromq.basekv.proto.KVPairOrBuilder> 
         getKvOrBuilderList() {
      if (kvBuilder_ != null) {
        return kvBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(kv_);
      }
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVPair.Builder addKvBuilder() {
      return getKvFieldBuilder().addBuilder(
          com.zachary.bifromq.basekv.proto.KVPair.getDefaultInstance());
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public com.zachary.bifromq.basekv.proto.KVPair.Builder addKvBuilder(
        int index) {
      return getKvFieldBuilder().addBuilder(
          index, com.zachary.bifromq.basekv.proto.KVPair.getDefaultInstance());
    }
    /**
     * <code>repeated .basekv.KVPair kv = 4;</code>
     */
    public java.util.List<com.zachary.bifromq.basekv.proto.KVPair.Builder> 
         getKvBuilderList() {
      return getKvFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.zachary.bifromq.basekv.proto.KVPair, com.zachary.bifromq.basekv.proto.KVPair.Builder, com.zachary.bifromq.basekv.proto.KVPairOrBuilder> 
        getKvFieldBuilder() {
      if (kvBuilder_ == null) {
        kvBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.zachary.bifromq.basekv.proto.KVPair, com.zachary.bifromq.basekv.proto.KVPair.Builder, com.zachary.bifromq.basekv.proto.KVPairOrBuilder>(
                kv_,
                ((bitField0_ & 0x00000008) != 0),
                getParentForChildren(),
                isClean());
        kv_ = null;
      }
      return kvBuilder_;
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


    // @@protoc_insertion_point(builder_scope:basekv.SaveSnapshotDataRequest)
  }

  // @@protoc_insertion_point(class_scope:basekv.SaveSnapshotDataRequest)
  private static final com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest();
  }

  public static com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<SaveSnapshotDataRequest>
      PARSER = new com.google.protobuf.AbstractParser<SaveSnapshotDataRequest>() {
    @java.lang.Override
    public SaveSnapshotDataRequest parsePartialFrom(
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

  public static com.google.protobuf.Parser<SaveSnapshotDataRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<SaveSnapshotDataRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

