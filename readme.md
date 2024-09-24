
记录平时接触少，需要单独学习的工具和方案

- RxJava 是响应式编程(Reactive Extensions)
- awaitility 接口自动化工具，异步测试工具
- TestNG Java 自动化测试框架
- netty-tcnative-boringssl-static 服务端定义 Netty 的 OpenSSL
- netty-transport-native-epoll 不同系统选择最佳性能的 netty 工具
- 度量库：micrometer
- commons-cli 命令行参数API库
- jackson-dataformat-yaml 读取Yaml文件配置并反序列化成类对象
-  RocksDB  K-V 数据库， JNI API依赖
- pf4j 开源轻量级的插件框架
- reflections Java 反射框架
- jmh-core 精确到微秒级别的性能测试框架
- CRDT 分布式系统一致性实现方案
- base-hlc  混合逻辑时钟

## 编译打包
```bash
cd bifromq
mvn wrapper:wrapper
./mvnw -U clean package
```
构建输出是两个 tar.gz 和一个 zip 文件，位于 /build/build-bifromq-starters/target/

- bifromq-xxx-all.tar.gz：适用于 Linux 和 macOS 的 standalone 集群部署 tar.gz 包
- bifromq-xxx-standalone.tar.gz：适用于 Linux 和 macOS 的 standalone 部署 tar.gz 包
- bifromq-xxx-windows-standalone.zip：适用于 Windows 的 standalone 部署的 zip 包
