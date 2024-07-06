package com.zachary.bifromq.plugin.authprovider;

import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthData;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthResult;
import com.zachary.bifromq.plugin.authprovider.type.MQTTAction;
import com.zachary.bifromq.plugin.authprovider.type.Ok;
import com.google.common.base.Strings;

import java.util.concurrent.CompletableFuture;

class DevOnlyAuthProvider implements IAuthProvider {
    @Override
    public CompletableFuture<MQTT3AuthResult> auth(MQTT3AuthData authData) {
        if (!Strings.isNullOrEmpty(authData.getUsername())) {
            String[] username = authData.getUsername().split("/");
            if (username.length == 2) {
                return CompletableFuture.completedFuture(MQTT3AuthResult
                    .newBuilder()
                    .setOk(Ok.newBuilder()
                        .setTenantId(username[0])
                        .setUserId(username[1])
                        .build())
                    .build());
            } else {
                return CompletableFuture.completedFuture(MQTT3AuthResult.newBuilder()
                    .setOk(Ok.newBuilder()
                        .setTenantId("DevOnly")
                        .setUserId("DevUser_" + System.nanoTime()).build())
                    .build());
            }
        } else {
            return CompletableFuture.completedFuture(MQTT3AuthResult.newBuilder()
                .setOk(Ok.newBuilder()
                    .setTenantId("DevOnly")
                    .setUserId("DevUser_" + System.nanoTime()).build())
                .build());
        }
    }

    @Override
    public CompletableFuture<Boolean> check(ClientInfo clientInfo, MQTTAction action) {
        return CompletableFuture.completedFuture(true);
    }
}