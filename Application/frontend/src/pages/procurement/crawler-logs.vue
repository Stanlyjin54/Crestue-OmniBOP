<template>
  <view class="crawler-logs">
    <view class="header">
      <text class="title">爬虫运行日志</text>
      <view class="header-actions">
        <button class="btn-refresh" @click="refreshLogs">刷新</button>
        <button class="btn-export" @click="exportLogs">导出</button>
        <button class="btn-clear" @click="clearLogs">清空</button>
      </view>
    </view>
    
    <view class="filter-section">
      <view class="filter-row">
        <view class="filter-item">
          <text class="filter-label">日志级别:</text>
          <picker mode="selector" :range="logLevels" :value="selectedLevel" @change="onLevelChange">
            <view class="filter-picker">{{ logLevels[selectedLevel] }}</view>
          </picker>
        </view>
        <view class="filter-item">
          <text class="filter-label">时间范围:</text>
          <picker mode="selector" :range="timeRanges" :value="selectedTimeRange" @change="onTimeRangeChange">
            <view class="filter-picker">{{ timeRanges[selectedTimeRange] }}</view>
          </picker>
        </view>
      </view>
      <view class="filter-row">
        <view class="filter-item">
          <text class="filter-label">搜索关键词:</text>
          <input class="filter-input" v-model="searchKeyword" placeholder="输入关键词搜索" @input="onSearch" />
        </view>
        <button class="btn-search" @click="searchLogs">搜索</button>
      </view>
    </view>
    
    <view class="log-stats">
      <view class="stat-item">
        <text class="stat-number">{{ stats.totalLogs }}</text>
        <text class="stat-label">总日志数</text>
      </view>
      <view class="stat-item">
        <text class="stat-number">{{ stats.errorCount }}</text>
        <text class="stat-label">错误数</text>
      </view>
      <view class="stat-item">
        <text class="stat-number">{{ stats.warningCount }}</text>
        <text class="stat-label">警告数</text>
      </view>
      <view class="stat-item">
        <text class="stat-number">{{ stats.successRate }}%</text>
        <text class="stat-label">成功率</text>
      </view>
    </view>
    
    <scroll-view class="log-list" scroll-y @scrolltolower="loadMoreLogs">
      <view class="log-item" v-for="(log, index) in filteredLogs" :key="index" :class="getLogLevelClass(log.level)">
        <view class="log-header">
          <view class="log-level" :class="getLevelClass(log.level)">
            <text class="level-text">{{ log.level }}</text>
          </view>
          <text class="log-time">{{ log.timestamp }}</text>
          <view class="log-actions">
            <button class="btn-detail" @click="viewLogDetail(log)">详情</button>
            <button class="btn-copy" @click="copyLog(log)">复制</button>
          </view>
        </view>
        <view class="log-content">
          <text class="log-message">{{ log.message }}</text>
          <view v-if="log.details" class="log-details">
            <text class="details-text">{{ log.details }}</text>
          </view>
        </view>
        <view class="log-meta">
          <text class="meta-item">爬虫: {{ log.crawlerName }}</text>
          <text class="meta-item">任务: {{ log.taskId }}</text>
          <text class="meta-item">耗时: {{ log.duration }}ms</text>
        </view>
      </view>
      
      <view v-if="loading" class="loading-more">
        <text class="loading-text">加载中...</text>
      </view>
      
      <view v-if="noMoreLogs" class="no-more-logs">
        <text class="no-more-text">没有更多日志了</text>
      </view>
    </scroll-view>
    
    <!-- 日志详情弹窗 -->
    <view class="modal" v-if="showDetailModal">
      <view class="modal-content">
        <view class="modal-header">
          <text class="modal-title">日志详情</text>
          <button class="btn-close" @click="closeDetailModal">×</button>
        </view>
        <view class="modal-body">
          <view class="detail-item">
            <text class="detail-label">日志级别:</text>
            <text class="detail-value">{{ currentLog.level }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">时间戳:</text>
            <text class="detail-value">{{ currentLog.timestamp }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">爬虫名称:</text>
            <text class="detail-value">{{ currentLog.crawlerName }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">任务ID:</text>
            <text class="detail-value">{{ currentLog.taskId }}</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">执行时间:</text>
            <text class="detail-value">{{ currentLog.duration }}ms</text>
          </view>
          <view class="detail-item">
            <text class="detail-label">日志消息:</text>
            <text class="detail-value">{{ currentLog.message }}</text>
          </view>
          <view v-if="currentLog.stackTrace" class="detail-item">
            <text class="detail-label">堆栈跟踪:</text>
            <text class="detail-value stack-trace">{{ currentLog.stackTrace }}</text>
          </view>
        </view>
        <view class="modal-footer">
          <button class="btn-copy-detail" @click="copyLogDetail">复制详情</button>
          <button class="btn-close-modal" @click="closeDetailModal">关闭</button>
        </view>
      </view>
    </view>
    
    <!-- 实时日志开关 -->
    <view class="real-time-toggle">
      <text class="toggle-label">实时日志</text>
      <switch :checked="isRealTime" @change="toggleRealTime" />
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      logs: [],
      filteredLogs: [],
      selectedLevel: 0,
      selectedTimeRange: 1,
      searchKeyword: '',
      logLevels: ['全部', 'DEBUG', 'INFO', 'WARNING', 'ERROR', 'CRITICAL'],
      timeRanges: ['最近1小时', '最近6小时', '最近24小时', '最近7天', '全部'],
      stats: {
        totalLogs: 0,
        errorCount: 0,
        warningCount: 0,
        successRate: 0
      },
      loading: false,
      noMoreLogs: false,
      currentPage: 1,
      pageSize: 20,
      showDetailModal: false,
      currentLog: null,
      isRealTime: false,
      realTimeInterval: null
    }
  },
  onLoad(options) {
    if (options.name) {
      this.crawlerName = options.name
      uni.setNavigationBarTitle({
        title: `${options.name} - 运行日志`
      })
    }
    this.loadLogs()
  },
  onUnload() {
    if (this.realTimeInterval) {
      clearInterval(this.realTimeInterval)
    }
  },
  methods: {
    loadLogs() {
      uni.showLoading({ title: '加载日志中...' })
      
      // 模拟日志数据
      setTimeout(() => {
        const mockLogs = this.generateMockLogs()
        this.logs = mockLogs
        this.filterLogs()
        this.updateStats()
        uni.hideLoading()
      }, 1000)
    },
    generateMockLogs() {
      const levels = ['DEBUG', 'INFO', 'WARNING', 'ERROR', 'CRITICAL']
      const messages = [
        '开始采集任务',
        '成功连接到目标网站',
        '解析页面内容',
        '提取数据字段',
        '数据验证通过',
        '保存数据到数据库',
        '网络连接超时',
        '页面解析失败',
        '数据格式错误',
        '数据库连接异常',
        '代理服务器连接失败',
        '采集任务完成',
        '发现新的招标信息',
        '数据去重处理',
        '数据清洗完成'
      ]
      
      const logs = []
      const now = new Date()
      
      for (let i = 0; i < 50; i++) {
        const level = levels[Math.floor(Math.random() * levels.length)]
        const message = messages[Math.floor(Math.random() * messages.length)]
        const timestamp = new Date(now.getTime() - Math.random() * 24 * 60 * 60 * 1000)
        
        logs.push({
          id: i + 1,
          level: level,
          message: message,
          details: Math.random() > 0.7 ? `详细信息: ${message} - 附加信息` : null,
          timestamp: timestamp.toLocaleString('zh-CN'),
          crawlerName: this.crawlerName || '政府采购网爬虫',
          taskId: `TASK_${Math.floor(Math.random() * 1000)}`,
          duration: Math.floor(Math.random() * 5000) + 100,
          stackTrace: level === 'ERROR' || level === 'CRITICAL' ? `Error: ${message}\n    at Crawler.run (crawler.js:123:45)\n    at processTicksAndRejections (internal/process/task_queues.js:93:5)` : null
        })
      }
      
      return logs.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp))
    },
    filterLogs() {
      let filtered = this.logs
      
      // 按级别过滤
      if (this.selectedLevel > 0) {
        const selectedLevelText = this.logLevels[this.selectedLevel]
        filtered = filtered.filter(log => log.level === selectedLevelText)
      }
      
      // 按时间范围过滤
      if (this.selectedTimeRange < 4) {
        const now = new Date()
        const timeRanges = [1, 6, 24, 24 * 7] // 小时
        const hours = timeRanges[this.selectedTimeRange]
        const cutoffTime = new Date(now.getTime() - hours * 60 * 60 * 1000)
        
        filtered = filtered.filter(log => new Date(log.timestamp) >= cutoffTime)
      }
      
      // 按关键词搜索
      if (this.searchKeyword) {
        const keyword = this.searchKeyword.toLowerCase()
        filtered = filtered.filter(log => 
          log.message.toLowerCase().includes(keyword) ||
          (log.details && log.details.toLowerCase().includes(keyword))
        )
      }
      
      this.filteredLogs = filtered
    },
    updateStats() {
      this.stats.totalLogs = this.logs.length
      this.stats.errorCount = this.logs.filter(log => log.level === 'ERROR' || log.level === 'CRITICAL').length
      this.stats.warningCount = this.logs.filter(log => log.level === 'WARNING').length
      
      const successLogs = this.logs.filter(log => log.level === 'INFO' || log.level === 'DEBUG')
      this.stats.successRate = this.stats.totalLogs > 0 
        ? Math.round((successLogs.length / this.stats.totalLogs) * 100) 
        : 0
    },
    onLevelChange(e) {
      this.selectedLevel = e.detail.value
      this.filterLogs()
    },
    onTimeRangeChange(e) {
      this.selectedTimeRange = e.detail.value
      this.filterLogs()
    },
    onSearch() {
      this.filterLogs()
    },
    searchLogs() {
      this.filterLogs()
      uni.showToast({ title: '搜索完成', icon: 'success' })
    },
    refreshLogs() {
      uni.showLoading({ title: '刷新中...' })
      setTimeout(() => {
        const newLogs = this.generateMockLogs()
        this.logs = newLogs
        this.filterLogs()
        this.updateStats()
        uni.hideLoading()
        uni.showToast({ title: '日志已刷新', icon: 'success' })
      }, 1000)
    },
    exportLogs() {
      uni.showModal({
        title: '导出日志',
        content: '确定要导出当前日志吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '导出中...' })
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({ title: '日志已导出', icon: 'success' })
            }, 1500)
          }
        }
      })
    },
    clearLogs() {
      uni.showModal({
        title: '清空日志',
        content: '确定要清空所有日志吗？此操作不可恢复。',
        success: (res) => {
          if (res.confirm) {
            this.logs = []
            this.filteredLogs = []
            this.updateStats()
            uni.showToast({ title: '日志已清空', icon: 'success' })
          }
        }
      })
    },
    loadMoreLogs() {
      if (this.loading || this.noMoreLogs) return
      
      this.loading = true
      setTimeout(() => {
        const moreLogs = this.generateMockLogs()
        if (moreLogs.length === 0) {
          this.noMoreLogs = true
        } else {
          this.logs = [...this.logs, ...moreLogs]
          this.filterLogs()
          this.updateStats()
        }
        this.loading = false
      }, 1000)
    },
    getLogLevelClass(level) {
      const levelClass = {
        'DEBUG': 'log-debug',
        'INFO': 'log-info',
        'WARNING': 'log-warning',
        'ERROR': 'log-error',
        'CRITICAL': 'log-critical'
      }
      return levelClass[level] || 'log-default'
    },
    getLevelClass(level) {
      const classMap = {
        'DEBUG': 'level-debug',
        'INFO': 'level-info',
        'WARNING': 'level-warning',
        'ERROR': 'level-error',
        'CRITICAL': 'level-critical'
      }
      return classMap[level] || 'level-default'
    },
    viewLogDetail(log) {
      this.currentLog = log
      this.showDetailModal = true
    },
    copyLog(log) {
      const logText = `[${log.timestamp}] [${log.level}] ${log.message}`
      uni.setClipboardData({
        data: logText,
        success: () => {
          uni.showToast({ title: '日志已复制', icon: 'success' })
        }
      })
    },
    closeDetailModal() {
      this.showDetailModal = false
      this.currentLog = null
    },
    copyLogDetail() {
      if (!this.currentLog) return
      
      let detailText = `
日志级别: ${this.currentLog.level}
时间戳: ${this.currentLog.timestamp}
爬虫名称: ${this.currentLog.crawlerName}
任务ID: ${this.currentLog.taskId}
执行时间: ${this.currentLog.duration}ms
日志消息: ${this.currentLog.message}
      `
      
      if (this.currentLog.stackTrace) {
        detailText += `\n堆栈跟踪:\n${this.currentLog.stackTrace}`
      }
      
      uni.setClipboardData({
        data: detailText.trim(),
        success: () => {
          uni.showToast({ title: '详情已复制', icon: 'success' })
        }
      })
    },
    toggleRealTime(e) {
      this.isRealTime = e.detail.value
      
      if (this.isRealTime) {
        this.startRealTimeLogs()
      } else {
        this.stopRealTimeLogs()
      }
    },
    startRealTimeLogs() {
      this.realTimeInterval = setInterval(() => {
        const newLog = this.generateRealTimeLog()
        this.logs.unshift(newLog)
        this.filterLogs()
        this.updateStats()
      }, 3000) // 每3秒添加一条新日志
      
      uni.showToast({ title: '实时日志已开启', icon: 'success' })
    },
    stopRealTimeLogs() {
      if (this.realTimeInterval) {
        clearInterval(this.realTimeInterval)
        this.realTimeInterval = null
      }
      
      uni.showToast({ title: '实时日志已关闭', icon: 'success' })
    },
    generateRealTimeLog() {
      const levels = ['DEBUG', 'INFO', 'WARNING', 'ERROR']
      const messages = [
        '实时连接正常',
        '数据采集进行中',
        '页面解析完成',
        '数据存储成功',
        '网络延迟检测',
        '代理连接检查'
      ]
      
      const level = levels[Math.floor(Math.random() * levels.length)]
      const message = messages[Math.floor(Math.random() * messages.length)]
      
      return {
        id: Date.now(),
        level: level,
        message: message,
        timestamp: new Date().toLocaleString('zh-CN'),
        crawlerName: this.crawlerName || '政府采购网爬虫',
        taskId: `REALTIME_${Date.now()}`,
        duration: Math.floor(Math.random() * 1000) + 50
      }
    }
  }
}
</script>

