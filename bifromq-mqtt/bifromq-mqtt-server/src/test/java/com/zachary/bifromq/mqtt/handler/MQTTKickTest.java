/*
 * Copyright (c) 2023. Baidu, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zachary.bifromq.mqtt.handler;

import com.zachary.bifromq.mqtt.utils.MQTTMessageUtils;
import com.zachary.bifromq.sessiondict.rpc.proto.Quit;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.zachary.bifromq.plugin.eventcollector.EventType.CLIENT_CONNECTED;
import static com.zachary.bifromq.plugin.eventcollector.EventType.KICKED;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_ACCEPTED;

public class MQTTKickTest extends BaseMQTTTest {

    @Test
    public void testKick() {
        mockAuthPass();
        mockSessionReg();
        mockInboxHas(false);
        MqttConnectMessage connectMessage = MQTTMessageUtils.mqttConnectMessage(true);
        channel.writeInbound(connectMessage);
        MqttConnAckMessage ackMessage = channel.readOutbound();
        Assert.assertEquals(CONNECTION_ACCEPTED, ackMessage.variableHeader().connectReturnCode());

        // kick
        kickSubject.onNext(Quit.newBuilder().build());
        channel.runPendingTasks();
        Assert.assertFalse(channel.isActive());
        verifyEvent(2, CLIENT_CONNECTED, KICKED);
    }
}
