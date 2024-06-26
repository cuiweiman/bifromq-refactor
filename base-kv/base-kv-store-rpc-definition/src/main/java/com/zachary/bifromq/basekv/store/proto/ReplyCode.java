// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/BaseKVStoreService.proto

package com.zachary.bifromq.basekv.store.proto;

/**
 * Protobuf enum {@code basekv.ReplyCode}
 */
public enum ReplyCode
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>Ok = 0;</code>
   */
  Ok(0),
  /**
   * <code>BadVersion = 1;</code>
   */
  BadVersion(1),
  /**
   * <code>TryLater = 2;</code>
   */
  TryLater(2),
  /**
   * <code>BadRequest = 3;</code>
   */
  BadRequest(3),
  /**
   * <code>InternalError = 4;</code>
   */
  InternalError(4),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>Ok = 0;</code>
   */
  public static final int Ok_VALUE = 0;
  /**
   * <code>BadVersion = 1;</code>
   */
  public static final int BadVersion_VALUE = 1;
  /**
   * <code>TryLater = 2;</code>
   */
  public static final int TryLater_VALUE = 2;
  /**
   * <code>BadRequest = 3;</code>
   */
  public static final int BadRequest_VALUE = 3;
  /**
   * <code>InternalError = 4;</code>
   */
  public static final int InternalError_VALUE = 4;


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
  public static ReplyCode valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static ReplyCode forNumber(int value) {
    switch (value) {
      case 0: return Ok;
      case 1: return BadVersion;
      case 2: return TryLater;
      case 3: return BadRequest;
      case 4: return InternalError;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<ReplyCode>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      ReplyCode> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<ReplyCode>() {
          public ReplyCode findValueByNumber(int number) {
            return ReplyCode.forNumber(number);
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
    return com.zachary.bifromq.basekv.store.proto.BaseKVStoreServiceOuterClass.getDescriptor().getEnumTypes().get(0);
  }

  private static final ReplyCode[] VALUES = values();

  public static ReplyCode valueOf(
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

  private ReplyCode(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:basekv.ReplyCode)
}

