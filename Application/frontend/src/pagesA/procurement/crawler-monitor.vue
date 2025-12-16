<template>
  <view class="crawler-monitor">
    <!-- 页面头部 -->
    <view class="page-header">
      <view class="header-left">
        <text class="page-title">爬虫监控面板</text>
        <text class="page-subtitle">实时监控爬虫运行状态和采集进度</text>
      </view>
      <view class="header-actions">
        <button class="btn-primary" @click="refreshAll">
          <text class="iconfont icon-refresh"></text>
          刷新数据
        </button>
      </view>
    </view>

    <!-- 统计概览 -->
    <view class="stats-overview">
      <view class="stat-card">
        <view class="stat-icon running">
          <text class="iconfont icon-running"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.runningCrawlers || 0 }}</text>
          <text class="stat-label">运行中</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon success">
          <text class="iconfont icon-success"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.successCrawlers || 0 }}</text>
          <text class="stat-label">成功完成</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon failed">
          <text class="iconfont icon-failed"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.failedCrawlers || 0 }}</text>
          <text class="stat-label">失败任务</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon total">
          <text class="iconfont icon-total"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.totalDataCollected || 0 }}</text>
          <text class="stat-label">总采集数据</text>
        </view>
      </view>
    </view>

    <!-- 实时状态监控 -->
    <view class="monitor-section">
      <view class="section-header">
        <text class="section-title">实时状态监控</text>
        <view class="section-actions">
          <view class="status-filter">
            <text class="filter-label">状态筛选:</text>
            <picker 
              mode="selector" 
              :range="statusOptions" 
              :value="statusFilterIndex"
              @change="onStatusFilterChange"
            >
              <view class="filter-picker">
                <text>{{ statusOptions[statusFilterIndex] }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
          </view>
        </view>
      </view>
      
      <view class="crawler-status-grid">
        <view 
          class="crawler-status-card" 
          v-for="crawler in crawlerList" 
          :key="crawler.id"
          :class="getStatusClass(crawler.status)"
        >
          <view class="card-header">
            <view class="header-left">
              <text class="crawler-name">{{ crawler.name }}</text>
              <text class="crawler-type">{{ getTypeText(crawler.type) }}</text>
            </view>
            <view class="header-right">
              <text class="status-badge" :class="getStatusClass(crawler.status)">
                {{ getStatusText(crawler.status) }}
              </text>
            </view>
          </view>
          
          <view class="card-content">
            <view class="progress-section">
              <view class="progress-header">
                <text class="progress-label">采集进度</text>
                <text class="progress-text">{{ crawler.progress }}%</text>
              </view>
              <view class="progress-bar">
                <view class="progress-fill" :style="{ width: crawler.progress + '%' }"></view>
              </view>
            </view>
            
            <view class="stats-section">
              <view class="stat-item">
                <text class="stat-label">已采集</text>
                <text class="stat-value">{{ crawler.collectedCount }}</text>
              </view>
              <view class="stat-item">
                <text class="stat-label">总目标</text>
                <text class="stat-value">{{ crawler.totalTarget }}</text>
              </view>
              <view class="stat-item">
                <text class="stat-label">成功率</text>
                <text class="stat-value">{{ crawler.successRate }}%</text>
              </view>
            </view>
            
            <view class="time-section">
              <view class="time-item">
                <text class="time-label">开始时间</text>
                <text class="time-value">{{ formatDateTime(crawler.startTime) }}</text>
              </view>
              <view class="time-item">
                <text class="time-label">预计完成</text>
                <text class="time-value">{{ formatDateTime(crawler.estimatedEndTime) }}</text>
              </view>
            </view>
          </view>
          
          <view class="card-footer">
            <view class="footer-actions">
              <button 
                class="action-btn primary" 
                @click="viewCrawlerDetail(crawler)"
              >
                查看详情
              </button>
              <button 
                class="action-btn secondary" 
                @click="controlCrawler(crawler)"
                :disabled="crawler.status === 'completed' || crawler.status === 'failed'"
              >
                {{ getControlText(crawler.status) }}
              </button>
              <button 
                class="action-btn danger" 
                @click="restartCrawler(crawler)"
                :disabled="crawler.status === 'running'"
              >
                重启
              </button>
            </view>
          </view>
        </view>
      </view>
      
      <view class="empty-state" v-if="crawlerList.length === 0">
        <text class="empty-text">暂无爬虫任务</text>
        <button class="empty-btn" @click="createNewCrawler">创建新任务</button>
      </view>
    </view>

    <!-- 性能监控图表 -->
    <view class="performance-section">
      <view class="section-header">
        <text class="section-title">性能监控</text>
        <view class="chart-options">
          <picker 
            mode="selector" 
            :range="timeRangeOptions" 
            :value="timeRangeIndex"
            @change="onTimeRangeChange"
          >
            <view class="filter-picker">
              <text>{{ timeRangeOptions[timeRangeIndex] }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
      </view>
      
      <view class="performance-charts">
        <view class="chart-container">
          <view class="chart-header">
            <text class="chart-title">采集速率趋势</text>
            <text class="chart-value">{{ currentRate }} 条/分钟</text>
          </view>
          <view class="chart-content">
            <!-- 这里可以集成图表组件，如 ECharts -->
            <view class="chart-placeholder">
              <text class="placeholder-text">采集速率图表</text>
            </view>
          </view>
        </view>
        
        <view class="chart-container">
          <view class="chart-header">
            <text class="chart-title">成功率趋势</text>
            <text class="chart-value">{{ currentSuccessRate }}%</text>
          </view>
          <view class="chart-content">
            <view class="chart-placeholder">
              <text class="placeholder-text">成功率图表</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 错误日志监控 -->
    <view class="error-section">
      <view class="section-header">
        <text class="section-title">错误日志监控</text>
        <view class="section-actions">
          <button class="btn-clear" @click="clearErrorLogs">
            <text class="iconfont icon-clear"></text>
            清空日志
          </button>
        </view>
      </view>
      
      <view class="error-logs">
        <view 
          class="error-item" 
          v-for="(error, index) in errorLogs" 
          :key="index"
          :class="getErrorLevelClass(error.level)"
        >
          <view class="error-header">
            <text class="error-time">{{ formatDateTime(error.timestamp) }}</text>
            <text class="error-level" :class="getErrorLevelClass(error.level)">
              {{ getErrorLevelText(error.level) }}
            </text>
            <text class="error-crawler">{{ error.crawlerName }}</text>
          </view>
          <view class="error-content">
            <text class="error-message">{{ error.message }}</text>
            <text class="error-detail" v-if="error.detail">{{ error.detail }}</text>
          </view>
        </view>
        
        <view class="empty-state" v-if="errorLogs.length === 0">
          <text class="empty-text">暂无错误日志</text>
        </view>
      </view>
    </view>

    <!-- 系统资源监控 -->
    <view class="resource-section">
      <view class="section-header">
        <text class="section-title">系统资源监控</text>
      </view>
      
      <view class="resource-grid">
        <view class="resource-card">
          <view class="resource-header">
            <text class="resource-title">CPU使用率</text>
            <text class="resource-value">{{ systemResources.cpu }}%</text>
          </view>
          <view class="resource-bar">
            <view class="resource-fill" :style="{ width: systemResources.cpu + '%' }" :class="getResourceClass(systemResources.cpu)"></view>
          </view>
        </view>
        
        <view class="resource-card">
          <view class="resource-header">
            <text class="resource-title">内存使用率</text>
            <text class="resource-value">{{ systemResources.memory }}%</text>
          </view>
          <view class="resource-bar">
            <view class="resource-fill" :style="{ width: systemResources.memory + '%' }" :class="getResourceClass(systemResources.memory)"></view>
          </view>
        </view>
        
        <view class="resource-card">
          <view class="resource-header">
            <text class="resource-title">磁盘使用率</text>
            <text class="resource-value">{{ systemResources.disk }}%</text>
          </view>
          <view class="resource-bar">
            <view class="resource-fill" :style="{ width: systemResources.disk + '%' }" :class="getResourceClass(systemResources.disk)"></view>
          </view>
        </view>
        
        <view class="resource-card">
          <view class="resource-header">
            <text class="resource-title">网络状态</text>
            <text class="resource-value" :class="getNetworkClass(systemResources.network)">
              {{ systemResources.network ? '正常' : '异常' }}
            </text>
          </view>
          <view class="network-indicator" :class="getNetworkClass(systemResources.network)"></view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { onShow, onHide } from '@dcloudio/uni-app'
import {
  getCrawlerStatus,
  getCrawlerStatistics,
  getSystemResources,
  getErrorLogs,
  controlCrawlerTask,
  restartCrawlerTask,
  clearErrorLogs as clearLogs
} from '@/api/procurement/procurementCrawler.js'

// 状态筛选
const statusFilterIndex = ref(0)
const statusOptions = ['全部状态', '运行中', '已完成', '已暂停', '失败']

// 时间范围
const timeRangeIndex = ref(0)
const timeRangeOptions = ['最近1小时', '最近6小时', '最近24小时', '最近7天']

// 数据列表
const crawlerList = ref([])
const statistics = ref({
  runningCrawlers: 0,
  successCrawlers: 0,
  failedCrawlers: 0,
  totalDataCollected: 0
})

// 性能监控
const currentRate = ref(0)
const currentSuccessRate = ref(0)

// 错误日志
const errorLogs = ref([])

// 系统资源
const systemResources = ref({
  cpu: 0,
  memory: 0,
  disk: 0,
  network: true
})

// 定时器
let refreshTimer = null
let resourceTimer = null

// 页面加载时获取数据
onMounted(() => {
  loadCrawlerData()
  loadStatistics()
  loadErrorLogs()
  loadSystemResources()
  startAutoRefresh()
})

onShow(() => {
  startAutoRefresh()
})

onHide(() => {
  stopAutoRefresh()
})

onUnmounted(() => {
  stopAutoRefresh()
})

// 自动刷新
function startAutoRefresh() {
  refreshTimer = setInterval(() => {
    loadCrawlerData()
    loadStatistics()
    loadErrorLogs()
  }, 30000) // 30秒刷新一次
  
  resourceTimer = setInterval(() => {
    loadSystemResources()
  }, 5000) // 5秒刷新系统资源
}

function stopAutoRefresh() {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }
  if (resourceTimer) {
    clearInterval(resourceTimer)
    resourceTimer = null
  }
}

// 加载爬虫数据
async function loadCrawlerData() {
  try {
    const params = {
      status: statusFilterIndex.value > 0 ? getStatusValue(statusFilterIndex.value) : '',
      timeRange: getTimeRangeValue(timeRangeIndex.value)
    }
    
    const result = await getCrawlerStatus(params)
    if (result.success) {
      crawlerList.value = result.data || []
    } else {
      uni.showToast({
        title: result.message || '加载失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('加载爬虫数据失败:', error)
    uni.showToast({
      title: '加载失败',
      icon: 'none'
    })
  }
}

// 加载统计信息
async function loadStatistics() {
  try {
    const result = await getCrawlerStatistics()
    if (result.success) {
      statistics.value = result.data
      currentRate.value = result.data.currentRate || 0
      currentSuccessRate.value = result.data.currentSuccessRate || 0
    }
  } catch (error) {
    console.error('加载统计信息失败:', error)
  }
}

// 加载错误日志
async function loadErrorLogs() {
  try {
    const result = await getErrorLogs({
      timeRange: getTimeRangeValue(timeRangeIndex.value)
    })
    if (result.success) {
      errorLogs.value = result.data || []
    }
  } catch (error) {
    console.error('加载错误日志失败:', error)
  }
}

// 加载系统资源
async function loadSystemResources() {
  try {
    const result = await getSystemResources()
    if (result.success) {
      systemResources.value = result.data
    }
  } catch (error) {
    console.error('加载系统资源失败:', error)
  }
}

// 筛选器变更
function onStatusFilterChange(e) {
  statusFilterIndex.value = e.detail.value
  loadCrawlerData()
}

function onTimeRangeChange(e) {
  timeRangeIndex.value = e.detail.value
  loadCrawlerData()
  loadErrorLogs()
}

// 爬虫操作
function viewCrawlerDetail(crawler) {
  uni.navigateTo({
    url: `/pagesA/procurement/crawler-detail?id=${crawler.id}`
  })
}

async function controlCrawler(crawler) {
  try {
    const action = crawler.status === 'running' ? 'pause' : 'resume'
    const result = await controlCrawlerTask(crawler.id, action)
    if (result.success) {
      uni.showToast({
        title: getControlSuccessText(action),
        icon: 'success'
      })
      loadCrawlerData()
    } else {
      uni.showToast({
        title: result.message || '操作失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('控制爬虫失败:', error)
    uni.showToast({
      title: '操作失败',
      icon: 'none'
    })
  }
}

async function restartCrawler(crawler) {
  uni.showModal({
    title: '确认重启',
    content: '确定要重启这个爬虫任务吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          const result = await restartCrawlerTask(crawler.id)
          if (result.success) {
            uni.showToast({
              title: '重启成功',
              icon: 'success'
            })
            loadCrawlerData()
          } else {
            uni.showToast({
              title: result.message || '重启失败',
              icon: 'none'
            })
          }
        } catch (error) {
          console.error('重启爬虫失败:', error)
          uni.showToast({
            title: '重启失败',
            icon: 'none'
          })
        }
      }
    }
  })
}

function createNewCrawler() {
  uni.navigateTo({
    url: '/pagesA/procurement/crawler-create'
  })
}

async function clearErrorLogs() {
  uni.showModal({
    title: '确认清空',
    content: '确定要清空所有错误日志吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          const result = await clearLogs()
          if (result.success) {
            uni.showToast({
              title: '清空成功',
              icon: 'success'
            })
            loadErrorLogs()
          } else {
            uni.showToast({
              title: result.message || '清空失败',
              icon: 'none'
            })
          }
        } catch (error) {
          console.error('清空错误日志失败:', error)
          uni.showToast({
            title: '清空失败',
            icon: 'none'
          })
        }
      }
    }
  })
}

function refreshAll() {
  Promise.all([
    loadCrawlerData(),
    loadStatistics(),
    loadErrorLogs(),
    loadSystemResources()
  ]).then(() => {
    uni.showToast({
      title: '刷新成功',
      icon: 'success'
    })
  })
}

// 工具函数
function getStatusValue(index) {
  const statusValues = ['', 'running', 'completed', 'paused', 'failed']
  return statusValues[index]
}

function getTimeRangeValue(index) {
  const timeValues = ['1h', '6h', '24h', '7d']
  return timeValues[index]
}

function getStatusClass(status) {
  const statusMap = {
    'running': 'status-running',
    'completed': 'status-completed',
    'paused': 'status-paused',
    'failed': 'status-failed'
  }
  return statusMap[status] || 'status-paused'
}

function getStatusText(status) {
  const statusMap = {
    'running': '运行中',
    'completed': '已完成',
    'paused': '已暂停',
    'failed': '失败'
  }
  return statusMap[status] || '未知'
}

function getTypeText(type) {
  const typeMap = {
    'government': '政府采购',
    'tender': '招标信息',
    'procurement': '采购信息',
    'opportunity': '商机匹配'
  }
  return typeMap[type] || '未知'
}

function getControlText(status) {
  return status === 'running' ? '暂停' : '继续'
}

function getControlSuccessText(action) {
  const actionMap = {
    'pause': '暂停成功',
    'resume': '继续成功'
  }
  return actionMap[action] || '操作成功'
}

function getErrorLevelClass(level) {
  const levelMap = {
    'error': 'error-level',
    'warning': 'warning-level',
    'info': 'info-level'
  }
  return levelMap[level] || 'info-level'
}

function getErrorLevelText(level) {
  const levelMap = {
    'error': '错误',
    'warning': '警告',
    'info': '信息'
  }
  return levelMap[level] || '未知'
}

function getResourceClass(value) {
  if (value >= 80) return 'resource-high'
  if (value >= 60) return 'resource-medium'
  return 'resource-low'
}

function getNetworkClass(isNormal) {
  return isNormal ? 'network-normal' : 'network-error'
}

function formatDateTime(dateStr) {
  if (!dateStr) return '--'
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.crawler-monitor {
  padding: 20rpx;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
  
  .header-left {
    flex: 1;
    
    .page-title {
      display: block;
      font-size: 36rpx;
      font-weight: bold;
      color: #303133;
      margin-bottom: 8rpx;
    }
    
    .page-subtitle {
      display: block;
      font-size: 26rpx;
      color: #909399;
    }
  }
  
  .header-actions {
    .btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
      border: none;
      border-radius: 12rpx;
      padding: 20rpx 30rpx;
      font-size: 28rpx;
      display: flex;
      align-items: center;
      gap: 10rpx;
      
      .iconfont {
        font-size: 32rpx;
      }
    }
  }
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
  
  .stat-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx 20rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
    
    .stat-icon {
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
      
      &.running {
        background: linear-gradient(135deg, #52c41a 0%, #95de64 100%);
      }
      
      &.success {
        background: linear-gradient(135deg, #409eff 0%, #85c5ff 100%);
      }
      
      &.failed {
        background: linear-gradient(135deg, #f56c6c 0%, #fab6b6 100%);
      }
      
      &.total {
        background: linear-gradient(135deg, #e6a23c 0%, #f3d19e 100%);
      }
      
      .iconfont {
        font-size: 32rpx;
        color: #fff;
      }
    }
    
    .stat-content {
      flex: 1;
      
      .stat-number {
        display: block;
        font-size: 32rpx;
        font-weight: bold;
        color: #303133;
        margin-bottom: 8rpx;
      }
      
      .stat-label {
        display: block;
        font-size: 24rpx;
        color: #909399;
      }
    }
  }
}

.monitor-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
    
    .section-actions {
      .status-filter {
        display: flex;
        align-items: center;
        gap: 16rpx;
        
        .filter-label {
          font-size: 26rpx;
          color: #606266;
        }
        
        .filter-picker {
          display: flex;
          justify-content: space-between;
          align-items: center;
          min-width: 160rpx;
          height: 60rpx;
          border: 1rpx solid #dcdfe6;
          border-radius: 8rpx;
          padding: 0 20rpx;
          font-size: 26rpx;
          background: #fff;
          
          .picker-arrow {
            color: #909399;
            margin-left: 12rpx;
          }
        }
      }
    }
  }
  
  .crawler-status-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300rpx, 1fr));
    gap: 20rpx;
    
    .crawler-status-card {
      background: #f8f9fa;
      border-radius: 12rpx;
      padding: 24rpx;
      border: 2rpx solid transparent;
      transition: all 0.3s ease;
      
      &.status-running {
        border-color: #52c41a;
        background: linear-gradient(135deg, #f6ffed 0%, #fcffe6 100%);
      }
      
      &.status-completed {
        border-color: #409eff;
        background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
      }
      
      &.status-paused {
        border-color: #e6a23c;
        background: linear-gradient(135deg, #fdf6ec 0%, #fffbe6 100%);
      }
      
      &.status-failed {
        border-color: #f56c6c;
        background: linear-gradient(135deg, #fef0f0 0%, #fff1f0 100%);
      }
      
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
        
        .header-left {
          flex: 1;
          
          .crawler-name {
            display: block;
            font-size: 28rpx;
            font-weight: bold;
            color: #303133;
            margin-bottom: 8rpx;
          }
          
          .crawler-type {
            display: block;
            font-size: 24rpx;
            color: #909399;
          }
        }
        
        .header-right {
          .status-badge {
            padding: 8rpx 16rpx;
            border-radius: 20rpx;
            font-size: 22rpx;
            font-weight: bold;
            
            &.status-running {
              background: #f6ffed;
              color: #52c41a;
            }
            
            &.status-completed {
              background: #f0f9ff;
              color: #409eff;
            }
            
            &.status-paused {
              background: #fdf6ec;
              color: #e6a23c;
            }
            
            &.status-failed {
              background: #fef0f0;
              color: #f56c6c;
            }
          }
        }
      }
      
      .card-content {
        margin-bottom: 20rpx;
        
        .progress-section {
          margin-bottom: 20rpx;
          
          .progress-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12rpx;
            
            .progress-label {
              font-size: 26rpx;
              color: #606266;
            }
            
            .progress-text {
              font-size: 26rpx;
              font-weight: bold;
              color: #409eff;
            }
          }
          
          .progress-bar {
            height: 8rpx;
            background: #e4e7ed;
            border-radius: 4rpx;
            overflow: hidden;
            
            .progress-fill {
              height: 100%;
              background: linear-gradient(90deg, #409eff 0%, #85c5ff 100%);
              border-radius: 4rpx;
              transition: width 0.3s ease;
            }
          }
        }
        
        .stats-section {
          display: flex;
          justify-content: space-between;
          margin-bottom: 20rpx;
          
          .stat-item {
            text-align: center;
            
            .stat-label {
              display: block;
              font-size: 24rpx;
              color: #909399;
              margin-bottom: 8rpx;
            }
            
            .stat-value {
              display: block;
              font-size: 28rpx;
              font-weight: bold;
              color: #303133;
            }
          }
        }
        
        .time-section {
          .time-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 8rpx;
            
            &:last-child {
              margin-bottom: 0;
            }
            
            .time-label {
              font-size: 24rpx;
              color: #909399;
            }
            
            .time-value {
              font-size: 24rpx;
              color: #606266;
            }
          }
        }
      }
      
      .card-footer {
        .footer-actions {
          display: flex;
          gap: 12rpx;
          
          .action-btn {
            flex: 1;
            padding: 16rpx 20rpx;
            border-radius: 8rpx;
            font-size: 24rpx;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
            
            &.primary {
              background: #409eff;
              color: #fff;
            }
            
            &.secondary {
              background: #fff;
              color: #606266;
              border: 1rpx solid #dcdfe6;
            }
            
            &.danger {
              background: #fef0f0;
              color: #f56c6c;
              border: 1rpx solid #f56c6c;
            }
            
            &:disabled {
              opacity: 0.6;
              cursor: not-allowed;
            }
          }
        }
      }
    }
    
    .empty-state {
      text-align: center;
      padding: 60rpx 0;
      
      .empty-text {
        display: block;
        font-size: 28rpx;
        color: #909399;
        margin-bottom: 20rpx;
      }
      
      .empty-btn {
        background: #409eff;
        color: #fff;
        border: none;
        border-radius: 8rpx;
        padding: 16rpx 32rpx;
        font-size: 26rpx;
        cursor: pointer;
      }
    }
  }
}

