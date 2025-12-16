<template>
  <view class="crawler-monitor">
    <view class="header">
      <text class="title">爬虫监控面板</text>
      <view class="actions">
        <button class="btn-primary" @click="startAllCrawlers">启动全部</button>
        <button class="btn-secondary" @click="stopAllCrawlers">停止全部</button>
        <button class="btn-refresh" @click="refreshStatus">刷新状态</button>
      </view>
    </view>
    
    <view class="monitor-stats">
      <view class="stat-card">
        <text class="stat-number">{{ stats.totalCrawlers }}</text>
        <text class="stat-label">爬虫总数</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.runningCrawlers }}</text>
        <text class="stat-label">运行中</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.successRate }}%</text>
        <text class="stat-label">成功率</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.todayData }}</text>
        <text class="stat-label">今日采集</text>
      </view>
    </view>
    
    <view class="crawler-list">
      <view class="list-header">
        <text class="list-title">爬虫实例监控</text>
        <view class="filter-options">
          <picker mode="selector" :range="statusFilters" :value="selectedFilter" @change="onFilterChange">
            <view class="filter-picker">{{ statusFilters[selectedFilter] }}</view>
          </picker>
        </view>
      </view>
      
      <scroll-view class="crawler-items" scroll-y>
        <view class="crawler-item" v-for="crawler in filteredCrawlers" :key="crawler.id">
          <view class="crawler-header">
            <view class="crawler-info">
              <text class="crawler-name">{{ crawler.name }}</text>
              <text class="crawler-target">{{ crawler.targetSite }}</text>
            </view>
            <view class="status-indicator" :class="getStatusClass(crawler.status)">
              <text class="status-text">{{ crawler.status }}</text>
            </view>
          </view>
          
          <view class="crawler-stats">
            <view class="stat-item">
              <text class="stat-label">今日采集:</text>
              <text class="stat-value">{{ crawler.todayRecords }}</text>
            </view>
            <view class="stat-item">
              <text class="stat-label">成功率:</text>
              <text class="stat-value">{{ crawler.successRate }}%</text>
            </view>
            <view class="stat-item">
              <text class="stat-label">运行时长:</text>
              <text class="stat-value">{{ crawler.runTime }}</text>
            </view>
            <view class="stat-item">
              <text class="stat-label">最后执行:</text>
              <text class="stat-value">{{ crawler.lastExecute }}</text>
            </view>
          </view>
          
          <view class="progress-bar">
            <view class="progress-fill" :style="{ width: crawler.progress + '%' }"></view>
            <text class="progress-text">{{ crawler.progress }}%</text>
          </view>
          
          <view class="crawler-actions">
            <button class="btn-start" v-if="crawler.status === '已停止'" @click="startCrawler(crawler)">启动</button>
            <button class="btn-stop" v-if="crawler.status === '运行中'" @click="stopCrawler(crawler)">停止</button>
            <button class="btn-restart" @click="restartCrawler(crawler)">重启</button>
            <button class="btn-logs" @click="viewLogs(crawler)">日志</button>
            <button class="btn-config" @click="viewConfig(crawler)">配置</button>
          </view>
        </view>
      </scroll-view>
    </view>
    
    <view class="alert-panel">
      <view class="alert-header">
        <text class="alert-title">异常告警</text>
        <button class="btn-clear" @click="clearAlerts">清除全部</button>
      </view>
      <scroll-view class="alert-list" scroll-y>
        <view class="alert-item" v-for="alert in alerts" :key="alert.id" :class="getAlertClass(alert.level)">
          <view class="alert-icon">⚠️</view>
          <view class="alert-content">
            <text class="alert-message">{{ alert.message }}</text>
            <text class="alert-time">{{ alert.time }}</text>
          </view>
          <button class="btn-dismiss" @click="dismissAlert(alert)">忽略</button>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      stats: {
        totalCrawlers: 12,
        runningCrawlers: 8,
        successRate: 94,
        todayData: 156
      },
      statusFilters: ['全部状态', '运行中', '已停止', '异常', '维护中'],
      selectedFilter: 0,
      crawlers: [
        {
          id: 1,
          name: '政府采购网爬虫',
          targetSite: 'www.ccgp.gov.cn',
          status: '运行中',
          todayRecords: 45,
          successRate: 98,
          runTime: '2小时30分',
          lastExecute: '5分钟前',
          progress: 75
        },
        {
          id: 2,
          name: '招标网爬虫',
          targetSite: 'www.bidcenter.com.cn',
          status: '运行中',
          todayRecords: 32,
          successRate: 92,
          runTime: '1小时45分',
          lastExecute: '8分钟前',
          progress: 60
        },
        {
          id: 3,
          name: '中国采购网爬虫',
          targetSite: 'www.chinabidding.com',
          status: '异常',
          todayRecords: 0,
          successRate: 0,
          runTime: '0小时0分',
          lastExecute: '2小时前',
          progress: 0
        },
        {
          id: 4,
          name: '地方采购网爬虫',
          targetSite: 'www.localprocurement.gov.cn',
          status: '已停止',
          todayRecords: 18,
          successRate: 85,
          runTime: '3小时15分',
          lastExecute: '1小时前',
          progress: 100
        }
      ],
      alerts: [
        {
          id: 1,
          level: 'error',
          message: '中国采购网爬虫连接超时，请检查网络连接',
          time: '2025-01-15 14:30:25'
        },
        {
          id: 2,
          level: 'warning',
          message: '招标网爬虫成功率下降，当前92%',
          time: '2025-01-15 14:25:10'
        },
        {
          id: 3,
          level: 'info',
          message: '政府采购网爬虫正常运行，采集效率良好',
          time: '2025-01-15 14:20:05'
        }
      ]
    }
  },
  computed: {
    filteredCrawlers() {
      if (this.selectedFilter === 0) return this.crawlers
      const filterStatus = this.statusFilters[this.selectedFilter]
      return this.crawlers.filter(crawler => crawler.status === filterStatus)
    }
  },
  methods: {
    startAllCrawlers() {
      uni.showModal({
        title: '启动全部爬虫',
        content: '确定要启动所有爬虫实例吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '正在启动全部爬虫',
              icon: 'success'
            })
            // 调用API启动全部爬虫
          }
        }
      })
    },
    stopAllCrawlers() {
      uni.showModal({
        title: '停止全部爬虫',
        content: '确定要停止所有爬虫实例吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '正在停止全部爬虫',
              icon: 'success'
            })
            // 调用API停止全部爬虫
          }
        }
      })
    },
    refreshStatus() {
      uni.showToast({
        title: '刷新状态',
        icon: 'success'
      })
      // 调用API刷新爬虫状态
    },
    onFilterChange(e) {
      this.selectedFilter = e.detail.value
    },
    getStatusClass(status) {
      const statusClass = {
        '运行中': 'status-running',
        '已停止': 'status-stopped',
        '异常': 'status-error',
        '维护中': 'status-maintenance'
      }
      return statusClass[status] || 'status-default'
    },
    startCrawler(crawler) {
      uni.showToast({
        title: `启动 ${crawler.name}`,
        icon: 'success'
      })
      // 调用API启动指定爬虫
    },
    stopCrawler(crawler) {
      uni.showModal({
        title: '停止爬虫',
        content: `确定要停止 ${crawler.name} 吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: `停止 ${crawler.name}`,
              icon: 'success'
            })
            // 调用API停止指定爬虫
          }
        }
      })
    },
    restartCrawler(crawler) {
      uni.showModal({
        title: '重启爬虫',
        content: `确定要重启 ${crawler.name} 吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: `重启 ${crawler.name}`,
              icon: 'success'
            })
            // 调用API重启指定爬虫
          }
        }
      })
    },
    viewLogs(crawler) {
      uni.navigateTo({
        url: `/pages/procurement/crawler-logs?name=${crawler.name}`
      })
    },
    viewConfig(crawler) {
      uni.navigateTo({
        url: `/pages/procurement/crawler-config?name=${crawler.name}`
      })
    },
    clearAlerts() {
      uni.showModal({
        title: '清除告警',
        content: '确定要清除所有告警信息吗？',
        success: (res) => {
          if (res.confirm) {
            this.alerts = []
            uni.showToast({
              title: '告警已清除',
              icon: 'success'
            })
          }
        }
      })
    },
    dismissAlert(alert) {
      const index = this.alerts.indexOf(alert)
      if (index > -1) {
        this.alerts.splice(index, 1)
      }
    },
    getAlertClass(level) {
      const alertClass = {
        'error': 'alert-error',
        'warning': 'alert-warning',
        'info': 'alert-info'
      }
      return alertClass[level] || 'alert-default'
    }
  }
}
</script>

