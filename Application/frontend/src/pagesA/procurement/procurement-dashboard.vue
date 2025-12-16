<template>
  <view class="procurement-dashboard">
    <!-- 页面头部 -->
    <view class="page-header">
      <text class="page-title">政府采购信息采集</text>
      <text class="page-subtitle">自动搜索获取公共平台采购信息</text>
    </view>

    <!-- 统计卡片 -->
    <view class="stats-cards">
      <view class="stat-card">
        <view class="stat-icon">
          <text class="iconfont icon-document"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.totalProcurements || 0 }}</text>
          <text class="stat-label">总采购信息</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon">
          <text class="iconfont icon-opportunity"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.matchedOpportunities || 0 }}</text>
          <text class="stat-label">匹配商机</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon">
          <text class="iconfont icon-success"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.successRate || 0 }}%</text>
          <text class="stat-label">采集成功率</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon">
          <text class="iconfont icon-running"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.activeTasks || 0 }}</text>
          <text class="stat-label">活跃任务</text>
        </view>
      </view>
    </view>

    <!-- 快速操作 -->
    <view class="quick-actions">
      <text class="section-title">快速操作</text>
      <view class="action-grid">
        <view class="action-item" @click="navigateToTenderCollection">
          <view class="action-icon">
            <text class="iconfont icon-tender"></text>
          </view>
          <text class="action-text">招标采集</text>
        </view>
        
        <view class="action-item" @click="navigateToOpportunities">
          <view class="action-icon">
            <text class="iconfont icon-match"></text>
          </view>
          <text class="action-text">商机匹配</text>
        </view>
        
        <view class="action-item" @click="navigateToCrawlerMonitor">
          <view class="action-icon">
            <text class="iconfont icon-monitor"></text>
          </view>
          <text class="action-text">爬虫监控</text>
        </view>
        
        <view class="action-item" @click="startQuickCrawl">
          <view class="action-icon">
            <text class="iconfont icon-start"></text>
          </view>
          <text class="action-text">快速采集</text>
        </view>
      </view>
    </view>

    <!-- 最近采集 -->
    <view class="recent-section">
      <view class="section-header">
        <text class="section-title">最近采集</text>
        <text class="view-more" @click="navigateToProcurementList">查看更多 ></text>
      </view>
      
      <view class="procurement-list" v-if="recentProcurements.length > 0">
        <view 
          class="procurement-item" 
          v-for="item in recentProcurements" 
          :key="item.procurementInfoId"
          @click="viewProcurementDetail(item)"
        >
          <view class="item-header">
            <text class="item-title">{{ item.title }}</text>
            <text class="item-status" :class="getStatusClass(item.status)">{{ getStatusText(item.status) }}</text>
          </view>
          
          <view class="item-info">
            <text class="info-item">地区: {{ item.region }}</text>
            <text class="info-item">预算: {{ formatBudget(item.budget) }}</text>
            <text class="info-item">类型: {{ item.procurementType }}</text>
          </view>
          
          <view class="item-footer">
            <text class="item-date">{{ formatDate(item.publishDate) }}</text>
            <view class="item-actions">
              <button class="action-btn primary" @click.stop="matchOpportunity(item)">匹配商机</button>
              <button class="action-btn secondary" @click.stop="convertToOpportunity(item)">转为商机</button>
            </view>
          </view>
        </view>
      </view>
      
      <view class="empty-state" v-else>
        <text class="empty-text">暂无采集数据</text>
        <button class="empty-btn" @click="startQuickCrawl">开始采集</button>
      </view>
    </view>

    <!-- 采集任务状态 -->
    <view class="task-status">
      <text class="section-title">采集任务状态</text>
      <view class="task-list">
        <view class="task-item" v-for="task in crawlTasks" :key="task.id">
          <view class="task-info">
            <text class="task-name">{{ task.name }}</text>
            <text class="task-time">{{ formatDate(task.createTime) }}</text>
          </view>
          <view class="task-status" :class="task.status">
            <text class="status-text">{{ getTaskStatusText(task.status) }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 快速采集弹窗 -->
    <uni-popup ref="quickCrawlPopup" type="center">
      <view class="popup-content">
        <view class="popup-header">
          <text class="popup-title">快速采集配置</text>
          <text class="popup-close" @click="closeQuickCrawlPopup">×</text>
        </view>
        
        <view class="popup-body">
          <view class="form-item">
            <text class="form-label">搜索关键词</text>
            <input 
              class="form-input" 
              v-model="crawlForm.keyword" 
              placeholder="请输入搜索关键词"
              placeholder-style="color: #999"
            />
          </view>
          
          <view class="form-item">
            <text class="form-label">地区</text>
            <picker 
              mode="selector" 
              :range="regionOptions" 
              :value="regionIndex"
              @change="onRegionChange"
            >
              <view class="form-picker">
                <text>{{ regionOptions[regionIndex] || '请选择地区' }}</text>
                <text class="picker-arrow">></text>
              </view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="form-label">采集天数</text>
            <input 
              class="form-input" 
              v-model="crawlForm.days" 
              type="number"
              placeholder="请输入采集天数"
              placeholder-style="color: #999"
            />
          </view>
          
          <view class="form-item">
            <text class="form-label">采集类型</text>
            <radio-group class="radio-group" v-model="crawlForm.type">
              <label class="radio-item">
                <radio value="government" :checked="crawlForm.type === 'government'" />
                <text>政府采购</text>
              </label>
              <label class="radio-item">
                <radio value="tender" :checked="crawlForm.type === 'tender'" />
                <text>招标公告</text>
              </label>
            </radio-group>
          </view>
        </view>
        
        <view class="popup-footer">
          <button class="btn-cancel" @click="closeQuickCrawlPopup">取消</button>
          <button class="btn-confirm" @click="confirmQuickCrawl" :loading="crawlLoading">
            开始采集
          </button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import {
  getCrawlerStatistics,
  getProcurementList,
  matchBusinessOpportunities,
  convertToOpportunity
} from '@/api/procurement/procurementCrawler.js'

// 统计数据
const statistics = ref({
  totalProcurements: 0,
  matchedOpportunities: 0,
  successRate: 0,
  activeTasks: 0
})

// 最近采集数据
const recentProcurements = ref([])

// 采集任务
const crawlTasks = ref([])

// 弹窗相关
const quickCrawlPopup = ref(null)
const crawlLoading = ref(false)

// 采集表单
const crawlForm = reactive({
  keyword: '',
  region: '',
  days: 7,
  type: 'government'
})

// 地区选项
const regionOptions = [
  '北京市', '上海市', '天津市', '重庆市', '广东省', '江苏省', '浙江省', '山东省', 
  '河南省', '四川省', '湖北省', '湖南省', '河北省', '山西省', '陕西省', '安徽省'
]
const regionIndex = ref(-1)

// 页面显示时加载数据
onShow(() => {
  loadDashboardData()
})

onMounted(() => {
  loadDashboardData()
})

// 加载仪表板数据
async function loadDashboardData() {
  try {
    // 获取统计信息
    const statsRes = await getCrawlerStatistics()
    if (statsRes.success) {
      statistics.value = statsRes.data
    }
    
    // 获取最近采集数据
    const listRes = await getProcurementList({
      current: 1,
      pageSize: 5,
      sortField: 'createTime',
      sortOrder: 'desc'
    })
    if (listRes.success) {
      recentProcurements.value = listRes.data.records || []
    }
    
    // 获取采集任务列表
    // const taskRes = await getCrawlTaskList({ status: 'running' })
    // if (taskRes.success) {
    //   crawlTasks.value = taskRes.data || []
    // }
    
  } catch (error) {
    console.error('加载仪表板数据失败:', error)
    uni.showToast({
      title: '加载数据失败',
      icon: 'none'
    })
  }
}

// 导航函数
function navigateToTenderCollection() {
  uni.navigateTo({
    url: '/pagesA/procurement/tender-collection'
  })
}

function navigateToOpportunities() {
  uni.navigateTo({
    url: '/pagesA/procurement/procurement-opportunities'
  })
}

function navigateToCrawlerMonitor() {
  uni.navigateTo({
    url: '/pagesA/procurement/crawler-monitor'
  })
}

function navigateToProcurementList() {
  uni.navigateTo({
    url: '/pagesA/procurement/procurement-list'
  })
}

// 快速采集
function startQuickCrawl() {
  quickCrawlPopup.value.open()
}

function closeQuickCrawlPopup() {
  quickCrawlPopup.value.close()
}

function onRegionChange(e) {
  regionIndex.value = e.detail.value
  crawlForm.region = regionOptions[regionIndex.value]
}

async function confirmQuickCrawl() {
  if (!crawlForm.keyword.trim()) {
    uni.showToast({
      title: '请输入搜索关键词',
      icon: 'none'
    })
    return
  }
  
  if (!crawlForm.region) {
    uni.showToast({
      title: '请选择地区',
      icon: 'none'
    })
    return
  }
  
  if (!crawlForm.days || crawlForm.days <= 0) {
    uni.showToast({
      title: '请输入有效的采集天数',
      icon: 'none'
    })
    return
  }
  
  crawlLoading.value = true
  
  try {
    let result
    if (crawlForm.type === 'government') {
      result = await crawlGovernmentProcurement({
        keyword: crawlForm.keyword,
        region: crawlForm.region,
        days: parseInt(crawlForm.days)
      })
    } else {
      result = await crawlTenderNotice({
        category: crawlForm.keyword,
        region: crawlForm.region,
        days: parseInt(crawlForm.days)
      })
    }
    
    if (result.success) {
      uni.showToast({
        title: `成功采集${result.data}条数据`,
        icon: 'success'
      })
      closeQuickCrawlPopup()
      loadDashboardData() // 重新加载数据
    } else {
      uni.showToast({
        title: result.message || '采集失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('采集失败:', error)
    uni.showToast({
      title: '采集失败',
      icon: 'none'
    })
  } finally {
    crawlLoading.value = false
  }
}

// 查看详情
function viewProcurementDetail(item) {
  uni.navigateTo({
    url: `/pagesA/procurement/procurement-detail?id=${item.procurementInfoId}`
  })
}

// 匹配商机
async function matchOpportunity(item) {
  try {
    const result = await matchBusinessOpportunities(item)
    if (result.success) {
      uni.showToast({
        title: `匹配到${result.data.length}个商机`,
        icon: 'success'
      })
    } else {
      uni.showToast({
        title: result.message || '匹配失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('匹配商机失败:', error)
    uni.showToast({
      title: '匹配失败',
      icon: 'none'
    })
  }
}

// 转为商机
async function convertToOpportunity(item) {
  try {
    const result = await convertToOpportunity(item)
    if (result.success) {
      uni.showToast({
        title: '成功转为商机',
        icon: 'success'
      })
    } else {
      uni.showToast({
        title: result.message || '转换失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('转换商机失败:', error)
    uni.showToast({
      title: '转换失败',
      icon: 'none'
    })
  }
}

// 工具函数
function getStatusClass(status) {
  const statusMap = {
    'pending': 'status-pending',
    'approved': 'status-approved',
    'rejected': 'status-rejected',
    'converted': 'status-converted'
  }
  return statusMap[status] || 'status-pending'
}

function getStatusText(status) {
  const statusMap = {
    'pending': '待审核',
    'approved': '已通过',
    'rejected': '已拒绝',
    'converted': '已转换'
  }
  return statusMap[status] || '未知'
}

function getTaskStatusText(status) {
  const statusMap = {
    'running': '运行中',
    'completed': '已完成',
    'failed': '失败',
    'stopped': '已停止'
  }
  return statusMap[status] || '未知'
}

function formatBudget(budget) {
  if (!budget) return '暂无'
  const num = parseFloat(budget)
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return budget + '元'
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.procurement-dashboard {
  padding: 20rpx;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 40rpx;
  
  .page-title {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    color: #303133;
    margin-bottom: 10rpx;
  }
  
  .page-subtitle {
    display: block;
    font-size: 28rpx;
    color: #909399;
  }
}

.stats-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-bottom: 40rpx;
  
  .stat-card {
    flex: 1;
    min-width: 300rpx;
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
    
    .stat-icon {
      width: 80rpx;
      height: 80rpx;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
      
      .iconfont {
        font-size: 40rpx;
        color: #fff;
      }
    }
    
    .stat-content {
      flex: 1;
      
      .stat-number {
        display: block;
        font-size: 36rpx;
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

.quick-actions {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 40rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .section-title {
    display: block;
    font-size: 32rpx;
    font-weight: bold;
    color: #303133;
    margin-bottom: 30rpx;
  }
  
  .action-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20rpx;
    
    .action-item {
      background: #f8f9fa;
      border-radius: 12rpx;
      padding: 40rpx 20rpx;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.98);
        background: #e9ecef;
      }
      
      .action-icon {
        width: 60rpx;
        height: 60rpx;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 20rpx;
        
        .iconfont {
          font-size: 32rpx;
          color: #fff;
        }
      }
      
      .action-text {
        display: block;
        font-size: 28rpx;
        color: #606266;
      }
    }
  }
}

.recent-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 40rpx;
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
    
    .view-more {
      font-size: 26rpx;
      color: #409eff;
      cursor: pointer;
    }
  }
  
  .procurement-list {
    .procurement-item {
      background: #f8f9fa;
      border-radius: 12rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      &:active {
        transform: scale(0.98);
        background: #e9ecef;
      }
      
      .item-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
        
        .item-title {
          font-size: 30rpx;
          font-weight: bold;
          color: #303133;
          flex: 1;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        
        .item-status {
          padding: 8rpx 16rpx;
          border-radius: 20rpx;
          font-size: 22rpx;
          
          &.status-pending {
            background: #fdf6ec;
            color: #e6a23c;
          }
          
          &.status-approved {
            background: #f0f9ff;
            color: #409eff;
          }
          
          &.status-rejected {
            background: #fef0f0;
            color: #f56c6c;
          }
          
          &.status-converted {
            background: #f6ffed;
            color: #52c41a;
          }
        }
      }
      
      .item-info {
        display: flex;
        flex-wrap: wrap;
        gap: 20rpx;
        margin-bottom: 20rpx;
        
        .info-item {
          font-size: 26rpx;
          color: #606266;
          background: #fff;
          padding: 8rpx 16rpx;
          border-radius: 8rpx;
        }
      }
      
      .item-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .item-date {
          font-size: 24rpx;
          color: #909399;
        }
        
        .item-actions {
          display: flex;
          gap: 16rpx;
          
          .action-btn {
            padding: 12rpx 24rpx;
            border-radius: 8rpx;
            font-size: 24rpx;
            border: none;
            cursor: pointer;
            
            &.primary {
              background: #409eff;
              color: #fff;
            }
            
            &.secondary {
              background: #fff;
              color: #606266;
              border: 1rpx solid #dcdfe6;
            }
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
      margin-bottom: 30rpx;
    }
    
    .empty-btn {
      background: #409eff;
      color: #fff;
      border: none;
      border-radius: 8rpx;
      padding: 20rpx 40rpx;
      font-size: 28rpx;
      cursor: pointer;
    }
  }
}

.task-status {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .section-title {
    display: block;
    font-size: 32rpx;
    font-weight: bold;
    color: #303133;
    margin-bottom: 30rpx;
  }
  
  .task-list {
    .task-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #ebeef5;
      
      &:last-child {
        border-bottom: none;
      }
      
      .task-info {
        flex: 1;
        
        .task-name {
          display: block;
          font-size: 28rpx;
          color: #303133;
          margin-bottom: 8rpx;
        }
        
        .task-time {
          display: block;
          font-size: 24rpx;
          color: #909399;
        }
      }
      
      .task-status {
        padding: 8rpx 16rpx;
        border-radius: 20rpx;
        font-size: 22rpx;
        
        &.running {
          background: #f0f9ff;
          color: #409eff;
        }
        
        &.completed {
          background: #f6ffed;
          color: #52c41a;
        }
        
        &.failed {
          background: #fef0f0;
          color: #f56c6c;
        }
        
        &.stopped {
          background: #f4f4f5;
          color: #909399;
        }
      }
    }
  }
}

// 弹窗样式
.popup-content {
  background: #fff;
  border-radius: 16rpx;
  width: 600rpx;
  max-width: 90vw;
  
  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #ebeef5;
    
    .popup-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
    
    .popup-close {
      font-size: 40rpx;
      color: #909399;
      cursor: pointer;
    }
  }
  
  .popup-body {
    padding: 30rpx;
    
    .form-item {
      margin-bottom: 30rpx;
      
      .form-label {
        display: block;
        font-size: 28rpx;
        color: #303133;
        margin-bottom: 16rpx;
      }
      
      .form-input {
        width: 100%;
        height: 80rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 28rpx;
      }
      
      .form-picker {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: 80rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 28rpx;
        
        .picker-arrow {
          color: #909399;
        }
      }
      
      .radio-group {
        display: flex;
        gap: 40rpx;
        
        .radio-item {
          display: flex;
          align-items: center;
          gap: 12rpx;
          font-size: 28rpx;
          color: #606266;
        }
      }
    }
  }
  
  .popup-footer {
    display: flex;
    justify-content: flex-end;
    gap: 20rpx;
    padding: 30rpx;
    border-top: 1rpx solid #ebeef5;
    
    .btn-cancel,
    .btn-confirm {
      padding: 20rpx 40rpx;
      border-radius: 8rpx;
      font-size: 28rpx;
      border: none;
      cursor: pointer;
    }
    
    .btn-cancel {
      background: #f4f4f5;
      color: #606266;
    }
    
    .btn-confirm {
      background: #409eff;
      color: #fff;
    }
  }
}
</style>