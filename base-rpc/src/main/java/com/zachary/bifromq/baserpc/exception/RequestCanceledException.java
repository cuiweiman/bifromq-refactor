package com.zachary.bifromq.baserpc.exception;

/**
 * @description: 请求取消异常
 * @author: cuiweiman
 * @date: 2024/4/28 17:44
 */
public class RequestCanceledException extends RuntimeException {
    public RequestCanceledException(Throwable e) {
        super(e);
    }
}
