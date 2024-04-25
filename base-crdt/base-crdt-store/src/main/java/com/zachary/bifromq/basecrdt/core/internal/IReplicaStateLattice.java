package com.zachary.bifromq.basecrdt.core.internal;

import com.google.protobuf.ByteString;
import com.zachary.bifromq.basecrdt.proto.Replacement;
import com.zachary.bifromq.basecrdt.proto.StateLattice;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/20 16:58
 */
public interface IReplicaStateLattice {

    interface JoinDiff {

        JoinDiff NO_DIFF = new JoinDiff() {

            @Override
            public Iterable<StateLattice> adds() {
                return Collections.emptyList();
            }

            @Override
            public Iterable<StateLattice> removes() {
                return Collections.emptyList();
            }
        };

        Iterable<StateLattice> adds();

        Iterable<StateLattice> removes();

    }

    int size();

    Duration historyDuration();

    /**
     * The next event which could be used to construct state lattice.
     * 可用于构造状态格 的 下一个事件。
     *
     * @return next event
     */
    long nextEvent();

    /**
     * The state-addition events contributed by the provided replica
     * 副本 的 状态添加事件
     *
     * @return 状态添加事件
     */
    Iterator<StateLattice> lattices();

    /**
     * Join a lattice into current state and return the essential diff made to the previous state, and memoize removed
     * events until exceed history duration
     * <p>
     * 将网格加入 当前状态 并返回与前一个状态的 基本差异，并记住 删除的事件 直到 超过 历史持续时间
     *
     * @param state 节点状态
     * @return 状态差异
     */
    JoinDiff join(Iterable<Replacement> state);


    /**
     * Get a lattice containing only the delta part by comparing the provided index with local index. The third argument
     * controls the maximum events included in the delta
     * <p>
     * 通过 将提供的索引 与 局部索引 进行比较，得到 仅包含 增量部分的格子。 第三个参数 控制 增量中 包含的 最大事件数
     *
     * @param coveredLatticeIndex the index of covered lattices to exclude from the delta
     *                            要从增量中 排除的 覆盖格子的 索引
     * @param coveredHistoryIndex the index of covered history to exclude from the delta
     *                            要从增量中 排除的 覆盖历史记录的 索引
     * @param maxEvents           the maximum events a state fragment could contain
     *                            状态片段 可以包含的 最大事件数
     * @return if no delta found returns Optional.empty() 如果没有找到增量 返回空
     */
    Optional<Iterable<Replacement>> delta(
            Map<ByteString, NavigableMap<Long, Long>> coveredLatticeIndex,
            Map<ByteString, NavigableMap<Long, Long>> coveredHistoryIndex,
            int maxEvents);


    /**
     * Get the summarized history of events contributing to current state
     * 获取对 当前状态 有贡献的事件 的 汇总历史记录
     *
     * @return 汇总历史记录
     */
    Map<ByteString, NavigableMap<Long, Long>> latticeIndex();

    /**
     * Get the summarized history of events which were contributing to local state
     * 获取 对 当前状态 做出贡献的 事件的 摘要历史
     *
     * @return 摘要历史
     */
    Map<ByteString, NavigableMap<Long, Long>> historyIndex();

    /**
     * Compact to free some memory occupied by obsolete history
     * 通过 压缩 来 释放 一些 被过时历史 占用的内存
     *
     * @return if next compaction should be scheduled 是否应安排下一次压缩
     */
    boolean compact();


}
