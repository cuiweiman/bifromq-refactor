package com.zachary.bifromq.basekv.balance.command;

import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
public class ChangeConfigCommand extends BalanceCommand {

    private Set<String> voters;
    private Set<String> learners;

    @Override
    public CommandType type() {
        return CommandType.CHANGE_CONFIG;
    }

    @Override
    public String toString() {
        return String.format("ChangeConfigCommand{toStore=%s, kvRangeId=%s, expectedVer=%d, voters=%s, learner=%s}",
            getToStore(), KVRangeIdUtil.toString(getKvRangeId()), getExpectedVer(), voters, learners);
    }
}
