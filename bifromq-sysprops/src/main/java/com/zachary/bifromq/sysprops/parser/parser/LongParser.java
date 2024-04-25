package com.zachary.bifromq.sysprops.parser.parser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * {@link AccessLevel#PRIVATE} 全参 构造函数 private 私有化
 *
 * @description: Long 类型解析
 * @author: cuiweiman
 * @date: 2024/4/3 11:52
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LongParser implements PropParser<Long> {

    private final long lowBound;
    private final long highBoundEx;

    /**
     * 正数
     */
    public static final LongParser POSITIVE = new LongParser(1, Long.MAX_VALUE);

    /**
     * 非负数
     */
    public static final LongParser NON_NEGATIVE = new LongParser(0, Long.MAX_VALUE);

    public static LongParser from(long lowBound, long highBoundEx) {
        assert lowBound < highBoundEx;
        return new LongParser(lowBound, highBoundEx);
    }

    @Override
    public Long parse(String value) {
        long val = lowBound >= 0 ? Long.parseUnsignedLong(value) : Long.parseLong(value);
        if (val >= lowBound && val < highBoundEx) {
            return val;
        }
        throw new SysPropParseException(String.format("%d is out of bound [%d,%d)", val, lowBound, highBoundEx));
    }
}
