package com.kuafu.plugin.api;

/**
 * 插件API接口，定义插件可调用的系统功能
 */
public interface PluginApi {
    /**
     * 获取系统配置
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    <T> T getConfig(String key, T defaultValue);
    
    /**
     * 设置系统配置
     * @param key 配置键
     * @param value 配置值
     */
    <T> void setConfig(String key, T value);
    
    /**
     * 记录日志
     * @param level 日志级别
     * @param message 日志消息
     * @param args 日志参数
     */
    void log(String level, String message, Object... args);
    
    /**
     * 获取其他插件实例
     * @param pluginId 插件ID
     * @return 插件实例
     */
    Object getPluginInstance(String pluginId);
    
    /**
     * 注册API端点
     * @param path API路径
     * @param handler API处理程序
     */
    void registerApiEndpoint(String path, Object handler);
    
    /**
     * 发送事件
     * @param eventName 事件名称
     * @param data 事件数据
     */
    void sendEvent(String eventName, Object data);
    
    /**
     * 订阅事件
     * @param eventName 事件名称
     * @param listener 事件监听器
     */
    void subscribeEvent(String eventName, Object listener);
    
    /**
     * 取消订阅事件
     * @param eventName 事件名称
     * @param listener 事件监听器
     */
    void unsubscribeEvent(String eventName, Object listener);
}
