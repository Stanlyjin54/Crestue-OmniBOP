<template>
  <view class="opportunity-management">
    <view class="header">
      <text class="title">商机管理</text>
      <view class="actions">
        <button class="btn-primary" @click="createOpportunity">新建商机</button>
        <button class="btn-secondary" @click="exportOpportunities">导出商机</button>
      </view>
    </view>
    
    <view class="filter-container">
      <picker mode="selector" :range="platforms" :value="selectedPlatform" @change="onPlatformChange">
        <view class="picker-item">
          <text>平台: {{ platforms[selectedPlatform] }}</text>
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
      <input class="search-input" v-model="keyword" placeholder="搜索商机信息" />
      <button class="btn-search" @click="searchOpportunities">搜索</button>
    </view>
    
    <scroll-view class="opportunity-list" scroll-y>
      <view class="opportunity-item" v-for="opportunity in opportunityList" :key="opportunity.id" @click="viewOpportunityDetail(opportunity)">
        <view class="opportunity-header">
          <text class="opportunity-title">{{ opportunity.title }}</text>
          <view class="opportunity-tags">
            <text class="tag platform">{{ opportunity.platform }}</text>
            <text class="tag status" :class="getStatusClass(opportunity.status)">{{ opportunity.status }}</text>
          </view>
        </view>
        <view class="opportunity-content">
          <text class="opportunity-description">{{ opportunity.description }}</text>
        </view>
        <view class="opportunity-info">
          <view class="info-row">
            <text class="info-label">商机编号:</text>
            <text class="info-value">{{ opportunity.code }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">发现时间:</text>
            <text class="info-value">{{ opportunity.discoverDate }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">跟进状态:</text>
            <text class="info-value">{{ opportunity.followUpStatus }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">预计价值:</text>
            <text class="info-value amount">{{ opportunity.estimatedValue }}</text>
          </view>
        </view>
        <view class="opportunity-actions">
          <button class="btn-follow" @click.stop="followUp(opportunity)">跟进</button>
          <button class="btn-convert" @click.stop="convertOpportunity(opportunity)">转化</button>
          <button class="btn-close" @click.stop="closeOpportunity(opportunity)">关闭</button>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      platforms: ['全部平台', '微信', '微博', '抖音', '小红书', '知乎', 'B站', '快手'],
      selectedPlatform: 0,
      categories: ['全部类别', '产品销售', '服务咨询', '合作推广', '用户反馈', '市场调研', '品牌建设'],
      selectedCategory: 0,
      statuses: ['全部状态', '新发现', '跟进中', '已转化', '已关闭', '待审核'],
      selectedStatus: 0,
      keyword: '',
      opportunityList: [
        {
          id: 1,
          title: '某品牌新品发布讨论商机',
          code: 'OP20250115001',
          description: '用户对新发布的智能产品表现出浓厚兴趣，讨论热度持续上升，存在潜在销售机会。',
          platform: '微博',
          discoverDate: '2025-01-15',
          followUpStatus: '初次接触',
          estimatedValue: '50万元',
          status: '新发现',
          category: '产品销售'
        },
        {
          id: 2,
          title: '教育行业需求调研商机',
          code: 'OP20250114002',
          description: '多个用户询问在线教育平台的功能和体验，存在潜在合作机会，需要进一步跟进。',
          platform: '知乎',
          discoverDate: '2025-01-14',
          followUpStatus: '深度沟通',
          estimatedValue: '80万元',
          status: '跟进中',
          category: '服务咨询'
        },
        {
          id: 3,
          title: '健康生活方式分享商机',
          code: 'OP20250113003',
          description: '用户分享健康生活理念，对健康管理类应用需求增加，可考虑产品推广合作。',
          platform: '小红书',
          discoverDate: '2025-01-13',
          followUpStatus: '方案制定',
          estimatedValue: '30万元',
          status: '跟进中',
          category: '合作推广'
        },
        {
          id: 4,
          title: '企业服务优化反馈商机',
          code: 'OP20250112004',
          description: '企业用户反馈现有服务存在的问题，希望获得更好的解决方案，存在服务升级机会。',
          platform: '微信',
          discoverDate: '2025-01-12',
          followUpStatus: '合同签署',
          estimatedValue: '120万元',
          status: '已转化',
          category: '服务咨询'
        }
      ]
    }
  },
  methods: {
    createOpportunity() {
      uni.showToast({
        title: '创建新商机',
        icon: 'success'
      })
      // 跳转到商机创建页面
    },
    exportOpportunities() {
      uni.showToast({
        title: '导出商机数据',
        icon: 'success'
      })
      // 调用API导出商机数据
    },
    onPlatformChange(e) {
      this.selectedPlatform = e.detail.value
      this.filterOpportunities()
    },
    onCategoryChange(e) {
      this.selectedCategory = e.detail.value
      this.filterOpportunities()
    },
    onStatusChange(e) {
      this.selectedStatus = e.detail.value
      this.filterOpportunities()
    },
    searchOpportunities() {
      this.filterOpportunities()
    },
    filterOpportunities() {
      uni.showToast({
        title: '筛选商机',
        icon: 'success'
      })
      // 根据筛选条件调用API获取商机列表
    },
    viewOpportunityDetail(opportunity) {
      uni.navigateTo({
        url: `/pages/social-media/opportunity-detail?id=${opportunity.id}`
      })
    },
    followUp(opportunity) {
      uni.showModal({
        title: '跟进商机',
        content: `确定要跟进商机 "${opportunity.title}" 吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '开始跟进',
              icon: 'success'
            })
            // 调用API更新商机跟进状态
          }
        }
      })
    },
    convertOpportunity(opportunity) {
      uni.showModal({
        title: '转化商机',
        content: `确定要将商机 "${opportunity.title}" 转化为实际订单吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '商机转化成功',
              icon: 'success'
            })
            // 调用API转化商机
          }
        }
      })
    },
    closeOpportunity(opportunity) {
      uni.showModal({
        title: '关闭商机',
        content: `确定要关闭商机 "${opportunity.title}" 吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '商机已关闭',
              icon: 'success'
            })
            // 调用API关闭商机
          }
        }
      })
    },
    getStatusClass(status) {
      const statusClass = {
        '新发现': 'status-new',
        '跟进中': 'status-following',
        '已转化': 'status-converted',
        '已关闭': 'status-closed',
        '待审核': 'status-pending'
      }
      return statusClass[status] || 'status-default'
    }
  }
}
</script>

<style>
.opportunity-management {
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

.opportunity-list {
  background: white;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
  max-height: 800rpx;
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
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.opportunity-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  flex: 1;
  margin-right: 20rpx;
  line-height: 1.4;
}

.opportunity-tags {
  display: flex;
  gap: 10rpx;
  flex-shrink: 0;
}

.tag {
  padding: 6rpx 12rpx;
  border-radius: 6rpx;
  font-size: 20rpx;
  font-weight: bold;
}

.tag.platform {
  background-color: #e3f2fd;
  color: #1976d2;
}

.tag.status {
  color: white;
}

.status-new {
  background-color: #17a2b8;
}

.status-following {
  background-color: #007bff;
}

.status-converted {
  background-color: #28a745;
}

.status-closed {
  background-color: #6c757d;
}

.status-pending {
  background-color: #ffc107;
  color: #212529;
}

.status-default {
  background-color: #6c757d;
}

.opportunity-content {
  margin-bottom: 20rpx;
}

.opportunity-description {
  font-size: 24rpx;
  color: #666;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.opportunity-info {
  background-color: #f8f9fa;
  padding: 20rpx;
  border-radius: 8rpx;
  margin-bottom: 20rpx;
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

.opportunity-actions {
  display: flex;
  gap: 20rpx;
  justify-content: flex-end;
}

.btn-follow {
  background-color: #007bff;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 6rpx;
  font-size: 22rpx;
}

.btn-convert {
  background-color: #28a745;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 6rpx;
  font-size: 22rpx;
}

.btn-close {
  background-color: #dc3545;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 6rpx;
  font-size: 22rpx;
}
</style>