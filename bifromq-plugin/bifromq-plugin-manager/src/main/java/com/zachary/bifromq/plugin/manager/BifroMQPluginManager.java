package com.zachary.bifromq.plugin.manager;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.CompoundPluginLoader;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ExtensionFactory;
import org.pf4j.PluginLoader;
import org.pf4j.PluginRuntimeException;

@Slf4j
public class BifroMQPluginManager extends DefaultPluginManager {
    @Override
    protected PluginLoader createPluginLoader() {
        return new CompoundPluginLoader()
            .add(new BifroMQDevelopmentPluginLoader(this), this::isDevelopment)
            .add(new BifroMQJarPluginLoader(this), this::isNotDevelopment)
            .add(new BifroMQDefaultPluginLoader(this), this::isNotDevelopment);
    }

    @Override
    protected ExtensionFactory createExtensionFactory() {
        return new ExtensionFactory() {
            @Override
            public <T> T create(Class<T> extensionClass) {
                log.debug("Create instance for extension '{}'", extensionClass.getName());
                ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
                try {
                    ClassLoader targetLoader = extensionClass.getClassLoader();
                    Thread.currentThread().setContextClassLoader(targetLoader);
                    T instance = extensionClass.newInstance();
                    Thread.currentThread().setContextClassLoader(originalLoader);
                    return instance;
                } catch (Exception e) {
                    Thread.currentThread().setContextClassLoader(originalLoader);
                    throw new PluginRuntimeException(e);
                }
            }
        };
    }
}
