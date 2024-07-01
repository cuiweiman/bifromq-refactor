package com.zachary.bifromq.plugin.settingprovider;

import org.pf4j.ExtensionPoint;

public interface ISettingProvider extends ExtensionPoint {
    /**
     * Provide a value of the setting for given tenant. The method will be called by BifroMQ working thread, so it's
     * expected to be performant and non-blocking otherwise the overall performance will be greatly impacted. It's
     * allowed to return null to reuse the current setting value, in case the value could not be determined in timely
     * manner.
     *
     * @param setting  the setting for the client
     * @param tenantId the id of the calling tenant
     * @return the setting value for the client or null
     */
    <R> R provide(Setting setting, String tenantId);

    /**
     * This method will be called during broker shutdown
     */
    default void close() {

    }
}
