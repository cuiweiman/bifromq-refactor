package com.zachary.bifromq.basehookloader;

/**
 * @description: 测试接口实现类
 * @author: cuiweiman
 * @date: 2024/4/3 10:34
 */
public class TestHookImpl implements ITestHookAPI {
    @Override
    public String sayHi() {
        return "\n\tTestHookImpl Hi ~  ^_^";
    }
}
