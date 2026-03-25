# Crestue OmniBOP - 一体化商业运营平台

[![License](https://img.shields.io/badge/License-AGPLv3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![Version](https://img.shields.io/badge/Version-1.0.0-green.svg)](https://github.com/crestue/omni-bop)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

## 📖 项目简介

Crestue OmniBOP（Business Operations Platform）是一个面向中小企业的**一体化商业运营平台**，集成了B2C商城、B2B渠道管理、客户关系管理（CRM）、进销存管理、支付结算等核心业务功能。

### 🎯 核心特性

- **🛒 全渠道销售**：B2C商城 + B2B渠道管理一体化
- **🤖 AI智能助手**：集成大语言模型，提供智能客服和业务分析
- **📊 数据驱动**：实时数据分析与业务洞察
- **🔧 模块化设计**：可插拔架构，支持按需扩展
- **☁️ 云原生架构**：支持容器化部署和弹性伸缩

## 🚀 快速开始

### 环境要求

- Java 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 安装部署

#### 1. 克隆项目
```bash
git clone https://github.com/Stanlyjin54/Crestue-OmniBOP.git
cd Crestue-OmniBOP
```

#### 2. 数据库初始化
```sql
# 创建数据库
CREATE DATABASE omni_bop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行初始化脚本（位于 scripts/database/init.sql）
```

#### 3. 后端服务启动
```bash
cd Application/backend
mvn clean install
mvn spring-boot:run
```

#### 4. 前端服务启动
```bash
# B2C前端
cd B2C/frontend
npm install
npm run serve

# B2B管理端
cd Application/frontend
npm install
npm run dev
```

#### 5. 访问系统
- Admin 管理系统：http://localhost:5173
- B2C商城：http://localhost:8080
- B2B管理端：http://localhost:3000
- API文档：http://localhost:8080/swagger-ui.html

## 🏗️ 系统架构

### 技术栈

| 模块 | 技术栈 | 说明 |
|------|--------|------|
| 后端 | Spring Boot + MyBatis Plus + Spring Security | 微服务架构，RESTful API |
| Admin 前端 | Vue 3 + Element Plus | 管理系统前端，响应式设计 |
| B2C前端 | Vue 3 + TypeScript + Element Plus | 响应式设计，PWA支持 |
| B2B管理端 | Uni-app + Vue 3 | 跨平台管理应用 |
| 数据库 | MySQL + Redis | 关系型+缓存数据库 |
| AI集成 | 阿里云DashVector + 通义千问 + Dify | 智能客服和业务分析 |

### 目录结构

```
Crestue-Omni-BOP/
├── Admin/                      # 管理系统
│   ├── backend/               # 管理后端服务
│   │   └── src/               # 后端源码
│   └── frontend/              # 管理前端
│       └── src/               # 前端源码
├── Application/               # 核心应用
│   ├── backend/               # 后端服务
│   │   ├── src/main/java/com/kuafu/
│   │   │   ├── api/           # API网关和代理
│   │   │   ├── llm/           # AI大语言模型模块
│   │   │   ├── customer/      # 客户管理
│   │   │   └── order/         # 订单管理
│   │   └── application.yml    # 应用配置
│   └── frontend/              # B2B管理前端
│       └── src/pages/         # 页面组件
├── B2C/                       # B2C商城
│   └── frontend/              # B2C前端
│       └── src/views/         # 商城页面
├── scripts/                   # 部署和运维脚本
└── docs/                      # 文档
```

## 📋 功能模块

### 🛒 B2C商城模块
- 商品展示和搜索
- 购物车和订单管理
- 支付集成（微信、支付宝）
- 会员中心和售后服务

### 🏢 B2B渠道管理
- 多级渠道管理
- 价格策略和返利管理
- 库存调拨和协同
- 客户关系管理（CRM）

### 📊 业务管理
- 进销存管理
- 财务结算
- 数据分析和报表
- 权限管理和组织架构

### 🤖 AI智能助手
- 智能客服机器人
- 业务数据智能分析
- 价格策略优化建议
- 客户行为预测

## 🔧 配置说明

### 核心配置（application.yml）

```yaml
# AI模块配置
llm:
  enable: ${LLM_ENABLE:true}           # 启用AI功能
  vector:
    provider: ${VECTOR_PROVIDER:ali}   # 向量数据库提供商
    collection_name: ${VECTOR_COLLECT_NAME:test}
    api_key: ${VECTOR_KEY:your_key}
    end_point: ${VECTOR_POINT:your_endpoint}
  embedding:
    provider: ${EMBEDDING_PROVIDER:ali} # 嵌入模型提供商
    api_key: ${EMBEDDING_KEY:your_key}
  chat:
    provider: ${CHAT_PROVIDER:dify}     # 对话模型提供商
    dify_key: ${CHAT_DIFY_KEY:your_key}
    dify_api: ${CHAT_DIDY_API:your_api}
```

### 环境变量配置

创建 `.env` 文件：
```bash
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=omni_bop
DB_USER=root
DB_PASSWORD=your_password

# Redis配置
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# AI服务配置
LLM_ENABLE=true
VECTOR_PROVIDER=ali
EMBEDDING_PROVIDER=ali
CHAT_PROVIDER=dify
```

## 🤝 参与贡献

我们欢迎各种形式的贡献！请阅读 [CONTRIBUTING.md](CONTRIBUTING.md) 了解如何参与项目开发。

### 贡献流程
1. Fork 本项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 **AGPLv3** 开源许可证，详细信息请查看 [LICENSE](LICENSE) 文件。

对于商业使用，我们提供商业许可证，请联系 business@crestue.com。

## 🆘 支持与帮助

### 文档资源
- [用户手册](docs/user-guide.md)
- [开发指南](docs/development.md)
- [API文档](docs/api.md)
- [部署指南](docs/deployment.md)

### 社区支持
- 📖 [GitHub Issues](https://github.com/Stanlyjin54/Crestue-OmniBOP/issues) - 问题反馈
- 💬 [Discussions](https://github.com/Stanlyjin54/Crestue-OmniBOP/discussions) - 技术讨论
- 📧 Email: 549057226@qq.com

### 商业支持
对于企业用户，我们提供专业的技术支持和定制开发服务：
- 🏢 企业版授权
- 🔧 定制开发
- ☁️ 云托管服务
- 📞 专业技术支持

联系邮箱：549057226@qq.com

## 🏆 致谢

感谢所有为这个项目做出贡献的开发者！

---

**Crestue OmniBOP** - 让商业运营更简单、更智能！