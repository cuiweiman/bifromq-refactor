package com.zachary.bifromq.basecluster.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/4/24 10:52
 */
public class RandomUtils {
    public static <T> List<T> uniqueRandomPickAtMost(List<T> list, int max, Predicate<T> predicate) {
        int n = list.size();
        if (n <= max) {
            return list.stream().filter(predicate).toList();
        } else {
            // n == list.size()  >  max
            HashMap<Integer, Integer> hash = new HashMap<>(2 * max);
            int[] offsets = new int[max];
            for (int i = 0; i < max; i++) {
                // 获取 n 以内的 随机数
                // Random 类为 全局 提供随机数生成，使用 ThreadLocalRandom 一个线程获得的随机数不受另一个线程的影响
                // 可以与当前线程隔离，简单地避免对 Random 对象的任何并发访问，在多线程环境中实现了更好的性能
                int j = i + ThreadLocalRandom.current().nextInt(n - i);
                offsets[i] = (hash.containsKey(j) ? hash.remove(j) : j);
                if (j > i) {
                    hash.put(j, (hash.containsKey(i) ? hash.remove(i) : i));
                }
            }
            List<T> randSelected = new ArrayList<>();
            for (int i : offsets) {
                T peer = list.get(i);
                if (predicate.test(peer)) {
                    randSelected.add(peer);
                }
            }
            return randSelected;
        }
    }
}
