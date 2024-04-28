package com.zachary.bifromq.basecluster.memberlist;

import com.zachary.bifromq.basecluster.membership.proto.Doubt;
import com.zachary.bifromq.basecluster.membership.proto.Endorse;
import com.zachary.bifromq.basecluster.membership.proto.Fail;
import com.zachary.bifromq.basecluster.membership.proto.HostEndpoint;
import com.zachary.bifromq.basecluster.membership.proto.HostMember;
import com.zachary.bifromq.basecluster.membership.proto.Join;
import com.zachary.bifromq.basecluster.membership.proto.Quit;
import com.zachary.bifromq.basecluster.messenger.MessageEnvelope;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import com.zachary.bifromq.basecrdt.proto.Replica;
import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.schedulers.Timed;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class Fixtures {
    public static final ByteString LOCAL_REPLICA_ID = ByteString.copyFromUtf8("hostListCRDTReplicaId");
    public static final Replica LOCAL_REPLICA = Replica.newBuilder()
        .setUri(CRDTUtil.AGENT_HOST_MAP_URI).setId(LOCAL_REPLICA_ID).build();
    public static final InetSocketAddress LOCAL_ADDR = new InetSocketAddress("localhost", 1111);
    public static final InetSocketAddress REMOTE_ADDR_1 = new InetSocketAddress("localhost", 2222);

    public static final HostEndpoint ZOMBIE_ENDPOINT = HostEndpoint.newBuilder()
        .setId(ByteString.copyFromUtf8("zombie"))
        .setAddress(LOCAL_ADDR.getHostName())
        .setPort(LOCAL_ADDR.getPort())
        .build();
    public static final HostEndpoint LOCAL_ENDPOINT = HostEndpoint.newBuilder()
        .setId(LOCAL_REPLICA_ID)
        .setAddress(LOCAL_ADDR.getHostName())
        .setPort(LOCAL_ADDR.getPort())
        .build();

    public static final HostMember LOCAL = HostMember.newBuilder()
        .setIncarnation(0)
        .setEndpoint(LOCAL_ENDPOINT)
        .build();
    public static final HostEndpoint REMOTE_HOST_1_ENDPOINT = HostEndpoint.newBuilder()
        .setId(ByteString.copyFromUtf8("remoteHost"))
        .setAddress(REMOTE_ADDR_1.getHostName())
        .setPort(REMOTE_ADDR_1.getPort())
        .build();
    public static Timed<MessageEnvelope> joinMsg(HostMember member) {
        return to(ClusterMessage.newBuilder()
            .setJoin(Join.newBuilder()
                .setMember(member)
                .build())
            .build());
    }

    public static Timed<MessageEnvelope> joinMsg(HostMember member, HostEndpoint expected) {
        return to(ClusterMessage.newBuilder()
            .setJoin(Join.newBuilder()
                .setMember(member)
                .setExpectedHost(expected)
                .build())
            .build());
    }

    public static Timed<MessageEnvelope> quitMsg(HostEndpoint quitEndpoint, int incarnation) {
        return to(ClusterMessage.newBuilder()
            .setQuit(Quit.newBuilder()
                .setEndpoint(quitEndpoint)
                .setIncarnation(incarnation)
                .build())
            .build());
    }

    public static Timed<MessageEnvelope> endorseMsg(HostEndpoint endorseEndpoint, int incarnation) {
        return to(ClusterMessage.newBuilder()
            .setEndorse(Endorse.newBuilder()
                .setEndpoint(endorseEndpoint)
                .setIncarnation(incarnation)
                .build())
            .build());
    }

    public static Timed<MessageEnvelope> doubtMsg(HostEndpoint doubtEndpoint, int incarnation) {
        return to(ClusterMessage.newBuilder()
            .setDoubt(Doubt.newBuilder()
                .setEndpoint(doubtEndpoint)
                .setIncarnation(incarnation)
                .build())
            .build());
    }

    public static Timed<MessageEnvelope> failMsg(HostEndpoint failedEndpoint, int incarnation) {
        return to(ClusterMessage.newBuilder()
            .setFail(Fail.newBuilder()
                .setEndpoint(failedEndpoint)
                .setIncarnation(incarnation)
                .build())
            .build());
    }

    public static Timed<MessageEnvelope> to(ClusterMessage clusterMessage) {
        return new Timed<>(MessageEnvelope.builder()
            .message(clusterMessage)
            .build(), System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
}
