package com.kuafu.plugin.service;

import com.kuafu.plugin.domain.PluginInfo;

import java.io.File;
import java.util.List;

/**
 * 插件管理核心接口，负责插件的生命周期管理
 */
public interface PluginManager {
    /**
     * 初始化插件管理器
     */
    void init();
    
    /**
     * 安装插件
     * @param pluginFile 插件文件
     * @return 插件信息
     */
    PluginInfo installPlugin(File pluginFile);
    
    /**
     * 卸载插件
     * @param pluginId 插件ID
     * @return 是否成功
     */
    boolean uninstallPlugin(String pluginId);
    
    /**
     * 启用插件
     * @param pluginId 插件ID
     * @return 是否成功
     */
    boolean enablePlugin(String pluginId);
    
    /**
     * 禁用插件
     * @param pluginId 插件ID
     * @return 是否成功
     */
    boolean disablePlugin(String pluginId);
    
    /**
     * 获取所有插件信息
     * @return 插件信息列表
     */
    List<PluginInfo> getAllPlugins();
    
    /**
     * 根据ID获取插件信息
     * @param pluginId 插件ID
     * @return 插件信息
     */
    PluginInfo getPluginById(String pluginId);
    
    /**
     * 获取插件实例
     * @param pluginId 插件ID
     * @return 插件实例
     */
    Object getPluginInstance(String pluginId);
    
    /**
     * 刷新插件列表
     */
    void refreshPlugins();
    
    /**
     * 关闭插件管理器
     */
    void shutdown();
}
