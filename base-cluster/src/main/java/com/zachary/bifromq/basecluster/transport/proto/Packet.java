// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecluster/transport/Packet.proto

package com.zachary.bifromq.basecluster.transport.proto;

/**
 * Protobuf type {@code basecluster.transport.Packet}
 */
public final class Packet extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basecluster.transport.Packet)
    PacketOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Packet.newBuilder() to construct.
  private Packet(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Packet() {
    messages_ = java.util.Collections.emptyList();
    clusterEnv_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Packet();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basecluster.transport.proto.PacketProtos.internal_static_basecluster_transport_Packet_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basecluster.transport.proto.PacketProtos.internal_static_basecluster_transport_Packet_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basecluster.transport.proto.Packet.class, com.zachary.bifromq.basecluster.transport.proto.Packet.Builder.class);
  }

  public static final int MESSAGES_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private java.util.List<com.google.protobuf.ByteString> messages_;
  /**
   * <code>repeated bytes messages = 1;</code>
   * @return A list containing the messages.
   */
  @java.lang.Override
  public java.util.List<com.google.protobuf.ByteString>
      getMessagesList() {
    return messages_;
  }
  /**
   * <code>repeated bytes messages = 1;</code>
   * @return The count of messages.
   */
  public int getMessagesCount() {
    return messages_.size();
  }
  /**
   * <code>repeated bytes messages = 1;</code>
   * @param index The index of the element to return.
   * @return The messages at the given index.
   */
  public com.google.protobuf.ByteString getMessages(int index) {
    return messages_.get(index);
  }

  public static final int CLUSTERENV_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile java.lang.Object clusterEnv_ = "";
  /**
   * <code>string clusterEnv = 2;</code>
   * @return The clusterEnv.
   */
  @java.lang.Override
  public java.lang.String getClusterEnv() {
    java.lang.Object ref = clusterEnv_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      clusterEnv_ = s;
      return s;
    }
  }
  /**
   * <code>string clusterEnv = 2;</code>
   * @return The bytes for clusterEnv.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getClusterEnvBytes() {
    java.lang.Object ref = clusterEnv_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      clusterEnv_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int HLC_FIELD_NUMBER = 3;
  private long hlc_ = 0L;
  /**
   * <code>uint64 hlc = 3;</code>
   * @return The hlc.
   */
  @java.lang.Override
  public long getHlc() {
    return hlc_;
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
    for (int i = 0; i < messages_.size(); i++) {
      output.writeBytes(1, messages_.get(i));
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(clusterEnv_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, clusterEnv_);
    }
    if (hlc_ != 0L) {
      output.writeUInt64(3, hlc_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    {
      int dataSize = 0;
      for (int i = 0; i < messages_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeBytesSizeNoTag(messages_.get(i));
      }
      size += dataSize;
      size += 1 * getMessagesList().size();
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(clusterEnv_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, clusterEnv_);
    }
    if (hlc_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(3, hlc_);
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
    if (!(obj instanceof com.zachary.bifromq.basecluster.transport.proto.Packet)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basecluster.transport.proto.Packet other = (com.zachary.bifromq.basecluster.transport.proto.Packet) obj;

    if (!getMessagesList()
        .equals(other.getMessagesList())) return false;
    if (!getClusterEnv()
        .equals(other.getClusterEnv())) return false;
    if (getHlc()
        != other.getHlc()) return false;
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
    if (getMessagesCount() > 0) {
      hash = (37 * hash) + MESSAGES_FIELD_NUMBER;
      hash = (53 * hash) + getMessagesList().hashCode();
    }
    hash = (37 * hash) + CLUSTERENV_FIELD_NUMBER;
    hash = (53 * hash) + getClusterEnv().hashCode();
    hash = (37 * hash) + HLC_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getHlc());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecluster.transport.proto.Packet parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basecluster.transport.proto.Packet prototype) {
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
   * Protobuf type {@code basecluster.transport.Packet}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basecluster.transport.Packet)
      com.zachary.bifromq.basecluster.transport.proto.PacketOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basecluster.transport.proto.PacketProtos.internal_static_basecluster_transport_Packet_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basecluster.transport.proto.PacketProtos.internal_static_basecluster_transport_Packet_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basecluster.transport.proto.Packet.class, com.zachary.bifromq.basecluster.transport.proto.Packet.Builder.class);
    }

    // Construct using com.zachary.bifromq.basecluster.transport.proto.Packet.newBuilder()
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
      messages_ = java.util.Collections.emptyList();
      clusterEnv_ = "";
      hlc_ = 0L;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basecluster.transport.proto.PacketProtos.internal_static_basecluster_transport_Packet_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.transport.proto.Packet getDefaultInstanceForType() {
      return com.zachary.bifromq.basecluster.transport.proto.Packet.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.transport.proto.Packet build() {
      com.zachary.bifromq.basecluster.transport.proto.Packet result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecluster.transport.proto.Packet buildPartial() {
      com.zachary.bifromq.basecluster.transport.proto.Packet result = new com.zachary.bifromq.basecluster.transport.proto.Packet(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(com.zachary.bifromq.basecluster.transport.proto.Packet result) {
      if (((bitField0_ & 0x00000001) != 0)) {
        messages_ = java.util.Collections.unmodifiableList(messages_);
        bitField0_ = (bitField0_ & ~0x00000001);
      }
      result.messages_ = messages_;
    }

    private void buildPartial0(com.zachary.bifromq.basecluster.transport.proto.Packet result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.clusterEnv_ = clusterEnv_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.hlc_ = hlc_;
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
      if (other instanceof com.zachary.bifromq.basecluster.transport.proto.Packet) {
        return mergeFrom((com.zachary.bifromq.basecluster.transport.proto.Packet)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basecluster.transport.proto.Packet other) {
      if (other == com.zachary.bifromq.basecluster.transport.proto.Packet.getDefaultInstance()) return this;
      if (!other.messages_.isEmpty()) {
        if (messages_.isEmpty()) {
          messages_ = other.messages_;
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          ensureMessagesIsMutable();
          messages_.addAll(other.messages_);
        }
        onChanged();
      }
      if (!other.getClusterEnv().isEmpty()) {
        clusterEnv_ = other.clusterEnv_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      if (other.getHlc() != 0L) {
        setHlc(other.getHlc());
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
              com.google.protobuf.ByteString v = input.readBytes();
              ensureMessagesIsMutable();
              messages_.add(v);
              break;
            } // case 10
            case 18: {
              clusterEnv_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 24: {
              hlc_ = input.readUInt64();
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

    private java.util.List<com.google.protobuf.ByteString> messages_ = java.util.Collections.emptyList();
    private void ensureMessagesIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        messages_ = new java.util.ArrayList<com.google.protobuf.ByteString>(messages_);
        bitField0_ |= 0x00000001;
      }
    }
    /**
     * <code>repeated bytes messages = 1;</code>
     * @return A list containing the messages.
     */
    public java.util.List<com.google.protobuf.ByteString>
        getMessagesList() {
      return ((bitField0_ & 0x00000001) != 0) ?
               java.util.Collections.unmodifiableList(messages_) : messages_;
    }
    /**
     * <code>repeated bytes messages = 1;</code>
     * @return The count of messages.
     */
    public int getMessagesCount() {
      return messages_.size();
    }
    /**
     * <code>repeated bytes messages = 1;</code>
     * @param index The index of the element to return.
     * @return The messages at the given index.
     */
    public com.google.protobuf.ByteString getMessages(int index) {
      return messages_.get(index);
    }
    /**
     * <code>repeated bytes messages = 1;</code>
     * @param index The index to set the value at.
     * @param value The messages to set.
     * @return This builder for chaining.
     */
    public Builder setMessages(
        int index, com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      ensureMessagesIsMutable();
      messages_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated bytes messages = 1;</code>
     * @param value The messages to add.
     * @return This builder for chaining.
     */
    public Builder addMessages(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      ensureMessagesIsMutable();
      messages_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated bytes messages = 1;</code>
     * @param values The messages to add.
     * @return This builder for chaining.
     */
    public Builder addAllMessages(
        java.lang.Iterable<? extends com.google.protobuf.ByteString> values) {
      ensureMessagesIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, messages_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated bytes messages = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearMessages() {
      messages_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }

    private java.lang.Object clusterEnv_ = "";
    /**
     * <code>string clusterEnv = 2;</code>
     * @return The clusterEnv.
     */
    public java.lang.String getClusterEnv() {
      java.lang.Object ref = clusterEnv_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        clusterEnv_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string clusterEnv = 2;</code>
     * @return The bytes for clusterEnv.
     */
    public com.google.protobuf.ByteString
        getClusterEnvBytes() {
      java.lang.Object ref = clusterEnv_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        clusterEnv_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string clusterEnv = 2;</code>
     * @param value The clusterEnv to set.
     * @return This builder for chaining.
     */
    public Builder setClusterEnv(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      clusterEnv_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>string clusterEnv = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearClusterEnv() {
      clusterEnv_ = getDefaultInstance().getClusterEnv();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <code>string clusterEnv = 2;</code>
     * @param value The bytes for clusterEnv to set.
     * @return This builder for chaining.
     */
    public Builder setClusterEnvBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      clusterEnv_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private long hlc_ ;
    /**
     * <code>uint64 hlc = 3;</code>
     * @return The hlc.
     */
    @java.lang.Override
    public long getHlc() {
      return hlc_;
    }
    /**
     * <code>uint64 hlc = 3;</code>
     * @param value The hlc to set.
     * @return This builder for chaining.
     */
    public Builder setHlc(long value) {
      
      hlc_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>uint64 hlc = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearHlc() {
      bitField0_ = (bitField0_ & ~0x00000004);
      hlc_ = 0L;
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


    // @@protoc_insertion_point(builder_scope:basecluster.transport.Packet)
  }

  // @@protoc_insertion_point(class_scope:basecluster.transport.Packet)
  private static final com.zachary.bifromq.basecluster.transport.proto.Packet DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basecluster.transport.proto.Packet();
  }

  public static com.zachary.bifromq.basecluster.transport.proto.Packet getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Packet>
      PARSER = new com.google.protobuf.AbstractParser<Packet>() {
    @java.lang.Override
    public Packet parsePartialFrom(
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

  public static com.google.protobuf.Parser<Packet> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Packet> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basecluster.transport.proto.Packet getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

