// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: basekv/StoreMessage.proto

package com.zachary.bifromq.basekv.proto;

public interface KVRangeMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:basekv.KVRangeMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.basekv.KVRangeId rangeId = 1;</code>
   * @return Whether the rangeId field is set.
   */
  boolean hasRangeId();
  /**
   * <code>.basekv.KVRangeId rangeId = 1;</code>
   * @return The rangeId.
   */
  com.zachary.bifromq.basekv.proto.KVRangeId getRangeId();
  /**
   * <code>.basekv.KVRangeId rangeId = 1;</code>
   */
  com.zachary.bifromq.basekv.proto.KVRangeIdOrBuilder getRangeIdOrBuilder();

  /**
   * <pre>
   * null for broadcast
   * </pre>
   *
   * <code>optional string hostStoreId = 2;</code>
   * @return Whether the hostStoreId field is set.
   */
  boolean hasHostStoreId();
  /**
   * <pre>
   * null for broadcast
   * </pre>
   *
   * <code>optional string hostStoreId = 2;</code>
   * @return The hostStoreId.
   */
  java.lang.String getHostStoreId();
  /**
   * <pre>
   * null for broadcast
   * </pre>
   *
   * <code>optional string hostStoreId = 2;</code>
   * @return The bytes for hostStoreId.
   */
  com.google.protobuf.ByteString
      getHostStoreIdBytes();

  /**
   * <code>.basekv.WALRaftMessages walRaftMessages = 4;</code>
   * @return Whether the walRaftMessages field is set.
   */
  boolean hasWalRaftMessages();
  /**
   * <code>.basekv.WALRaftMessages walRaftMessages = 4;</code>
   * @return The walRaftMessages.
   */
  com.zachary.bifromq.basekv.proto.WALRaftMessages getWalRaftMessages();
  /**
   * <code>.basekv.WALRaftMessages walRaftMessages = 4;</code>
   */
  com.zachary.bifromq.basekv.proto.WALRaftMessagesOrBuilder getWalRaftMessagesOrBuilder();

  /**
   * <code>.basekv.SnapshotSyncRequest snapshotSyncRequest = 5;</code>
   * @return Whether the snapshotSyncRequest field is set.
   */
  boolean hasSnapshotSyncRequest();
  /**
   * <code>.basekv.SnapshotSyncRequest snapshotSyncRequest = 5;</code>
   * @return The snapshotSyncRequest.
   */
  com.zachary.bifromq.basekv.proto.SnapshotSyncRequest getSnapshotSyncRequest();
  /**
   * <code>.basekv.SnapshotSyncRequest snapshotSyncRequest = 5;</code>
   */
  com.zachary.bifromq.basekv.proto.SnapshotSyncRequestOrBuilder getSnapshotSyncRequestOrBuilder();

  /**
   * <code>.basekv.SaveSnapshotDataRequest saveSnapshotDataRequest = 6;</code>
   * @return Whether the saveSnapshotDataRequest field is set.
   */
  boolean hasSaveSnapshotDataRequest();
  /**
   * <code>.basekv.SaveSnapshotDataRequest saveSnapshotDataRequest = 6;</code>
   * @return The saveSnapshotDataRequest.
   */
  com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequest getSaveSnapshotDataRequest();
  /**
   * <code>.basekv.SaveSnapshotDataRequest saveSnapshotDataRequest = 6;</code>
   */
  com.zachary.bifromq.basekv.proto.SaveSnapshotDataRequestOrBuilder getSaveSnapshotDataRequestOrBuilder();

  /**
   * <code>.basekv.SaveSnapshotDataReply saveSnapshotDataReply = 7;</code>
   * @return Whether the saveSnapshotDataReply field is set.
   */
  boolean hasSaveSnapshotDataReply();
  /**
   * <code>.basekv.SaveSnapshotDataReply saveSnapshotDataReply = 7;</code>
   * @return The saveSnapshotDataReply.
   */
  com.zachary.bifromq.basekv.proto.SaveSnapshotDataReply getSaveSnapshotDataReply();
  /**
   * <code>.basekv.SaveSnapshotDataReply saveSnapshotDataReply = 7;</code>
   */
  com.zachary.bifromq.basekv.proto.SaveSnapshotDataReplyOrBuilder getSaveSnapshotDataReplyOrBuilder();

  /**
   * <code>.basekv.EnsureRange ensureRange = 8;</code>
   * @return Whether the ensureRange field is set.
   */
  boolean hasEnsureRange();
  /**
   * <code>.basekv.EnsureRange ensureRange = 8;</code>
   * @return The ensureRange.
   */
  com.zachary.bifromq.basekv.proto.EnsureRange getEnsureRange();
  /**
   * <code>.basekv.EnsureRange ensureRange = 8;</code>
   */
  com.zachary.bifromq.basekv.proto.EnsureRangeOrBuilder getEnsureRangeOrBuilder();

  /**
   * <code>.basekv.EnsureRangeReply ensureRangeReply = 9;</code>
   * @return Whether the ensureRangeReply field is set.
   */
  boolean hasEnsureRangeReply();
  /**
   * <code>.basekv.EnsureRangeReply ensureRangeReply = 9;</code>
   * @return The ensureRangeReply.
   */
  com.zachary.bifromq.basekv.proto.EnsureRangeReply getEnsureRangeReply();
  /**
   * <code>.basekv.EnsureRangeReply ensureRangeReply = 9;</code>
   */
  com.zachary.bifromq.basekv.proto.EnsureRangeReplyOrBuilder getEnsureRangeReplyOrBuilder();

  /**
   * <code>.basekv.PrepareMergeToRequest prepareMergeToRequest = 10;</code>
   * @return Whether the prepareMergeToRequest field is set.
   */
  boolean hasPrepareMergeToRequest();
  /**
   * <code>.basekv.PrepareMergeToRequest prepareMergeToRequest = 10;</code>
   * @return The prepareMergeToRequest.
   */
  com.zachary.bifromq.basekv.proto.PrepareMergeToRequest getPrepareMergeToRequest();
  /**
   * <code>.basekv.PrepareMergeToRequest prepareMergeToRequest = 10;</code>
   */
  com.zachary.bifromq.basekv.proto.PrepareMergeToRequestOrBuilder getPrepareMergeToRequestOrBuilder();

  /**
   * <code>.basekv.PrepareMergeToReply prepareMergeToReply = 11;</code>
   * @return Whether the prepareMergeToReply field is set.
   */
  boolean hasPrepareMergeToReply();
  /**
   * <code>.basekv.PrepareMergeToReply prepareMergeToReply = 11;</code>
   * @return The prepareMergeToReply.
   */
  com.zachary.bifromq.basekv.proto.PrepareMergeToReply getPrepareMergeToReply();
  /**
   * <code>.basekv.PrepareMergeToReply prepareMergeToReply = 11;</code>
   */
  com.zachary.bifromq.basekv.proto.PrepareMergeToReplyOrBuilder getPrepareMergeToReplyOrBuilder();

  /**
   * <code>.basekv.MergeRequest mergeRequest = 12;</code>
   * @return Whether the mergeRequest field is set.
   */
  boolean hasMergeRequest();
  /**
   * <code>.basekv.MergeRequest mergeRequest = 12;</code>
   * @return The mergeRequest.
   */
  com.zachary.bifromq.basekv.proto.MergeRequest getMergeRequest();
  /**
   * <code>.basekv.MergeRequest mergeRequest = 12;</code>
   */
  com.zachary.bifromq.basekv.proto.MergeRequestOrBuilder getMergeRequestOrBuilder();

  /**
   * <code>.basekv.MergeReply mergeReply = 13;</code>
   * @return Whether the mergeReply field is set.
   */
  boolean hasMergeReply();
  /**
   * <code>.basekv.MergeReply mergeReply = 13;</code>
   * @return The mergeReply.
   */
  com.zachary.bifromq.basekv.proto.MergeReply getMergeReply();
  /**
   * <code>.basekv.MergeReply mergeReply = 13;</code>
   */
  com.zachary.bifromq.basekv.proto.MergeReplyOrBuilder getMergeReplyOrBuilder();

  /**
   * <code>.basekv.CancelMergingRequest cancelMergingRequest = 14;</code>
   * @return Whether the cancelMergingRequest field is set.
   */
  boolean hasCancelMergingRequest();
  /**
   * <code>.basekv.CancelMergingRequest cancelMergingRequest = 14;</code>
   * @return The cancelMergingRequest.
   */
  com.zachary.bifromq.basekv.proto.CancelMergingRequest getCancelMergingRequest();
  /**
   * <code>.basekv.CancelMergingRequest cancelMergingRequest = 14;</code>
   */
  com.zachary.bifromq.basekv.proto.CancelMergingRequestOrBuilder getCancelMergingRequestOrBuilder();

  /**
   * <code>.basekv.CancelMergingReply cancelMergingReply = 15;</code>
   * @return Whether the cancelMergingReply field is set.
   */
  boolean hasCancelMergingReply();
  /**
   * <code>.basekv.CancelMergingReply cancelMergingReply = 15;</code>
   * @return The cancelMergingReply.
   */
  com.zachary.bifromq.basekv.proto.CancelMergingReply getCancelMergingReply();
  /**
   * <code>.basekv.CancelMergingReply cancelMergingReply = 15;</code>
   */
  com.zachary.bifromq.basekv.proto.CancelMergingReplyOrBuilder getCancelMergingReplyOrBuilder();

  /**
   * <code>.basekv.MergeDoneRequest mergeDoneRequest = 16;</code>
   * @return Whether the mergeDoneRequest field is set.
   */
  boolean hasMergeDoneRequest();
  /**
   * <code>.basekv.MergeDoneRequest mergeDoneRequest = 16;</code>
   * @return The mergeDoneRequest.
   */
  com.zachary.bifromq.basekv.proto.MergeDoneRequest getMergeDoneRequest();
  /**
   * <code>.basekv.MergeDoneRequest mergeDoneRequest = 16;</code>
   */
  com.zachary.bifromq.basekv.proto.MergeDoneRequestOrBuilder getMergeDoneRequestOrBuilder();

  /**
   * <code>.basekv.MergeDoneReply mergeDoneReply = 17;</code>
   * @return Whether the mergeDoneReply field is set.
   */
  boolean hasMergeDoneReply();
  /**
   * <code>.basekv.MergeDoneReply mergeDoneReply = 17;</code>
   * @return The mergeDoneReply.
   */
  com.zachary.bifromq.basekv.proto.MergeDoneReply getMergeDoneReply();
  /**
   * <code>.basekv.MergeDoneReply mergeDoneReply = 17;</code>
   */
  com.zachary.bifromq.basekv.proto.MergeDoneReplyOrBuilder getMergeDoneReplyOrBuilder();

  public com.zachary.bifromq.basekv.proto.KVRangeMessage.PayloadTypeCase getPayloadTypeCase();
}
