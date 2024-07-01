package com.zachary.bifromq.baserpc.exception;

/**
 * @description: 请求中止 异常
 * @author: cuiweiman
 * @date: 2024/4/28 16:31
 */
public class RequestAbortException  extends RuntimeException{
    public RequestAbortException(String reason){
        super(reason);
    }
}
