// API测试工具 - 用于前后端联调测试
const API_BASE_URL = 'http://localhost:6789';

// 模拟响应数据
const mockResponses = {
  '/api/procurement/crawler/statistics': {
    code: 0,
    message: 'success',
    data: {
      totalCrawled: 1250,
      successCount: 1180,
      failedCount: 70,
      todayCrawled: 45,
      activeTasks: 3,
      complianceRate: 94.4
    }
  },
  '/api/procurement/list': {
    code: 0,
    message: 'success',
    data: {
      records: [
        {
          id: 1,
          procurementCode: 'CG2024001',
          title: '办公用品采购项目',
          region: '北京市',
          procurementType: '货物类',
          budgetAmount: 500000,
          publishDate: '2024-01-15',
          deadline: '2024-02-15',
          status: '进行中'
        },
        {
          id: 2,
          procurementCode: 'CG2024002',
          title: 'IT设备采购项目',
          region: '上海市',
          procurementType: '服务类',
          budgetAmount: 1200000,
          publishDate: '2024-01-20',
          deadline: '2024-02-20',
          status: '进行中'
        }
      ],
      total: 2,
      current: 1,
      size: 10
    }
  },
  '/api/social-media/douyin/analyze': {
    code: 0,
    message: 'success',
    data: {
      contentId: 'dy_123456',
      platform: 'douyin',
      businessOpportunities: [
        {
          type: 'product_demand',
          description: '用户对高端护肤品的需求较高',
          confidence: 0.85,
          keywords: ['护肤品', '高端', '需求']
        }
      ],
      brandMentions: ['兰蔻', '雅诗兰黛'],
      commercialValue: 0.78,
      trendingTopics: ['美妆护肤', '高端产品']
    }
  },
  '/api/social-media/compliance/check': {
    code: 0,
    message: 'success',
    data: {
      isCompliant: true,
      riskLevel: 'low',
      issues: [],
      suggestions: ['内容符合平台规范'],
      checkItems: {
        sensitiveWords: { passed: true },
        adContent: { passed: true },
        copyright: { passed: true },
        platformRules: { passed: true }
      }
    }
  }
};

// API测试类
class APITestClient {
  constructor() {
    this.baseURL = API_BASE_URL;
    this.useMock = true; // 默认使用模拟数据
  }

  // 设置是否使用模拟数据
  setUseMock(useMock) {
    this.useMock = useMock;
  }

  // 基础请求方法
  async request(method, url, data = null, params = null) {
    if (this.useMock && mockResponses[url]) {
      console.log(`[MOCK] ${method} ${url}`, data || params);
      return new Promise(resolve => {
        setTimeout(() => {
          resolve(mockResponses[url]);
        }, 500);
      });
    }

    try {
      const options = {
        method: method,
        headers: {
          'Content-Type': 'application/json',
        }
      };

      if (data) {
        options.body = JSON.stringify(data);
      }

      let fullUrl = this.baseURL + url;
      if (params) {
        const queryString = new URLSearchParams(params).toString();
        fullUrl += (fullUrl.includes('?') ? '&' : '?') + queryString;
      }

      console.log(`[REAL] ${method} ${fullUrl}`, data || params);
      const response = await fetch(fullUrl, options);
      const result = await response.json();
      
      return result;
    } catch (error) {
      console.error(`[ERROR] ${method} ${url}:`, error);
      return {
        code: -1,
        message: `网络错误: ${error.message}`,
        data: null
      };
    }
  }

