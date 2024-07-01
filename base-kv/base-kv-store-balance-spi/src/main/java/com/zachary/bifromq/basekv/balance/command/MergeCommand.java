package com.zachary.bifromq.basekv.balance.command;

import com.zachary.bifromq.basekv.proto.KVRangeId;
import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MergeCommand extends BalanceCommand {

    private KVRangeId mergeeId;

    @Override
    public CommandType type() {
        return CommandType.MERGE;
    }

    @Override
    public String toString() {
        return String.format("MergeCommand{toStore=%s, kvRangeId=%s, mergeeId=%s, expectedVer=%d}",
            getToStore(), KVRangeIdUtil.toString(getKvRangeId()), KVRangeIdUtil.toString(mergeeId), getExpectedVer());
    }

}
