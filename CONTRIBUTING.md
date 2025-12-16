# 🤝 贡献指南

感谢您对 Crestue Omni-BOP 项目的关注！我们欢迎各种形式的贡献，无论是代码、文档、测试还是问题反馈。

## 📋 贡献类型

### 1. 代码贡献
- 新功能开发
- Bug修复
- 性能优化
- 代码重构

### 2. 文档贡献
- 用户手册完善
- API文档编写
- 部署指南更新
- 翻译工作

### 3. 测试贡献
- 单元测试编写
- 集成测试
- 性能测试
- 安全测试

### 4. 问题反馈
- Bug报告
- 功能建议
- 使用体验反馈

## 🚀 开始贡献

### 环境准备

1. **Fork 项目**
   ```bash
   # 在 GitHub 上 Fork 本项目
   # 然后克隆到本地
   git clone https://github.com/your-username/omni-bop.git
   cd omni-bop
   ```

2. **设置上游仓库**
   ```bash
   git remote add upstream https://github.com/crestue/omni-bop.git
   ```

3. **安装开发依赖**
   ```bash
   # 后端依赖
   cd Application/backend
   mvn clean install
   
   # 前端依赖
   cd ../../B2C/frontend
   npm install
   
   cd ../../Application/frontend
   npm install
   ```

### 开发流程

1. **同步最新代码**
   ```bash
   git checkout main
   git fetch upstream
   git merge upstream/main
   ```

2. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   # 或
   git checkout -b fix/issue-number
   ```

3. **开发与测试**
   ```bash
   # 后端测试
   cd Application/backend
   mvn test
   
   # 前端测试
   cd ../../B2C/frontend
   npm run test:unit
   ```

4. **提交代码**
   ```bash
   git add .
   git commit -m "feat: 添加新功能描述"
   git push origin feature/your-feature-name
   ```

5. **创建 Pull Request**
   - 在 GitHub 上创建 PR
   - 填写详细的描述信息
   - 关联相关 Issue

## 📝 代码规范

### 提交信息规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```bash
# 格式：<type>(<scope>): <description>

# 示例：
feat(api): 添加用户注册接口
fix(auth): 修复登录验证漏洞
docs(readme): 更新安装说明
style(css): 调整按钮样式
refactor(order): 重构订单处理逻辑
test(user): 添加用户服务单元测试
chore(deps): 更新依赖版本
```

### 代码风格

#### Java 代码规范
```java
/**
 * 用户服务接口
 * 
 * @author Your Name
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
@Slf4j
public class UserService {
    
    /**
     * 根据ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户实体
     * @throws UserNotFoundException 用户不存在时抛出
     */
    public User getUserById(Long userId) throws UserNotFoundException {
        // 方法实现
    }
}
```

#### Vue/TypeScript 代码规范
```typescript
/**
 * 用户列表组件
 * 
 * @description 显示用户列表和操作
 * @author Your Name
 * @version 1.0.0
 */
@Component
export default class UserList extends Vue {
    
    /** 用户列表数据 */
    @Prop({ type: Array, default: () => [] })
    private readonly users!: User[]
    
    /**
     * 删除用户
     * 
     * @param userId 用户ID
     */
    private async deleteUser(userId: number): Promise<void> {
        // 方法实现
    }
}
```

### 测试规范

#### 单元测试示例
```java
/**
 * 用户服务测试类
 */
@SpringBootTest
class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    @DisplayName("根据ID获取用户 - 用户存在")
    void testGetUserById_WhenUserExists_ShouldReturnUser() {
        // 准备测试数据
        Long userId = 1L;
        
        // 执行测试
        User user = userService.getUserById(userId);
        
        // 验证结果
        assertNotNull(user);
        assertEquals(userId, user.getId());
    }
}
```

## 🔍 问题报告

### Bug 报告模板
```markdown
## 问题描述
清晰描述遇到的问题

## 重现步骤
1. 第一步
2. 第二步
3. ...

## 期望行为
描述期望的正常行为

## 实际行为
描述实际发生的异常行为

## 环境信息
- 操作系统：
- 浏览器：
- 版本：
- 其他相关信息：

## 截图/日志
如有必要，提供相关截图或日志
```

### 功能建议模板
```markdown
## 功能描述
详细描述建议的功能

## 使用场景
说明该功能的使用场景和价值

## 实现建议
如有实现思路，可以在此描述

## 相关功能
说明与现有功能的关联性
```

## 🏆 贡献者权益

### 贡献者名单
所有贡献者将被列入项目的贡献者名单中。

### 特殊权益
- 活跃贡献者可获得项目维护者权限
- 重大贡献者可参与项目 roadmap 制定
- 优秀贡献者将获得项目周边礼品

### 行为准则
我们遵守 [Contributor Covenant](https://www.contributor-covenant.org/) 行为准则，确保社区友好和包容。

## ❓ 常见问题

### Q: 如何选择要贡献的功能？
A: 可以查看项目的 [Issues](https://github.com/crestue/omni-bop/issues) 页面，选择标记为 `good first issue` 或 `help wanted` 的 Issue。

### Q: 贡献代码需要签署 CLA 吗？
A: 目前不需要签署 CLA，但请确保您拥有提交代码的版权。

### Q: 如何获取开发帮助？
A: 可以通过以下方式获取帮助：
- GitHub Discussions
- 项目文档
- 社区聊天群组

### Q: 我的 Pull Request 多久会被审核？
A: 我们会在 3-5 个工作日内审核 PR，重大功能可能需要更长时间。

## 📞 联系我们

如有任何问题，可以通过以下方式联系我们：

- 📧 Email: dev@crestue.com
- 💬 GitHub Discussions
- 🐛 GitHub Issues

---

感谢您的贡献！让我们一起打造更好的 Crestue Omni-BOP！ 🚀