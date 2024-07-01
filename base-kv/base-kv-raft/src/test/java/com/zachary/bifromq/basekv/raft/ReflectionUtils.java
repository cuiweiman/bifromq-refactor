package com.zachary.bifromq.basekv.raft;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class ReflectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getField(Object targetObject, String fieldName) {
        try {
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(targetObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("get field {} from {} failed: {}", targetObject, fieldName, e.getMessage());
        }
        return null;
    }
}