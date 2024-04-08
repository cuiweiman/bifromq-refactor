package com.zachary.bifromq.basehookloader;

import org.testng.Assert;
import org.testng.annotations.Test;

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

}
