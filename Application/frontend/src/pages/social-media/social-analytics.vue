<template>
  <view class="social-analytics">
    <view class="header">
      <text class="title">社媒数据分析</text>
      <view class="header-actions">
        <button class="btn-refresh" @click="refreshData">刷新</button>
        <button class="btn-export" @click="exportReport">导出报告</button>
        <picker mode="date" :value="selectedDate" @change="onDateChange">
          <view class="date-picker">{{ selectedDate }}</view>
        </picker>
      </view>
    </view>
    
    <view class="analytics-overview">
      <view class="overview-card">
        <text class="card-title">总览指标</text>
        <view class="metrics-grid">
          <view class="metric-item">
            <text class="metric-number">{{ overview.totalPosts }}</text>
            <text class="metric-label">总帖子数</text>
          </view>
          <view class="metric-item">
            <text class="metric-number">{{ overview.totalEngagement }}</text>
            <text class="metric-label">总互动量</text>
          </view>
          <view class="metric-item">
            <text class="metric-number">{{ overview.totalReach }}</text>
            <text class="metric-label">总触达量</text>
          </view>
          <view class="metric-item">
            <text class="metric-number">{{ overview.avgEngagementRate }}%</text>
            <text class="metric-label">平均互动率</text>
          </view>
        </view>
      </view>
    </view>
    
    <view class="charts-section">
      <view class="chart-container">
        <text class="chart-title">互动趋势分析</text>
        <view class="chart-wrapper">
          <canvas canvas-id="engagementChart" class="chart-canvas"></canvas>
        </view>
      </view>
      
      <view class="chart-container">
        <text class="chart-title">平台分布对比</text>
        <view class="chart-wrapper">
          <canvas canvas-id="platformChart" class="chart-canvas"></canvas>
        </view>
      </view>
    </view>
    
    <view class="platform-analysis">
      <text class="section-title">平台详细分析</text>
      <view class="platform-cards">
        <view class="platform-card" v-for="platform in platformData" :key="platform.name">
          <view class="platform-header">
            <text class="platform-name">{{ platform.name }}</text>
            <text class="platform-status" :class="getStatusClass(platform.status)">{{ platform.status }}</text>
          </view>
          <view class="platform-metrics">
            <view class="metric-row">
              <text class="metric-name">帖子数:</text>
              <text class="metric-value">{{ platform.posts }}</text>
            </view>
            <view class="metric-row">
              <text class="metric-name">互动量:</text>
              <text class="metric-value">{{ platform.engagement }}</text>
            </view>
            <view class="metric-row">
              <text class="metric-name">触达量:</text>
              <text class="metric-value">{{ platform.reach }}</text>
            </view>
            <view class="metric-row">
              <text class="metric-name">互动率:</text>
              <text class="metric-value">{{ platform.engagementRate }}%</text>
            </view>
          </view>
          <view class="platform-trend">
            <text class="trend-text">{{ platform.trend }}</text>
            <text class="trend-value" :class="getTrendClass(platform.trendValue)">{{ platform.trendValue }}%</text>
          </view>
        </view>
      </view>
    </view>
    
    <view class="content-analysis">
      <text class="section-title">内容分析</text>
      <view class="content-stats">
        <view class="stat-card">
          <text class="stat-title">热门话题</text>
          <view class="topic-list">
            <view class="topic-item" v-for="topic in hotTopics" :key="topic.name">
              <text class="topic-name">{{ topic.name }}</text>
              <view class="topic-bar">
                <view class="topic-fill" :style="{ width: topic.percentage + '%' }"></view>
              </view>
              <text class="topic-count">{{ topic.count }}</text>
            </view>
          </view>
        </view>
        
        <view class="stat-card">
          <text class="stat-title">内容类型分布</text>
          <view class="content-types">
            <view class="type-item" v-for="type in contentTypes" :key="type.name">
              <view class="type-icon">{{ type.icon }}</view>
              <text class="type-name">{{ type.name }}</text>
              <text class="type-percentage">{{ type.percentage }}%</text>
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <view class="audience-analysis">
      <text class="section-title">受众分析</text>
      <view class="audience-cards">
        <view class="audience-card">
          <text class="card-title">年龄分布</text>
          <view class="age-chart">
            <canvas canvas-id="ageChart" class="chart-canvas-small"></canvas>
          </view>
        </view>
        
        <view class="audience-card">
          <text class="card-title">性别分布</text>
          <view class="gender-stats">
            <view class="gender-item">
              <text class="gender-label">男性</text>
              <view class="gender-bar">
                <view class="gender-fill male" :style="{ width: audienceGender.male + '%' }"></view>
              </view>
              <text class="gender-percentage">{{ audienceGender.male }}%</text>
            </view>
            <view class="gender-item">
              <text class="gender-label">女性</text>
              <view class="gender-bar">
                <view class="gender-fill female" :style="{ width: audienceGender.female + '%' }"></view>
              </view>
              <text class="gender-percentage">{{ audienceGender.female }}%</text>
            </view>
          </view>
        </view>
        
        <view class="audience-card">
          <text class="card-title">地域分布</text>
          <view class="location-list">
            <view class="location-item" v-for="location in topLocations" :key="location.name">
              <text class="location-name">{{ location.name }}</text>
              <text class="location-percentage">{{ location.percentage }}%</text>
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <view class="competitor-analysis">
      <text class="section-title">竞品分析</text>
      <view class="competitor-table">
        <view class="table-header">
          <text class="header-cell">竞品名称</text>
          <text class="header-cell">粉丝数</text>
          <text class="header-cell">互动率</text>
          <text class="header-cell">活跃度</text>
          <text class="header-cell">对比结果</text>
        </view>
        <view class="table-row" v-for="competitor in competitors" :key="competitor.name">
          <text class="table-cell">{{ competitor.name }}</text>
          <text class="table-cell">{{ competitor.followers }}</text>
          <text class="table-cell">{{ competitor.engagementRate }}%</text>
          <text class="table-cell">{{ competitor.activity }}</text>
          <text class="table-cell" :class="getComparisonClass(competitor.comparison)">
            {{ competitor.comparison }}
          </text>
        </view>
      </view>
    </view>
    
    <view class="recommendations">
      <text class="section-title">优化建议</text>
      <view class="recommendation-list">
        <view class="recommendation-item" v-for="rec in recommendations" :key="rec.id">
          <view class="rec-header">
            <text class="rec-title">{{ rec.title }}</text>
            <text class="rec-priority" :class="getPriorityClass(rec.priority)">{{ rec.priority }}</text>
          </view>
          <text class="rec-description">{{ rec.description }}</text>
          <view class="rec-actions">
            <button class="btn-apply" @click="applyRecommendation(rec)">应用建议</button>
            <button class="btn-ignore" @click="ignoreRecommendation(rec)">忽略</button>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 实时数据更新指示器 -->
    <view class="real-time-indicator" v-if="isRealTime">
      <view class="indicator-dot"></view>
      <text class="indicator-text">实时数据更新中</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedDate: new Date().toISOString().split('T')[0],
      overview: {
        totalPosts: 1256,
        totalEngagement: 45892,
        totalReach: 234567,
        avgEngagementRate: 3.2
      },
      platformData: [
        {
          name: '微信',
          status: '活跃',
          posts: 456,
          engagement: 12345,
          reach: 67890,
          engagementRate: 2.7,
          trend: '上升',
          trendValue: 12.5
        },
        {
          name: '微博',
          status: '活跃',
          posts: 234,
          engagement: 9876,
          reach: 45678,
          engagementRate: 4.1,
          trend: '下降',
          trendValue: -5.2
        },
        {
          name: '抖音',
          status: '活跃',
          posts: 345,
          engagement: 15678,
          reach: 89012,
          engagementRate: 5.8,
          trend: '上升',
          trendValue: 23.1
        },
        {
          name: '小红书',
          status: '活跃',
          posts: 221,
          engagement: 7993,
          reach: 31987,
          engagementRate: 3.9,
          trend: '上升',
          trendValue: 8.7
        }
      ],
      hotTopics: [
        { name: '数字化转型', count: 156, percentage: 35 },
        { name: '人工智能', count: 134, percentage: 30 },
        { name: '绿色发展', count: 98, percentage: 22 },
        { name: '创新科技', count: 58, percentage: 13 }
      ],
      contentTypes: [
        { name: '图文', icon: '📝', percentage: 45 },
        { name: '视频', icon: '🎥', percentage: 30 },
        { name: '直播', icon: '📺', percentage: 15 },
        { name: '音频', icon: '🎵', percentage: 10 }
      ],
      audienceGender: { male: 58, female: 42 },
      topLocations: [
        { name: '北京', percentage: 25 },
        { name: '上海', percentage: 20 },
        { name: '广州', percentage: 15 },
        { name: '深圳', percentage: 12 },
        { name: '杭州', percentage: 8 }
      ],
      competitors: [
        {
          name: '竞品A',
          followers: '125K',
          engagementRate: 2.8,
          activity: '高',
          comparison: '领先'
        },
        {
          name: '竞品B',
          followers: '89K',
          engagementRate: 3.5,
          activity: '中',
          comparison: '持平'
        },
        {
          name: '竞品C',
          followers: '156K',
          engagementRate: 2.1,
          activity: '低',
          comparison: '落后'
        }
      ],
      recommendations: [
        {
          id: 1,
          title: '增加视频内容发布频率',
          description: '视频内容的互动率比图文内容高30%，建议增加视频发布频率到每周3-4个',
          priority: '高'
        },
        {
          id: 2,
          title: '优化发布时间',
          description: '数据显示晚上8-10点是用户活跃高峰期，建议调整内容发布时间',
          priority: '中'
        },
        {
          id: 3,
          title: '加强用户互动',
          description: '回复用户评论可以提高用户粘性，建议增加回复频率和质量',
          priority: '中'
        }
      ],
      isRealTime: false,
      realTimeInterval: null
    }
  },
  onLoad() {
    this.initCharts()
    this.startRealTimeUpdate()
  },
  onUnload() {
    if (this.realTimeInterval) {
      clearInterval(this.realTimeInterval)
    }
  },
  methods: {
    initCharts() {
      this.drawEngagementChart()
      this.drawPlatformChart()
      this.drawAgeChart()
    },
    drawEngagementChart() {
      const ctx = uni.createCanvasContext('engagementChart', this)
      
      // 绘制简单的折线图
      const data = [120, 190, 300, 500, 200, 300, 450, 380, 420, 350, 480, 520]
      const labels = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
      
      const width = 300
      const height = 200
      const padding = 40
      
      // 清除画布
      ctx.clearRect(0, 0, width, height)
      
      // 绘制坐标轴
      ctx.beginPath()
      ctx.moveTo(padding, padding)
      ctx.lineTo(padding, height - padding)
      ctx.lineTo(width - padding, height - padding)
      ctx.strokeStyle = '#ccc'
      ctx.stroke()
      
      // 绘制数据线
      ctx.beginPath()
      const stepX = (width - 2 * padding) / (data.length - 1)
      const maxValue = Math.max(...data)
      const stepY = (height - 2 * padding) / maxValue
      
      data.forEach((value, index) => {
        const x = padding + index * stepX
        const y = height - padding - value * stepY
        
        if (index === 0) {
          ctx.moveTo(x, y)
        } else {
          ctx.lineTo(x, y)
        }
      })
      
      ctx.strokeStyle = '#007aff'
      ctx.lineWidth = 2
      ctx.stroke()
      
      // 绘制数据点
      data.forEach((value, index) => {
        const x = padding + index * stepX
        const y = height - padding - value * stepY
        
        ctx.beginPath()
        ctx.arc(x, y, 4, 0, 2 * Math.PI)
        ctx.fillStyle = '#007aff'
        ctx.fill()
      })
      
      ctx.draw()
    },
    drawPlatformChart() {
      const ctx = uni.createCanvasContext('platformChart', this)
      
      // 绘制饼图
      const data = [35, 30, 22, 13] // 对应平台百分比
      const colors = ['#007aff', '#28a745', '#ffc107', '#dc3545']
      const labels = ['微信', '微博', '抖音', '小红书']
      
      const width = 300
      const height = 200
      const centerX = width / 2
      const centerY = height / 2
      const radius = 80
      
      ctx.clearRect(0, 0, width, height)
      
      let currentAngle = -Math.PI / 2
      
      data.forEach((value, index) => {
        const sliceAngle = (value / 100) * 2 * Math.PI
        
        ctx.beginPath()
        ctx.moveTo(centerX, centerY)
        ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + sliceAngle)
        ctx.closePath()
        ctx.fillStyle = colors[index]
        ctx.fill()
        
        // 绘制标签
        const labelAngle = currentAngle + sliceAngle / 2
        const labelX = centerX + Math.cos(labelAngle) * (radius + 20)
        const labelY = centerY + Math.sin(labelAngle) * (radius + 20)
        
        ctx.fillStyle = '#333'
        ctx.font = '12px sans-serif'
        ctx.textAlign = 'center'
        ctx.fillText(labels[index], labelX, labelY)
        
        currentAngle += sliceAngle
      })
      
      ctx.draw()
    },
    drawAgeChart() {
      const ctx = uni.createCanvasContext('ageChart', this)
      
      // 绘制年龄分布柱状图
      const data = [15, 25, 35, 20, 5] // 年龄段百分比
      const labels = ['18-24', '25-34', '35-44', '45-54', '55+']
      
      const width = 250
      const height = 150
      const padding = 30
      
      ctx.clearRect(0, 0, width, height)
      
      const barWidth = (width - 2 * padding) / data.length - 10
      const maxValue = Math.max(...data)
      const scale = (height - 2 * padding) / maxValue
      
      data.forEach((value, index) => {
        const x = padding + index * (barWidth + 10)
        const barHeight = value * scale
        const y = height - padding - barHeight
        
        ctx.fillStyle = '#007aff'
        ctx.fillRect(x, y, barWidth, barHeight)
        
        ctx.fillStyle = '#333'
        ctx.font = '10px sans-serif'
        ctx.textAlign = 'center'
        ctx.fillText(labels[index], x + barWidth / 2, height - padding + 15)
        ctx.fillText(value + '%', x + barWidth / 2, y - 5)
      })
      
      ctx.draw()
    },
    onDateChange(e) {
      this.selectedDate = e.detail.value
      this.refreshData()
    },
    refreshData() {
      uni.showLoading({ title: '刷新数据中...' })
      setTimeout(() => {
        // 模拟数据更新
        this.overview.totalPosts += Math.floor(Math.random() * 50)
        this.overview.totalEngagement += Math.floor(Math.random() * 1000)
        this.overview.totalReach += Math.floor(Math.random() * 5000)
        this.overview.avgEngagementRate = (Math.random() * 2 + 2).toFixed(1)
        
        this.initCharts()
        uni.hideLoading()
        uni.showToast({ title: '数据已刷新', icon: 'success' })
      }, 1500)
    },
    exportReport() {
      uni.showModal({
        title: '导出报告',
        content: '确定要导出当前分析报告吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '生成报告中...' })
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({ title: '报告已导出', icon: 'success' })
            }, 2000)
          }
        }
      })
    },
    getStatusClass(status) {
      return {
        '活跃': 'status-active',
        '正常': 'status-normal',
        '异常': 'status-error'
      }[status] || 'status-default'
    },
    getTrendClass(trendValue) {
      return trendValue > 0 ? 'trend-up' : 'trend-down'
    },
    getComparisonClass(comparison) {
      return {
        '领先': 'comparison-lead',
        '持平': 'comparison-equal',
        '落后': 'comparison-lag'
      }[comparison] || 'comparison-default'
    },
    getPriorityClass(priority) {
      return {
        '高': 'priority-high',
        '中': 'priority-medium',
        '低': 'priority-low'
      }[priority] || 'priority-default'
    },
    applyRecommendation(rec) {
      uni.showModal({
        title: '应用建议',
        content: `确定要应用"${rec.title}"这个建议吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '建议已应用', icon: 'success' })
          }
        }
      })
    },
    ignoreRecommendation(rec) {
      const index = this.recommendations.indexOf(rec)
      if (index > -1) {
        this.recommendations.splice(index, 1)
        uni.showToast({ title: '建议已忽略', icon: 'success' })
      }
    },
    startRealTimeUpdate() {
      this.isRealTime = true
      this.realTimeInterval = setInterval(() => {
        // 模拟实时数据更新
        this.overview.totalEngagement += Math.floor(Math.random() * 100)
        this.overview.totalReach += Math.floor(Math.random() * 500)
        
        // 更新平台数据
        this.platformData.forEach(platform => {
          if (Math.random() > 0.7) {
            platform.engagement += Math.floor(Math.random() * 50)
            platform.reach += Math.floor(Math.random() * 200)
          }
        })
        
        this.updateCharts()
      }, 5000) // 每5秒更新一次
    },
    updateCharts() {
      // 重新绘制图表以反映数据变化
      this.drawEngagementChart()
      this.drawPlatformChart()
    }
  }
}
</script>

<style>
.social-analytics {
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
  align-items: center;
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

.date-picker {
  background: #f8f9fa;
  border: 1rpx solid #ddd;
  border-radius: 8rpx;
  padding: 12rpx 20rpx;
  font-size: 24rpx;
  color: #333;
}

.analytics-overview {
  margin-bottom: 30rpx;
}

.overview-card {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.card-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.metric-item {
  text-align: center;
}

.metric-number {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #007aff;
  margin-bottom: 5rpx;
}

.metric-label {
  font-size: 20rpx;
  color: #666;
}

.charts-section {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.chart-container {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.chart-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}

.chart-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200rpx;
}

.chart-canvas {
  width: 300rpx;
  height: 200rpx;
}

.chart-canvas-small {
  width: 250rpx;
  height: 150rpx;
}

.platform-analysis {
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}

.platform-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.platform-card {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.platform-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.platform-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.platform-status {
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
  font-weight: bold;
}

.status-active {
  background-color: #d4edda;
  color: #155724;
}

.status-normal {
  background-color: #d1ecf1;
  color: #0c5460;
}

.status-error {
  background-color: #f5c6cb;
  color: #721c24;
}

.status-default {
  background-color: #e2e3e5;
  color: #383d41;
}

.platform-metrics {
  margin-bottom: 20rpx;
}

.metric-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.metric-name {
  font-size: 24rpx;
  color: #666;
}

.metric-value {
  font-size: 24rpx;
  color: #333;
  font-weight: 500;
}

.platform-trend {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20rpx;
  border-top: 1rpx solid #eee;
}

.trend-text {
  font-size: 24rpx;
  color: #666;
}

.trend-value {
  font-size: 24rpx;
  font-weight: bold;
}

.trend-up {
  color: #28a745;
}

.trend-down {
  color: #dc3545;
}

.content-analysis {
  margin-bottom: 30rpx;
}

.content-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.stat-card {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.stat-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}

.topic-list {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.topic-item {
  display: flex;
  align-items: center;
  gap: 15rpx;
}

.topic-name {
  font-size: 24rpx;
  color: #333;
  width: 120rpx;
}

.topic-bar {
  flex: 1;
  height: 20rpx;
  background: #e9ecef;
  border-radius: 10rpx;
  overflow: hidden;
}

.topic-fill {
  height: 100%;
  background: linear-gradient(90deg, #007bff, #0056b3);
  border-radius: 10rpx;
  transition: width 0.3s ease;
}

.topic-count {
  font-size: 24rpx;
  color: #666;
  width: 60rpx;
  text-align: right;
}

.content-types {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.type-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 8rpx;
}

.type-icon {
  font-size: 32rpx;
  margin-bottom: 10rpx;
}

.type-name {
  font-size: 24rpx;
  color: #333;
  margin-bottom: 5rpx;
}

.type-percentage {
  font-size: 20rpx;
  color: #666;
}

.audience-analysis {
  margin-bottom: 30rpx;
}

.audience-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
}

.audience-card {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.age-chart,
.gender-stats,
.location-list {
  margin-top: 20rpx;
}

.gender-item {
  display: flex;
  align-items: center;
  gap: 15rpx;
  margin-bottom: 15rpx;
}

.gender-label {
  font-size: 24rpx;
  color: #333;
  width: 60rpx;
}

.gender-bar {
  flex: 1;
  height: 20rpx;
  background: #e9ecef;
  border-radius: 10rpx;
  overflow: hidden;
}

.gender-fill {
  height: 100%;
  border-radius: 10rpx;
  transition: width 0.3s ease;
}

.gender-fill.male {
  background: #007bff;
}

.gender-fill.female {
  background: #e91e63;
}

.gender-percentage {
  font-size: 24rpx;
  color: #666;
  width: 60rpx;
  text-align: right;
}

.location-list {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.location-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.location-name {
  font-size: 24rpx;
  color: #333;
}

.location-percentage {
  font-size: 24rpx;
  color: #666;
}

.competitor-analysis {
  margin-bottom: 30rpx;
}

.competitor-table {
  background: white;
  border-radius: 12rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.table-header {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 1fr;
  background: #f8f9fa;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #eee;
}

.header-cell {
  font-size: 24rpx;
  font-weight: bold;
  color: #333;
}

.table-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr 1fr;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #eee;
  align-items: center;
}

.table-row:last-child {
  border-bottom: none;
}

.table-cell {
  font-size: 24rpx;
  color: #333;
}

.comparison-lead {
  color: #28a745;
  font-weight: bold;
}

.comparison-equal {
  color: #ffc107;
  font-weight: bold;
}

.comparison-lag {
  color: #dc3545;
  font-weight: bold;
}

.comparison-default {
  color: #666;
}

.recommendations {
  margin-bottom: 30rpx;
}

.recommendation-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.recommendation-item {
  background: white;
  border-radius: 12rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.rec-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.rec-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.rec-priority {
  padding: 4rpx 12rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
  font-weight: bold;
}

.priority-high {
  background-color: #f5c6cb;
  color: #721c24;
}

.priority-medium {
  background-color: #fff3cd;
  color: #856404;
}

.priority-low {
  background-color: #d1ecf1;
  color: #0c5460;
}

.priority-default {
  background-color: #e2e3e5;
  color: #383d41;
}

.rec-description {
  font-size: 24rpx;
  color: #666;
  line-height: 1.5;
  margin-bottom: 20rpx;
}

.rec-actions {
  display: flex;
  gap: 15rpx;
}

.btn-apply {
  background-color: #28a745;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.btn-ignore {
  background-color: #6c757d;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.real-time-indicator {
  position: fixed;
  top: 100rpx;
  right: 30rpx;
  background: rgba(40, 167, 69, 0.9);
  color: white;
  padding: 15rpx 25rpx;
  border-radius: 25rpx;
  display: flex;
  align-items: center;
  gap: 10rpx;
  z-index: 100;
}

.indicator-dot {
  width: 12rpx;
  height: 12rpx;
  background: white;
  border-radius: 50%;
  animation: pulse 1.5s infinite;
}

.indicator-text {
  font-size: 22rpx;
  font-weight: bold;
}

@keyframes pulse {
  0% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.7;
    transform: scale(1.1);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}
</style>