.performance-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
    
    .chart-options {
      .filter-picker {
        display: flex;
        justify-content: space-between;
        align-items: center;
        min-width: 160rpx;
        height: 60rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 26rpx;
        background: #fff;
        
        .picker-arrow {
          color: #909399;
          margin-left: 12rpx;
        }
      }
    }
  }
  
  .performance-charts {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 30rpx;
    
    .chart-container {
      background: #f8f9fa;
      border-radius: 12rpx;
      padding: 24rpx;
      
      .chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
        
        .chart-title {
          font-size: 28rpx;
          font-weight: bold;
          color: #303133;
        }
        
        .chart-value {
          font-size: 28rpx;
          font-weight: bold;
          color: #409eff;
        }
      }
      
      .chart-content {
        .chart-placeholder {
          height: 200rpx;
          background: #e4e7ed;
          border-radius: 8rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          
          .placeholder-text {
            font-size: 24rpx;
            color: #909399;
          }
        }
      }
    }
  }
}

.error-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
    
    .section-actions {
      .btn-clear {
        background: #f56c6c;
        color: #fff;
        border: none;
        border-radius: 8rpx;
        padding: 16rpx 24rpx;
        font-size: 26rpx;
        display: flex;
        align-items: center;
        gap: 8rpx;
        cursor: pointer;
        
        .iconfont {
          font-size: 28rpx;
        }
      }
    }
  }
  
  .error-logs {
    max-height: 400rpx;
    overflow-y: auto;
    
    .error-item {
      background: #f8f9fa;
      border-radius: 8rpx;
      padding: 20rpx;
      margin-bottom: 16rpx;
      border-left: 4rpx solid transparent;
      
      &.error-level {
        border-left-color: #f56c6c;
        background: #fef0f0;
      }
      
      &.warning-level {
        border-left-color: #e6a23c;
        background: #fdf6ec;
      }
      
      &.info-level {
        border-left-color: #409eff;
        background: #f0f9ff;
      }
      
      .error-header {
        display: flex;
        align-items: center;
        gap: 16rpx;
        margin-bottom: 12rpx;
        
        .error-time {
          font-size: 24rpx;
          color: #909399;
        }
        
        .error-level {
          padding: 4rpx 12rpx;
          border-radius: 12rpx;
          font-size: 22rpx;
          font-weight: bold;
          
          &.error-level {
            background: #fef0f0;
            color: #f56c6c;
          }
          
          &.warning-level {
            background: #fdf6ec;
            color: #e6a23c;
          }
          
          &.info-level {
            background: #f0f9ff;
            color: #409eff;
          }
        }
        
        .error-crawler {
          font-size: 24rpx;
          color: #606266;
          font-weight: bold;
        }
      }
      
      .error-content {
        .error-message {
          display: block;
          font-size: 26rpx;
          color: #303133;
          margin-bottom: 8rpx;
        }
        
        .error-detail {
          display: block;
          font-size: 24rpx;
          color: #909399;
          font-family: monospace;
        }
      }
    }
    
    .empty-state {
      text-align: center;
      padding: 40rpx 0;
      
      .empty-text {
        font-size: 28rpx;
        color: #909399;
      }
    }
  }
}

