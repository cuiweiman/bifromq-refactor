package com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect;

import com.zachary.bifromq.plugin.eventcollector.ClientEvent;
import lombok.ToString;

/**
 * ClientClosed event will be reported after client being identified. Examine the reason enum and downcast to
 * corresponding subclass for concrete details.
 */
@ToString(callSuper = true)
public abstract class ClientDisconnectEvent<T extends ClientDisconnectEvent<T>> extends ClientEvent<T> {
}
