package com.zachary.bifromq.basehookloader;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: {@link BaseHookLoader} 的单元测试
 * @author: cuiweiman
 * @date: 2024/4/3 10:35
 */
public class BaseHookLoaderTest {

    @Test
    public void testLoad() {
        Map<String, ITestHookAPI> load = BaseHookLoader.load(ITestHookAPI.class);
        // 注意配置 META-INF/services
        if (load.size() == 1) {
            ITestHookAPI hookAPI = load.get("com.zachary.bifromq.basehookloader.TestHookImpl");
            System.out.println(hookAPI.sayHi());
        }
        Assert.assertEquals(load.size(), 1);
    }

    @Test
    public void testPutIfAbsent() {
        String keyA = "a";
        String valA = "aValue";
        String valB = "bValue";
        Map<String, String> map = new HashMap<>();

        // map.putIfAbsent 方法 会返回 key 的旧值，若 key 之前不存在则返回 null
        String firstPutIfAbsent = map.putIfAbsent(keyA, valA);
        Assert.assertNull(firstPutIfAbsent);

        // keyA 已经有值了 是 valA，再次 存储 valB 时放不进去了，只返回 旧值
        String secondPutIfAbsent = map.putIfAbsent(keyA, valB);
        Assert.assertEquals(secondPutIfAbsent, valA);
        Assert.assertEquals(map.get(keyA), valA);

        // map.put 方法 会返回 key 的旧值，若 key 之前不存在则返回 null
        String firstPut = map.put("b", valB);
        Assert.assertNull(firstPut);
        String oldValue = map.put("b", valA);
        Assert.assertEquals(oldValue, valB);

    }

}
