package com.zachary.bifromq.baserpc.exception;

/**
 * @description: 瞬时故障异常
 * @author: cuiweiman
 * @date: 2024/4/28 16:31
 */
public class TransientFailureException extends RuntimeException{

    public TransientFailureException(Throwable e){
        super(e);
    }

}
