package com.kuafu.plugin.controller;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.plugin.domain.PluginInfo;
import com.kuafu.plugin.service.PluginManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 插件管理REST API
 */
@RestController
@RequestMapping("/api/plugin")
@Slf4j
public class PluginController {
    
    @Autowired
    private PluginManager pluginManager;
    
    /**
     * 获取所有插件列表
     */
    @GetMapping("/list")
    public BaseResponse<List<PluginInfo>> getAllPlugins() {
        log.info("获取所有插件列表");
        List<PluginInfo> plugins = pluginManager.getAllPlugins();
        return ResultUtils.success(plugins);
    }
    
    /**
     * 根据ID获取插件详情
     */
    @GetMapping("/{pluginId}")
    public BaseResponse<PluginInfo> getPluginById(@PathVariable String pluginId) {
        log.info("根据ID获取插件详情: {}", pluginId);
        PluginInfo plugin = pluginManager.getPluginById(pluginId);
        if (plugin == null) {
            return ResultUtils.error(HttpStatus.NOT_FOUND.value(), "插件不存在");
        }
        return ResultUtils.success(plugin);
    }
    
    /**
     * 安装插件
     */
    @PostMapping("/install")
    public BaseResponse<PluginInfo> installPlugin(@RequestParam("file") MultipartFile file) {
        log.info("安装插件: {}", file.getOriginalFilename());
        try {
            // 创建临时文件
            File tempFile = File.createTempFile("plugin_", ".jar");
            file.transferTo(tempFile);
            
            // 安装插件
            PluginInfo pluginInfo = pluginManager.installPlugin(tempFile);
            
            // 删除临时文件
            tempFile.delete();
            
            if (pluginInfo == null) {
                return ResultUtils.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "插件安装失败");
            }
            
            return ResultUtils.success(pluginInfo);
        } catch (IOException e) {
            log.error("插件安装失败: {}", file.getOriginalFilename(), e);
            return ResultUtils.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "插件安装失败: " + e.getMessage());
        }
    }
    
    /**
     * 卸载插件
     */
    @DeleteMapping("/{pluginId}")
    public BaseResponse<Boolean> uninstallPlugin(@PathVariable String pluginId) {
        log.info("卸载插件: {}", pluginId);
        boolean success = pluginManager.uninstallPlugin(pluginId);
        if (!success) {
            return ResultUtils.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "插件卸载失败");
        }
        return ResultUtils.success(true);
    }
    
    /**
     * 启用插件
     */
    @PostMapping("/{pluginId}/enable")
    public BaseResponse<Boolean> enablePlugin(@PathVariable String pluginId) {
        log.info("启用插件: {}", pluginId);
        boolean success = pluginManager.enablePlugin(pluginId);
        if (!success) {
            return ResultUtils.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "插件启用失败");
        }
        return ResultUtils.success(true);
    }
    
    /**
     * 禁用插件
     */
    @PostMapping("/{pluginId}/disable")
    public BaseResponse<Boolean> disablePlugin(@PathVariable String pluginId) {
        log.info("禁用插件: {}", pluginId);
        boolean success = pluginManager.disablePlugin(pluginId);
        if (!success) {
            return ResultUtils.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "插件禁用失败");
        }
        return ResultUtils.success(true);
    }
    
    /**
     * 刷新插件列表
     */
    @PostMapping("/refresh")
    public BaseResponse<Boolean> refreshPlugins() {
        log.info("刷新插件列表");
        pluginManager.refreshPlugins();
        return ResultUtils.success(true);
    }
}
