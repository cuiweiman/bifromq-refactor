package com.zachary.bifromq.basekv.balance.command;

import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TransferLeadershipCommand extends BalanceCommand {

    private String newLeaderStore;

    @Override
    public CommandType type() {
        return CommandType.TRANSFER_LEADERSHIP;
    }

    @Override
    public String toString() {
        return String.format("TransferLeadershipCommand{toStore=%s, kvRangeId=%s, expectedVer=%d, newLeaderStore=%s}",
            getToStore(), KVRangeIdUtil.toString(getKvRangeId()), getExpectedVer(), newLeaderStore);
    }
}
