package com.zachary.bifromq.basehlc;

import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.time.Instant;

/**
 * @description:
 * @author: cuiweiman
 * @date: 2024/7/1 15:09
 */
public class VarHandlerTest {

    private volatile long test = 1719825477847L;

    private static final VarHandle CAE;

    static {
        try {
            // 私有变量 寻找器
            MethodHandles.Lookup privateLookupIn = MethodHandles.privateLookupIn(VarHandlerTest.class, MethodHandles.lookup());
            CAE = privateLookupIn.findVarHandle(VarHandlerTest.class, "test", long.class);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testVarHandlerCae() {
        // long excepted = 1719825477847L;
        long excepted = 1719825477847L - 1;
        long newVal = Instant.now().toEpochMilli();

        long comparedAndExchange = (long) CAE.compareAndExchange(this, excepted, newVal);
        if (comparedAndExchange == excepted || comparedAndExchange >= newVal) {
            System.out.printf("更新成功  %d", test);
        } else {
            System.out.println("更新失败，期望值 不正确");
        }
    }


}
