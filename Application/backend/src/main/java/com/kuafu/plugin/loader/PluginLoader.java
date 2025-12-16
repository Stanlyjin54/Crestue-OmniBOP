package com.kuafu.plugin.loader;

import com.kuafu.plugin.domain.PluginInfo;

import java.io.File;
import java.util.List;

/**
 * 插件加载器，负责从文件系统加载插件
 */
public interface PluginLoader {
    /**
     * 加载单个插件文件
     * @param pluginFile 插件文件
     * @return 插件信息
     */
    PluginInfo loadPlugin(File pluginFile);
    
    /**
     * 从目录加载所有插件
     * @param pluginDir 插件目录
     * @return 插件信息列表
     */
    List<PluginInfo> loadPluginsFromDir(File pluginDir);
    
    /**
     * 卸载插件
     * @param pluginInfo 插件信息
     * @return 是否成功
     */
    boolean unloadPlugin(PluginInfo pluginInfo);
    
    /**
     * 初始化插件类加载器
     * @param pluginInfo 插件信息
     */
    void initClassLoader(PluginInfo pluginInfo);
    
    /**
     * 获取插件类加载器
     * @param pluginId 插件ID
     * @return 类加载器
     */
    ClassLoader getClassLoader(String pluginId);
    
    /**
     * 关闭所有类加载器
     */
    void closeAllClassLoaders();
}
