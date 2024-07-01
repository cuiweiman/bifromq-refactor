package com.zachary.bifromq.plugin.eventcollector;

import org.pf4j.ExtensionPoint;

public interface IEventCollector extends ExtensionPoint {
    /**
     * Implement this method to receive various events at runtime.
     * <p/>
     * Note: To reduce memory pressure, the argument event object will be reused in later call,
     * so the ownership is not transferred to the method implementation. Make a clone if needed.
     *
     * @param event the event reported
     */
    void report(Event<?> event);

    /**
     * This method will be called during broker shutdown
     */
    default void close() {
    }
}
