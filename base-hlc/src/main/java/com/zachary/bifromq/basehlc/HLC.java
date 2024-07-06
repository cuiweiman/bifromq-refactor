package com.zachary.bifromq.basehlc;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * ** 物理时钟:
 * - 基本概念
 * 计算机的系统时钟是一个频率精确和稳定的脉冲信号发生器，也称为"物理时钟"。
 * 对于传统的单机数据库，物理时钟的大小可以用来准确描述事件在当前物理机发生的先后关系。
 * - 存在问题
 * 但在分布式系统中，各个节点分布在不同的物理位置，每个节点都有自己的物理时钟，
 * 即使采用 NTP 对各个设备的物理时钟进行同步，也有可能发生毫秒级的偏移。
 * 所以物理时钟 不能作为 分布式系统内 在不同节点上 并发执行的事务 的 排序依据。
 * <p>
 * ** 逻辑时钟: 解决物理时钟无法解决的分布式系统中两个事件先后顺序的问题。
 * - 基本概念
 * 通过 happened-before 关系确定事件的逻辑时钟，从而确定事件的偏序关系。
 * 在分布式场景下，不同机器的时间可能存在不一致，没办法对跨节点的事件确定的先后关系。
 * <p>
 * 逻辑时钟关键点在于定义事件的先后关系，将 事件a 发生在 b 之前定义为 a→b。以下三种情况都满足 a→b：
 * 1. a和b是同一个进程内的事件，a发生在b之前，则a→b。
 * 2. a和b在不同的进程中，a是发送进程内的 发送事件，b是同一消息接收进程内的 接收事件，则a→b (先发送，再接收)。
 * 3. 如果a→b且b→c，则a→c (逻辑推导)。
 * 4. 如果a和b没有先后关系，则两个事件是并发的，记作a||b。
 * - 存在问题
 * 逻辑时钟算法 还不能完全解决 两个事件 真正的 先后关系，只能对进程间的事件赋予逻辑意义的顺序。
 * <p>
 * *************** 混合逻辑时钟 ***************
 * - 基本概念
 * 解决逻辑时钟和物理时钟之间的差距，支持因果关系，同时又有物理时钟直观的特点。HLC是将物理时钟和逻辑时钟结合起来的一种算法。
 * HLC 由两部分值组成，即 物理时钟值 和 逻辑时钟值，可以表示为一个二元组 [l,c]，其中l代表物理时钟值，c代表逻辑时钟值，例如[10,1]。
 * <a href="https://blog.csdn.net/Post_Yuan/article/details/124571980">分布式一致性问题之混合逻辑时钟</a>
 *
 * <p>
 * 1. 满足逻辑时钟的因果一致性happened-before。
 * 2. 尽可能接近物理时钟PT，当物理时钟推进时，逻辑时钟部分被置零。
 * 3. 记录时间的因果关系，保证和物理时钟的偏差是 bounded (范围内的) 。
 * 4. 消除中心节点，用本地的物理时间加上逻辑时间，为具备数据库定义的因果关系的事务排序。
 * 5. 可以替代wall time使用。
 *
 * <p>
 * ---- 分布式系统 不能使用 NTP 网络时间协议:
 * NTP 单个计算机 或 网络内的多台计算机 可以使用NTP服务器来校准其系统时钟，保持与统一时间（例如，UTC，协调世界时）的一致性，减少时间误差。
 * 但是，在分布式系统中，仅仅依赖NTP来同步时间可能不足够，因为：
 * 1. 分布式系统 跨越广泛的 地理位置，网络延迟和变化可以造成同步的不精确。（多数据中心）
 * 2. NTP 无法确保绝对的时钟同步一致性，通常会有毫秒级别的误差，这对于需要高精度时钟同步的分布式系统可能是不可接受的。
 * 3. 分布式系统中的某些算法和应用可能需要比NTP更强的时间协调机制，比如逻辑时钟或向量时钟，它们能够提供系统事件的顺序一致性而不仅仅是时间同步。
 * <p>
 * 复杂的分布式系统可能会采用额外的协议和技术，如真实时间时钟（RTCs）、时钟同步算法（比如Google的TrueTime API），以及各种容错机制来更准确地同步各个节点的时钟。
 *
 * @description: 分布式一致性问题: 时钟同步 之 HLC 混合逻辑时钟
 * @author: cuiweiman
 * @date: 2024/3/28 17:53
 */
public class HLC {

    /**
     * 饿汉式 初始化实例
     */
    public static final HLC INST = new HLC();

    /**
     * 65535
     */
    private static final long CAUSAL_MASK = 0x00_00_00_00_00_00_FF_FFL;