<style>
.crawler-logs {
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

.header-actions {
  display: flex;
  gap: 15rpx;
}

.btn-refresh {
  background-color: #17a2b8;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.btn-export {
  background-color: #28a745;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.btn-clear {
  background-color: #dc3545;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.filter-section {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.filter-row {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.filter-row:last-child {
  margin-bottom: 0;
}

.filter-item {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 15rpx;
}

.filter-label {
  font-size: 24rpx;
  color: #333;
  width: 100rpx;
}

.filter-picker {
  flex: 1;
  background: #f8f9fa;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 12rpx 20rpx;
  font-size: 24rpx;
  color: #333;
}

.filter-input {
  flex: 1;
  background: #f8f9fa;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 12rpx 20rpx;
  font-size: 24rpx;
}

.btn-search {
  background-color: #007aff;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.log-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.stat-item {
  background: white;
  padding: 20rpx;
  border-radius: 12rpx;
  text-align: center;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.stat-number {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #007aff;
  margin-bottom: 5rpx;
}

.stat-label {
  font-size: 20rpx;
  color: #666;
}

.log-list {
  background: white;
  border-radius: 12rpx;
  max-height: 60vh;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.log-item {
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #eee;
}

.log-item:last-child {
  border-bottom: none;
}

.log-debug {
  background-color: #f8f9fa;
}

.log-info {
  background-color: #d1ecf1;
}

.log-warning {
  background-color: #fff3cd;
}

.log-error {
  background-color: #f8d7da;
}

.log-critical {
  background-color: #f5c6cb;
}

.log-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.log-level {
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
  font-weight: bold;
}

.level-debug {
  background-color: #6c757d;
  color: white;
}

.level-info {
  background-color: #17a2b8;
  color: white;
}

.level-warning {
  background-color: #ffc107;
  color: #212529;
}

.level-error {
  background-color: #dc3545;
  color: white;
}

.level-critical {
  background-color: #721c24;
  color: white;
}

.level-default {
  background-color: #e2e3e5;
  color: #383d41;
}

.level-text {
  font-size: 20rpx;
  font-weight: bold;
}

.log-time {
  font-size: 20rpx;
  color: #999;
}

.log-actions {
  display: flex;
  gap: 10rpx;
}

.btn-detail {
  background-color: #17a2b8;
  color: white;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
  font-size: 18rpx;
}

.btn-copy {
  background-color: #28a745;
  color: white;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
  font-size: 18rpx;
}

.log-content {
  margin-bottom: 10rpx;
}

.log-message {
  font-size: 24rpx;
  color: #333;
  line-height: 1.5;
}

.log-details {
  margin-top: 10rpx;
  padding: 10rpx;
  background: rgba(0,0,0,0.05);
  border-radius: 6rpx;
}

.details-text {
  font-size: 22rpx;
  color: #666;
}

.log-meta {
  display: flex;
  gap: 20rpx;
  font-size: 20rpx;
  color: #999;
}

.meta-item {
  padding: 2rpx 8rpx;
  background: rgba(0,0,0,0.05);
  border-radius: 4rpx;
}

.loading-more,
.no-more-logs {
  text-align: center;
  padding: 30rpx;
}

.loading-text,
.no-more-text {
  font-size: 24rpx;
  color: #999;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12rpx;
  width: 90%;
  max-width: 700rpx;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.modal-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.btn-close {
  background: none;
  border: none;
  font-size: 36rpx;
  color: #999;
  padding: 0;
  width: 40rpx;
  height: 40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-body {
  padding: 30rpx;
}

.detail-item {
  display: flex;
  margin-bottom: 20rpx;
  align-items: flex-start;
}

.detail-label {
  font-size: 24rpx;
  color: #666;
  width: 120rpx;
  margin-right: 20rpx;
}

.detail-value {
  flex: 1;
  font-size: 24rpx;
  color: #333;
}

.stack-trace {
  background: #f8f9fa;
  padding: 15rpx;
  border-radius: 6rpx;
  font-family: monospace;
  font-size: 20rpx;
  line-height: 1.4;
  white-space: pre-wrap;
}

.modal-footer {
  display: flex;
  gap: 20rpx;
  justify-content: flex-end;
  padding: 30rpx;
  border-top: 1rpx solid #eee;
}

.btn-copy-detail {
  background-color: #28a745;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.btn-close-modal {
  background-color: #6c757d;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.real-time-toggle {
  position: fixed;
  bottom: 30rpx;
  right: 30rpx;
  background: white;
  padding: 20rpx;
  border-radius: 50rpx;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.2);
  display: flex;
  align-items: center;
  gap: 15rpx;
}

.toggle-label {
  font-size: 24rpx;
  color: #333;
  font-weight: bold;
}
</style>