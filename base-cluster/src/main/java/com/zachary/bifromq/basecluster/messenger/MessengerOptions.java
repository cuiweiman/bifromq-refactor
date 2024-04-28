
package com.zachary.bifromq.basecluster.messenger;

import com.zachary.bifromq.basecluster.transport.Transport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Duration;

@Builder(toBuilder = true)
@Accessors(chain = true, fluent = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessengerOptions {
    private Transport.TransportOptions transporterOptions = new Transport.TransportOptions();
    private int maxFanout = 4;
    private int maxFanoutGossips = 60;
    private int maxHealthScore = 4;
    private int retransmitMultiplier = 4;
    private Duration spreadPeriod = Duration.ofMillis(500);
}
