package com.zachary.bifromq.plugin.eventcollector;

import com.zachary.bifromq.plugin.eventcollector.Event;
import com.zachary.bifromq.plugin.eventcollector.EventType;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

class EventPool {
    private static final Set<Class<Event<?>>> EVENT_TYPES = new HashSet<>();
    private final Event<?>[] events;

    static {
        Reflections reflections = new Reflections(Event.class.getPackageName());
        for (Class<?> eventClass : reflections.getSubTypesOf(Event.class)) {
            if (!Modifier.isAbstract(eventClass.getModifiers())) {
                EVENT_TYPES.add((Class<Event<?>>) eventClass);
            }
        }
    }

    EventPool() {
        events = new Event[EVENT_TYPES.size()];
        EVENT_TYPES.forEach(this::add);
    }

    <T extends Event<T>> T get(EventType eventType) {
        return (T) events[eventType.ordinal()];
    }

    @SneakyThrows
    private void add(Class<Event<?>> eventClass) {
        Event<?> event = eventClass.getConstructor().newInstance();
        events[event.type().ordinal()] = event;
    }
}
