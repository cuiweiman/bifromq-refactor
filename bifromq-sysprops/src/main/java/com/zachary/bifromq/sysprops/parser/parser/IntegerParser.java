package com.zachary.bifromq.sysprops.parser.parser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * {@link AccessLevel#PRIVATE} 全参 构造函数 private 私有化
 *
 * @description: Integer 类型解析
 * @author: cuiweiman
 * @date: 2024/4/3 11:52
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerParser implements PropParser<Integer> {

    /**
     * 正数 范围 左闭右开
     */
    public static final IntegerParser POSITIVE = new IntegerParser(1, Integer.MAX_VALUE);
    /**
     * 非负数 范围 左闭右开
     */
    public static final IntegerParser NON_NEGATIVE = new IntegerParser(0, Integer.MAX_VALUE);

    private final int lowBound;
    private final int highBoundEx;

    public static IntegerParser from(int lowBound, int highBoundEx) {
        assert lowBound < highBoundEx;
        return new IntegerParser(lowBound, highBoundEx);
    }


    @Override
    public Integer parse(String value) {
        int val = lowBound >= 0 ?
                Integer.parseUnsignedInt(value)
                : Integer.parseInt(value);
        if (val >= lowBound && val < highBoundEx) {
            return val;
        }
        throw new SysPropParseException(String.format("%d is out of bound [%d,%d)", val, lowBound, highBoundEx));
    }


}

