package com.kuafu.plugin.loader;

import com.kuafu.plugin.domain.PluginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认插件加载器实现
 */
@Component
@Slf4j
public class DefaultPluginLoader implements PluginLoader {

    private final Map<String, ClassLoader> classLoaderMap = new HashMap<>();

    @Override
    public PluginInfo loadPlugin(File pluginFile) {
        log.info("跳过加载插件文件: {}", pluginFile.getName());
        return null;
    }

    @Override
    public List<PluginInfo> loadPluginsFromDir(File pluginDir) {
        log.info("插件目录不存在或为空: {}", pluginDir.getAbsolutePath());
        return new ArrayList<>();
    }

    @Override
    public boolean unloadPlugin(PluginInfo pluginInfo) {
        return true;
    }

    @Override
    public void initClassLoader(PluginInfo pluginInfo) {
        log.info("初始化插件类加载器: {}", pluginInfo.getId());
    }

    @Override
    public ClassLoader getClassLoader(String pluginId) {
        return classLoaderMap.get(pluginId);
    }

    @Override
    public void closeAllClassLoaders() {
        log.info("关闭所有插件类加载器");
        classLoaderMap.clear();
    }
}