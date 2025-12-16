<template>
  <view class="social-media-dashboard">
    <view class="header">
      <text class="title">社交媒体商机</text>
      <view class="actions">
        <button class="btn-primary" @click="startMonitoring">开始监控</button>
        <button class="btn-secondary" @click="exportReport">导出报告</button>
      </view>
    </view>
    
    <view class="stats-container">
      <view class="stat-card">
        <text class="stat-number">{{ stats.totalOpportunities }}</text>
        <text class="stat-label">总商机数</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.todayOpportunities }}</text>
        <text class="stat-label">今日新增</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.complianceRate }}%</text>
        <text class="stat-label">合规率</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.monitorStatus }}</text>
        <text class="stat-label">监控状态</text>
      </view>
    </view>
    
    <view class="filter-container">
      <picker mode="date" :value="startDate" @change="onStartDateChange">
        <view class="date-picker">
          <text>开始日期: {{ startDate }}</text>
        </view>
      </picker>
      <picker mode="date" :value="endDate" @change="onEndDateChange">
        <view class="date-picker">
          <text>结束日期: {{ endDate }}</text>
        </view>
      </picker>
      <picker mode="selector" :range="platforms" :value="selectedPlatform" @change="onPlatformChange">
        <view class="platform-picker">
          <text>平台: {{ platforms[selectedPlatform] }}</text>
        </view>
      </picker>
      <input class="search-input" v-model="keyword" placeholder="搜索关键词" />
      <button class="btn-search" @click="searchOpportunities">搜索</button>
    </view>
    
    <scroll-view class="opportunity-list" scroll-y>
      <view class="opportunity-item" v-for="opportunity in opportunityList" :key="opportunity.id" @click="viewOpportunityDetail(opportunity)">
        <view class="opportunity-header">
          <text class="opportunity-title">{{ opportunity.title }}</text>
          <text class="opportunity-platform">{{ opportunity.platform }}</text>
        </view>
        <view class="opportunity-content">
          <text class="opportunity-description">{{ opportunity.description }}</text>
        </view>
        <view class="opportunity-info">
          <view class="info-item">
            <text class="info-label">发布时间:</text>
            <text class="info-value">{{ opportunity.publishDate }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">互动量:</text>
            <text class="info-value">{{ opportunity.interaction }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">合规状态:</text>
            <text class="info-value" :class="getComplianceClass(opportunity.compliance)">{{ opportunity.compliance }}</text>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      stats: {
        totalOpportunities: 89,
        todayOpportunities: 15,
        complianceRate: 92,
        monitorStatus: '运行中'
      },
      startDate: '2025-01-01',
      endDate: '2025-12-31',
      platforms: ['全部平台', '微信', '微博', '抖音', '小红书', '知乎', 'B站'],
      selectedPlatform: 0,
      keyword: '',
      opportunityList: [
        {
          id: 1,
          title: '某品牌新品发布讨论',
          platform: '微博',
          description: '用户对新发布的智能产品表现出浓厚兴趣，讨论热度持续上升...',
          publishDate: '2025-01-15',
          interaction: '1.2k',
          compliance: '合规'
        },
        {
          id: 2,
          title: '教育行业需求调研',
          platform: '知乎',
          description: '多个用户询问在线教育平台的功能和体验，存在潜在合作机会...',
          publishDate: '2025-01-14',
          interaction: '856',
          compliance: '合规'
        },
        {
          id: 3,
          title: '健康生活方式分享',
          platform: '小红书',
          description: '用户分享健康生活理念，对健康管理类应用需求增加...',
          publishDate: '2025-01-13',
          interaction: '2.3k',
          compliance: '需审核'
        }
      ]
    }
  },
  methods: {
    startMonitoring() {
      uni.showToast({
        title: '开始社交媒体监控',
        icon: 'success'
      })
      // 调用API开始监控
    },
    exportReport() {
      uni.showToast({
        title: '导出监控报告',
        icon: 'success'
      })
      // 调用API导出报告
    },
    onStartDateChange(e) {
      this.startDate = e.detail.value
    },
    onEndDateChange(e) {
      this.endDate = e.detail.value
    },
    onPlatformChange(e) {
      this.selectedPlatform = e.detail.value
    },
    searchOpportunities() {
      uni.showToast({
        title: '搜索商机',
        icon: 'success'
      })
      // 调用API搜索商机
    },
    viewOpportunityDetail(opportunity) {
      uni.navigateTo({
        url: `/pages/social-media/opportunity?id=${opportunity.id}`
      })
    },
    getComplianceClass(compliance) {
      const classMap = {
        '合规': 'compliance-pass',
        '不合规': 'compliance-fail',
        '需审核': 'compliance-review'
      }
      return classMap[compliance] || 'compliance-default'
    }
  }
}
</script>

<style>
.social-media-dashboard {
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
  background-color: #007aff;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.stats-container {
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

.filter-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-bottom: 30rpx;
  background: white;
  padding: 20rpx;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.date-picker, .platform-picker {
  background: #f8f9fa;
  padding: 16rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #333;
  min-width: 200rpx;
}

.search-input {
  flex: 1;
  background: #f8f9fa;
  padding: 16rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  border: 1rpx solid #ddd;
  min-width: 300rpx;
}

.btn-search {
  background-color: #28a745;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.opportunity-list {
  background: white;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
  max-height: 600rpx;
}

.opportunity-item {
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
  cursor: pointer;
}

.opportunity-item:last-child {
  border-bottom: none;
}

.opportunity-item:active {
  background-color: #f8f9fa;
}

.opportunity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.opportunity-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  flex: 1;
}

.opportunity-platform {
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 8rpx 16rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
  font-weight: bold;
}

.opportunity-content {
  margin-bottom: 20rpx;
}

.opportunity-description {
  font-size: 24rpx;
  color: #666;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.opportunity-info {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 20rpx;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.info-label {
  font-size: 24rpx;
  color: #999;
}

.info-value {
  font-size: 24rpx;
  color: #333;
  font-weight: 500;
}

.compliance-pass {
  color: #28a745;
  font-weight: bold;
}

.compliance-fail {
  color: #dc3545;
  font-weight: bold;
}

.compliance-review {
  color: #ffc107;
  font-weight: bold;
}

.compliance-default {
  color: #6c757d;
}
</style>