package com.kuafu.plugin.domain;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 插件信息实体类
 */
@Data
public class PluginInfo {
    /**
     * 插件ID
     */
    private String id;
    
    /**
     * 插件名称
     */
    private String name;
    
    /**
     * 插件版本
     */
    private String version;
    
    /**
     * 插件描述
     */
    private String description;
    
    /**
     * 插件作者
     */
    private String author;
    
    /**
     * 插件状态：0-未安装，1-已安装，2-已启用，3-已禁用
     */
    private Integer status;
    
    /**
     * 插件依赖
     */
    private String[] dependencies;
    
    /**
     * 插件配置
     */
    private Map<String, Object> config;
    
    /**
     * 插件类名
     */
    private String className;
    
    /**
     * 插件路径
     */
    private String path;
    
    /**
     * 安装时间
     */
    private Date installTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 最后启用时间
     */
    private Date lastEnableTime;
}
