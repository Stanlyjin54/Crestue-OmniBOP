const http = require('http');
const url = require('url');

const PORT = 6789;

// 模拟数据
const mockData = {
  procurementList: {
    code: 0,
    message: 'success',
    data: {
      records: [
        {
          id: 1,
          title: '政府采购项目测试1',
          content: '这是一个测试的政府采购项目',
          publishTime: '2024-01-01',
          source: '中国政府采购网',
          category: '货物类',
          budget: 1000000,
          region: '北京市'
        },
        {
          id: 2,
          title: '政府采购项目测试2',
          content: '这是另一个测试的政府采购项目',
          publishTime: '2024-01-02',
          source: '中国政府采购网',
          category: '服务类',
          budget: 2000000,
          region: '上海市'
        }
      ],
      total: 2,
      current: 1,
      pageSize: 10
    }
  },
  
  crawlerStatistics: {
    code: 0,
    message: 'success',
    data: {
      totalCrawled: 1250,
      todayCrawled: 45,
      totalOpportunities: 320,
      successRate: 0.92,
      lastUpdateTime: '2024-01-15 14:30:00'
    }
  },
  
  socialMediaAnalysis: {
    code: 0,
    message: 'success',
    data: {
      content: '这是一个测试的抖音视频内容，包含产品信息',
      platform: 'douyin',
      analysis: {
        productMentions: ['产品A', '产品B'],
        sentiment: 'positive',
        engagement: 85,
        businessValue: 'high',
        keywords: ['产品', '测试', '抖音']
      }
    }
  },
  
  complianceCheck: {
    code: 0,
    message: 'success',
    data: {
      content: '这是一个测试内容，用于合规性检查',
      platform: 'douyin',
      compliance: {
        isCompliant: true,
        riskLevel: 'low',
        issues: [],
        suggestions: ['内容符合平台规范'],
        score: 95
      }
    }
  }
};

// 创建HTTP服务器
const server = http.createServer((req, res) => {
  // 设置CORS头
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
  
  // 处理OPTIONS请求
  if (req.method === 'OPTIONS') {
    res.writeHead(200);
    res.end();
    return;
  }
  
  const parsedUrl = url.parse(req.url, true);
  const path = parsedUrl.pathname;
  const method = req.method;
  
  console.log(`📡 收到请求: ${method} ${path}`);
  
  let body = '';
  req.on('data', chunk => {
    body += chunk.toString();
  });
  
  req.on('end', () => {
    let responseData = { code: 404, message: 'API 不存在' };
    
    try {
      // 路由处理
      if (path === '/api/procurement/list' && method === 'POST') {
        responseData = mockData.procurementList;
      } else if (path === '/api/procurement/crawler/statistics' && method === 'GET') {
        responseData = mockData.crawlerStatistics;
      } else if (path === '/api/social-media/douyin/analyze' && method === 'POST') {
        responseData = mockData.socialMediaAnalysis;
      } else if (path === '/api/social-media/compliance/check' && method === 'POST') {
        responseData = mockData.complianceCheck;
      } else if (path === '/api/test/connection' && method === 'GET') {
        responseData = { code: 0, message: '连接成功' };
      }
      
      // 添加随机延迟模拟真实网络
      const delay = Math.random() * 500 + 200;
      
      setTimeout(() => {
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify(responseData));
        console.log(`✅ 响应成功: ${method} ${path}`);
      }, delay);
      
    } catch (error) {
      console.error(`❌ 处理请求失败:`, error);
      res.writeHead(500, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ 
        code: 500, 
        message: '服务器内部错误',
        error: error.message 
      }));
    }
  });
});

server.listen(PORT, () => {
  console.log(`🚀 Mock服务器启动成功！`);
  console.log(`📡 监听端口: ${PORT}`);
  console.log(`🔗 测试地址: http://localhost:${PORT}`);
  console.log(`📋 可用API端点:`);
  console.log(`   - POST http://localhost:${PORT}/api/procurement/list`);
  console.log(`   - GET  http://localhost:${PORT}/api/procurement/crawler/statistics`);
  console.log(`   - POST http://localhost:${PORT}/api/social-media/douyin/analyze`);
  console.log(`   - POST http://localhost:${PORT}/api/social-media/compliance/check`);
  console.log(`   - GET  http://localhost:${PORT}/api/test/connection`);
});

// 优雅关闭
process.on('SIGINT', () => {
  console.log('\n👋 正在关闭服务器...');
  server.close(() => {
    console.log('✅ 服务器已关闭');
    process.exit(0);
  });
});