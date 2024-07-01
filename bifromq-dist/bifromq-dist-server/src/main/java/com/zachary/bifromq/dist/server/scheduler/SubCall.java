package com.zachary.bifromq.dist.server.scheduler;

import com.zachary.bifromq.dist.rpc.proto.ClearRequest;
import com.zachary.bifromq.dist.rpc.proto.SubRequest;
import com.zachary.bifromq.dist.rpc.proto.UnsubRequest;

public abstract class SubCall {

    abstract SubCallType type();

    public static class AddTopicFilter extends SubCall {
        public final SubRequest request;

        public AddTopicFilter(SubRequest request) {
            this.request = request;
        }

        @Override
        final SubCallType type() {
            return SubCallType.ADD_TOPIC_FILTER;
        }
    }

    public static class InsertMatchRecord extends SubCall {
        public final SubRequest request;

        public InsertMatchRecord(SubRequest request) {
            this.request = request;
        }

        @Override
        final SubCallType type() {
            return SubCallType.INSERT_MATCH_RECORD;
        }

    }

    public static class JoinMatchGroup extends SubCall {
        public final SubRequest request;

        public JoinMatchGroup(SubRequest request) {
            this.request = request;
        }

        @Override
        final SubCallType type() {
            return SubCallType.JOIN_MATCH_GROUP;
        }

    }

    public static class RemoveTopicFilter extends SubCall {
        public final UnsubRequest request;

        public RemoveTopicFilter(UnsubRequest request) {
            this.request = request;
        }

        @Override
        final SubCallType type() {
            return SubCallType.REMOVE_TOPIC_FILTER;
        }


    }

    public static class DeleteMatchRecord extends SubCall {
        public final UnsubRequest request;

        public DeleteMatchRecord(UnsubRequest request) {
            this.request = request;
        }

        @Override
        final SubCallType type() {
            return SubCallType.DELETE_MATCH_RECORD;
        }


    }

    public static class LeaveJoinGroup extends SubCall {
        public final UnsubRequest request;

        public LeaveJoinGroup(UnsubRequest request) {
            this.request = request;
        }

        @Override
        final SubCallType type() {
            return SubCallType.LEAVE_JOIN_GROUP;
        }
    }

    public static class Clear extends SubCall {
        public final ClearRequest request;

        public Clear(ClearRequest request) {
            this.request = request;
        }

        @Override
        final SubCallType type() {
            return SubCallType.CLEAR;
        }
    }
}
