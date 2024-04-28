package com.zachary.bifromq.basecluster.fd;

import com.zachary.bifromq.basecluster.fd.proto.Ack;
import com.zachary.bifromq.basecluster.fd.proto.Nack;
import com.zachary.bifromq.basecluster.fd.proto.Ping;
import com.zachary.bifromq.basecluster.fd.proto.PingReq;
import com.zachary.bifromq.basecluster.proto.ClusterMessage;
import com.google.protobuf.ByteString;

import java.net.InetSocketAddress;
import java.time.Duration;

public class Fixtures {
    public static final InetSocketAddress LOCAL_ADDRESS = new InetSocketAddress("127.0.0.1", 12345);
    public static final InetSocketAddress DIRECT_TARGET_ADDRESS = new InetSocketAddress("127.0.0.2", 12345);
    public static final InetSocketAddress INDIRECT_TARGET_ADDRESS_1 = new InetSocketAddress("127.0.0.3", 12345);
    public static final InetSocketAddress INDIRECT_TARGET_ADDRESS_2 = new InetSocketAddress("127.0.0.4", 12345);
    public static final IProbingTarget LOCAL_PROBING_TARGET = new IProbingTarget() {
        @Override
        public ByteString id() {
            return ByteString.copyFromUtf8("local");
        }

        @Override
        public InetSocketAddress addr() {
            return LOCAL_ADDRESS;
        }
    };
    public static final IProbingTarget DIRECT_PROBING_TARGET = new IProbingTarget() {
        @Override
        public ByteString id() {
            return ByteString.copyFromUtf8("probeTarget");
        }

        @Override
        public InetSocketAddress addr() {
            return DIRECT_TARGET_ADDRESS;
        }
    };
    public static final IProbingTarget INDIRECT_PROBING_TARGET_1 = new IProbingTarget() {
        @Override
        public ByteString id() {
            return ByteString.copyFromUtf8("inDirectProbeTarget1");
        }

        @Override
        public InetSocketAddress addr() {
            return INDIRECT_TARGET_ADDRESS_1;
        }
    };
    public static final IProbingTarget INDIRECT_PROBING_TARGET_2 = new IProbingTarget() {
        @Override
        public ByteString id() {
            return ByteString.copyFromUtf8("inDirectProbeTarget2");
        }

        @Override
        public InetSocketAddress addr() {
            return INDIRECT_TARGET_ADDRESS_2;
        }
    };

    public static final Duration BASE_PROBE_INTERVAL = Duration.ofMillis(1000);
    public static final Duration BASE_PROBE_TIMEOUT = Duration.ofMillis(500);
    public static final int INDIRECT_PROBES = 2;
    public static final int WORST_HEALTH_SCORE = 4;

    public static ClusterMessage toPing(int seqNum, IProbingTarget local, IProbingTarget target) {
        return ClusterMessage.newBuilder()
            .setPing(Ping.newBuilder()
                .setSeqNo(seqNum)
                .setId(target.id())
                .setPingerId(local.id())
                .setPingerAddr(local.addr().getAddress().getHostAddress())
                .setPingerPort(local.addr().getPort())
                .build())
            .build();
    }

    public static ClusterMessage toPingReq(int seqNum, IProbingTarget local, IProbingTarget probe) {
        return ClusterMessage.newBuilder()
            .setPingReq(PingReq.newBuilder()
                .setSeqNo(seqNum)
                .setId(probe.id())
                .setAddr(probe.addr().getAddress().getHostAddress())
                .setPort(probe.addr().getPort())
                .setPingerId(local.id())
                .setPingerAddr(local.addr().getAddress().getHostAddress())
                .setPingerPort(local.addr().getPort())
                .build())
            .build();
    }

    public static ClusterMessage toPingAck(int seqNum) {
        return ClusterMessage.newBuilder()
            .setAck(Ack.newBuilder().setSeqNo(seqNum).build())
            .build();
    }

    public static ClusterMessage toPingNack(int seqNum) {
        return ClusterMessage.newBuilder()
            .setNack(Nack.newBuilder().setSeqNo(seqNum).build())
            .build();
    }
}
