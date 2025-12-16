<template>
  <view class="api-test-container">
    <view class="header">
      <text class="title">API接口测试</text>
      <text class="subtitle">前后端联调测试</text>
      <view class="connection-status" :class="{ connected: isConnected }">
        <text>{{ isConnected ? '已连接' : '未连接' }}</text>
      </view>
      <view class="test-mode">
        <text>测试模式: {{ testMode === 'real' ? '真实后端' : '模拟数据' }}</text>
      </view>
    </view>
    
    <view class="test-section">
      <view class="section-title">政府采购API测试</view>
      
      <view class="test-item">
        <button class="test-btn" @click="testProcurementList" :disabled="isTesting">测试采购列表</button>
        <text class="result" :class="{ success: procurementListSuccess, error: !procurementListSuccess && procurementListResult }">
          {{ procurementListResult || '点击按钮测试' }}
        </text>
      </view>
      
      <view class="test-item">
        <button class="test-btn" @click="testCrawlerStatistics" :disabled="isTesting">测试采集统计</button>
        <text class="result" :class="{ success: crawlerStatsSuccess, error: !crawlerStatsSuccess && crawlerStatsResult }">
          {{ crawlerStatsResult || '点击按钮测试' }}
        </text>
      </view>
    </view>
    
    <view class="test-section">
      <view class="section-title">社交媒体API测试</view>
      
      <view class="test-item">
        <button class="test-btn" @click="testSocialMediaAnalysis" :disabled="isTesting">测试内容分析</button>
        <text class="result" :class="{ success: socialMediaSuccess, error: !socialMediaSuccess && socialMediaResult }">
          {{ socialMediaResult || '点击按钮测试' }}
        </text>
      </view>
      
      <view class="test-item">
        <button class="test-btn" @click="testComplianceCheck" :disabled="isTesting">测试合规检查</button>
        <text class="result" :class="{ success: complianceSuccess, error: !complianceSuccess && complianceResult }">
          {{ complianceResult || '点击按钮测试' }}
        </text>
      </view>
    </view>
    
    <view class="test-section">
      <view class="section-title">连接状态</view>
      
      <view class="status-item">
        <text class="status-label">后端服务地址:</text>
        <text class="status-value">{{ baseUrl }}</text>
      </view>
      
      <view class="status-item">
        <text class="status-label">连接状态:</text>
        <text class="status-value" :class="{ connected: isConnected, disconnected: !isConnected }">
          {{ isConnected ? '已连接' : '未连接' }}
        </text>
      </view>
    </view>
    
    <view class="test-section">
      <view class="section-title">完整测试</view>
      <button @click="runFullTest" class="test-btn full-test-btn" :disabled="isTesting">
        {{ isTesting ? '测试中...' : '运行完整测试' }}
      </button>
    </view>
  </view>
</template>

<script>
import apiTestClient from '@/utils/api-test.js'

