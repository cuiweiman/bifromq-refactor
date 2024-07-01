package com.zachary.bifromq.basekv.balance.command;

import com.zachary.bifromq.basekv.utils.KVRangeIdUtil;
import com.google.protobuf.ByteString;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Base64;

@Getter
@Setter
@SuperBuilder
public class SplitCommand extends BalanceCommand {

    private ByteString splitKey;

    @Override
    public CommandType type() {
        return CommandType.SPLIT;
    }

    @Override
    public String toString() {
        return String.format("SplitCommand{toStore=%s, kvRangeId=%s, expectedVer=%d, splitKey=%s}",
            getToStore(), KVRangeIdUtil.toString(getKvRangeId()), getExpectedVer(),
            Base64.getEncoder().encodeToString(splitKey.toByteArray()));
    }

}
