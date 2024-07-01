package com.zachary.bifromq.dist.server.scheduler;

import com.zachary.bifromq.dist.rpc.proto.AddTopicFilterReply;
import com.zachary.bifromq.dist.rpc.proto.InboxSubInfo;
import com.zachary.bifromq.dist.rpc.proto.JoinMatchGroupReply;

public abstract class SubCallResult {
    abstract SubCallType type();

    public static class AddTopicFilterResult extends SubCallResult {
        public final AddTopicFilterReply.Result result;

        public AddTopicFilterResult(AddTopicFilterReply.Result result) {
            this.result = result;
        }

        @Override
        final SubCallType type() {
            return SubCallType.ADD_TOPIC_FILTER;
        }
    }

    public static class InsertMatchRecordResult extends SubCallResult {
        @Override
        final SubCallType type() {
            return SubCallType.INSERT_MATCH_RECORD;
        }

    }

    public static class JoinMatchGroupResult extends SubCallResult {
        public final JoinMatchGroupReply.Result result;

        public JoinMatchGroupResult(JoinMatchGroupReply.Result result) {
            this.result = result;
        }

        @Override
        final SubCallType type() {
            return SubCallType.JOIN_MATCH_GROUP;
        }

    }

    public static class RemoveTopicFilterResult extends SubCallResult {
        public final boolean exist;

        public RemoveTopicFilterResult(boolean exist) {
            this.exist = exist;
        }

        @Override
        final SubCallType type() {
            return SubCallType.REMOVE_TOPIC_FILTER;
        }


    }

    public static class DeleteMatchRecordResult extends SubCallResult {
        public final boolean exist;

        public DeleteMatchRecordResult(boolean exist) {
            this.exist = exist;
        }

        @Override
        final SubCallType type() {
            return SubCallType.DELETE_MATCH_RECORD;
        }
    }

    public static class LeaveJoinGroupResult extends SubCallResult {
        @Override
        final SubCallType type() {
            return SubCallType.LEAVE_JOIN_GROUP;
        }
    }

    public static class ClearResult extends SubCallResult {
        public final InboxSubInfo subInfo;

        public ClearResult(InboxSubInfo subInfo) {
            this.subInfo = subInfo;
        }

        @Override
        final SubCallType type() {
            return SubCallType.CLEAR;
        }
    }
}
