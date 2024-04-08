package com.zachary.bifromq.basehookloader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * {@link AccessLevel#PRIVATE} 将构造器权限设为 private。有时使用单例模式，需要将构造器私有化。
 * <p>
 * {@link ServiceLoader#load(Class)} 加载 接口或抽象类 的所有 实现类，
 * 类似于 Spring 的 ApplicationContext, 根据类名获取类实例。
 * 需要提前在 resources/META-INF/services 下配置，配置即 创建 名称为接口路径 的文件, 文件中配置 该接口的实现类。
 * 如 本 Module 的测试
 * <p>
 * {@link Iterator#forEachRemaining(Consumer)} 对集合中剩余的元素进行操作，直到元素完毕或者抛出异常。
 * 剩余的元素: Iterator 每遍历一个元素，迭代器中就会丢失一个元素。
 * 当遍历结束，但 Iterator 中还存在剩余元素时，可以继续使用 forEachRemaining 进行遍历。
 *
 * @description: 根据 接口/抽象类 , 获取 其 所有的 实现类 的内部缓存, 根据 ClassName-Class 格式缓存到 Map 中。
 * @author: cuiweiman
 * @date: 2024/4/3 09:58
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseHookLoader {

    public static <T> Map<String, T> load(Class<T> hookInterface) {
        Map<String, T> loadedFactories = new HashMap<>();
        // Java SPI, 加载 hookInterface 接口的所有 实现类
        ServiceLoader<T> serviceLoader = ServiceLoader.load(hookInterface);
        Iterator<T> iterator = serviceLoader.iterator();
        // Iterator 遍历的一种方式
        iterator.forEachRemaining((impl) -> {
            String className = impl.getClass().getName();
            if (className.isBlank()) {
                // 非法状态异常，不允许 匿名实现类
                throw new IllegalStateException("Anonymous implementation is not allowed");
            }
            log.info("Loaded {} implelation: {}", hookInterface.getSimpleName(), className);
            // 当 className 存在是，返回已存在的 value, 当 className 不存在时，map缓存 (className,impl), 并返回 null
            // 即 Map#putIfAbsent 返回内容 非空，表明 存在 重复的 className
            if (Objects.nonNull(loadedFactories.putIfAbsent(className, impl))) {
                // 接口 hookInterface 的实现类, 其 class name 存在重复
                throw new IllegalStateException("More than one implementations using same name " + className);
            }
        });
        return loadedFactories;
    }


}
