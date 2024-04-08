package com.zachary.bifromq.baseenv;

import org.testng.annotations.Test;

import java.util.concurrent.ThreadFactory;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * {@link EnvProvider} 访问级别限制原因，无法直接单元测试
 *
 * @description: {@link TestEnvProvider} 测试类
 * @author: cuiweiman
 * @date: 2024/4/3 11:19
 */
public class EnvProviderTest {

    @Test
    public void testDefaultProvider() {
        IEnvProvider envProvider = new EnvProvider();
        ThreadFactory threadFactory = envProvider.newThreadFactory("Test");
        Thread t = threadFactory.newThread(() -> {
        });
        assertFalse(t.isDaemon());
        assertEquals(t.getPriority(), Thread.NORM_PRIORITY);
        assertEquals(t.getName(), "Test");
        Thread t1 = threadFactory.newThread(() -> {
        });
        assertEquals(t1.getName(), "Test-1");
    }

    /**
     * {@link TestEnvProvider#availableProcessors}
     */
    @Test
    public void customProvider() {
        IEnvProvider envProvider = EnvProvider.INSTANCE;
        assertEquals(envProvider.availableProcessors(), 1);
        ThreadFactory threadFactory = envProvider.newThreadFactory("test");
        Thread thread = threadFactory.newThread(() -> {
        });
        assertFalse(thread.isDaemon());
        assertEquals(thread.getName(), "test");
        assertEquals(thread.getPriority(), Thread.NORM_PRIORITY);
    }

}
