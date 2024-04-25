package com.zachary.bifromq.sysprops.parser.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * {@link AccessLevel#PRIVATE} 无参构造函数 私有化
 *
 * @description: 布尔类型解析
 * @author: cuiweiman
 * @date: 2024/4/3 11:52
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BooleanParser implements PropParser<Boolean> {

    public static final BooleanParser INSTANCE = new BooleanParser();

    @Override
    public Boolean parse(String value) {
        return "true".equalsIgnoreCase(value)
                || "yes".equalsIgnoreCase(value)
                || "1".equalsIgnoreCase(value);
    }

}
