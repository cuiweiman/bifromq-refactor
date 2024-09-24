// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: retainservice/RetainCoProc.proto

package com.zachary.bifromq.retain.rpc.proto;

/**
 * Protobuf type {@code retainservice.MatchCoProcReply}
 */
public final class MatchCoProcReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:retainservice.MatchCoProcReply)
    MatchCoProcReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use MatchCoProcReply.newBuilder() to construct.
  private MatchCoProcReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private MatchCoProcReply() {
    result_ = 0;
    messages_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new MatchCoProcReply();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_MatchCoProcReply_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_MatchCoProcReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.class, com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Builder.class);
  }

  /**
   * Protobuf enum {@code retainservice.MatchCoProcReply.Result}
   */
  public enum Result
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>OK = 0;</code>
     */
    OK(0),
    /**
     * <code>ERROR = 1;</code>
     */
    ERROR(1),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>OK = 0;</code>
     */
    public static final int OK_VALUE = 0;
    /**
     * <code>ERROR = 1;</code>
     */
    public static final int ERROR_VALUE = 1;


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
    public static Result valueOf(int value) {
      return forNumber(value);
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     */
    public static Result forNumber(int value) {
      switch (value) {
        case 0: return OK;
        case 1: return ERROR;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Result>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        Result> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Result>() {
            public Result findValueByNumber(int number) {
              return Result.forNumber(number);
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
      return com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.getDescriptor().getEnumTypes().get(0);
    }

    private static final Result[] VALUES = values();

    public static Result valueOf(
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

    private Result(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:retainservice.MatchCoProcReply.Result)
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

  public static final int RESULT_FIELD_NUMBER = 2;
  private int result_ = 0;
  /**
   * <code>.retainservice.MatchCoProcReply.Result result = 2;</code>
   * @return The enum numeric value on the wire for result.
   */
  @java.lang.Override public int getResultValue() {
    return result_;
  }
  /**
   * <code>.retainservice.MatchCoProcReply.Result result = 2;</code>
   * @return The result.
   */
  @java.lang.Override public com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result getResult() {
    com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result result = com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result.forNumber(result_);
    return result == null ? com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result.UNRECOGNIZED : result;
  }

  public static final int MESSAGES_FIELD_NUMBER = 3;
  @SuppressWarnings("serial")
  private java.util.List<com.zachary.bifromq.type.TopicMessage> messages_;
  /**
   * <code>repeated .commontype.TopicMessage messages = 3;</code>
   */
  @java.lang.Override
  public java.util.List<com.zachary.bifromq.type.TopicMessage> getMessagesList() {
    return messages_;
  }
  /**
   * <code>repeated .commontype.TopicMessage messages = 3;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.zachary.bifromq.type.TopicMessageOrBuilder> 
      getMessagesOrBuilderList() {
    return messages_;
  }
  /**
   * <code>repeated .commontype.TopicMessage messages = 3;</code>
   */
  @java.lang.Override
  public int getMessagesCount() {
    return messages_.size();
  }
  /**
   * <code>repeated .commontype.TopicMessage messages = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.type.TopicMessage getMessages(int index) {
    return messages_.get(index);
  }
  /**
   * <code>repeated .commontype.TopicMessage messages = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.type.TopicMessageOrBuilder getMessagesOrBuilder(
      int index) {
    return messages_.get(index);
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
    if (result_ != com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result.OK.getNumber()) {
      output.writeEnum(2, result_);
    }
    for (int i = 0; i < messages_.size(); i++) {
      output.writeMessage(3, messages_.get(i));
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
    if (result_ != com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result.OK.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(2, result_);
    }
    for (int i = 0; i < messages_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, messages_.get(i));
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
    if (!(obj instanceof com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply other = (com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply) obj;

    if (getReqId()
        != other.getReqId()) return false;
    if (result_ != other.result_) return false;
    if (!getMessagesList()
        .equals(other.getMessagesList())) return false;
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
    hash = (37 * hash) + RESULT_FIELD_NUMBER;
    hash = (53 * hash) + result_;
    if (getMessagesCount() > 0) {
      hash = (37 * hash) + MESSAGES_FIELD_NUMBER;
      hash = (53 * hash) + getMessagesList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply prototype) {
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
   * Protobuf type {@code retainservice.MatchCoProcReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:retainservice.MatchCoProcReply)
      com.zachary.bifromq.retain.rpc.proto.MatchCoProcReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_MatchCoProcReply_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_MatchCoProcReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.class, com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Builder.class);
    }

    // Construct using com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.newBuilder()
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
      result_ = 0;
      if (messagesBuilder_ == null) {
        messages_ = java.util.Collections.emptyList();
      } else {
        messages_ = null;
        messagesBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000004);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.retain.rpc.proto.RetainCoProcProtos.internal_static_retainservice_MatchCoProcReply_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply getDefaultInstanceForType() {
      return com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply build() {
      com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply buildPartial() {
      com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply result = new com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply result) {
      if (messagesBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0)) {
          messages_ = java.util.Collections.unmodifiableList(messages_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.messages_ = messages_;
      } else {
        result.messages_ = messagesBuilder_.build();
      }
    }

    private void buildPartial0(com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.reqId_ = reqId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.result_ = result_;
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
      if (other instanceof com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply) {
        return mergeFrom((com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply other) {
      if (other == com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.getDefaultInstance()) return this;
      if (other.getReqId() != 0L) {
        setReqId(other.getReqId());
      }
      if (other.result_ != 0) {
        setResultValue(other.getResultValue());
      }
      if (messagesBuilder_ == null) {
        if (!other.messages_.isEmpty()) {
          if (messages_.isEmpty()) {
            messages_ = other.messages_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureMessagesIsMutable();
            messages_.addAll(other.messages_);
          }
          onChanged();
        }
      } else {
        if (!other.messages_.isEmpty()) {
          if (messagesBuilder_.isEmpty()) {
            messagesBuilder_.dispose();
            messagesBuilder_ = null;
            messages_ = other.messages_;
            bitField0_ = (bitField0_ & ~0x00000004);
            messagesBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getMessagesFieldBuilder() : null;
          } else {
            messagesBuilder_.addAllMessages(other.messages_);
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
            case 8: {
              reqId_ = input.readUInt64();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              result_ = input.readEnum();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 26: {
              com.zachary.bifromq.type.TopicMessage m =
                  input.readMessage(
                      com.zachary.bifromq.type.TopicMessage.parser(),
                      extensionRegistry);
              if (messagesBuilder_ == null) {
                ensureMessagesIsMutable();
                messages_.add(m);
              } else {
                messagesBuilder_.addMessage(m);
              }
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

    private int result_ = 0;
    /**
     * <code>.retainservice.MatchCoProcReply.Result result = 2;</code>
     * @return The enum numeric value on the wire for result.
     */
    @java.lang.Override public int getResultValue() {
      return result_;
    }
    /**
     * <code>.retainservice.MatchCoProcReply.Result result = 2;</code>
     * @param value The enum numeric value on the wire for result to set.
     * @return This builder for chaining.
     */
    public Builder setResultValue(int value) {
      result_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.retainservice.MatchCoProcReply.Result result = 2;</code>
     * @return The result.
     */
    @java.lang.Override
    public com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result getResult() {
      com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result result = com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result.forNumber(result_);
      return result == null ? com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result.UNRECOGNIZED : result;
    }
    /**
     * <code>.retainservice.MatchCoProcReply.Result result = 2;</code>
     * @param value The result to set.
     * @return This builder for chaining.
     */
    public Builder setResult(com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply.Result value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000002;
      result_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.retainservice.MatchCoProcReply.Result result = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearResult() {
      bitField0_ = (bitField0_ & ~0x00000002);
      result_ = 0;
      onChanged();
      return this;
    }

    private java.util.List<com.zachary.bifromq.type.TopicMessage> messages_ =
      java.util.Collections.emptyList();
    private void ensureMessagesIsMutable() {
      if (!((bitField0_ & 0x00000004) != 0)) {
        messages_ = new java.util.ArrayList<com.zachary.bifromq.type.TopicMessage>(messages_);
        bitField0_ |= 0x00000004;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.zachary.bifromq.type.TopicMessage, com.zachary.bifromq.type.TopicMessage.Builder, com.zachary.bifromq.type.TopicMessageOrBuilder> messagesBuilder_;

    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public java.util.List<com.zachary.bifromq.type.TopicMessage> getMessagesList() {
      if (messagesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(messages_);
      } else {
        return messagesBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public int getMessagesCount() {
      if (messagesBuilder_ == null) {
        return messages_.size();
      } else {
        return messagesBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public com.zachary.bifromq.type.TopicMessage getMessages(int index) {
      if (messagesBuilder_ == null) {
        return messages_.get(index);
      } else {
        return messagesBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder setMessages(
        int index, com.zachary.bifromq.type.TopicMessage value) {
      if (messagesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureMessagesIsMutable();
        messages_.set(index, value);
        onChanged();
      } else {
        messagesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder setMessages(
        int index, com.zachary.bifromq.type.TopicMessage.Builder builderForValue) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        messages_.set(index, builderForValue.build());
        onChanged();
      } else {
        messagesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder addMessages(com.zachary.bifromq.type.TopicMessage value) {
      if (messagesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureMessagesIsMutable();
        messages_.add(value);
        onChanged();
      } else {
        messagesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder addMessages(
        int index, com.zachary.bifromq.type.TopicMessage value) {
      if (messagesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureMessagesIsMutable();
        messages_.add(index, value);
        onChanged();
      } else {
        messagesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder addMessages(
        com.zachary.bifromq.type.TopicMessage.Builder builderForValue) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        messages_.add(builderForValue.build());
        onChanged();
      } else {
        messagesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder addMessages(
        int index, com.zachary.bifromq.type.TopicMessage.Builder builderForValue) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        messages_.add(index, builderForValue.build());
        onChanged();
      } else {
        messagesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder addAllMessages(
        java.lang.Iterable<? extends com.zachary.bifromq.type.TopicMessage> values) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, messages_);
        onChanged();
      } else {
        messagesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder clearMessages() {
      if (messagesBuilder_ == null) {
        messages_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
      } else {
        messagesBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public Builder removeMessages(int index) {
      if (messagesBuilder_ == null) {
        ensureMessagesIsMutable();
        messages_.remove(index);
        onChanged();
      } else {
        messagesBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public com.zachary.bifromq.type.TopicMessage.Builder getMessagesBuilder(
        int index) {
      return getMessagesFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public com.zachary.bifromq.type.TopicMessageOrBuilder getMessagesOrBuilder(
        int index) {
      if (messagesBuilder_ == null) {
        return messages_.get(index);  } else {
        return messagesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public java.util.List<? extends com.zachary.bifromq.type.TopicMessageOrBuilder> 
         getMessagesOrBuilderList() {
      if (messagesBuilder_ != null) {
        return messagesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(messages_);
      }
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public com.zachary.bifromq.type.TopicMessage.Builder addMessagesBuilder() {
      return getMessagesFieldBuilder().addBuilder(
          com.zachary.bifromq.type.TopicMessage.getDefaultInstance());
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public com.zachary.bifromq.type.TopicMessage.Builder addMessagesBuilder(
        int index) {
      return getMessagesFieldBuilder().addBuilder(
          index, com.zachary.bifromq.type.TopicMessage.getDefaultInstance());
    }
    /**
     * <code>repeated .commontype.TopicMessage messages = 3;</code>
     */
    public java.util.List<com.zachary.bifromq.type.TopicMessage.Builder> 
         getMessagesBuilderList() {
      return getMessagesFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.zachary.bifromq.type.TopicMessage, com.zachary.bifromq.type.TopicMessage.Builder, com.zachary.bifromq.type.TopicMessageOrBuilder> 
        getMessagesFieldBuilder() {
      if (messagesBuilder_ == null) {
        messagesBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.zachary.bifromq.type.TopicMessage, com.zachary.bifromq.type.TopicMessage.Builder, com.zachary.bifromq.type.TopicMessageOrBuilder>(
                messages_,
                ((bitField0_ & 0x00000004) != 0),
                getParentForChildren(),
                isClean());
        messages_ = null;
      }
      return messagesBuilder_;
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


    // @@protoc_insertion_point(builder_scope:retainservice.MatchCoProcReply)
  }

  // @@protoc_insertion_point(class_scope:retainservice.MatchCoProcReply)
  private static final com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply();
  }

  public static com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MatchCoProcReply>
      PARSER = new com.google.protobuf.AbstractParser<MatchCoProcReply>() {
    @java.lang.Override
    public MatchCoProcReply parsePartialFrom(
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

  public static com.google.protobuf.Parser<MatchCoProcReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MatchCoProcReply> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.retain.rpc.proto.MatchCoProcReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

