// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basecrdt/Dot.proto

package com.zachary.bifromq.basecrdt.proto;

/**
 * <pre>
 **
 *oneof 定义多个字段，但只能选择一个进行赋值的结构，设置了其中一个成员，其他成员将被自动清除。
 * </pre>
 *
 * Protobuf type {@code basecrdt.StateLattice}
 */
public final class StateLattice extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:basecrdt.StateLattice)
    StateLatticeOrBuilder {
private static final long serialVersionUID = 0L;
  // Use StateLattice.newBuilder() to construct.
  private StateLattice(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private StateLattice() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new StateLattice();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.basecrdt.proto.DotOuterClass.internal_static_basecrdt_StateLattice_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.basecrdt.proto.DotOuterClass.internal_static_basecrdt_StateLattice_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.basecrdt.proto.StateLattice.class, com.zachary.bifromq.basecrdt.proto.StateLattice.Builder.class);
  }

  private int stateTypeCase_ = 0;
  private java.lang.Object stateType_;
  public enum StateTypeCase
      implements com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    SINGLEDOT(1),
    SINGLEVALUE(2),
    SINGLEMAP(3),
    STATETYPE_NOT_SET(0);
    private final int value;
    private StateTypeCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static StateTypeCase valueOf(int value) {
      return forNumber(value);
    }

    public static StateTypeCase forNumber(int value) {
      switch (value) {
        case 1: return SINGLEDOT;
        case 2: return SINGLEVALUE;
        case 3: return SINGLEMAP;
        case 0: return STATETYPE_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public StateTypeCase
  getStateTypeCase() {
    return StateTypeCase.forNumber(
        stateTypeCase_);
  }

  public static final int SINGLEDOT_FIELD_NUMBER = 1;
  /**
   * <code>.basecrdt.SingleDot singleDot = 1;</code>
   * @return Whether the singleDot field is set.
   */
  @java.lang.Override
  public boolean hasSingleDot() {
    return stateTypeCase_ == 1;
  }
  /**
   * <code>.basecrdt.SingleDot singleDot = 1;</code>
   * @return The singleDot.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.proto.SingleDot getSingleDot() {
    if (stateTypeCase_ == 1) {
       return (com.zachary.bifromq.basecrdt.proto.SingleDot) stateType_;
    }
    return com.zachary.bifromq.basecrdt.proto.SingleDot.getDefaultInstance();
  }
  /**
   * <code>.basecrdt.SingleDot singleDot = 1;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.proto.SingleDotOrBuilder getSingleDotOrBuilder() {
    if (stateTypeCase_ == 1) {
       return (com.zachary.bifromq.basecrdt.proto.SingleDot) stateType_;
    }
    return com.zachary.bifromq.basecrdt.proto.SingleDot.getDefaultInstance();
  }

  public static final int SINGLEVALUE_FIELD_NUMBER = 2;
  /**
   * <code>.basecrdt.SingleValue singleValue = 2;</code>
   * @return Whether the singleValue field is set.
   */
  @java.lang.Override
  public boolean hasSingleValue() {
    return stateTypeCase_ == 2;
  }
  /**
   * <code>.basecrdt.SingleValue singleValue = 2;</code>
   * @return The singleValue.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.proto.SingleValue getSingleValue() {
    if (stateTypeCase_ == 2) {
       return (com.zachary.bifromq.basecrdt.proto.SingleValue) stateType_;
    }
    return com.zachary.bifromq.basecrdt.proto.SingleValue.getDefaultInstance();
  }
  /**
   * <code>.basecrdt.SingleValue singleValue = 2;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.proto.SingleValueOrBuilder getSingleValueOrBuilder() {
    if (stateTypeCase_ == 2) {
       return (com.zachary.bifromq.basecrdt.proto.SingleValue) stateType_;
    }
    return com.zachary.bifromq.basecrdt.proto.SingleValue.getDefaultInstance();
  }

  public static final int SINGLEMAP_FIELD_NUMBER = 3;
  /**
   * <code>.basecrdt.SingleMap singleMap = 3;</code>
   * @return Whether the singleMap field is set.
   */
  @java.lang.Override
  public boolean hasSingleMap() {
    return stateTypeCase_ == 3;
  }
  /**
   * <code>.basecrdt.SingleMap singleMap = 3;</code>
   * @return The singleMap.
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.proto.SingleMap getSingleMap() {
    if (stateTypeCase_ == 3) {
       return (com.zachary.bifromq.basecrdt.proto.SingleMap) stateType_;
    }
    return com.zachary.bifromq.basecrdt.proto.SingleMap.getDefaultInstance();
  }
  /**
   * <code>.basecrdt.SingleMap singleMap = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.basecrdt.proto.SingleMapOrBuilder getSingleMapOrBuilder() {
    if (stateTypeCase_ == 3) {
       return (com.zachary.bifromq.basecrdt.proto.SingleMap) stateType_;
    }
    return com.zachary.bifromq.basecrdt.proto.SingleMap.getDefaultInstance();
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
    if (stateTypeCase_ == 1) {
      output.writeMessage(1, (com.zachary.bifromq.basecrdt.proto.SingleDot) stateType_);
    }
    if (stateTypeCase_ == 2) {
      output.writeMessage(2, (com.zachary.bifromq.basecrdt.proto.SingleValue) stateType_);
    }
    if (stateTypeCase_ == 3) {
      output.writeMessage(3, (com.zachary.bifromq.basecrdt.proto.SingleMap) stateType_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (stateTypeCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (com.zachary.bifromq.basecrdt.proto.SingleDot) stateType_);
    }
    if (stateTypeCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (com.zachary.bifromq.basecrdt.proto.SingleValue) stateType_);
    }
    if (stateTypeCase_ == 3) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, (com.zachary.bifromq.basecrdt.proto.SingleMap) stateType_);
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
    if (!(obj instanceof com.zachary.bifromq.basecrdt.proto.StateLattice)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.basecrdt.proto.StateLattice other = (com.zachary.bifromq.basecrdt.proto.StateLattice) obj;

    if (!getStateTypeCase().equals(other.getStateTypeCase())) return false;
    switch (stateTypeCase_) {
      case 1:
        if (!getSingleDot()
            .equals(other.getSingleDot())) return false;
        break;
      case 2:
        if (!getSingleValue()
            .equals(other.getSingleValue())) return false;
        break;
      case 3:
        if (!getSingleMap()
            .equals(other.getSingleMap())) return false;
        break;
      case 0:
      default:
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
    switch (stateTypeCase_) {
      case 1:
        hash = (37 * hash) + SINGLEDOT_FIELD_NUMBER;
        hash = (53 * hash) + getSingleDot().hashCode();
        break;
      case 2:
        hash = (37 * hash) + SINGLEVALUE_FIELD_NUMBER;
        hash = (53 * hash) + getSingleValue().hashCode();
        break;
      case 3:
        hash = (37 * hash) + SINGLEMAP_FIELD_NUMBER;
        hash = (53 * hash) + getSingleMap().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.basecrdt.proto.StateLattice parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.basecrdt.proto.StateLattice prototype) {
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
   * <pre>
   **
   *oneof 定义多个字段，但只能选择一个进行赋值的结构，设置了其中一个成员，其他成员将被自动清除。
   * </pre>
   *
   * Protobuf type {@code basecrdt.StateLattice}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:basecrdt.StateLattice)
      com.zachary.bifromq.basecrdt.proto.StateLatticeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.basecrdt.proto.DotOuterClass.internal_static_basecrdt_StateLattice_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.basecrdt.proto.DotOuterClass.internal_static_basecrdt_StateLattice_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.basecrdt.proto.StateLattice.class, com.zachary.bifromq.basecrdt.proto.StateLattice.Builder.class);
    }

    // Construct using com.zachary.bifromq.basecrdt.proto.StateLattice.newBuilder()
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
      if (singleDotBuilder_ != null) {
        singleDotBuilder_.clear();
      }
      if (singleValueBuilder_ != null) {
        singleValueBuilder_.clear();
      }
      if (singleMapBuilder_ != null) {
        singleMapBuilder_.clear();
      }
      stateTypeCase_ = 0;
      stateType_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.basecrdt.proto.DotOuterClass.internal_static_basecrdt_StateLattice_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.StateLattice getDefaultInstanceForType() {
      return com.zachary.bifromq.basecrdt.proto.StateLattice.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.StateLattice build() {
      com.zachary.bifromq.basecrdt.proto.StateLattice result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.StateLattice buildPartial() {
      com.zachary.bifromq.basecrdt.proto.StateLattice result = new com.zachary.bifromq.basecrdt.proto.StateLattice(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      buildPartialOneofs(result);
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.basecrdt.proto.StateLattice result) {
      int from_bitField0_ = bitField0_;
    }

    private void buildPartialOneofs(com.zachary.bifromq.basecrdt.proto.StateLattice result) {
      result.stateTypeCase_ = stateTypeCase_;
      result.stateType_ = this.stateType_;
      if (stateTypeCase_ == 1 &&
          singleDotBuilder_ != null) {
        result.stateType_ = singleDotBuilder_.build();
      }
      if (stateTypeCase_ == 2 &&
          singleValueBuilder_ != null) {
        result.stateType_ = singleValueBuilder_.build();
      }
      if (stateTypeCase_ == 3 &&
          singleMapBuilder_ != null) {
        result.stateType_ = singleMapBuilder_.build();
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
      if (other instanceof com.zachary.bifromq.basecrdt.proto.StateLattice) {
        return mergeFrom((com.zachary.bifromq.basecrdt.proto.StateLattice)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.basecrdt.proto.StateLattice other) {
      if (other == com.zachary.bifromq.basecrdt.proto.StateLattice.getDefaultInstance()) return this;
      switch (other.getStateTypeCase()) {
        case SINGLEDOT: {
          mergeSingleDot(other.getSingleDot());
          break;
        }
        case SINGLEVALUE: {
          mergeSingleValue(other.getSingleValue());
          break;
        }
        case SINGLEMAP: {
          mergeSingleMap(other.getSingleMap());
          break;
        }
        case STATETYPE_NOT_SET: {
          break;
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
              input.readMessage(
                  getSingleDotFieldBuilder().getBuilder(),
                  extensionRegistry);
              stateTypeCase_ = 1;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getSingleValueFieldBuilder().getBuilder(),
                  extensionRegistry);
              stateTypeCase_ = 2;
              break;
            } // case 18
            case 26: {
              input.readMessage(
                  getSingleMapFieldBuilder().getBuilder(),
                  extensionRegistry);
              stateTypeCase_ = 3;
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
    private int stateTypeCase_ = 0;
    private java.lang.Object stateType_;
    public StateTypeCase
        getStateTypeCase() {
      return StateTypeCase.forNumber(
          stateTypeCase_);
    }

    public Builder clearStateType() {
      stateTypeCase_ = 0;
      stateType_ = null;
      onChanged();
      return this;
    }

    private int bitField0_;

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.proto.SingleDot, com.zachary.bifromq.basecrdt.proto.SingleDot.Builder, com.zachary.bifromq.basecrdt.proto.SingleDotOrBuilder> singleDotBuilder_;
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     * @return Whether the singleDot field is set.
     */
    @java.lang.Override
    public boolean hasSingleDot() {
      return stateTypeCase_ == 1;
    }
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     * @return The singleDot.
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.SingleDot getSingleDot() {
      if (singleDotBuilder_ == null) {
        if (stateTypeCase_ == 1) {
          return (com.zachary.bifromq.basecrdt.proto.SingleDot) stateType_;
        }
        return com.zachary.bifromq.basecrdt.proto.SingleDot.getDefaultInstance();
      } else {
        if (stateTypeCase_ == 1) {
          return singleDotBuilder_.getMessage();
        }
        return com.zachary.bifromq.basecrdt.proto.SingleDot.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     */
    public Builder setSingleDot(com.zachary.bifromq.basecrdt.proto.SingleDot value) {
      if (singleDotBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        stateType_ = value;
        onChanged();
      } else {
        singleDotBuilder_.setMessage(value);
      }
      stateTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     */
    public Builder setSingleDot(
        com.zachary.bifromq.basecrdt.proto.SingleDot.Builder builderForValue) {
      if (singleDotBuilder_ == null) {
        stateType_ = builderForValue.build();
        onChanged();
      } else {
        singleDotBuilder_.setMessage(builderForValue.build());
      }
      stateTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     */
    public Builder mergeSingleDot(com.zachary.bifromq.basecrdt.proto.SingleDot value) {
      if (singleDotBuilder_ == null) {
        if (stateTypeCase_ == 1 &&
            stateType_ != com.zachary.bifromq.basecrdt.proto.SingleDot.getDefaultInstance()) {
          stateType_ = com.zachary.bifromq.basecrdt.proto.SingleDot.newBuilder((com.zachary.bifromq.basecrdt.proto.SingleDot) stateType_)
              .mergeFrom(value).buildPartial();
        } else {
          stateType_ = value;
        }
        onChanged();
      } else {
        if (stateTypeCase_ == 1) {
          singleDotBuilder_.mergeFrom(value);
        } else {
          singleDotBuilder_.setMessage(value);
        }
      }
      stateTypeCase_ = 1;
      return this;
    }
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     */
    public Builder clearSingleDot() {
      if (singleDotBuilder_ == null) {
        if (stateTypeCase_ == 1) {
          stateTypeCase_ = 0;
          stateType_ = null;
          onChanged();
        }
      } else {
        if (stateTypeCase_ == 1) {
          stateTypeCase_ = 0;
          stateType_ = null;
        }
        singleDotBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     */
    public com.zachary.bifromq.basecrdt.proto.SingleDot.Builder getSingleDotBuilder() {
      return getSingleDotFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.SingleDotOrBuilder getSingleDotOrBuilder() {
      if ((stateTypeCase_ == 1) && (singleDotBuilder_ != null)) {
        return singleDotBuilder_.getMessageOrBuilder();
      } else {
        if (stateTypeCase_ == 1) {
          return (com.zachary.bifromq.basecrdt.proto.SingleDot) stateType_;
        }
        return com.zachary.bifromq.basecrdt.proto.SingleDot.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.SingleDot singleDot = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.proto.SingleDot, com.zachary.bifromq.basecrdt.proto.SingleDot.Builder, com.zachary.bifromq.basecrdt.proto.SingleDotOrBuilder> 
        getSingleDotFieldBuilder() {
      if (singleDotBuilder_ == null) {
        if (!(stateTypeCase_ == 1)) {
          stateType_ = com.zachary.bifromq.basecrdt.proto.SingleDot.getDefaultInstance();
        }
        singleDotBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecrdt.proto.SingleDot, com.zachary.bifromq.basecrdt.proto.SingleDot.Builder, com.zachary.bifromq.basecrdt.proto.SingleDotOrBuilder>(
                (com.zachary.bifromq.basecrdt.proto.SingleDot) stateType_,
                getParentForChildren(),
                isClean());
        stateType_ = null;
      }
      stateTypeCase_ = 1;
      onChanged();
      return singleDotBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.proto.SingleValue, com.zachary.bifromq.basecrdt.proto.SingleValue.Builder, com.zachary.bifromq.basecrdt.proto.SingleValueOrBuilder> singleValueBuilder_;
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     * @return Whether the singleValue field is set.
     */
    @java.lang.Override
    public boolean hasSingleValue() {
      return stateTypeCase_ == 2;
    }
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     * @return The singleValue.
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.SingleValue getSingleValue() {
      if (singleValueBuilder_ == null) {
        if (stateTypeCase_ == 2) {
          return (com.zachary.bifromq.basecrdt.proto.SingleValue) stateType_;
        }
        return com.zachary.bifromq.basecrdt.proto.SingleValue.getDefaultInstance();
      } else {
        if (stateTypeCase_ == 2) {
          return singleValueBuilder_.getMessage();
        }
        return com.zachary.bifromq.basecrdt.proto.SingleValue.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     */
    public Builder setSingleValue(com.zachary.bifromq.basecrdt.proto.SingleValue value) {
      if (singleValueBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        stateType_ = value;
        onChanged();
      } else {
        singleValueBuilder_.setMessage(value);
      }
      stateTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     */
    public Builder setSingleValue(
        com.zachary.bifromq.basecrdt.proto.SingleValue.Builder builderForValue) {
      if (singleValueBuilder_ == null) {
        stateType_ = builderForValue.build();
        onChanged();
      } else {
        singleValueBuilder_.setMessage(builderForValue.build());
      }
      stateTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     */
    public Builder mergeSingleValue(com.zachary.bifromq.basecrdt.proto.SingleValue value) {
      if (singleValueBuilder_ == null) {
        if (stateTypeCase_ == 2 &&
            stateType_ != com.zachary.bifromq.basecrdt.proto.SingleValue.getDefaultInstance()) {
          stateType_ = com.zachary.bifromq.basecrdt.proto.SingleValue.newBuilder((com.zachary.bifromq.basecrdt.proto.SingleValue) stateType_)
              .mergeFrom(value).buildPartial();
        } else {
          stateType_ = value;
        }
        onChanged();
      } else {
        if (stateTypeCase_ == 2) {
          singleValueBuilder_.mergeFrom(value);
        } else {
          singleValueBuilder_.setMessage(value);
        }
      }
      stateTypeCase_ = 2;
      return this;
    }
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     */
    public Builder clearSingleValue() {
      if (singleValueBuilder_ == null) {
        if (stateTypeCase_ == 2) {
          stateTypeCase_ = 0;
          stateType_ = null;
          onChanged();
        }
      } else {
        if (stateTypeCase_ == 2) {
          stateTypeCase_ = 0;
          stateType_ = null;
        }
        singleValueBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     */
    public com.zachary.bifromq.basecrdt.proto.SingleValue.Builder getSingleValueBuilder() {
      return getSingleValueFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.SingleValueOrBuilder getSingleValueOrBuilder() {
      if ((stateTypeCase_ == 2) && (singleValueBuilder_ != null)) {
        return singleValueBuilder_.getMessageOrBuilder();
      } else {
        if (stateTypeCase_ == 2) {
          return (com.zachary.bifromq.basecrdt.proto.SingleValue) stateType_;
        }
        return com.zachary.bifromq.basecrdt.proto.SingleValue.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.SingleValue singleValue = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.proto.SingleValue, com.zachary.bifromq.basecrdt.proto.SingleValue.Builder, com.zachary.bifromq.basecrdt.proto.SingleValueOrBuilder> 
        getSingleValueFieldBuilder() {
      if (singleValueBuilder_ == null) {
        if (!(stateTypeCase_ == 2)) {
          stateType_ = com.zachary.bifromq.basecrdt.proto.SingleValue.getDefaultInstance();
        }
        singleValueBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecrdt.proto.SingleValue, com.zachary.bifromq.basecrdt.proto.SingleValue.Builder, com.zachary.bifromq.basecrdt.proto.SingleValueOrBuilder>(
                (com.zachary.bifromq.basecrdt.proto.SingleValue) stateType_,
                getParentForChildren(),
                isClean());
        stateType_ = null;
      }
      stateTypeCase_ = 2;
      onChanged();
      return singleValueBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.proto.SingleMap, com.zachary.bifromq.basecrdt.proto.SingleMap.Builder, com.zachary.bifromq.basecrdt.proto.SingleMapOrBuilder> singleMapBuilder_;
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     * @return Whether the singleMap field is set.
     */
    @java.lang.Override
    public boolean hasSingleMap() {
      return stateTypeCase_ == 3;
    }
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     * @return The singleMap.
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.SingleMap getSingleMap() {
      if (singleMapBuilder_ == null) {
        if (stateTypeCase_ == 3) {
          return (com.zachary.bifromq.basecrdt.proto.SingleMap) stateType_;
        }
        return com.zachary.bifromq.basecrdt.proto.SingleMap.getDefaultInstance();
      } else {
        if (stateTypeCase_ == 3) {
          return singleMapBuilder_.getMessage();
        }
        return com.zachary.bifromq.basecrdt.proto.SingleMap.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     */
    public Builder setSingleMap(com.zachary.bifromq.basecrdt.proto.SingleMap value) {
      if (singleMapBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        stateType_ = value;
        onChanged();
      } else {
        singleMapBuilder_.setMessage(value);
      }
      stateTypeCase_ = 3;
      return this;
    }
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     */
    public Builder setSingleMap(
        com.zachary.bifromq.basecrdt.proto.SingleMap.Builder builderForValue) {
      if (singleMapBuilder_ == null) {
        stateType_ = builderForValue.build();
        onChanged();
      } else {
        singleMapBuilder_.setMessage(builderForValue.build());
      }
      stateTypeCase_ = 3;
      return this;
    }
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     */
    public Builder mergeSingleMap(com.zachary.bifromq.basecrdt.proto.SingleMap value) {
      if (singleMapBuilder_ == null) {
        if (stateTypeCase_ == 3 &&
            stateType_ != com.zachary.bifromq.basecrdt.proto.SingleMap.getDefaultInstance()) {
          stateType_ = com.zachary.bifromq.basecrdt.proto.SingleMap.newBuilder((com.zachary.bifromq.basecrdt.proto.SingleMap) stateType_)
              .mergeFrom(value).buildPartial();
        } else {
          stateType_ = value;
        }
        onChanged();
      } else {
        if (stateTypeCase_ == 3) {
          singleMapBuilder_.mergeFrom(value);
        } else {
          singleMapBuilder_.setMessage(value);
        }
      }
      stateTypeCase_ = 3;
      return this;
    }
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     */
    public Builder clearSingleMap() {
      if (singleMapBuilder_ == null) {
        if (stateTypeCase_ == 3) {
          stateTypeCase_ = 0;
          stateType_ = null;
          onChanged();
        }
      } else {
        if (stateTypeCase_ == 3) {
          stateTypeCase_ = 0;
          stateType_ = null;
        }
        singleMapBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     */
    public com.zachary.bifromq.basecrdt.proto.SingleMap.Builder getSingleMapBuilder() {
      return getSingleMapFieldBuilder().getBuilder();
    }
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.basecrdt.proto.SingleMapOrBuilder getSingleMapOrBuilder() {
      if ((stateTypeCase_ == 3) && (singleMapBuilder_ != null)) {
        return singleMapBuilder_.getMessageOrBuilder();
      } else {
        if (stateTypeCase_ == 3) {
          return (com.zachary.bifromq.basecrdt.proto.SingleMap) stateType_;
        }
        return com.zachary.bifromq.basecrdt.proto.SingleMap.getDefaultInstance();
      }
    }
    /**
     * <code>.basecrdt.SingleMap singleMap = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.basecrdt.proto.SingleMap, com.zachary.bifromq.basecrdt.proto.SingleMap.Builder, com.zachary.bifromq.basecrdt.proto.SingleMapOrBuilder> 
        getSingleMapFieldBuilder() {
      if (singleMapBuilder_ == null) {
        if (!(stateTypeCase_ == 3)) {
          stateType_ = com.zachary.bifromq.basecrdt.proto.SingleMap.getDefaultInstance();
        }
        singleMapBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.basecrdt.proto.SingleMap, com.zachary.bifromq.basecrdt.proto.SingleMap.Builder, com.zachary.bifromq.basecrdt.proto.SingleMapOrBuilder>(
                (com.zachary.bifromq.basecrdt.proto.SingleMap) stateType_,
                getParentForChildren(),
                isClean());
        stateType_ = null;
      }
      stateTypeCase_ = 3;
      onChanged();
      return singleMapBuilder_;
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


    // @@protoc_insertion_point(builder_scope:basecrdt.StateLattice)
  }

  // @@protoc_insertion_point(class_scope:basecrdt.StateLattice)
  private static final com.zachary.bifromq.basecrdt.proto.StateLattice DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.basecrdt.proto.StateLattice();
  }

  public static com.zachary.bifromq.basecrdt.proto.StateLattice getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<StateLattice>
      PARSER = new com.google.protobuf.AbstractParser<StateLattice>() {
    @java.lang.Override
    public StateLattice parsePartialFrom(
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

  public static com.google.protobuf.Parser<StateLattice> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<StateLattice> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.basecrdt.proto.StateLattice getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

