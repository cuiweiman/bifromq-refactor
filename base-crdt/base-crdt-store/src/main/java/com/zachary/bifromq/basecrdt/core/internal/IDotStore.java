package com.zachary.bifromq.basecrdt.core.internal;

import com.zachary.bifromq.basecrdt.proto.Dot;

/**
 * 实现 {@link Iterable}接口，实现对集合中元素的 迭代访问
 *
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 16:45
 */
public interface IDotStore extends Iterable<Dot> {

    /**
     * If in bottom state
     *
     * @return 是否处于底部状态
     */
    boolean isBottom();

}
