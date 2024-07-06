// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: distservice/DistCoProc.proto

package com.zachary.bifromq.dist.rpc.proto;

/**
 * Protobuf type {@code distservice.DistServiceROCoProcInput}
 */
public final class DistServiceROCoProcInput extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:distservice.DistServiceROCoProcInput)
    DistServiceROCoProcInputOrBuilder {
private static final long serialVersionUID = 0L;
  // Use DistServiceROCoProcInput.newBuilder() to construct.
  private DistServiceROCoProcInput(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private DistServiceROCoProcInput() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new DistServiceROCoProcInput();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.zachary.bifromq.dist.rpc.proto.DistCoProcProtos.internal_static_distservice_DistServiceROCoProcInput_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.zachary.bifromq.dist.rpc.proto.DistCoProcProtos.internal_static_distservice_DistServiceROCoProcInput_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput.class, com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput.Builder.class);
  }

  private int inputCase_ = 0;
  private java.lang.Object input_;
  public enum InputCase
      implements com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    DIST(1),
    GCREQUEST(2),
    COLLECTMETRICSREQUEST(3),
    INPUT_NOT_SET(0);
    private final int value;
    private InputCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static InputCase valueOf(int value) {
      return forNumber(value);
    }

    public static InputCase forNumber(int value) {
      switch (value) {
        case 1: return DIST;
        case 2: return GCREQUEST;
        case 3: return COLLECTMETRICSREQUEST;
        case 0: return INPUT_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public InputCase
  getInputCase() {
    return InputCase.forNumber(
        inputCase_);
  }

  public static final int DIST_FIELD_NUMBER = 1;
  /**
   * <code>.distservice.BatchDist dist = 1;</code>
   * @return Whether the dist field is set.
   */
  @java.lang.Override
  public boolean hasDist() {
    return inputCase_ == 1;
  }
  /**
   * <code>.distservice.BatchDist dist = 1;</code>
   * @return The dist.
   */
  @java.lang.Override
  public com.zachary.bifromq.dist.rpc.proto.BatchDist getDist() {
    if (inputCase_ == 1) {
       return (com.zachary.bifromq.dist.rpc.proto.BatchDist) input_;
    }
    return com.zachary.bifromq.dist.rpc.proto.BatchDist.getDefaultInstance();
  }
  /**
   * <code>.distservice.BatchDist dist = 1;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.dist.rpc.proto.BatchDistOrBuilder getDistOrBuilder() {
    if (inputCase_ == 1) {
       return (com.zachary.bifromq.dist.rpc.proto.BatchDist) input_;
    }
    return com.zachary.bifromq.dist.rpc.proto.BatchDist.getDefaultInstance();
  }

  public static final int GCREQUEST_FIELD_NUMBER = 2;
  /**
   * <code>.distservice.GCRequest gcRequest = 2;</code>
   * @return Whether the gcRequest field is set.
   */
  @java.lang.Override
  public boolean hasGcRequest() {
    return inputCase_ == 2;
  }
  /**
   * <code>.distservice.GCRequest gcRequest = 2;</code>
   * @return The gcRequest.
   */
  @java.lang.Override
  public com.zachary.bifromq.dist.rpc.proto.GCRequest getGcRequest() {
    if (inputCase_ == 2) {
       return (com.zachary.bifromq.dist.rpc.proto.GCRequest) input_;
    }
    return com.zachary.bifromq.dist.rpc.proto.GCRequest.getDefaultInstance();
  }
  /**
   * <code>.distservice.GCRequest gcRequest = 2;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.dist.rpc.proto.GCRequestOrBuilder getGcRequestOrBuilder() {
    if (inputCase_ == 2) {
       return (com.zachary.bifromq.dist.rpc.proto.GCRequest) input_;
    }
    return com.zachary.bifromq.dist.rpc.proto.GCRequest.getDefaultInstance();
  }

  public static final int COLLECTMETRICSREQUEST_FIELD_NUMBER = 3;
  /**
   * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
   * @return Whether the collectMetricsRequest field is set.
   */
  @java.lang.Override
  public boolean hasCollectMetricsRequest() {
    return inputCase_ == 3;
  }
  /**
   * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
   * @return The collectMetricsRequest.
   */
  @java.lang.Override
  public com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest getCollectMetricsRequest() {
    if (inputCase_ == 3) {
       return (com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest) input_;
    }
    return com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.getDefaultInstance();
  }
  /**
   * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
   */
  @java.lang.Override
  public com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequestOrBuilder getCollectMetricsRequestOrBuilder() {
    if (inputCase_ == 3) {
       return (com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest) input_;
    }
    return com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.getDefaultInstance();
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
    if (inputCase_ == 1) {
      output.writeMessage(1, (com.zachary.bifromq.dist.rpc.proto.BatchDist) input_);
    }
    if (inputCase_ == 2) {
      output.writeMessage(2, (com.zachary.bifromq.dist.rpc.proto.GCRequest) input_);
    }
    if (inputCase_ == 3) {
      output.writeMessage(3, (com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest) input_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (inputCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (com.zachary.bifromq.dist.rpc.proto.BatchDist) input_);
    }
    if (inputCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (com.zachary.bifromq.dist.rpc.proto.GCRequest) input_);
    }
    if (inputCase_ == 3) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, (com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest) input_);
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
    if (!(obj instanceof com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput)) {
      return super.equals(obj);
    }
    com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput other = (com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput) obj;

    if (!getInputCase().equals(other.getInputCase())) return false;
    switch (inputCase_) {
      case 1:
        if (!getDist()
            .equals(other.getDist())) return false;
        break;
      case 2:
        if (!getGcRequest()
            .equals(other.getGcRequest())) return false;
        break;
      case 3:
        if (!getCollectMetricsRequest()
            .equals(other.getCollectMetricsRequest())) return false;
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
    switch (inputCase_) {
      case 1:
        hash = (37 * hash) + DIST_FIELD_NUMBER;
        hash = (53 * hash) + getDist().hashCode();
        break;
      case 2:
        hash = (37 * hash) + GCREQUEST_FIELD_NUMBER;
        hash = (53 * hash) + getGcRequest().hashCode();
        break;
      case 3:
        hash = (37 * hash) + COLLECTMETRICSREQUEST_FIELD_NUMBER;
        hash = (53 * hash) + getCollectMetricsRequest().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput parseFrom(
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
  public static Builder newBuilder(com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput prototype) {
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
   * Protobuf type {@code distservice.DistServiceROCoProcInput}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:distservice.DistServiceROCoProcInput)
      com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInputOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zachary.bifromq.dist.rpc.proto.DistCoProcProtos.internal_static_distservice_DistServiceROCoProcInput_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zachary.bifromq.dist.rpc.proto.DistCoProcProtos.internal_static_distservice_DistServiceROCoProcInput_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput.class, com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput.Builder.class);
    }

    // Construct using com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput.newBuilder()
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
      if (distBuilder_ != null) {
        distBuilder_.clear();
      }
      if (gcRequestBuilder_ != null) {
        gcRequestBuilder_.clear();
      }
      if (collectMetricsRequestBuilder_ != null) {
        collectMetricsRequestBuilder_.clear();
      }
      inputCase_ = 0;
      input_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.zachary.bifromq.dist.rpc.proto.DistCoProcProtos.internal_static_distservice_DistServiceROCoProcInput_descriptor;
    }

    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput getDefaultInstanceForType() {
      return com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput.getDefaultInstance();
    }

    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput build() {
      com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput buildPartial() {
      com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput result = new com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      buildPartialOneofs(result);
      onBuilt();
      return result;
    }

    private void buildPartial0(com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput result) {
      int from_bitField0_ = bitField0_;
    }

    private void buildPartialOneofs(com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput result) {
      result.inputCase_ = inputCase_;
      result.input_ = this.input_;
      if (inputCase_ == 1 &&
          distBuilder_ != null) {
        result.input_ = distBuilder_.build();
      }
      if (inputCase_ == 2 &&
          gcRequestBuilder_ != null) {
        result.input_ = gcRequestBuilder_.build();
      }
      if (inputCase_ == 3 &&
          collectMetricsRequestBuilder_ != null) {
        result.input_ = collectMetricsRequestBuilder_.build();
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
      if (other instanceof com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput) {
        return mergeFrom((com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput other) {
      if (other == com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput.getDefaultInstance()) return this;
      switch (other.getInputCase()) {
        case DIST: {
          mergeDist(other.getDist());
          break;
        }
        case GCREQUEST: {
          mergeGcRequest(other.getGcRequest());
          break;
        }
        case COLLECTMETRICSREQUEST: {
          mergeCollectMetricsRequest(other.getCollectMetricsRequest());
          break;
        }
        case INPUT_NOT_SET: {
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
                  getDistFieldBuilder().getBuilder(),
                  extensionRegistry);
              inputCase_ = 1;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getGcRequestFieldBuilder().getBuilder(),
                  extensionRegistry);
              inputCase_ = 2;
              break;
            } // case 18
            case 26: {
              input.readMessage(
                  getCollectMetricsRequestFieldBuilder().getBuilder(),
                  extensionRegistry);
              inputCase_ = 3;
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
    private int inputCase_ = 0;
    private java.lang.Object input_;
    public InputCase
        getInputCase() {
      return InputCase.forNumber(
          inputCase_);
    }

    public Builder clearInput() {
      inputCase_ = 0;
      input_ = null;
      onChanged();
      return this;
    }

    private int bitField0_;

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.dist.rpc.proto.BatchDist, com.zachary.bifromq.dist.rpc.proto.BatchDist.Builder, com.zachary.bifromq.dist.rpc.proto.BatchDistOrBuilder> distBuilder_;
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     * @return Whether the dist field is set.
     */
    @java.lang.Override
    public boolean hasDist() {
      return inputCase_ == 1;
    }
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     * @return The dist.
     */
    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.BatchDist getDist() {
      if (distBuilder_ == null) {
        if (inputCase_ == 1) {
          return (com.zachary.bifromq.dist.rpc.proto.BatchDist) input_;
        }
        return com.zachary.bifromq.dist.rpc.proto.BatchDist.getDefaultInstance();
      } else {
        if (inputCase_ == 1) {
          return distBuilder_.getMessage();
        }
        return com.zachary.bifromq.dist.rpc.proto.BatchDist.getDefaultInstance();
      }
    }
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     */
    public Builder setDist(com.zachary.bifromq.dist.rpc.proto.BatchDist value) {
      if (distBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        input_ = value;
        onChanged();
      } else {
        distBuilder_.setMessage(value);
      }
      inputCase_ = 1;
      return this;
    }
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     */
    public Builder setDist(
        com.zachary.bifromq.dist.rpc.proto.BatchDist.Builder builderForValue) {
      if (distBuilder_ == null) {
        input_ = builderForValue.build();
        onChanged();
      } else {
        distBuilder_.setMessage(builderForValue.build());
      }
      inputCase_ = 1;
      return this;
    }
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     */
    public Builder mergeDist(com.zachary.bifromq.dist.rpc.proto.BatchDist value) {
      if (distBuilder_ == null) {
        if (inputCase_ == 1 &&
            input_ != com.zachary.bifromq.dist.rpc.proto.BatchDist.getDefaultInstance()) {
          input_ = com.zachary.bifromq.dist.rpc.proto.BatchDist.newBuilder((com.zachary.bifromq.dist.rpc.proto.BatchDist) input_)
              .mergeFrom(value).buildPartial();
        } else {
          input_ = value;
        }
        onChanged();
      } else {
        if (inputCase_ == 1) {
          distBuilder_.mergeFrom(value);
        } else {
          distBuilder_.setMessage(value);
        }
      }
      inputCase_ = 1;
      return this;
    }
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     */
    public Builder clearDist() {
      if (distBuilder_ == null) {
        if (inputCase_ == 1) {
          inputCase_ = 0;
          input_ = null;
          onChanged();
        }
      } else {
        if (inputCase_ == 1) {
          inputCase_ = 0;
          input_ = null;
        }
        distBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     */
    public com.zachary.bifromq.dist.rpc.proto.BatchDist.Builder getDistBuilder() {
      return getDistFieldBuilder().getBuilder();
    }
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.BatchDistOrBuilder getDistOrBuilder() {
      if ((inputCase_ == 1) && (distBuilder_ != null)) {
        return distBuilder_.getMessageOrBuilder();
      } else {
        if (inputCase_ == 1) {
          return (com.zachary.bifromq.dist.rpc.proto.BatchDist) input_;
        }
        return com.zachary.bifromq.dist.rpc.proto.BatchDist.getDefaultInstance();
      }
    }
    /**
     * <code>.distservice.BatchDist dist = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.dist.rpc.proto.BatchDist, com.zachary.bifromq.dist.rpc.proto.BatchDist.Builder, com.zachary.bifromq.dist.rpc.proto.BatchDistOrBuilder> 
        getDistFieldBuilder() {
      if (distBuilder_ == null) {
        if (!(inputCase_ == 1)) {
          input_ = com.zachary.bifromq.dist.rpc.proto.BatchDist.getDefaultInstance();
        }
        distBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.dist.rpc.proto.BatchDist, com.zachary.bifromq.dist.rpc.proto.BatchDist.Builder, com.zachary.bifromq.dist.rpc.proto.BatchDistOrBuilder>(
                (com.zachary.bifromq.dist.rpc.proto.BatchDist) input_,
                getParentForChildren(),
                isClean());
        input_ = null;
      }
      inputCase_ = 1;
      onChanged();
      return distBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.dist.rpc.proto.GCRequest, com.zachary.bifromq.dist.rpc.proto.GCRequest.Builder, com.zachary.bifromq.dist.rpc.proto.GCRequestOrBuilder> gcRequestBuilder_;
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     * @return Whether the gcRequest field is set.
     */
    @java.lang.Override
    public boolean hasGcRequest() {
      return inputCase_ == 2;
    }
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     * @return The gcRequest.
     */
    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.GCRequest getGcRequest() {
      if (gcRequestBuilder_ == null) {
        if (inputCase_ == 2) {
          return (com.zachary.bifromq.dist.rpc.proto.GCRequest) input_;
        }
        return com.zachary.bifromq.dist.rpc.proto.GCRequest.getDefaultInstance();
      } else {
        if (inputCase_ == 2) {
          return gcRequestBuilder_.getMessage();
        }
        return com.zachary.bifromq.dist.rpc.proto.GCRequest.getDefaultInstance();
      }
    }
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     */
    public Builder setGcRequest(com.zachary.bifromq.dist.rpc.proto.GCRequest value) {
      if (gcRequestBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        input_ = value;
        onChanged();
      } else {
        gcRequestBuilder_.setMessage(value);
      }
      inputCase_ = 2;
      return this;
    }
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     */
    public Builder setGcRequest(
        com.zachary.bifromq.dist.rpc.proto.GCRequest.Builder builderForValue) {
      if (gcRequestBuilder_ == null) {
        input_ = builderForValue.build();
        onChanged();
      } else {
        gcRequestBuilder_.setMessage(builderForValue.build());
      }
      inputCase_ = 2;
      return this;
    }
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     */
    public Builder mergeGcRequest(com.zachary.bifromq.dist.rpc.proto.GCRequest value) {
      if (gcRequestBuilder_ == null) {
        if (inputCase_ == 2 &&
            input_ != com.zachary.bifromq.dist.rpc.proto.GCRequest.getDefaultInstance()) {
          input_ = com.zachary.bifromq.dist.rpc.proto.GCRequest.newBuilder((com.zachary.bifromq.dist.rpc.proto.GCRequest) input_)
              .mergeFrom(value).buildPartial();
        } else {
          input_ = value;
        }
        onChanged();
      } else {
        if (inputCase_ == 2) {
          gcRequestBuilder_.mergeFrom(value);
        } else {
          gcRequestBuilder_.setMessage(value);
        }
      }
      inputCase_ = 2;
      return this;
    }
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     */
    public Builder clearGcRequest() {
      if (gcRequestBuilder_ == null) {
        if (inputCase_ == 2) {
          inputCase_ = 0;
          input_ = null;
          onChanged();
        }
      } else {
        if (inputCase_ == 2) {
          inputCase_ = 0;
          input_ = null;
        }
        gcRequestBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     */
    public com.zachary.bifromq.dist.rpc.proto.GCRequest.Builder getGcRequestBuilder() {
      return getGcRequestFieldBuilder().getBuilder();
    }
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.GCRequestOrBuilder getGcRequestOrBuilder() {
      if ((inputCase_ == 2) && (gcRequestBuilder_ != null)) {
        return gcRequestBuilder_.getMessageOrBuilder();
      } else {
        if (inputCase_ == 2) {
          return (com.zachary.bifromq.dist.rpc.proto.GCRequest) input_;
        }
        return com.zachary.bifromq.dist.rpc.proto.GCRequest.getDefaultInstance();
      }
    }
    /**
     * <code>.distservice.GCRequest gcRequest = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.dist.rpc.proto.GCRequest, com.zachary.bifromq.dist.rpc.proto.GCRequest.Builder, com.zachary.bifromq.dist.rpc.proto.GCRequestOrBuilder> 
        getGcRequestFieldBuilder() {
      if (gcRequestBuilder_ == null) {
        if (!(inputCase_ == 2)) {
          input_ = com.zachary.bifromq.dist.rpc.proto.GCRequest.getDefaultInstance();
        }
        gcRequestBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.dist.rpc.proto.GCRequest, com.zachary.bifromq.dist.rpc.proto.GCRequest.Builder, com.zachary.bifromq.dist.rpc.proto.GCRequestOrBuilder>(
                (com.zachary.bifromq.dist.rpc.proto.GCRequest) input_,
                getParentForChildren(),
                isClean());
        input_ = null;
      }
      inputCase_ = 2;
      onChanged();
      return gcRequestBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest, com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.Builder, com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequestOrBuilder> collectMetricsRequestBuilder_;
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     * @return Whether the collectMetricsRequest field is set.
     */
    @java.lang.Override
    public boolean hasCollectMetricsRequest() {
      return inputCase_ == 3;
    }
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     * @return The collectMetricsRequest.
     */
    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest getCollectMetricsRequest() {
      if (collectMetricsRequestBuilder_ == null) {
        if (inputCase_ == 3) {
          return (com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest) input_;
        }
        return com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.getDefaultInstance();
      } else {
        if (inputCase_ == 3) {
          return collectMetricsRequestBuilder_.getMessage();
        }
        return com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.getDefaultInstance();
      }
    }
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     */
    public Builder setCollectMetricsRequest(com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest value) {
      if (collectMetricsRequestBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        input_ = value;
        onChanged();
      } else {
        collectMetricsRequestBuilder_.setMessage(value);
      }
      inputCase_ = 3;
      return this;
    }
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     */
    public Builder setCollectMetricsRequest(
        com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.Builder builderForValue) {
      if (collectMetricsRequestBuilder_ == null) {
        input_ = builderForValue.build();
        onChanged();
      } else {
        collectMetricsRequestBuilder_.setMessage(builderForValue.build());
      }
      inputCase_ = 3;
      return this;
    }
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     */
    public Builder mergeCollectMetricsRequest(com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest value) {
      if (collectMetricsRequestBuilder_ == null) {
        if (inputCase_ == 3 &&
            input_ != com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.getDefaultInstance()) {
          input_ = com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.newBuilder((com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest) input_)
              .mergeFrom(value).buildPartial();
        } else {
          input_ = value;
        }
        onChanged();
      } else {
        if (inputCase_ == 3) {
          collectMetricsRequestBuilder_.mergeFrom(value);
        } else {
          collectMetricsRequestBuilder_.setMessage(value);
        }
      }
      inputCase_ = 3;
      return this;
    }
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     */
    public Builder clearCollectMetricsRequest() {
      if (collectMetricsRequestBuilder_ == null) {
        if (inputCase_ == 3) {
          inputCase_ = 0;
          input_ = null;
          onChanged();
        }
      } else {
        if (inputCase_ == 3) {
          inputCase_ = 0;
          input_ = null;
        }
        collectMetricsRequestBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     */
    public com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.Builder getCollectMetricsRequestBuilder() {
      return getCollectMetricsRequestFieldBuilder().getBuilder();
    }
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     */
    @java.lang.Override
    public com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequestOrBuilder getCollectMetricsRequestOrBuilder() {
      if ((inputCase_ == 3) && (collectMetricsRequestBuilder_ != null)) {
        return collectMetricsRequestBuilder_.getMessageOrBuilder();
      } else {
        if (inputCase_ == 3) {
          return (com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest) input_;
        }
        return com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.getDefaultInstance();
      }
    }
    /**
     * <code>.distservice.CollectMetricsRequest collectMetricsRequest = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest, com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.Builder, com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequestOrBuilder> 
        getCollectMetricsRequestFieldBuilder() {
      if (collectMetricsRequestBuilder_ == null) {
        if (!(inputCase_ == 3)) {
          input_ = com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.getDefaultInstance();
        }
        collectMetricsRequestBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest, com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest.Builder, com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequestOrBuilder>(
                (com.zachary.bifromq.dist.rpc.proto.CollectMetricsRequest) input_,
                getParentForChildren(),
                isClean());
        input_ = null;
      }
      inputCase_ = 3;
      onChanged();
      return collectMetricsRequestBuilder_;
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


    // @@protoc_insertion_point(builder_scope:distservice.DistServiceROCoProcInput)
  }

  // @@protoc_insertion_point(class_scope:distservice.DistServiceROCoProcInput)
  private static final com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput();
  }

  public static com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<DistServiceROCoProcInput>
      PARSER = new com.google.protobuf.AbstractParser<DistServiceROCoProcInput>() {
    @java.lang.Override
    public DistServiceROCoProcInput parsePartialFrom(
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

  public static com.google.protobuf.Parser<DistServiceROCoProcInput> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<DistServiceROCoProcInput> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.zachary.bifromq.dist.rpc.proto.DistServiceROCoProcInput getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
