package com.zachary.bifromq.baseenv;

import com.zachary.bifromq.basehookloader.BaseHookLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link AccessLevel#PACKAGE} 构造函数 的 访问级别 为 包级别（同一个包内可见，其他包不可见）。
 * {@link AccessLevel#PRIVATE} 将构造器权限设为 private。有时使用单例模式，需要将构造器私有化。
 * {@link AccessLevel#PROTECTED} 将构造器权限设为 protected。
 * {@link AccessLevel#MODULE} 构造器 的 访问级别 为 模块级别 同一个 module 可见。
 * {@link AccessLevel#PUBLIC} 将构造器权限设为 public。
 *
 * @description: {@link IEnvProvider} 实现类
 * @author: cuiweiman
 * @date: 2024/4/3 11:06
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class EnvProvider implements IEnvProvider {

    public static final IEnvProvider INSTANCE;

    /*
     * 构造 EnvProvider 的 INSTANCE 唯一实例，类似于 Spring 中的 Bean 含义
     */
    static {
        // 加载 IEnvProvider 接口的 所有实现类
        Map<String, IEnvProvider> envProviderMap = BaseHookLoader.load(IEnvProvider.class);
        if (envProviderMap.isEmpty()) {
            // 实现类 为空
            log.info("No custom env provider found, fallback to default behavior");
            INSTANCE = new EnvProvider();
        } else {
            // 只获取 首个 IEnvProvider 的实现类，只允许一个 实现类
            Map.Entry<String, IEnvProvider> firstFound = envProviderMap.entrySet().stream().findFirst().get();
            log.info("Custom env provider is loaded: {}", firstFound.getKey());
            INSTANCE = firstFound.getValue();
        }
    }

    @Override
    public int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public ThreadFactory newThreadFactory(String name, boolean daemon, int priority) {
        return new ThreadFactory() {
            private final AtomicInteger poolNumber = new AtomicInteger();

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                int s = poolNumber.getAndIncrement();
                thread.setName(s > 0 ? name + "-" + s : name);
                thread.setDaemon(daemon);
                thread.setPriority(priority);
                return thread;
            }
        };
    }
}
