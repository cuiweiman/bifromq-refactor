package com.zachary.bifromq.baserpc.exception;

/**
 * @description: 未找到 服务器 异常
 * @author: cuiweiman
 * @date: 2024/4/28 17:39
 */
public class ServerNotFoundException extends RuntimeException{

    public ServerNotFoundException(String reason){
        super(reason);
    }

    public ServerNotFoundException(Throwable e){
        super(e);
    }

    public ServerNotFoundException(String reason,Throwable e){
        super(reason,e);
    }

}
