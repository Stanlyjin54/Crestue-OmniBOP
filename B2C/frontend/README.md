# B2C商城前端项目

基于 Vue 3 + TypeScript + Element Plus 的现代化 B2C 商城前端项目。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - JavaScript 的超集，添加了类型系统
- **Vite** - 下一代前端构建工具
- **Element Plus** - 基于 Vue 3 的组件库
- **Pinia** - Vue 状态管理工具
- **Vue Router** - Vue.js 官方路由管理器
- **Tailwind CSS** - 实用优先的 CSS 框架
- **SCSS** - CSS 预处理器

## 功能特性

### 已完成功能
- ✅ 项目基础架构搭建
- ✅ 路由配置和权限控制
- ✅ 状态管理（用户、商品、购物车）
- ✅ 全局样式和主题配置
- ✅ API 请求封装
- ✅ 首页展示（轮播图、分类导航、商品推荐）
- ✅ 购物车功能（添加、删除、修改数量、结算）
- ✅ 响应式布局设计

### 待开发功能
- 🔄 商品详情页面
- 🔄 商品分类和搜索
- 🔄 用户登录注册
- 🔄 订单管理流程
- 🔄 支付集成
- 🔄 个人中心

## 项目结构

```
src/
├── api/              # API 接口定义
├── assets/           # 静态资源
├── components/       # 通用组件
├── router/           # 路由配置
├── stores/           # 状态管理
├── styles/           # 全局样式
├── types/            # TypeScript 类型定义
├── utils/            # 工具函数
├── views/            # 页面组件
└── main.ts           # 应用入口
```

## 快速开始

### 安装依赖
```bash
npm install
```

### 开发环境
```bash
npm run dev
```

### 构建项目
```bash
npm run build
```

### 预览构建结果
```bash
npm run preview
```

## 开发规范

### 组件命名
- 组件文件名使用 PascalCase
- 组件内部使用 `<script setup>` 语法糖
- 组件 props 使用 TypeScript 类型定义

### 状态管理
- 使用 Pinia 进行状态管理
- 每个模块对应一个 store 文件
- 状态修改通过 actions 进行

### API 请求
- 统一使用封装的 request 函数
- API 函数放在对应的模块文件中
- 响应数据需要定义 TypeScript 类型

## 浏览器支持

- Chrome >= 87
- Firefox >= 78
- Safari >= 13
- Edge >= 88

## 许可证

MIT License