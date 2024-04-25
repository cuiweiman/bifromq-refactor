package com.zachary.bifromq.sysprops.parser.parser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * {@link AccessLevel#PRIVATE} 全参 构造函数 private 私有化
 * <p>
 * {@link #lowBound} 和 {@link  #highBoundEx} 用于判断 String 转 Double 后的 结果范围
 * 布尔类型{@link #excludeLowerBound} 用于声明 是否包含 {@link #lowBound} 的边界
 * <p>
 * exclude lower bound 指的是 "不包含 左边界"
 * 若 {@link #excludeLowerBound} 为 true, 则解析范围是 (lowBound, highBoundEx)
 * 否则解析范围是 [lowBound, highBoundEx)
 *
 * @description: Double 类型解析
 * @author: cuiweiman
 * @date: 2024/4/3 11:52
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DoubleParser implements PropParser<Double> {

    private final double lowBound;
    private final double highBoundEx;
    private final boolean excludeLowerBound;


    public static DoubleParser from(double lowBound, double highBoundEx, boolean excludeLowerBound) {
        // 进行参数校验 断言 必须存在 lowBound < highBoundEx
        assert Double.compare(lowBound, highBoundEx) < 0;
        return new DoubleParser(lowBound, highBoundEx, excludeLowerBound);
    }

    /**
     * {@link Double#compare(double, double)}
     * d1==d2, result = 0
     * d1 < d2, result = -1
     * d1 > d2, result =1
     */
    @Override
    public Double parse(String value) {
        double val = Double.parseDouble(value);
        int isLower = Double.compare(lowBound, val);

        if ((excludeLowerBound ? isLower < 0 : isLower <= 0)
                && Double.compare(val, highBoundEx) < 0) {
            return val;
        }
        if (excludeLowerBound) {
            throw new SysPropParseException(String.format("%f is out of bound (%f,%f)", val, lowBound, highBoundEx));
        } else {
            throw new SysPropParseException(String.format("%f is out of bound [%f,%f)", val, lowBound, highBoundEx));
        }
    }

}