.resource-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .section-header {
    margin-bottom: 30rpx;
    
    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
  }
  
  .resource-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 30rpx;
    
    .resource-card {
      background: #f8f9fa;
      border-radius: 12rpx;
      padding: 24rpx;
      
      .resource-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
        
        .resource-title {
          font-size: 28rpx;
          font-weight: bold;
          color: #303133;
        }
        
        .resource-value {
          font-size: 32rpx;
          font-weight: bold;
          
          &.resource-high {
            color: #f56c6c;
          }
          
          &.resource-medium {
            color: #e6a23c;
          }
          
          &.resource-low {
            color: #52c41a;
          }
          
          &.network-normal {
            color: #52c41a;
          }
          
          &.network-error {
            color: #f56c6c;
          }
        }
      }
      
      .resource-bar {
        height: 12rpx;
        background: #e4e7ed;
        border-radius: 6rpx;
        overflow: hidden;
        
        .resource-fill {
          height: 100%;
          border-radius: 6rpx;
          transition: width 0.3s ease;
          
          &.resource-high {
            background: linear-gradient(90deg, #f56c6c 0%, #fab6b6 100%);
          }
          
          &.resource-medium {
            background: linear-gradient(90deg, #e6a23c 0%, #f3d19e 100%);
          }
          
          &.resource-low {
            background: linear-gradient(90deg, #52c41a 0%, #95de64 100%);
          }
        }
      }
      
      .network-indicator {
        width: 24rpx;
        height: 24rpx;
        border-radius: 50%;
        
        &.network-normal {
          background: #52c41a;
          box-shadow: 0 0 10rpx rgba(82, 196, 26, 0.5);
        }
        
        &.network-error {
          background: #f56c6c;
          box-shadow: 0 0 10rpx rgba(245, 108, 108, 0.5);
        }
      }
    }
  }
}
</style>