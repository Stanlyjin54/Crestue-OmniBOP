<template>
  <view class="procurement-dashboard">
    <view class="header">
      <text class="title">政府采购信息采集</text>
      <view class="actions">
        <button class="btn-primary" @click="startCrawling">开始采集</button>
        <button class="btn-secondary" @click="exportData">导出数据</button>
      </view>
    </view>
    
    <view class="stats-container">
      <view class="stat-card">
        <text class="stat-number">{{ stats.totalTenders }}</text>
        <text class="stat-label">总招标数</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.todayTenders }}</text>
        <text class="stat-label">今日新增</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.matchedOpportunities }}</text>
        <text class="stat-label">匹配商机</text>
      </view>
      <view class="stat-card">
        <text class="stat-number">{{ stats.crawlerStatus }}</text>
        <text class="stat-label">采集状态</text>
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
      <input class="search-input" v-model="keyword" placeholder="搜索关键词" />
      <button class="btn-search" @click="searchTenders">搜索</button>
    </view>
    
    <scroll-view class="tender-list" scroll-y>
      <view class="tender-item" v-for="tender in tenderList" :key="tender.id" @click="viewTenderDetail(tender)">
        <view class="tender-header">
          <text class="tender-title">{{ tender.title }}</text>
          <text class="tender-status" :class="getStatusClass(tender.status)">{{ tender.status }}</text>
        </view>
        <view class="tender-info">
          <text class="tender-agency">{{ tender.agency }}</text>
          <text class="tender-date">{{ tender.publishDate }}</text>
        </view>
        <view class="tender-amount">
          <text class="amount-label">预算金额:</text>
          <text class="amount-value">{{ tender.budget }}</text>
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
        totalTenders: 156,
        todayTenders: 12,
        matchedOpportunities: 8,
        crawlerStatus: '运行中'
      },
      startDate: '2025-01-01',
      endDate: '2025-12-31',
      keyword: '',
      tenderList: [
        {
          id: 1,
          title: '某市政府办公用品采购项目',
          agency: '某市政府采购中心',
          publishDate: '2025-01-15',
          budget: '50万元',
          status: '招标中'
        },
        {
          id: 2,
          title: '某县教育局教学设备采购',
          agency: '某县教育局',
          publishDate: '2025-01-14',
          budget: '120万元',
          status: '已截止'
        },
        {
          id: 3,
          title: '某医院医疗设备采购项目',
          agency: '某市人民医院',
          publishDate: '2025-01-13',
          budget: '300万元',
          status: '招标中'
        }
      ]
    }
  },
  methods: {
    startCrawling() {
      uni.showToast({
        title: '开始采集政府采购信息',
        icon: 'success'
      })
      // 调用API开始采集
    },
    exportData() {
      uni.showToast({
        title: '导出数据功能',
        icon: 'success'
      })
      // 调用API导出数据
    },
    onStartDateChange(e) {
      this.startDate = e.detail.value
    },
    onEndDateChange(e) {
      this.endDate = e.detail.value
    },
    searchTenders() {
      uni.showToast({
        title: '搜索功能',
        icon: 'success'
      })
      // 调用API搜索招标信息
    },
    viewTenderDetail(tender) {
      uni.navigateTo({
        url: `/pages/procurement/tender-collection?id=${tender.id}`
      })
    },
    getStatusClass(status) {
      const statusClass = {
        '招标中': 'status-active',
        '已截止': 'status-expired',
        '已完成': 'status-completed'
      }
      return statusClass[status] || 'status-default'
    }
  }
}
</script>

<style>
.procurement-dashboard {
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
  gap: 20rpx;
  margin-bottom: 30rpx;
  background: white;
  padding: 20rpx;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.date-picker {
  background: #f8f9fa;
  padding: 16rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #333;
}

.search-input {
  flex: 1;
  background: #f8f9fa;
  padding: 16rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  border: 1rpx solid #ddd;
}

.btn-search {
  background-color: #28a745;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.tender-list {
  background: white;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
  max-height: 600rpx;
}

.tender-item {
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.tender-item:last-child {
  border-bottom: none;
}

.tender-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.tender-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  flex: 1;
}

.tender-status {
  padding: 8rpx 16rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
  font-weight: bold;
}

.status-active {
  background-color: #d4edda;
  color: #155724;
}

.status-expired {
  background-color: #f8d7da;
  color: #721c24;
}

.status-completed {
  background-color: #d1ecf1;
  color: #0c5460;
}

.status-default {
  background-color: #e2e3e5;
  color: #383d41;
}

.tender-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.tender-agency {
  font-size: 24rpx;
  color: #666;
}

.tender-date {
  font-size: 24rpx;
  color: #999;
}

.tender-amount {
  display: flex;
  align-items: center;
}

.amount-label {
  font-size: 24rpx;
  color: #666;
  margin-right: 10rpx;
}

.amount-value {
  font-size: 28rpx;
  font-weight: bold;
  color: #dc3545;
}
</style>