    /**
     * VarHandle 是JDK9+引入的对标 {@Unsafe} 类的，用于在Java中进行原子性和可变性访问。
     * 它提供了一种机制，可以在不使用锁的情况下对共享数据进行原子性访问。
     * VarHandle 类可以用于访问对象字段，数组元素和静态字段。
     * 它提供了一组方法，可以执行原子性读取，写入和更新操作。
     * VarHandle 类还提供了一些方法，可以执行非原子性的访问，例如读取和写入操作，这些操作不需要保证线程安全性。
     * <p>
     * 静态代码块中，将 private long hlc 属性的反射信息 赋给了 CAE 变量。
     * <p>
     * 为了使用 {@link VarHandle#compareAndExchange(Object...)} 方法，确保 多线程 更新时 的 线程安全
     */
    private static final VarHandle CAE;

    static {
        try {
            // 获取 HLC.class 类信息
            MethodHandles.Lookup l = MethodHandles.privateLookupIn(HLC.class, MethodHandles.lookup());
            // 获取 HLC 类的 hlc 私有属性，私有属性 属于 HLC.class，属性名为 hlc，类型为 long
            CAE = l.findVarHandle(HLC.class, "hlc", long.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Assume cache line size is 64bytes, and first 16 bytes are reserved for object header
     * 假设缓存行大小为 64 字节，前 16 字节保留给对象头
     * <p>
     * 解决 CPU缓存行的伪共享问题，一个 CacheLine 缓存行有 64 字节，可以存储 8 个8字节的Long类型。
     * 空间换时间，将一个 变量类型 放大到 64 字节
     */
    private long p3;
    private long p4;
    private long p5;
    private long p6;
    private long p7;
    private volatile long hlc = 0L;

    private long p9;
    private long p10;
    private long p11;
    private long p12;
    private long p13;
    private long p14;
    private long p15;

    private HLC() {
    }

    /**
     * 获取一个 混合逻辑时钟
     *
     * @return 时间戳 + 自增偏移量
     */
    public long get() {
        long now = hlc;
        // hlc 初始化为 0, l 右移 16 位 依然是 0
        long l = logical(now);
        // hlc 初始为 0, hlc & CAUSAL_MASK 65535 = 0
        long c = causal(now);
        // 初始时 updateL = 当前时间戳
        long updateL = Math.max(l, System.currentTimeMillis());
        if (updateL == l) {
            // 同一个时间戳内，自动偏移量
            c++;
        } else {
            // 不同的时间戳，不需要偏移量
            c = 0;
        }
        // 将 updateL 左移 16位, 加上 变量c, 类似于雪花算法的拼接
        // 若 在同一个时间戳内, 则 c 自增1,
        long newHLC = toTimestamp(updateL, c);
        // cas 操作 更新 hlc 值
        set(now, newHLC);
        return newHLC;
    }

    /**
     * 更新 hlc 值
     *
     * @param otherHLC
     * @return
     */
    public long update(long otherHLC) {
        long now = hlc;
        long l = logical(now);
        long c = causal(now);
        long otherL = logical(otherHLC);
        long otherC = causal(otherHLC);
        long updateL = Math.max(l, Math.max(otherL, System.currentTimeMillis()));
        if (updateL == l && otherL == l) {
            c = Math.max(c, otherC) + 1;
        } else if (updateL == l) {
            c++;
        } else if (otherL == l) {
            c = otherC + 1;
        } else {
            c = 0;
        }
        long newHLC = toTimestamp(updateL, c);
        set(now, newHLC);
        return newHLC;
    }

    public long getPhysical() {
        return getPhysical(get());
    }

    public long getPhysical(long hlc) {
        return logical(hlc);
    }

    private long toTimestamp(long l, long c) {
        return (l << 16) + c;
    }

    /**
     * 将 hlc 右移 16 位
     * 因为 hlc 旧值在计算时，向左偏移过，
     * 重新计算 hlc 时，需要 右移 恢复 到 原值 再进行比较
     */
    private long logical(long hlc) {
        return hlc >> 16;
    }

    /**
     * 计算 hlc & 65535
     */
    private long causal(long hlc) {
        return hlc & CAUSAL_MASK;
    }

    /**
     * CAS 比较和交换 hlc 变量的 值
     */
    private void set(long now, long newHLC) {
        long expected = now;
        long witness;
        do {
            // 比较和交换: CAE 代表的 hlc 变量，先与 expected 期望值比较，若比较相等，则与 newHLC 交换值
            // the witness value, which will be the same as the expected value if successful
            // 若 执行成功，那么 返回值 witness 将 等于 expected 值
            witness = (long) CAE.compareAndExchange(this, expected, newHLC);
            if (witness == expected || witness >= newHLC) {
                break;
            } else {
                // 更新失败，修改 excepted 期望值，重新 CAS 更新 hlc 值
                expected = witness;
            }
        } while (true);
    }
}
