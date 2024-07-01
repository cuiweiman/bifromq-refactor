package com.zachary.bifromq.basekv.balance.command;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class BalanceCommand {

    private String toStore;
    private Long expectedVer;
    private KVRangeId kvRangeId;

    public abstract CommandType type();

}
