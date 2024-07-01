package com.zachary.bifromq.baserpc.exception;

/**
 * @description: 服务 无法访问 异常
 * @author: cuiweiman
 * @date: 2024/4/28 17:39
 */
public class ServerUnreachableException extends RuntimeException{

    public ServerUnreachableException(Throwable e) {
        super(e);
    }

}
