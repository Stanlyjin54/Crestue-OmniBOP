-- API网关数据库表结构

-- API路由表
CREATE TABLE IF NOT EXISTS api_route (
    route_id VARCHAR(64) PRIMARY KEY COMMENT '路由ID',
    path VARCHAR(255) NOT NULL COMMENT '路由路径',
    target_url VARCHAR(255) NOT NULL COMMENT '目标URL',
    method VARCHAR(16) NOT NULL COMMENT '请求方法',
    auth_type VARCHAR(32) NOT NULL DEFAULT 'none' COMMENT '认证方式：none, jwt, oauth2',
    enabled BOOLEAN NOT NULL DEFAULT true COMMENT '是否启用',
    load_balance_strategy VARCHAR(32) NOT NULL DEFAULT 'round_robin' COMMENT '负载均衡策略：round_robin, random, weighted_round_robin',
    timeout INT NOT NULL DEFAULT 30000 COMMENT '超时时间（毫秒）',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    CONSTRAINT uk_api_route UNIQUE (path, method)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API路由表';

-- API认证表
CREATE TABLE IF NOT EXISTS api_auth (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id VARCHAR(64) NOT NULL COMMENT '路由ID',
    auth_name VARCHAR(64) NOT NULL COMMENT '认证名称',
    auth_config TEXT NOT NULL COMMENT '认证配置（JSON格式）',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (route_id) REFERENCES api_route(route_id) ON DELETE CASCADE,
    CONSTRAINT uk_api_auth_route UNIQUE (route_id, auth_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API认证表';

-- API限流表
CREATE TABLE IF NOT EXISTS api_rate_limit (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id VARCHAR(64) NOT NULL COMMENT '路由ID',
    limit_type VARCHAR(32) NOT NULL DEFAULT 'api' COMMENT '限流类型：ip, user, api',
    requests_per_second INT NOT NULL DEFAULT 100 COMMENT '每秒允许的请求数',
    burst_requests INT NOT NULL DEFAULT 100 COMMENT '突发请求数',
    window_size INT NOT NULL DEFAULT 1 COMMENT '限流窗口大小（秒）',
    enabled BOOLEAN NOT NULL DEFAULT false COMMENT '是否启用',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    FOREIGN KEY (route_id) REFERENCES api_route(route_id) ON DELETE CASCADE,
    CONSTRAINT uk_api_rate_limit UNIQUE (route_id, limit_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API限流表';

-- API监控表
CREATE TABLE IF NOT EXISTS api_monitor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id VARCHAR(64) NOT NULL COMMENT '请求ID',
    route_id VARCHAR(64) NOT NULL COMMENT '路由ID',
    method VARCHAR(16) NOT NULL COMMENT '请求方法',
    path VARCHAR(255) NOT NULL COMMENT '请求路径',
    status_code INT NOT NULL COMMENT '响应状态码',
    response_time INT NOT NULL COMMENT '响应时间（毫秒）',
    client_ip VARCHAR(64) NOT NULL COMMENT '客户端IP',
    user_agent VARCHAR(255) COMMENT '用户代理',
    error_message TEXT COMMENT '错误信息',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    FOREIGN KEY (route_id) REFERENCES api_route(route_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API监控表';

-- API请求头映射表
CREATE TABLE IF NOT EXISTS api_header_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id VARCHAR(64) NOT NULL COMMENT '路由ID',
    source_header VARCHAR(128) NOT NULL COMMENT '源请求头',
    target_header VARCHAR(128) NOT NULL COMMENT '目标请求头',
    FOREIGN KEY (route_id) REFERENCES api_route(route_id) ON DELETE CASCADE,
    CONSTRAINT uk_api_header_mapping UNIQUE (route_id, source_header)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API请求头映射表';

-- API参数映射表
CREATE TABLE IF NOT EXISTS api_param_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    route_id VARCHAR(64) NOT NULL COMMENT '路由ID',
    source_param VARCHAR(128) NOT NULL COMMENT '源参数',
    target_param VARCHAR(128) NOT NULL COMMENT '目标参数',
    param_type VARCHAR(16) NOT NULL DEFAULT 'query' COMMENT '参数类型：query, path, body',
    FOREIGN KEY (route_id) REFERENCES api_route(route_id) ON DELETE CASCADE,
    CONSTRAINT uk_api_param_mapping UNIQUE (route_id, source_param, param_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API参数映射表';
