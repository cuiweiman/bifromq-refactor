package com.zachary.bifromq.metrics.basetest;

import java.lang.ref.Cleaner;
import java.util.concurrent.TimeUnit;

/**
 * Object#finalize() 方法的问题：
 * 1. 执行对象的销毁操作，帮助GC回收资源，比如关闭打开的文件或者网元资源，删除临时文件
 * 2.1 Jvm 会给每个实现了 Object#finalize() 方法的实例 创建一个监听，这个监听称为 Finalizer
 * 2.2 每次调用对象的 finalize 方法时，JVM会创建一个 java.lang.ref.Finalizer 对象，这个Finalizer对象会持有这个对象的引用
 * 2.3 这些对象被 Finalizer 监听对象引用了，当对象数量较多时，就会导致Eden区空间满了，经历多次youngGC后可能对象就进入到老年代了
 * <p>
 * 2.4 大部分场景 finalizer 线程清理 finalizer队列 是比较快的，但是一旦finalize方法里执行一些耗时的操作，可能导致内存无法及时释放进而导致内存溢出的错误
 * 2.5 在实际场景不推荐 finalize 方法
 * <p>
 * Cleaner 类相比 Object#finalize()（jdk9之前的回收方法,官方不推荐使用） 和 PhantomReference(虚引用) 的优势:
 * 类型	            自动化清理	易用性	                        安全性	性能影响
 * Cleaner	        是	        开箱即用	                        更强	    较小
 * Finalizer	    是	        开箱即用	                        较弱	    较大
 * PhantomReference 否	        需要与引用队列相结合，额外代码工作量	较弱	    较小
 * <p>
 * Cleaner 类和手动调用Close方法的区别:
 * 操作	                执行时机	            确定性	            是否自动化清理	资源泄漏风险
 * Cleaner	            对象被回收时隐式调用	执行时机无法控制	    是	            低（只要对象不再可达，就会自动清理）
 * 调用资源的Close方法	代码显示调用	        只要调用，一定执行	    否	            高（如果忘记调用 close 方法，可能会导致资源泄露）
 * <p>
 * Cleaner 潜在的问题
 * 1. 每注册一个 Cleaner 类，就会新开一条线程 用于监听目标对象 是否已经进入到 引用队列。直到 目标对象 被回收后，新线程 才结束。
 * 2. Cleaner 回收时间点 无法控制。
 * 3. 不能替换所有的资源释放，必要时还是需要显式执行 资源的 Close 方法。
 * 4. 无法控制传入的回收执行逻辑，可能导致性能问题
 *
 * @description: jdk9+ 资源回收方法的使用
 * @author: cuiweiman
 * @date: 2024/7/8 14:15
 */

public class CleanerTest {

    static class Animal implements Runnable {

        public Animal() {
            System.out.println("【构造】动物出生");
        }

        @Override
        public void run() {
            System.out.println("【回收】动物死亡, 资源回收");
        }
    }

    // 创建一个 清理器
    private static final Cleaner cleaner = Cleaner.create();
    private Animal animal;
    private Cleaner.Cleanable cleanable;

    public CleanerTest() {
        // 创建新对象
        this.animal = new Animal();
        // 注册 清理器 监听对象
        this.cleanable = cleaner.register(this, this.animal);
    }

    public void logical() {
        System.out.println("执行业务逻辑方法");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        CleanerTest demo = new CleanerTest();
        demo.logical();
        demo.cleanable.clean();
    }
}
