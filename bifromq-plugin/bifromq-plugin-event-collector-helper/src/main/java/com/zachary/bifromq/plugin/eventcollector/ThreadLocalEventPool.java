package com.zachary.bifromq.plugin.eventcollector;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

import static java.lang.ThreadLocal.withInitial;

public final class ThreadLocalEventPool {
    private static final Set<Class<? extends Event<?>>> EVENT_TYPES = new HashSet<>();

    private static final ThreadLocal<IdentityHashMap<Class<? extends Event<?>>, Event<?>>> THREAD_LOCAL_EVENTS;

    static {
        Reflections reflections = new Reflections(Event.class.getPackageName());
        for (Class<?> eventClass : reflections.getSubTypesOf(Event.class)) {
            if (!Modifier.isAbstract(eventClass.getModifiers())) {
                EVENT_TYPES.add((Class<? extends Event<?>>) eventClass);
            }
        }
        THREAD_LOCAL_EVENTS = withInitial(ThreadLocalEventPool::init);
    }

    private static IdentityHashMap<Class<? extends Event<?>>, Event<?>> init() {
        IdentityHashMap<Class<? extends Event<?>>, Event<?>> events = new IdentityHashMap<>(EVENT_TYPES.size());
        EVENT_TYPES.forEach(t -> add(t, events));
        return events;
    }

    @SneakyThrows
    private static void add(Class<? extends Event<?>> eventClass,
                            IdentityHashMap<Class<? extends Event<?>>, Event<?>> eventMap) {
        Event<?> event = eventClass.getConstructor().newInstance();
        eventMap.put(eventClass, event);
    }

    public static <T extends Event<T>> T getLocal(Class<T> eventClass) {
        return (T) THREAD_LOCAL_EVENTS.get().get(eventClass);
    }
}
