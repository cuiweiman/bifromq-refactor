package com.zachary.bifromq.basekv.balance.option;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class KVRangeBalanceControllerOptions {

    private long scheduleIntervalInMs = 5000;
    private List<String> balancers = new ArrayList<>();
}
