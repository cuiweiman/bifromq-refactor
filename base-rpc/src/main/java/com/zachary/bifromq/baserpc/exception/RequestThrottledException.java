package com.zachary.bifromq.baserpc.exception;

/**
 * @description: 请求 限流 异常
 * @author: cuiweiman
 * @date: 2024/4/28 17:40
 */
public class RequestThrottledException extends RuntimeException{
    public RequestThrottledException(Throwable e){
        super(e);
    }
}
