package com.zachary.bifromq.plugin.manager;

import org.pf4j.ClassLoadingStrategy;
import org.pf4j.PluginClassLoader;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginManager;

class BifroMQPluginClassLoader extends PluginClassLoader {
    public BifroMQPluginClassLoader(PluginManager pluginManager,
                                    PluginDescriptor pluginDescriptor,
                                    ClassLoader parent) {
        super(pluginManager, pluginDescriptor, parent);
    }

    public BifroMQPluginClassLoader(PluginManager pluginManager,
                                    PluginDescriptor pluginDescriptor,
                                    ClassLoader parent,
                                    ClassLoadingStrategy classLoadingStrategy) {
        super(pluginManager, pluginDescriptor, parent, classLoadingStrategy);
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(className)) {
            // if the class is provided by bifromq
            if (ProvidedPackages.isProvided(className)) {
                return getParent().loadClass(className);
            }
        }
        return super.loadClass(className);
    }
}
