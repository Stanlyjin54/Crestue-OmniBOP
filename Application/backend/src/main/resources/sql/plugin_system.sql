-- 插件系统数据库表结构

-- 插件信息表
CREATE TABLE IF NOT EXISTS plugin_info (
    id VARCHAR(64) PRIMARY KEY COMMENT '插件ID',
    name VARCHAR(128) NOT NULL COMMENT '插件名称',
    version VARCHAR(32) NOT NULL COMMENT '插件版本',
    description TEXT COMMENT '插件描述',
    author VARCHAR(64) COMMENT '插件作者',
    status INT NOT NULL DEFAULT 0 COMMENT '插件状态：0-未安装，1-已安装，2-已启用，3-已禁用',
    class_name VARCHAR(255) NOT NULL COMMENT '插件类名',
    path VARCHAR(255) NOT NULL COMMENT '插件路径',
    install_time DATETIME NOT NULL COMMENT '安装时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    last_enable_time DATETIME COMMENT '最后启用时间',
    CONSTRAINT uk_plugin_id UNIQUE (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='插件信息表';

-- 插件依赖表
CREATE TABLE IF NOT EXISTS plugin_dependency (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plugin_id VARCHAR(64) NOT NULL COMMENT '插件ID',
    dependency_id VARCHAR(64) NOT NULL COMMENT '依赖插件ID',
    dependency_version VARCHAR(32) NOT NULL COMMENT '依赖插件版本',
    FOREIGN KEY (plugin_id) REFERENCES plugin_info(id) ON DELETE CASCADE,
    FOREIGN KEY (dependency_id) REFERENCES plugin_info(id) ON DELETE CASCADE,
    CONSTRAINT uk_plugin_dependency UNIQUE (plugin_id, dependency_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='插件依赖表';

-- 插件配置表
CREATE TABLE IF NOT EXISTS plugin_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plugin_id VARCHAR(64) NOT NULL COMMENT '插件ID',
    config_key VARCHAR(128) NOT NULL COMMENT '配置键',
    config_value TEXT NOT NULL COMMENT '配置值',
    config_type VARCHAR(32) NOT NULL DEFAULT 'string' COMMENT '配置类型：string, int, boolean, json等',
    description VARCHAR(255) COMMENT '配置描述',
    FOREIGN KEY (plugin_id) REFERENCES plugin_info(id) ON DELETE CASCADE,
    CONSTRAINT uk_plugin_config UNIQUE (plugin_id, config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='插件配置表';
