package com.kuafu.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig - 应用配置类
 */
@Configuration
@Slf4j
public class AppConfig {
    
    private static String profile = "dev";
    
    /**
     * 初始化配置
     */
    public void init() {
        log.info("初始化AppConfig");
        // 配置初始化代码
    }
    
    /**
     * 获取当前配置环境
     * 
     * @return 配置环境
     */
    public static String getProfile() {
        return profile;
    }
    
    /**
     * 设置配置环境
     * 
     * @param profile 配置环境
     */
    public static void setProfile(String profile) {
        AppConfig.profile = profile;
    }
}