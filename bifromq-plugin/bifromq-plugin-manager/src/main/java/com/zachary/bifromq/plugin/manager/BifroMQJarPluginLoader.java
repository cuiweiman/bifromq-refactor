package com.zachary.bifromq.plugin.manager;

import org.pf4j.JarPluginLoader;
import org.pf4j.PluginClassLoader;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginManager;

import java.nio.file.Path;

class BifroMQJarPluginLoader extends JarPluginLoader {
    public BifroMQJarPluginLoader(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public ClassLoader loadPlugin(Path pluginPath, PluginDescriptor pluginDescriptor) {
        PluginClassLoader pluginClassLoader =
            new BifroMQPluginClassLoader(pluginManager, pluginDescriptor, getClass().getClassLoader());
        pluginClassLoader.addFile(pluginPath.toFile());

        return pluginClassLoader;
    }
}
