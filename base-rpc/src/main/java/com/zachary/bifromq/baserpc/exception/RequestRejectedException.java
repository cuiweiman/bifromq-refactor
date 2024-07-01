package com.zachary.bifromq.baserpc.exception;

/**
 * @description: 请求拒绝 异常
 * @author: cuiweiman
 * @date: 2024/4/28 17:43
 */
public class RequestRejectedException extends RuntimeException{
    public RequestRejectedException(Throwable e) {
        super(e);
    }

    public RequestRejectedException(String reason) {
        super(reason);
    }

    public RequestRejectedException(String reason, Throwable e) {
        super(reason, e);
    }
}
