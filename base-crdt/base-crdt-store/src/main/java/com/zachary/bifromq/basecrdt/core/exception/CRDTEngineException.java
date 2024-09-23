package com.zachary.bifromq.basecrdt.core.exception;

/**
 * @description: CRDT 异常处理
 * @author: cuiweiman
 * @date: 2024/4/18 11:55
 */
public class CRDTEngineException extends RuntimeException {

    /**
     * CRDT 引擎关闭异常
     */
    public static final CRDTEngineException CRDT_IS_CLOSE = new CRDTEngineException("CRDT is closed");
    /**
     * CRDT 引擎未找到 异常
     */
    public static final CRDTEngineException CRDT_NOT_FOUND = new CRDTEngineException("CRDT NotFound");


    private CRDTEngineException(String message) {
        super(message);
    }

}
