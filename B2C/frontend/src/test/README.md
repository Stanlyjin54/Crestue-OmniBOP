# B2C前端测试文档

## 测试架构

### 1. 测试框架
- **测试运行器**: Vitest
- **UI测试**: Vue Test Utils
- **覆盖率**: @vitest/coverage-v8
- **UI界面**: @vitest/ui

### 2. 测试分类

#### 单元测试 (Unit Tests)
- **工具函数测试**: `src/test/utils/`
  - `index.test.ts` - 基础工具函数测试
  - `performance.test.ts` - 性能优化工具测试
- **组件测试**: `src/test/components/`
  - `LoadingSpinner.test.ts` - 加载动画组件测试
  - `ErrorState.test.ts` - 错误状态组件测试
  - `ProductCard.test.ts` - 商品卡片组件测试

#### 集成测试 (Integration Tests)
- **页面集成测试**: `src/test/views/`
  - `Products.test.ts` - 商品列表页面测试
  - `Home.test.ts` - 首页测试
- **性能集成测试**: `src/test/integration/`
  - `performance.integration.test.ts` - 性能工具集成测试

### 3. 测试配置

#### Vitest配置 (`vitest.config.ts`)
```typescript
export default defineConfig({
  plugins: [vue()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.ts'],
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      thresholds: {
        lines: 80,
        functions: 80,
        branches: 70,
        statements: 80
      }
    }
  }
})
```

#### 测试环境设置 (`src/test/setup.ts`)
- Vue Router Mock
- Element Plus Mock
- localStorage/sessionStorage Mock
- IntersectionObserver Mock
- 全局错误处理

### 4. 测试工具函数 (`src/test/utils/testHelpers.ts`)
- `createTestPinia()` - 创建测试用的Pinia实例
- `generateMockProducts()` - 生成测试商品数据
- `generateMockCategories()` - 生成测试分类数据
- `mockLocalStorage()` - 模拟localStorage
- `testUtils` - 常用测试工具函数

### 5. 运行测试

#### 基本命令
```bash
# 运行所有测试
npm run test

# 运行测试并监听变化
npm run test:watch

# 运行测试并生成覆盖率报告
npm run test:coverage

# 打开测试UI界面
npm run test:ui
```

#### 分类运行
```bash
# 仅运行组件测试
npm run test:components

# 仅运行页面测试
npm run test:views

# 仅运行工具函数测试
npm run test:utils

# 仅运行集成测试
npm run test:integration
```

### 6. 测试覆盖率目标
- **行覆盖率**: ≥80%
- **函数覆盖率**: ≥80%
- **分支覆盖率**: ≥70%
- **语句覆盖率**: ≥80%

### 7. 关键测试场景

#### 性能优化测试
- 缓存机制测试
- 防抖节流函数测试
- 网络状态检测测试
- 图片懒加载测试

#### 组件功能测试
- 加载状态显示测试
- 错误状态处理测试
- 商品卡片交互测试
- 响应式布局测试

#### 页面集成测试
- 商品列表筛选测试
- 分页功能测试
- 购物车操作测试
- 网络状态变化测试

### 8. 持续集成
测试将集成到CI/CD流程中，确保代码质量和功能稳定性。