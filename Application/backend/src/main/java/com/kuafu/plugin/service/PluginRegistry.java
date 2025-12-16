package com.kuafu.plugin.service;

import com.kuafu.plugin.domain.PluginInfo;

import java.util.List;
import java.util.Map;

/**
 * 插件注册表，管理所有已安装的插件
 */
public interface PluginRegistry {
    /**
     * 注册插件
     * @param pluginInfo 插件信息
     */
    void registerPlugin(PluginInfo pluginInfo);
    
    /**
     * 注销插件
     * @param pluginId 插件ID
     */
    void unregisterPlugin(String pluginId);
    
    /**
     * 获取所有插件
     * @return 插件信息映射
     */
    Map<String, PluginInfo> getAllPlugins();
    
    /**
     * 根据ID获取插件
     * @param pluginId 插件ID
     * @return 插件信息
     */
    PluginInfo getPluginById(String pluginId);
    
    /**
     * 根据状态获取插件
     * @param status 插件状态
     * @return 插件信息列表
     */
    List<PluginInfo> getPluginsByStatus(Integer status);
    
    /**
     * 更新插件信息
     * @param pluginInfo 插件信息
     */
    void updatePlugin(PluginInfo pluginInfo);
    
    /**
     * 检查插件是否已注册
     * @param pluginId 插件ID
     * @return 是否已注册
     */
    boolean isPluginRegistered(String pluginId);
    
    /**
     * 获取插件依赖关系图
     * @return 依赖关系图
     */
    Map<String, List<String>> getDependencyGraph();
    
    /**
     * 检查插件依赖是否满足
     * @param pluginInfo 插件信息
     * @return 是否满足依赖
     */
    boolean checkDependencies(PluginInfo pluginInfo);
    
    /**
     * 获取插件的依赖链
     * @param pluginId 插件ID
     * @return 依赖链
     */
    List<String> getDependencyChain(String pluginId);
}
