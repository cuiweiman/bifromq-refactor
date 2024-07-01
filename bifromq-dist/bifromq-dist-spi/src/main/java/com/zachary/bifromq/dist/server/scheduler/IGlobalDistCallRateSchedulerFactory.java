package com.zachary.bifromq.dist.server.scheduler;

import com.zachary.bifromq.basecrdt.service.ICRDTService;
import com.zachary.bifromq.basescheduler.ICallScheduler;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;

public interface IGlobalDistCallRateSchedulerFactory {
    IGlobalDistCallRateSchedulerFactory DEFAULT = (settingProvider, crdtService) -> new ICallScheduler<>() {
    };

    ICallScheduler<DistCall> createScheduler(ISettingProvider settingProvider, ICRDTService crdtService);

}
