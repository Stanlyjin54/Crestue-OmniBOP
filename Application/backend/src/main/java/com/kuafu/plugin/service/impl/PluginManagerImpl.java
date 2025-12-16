package com.kuafu.plugin.service.impl;

import com.kuafu.plugin.domain.PluginInfo;
import com.kuafu.plugin.loader.PluginLoader;
import com.kuafu.plugin.service.PluginManager;
import com.kuafu.plugin.service.PluginRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * 插件管理核心类，负责插件的生命周期管理
 */
@Service
@Slf4j
public class PluginManagerImpl implements PluginManager {
    
    @Autowired
    private PluginLoader pluginLoader;
    
    @Autowired
    private PluginRegistry pluginRegistry;
    
    private final File pluginDir = new File("plugins");
    
    @Override
    public void init() {
        log.info("初始化插件管理器");
        // 确保插件目录存在
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }
        // 加载插件目录下的所有插件
        refreshPlugins();
        log.info("插件管理器初始化完成");
    }
    
    @Override
    public PluginInfo installPlugin(File pluginFile) {
        log.info("安装插件: {}", pluginFile.getName());
        try {
            // 加载插件
            PluginInfo pluginInfo = pluginLoader.loadPlugin(pluginFile);
            if (pluginInfo == null) {
                log.error("插件加载失败: {}", pluginFile.getName());
                return null;
            }
            
            // 检查依赖
            if (!pluginRegistry.checkDependencies(pluginInfo)) {
                log.error("插件依赖不满足: {}", pluginInfo.getId());
                return null;
            }
            
            // 注册插件
            pluginRegistry.registerPlugin(pluginInfo);
            log.info("插件安装成功: {}", pluginInfo.getName());
            return pluginInfo;
        } catch (Exception e) {
            log.error("插件安装失败: {}", pluginFile.getName(), e);
            return null;
        }
    }
    
    @Override
    public boolean uninstallPlugin(String pluginId) {
        log.info("卸载插件: {}", pluginId);
        try {
            PluginInfo pluginInfo = pluginRegistry.getPluginById(pluginId);
            if (pluginInfo == null) {
                log.error("插件不存在: {}", pluginId);
                return false;
            }
            
            // 卸载插件
            boolean success = pluginLoader.unloadPlugin(pluginInfo);
            if (success) {
                // 注销插件
                pluginRegistry.unregisterPlugin(pluginId);
                log.info("插件卸载成功: {}", pluginId);
            } else {
                log.error("插件卸载失败: {}", pluginId);
            }
            return success;
        } catch (Exception e) {
            log.error("插件卸载失败: {}", pluginId, e);
            return false;
        }
    }
    
    @Override
    public boolean enablePlugin(String pluginId) {
        log.info("启用插件: {}", pluginId);
        try {
            PluginInfo pluginInfo = pluginRegistry.getPluginById(pluginId);
            if (pluginInfo == null) {
                log.error("插件不存在: {}", pluginId);
                return false;
            }
            
            // 检查依赖
            if (!pluginRegistry.checkDependencies(pluginInfo)) {
                log.error("插件依赖不满足: {}", pluginId);
                return false;
            }
            
            // 启用插件
            pluginInfo.setStatus(2); // 2-已启用
            pluginRegistry.updatePlugin(pluginInfo);
            log.info("插件启用成功: {}", pluginId);
            return true;
        } catch (Exception e) {
            log.error("插件启用失败: {}", pluginId, e);
            return false;
        }
    }
    
    @Override
    public boolean disablePlugin(String pluginId) {
        log.info("禁用插件: {}", pluginId);
        try {
            PluginInfo pluginInfo = pluginRegistry.getPluginById(pluginId);
            if (pluginInfo == null) {
                log.error("插件不存在: {}", pluginId);
                return false;
            }
            
            // 禁用插件
            pluginInfo.setStatus(3); // 3-已禁用
            pluginRegistry.updatePlugin(pluginInfo);
            log.info("插件禁用成功: {}", pluginId);
            return true;
        } catch (Exception e) {
            log.error("插件禁用失败: {}", pluginId, e);
            return false;
        }
    }
    
    @Override
    public List<PluginInfo> getAllPlugins() {
        return pluginRegistry.getAllPlugins().values().stream().toList();
    }
    
    @Override
    public PluginInfo getPluginById(String pluginId) {
        return pluginRegistry.getPluginById(pluginId);
    }
    
    @Override
    public Object getPluginInstance(String pluginId) {
        // TODO: 实现插件实例获取逻辑
        return null;
    }
    
    @Override
    public void refreshPlugins() {
        log.info("刷新插件列表");
        try {
            List<PluginInfo> pluginInfos = pluginLoader.loadPluginsFromDir(pluginDir);
            for (PluginInfo pluginInfo : pluginInfos) {
                pluginRegistry.registerPlugin(pluginInfo);
            }
            log.info("插件列表刷新完成，共加载 {} 个插件", pluginInfos.size());
        } catch (Exception e) {
            log.error("插件列表刷新失败", e);
        }
    }
    
    @Override
    public void shutdown() {
        log.info("关闭插件管理器");
        pluginLoader.closeAllClassLoaders();
        log.info("插件管理器已关闭");
    }
}
