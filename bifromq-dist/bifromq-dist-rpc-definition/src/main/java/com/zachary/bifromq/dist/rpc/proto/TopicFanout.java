// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/TopicFanout.proto

package com.zachary.bifromq.dist.rpc.proto;

/**
 * Protobuf type {@code distservice.TopicFanout}
 */
public final class TopicFanout extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:distservice.TopicFanout)
    TopicFanoutOrBuilder {
private static final long serialVersionUID = 0L;
  // Use TopicFanout.newBuilder() to construct.
  private TopicFanout(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private TopicFanout() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new TopicFanout();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.dist.rpc.proto.TopicFanoutProtos.internal_static_distservice_TopicFanout_descriptor;
  }

  @SuppressWarnings({"rawtypes"})
  @java.lang.Override
  protected com.google.protobuf.MapField internalGetMapField(
      int number) {
    switch (number) {
      case 1:
        return internalGetFanout();
      default:
        throw new RuntimeException(
            "Invalid map field number: " + number);
    }
  }
  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.dist.rpc.proto.TopicFanoutProtos.internal_static_distservice_TopicFanout_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.dist.rpc.proto.TopicFanout.class, com.zachary.bifromq.dist.rpc.proto.TopicFanout.Builder.class);
  }

  public static final int FANOUT_FIELD_NUMBER = 1;
  private static final class FanoutDefaultEntryHolder {
    static final com.google.protobuf.MapEntry<
        java.lang.String, java.lang.Integer> defaultEntry =
            com.google.protobuf.MapEntry
            .<java.lang.String, java.lang.Integer>newDefaultInstance(
                com.zachary.bifromq.dist.rpc.proto.TopicFanoutProtos.internal_static_distservice_TopicFanout_FanoutEntry_descriptor, 
                com.google.protobuf.WireFormat.FieldType.STRING,
                "",
                com.google.protobuf.WireFormat.FieldType.UINT32,
                0);
  }
  @SuppressWarnings("serial")
  private com.google.protobuf.MapField<
      java.lang.String, java.lang.Integer> fanout_;
  private com.google.protobuf.MapField<java.lang.String, java.lang.Integer>
  internalGetFanout() {
    if (fanout_ == null) {
      return com.google.protobuf.MapField.emptyMapField(
          FanoutDefaultEntryHolder.defaultEntry);
    }
    return fanout_;
  }
  public int getFanoutCount() {
    return internalGetFanout().getMap().size();
  }
  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  @java.lang.Override
  public boolean containsFanout(
      java.lang.String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    return internalGetFanout().getMap().containsKey(key);
  }
  /**
   * Use {@link #getFanoutMap()} instead.
   */
  @java.lang.Override
  @java.lang.Deprecated
  public java.util.Map<java.lang.String, java.lang.Integer> getFanout() {
    return getFanoutMap();
  }
  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  @java.lang.Override
  public java.util.Map<java.lang.String, java.lang.Integer> getFanoutMap() {
    return internalGetFanout().getMap();
  }
  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  @java.lang.Override
  public int getFanoutOrDefault(
      java.lang.String key,
      int defaultValue) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<java.lang.String, java.lang.Integer> map =
        internalGetFanout().getMap();
    return map.containsKey(key) ? map.get(key) : defaultValue;
  }
  /**
   * <pre>
   * key: topic
   * </pre>
   *
   * <code>map&lt;string, uint32&gt; fanout = 1;</code>
   */
  @java.lang.Override
  public int getFanoutOrThrow(
      java.lang.String key) {
    if (key == null) { throw new NullPointerException("map key"); }
    java.util.Map<java.lang.String, java.lang.Integer> map =
        internalGetFanout().getMap();
    if (!map.containsKey(key)) {
      throw new java.lang.IllegalArgumentException();
    }
    return map.get(key);
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
    com.google.protobuf.GeneratedMessageV3
      .serializeStringMapTo(
        output,
        internalGetFanout(),
        FanoutDefaultEntryHolder.defaultEntry,
        1);
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry
         : internalGetFanout().getMap().entrySet()) {
      com.google.protobuf.MapEntry<java.lang.String, java.lang.Integer>
      fanout__ = FanoutDefaultEntryHolder.defaultEntry.newBuilderForType()
          .setKey(entry.getKey())
          .setValue(entry.getValue())
          .build();
      size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, fanout__);
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
    if (!(obj instanceof com.zachary.bifromq.dist.rpc.proto.TopicFanout)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.dist.rpc.proto.TopicFanout other = (com.zachary.bifromq.dist.rpc.proto.TopicFanout) obj;

    if (!internalGetFanout().equals(
        other.internalGetFanout())) return false;
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
    if (!internalGetFanout().getMap().isEmpty()) {
      hash = (37 * hash) + FANOUT_FIELD_NUMBER;
      hash = (53 * hash) + internalGetFanout().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.dist.rpc.proto.TopicFanout prototype) {
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
   * Protobuf type {@code distservice.TopicFanout}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:distservice.TopicFanout)
      com.zachary.bifromq.dist.rpc.proto.TopicFanoutOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.dist.rpc.proto.TopicFanoutProtos.internal_static_distservice_TopicFanout_descriptor;
    }

    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMapField(
        int number) {
      switch (number) {
        case 1:
          return internalGetFanout();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @SuppressWarnings({"rawtypes"})
    protected com.google.protobuf.MapField internalGetMutableMapField(
        int number) {
      switch (number) {
        case 1:
          return internalGetMutableFanout();
        default:
          throw new RuntimeException(
              "Invalid map field number: " + number);
      }
    }
    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.dist.rpc.proto.TopicFanoutProtos.internal_static_distservice_TopicFanout_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.dist.rpc.proto.TopicFanout.class, com.zachary.bifromq.dist.rpc.proto.TopicFanout.Builder.class);
    }

    // Construct using com.zachary.bifromq.dist.rpc.proto.TopicFanout.newBuilder()
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
      internalGetMutableFanout().clear();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.dist.rpc.proto.TopicFanoutProtos.internal_static_distservice_TopicFanout_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.TopicFanout getDefaultInstanceForType() {
      return com.zachary.bifromq.dist.rpc.proto.TopicFanout.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.TopicFanout build() {
      com.zachary.bifromq.dist.rpc.proto.TopicFanout result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.TopicFanout buildPartial() {
      com.zachary.bifromq.dist.rpc.proto.TopicFanout result = new com.zachary.bifromq.dist.rpc.proto.TopicFanout(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.dist.rpc.proto.TopicFanout result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.fanout_ = internalGetFanout();
        result.fanout_.makeImmutable();
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
      if (other instanceof com.zachary.bifromq.dist.rpc.proto.TopicFanout) {
        return mergeFrom((com.zachary.bifromq.dist.rpc.proto.TopicFanout)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.dist.rpc.proto.TopicFanout other) {
      if (other == com.zachary.bifromq.dist.rpc.proto.TopicFanout.getDefaultInstance()) return this;
      internalGetMutableFanout().mergeFrom(
          other.internalGetFanout());
      bitField0_ |= 0x00000001;
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
              com.google.protobuf.MapEntry<java.lang.String, java.lang.Integer>
              fanout__ = input.readMessage(
                  FanoutDefaultEntryHolder.defaultEntry.getParserForType(), extensionRegistry);
              internalGetMutableFanout().getMutableMap().put(
                  fanout__.getKey(), fanout__.getValue());
              bitField0_ |= 0x00000001;
              break;
            } // case 10
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

    private com.google.protobuf.MapField<
        java.lang.String, java.lang.Integer> fanout_;
    private com.google.protobuf.MapField<java.lang.String, java.lang.Integer>
        internalGetFanout() {
      if (fanout_ == null) {
        return com.google.protobuf.MapField.emptyMapField(
            FanoutDefaultEntryHolder.defaultEntry);
      }
      return fanout_;
    }
    private com.google.protobuf.MapField<java.lang.String, java.lang.Integer>
        internalGetMutableFanout() {
      if (fanout_ == null) {
        fanout_ = com.google.protobuf.MapField.newMapField(
            FanoutDefaultEntryHolder.defaultEntry);
      }
      if (!fanout_.isMutable()) {
        fanout_ = fanout_.copy();
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return fanout_;
    }
    public int getFanoutCount() {
      return internalGetFanout().getMap().size();
    }
    /**
     * <pre>
     * key: topic
     * </pre>
     *
     * <code>map&lt;string, uint32&gt; fanout = 1;</code>
     */
    @java.lang.Override
    public boolean containsFanout(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      return internalGetFanout().getMap().containsKey(key);
    }
    /**
     * Use {@link #getFanoutMap()} instead.
     */
    @java.lang.Override
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.Integer> getFanout() {
      return getFanoutMap();
    }
    /**
     * <pre>
     * key: topic
     * </pre>
     *
     * <code>map&lt;string, uint32&gt; fanout = 1;</code>
     */
    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Integer> getFanoutMap() {
      return internalGetFanout().getMap();
    }
    /**
     * <pre>
     * key: topic
     * </pre>
     *
     * <code>map&lt;string, uint32&gt; fanout = 1;</code>
     */
    @java.lang.Override
    public int getFanoutOrDefault(
        java.lang.String key,
        int defaultValue) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<java.lang.String, java.lang.Integer> map =
          internalGetFanout().getMap();
      return map.containsKey(key) ? map.get(key) : defaultValue;
    }
    /**
     * <pre>
     * key: topic
     * </pre>
     *
     * <code>map&lt;string, uint32&gt; fanout = 1;</code>
     */
    @java.lang.Override
    public int getFanoutOrThrow(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      java.util.Map<java.lang.String, java.lang.Integer> map =
          internalGetFanout().getMap();
      if (!map.containsKey(key)) {
        throw new java.lang.IllegalArgumentException();
      }
      return map.get(key);
    }
    public Builder clearFanout() {
      bitField0_ = (bitField0_ & ~0x00000001);
      internalGetMutableFanout().getMutableMap()
          .clear();
      return this;
    }
    /**
     * <pre>
     * key: topic
     * </pre>
     *
     * <code>map&lt;string, uint32&gt; fanout = 1;</code>
     */
    public Builder removeFanout(
        java.lang.String key) {
      if (key == null) { throw new NullPointerException("map key"); }
      internalGetMutableFanout().getMutableMap()
          .remove(key);
      return this;
    }
    /**
     * Use alternate mutation accessors instead.
     */
    @java.lang.Deprecated
    public java.util.Map<java.lang.String, java.lang.Integer>
        getMutableFanout() {
      bitField0_ |= 0x00000001;
      return internalGetMutableFanout().getMutableMap();
    }
    /**
     * <pre>
     * key: topic
     * </pre>
     *
     * <code>map&lt;string, uint32&gt; fanout = 1;</code>
     */
    public Builder putFanout(
        java.lang.String key,
        int value) {
      if (key == null) { throw new NullPointerException("map key"); }
      
      internalGetMutableFanout().getMutableMap()
          .put(key, value);
      bitField0_ |= 0x00000001;
      return this;
    }
    /**
     * <pre>
     * key: topic
     * </pre>
     *
     * <code>map&lt;string, uint32&gt; fanout = 1;</code>
     */
    public Builder putAllFanout(
        java.util.Map<java.lang.String, java.lang.Integer> values) {
      internalGetMutableFanout().getMutableMap()
          .putAll(values);
      bitField0_ |= 0x00000001;
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


    // @@protoc_insertion_point(builder_scope:distservice.TopicFanout)
  }

  // @@protoc_insertion_point(class_scope:distservice.TopicFanout)
  private static final com.zachary.bifromq.dist.rpc.proto.TopicFanout DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.dist.rpc.proto.TopicFanout();
  }

  public static com.zachary.bifromq.dist.rpc.proto.TopicFanout getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<TopicFanout>
      PARSER = new com.google.protobuf.AbstractParser<TopicFanout>() {
    @java.lang.Override
    public TopicFanout parsePartialFrom(
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

  public static com.google.protobuf.Parser<TopicFanout> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<TopicFanout> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.dist.rpc.proto.TopicFanout getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
