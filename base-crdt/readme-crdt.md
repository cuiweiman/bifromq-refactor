- [什么是 CRDT?](https://www.zhihu.com/question/507425610/answer/2299709925)
- [彻底搞懵 CRDT](https://zhuanlan.zhihu.com/p/676560273)

[TOC]


> CRDT 分布式系统一致性实现方案
> https://crdt.tech/papers.html
> 主要是 state-based crdt 的拓展 delta-state crdt，https://arxiv.org/abs/1603.01529
> 模块中涉及较多 抽象代数里的概念

# 什么是 CRDT

> CRDT (conflict-free replicated data type) 无冲突复制数据类型，是一种数据类型，或者说 **一种数据一致性的方案**

乐观锁复制策略，算法能够自动地处理 数据不一致 的问题。

多副本系统中，一个副本和另一个副本通常是不同的，当其他副本数据同步过来时，有可能会出现冲突（不一致）的地方，比如两个副本同时删除和新增一个元素。

这时需要 CRDT 算法使用特定的策略去自动处理，而不是像 git merge 那样去手动处理冲突。

同一时刻不同副本的状态可能不同，但同步后它们能最终收敛（converge），达到相同的状态（最终一致性）。

CRDT 一种可以在网络中的多台计算机上复制的数据结构，副本可以独立和并行地更新，不需要在副本之间进行协调，并保证不会有冲突发生。能够根据一定的规则自动合并，解决数据冲突，达到强最终一致的效果。

CRDT 常被用在协作软件上，例如多个用户需要共同编辑/读取共享的文档、数据库或状态的场景。在数据库软件，文本编辑软件，聊天软件
等都可以用到它。

## CAP 理论

- C 一致性 consistency
- A 可用性 availability
- P 分区容错性 partition-tolerance

单副本可以满足 强一致性，但单副本不具备容错性；
因此分布式系统一般会 添加 数据备份节点，即多副本。

多副本 的情形下，强一致性满足的情况下，难以保证 可用性，
为了较好的均衡 分布式系统的 强一致性 和 可用性，通常做法是 选择一个优先保证。

- Redis 支持 AP
    - Redis 集群通过主从或者集群模式，可以较好的维持 可用性。
    - 由于 Redis 多副本是通过 Gossip 协议进行节点数据同步的，只能满足 节点数据 的 最终一致性。
- Zookeeper 支持 CP 理论
    - ZK 使用 **ZAB原子广播协议**， 在接收到 数据请求时，需要 半数以上的节点都同步完成后，才进行请求响应，较好的保证了一致性；
    - 但是在 ZK 节点选举 期间的 1~2 分钟内，无法对外提供服务，不满足可用性；
- CRDT 是 满足 多副本 一致性的 方案

## CRDT 数据同步方式

---

- Operation-based 基于操作的 CRDT

副本数据进行同步时，只发送 **新增的本地操作 operation**；
其它副本接收到这个 operation 后会将其应用到自己的状态上，operation 需要满足 交换律(commutative)。

优点：
待同步、需传输的数据量少。

交换律，指的是交换运算顺序，最后的结果不变；如 a+b==b+a;

> 本地操作 operation 满足交换律，是因为网络不可知；  
> 假设 a、b 副本同时发送 operation，因网络问题或其他问题，其它副本接收到数据同步的请求可能是 先a后b，也可能是 先b后a，  
> 交换律 保证是 即即使先后顺序不同，最终结果需要一致。

---

- State-based 基于状态的 CRDT

副本数据同步时，将 **完整的本地状态 State** 全部发送；
其它副本需要支持 合并 merge 操作，merge 方法需要满足 交换律、分配率、幂等性。

## 简单的 CRDT 模型 介绍

### AWSet (Add-Wins set)

新增 优先 删除 的集合数据结构

> 刚开始时，副本 A 和 副本 B 的状态是一致的，有一个 a 在集合中；  
> 副本 A 删除了 a，然后再新增 a；  
> 副本 B 删除了 a；  
> 副本 A 的新增 a 和 副本 B 的删除 a， 同时 在两个节点 发生了；  
> 此时会 选择新增，忽略删除，最后两个副本的状态还是 a 在集合中。

### RWSet (Remove-Wins set)

类似于 AWSet，不同的是 删除 优先 新增 的集合数据结构。

### LWW (Last-Writer-wins)

最后写入者 优先

> 所有的操作事件，都具备一个先后顺序，或时间戳元素；  
> 副本 通过对比操作的时间戳，若 同步过来的 操作事件 时间戳 大于当前状态 的 时间戳，就覆盖旧状态。  
> 若 同步事件的时间戳，小于 当前旧状态，则忽略该 操作。

### 2P-Set (Two-Phase Set)

副本接收到 新增操作 和 删除操作，会维护 这两个集合；  
模型的最终状态是: 两个集合 的 差集，

### YATA (Y.js 的 Yet Another Transformation Approach)

假设我们有值为 "ABCD" 的字符串。YATA 模型会将其拆分成一个个字符，加上元数据，然后按顺序首尾相连组成一个双链表。

```json
// 大概这样子
{
  // 客户端 ID + clock ID
  "id": "2:0",
  "val": "B",
  // 当前节点的左节点
  "left": "A",
  // 右节点
  "right": "C",
  // 节点创建时的左节点
  "origin": "A",
  // 节点创建时的右节点
  "rightOrigin": "C"
}
```

操作有 “插入” 和 “删除”。

假设 当前副本 在 AB 之间插入 E，此时没有发送同步，然后收到其他副本传过来的 F，也要插入到 AB 之间。

此时 E 和 F 是冲突的，YATA 会对唯一的 id（某种意义上的时间戳）使用特定的规则来决定先后顺序。

至于 删除操作，因为插入操作需要找到在左右节点的位置，所以节点即使被删除了也是不能从双链表中移出的。

对此，YATA 选择使用 **墓碑机制**。YATA 将对应节点**标记为删除**（item.deleted 设置为 true），并将节点记录到删除集合 DeleteSet
里。


> Y.js 通过一系列手段（比如将多个节点合并为一个大节点），将性能优化到足够面对大多数场景，证明了用 CRDT
> 是做协同编辑的是不用担心性能问题的，如果有，一定是你没优化好。

