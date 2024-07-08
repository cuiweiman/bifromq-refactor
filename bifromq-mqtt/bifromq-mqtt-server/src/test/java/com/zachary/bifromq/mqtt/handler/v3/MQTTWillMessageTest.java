package com.zachary.bifromq.mqtt.handler.v3;


import com.zachary.bifromq.mqtt.handler.BaseMQTTTest;
import com.zachary.bifromq.sessiondict.rpc.proto.Quit;
import com.zachary.bifromq.type.ClientInfo;
import com.zachary.bifromq.type.QoS;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.plugin.eventcollector.EventType.CLIENT_CONNECTED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.IDLE;
import static com.zachary.bifromq.plugin.eventcollector.EventType.KICKED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.MSG_RETAINED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.MSG_RETAINED_ERROR;
import static com.zachary.bifromq.plugin.eventcollector.EventType.PUB_ACTION_DISALLOW;
import static com.zachary.bifromq.plugin.eventcollector.EventType.RETAIN_MSG_CLEARED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.WILL_DISTED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.WILL_DIST_ERROR;
import static com.zachary.bifromq.retain.rpc.proto.RetainReply.Result.CLEARED;
import static com.zachary.bifromq.retain.rpc.proto.RetainReply.Result.ERROR;
import static com.zachary.bifromq.retain.rpc.proto.RetainReply.Result.RETAINED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
public class MQTTWillMessageTest extends BaseMQTTTest {

    @Test
    public void willWhenIdle() {
        connectAndVerify(true, false, 30, true, false);
        mockAuthCheck(true);
        mockDistDist(true);
        channel.advanceTimeBy(100, TimeUnit.SECONDS);
        testTicker.advanceTimeBy(50, TimeUnit.SECONDS);
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(3, CLIENT_CONNECTED, IDLE, WILL_DISTED);
    }

    //    @Test
    //    public void willWhenSelfKick() {
    //        connectAndVerify(true, false, 30, true, false);
    //        mockAuthCheck(CheckResult.Type.ALLOW);
    //        mockDistDist(true);
    //        kickSubject.onNext(
    //            Quit.newBuilder()
    //                .setKiller(
    //                    ClientInfo.newBuilder()
    //                        .setTenantId(trafficId)
    //                        .setUserId(userId)
    //                        .setMqtt3ClientInfo(
    //                            MQTT3ClientInfo.newBuilder()
    //                                .setClientId(clientId)
    //                                .build()
    //                        )
    //                        .build()
    //                )
    //                .build()
    //        );
    //        channel.runPendingTasks();
    //        Assert.assertFalse(channel.isActive());
    //        verifyEvent(3, ClientConnected, Kicked, WillDisted);
    //    }

    @Test
    public void willWhenNotSelfKick() {
        connectAndVerify(true, false, 30, true, false);
        mockAuthCheck(true);
        mockDistDist(true);
        kickSubject.onNext(
                Quit.newBuilder()
                        .setKiller(
                                ClientInfo.newBuilder()
                                        .setTenantId("sys")
                                        .putMetadata("agent", "sys")
                                        .putMetadata("clientId", clientId)
                                        .build()
                        )
                        .build()
        );
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(3, CLIENT_CONNECTED, KICKED, WILL_DISTED);
    }

    @Test
    public void willAuthCheckFailed() {
        connectAndVerify(true, false, 30, true, false);
        mockAuthCheck(false);
        channel.advanceTimeBy(50, TimeUnit.SECONDS);
        testTicker.advanceTimeBy(50, TimeUnit.SECONDS);
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(3, CLIENT_CONNECTED, IDLE, PUB_ACTION_DISALLOW);
        verify(distClient, times(0)).pub(anyLong(), anyString(), any(QoS.class), any(ByteBuffer.class), anyInt(),
                any(ClientInfo.class));
    }

    @Test
    public void willDistError() {
        connectAndVerify(true, false, 30, true, false);
        mockAuthCheck(true);
        mockDistDist(false);
        channel.advanceTimeBy(50, TimeUnit.SECONDS);
        testTicker.advanceTimeBy(50, TimeUnit.SECONDS);
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(3, CLIENT_CONNECTED, IDLE, WILL_DIST_ERROR);
    }

    @Test
    public void willDistDrop() {
        connectAndVerify(true, false, 30, true, false);
        mockAuthCheck(true);
        mockDistDrop();
        channel.advanceTimeBy(50, TimeUnit.SECONDS);
        testTicker.advanceTimeBy(50, TimeUnit.SECONDS);
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(3, CLIENT_CONNECTED, IDLE, WILL_DIST_ERROR);
    }


    @Test
    public void willAndRetain() {
        connectAndVerify(true, false, 30, true, true);
        mockAuthCheck(true);
        mockDistDist(true);
        mockRetainPipeline(RETAINED);
        channel.advanceTimeBy(50, TimeUnit.SECONDS);
        testTicker.advanceTimeBy(50, TimeUnit.SECONDS);
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(4, CLIENT_CONNECTED, IDLE, WILL_DISTED, MSG_RETAINED);
    }


    @Test
    public void willAndRetainClear() {
        connectAndVerify(true, false, 30, true, true);
        mockAuthCheck(true);
        mockDistDist(true);
        mockRetainPipeline(CLEARED);
        channel.advanceTimeBy(50, TimeUnit.SECONDS);
        testTicker.advanceTimeBy(50, TimeUnit.SECONDS);
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(4, CLIENT_CONNECTED, IDLE, WILL_DISTED, RETAIN_MSG_CLEARED);
    }

    @Test
    public void willAndRetainError() {
        connectAndVerify(true, false, 30, true, true);
        mockAuthCheck(true);
        mockDistDist(true);
        mockRetainPipeline(ERROR);
        channel.advanceTimeBy(50, TimeUnit.SECONDS);
        testTicker.advanceTimeBy(50, TimeUnit.SECONDS);
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(4, CLIENT_CONNECTED, IDLE, WILL_DISTED, MSG_RETAINED_ERROR);
    }
}
