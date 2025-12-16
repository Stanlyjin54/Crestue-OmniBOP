<template>
  <view class="tender-collection">
    <view class="header">
      <text class="title">招标信息采集</text>
      <view class="actions">
        <button class="btn-primary" @click="startCollection">开始采集</button>
        <button class="btn-secondary" @click="exportTenders">导出招标</button>
      </view>
    </view>
    
    <view class="filter-container">
      <picker mode="selector" :range="regions" :value="selectedRegion" @change="onRegionChange">
        <view class="picker-item">
          <text>地区: {{ regions[selectedRegion] }}</text>
        </view>
      </picker>
      <picker mode="selector" :range="categories" :value="selectedCategory" @change="onCategoryChange">
        <view class="picker-item">
          <text>类别: {{ categories[selectedCategory] }}</text>
        </view>
      </picker>
      <picker mode="selector" :range="statuses" :value="selectedStatus" @change="onStatusChange">
        <view class="picker-item">
          <text>状态: {{ statuses[selectedStatus] }}</text>
        </view>
      </picker>
      <input class="search-input" v-model="keyword" placeholder="搜索招标信息" />
      <button class="btn-search" @click="searchTenders">搜索</button>
    </view>
    
    <scroll-view class="tender-list" scroll-y>
      <view class="tender-item" v-for="tender in tenderList" :key="tender.id" @click="viewTenderDetail(tender)">
        <view class="tender-header">
          <text class="tender-title">{{ tender.title }}</text>
          <text class="tender-status" :class="getStatusClass(tender.status)">{{ tender.status }}</text>
        </view>
        <view class="tender-content">
          <text class="tender-description">{{ tender.description }}</text>
        </view>
        <view class="tender-info">
          <view class="info-row">
            <text class="info-label">招标编号:</text>
            <text class="info-value">{{ tender.code }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">招标机构:</text>
            <text class="info-value">{{ tender.agency }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">发布时间:</text>
            <text class="info-value">{{ tender.publishDate }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">截止时间:</text>
            <text class="info-value">{{ tender.deadline }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">预算金额:</text>
            <text class="info-value amount">{{ tender.budget }}</text>
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
      regions: ['全部地区', '北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '西安'],
      selectedRegion: 0,
      categories: ['全部类别', '办公用品', 'IT设备', '医疗设备', '教育设备', '交通工具', '建筑工程', '服务采购'],
      selectedCategory: 0,
      statuses: ['全部状态', '招标中', '已截止', '已完成', '流标'],
      selectedStatus: 0,
      keyword: '',
      tenderList: [
        {
          id: 1,
          title: '某市政府办公用品采购项目',
          code: 'ZB20250115001',
          description: '采购办公桌椅、电脑、打印机等办公用品，要求供应商具备相关资质和良好的售后服务能力。',
          agency: '某市政府采购中心',
          publishDate: '2025-01-15',
          deadline: '2025-02-15',
          budget: '50万元',
          status: '招标中',
          region: '北京',
          category: '办公用品'
        },
        {
          id: 2,
          title: '某县教育局教学设备采购',
          code: 'ZB20250114002',
          description: '采购多媒体教学设备、实验仪器、体育器材等，提升教学质量。',
          agency: '某县教育局',
          publishDate: '2025-01-14',
          deadline: '2025-02-10',
          budget: '120万元',
          status: '已截止',
          region: '上海',
          category: '教育设备'
        },
        {
          id: 3,
          title: '某医院医疗设备采购项目',
          code: 'ZB20250113003',
          description: '采购先进的医疗诊断设备和治疗仪器，要求设备性能稳定可靠。',
          agency: '某市人民医院',
          publishDate: '2025-01-13',
          deadline: '2025-02-20',
          budget: '300万元',
          status: '招标中',
          region: '广州',
          category: '医疗设备'
        },
        {
          id: 4,
          title: '某交通局执法车辆采购',
          code: 'ZB20250112004',
          description: '采购执法执勤车辆，要求车辆性能良好，符合相关技术标准。',
          agency: '某市交通局',
          publishDate: '2025-01-12',
          deadline: '2025-02-05',
          budget: '200万元',
          status: '招标中',
          region: '深圳',
          category: '交通工具'
        }
      ]
    }
  },
  methods: {
    startCollection() {
      uni.showToast({
        title: '开始采集招标信息',
        icon: 'success'
      })
      // 调用API开始采集
    },
    exportTenders() {
      uni.showToast({
        title: '导出招标信息',
        icon: 'success'
      })
      // 调用API导出招标信息
    },
    onRegionChange(e) {
      this.selectedRegion = e.detail.value
      this.filterTenders()
    },
    onCategoryChange(e) {
      this.selectedCategory = e.detail.value
      this.filterTenders()
    },
    onStatusChange(e) {
      this.selectedStatus = e.detail.value
      this.filterTenders()
    },
    searchTenders() {
      this.filterTenders()
    },
    filterTenders() {
      uni.showToast({
        title: '筛选招标信息',
        icon: 'success'
      })
      // 根据筛选条件调用API获取招标信息
    },
    viewTenderDetail(tender) {
      uni.navigateTo({
        url: `/pages/procurement/procurement-opportunities?id=${tender.id}`
      })
    },
    getStatusClass(status) {
      const statusClass = {
        '招标中': 'status-active',
        '已截止': 'status-expired',
        '已完成': 'status-completed',
        '流标': 'status-failed'
      }
      return statusClass[status] || 'status-default'
    }
  }
}
</script>

<style>
.tender-collection {
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

.picker-item {
  background: #f8f9fa;
  padding: 16rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #333;
  min-width: 180rpx;
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

.tender-list {
  background: white;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
  max-height: 800rpx;
}

.tender-item {
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
  cursor: pointer;
}

.tender-item:last-child {
  border-bottom: none;
}

.tender-item:active {
  background-color: #f8f9fa;
}

.tender-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.tender-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  flex: 1;
  margin-right: 20rpx;
  line-height: 1.4;
}

.tender-status {
  padding: 8rpx 16rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
  font-weight: bold;
  white-space: nowrap;
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

.status-failed {
  background-color: #fff3cd;
  color: #856404;
}

.status-default {
  background-color: #e2e3e5;
  color: #383d41;
}

.tender-content {
  margin-bottom: 20rpx;
}

.tender-description {
  font-size: 24rpx;
  color: #666;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.tender-info {
  background-color: #f8f9fa;
  padding: 20rpx;
  border-radius: 8rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-label {
  font-size: 24rpx;
  color: #999;
  min-width: 120rpx;
}

.info-value {
  font-size: 24rpx;
  color: #333;
  font-weight: 500;
  flex: 1;
  text-align: right;
}

.amount {
  color: #dc3545;
  font-weight: bold;
  font-size: 28rpx;
}
</style>