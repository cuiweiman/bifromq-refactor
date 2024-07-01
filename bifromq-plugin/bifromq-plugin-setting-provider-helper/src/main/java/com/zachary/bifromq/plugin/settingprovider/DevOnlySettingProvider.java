package com.zachary.bifromq.plugin.settingprovider;

class DevOnlySettingProvider implements ISettingProvider {
    @Override
    public <R> R provide(Setting setting, String tenantId) {
        return setting.current(tenantId);
    }
}
