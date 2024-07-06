package com.zachary.bifromq.plugin.authprovider;

import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthData;
import com.zachary.bifromq.plugin.authprovider.type.MQTT3AuthResult;
import com.zachary.bifromq.plugin.authprovider.type.MQTTAction;
import org.pf4j.ExtensionPoint;

import java.util.concurrent.CompletableFuture;

/**
 * 扩展点需要遵循 扩展规范
 * <a href="http://www.blogjava.net/yangbutao/archive/2007/09/27/148500.html">ExtensionPoint</a>
 *
 * @description: 鉴权扩展点
 * @author: cuiweiman
 * @date: 2024/6/25 14:20
 */
public interface IAuthProvider extends ExtensionPoint {

    /**
     * Implement this method to hook authentication logic of mqtt3 client into BifroMQ.
     *
     * @param authData the authentication data
     */
    CompletableFuture<MQTT3AuthResult> auth(MQTT3AuthData authData);

    /**
     * Implement this method to hook action permission check logic.
     *
     * @param client the client to check permission
     * @param action the action
     */
    CompletableFuture<Boolean> check(ClientInfo client, MQTTAction action);

    /**
     * This method will be called during broker shutdown
     */
    default void close() {
    }



}
