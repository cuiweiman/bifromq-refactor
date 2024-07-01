package com.zachary.bifromq.baserpc.exception;

/**
 * @description: 服务不可用 异常
 * @author: cuiweiman
 * @date: 2024/4/28 16:32
 */
public class ServiceUnavailableException extends RuntimeException{

    public ServiceUnavailableException(String reason){
        super(reason);
    }

    public ServiceUnavailableException(Throwable e){
        super(e);
    }

    public ServiceUnavailableException(String reason,Throwable e){
        super(reason,e);
    }

}