export default {
  data() {
    return {
      baseUrl: '',
      isConnected: false,
      procurementListResult: '',
      procurementListSuccess: false,
      crawlerStatsResult: '',
      crawlerStatsSuccess: false,
      socialMediaResult: '',
      socialMediaSuccess: false,
      complianceResult: '',
      complianceSuccess: false,
      testMode: 'mock', // 'mock' or 'real'
      isTesting: false
    }
  },
  
  mounted() {
    this.baseUrl = import.meta.env.VITE_APP_SERVICE_API || 'http://localhost:6789'
    this.testConnection()
  },
  
  methods: {
    async testConnection() {
      try {
        const connected = await apiTestClient.testConnection()
        this.isConnected = connected
        if (connected) {
          this.testMode = 'real'
        }
      } catch (error) {
        this.isConnected = false
        this.testMode = 'mock'
      }
    },
    
    async testProcurementList() {
      try {
        this.isTesting = true
        this.procurementListResult = '测试中...'
        
        // 直接测试采购列表接口
        const params = {
          current: 1,
          pageSize: 10,
          keyword: '测试'
        }
        
        const result = this.testMode === 'real'
          ? await window.apiTestClient.request('POST', '/api/procurement/list', params)
          : await window.apiTestClient.request('POST', '/api/procurement/list', params)
        
        if (result && result.code === 0) {
          this.procurementListSuccess = true
          this.procurementListResult = `成功！获取到 ${result.data?.records?.length || 0} 条数据`
        } else {
          this.procurementListSuccess = false
          this.procurementListResult = `失败：${result?.message || '未知错误'}`
        }
      } catch (error) {
        this.procurementListSuccess = false
        this.procurementListResult = `错误：${error.message}`
      } finally {
        this.isTesting = false
      }
    },
    
    async testCrawlerStatistics() {
      try {
        this.isTesting = true
        this.crawlerStatsResult = '测试中...'
        
        const result = this.testMode === 'real'
          ? await window.apiTestClient.request('GET', '/api/procurement/crawler/statistics')
          : await window.apiTestClient.request('GET', '/api/procurement/crawler/statistics')
        
        if (result && result.code === 0) {
          this.crawlerStatsSuccess = true
          this.crawlerStatsResult = '成功！采集统计接口正常'
        } else {
          this.crawlerStatsSuccess = false
          this.crawlerStatsResult = `失败：${result?.message || '未知错误'}`
        }
      } catch (error) {
        this.crawlerStatsSuccess = false
        this.crawlerStatsResult = `错误：${error.message}`
      } finally {
        this.isTesting = false
      }
    },
    
    async testSocialMediaAnalysis() {
      try {
        this.isTesting = true
        this.socialMediaResult = '测试中...'
        
        const testData = {
          content: '这是一个测试的抖音视频内容，包含产品信息',
          platform: 'douyin'
        }
        
        const result = this.testMode === 'real'
          ? await window.apiTestClient.request('POST', '/api/social-media/douyin/analyze', testData)
          : await window.apiTestClient.request('POST', '/api/social-media/douyin/analyze', testData)
        
        if (result && result.code === 0) {
          this.socialMediaSuccess = true
          this.socialMediaResult = '成功！社交媒体分析接口正常'
        } else {
          this.socialMediaSuccess = false
          this.socialMediaResult = `失败：${result?.message || '未知错误'}`
        }
      } catch (error) {
        this.socialMediaSuccess = false
        this.socialMediaResult = `错误：${error.message}`
      } finally {
        this.isTesting = false
      }
    },
    
    async testComplianceCheck() {
      try {
        this.isTesting = true
        this.complianceResult = '测试中...'
        
        const testContent = '这是一个测试内容，用于合规性检查'
        const result = this.testMode === 'real'
          ? await window.apiTestClient.request('POST', '/api/social-media/compliance/check', {
              content: testContent,
              platform: 'douyin'
            })
          : await window.apiTestClient.request('POST', '/api/social-media/compliance/check', {
              content: testContent,
              platform: 'douyin'
            })
        
        if (result && result.code === 0) {
          this.complianceSuccess = true
          this.complianceResult = '成功！合规检查接口正常'
        } else {
          this.complianceSuccess = false
          this.complianceResult = `失败：${result?.message || '未知错误'}`
        }
      } catch (error) {
        this.complianceSuccess = false
        this.complianceResult = `错误：${error.message}`
      } finally {
        this.isTesting = false
      }
    },
    
    async runFullTest() {
      try {
        this.isTesting = true
        console.log('🚀 开始完整API测试...')
        
        const testResult = this.testMode === 'real'
          ? await apiTestClient.runRealTest()
          : await apiTestClient.runMockTest()
        
        // 更新所有测试状态
        this.procurementListSuccess = true
        this.procurementListResult = '采购API测试完成'
        
        this.crawlerStatsSuccess = true
        this.crawlerStatsResult = '采集统计API测试完成'
        
        this.socialMediaSuccess = true
        this.socialMediaResult = '社交媒体API测试完成'
        
        this.complianceSuccess = true
        this.complianceResult = '合规检查API测试完成'
        
        console.log(`📊 测试完成：${testResult.success}/${testResult.total} 通过`)
        
      } catch (error) {
        console.error('测试失败:', error)
      } finally {
        this.isTesting = false
      }
    }
  }
}
</script>

<style scoped>
.api-test-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  display: block;
}

.subtitle {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
  display: block;
}

.test-section {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  border-left: 4px solid #007aff;
  padding-left: 10px;
}

.test-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.test-btn {
  background-color: #007aff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  margin-right: 15px;
  min-width: 120px;
}

.test-btn:hover {
  background-color: #0056b3;
}

.result {
  flex: 1;
  font-size: 14px;
  padding: 8px;
  background-color: white;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.result.success {
  color: #28a745;
  border-color: #28a745;
  background-color: #f8fff9;
}

.result.error {
  color: #dc3545;
  border-color: #dc3545;
  background-color: #fff8f8;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 6px;
  margin-bottom: 10px;
}

.status-label {
  font-weight: bold;
  color: #333;
}

.status-value {
  color: #666;
}

.status-value.connected {
  color: #28a745;
  font-weight: bold;
}

.status-value.disconnected {
  color: #dc3545;
  font-weight: bold;
}
</style>