package com.zachary.bifromq.basekv.balance.command;

import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RecoveryCommand extends BalanceCommand {

    @Override
    public CommandType type() {
        return CommandType.RECOVERY;
    }

    @Override
    public String toString() {
        return String.format("RecoveryCommand{toStore=%s, kvRangeId=%s, expectedVer=%d}",
            getToStore(), KVRangeIdUtil.toString(getKvRangeId()), getExpectedVer());
    }

}