  // 政府采购相关API测试
  async testProcurementAPIs() {
    console.log('🧪 开始测试政府采购API...');
    
    const tests = [
      {
        name: '获取采购列表',
        method: 'GET',
        url: '/api/procurement/list',
        params: { current: 1, pageSize: 10, keyword: '测试' }
      },
      {
        name: '获取采集统计',
        method: 'GET',
        url: '/api/procurement/crawler/statistics'
      }
    ];

    const results = [];
    
    for (const test of tests) {
      try {
        console.log(`  📋 ${test.name}`);
        const result = await this.request(test.method, test.url, null, test.params);
        
        const success = result && result.code === 0;
        results.push({
          name: test.name,
          success: success,
          result: result
        });
        
        console.log(`    ${success ? '✅' : '❌'} ${test.name}: ${success ? '成功' : '失败'}`);
        if (!success) {
          console.log(`    💬 错误信息: ${result.message}`);
        }
      } catch (error) {
        console.error(`    ❌ ${test.name}: 异常 - ${error.message}`);
        results.push({
          name: test.name,
          success: false,
          error: error.message
        });
      }
    }
    
    return results;
  }

  // 社交媒体相关API测试
  async testSocialMediaAPIs() {
    console.log('🧪 开始测试社交媒体API...');
    
    const tests = [
      {
        name: '抖音内容分析',
        method: 'POST',
        url: '/api/social-media/douyin/analyze',
        data: {
          content: '这是一个测试的抖音视频内容，包含产品信息',
          platform: 'douyin'
        }
      },
      {
        name: '内容合规检查',
        method: 'POST',
        url: '/api/social-media/compliance/check',
        data: {
          content: '这是一个测试内容，用于合规性检查',
          platform: 'douyin'
        }
      }
    ];

    const results = [];
    
    for (const test of tests) {
      try {
        console.log(`  📋 ${test.name}`);
        const result = await this.request(test.method, test.url, test.data);
        
        const success = result && result.code === 0;
        results.push({
          name: test.name,
          success: success,
          result: result
        });
        
        console.log(`    ${success ? '✅' : '❌'} ${test.name}: ${success ? '成功' : '失败'}`);
        if (!success) {
          console.log(`    💬 错误信息: ${result.message}`);
        }
      } catch (error) {
        console.error(`    ❌ ${test.name}: 异常 - ${error.message}`);
        results.push({
          name: test.name,
          success: false,
          error: error.message
        });
      }
    }
    
    return results;
  }

  // 运行完整的API测试
  async runFullTest() {
    console.log('🔍 开始前后端API联调测试...');
    console.log(`📡 后端服务地址: ${this.baseURL}`);
    console.log(`🎯 测试模式: ${this.useMock ? '模拟数据' : '真实后端'}`);
    console.log('');

    const procurementResults = await this.testProcurementAPIs();
    console.log('');
    
    const socialMediaResults = await this.testSocialMediaAPIs();
    console.log('');

    // 生成测试报告
    const allResults = [...procurementResults, ...socialMediaResults];
    const successCount = allResults.filter(r => r.success).length;
    const totalCount = allResults.length;
    
    console.log('📊 测试报告:');
    console.log(`  总测试数: ${totalCount}`);
    console.log(`  成功数: ${successCount}`);
    console.log(`  失败数: ${totalCount - successCount}`);
    console.log(`  成功率: ${((successCount / totalCount) * 100).toFixed(1)}%`);
    
    if (successCount === totalCount) {
      console.log('🎉 所有API测试通过！');
    } else {
      console.log('⚠️  部分API测试失败，请检查后端服务');
    }

    return {
      total: totalCount,
      success: successCount,
      failed: totalCount - successCount,
      successRate: (successCount / totalCount) * 100,
      results: allResults
    };
  }
}

// 创建全局测试实例
window.apiTestClient = new APITestClient();

// 导出测试函数
export default {
  // 运行模拟测试
  runMockTest() {
    console.log('🚀 开始模拟API测试...');
    return window.apiTestClient.runFullTest();
  },
  
  // 运行真实后端测试
  async runRealTest() {
    console.log('🚀 开始真实后端API测试...');
    window.apiTestClient.setUseMock(false);
    return await window.apiTestClient.runFullTest();
  },
  
  // 测试连接性
  async testConnection() {
    try {
      const response = await fetch(`${this.baseUrl}/api/test/connection`);
      const data = await response.json();
      return response.ok && data.code === 0;
    } catch (error) {
      console.warn('连接测试失败:', error);
      return false;
    }
  }
};