# 版本历史记录

## 版本 0.1.6 (2026-03-28)
# 添加用户类型区分和B2C接口支持

### 主要改动
- **用户类型区分**: 在 UserInfo 实体添加 user_type 字段，区分 consumer(B2C消费者) 和 operator(经营者)
- **收货地址管理**: 创建 UserAddress 实体和 /user/addresses 接口，支持完整的收货地址CRUD
- **B2C订单接口**: 创建 /b2c/order/create 等接口，支持B2C订单创建和管理
- **商品SKU接口**: 创建 ProductSku 实体和 /product/{id}/skus 接口，支持商品规格管理
- **数据库迁移**: 添加 V20250328190000 迁移脚本，创建新表和字段
- **权限控制**: 使用 SecurityUtils.getUserId() 获取当前用户，确保数据隔离

### 新增文件
- `Application/backend/src/main/java/com/kuafu/web/entity/UserAddress.java`: 收货地址实体
- `Application/backend/src/main/java/com/kuafu/web/controller/UserAddressController.java`: 收货地址控制器
- `Application/backend/src/main/java/com/kuafu/web/entity/ProductSku.java`: 商品SKU实体
- `Application/backend/src/main/java/com/kuafu/web/controller/ProductSkuController.java`: SKU控制器
- `Application/backend/src/main/java/com/kuafu/web/controller/B2COrderController.java`: B2C订单控制器
- `Application/backend/src/main/resources/db/sqlite/V20250328190000__add_user_type_and_b2c_tables.sql`: 数据库迁移脚本

### 修改文件
- `Application/backend/src/main/java/com/kuafu/web/entity/UserInfo.java`: 添加 user_type 字段
- `Application/backend/src/main/java/com/kuafu/login/model/LoginVo.java`: 添加 userType 参数
- `.gitignore`: 添加临时脚本和数据库文件忽略规则

## 版本 0.1.5 (2026-03-25)
# 修复项目构建问题，更新文档和仓库地址

### 主要改动
- **Admin 后端修复**: 添加 Lombok 注解处理器配置，解决编译错误
- **Admin 前端修复**: 创建缺失的 RoleFormDialog.vue 组件
- **B2C 前端修复**: 更新 vue-tsc 版本，修复 TypeScript 类型错误
- **Application 后端修复**: 复制 Maven Wrapper 文件，添加 Lombok 配置
- **JDK 兼容性**: 安装 JDK 21 LTS 解决 Lombok 与 JDK 25 兼容性问题
- **文档更新**: 更新 README.md，添加 Admin 目录信息和系统架构说明
- **用户指南**: 新增 docs/user-guide.md 用户使用指南文档
- **仓库地址**: 更新 GitHub 仓库地址为 Stanlyjin54/Crestue-OmniBOP

### 技术总结
- **核心问题**: Lombok 注解处理器与 JDK 25 不兼容
- **解决策略**: 降级到 JDK 21 LTS，配置 maven-compiler-plugin
- **TypeScript 修复**: 更新类型定义，修复组件导入和 API 返回值
- **构建状态**: Admin 后端 ✅ / Admin 前端 ✅ / B2C 前端 ✅ / Application 后端 ✅

### 文件变更
- `Admin/backend/pom.xml`: 添加 maven-compiler-plugin Lombok 配置
- `Admin/frontend/src/views/role/components/RoleFormDialog.vue`: 新增组件
- `B2C/frontend/package.json`: 更新 vue-tsc 版本
- `B2C/frontend/src/types/index.ts`: 补充类型定义
- `B2C/frontend/src/api/product.ts`: 修复 API 返回类型
- `B2C/frontend/src/stores/cart.ts`: 添加 updateCartCount 方法
- `B2C/frontend/src/views/Order/Confirm.vue`: 修复模板类型错误
- `B2C/frontend/src/views/Product/Detail.vue`: 修复规格选择和类型问题
- `README.md`: 更新仓库地址和系统架构说明
- `CONTRIBUTING.md`: 更新仓库地址
- `docs/user-guide.md`: 新增用户使用指南
# 修复SCSS循环导入错误，优化前端样式配置

