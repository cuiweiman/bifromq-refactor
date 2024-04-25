package com.zachary.bifromq.sysprops.parser.parser;

/**
 * @description: 字符串 类型转换 异常
 * @author: cuiweiman
 * @date: 2024/4/9 09:29
 */
public class SysPropParseException extends RuntimeException {
    public SysPropParseException(String message) {
        super(message);
    }
}
