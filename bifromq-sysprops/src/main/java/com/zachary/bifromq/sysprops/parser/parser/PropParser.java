package com.zachary.bifromq.sysprops.parser.parser;

/**
 * 主要进行 String 类型 转为 <T> 泛型
 *
 * @description: 类型解析 接口
 * @author: cuiweiman
 * @date: 2024/4/3 11:53
 */
public interface PropParser<T> {

    T parse(String value);

}