### 主要改动
- **SCSS配置修复**: 解决Vite配置中index.scss循环导入错误
- **样式处理优化**: 移除Vue插件中重复的SCSS导入配置
- **应用稳定性**: 确保前端应用加载过程无样式错误

### 技术总结
- **核心问题**: This file is already being loaded (SCSS循环导入)
- **解决策略**: 移除Vite配置中的additionalData重复导入
- **优化效果**: 消除样式编译警告，提升应用加载稳定性
- **运行状态**: 应用正常访问，HTTP 200状态码响应

## 版本 0.1.3 (2025-12-16)
# 解决Vue 3前端应用无法启动的版本兼容性问题

### 主要改动
- **版本兼容修复**: 解决Vite 7.2.6与Vue插件版本不兼容问题，更新至Vite 5.4.21
- **依赖版本升级**: 更新sass到1.97.0，确保与Vite版本兼容
- **SCSS配置优化**: 修复Vite配置中的SCSS处理问题，启用全局样式导入
- **代理配置调整**: 禁用后端代理配置避免连接错误，实现前端独立运行
- **应用启动验证**: 确认前端应用可正常启动并访问http://localhost:5173

### 技术总结
- **核心问题**: sass.initAsyncCompiler is not a function (Vite与Sass版本冲突)
- **解决策略**: 降级Vite到稳定版本，更新配套依赖
- **配置优化**: 简化Vite配置，确保SCSS正确编译
- **运行状态**: 应用启动时间400ms，加载正常，页面响应200状态码

## 版本 0.1.2 (2025-12-16)
# 完成前端部署问题解决，更新项目文档和版本信息

### 主要改动
- **文档完善**: 完成项目版本历史记录和部署问题总结
- **版本升级**: 更新package.json版本至0.1.2
- **任务完成**: 完成前端部署问题的系统化解决方案

### 技术总结
- **部署方案**: 采用简化配置绕过SCSS预处理错误
- **兼容性**: 通过版本管理和配置调整解决依赖冲突
- **可维护性**: 建立完整的文档体系支持后续开发

## 版本 0.1.1 (2025-12-16)
# 修复前端部署SCSS加载错误，实现管理后台可运行演示

### 主要改动
- **前端部署修复**: 创建简化版Vite配置(vite.config.simple.js)解决SCSS加载错误
- **依赖版本兼容**: 更新Sass到1.69.5版本，解决与Vite 7.2.6的兼容性问题
- **组件错误修复**: 修复用户管理组件中formatDateTime函数引用错误，替换为formatTime
- **配置优化**: 禁用SCSS预处理和代理配置，实现前端独立运行演示

### 技术细节
- **问题根因**: sass.initAsyncCompiler is not a function (Sass版本兼容性问题)
- **解决方案**: 使用纯CSS配置，排除Sass依赖，绕过预处理错误
- **部署地址**: http://localhost:5173/ (本地演示环境)

### 文件变更
- `Admin/frontend/vite.config.simple.js`: 新增简化版Vite配置
- `Admin/frontend/vite.config.demo.js`: 新增演示版配置
- `Admin/frontend/src/views/user/index.vue`: 修复函数引用错误
- `Admin/frontend/package.json`: 更新Sass依赖版本

## 版本 0.1.0 (2025-12-16)
# 初始版本 - 技术咨询业务文档体系建立

### 主要功能
- **文档组织**: 创建技术咨询业务文档体系文件夹结构
- **项目初始化**: 建立完整的商业运营管理平台基础架构
- **功能模块**: 包含B2C商城、B2B批发、SCRM客户管理等核心模块

---
*文档维护说明: 每次版本更新需按此模板添加记录，确保版本历史清晰可追溯*