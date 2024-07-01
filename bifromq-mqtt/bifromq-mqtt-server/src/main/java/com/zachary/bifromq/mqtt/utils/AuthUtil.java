package com.zachary.bifromq.mqtt.utils;

import com.zachary.bifromq.mqtt.handler.ChannelAttrs;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthData;
import com.zachary.bifromq.plugin.authprovider.type.MQTTAction;
import com.zachary.bifromq.plugin.authprovider.type.PubAction;
import com.zachary.bifromq.plugin.authprovider.type.SubAction;
import com.zachary.bifromq.plugin.authprovider.type.UnsubAction;
import com.zachary.bifromq.type.QoS;
import com.google.common.base.Strings;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.cert.X509Certificate;
import java.util.Base64;

import static com.google.protobuf.UnsafeByteOperations.unsafeWrap;

public class AuthUtil {
    @SneakyThrows
    public static MQTT3AuthData buildMQTT3AuthData(Channel channel, MqttConnectMessage msg) {
        assert msg.variableHeader().version() != 5;
        MQTT3AuthData.Builder authData = MQTT3AuthData.newBuilder();
        if (msg.variableHeader().version() == 3) {
            authData.setIsMQIsdp(true);
        }
        X509Certificate cert = ChannelAttrs.clientCertificate(channel);
        if (cert != null) {
            authData.setCert(unsafeWrap(Base64.getEncoder().encode(cert.getEncoded())));
        }
        if (msg.variableHeader().hasUserName()) {
            authData.setUsername(msg.payload().userName());
        }
        if (msg.variableHeader().hasPassword()) {
            authData.setPassword(unsafeWrap(msg.payload().passwordInBytes()));
        }
        if (!Strings.isNullOrEmpty(msg.payload().clientIdentifier())) {
            authData.setClientId(msg.payload().clientIdentifier());
        }
        InetSocketAddress remoteAddr = ChannelAttrs.socketAddress(channel);
        if (remoteAddr != null) {
            authData.setRemotePort(remoteAddr.getPort())
                    .setChannelId(channel.id().asLongText());
            InetAddress ip = remoteAddr.getAddress();
            if (remoteAddr.getAddress() != null) {
                authData.setRemoteAddr(ip.getHostAddress());
            }
        }
        return authData.build();
    }


    public static MQTTAction buildPubAction(String topic, QoS qos, boolean retained) {
        return MQTTAction.newBuilder()
                .setPub(PubAction.newBuilder()
                        .setTopic(topic)
                        .setQos(qos)
                        .setIsRetained(retained)
                        .build())
                .build();
    }

    public static MQTTAction buildSubAction(String topicFilter, QoS qos) {
        return MQTTAction.newBuilder()
                .setSub(SubAction.newBuilder()
                        .setTopicFilter(topicFilter)
                        .setQos(qos)
                        .build())
                .build();
    }

    public static MQTTAction buildUnsubAction(String topicFilter) {
        return MQTTAction.newBuilder()
                .setUnsub(UnsubAction.newBuilder()
                        .setTopicFilter(topicFilter)
                        .build())
                .build();
    }
}
