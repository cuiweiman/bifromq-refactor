package com.zachary.bifromq.basecrdt.core.api.operation;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.core.api.ICRDTOperation;
import lombok.ToString;

/**
 * 一个 添加获胜优化的观察删除集，允许添加和删除
 *
 * @description: An add-wins optimized observed-remove set that allows adds and removes
 * @author: cuiweiman
 * @date: 2024/4/20 17:38
 */
@ToString
public final class AWORSetOperation implements ICRDTOperation {

    public enum Type {
        /**
         *  AWOR 的 操作类型 添加、删除、清理
         */
        Add, Remove, Clear
    }

    /**
     * 饿汉式 初始化 静态常量 CLEAR
     */
    private static final AWORSetOperation CLEAR = new AWORSetOperation(Type.Clear, ByteString.EMPTY);

    public final Type type;
    public final ByteString element;

    private AWORSetOperation(Type type, ByteString element) {
        this.type = type;
        this.element = element;
    }

    /**
     * 添加操作
     *
     * @param e ByteString
     * @return AWORSetOperation
     */
    public static AWORSetOperation add(ByteString e) {
        return new AWORSetOperation(Type.Add, e);
    }

    /**
     * 删除操作
     *
     * @param e ByteString
     * @return AWORSetOperation
     */
    public static AWORSetOperation remove(ByteString e) {
        return new AWORSetOperation(Type.Remove, e);
    }

    /**
     * 清理操作
     *
     * @return AWORSetOperation
     */
    public static AWORSetOperation clear() {
        return CLEAR;
    }


}
