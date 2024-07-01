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

package com.zachary.bifromq.mqtt.client;

import com.google.protobuf.ByteString;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class MqttTestAsyncClient {
    private final MqttAsyncClient client;
    private final ReplaySubject<MqttMsg> messageSubject = ReplaySubject.create();
    private final ReplaySubject<MqttResponse> responseSubject = ReplaySubject.create();

    @SneakyThrows
    public MqttTestAsyncClient(String brokerURI, String clientId) {
        client = new MqttAsyncClient(brokerURI, clientId, new MemoryPersistence());
    }

    public MqttTestAsyncClient(String brokerURI) {
        this(brokerURI, MqttClient.generateClientId());
    }

    public void manualAck(boolean manual) {
        client.setManualAcks(manual);
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    @SneakyThrows
    public CompletableFuture<Void> connect(MqttConnectOptions options) {
        CompletableFuture<Void> onDone = new CompletableFuture<>();
        IMqttToken token = client.connect(options);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                log.debug("Receive message from broker: {}", message);
                messageSubject.onNext(new MqttMsg(topic, message));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                try {
                    if (token.getResponse() != null) {
                        responseSubject.onNext(new MqttResponse(token.getResponse().getType(),
                            token.getResponse().getMessageId()));
                    }
                } catch (Exception exception) {
                    responseSubject.onError(exception);
                }
            }
        });
        token.setActionCallback(convert(onDone));
        return onDone;
    }

    @SneakyThrows
    public CompletableFuture<Void> disconnect() {
        CompletableFuture<Void> onDone = new CompletableFuture<>();
        IMqttToken token = client.disconnect();
        token.setActionCallback(convert(onDone));
        return onDone;
    }

    @SneakyThrows
    public void close() {
        client.close();
    }

    public void closeForcibly() {
        try {
            client.disconnectForcibly();
            client.close(true);
        } catch (Throwable e) {
            // ignore intentionally;
        }
    }
//    Observable<MqttMsg> subscribe(String topicFilter, int qos) {
//        Observable<MqttMsg> orig = Observable.create(emitter ->
//                client.subscribe(topicFilter, qos, (topic, message) -> emitter.onNext(new MqttMsg(topic, message))));
//        Observable<MqttMsg> replayed = orig.cache();
//        replayed.subscribe().dispose();// trigger the sub action
//        return replayed.doOnDispose(() -> client.unsubscribe(topicFilter)).share();
//    }

    @SneakyThrows
    public CompletableFuture<Observable<MqttMsg>> subscribe(String topicFilter, int qos) {
        CompletableFuture<Observable<MqttMsg>> onDone = new CompletableFuture<>();
        IMqttToken token = client.subscribe(topicFilter, qos);
        token.setActionCallback(convert(onDone, messageSubject));
        return onDone;
    }

    public Observable<MqttMsg> messageArrived() {
        return messageSubject;
    }

    public Observable<MqttResponse> deliveryComplete() {
        return responseSubject;
    }

    @SneakyThrows
    public CompletableFuture<Void> unsubscribe(String topicFilter) {
        CompletableFuture<Void> onDone = new CompletableFuture<>();
        IMqttToken token = client.unsubscribe(topicFilter);
        token.setActionCallback(convert(onDone));
        return onDone;
    }

    @SneakyThrows
    public void ack(int messageId, int qos) {
        client.messageArrivedComplete(messageId, qos);
    }

    @SneakyThrows
    public CompletableFuture<Void> publish(String topic, int qos, ByteString payload, boolean retain) {
        CompletableFuture<Void> onDone = new CompletableFuture<>();
        IMqttToken token = client.publish(topic, payload.toByteArray(), qos, retain);
        token.setActionCallback(convert(onDone));
        return onDone;
    }

    private IMqttActionListener convert(CompletableFuture<Void> onDone) {
        return convert(onDone, null);
    }

    private <T> IMqttActionListener convert(CompletableFuture<T> onDone, T value) {
        return new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                onDone.complete(null);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                onDone.completeExceptionally(exception);
            }
        };
    }
}
