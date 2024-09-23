package com.zachary.bifromq.basecrdt.core.util;

import com.google.protobuf.ByteString;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiFunction;

/**
 * {@link NavigableMap} 按照 key 的顺序排序，根据键值的顺序 进行 范围查找 和 遍历操作
 * {@link ConcurrentSkipListMap} 实现了一个基于跳表（Skip List）算法的并发有序映射，保持键值对的排序顺序，并且多个线程可能同时读写映射。
 * 提供了高效的查找、插入和删除操作，非常适合于需要 频繁 更新 的场景。
 * 线程安全，操作时间复杂度 具有 对数级别，大数据集下也能维持高效性能。
 * <p>
 * {@link Map#compute(Object, BiFunction)}
 * 1. 不论当前 key 是否存在，使用 key 和 oldValue 计算返回一个 newValue
 * 2. 当 newValue 为空时，若key存在则移除 key 缓存；若key不存在则不做任何处理。
 * 3. 当 newValue 不为空时，不论当前 key 是否存在，添加或更新 key-newValue 并返回 newValue。
 *
 * @description: 格子索引工具、格点索引工具
 * @author: cuiweiman
 * @date: 2024/4/11 22:15
 */
public class LatticeIndexUtil {

    public static void remember(Map<ByteString, NavigableMap<Long, Long>> historyMap,
                                ByteString replicaId, long ver) {
        historyMap.compute(replicaId, new BiFunction<ByteString, NavigableMap<Long, Long>, NavigableMap<Long, Long>>() {
            @Override
            public NavigableMap<Long, Long> apply(ByteString key, NavigableMap<Long, Long> history) {
                // 判断 historyMap 中 oldValue 是否存在
                if (Objects.isNull(history)) {
                    history = new ConcurrentSkipListMap<>();
                }
                // 判断 oldValue 即 history 变量 中是否包含 ver，如果已包含，则返回 oldValue，保持 key-oldValue
                if (history.containsKey(ver)) {
                    return history;
                }
                // 否则，将 ver 存入 history，再返回 oldValue
                remember(history, ver);
                return history;
            }
        });
    }


    public static void remember(Map<ByteString, NavigableMap<Long, Long>> historyMap,
                                ByteString replicaId, long startVer, long endVer) {
        historyMap.compute(replicaId, (k, history) -> {
            if (Objects.isNull(history)) {
                history = new ConcurrentSkipListMap<>();
            }
            remember(history, startVer, endVer);
            return history;
        });
    }


    public static void remember(NavigableMap<Long, Long> ranges, long ver) {
        remember(ranges, ver, ver);
    }


    public static void remember(NavigableMap<Long, Long> ranges, long startVer, long endVer) {
        // 返回 小于 或 等于 给定键 的 最大键，若不存在则返回 null
        Long startKey = ranges.floorKey(startVer);
        if (Objects.isNull(startKey)) {
            startKey = startVer;
        } else {
            if (ranges.get(startKey) + 1 < startVer) {
                startKey = startVer;
            }
        }

        Long endKey = ranges.floorKey(endVer);
        if (Objects.isNull(endKey)) {
            endKey = ranges.getOrDefault(endVer + 1, endVer);
        } else {
            endKey = Math.max(endVer, ranges.get(endKey));
            if (ranges.containsKey(endKey + 1)) {
                endKey = ranges.get(endKey + 1);
            }
        }
        // 清理 [startKey,endKey] 之间的所有缓存
        ranges.subMap(startKey, true, endKey, true).clear();
        // 重新缓存 [startKey,endKey]
        ranges.put(startKey, endKey);
    }


}
