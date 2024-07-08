- [micrometer 官方文档](https://docs.micrometer.io/micrometer/reference/)
- [服务监控 | 万字长文详解Micrometer](https://www.cnblogs.com/cjsblog/p/11556029.html)
- [Spring Boot 使用 Micrometer 集成 Prometheus 监控 Java 应用性能](https://blog.csdn.net/aixiaoyang168/article/details/100866159)
- [springboot如何集成Prometheus如何暴露Histogram来获取P99等监控指标](https://blog.csdn.net/lp19861126/article/details/106309546)

## Registry

Registry 代表 度量 的注册器，每种监控软件都有对应的 MeterRegistry 实现。

如果还没有首选的监控系统(Prometheus 或 Grafana )，则可以使用简单的注册表开始使用 度量

- SimpleMeterRegistry: 简单注册器
    ```java
    // 简单 注册表, 可以将 指标 注册到 注册表中, 纳管指标信息
    MeterRegistry registry=new SimpleMeterRegistry();
    ```
- CompositeMeterRegistry: 复合注册器, 在维护 度量 信息的同时，
  可以添加 其它监控注册器，同时将指标信息发布到多个 监控注册器中。
  ```java
  CompositeMeterRegistry composite = new CompositeMeterRegistry();
  Counter compositeCounter = composite.counter("counter");
  // 在组合中存在注册表之前，增量是 NOOP 的。此时计数器的计数仍为 0
  compositeCounter.increment();
  SimpleMeterRegistry simple = new SimpleMeterRegistry();
  // 名为 counter 的计数器 注册 到 简单注册表
  composite.add(simple);
  // 简单注册表计数器以及组合中任何其他注册表的计数器都会递增。
  compositeCounter.increment();
  ```

- GlobalRegistry: 静态全局注册表, 是一个复合注册表
  ```java
  class MyComponent {
    // 尽量 使用 变量 维护 度量
    Counter featureCounter = Metrics.counter("feature", "region", "test");
    void feature() {
        featureCounter.increment();
    }
    // 不推荐 使用 如下方式 统计 度量
    void feature2(String type) {
        Metrics.counter("feature.2", "type", type).increment();
    }
  }
  class MyApplication {
    void start() {
     // wire your monitoring system to global static state
     // 通过 Metrics 注册的两个 度量，都绑定到了 简单注册器
      Metrics.addRegistry(new SimpleMeterRegistry());
    }
  }
  ```

## Meter

Meter表示一个指标，新增业务指标时首先需要确定它的类型，Micrometer支持下面几种类型

- Counter：计数器，用于保存单调递增型的数据，例如站点的访问次数，JVM的GC次数等；不能为负值，也不支持减少，但可以重置为0
- Gauge：仪表盘，用于存储有着起伏特征的数据，例如堆内存的大小，注意能用Counter记录的指标不要用Guage
- Timer：计时器，记录事件的次数和总时间，例如HTTP请求消耗的时间，Timer同时也会包含次数统计，不需要再使用Counter
- Distribution Summaries： 用于跟踪事件的分布，与Timer结构类似，但值的单位可以是自定义的任意单位

确定类型后要为 指标 取一个合适的 名称 并 添加标签(Tag)，名称 最好由 小写字母 和 点 组成例如http.request.count，标签是
key-value 格式的数据，key-value 都是字符串，key 最好也只包含小写字母和点，一个指标可以包含多个Tag，最终的指标形式如下

```bash
# meter 名称为 cpu.usage,
# tag-key = host, tag-value = ip
# 名称 和 标签 唯一确定了一个指标
# 表示示两台主机的cpu利用率
# host 值不同就是两个不同的指标
cpu.usage {"host"="192.168.3.1"}
cpu.usage {"host"="192.168.3.2"}

```












