# 📝 代码风格与注释规范

本文档定义了 Crestue Omni-BOP 项目的代码风格和注释规范，确保代码的可读性和可维护性。

## 📋 文件头注释规范

### Java 文件头注释
```java
/**
 * Crestue Omni-BOP - 一体化商业运营平台
 * 
 * @filename UserService.java
 * @description 用户服务接口实现
 * @author Your Name
 * @email your.email@example.com
 * @version 1.0.0
 * @date 2024-01-01
 * 
 * Copyright (c) 2024 Crestue. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
```

### TypeScript/Vue 文件头注释
```typescript
/**
 * Crestue Omni-BOP - 一体化商业运营平台
 * 
 * @filename UserList.vue
 * @description 用户列表组件
 * @author Your Name
 * @email your.email@example.com
 * @version 1.0.0
 * @date 2024-01-01
 * 
 * Copyright (c) 2024 Crestue. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
```

### 配置文件头注释
```yaml
# Crestue Omni-BOP - 一体化商业运营平台
# 
# @filename application.yml
# @description 应用配置文件
# @author Your Name
# @email your.email@example.com
# @version 1.0.0
# @date 2024-01-01
# 
# Copyright (c) 2024 Crestue. All rights reserved.
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published
# by the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
```

## 🔧 类/接口注释规范

### Java 类注释
```java
/**
 * 用户服务接口实现类
 * 
 * @description 提供用户相关的业务逻辑处理，包括用户注册、登录、信息查询等功能
 * @author Your Name
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
@Slf4j
public class UserService {
    // 类实现
}
```

### TypeScript 类注释
```typescript
/**
 * 用户服务类
 * 
 * @description 提供用户相关的业务逻辑处理
 * @author Your Name
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
export class UserService {
    // 类实现
}
```

### Vue 组件注释
```vue
<template>
  <!-- 用户列表组件模板 -->
</template>

<script lang="ts">
/**
 * 用户列表组件
 * 
 * @description 显示用户列表，支持搜索、分页、操作等功能
 * @author Your Name
 * @version 1.0.0
 * @since 2024-01-01
 */
@Component
export default class UserList extends Vue {
    // 组件实现
}
</script>
```

## 📝 方法/函数注释规范

### Java 方法注释
```java
/**
 * 根据用户ID获取用户详细信息
 * 
 * @param userId 用户ID，不能为空
 * @return 用户实体对象，包含用户完整信息
 * @throws UserNotFoundException 当用户不存在时抛出此异常
 * @throws IllegalArgumentException 当userId为null或小于等于0时抛出
 * @see User
 * @since 1.0.0
 */
public User getUserById(@NotNull Long userId) throws UserNotFoundException {
    // 方法实现
}
```

### TypeScript 方法注释
```typescript
/**
 * 根据用户ID获取用户详细信息
 * 
 * @param userId - 用户ID，必须大于0
 * @returns 用户信息对象
 * @throws UserNotFoundError 当用户不存在时抛出
 * @since 1.0.0
 */
public async getUserById(userId: number): Promise<User> {
    // 方法实现
}
```

### Vue 方法注释
```typescript
/**
 * 删除用户
 * 
 * @param userId - 要删除的用户ID
 * @returns Promise<void>
 * @throws Error 当删除失败时抛出
 * @emits user-deleted 用户删除成功事件
 * @since 1.0.0
 */
private async deleteUser(userId: number): Promise<void> {
    // 方法实现
}
```

## 🔍 属性/字段注释规范

### Java 字段注释
```java
/** 用户数据访问对象 */
@Autowired
private UserMapper userMapper;

/** 用户缓存键前缀 */
private static final String USER_CACHE_PREFIX = "user:";

/** 默认分页大小 */
@Value("${app.page-size:20}")
private Integer defaultPageSize;
```

### TypeScript 属性注释
```typescript
/** 用户列表数据 */
@Prop({ type: Array, default: () => [] })
private readonly users!: User[]

/** 加载状态 */
private loading: boolean = false

/** 搜索关键词 */
private searchKeyword: string = ''
```

## ⚙️ 配置注释规范

### YAML 配置注释
```yaml
# 数据库配置
spring:
  datasource:
    # 数据库连接URL
    url: jdbc:mysql://localhost:3306/omni_bop?useUnicode=true&characterEncoding=utf8
    # 数据库用户名
    username: root
    # 数据库密码
    password: ${DB_PASSWORD:123456}

# Redis配置
redis:
  # Redis服务器地址
  host: localhost
  # Redis服务器端口
  port: 6379
  # Redis密码（可选）
  password: ${REDIS_PASSWORD:}
  # 数据库索引
  database: 0
```

### 环境变量注释
```bash
# 数据库配置
DB_HOST=localhost          # 数据库主机地址
DB_PORT=3306              # 数据库端口
DB_NAME=omni_bop          # 数据库名称
DB_USER=root              # 数据库用户名
DB_PASSWORD=your_password # 数据库密码

# Redis配置
REDIS_HOST=localhost       # Redis主机地址
REDIS_PORT=6379           # Redis端口
REDIS_PASSWORD=           # Redis密码（可选）

# 应用配置
APP_PORT=8080             # 应用服务端口
APP_ENV=dev               # 运行环境：dev/test/prod
```

## 🎯 特殊注释规范

### TODO 注释
```java
// TODO: [高优先级] 需要优化查询性能，添加缓存机制
// 预计完成时间：2024-02-01
// 负责人：Your Name
public List<User> getUsers() {
    // 当前实现
    return userMapper.selectList(null);
}
```

### FIXME 注释
```java
// FIXME: [严重] 并发情况下可能出现数据不一致问题
// 问题描述：多线程同时修改用户状态可能导致状态混乱
// 临时解决方案：添加同步锁
// 长期解决方案：使用分布式锁或乐观锁
@synchronized
public void updateUserStatus(Long userId, UserStatus status) {
    // 方法实现
}
```

### NOTE 注释
```java
// NOTE: 此方法性能敏感，避免在循环中调用
// 优化建议：批量处理或使用缓存
public UserDetail getUserDetail(Long userId) {
    // 方法实现
}
```

## 📊 数据库表注释规范

### SQL 表结构注释
```sql
-- 用户表
-- 存储系统用户的基本信息
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名，唯一标识',
    `email` VARCHAR(100) NOT NULL COMMENT '用户邮箱',
    `phone` VARCHAR(20) COMMENT '用户手机号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

## 🔄 代码格式化规范

### Java 代码格式化
- 使用 4 个空格缩进
- 类和方法之间空一行
- 导入语句按组排序
- 每行不超过 120 个字符

### TypeScript 代码格式化
- 使用 2 个空格缩进
- 使用单引号
- 尾随逗号
- 分号可选

## 📋 检查清单

在提交代码前，请检查以下事项：

- [ ] 所有文件都有正确的文件头注释
- [ ] 所有类和方法都有完整的 JSDoc 注释
- [ ] 配置文件和 SQL 脚本有详细注释
- [ ] 复杂的业务逻辑有行内注释说明
- [ ] 代码符合格式化规范
- [ ] 删除了调试代码和临时注释

---

**遵循这些规范将有助于保持代码的一致性和可维护性！** 🚀