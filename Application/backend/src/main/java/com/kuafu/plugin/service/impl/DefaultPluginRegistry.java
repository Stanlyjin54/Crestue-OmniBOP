package com.kuafu.plugin.service.impl;

import com.kuafu.plugin.domain.PluginInfo;
import com.kuafu.plugin.service.PluginRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认插件注册表实现
 */
@Component
@Slf4j
public class DefaultPluginRegistry implements PluginRegistry {

    private final Map<String, PluginInfo> pluginMap = new HashMap<>();

    @Override
    public void registerPlugin(PluginInfo pluginInfo) {
        log.info("注册插件: {}", pluginInfo.getId());
        pluginMap.put(pluginInfo.getId(), pluginInfo);
    }

    @Override
    public void unregisterPlugin(String pluginId) {
        log.info("注销插件: {}", pluginId);
        pluginMap.remove(pluginId);
    }

    @Override
    public Map<String, PluginInfo> getAllPlugins() {
        return new HashMap<>(pluginMap);
    }

    @Override
    public PluginInfo getPluginById(String pluginId) {
        return pluginMap.get(pluginId);
    }

    @Override
    public List<PluginInfo> getPluginsByStatus(Integer status) {
        List<PluginInfo> result = new ArrayList<>();
        for (PluginInfo plugin : pluginMap.values()) {
            if (plugin.getStatus().equals(status)) {
                result.add(plugin);
            }
        }
        return result;
    }

    @Override
    public void updatePlugin(PluginInfo pluginInfo) {
        if (pluginMap.containsKey(pluginInfo.getId())) {
            pluginMap.put(pluginInfo.getId(), pluginInfo);
        }
    }

    @Override
    public boolean isPluginRegistered(String pluginId) {
        return pluginMap.containsKey(pluginId);
    }

    @Override
    public Map<String, List<String>> getDependencyGraph() {
        return new HashMap<>();
    }

    @Override
    public boolean checkDependencies(PluginInfo pluginInfo) {
        return true;
    }

    @Override
    public List<String> getDependencyChain(String pluginId) {
        return new ArrayList<>();
    }
}