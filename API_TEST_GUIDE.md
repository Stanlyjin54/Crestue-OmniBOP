# API测试功能使用指南

## 快速开始

### 1. 启动模拟服务器
```bash
cd d:\qqiuscrm
node mock-server.js
```

### 2. 启动前端开发服务器
```bash
cd d:\qqiuscrm\Application\frontend
npm run dev
# 或
npx uni dev
```

### 3. 访问测试页面
打开浏览器访问: http://localhost:5173/pages/api-test

## API测试页面功能

### 界面说明
- **连接状态**: 显示后端服务连接状态
- **测试模式**: 显示当前测试模式（真实后端/模拟数据）
- **政府采购API**: 测试政府采购相关接口
- **社交媒体API**: 测试社交媒体分析接口
- **完整测试**: 一键运行所有API测试

### 测试按钮
- **测试采购列表**: 测试政府采购项目列表接口
- **测试采集统计**: 测试采集统计数据接口
- **测试内容分析**: 测试抖音内容分析接口
- **测试合规检查**: 测试内容合规性检查接口
- **运行完整测试**: 运行所有API测试

## 程序化测试

### 使用API测试客户端
```javascript
// 在浏览器控制台中使用
const client = window.apiTestClient;

// 测试连接
const connected = await client.testConnection();
console.log('连接状态:', connected);

// 运行模拟测试
const mockResult = await client.runMockTest();
console.log('模拟测试结果:', mockResult);

// 运行真实测试
const realResult = await client.runRealTest();
console.log('真实测试结果:', realResult);

// 单独测试某个API
const result = await client.request('POST', '/api/procurement/list', {
  current: 1,
  pageSize: 10,
  keyword: '测试'
});
console.log('采购列表结果:', result);
```

### 测试政府采购API
```javascript
// 测试采购列表
const procurementResult = await client.request('POST', '/api/procurement/list', {
  current: 1,
  pageSize: 10,
  keyword: '政府采购'
});

// 测试采集统计
const statsResult = await client.request('GET', '/api/procurement/crawler/statistics');
```

### 测试社交媒体API
```javascript
// 测试内容分析
const analysisResult = await client.request('POST', '/api/social-media/douyin/analyze', {
  content: '这是一个测试的抖音视频内容',
  platform: 'douyin'
});

// 测试合规检查
const complianceResult = await client.request('POST', '/api/social-media/compliance/check', {
  content: '测试内容合规性',
  platform: 'douyin'
});
```

## 模拟数据说明

### 采购列表数据
- 返回2条模拟采购项目
- 包含项目标题、内容、预算、地区等信息
- 支持分页参数

### 采集统计数据
- 总采集数: 1250
- 今日采集: 45
- 总商机数: 320
- 成功率: 92%

### 社交媒体分析数据
- 产品提及: 产品A、产品B
- 情感分析: positive
- 参与度: 85
- 商业价值: high

### 合规检查数据
- 合规状态: 合规
- 风险等级: low
- 合规评分: 95分

## 故障排除

### 连接失败
1. 检查模拟服务器是否启动
2. 确认端口6789未被占用
3. 检查防火墙设置

### API请求失败
1. 检查请求方法和参数
2. 查看浏览器控制台错误信息
3. 确认CORS设置正确

### 测试页面无法访问
1. 确认前端服务器正常运行
2. 检查路由配置是否正确
3. 查看pages.json中的页面配置

## 切换到真实后端

当真实后端配置完成后，修改前端环境变量：

### 修改环境变量
编辑 `d:\qqiuscrm\Application\frontend\.env.development`:
```
VITE_APP_SERVICE_API = 'http://localhost:6789'
```

### 启动真实后端
```bash
cd d:\qqiuscrm\Application\backend
# 确保Java环境已配置
mvn spring-boot:run
```

### 验证连接
访问测试页面，系统会自动检测后端连接并切换到真实模式。

## 高级功能

### 自定义测试数据
修改 `d:\qqiuscrm\mock-server.js` 中的 `mockData` 对象：
```javascript
const mockData = {
  // 添加自定义数据
  yourCustomEndpoint: {
    code: 0,
    message: 'success',
    data: { /* 自定义数据 */ }
  }
};
```

### 添加新API端点
在 `mock-server.js` 的路由处理部分添加：
```javascript
if (path === '/api/your/endpoint' && method === 'POST') {
  responseData = mockData.yourCustomEndpoint;
}
```

### 扩展测试客户端
在 `api-test.js` 中添加新的测试方法：
```javascript
async testYourCustomAPI() {
  return await this.request('POST', '/api/your/endpoint', data);
}
```

## 最佳实践

1. **定期测试**: 建议每次代码修改后运行API测试
2. **监控性能**: 关注API响应时间
3. **错误处理**: 完善错误处理和用户提示
4. **文档维护**: 及时更新API文档
5. **版本控制**: 使用版本控制管理测试代码

---

**创建时间**: 2024年1月15日  
**最后更新**: 2024年1月15日