<style>
.crawler-monitor {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.actions {
  display: flex;
  gap: 20rpx;
}

.btn-primary {
  background-color: #28a745;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.btn-secondary {
  background-color: #dc3545;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.btn-refresh {
  background-color: #17a2b8;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.monitor-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.stat-card {
  background: white;
  padding: 30rpx;
  border-radius: 12rpx;
  text-align: center;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.stat-number {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #007aff;
  margin-bottom: 10rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #666;
}

.crawler-list {
  background: white;
  border-radius: 12rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.list-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.filter-picker {
  background: #f8f9fa;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #333;
  border: 1rpx solid #ddd;
}

.crawler-items {
  max-height: 600rpx;
}

.crawler-item {
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.crawler-item:last-child {
  border-bottom: none;
}

.crawler-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.crawler-info {
  flex: 1;
}

.crawler-name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.crawler-target {
  font-size: 24rpx;
  color: #666;
}

.status-indicator {
  padding: 8rpx 16rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
  font-weight: bold;
}

.status-running {
  background-color: #d4edda;
  color: #155724;
}

.status-stopped {
  background-color: #f8d7da;
  color: #721c24;
}

.status-error {
  background-color: #f5c6cb;
  color: #721c24;
}

.status-maintenance {
  background-color: #fff3cd;
  color: #856404;
}

.status-default {
  background-color: #e2e3e5;
  color: #383d41;
}

.crawler-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  font-size: 24rpx;
  color: #666;
}

.stat-value {
  font-size: 24rpx;
  color: #333;
  font-weight: 500;
}

.progress-bar {
  position: relative;
  height: 20rpx;
  background-color: #e9ecef;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #007bff, #0056b3);
  border-radius: 10rpx;
  transition: width 0.3s ease;
}

.progress-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 20rpx;
  color: white;
  font-weight: bold;
}

.crawler-actions {
  display: flex;
  gap: 15rpx;
  flex-wrap: wrap;
}

.btn-start {
  background-color: #28a745;
  color: white;
  padding: 10rpx 20rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
}

.btn-stop {
  background-color: #dc3545;
  color: white;
  padding: 10rpx 20rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
}

.btn-restart {
  background-color: #ffc107;
  color: #212529;
  padding: 10rpx 20rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
}

.btn-logs {
  background-color: #17a2b8;
  color: white;
  padding: 10rpx 20rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
}

.btn-config {
  background-color: #6c757d;
  color: white;
  padding: 10rpx 20rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
}

.alert-panel {
  background: white;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.alert-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.btn-clear {
  background-color: #6c757d;
  color: white;
  padding: 10rpx 20rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
}

.alert-list {
  max-height: 400rpx;
}

.alert-item {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #eee;
}

.alert-item:last-child {
  border-bottom: none;
}

.alert-icon {
  font-size: 32rpx;
  margin-right: 20rpx;
}

.alert-content {
  flex: 1;
}

.alert-message {
  display: block;
  font-size: 24rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.alert-time {
  font-size: 20rpx;
  color: #999;
}

.btn-dismiss {
  background-color: #dc3545;
  color: white;
  padding: 8rpx 16rpx;
  border-radius: 6rpx;
  font-size: 18rpx;
}

.alert-error {
  background-color: #f8d7da;
}

.alert-warning {
  background-color: #fff3cd;
}

.alert-info {
  background-color: #d1ecf1;
}

.alert-default {
  background-color: #f8f9fa;
}
</style>