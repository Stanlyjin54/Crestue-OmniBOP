<template>
  <view class="procurement-opportunities">
    <view class="header">
      <text class="title">采购商机匹配</text>
      <view class="actions">
        <button class="btn-primary" @click="startMatching">开始匹配</button>
        <button class="btn-secondary" @click="exportMatches">导出匹配</button>
      </view>
    </view>
    
    <view class="match-criteria">
      <view class="criteria-title">匹配条件设置</view>
      <view class="criteria-form">
        <view class="form-row">
          <text class="form-label">产品类别:</text>
          <picker mode="selector" :range="productCategories" :value="selectedCategory" @change="onCategoryChange">
            <view class="picker-field">{{ productCategories[selectedCategory] }}</view>
          </picker>
        </view>
        <view class="form-row">
          <text class="form-label">预算范围:</text>
          <view class="budget-range">
            <input class="budget-input" v-model="minBudget" placeholder="最低预算(万元)" type="number" />
            <text class="range-separator">-</text>
            <input class="budget-input" v-model="maxBudget" placeholder="最高预算(万元)" type="number" />
          </view>
        </view>
        <view class="form-row">
          <text class="form-label">地区偏好:</text>
          <picker mode="selector" :range="regions" :value="selectedRegion" @change="onRegionChange">
            <view class="picker-field">{{ regions[selectedRegion] }}</view>
          </picker>
        </view>
        <view class="form-row">
          <text class="form-label">匹配度:</text>
          <slider class="match-slider" :value="matchThreshold" @change="onMatchThresholdChange" min="60" max="100" />
          <text class="match-value">{{ matchThreshold }}%</text>
        </view>
        <button class="btn-apply" @click="applyCriteria">应用条件</button>
      </view>
    </view>
    
    <view class="match-results">
      <view class="results-header">
        <text class="results-title">匹配结果 ({{ matchResults.length }})</text>
        <view class="sort-options">
          <picker mode="selector" :range="sortOptions" :value="selectedSort" @change="onSortChange">
            <view class="sort-picker">{{ sortOptions[selectedSort] }}</view>
          </picker>
        </view>
      </view>
      
      <scroll-view class="results-list" scroll-y>
        <view class="match-item" v-for="result in matchResults" :key="result.id" @click="viewMatchDetail(result)">
          <view class="match-header">
            <text class="tender-title">{{ result.tenderTitle }}</text>
            <view class="match-score">
              <text class="score-label">匹配度</text>
              <text class="score-value" :class="getScoreClass(result.matchScore)">{{ result.matchScore }}%</text>
            </view>
          </view>
          
          <view class="company-info">
            <text class="company-name">{{ result.companyName }}</text>
            <text class="company-type">{{ result.companyType }}</text>
          </view>
          
          <view class="match-details">
            <view class="detail-item">
              <text class="detail-label">招标编号:</text>
              <text class="detail-value">{{ result.tenderCode }}</text>
            </view>
            <view class="detail-item">
              <text class="detail-label">预算金额:</text>
              <text class="detail-value amount">{{ result.budget }}</text>
            </view>
            <view class="detail-item">
              <text class="detail-label">截止时间:</text>
              <text class="detail-value">{{ result.deadline }}</text>
            </view>
            <view class="detail-item">
              <text class="detail-label">匹配原因:</text>
              <text class="detail-value">{{ result.matchReason }}</text>
            </view>
          </view>
          
          <view class="match-actions">
            <button class="btn-contact" @click.stop="contactCompany(result)">联系企业</button>
            <button class="btn-recommend" @click.stop="recommendMatch(result)">推荐匹配</button>
            <button class="btn-save" @click.stop="saveMatch(result)">保存商机</button>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      productCategories: ['全部类别', 'IT设备', '办公用品', '医疗设备', '教育设备', '交通工具', '建筑工程', '服务采购'],
      selectedCategory: 0,
      minBudget: '',
      maxBudget: '',
      regions: ['全部地区', '北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '西安'],
      selectedRegion: 0,
      matchThreshold: 80,
      sortOptions: ['按匹配度排序', '按预算金额排序', '按截止时间排序'],
      selectedSort: 0,
      matchResults: [
        {
          id: 1,
          tenderTitle: '某市政府办公设备采购项目',
          tenderCode: 'ZB20250115001',
          companyName: '华为技术有限公司',
          companyType: 'IT设备供应商',
          matchScore: 95,
          budget: '500万元',
          deadline: '2025-02-15',
          matchReason: '产品类别匹配、技术能力符合、历史合作良好',
          category: 'IT设备',
          region: '北京'
        },
        {
          id: 2,
          tenderTitle: '某医院医疗设备采购项目',
          tenderCode: 'ZB20250113003',
          companyName: '迈瑞医疗科技有限公司',
          companyType: '医疗设备供应商',
          matchScore: 88,
          budget: '300万元',
          deadline: '2025-02-20',
          matchReason: '医疗设备专业、资质齐全、行业经验丰富',
          category: '医疗设备',
          region: '上海'
        },
        {
          id: 3,
          tenderTitle: '某教育局教学设备采购',
          tenderCode: 'ZB20250114002',
          companyName: '科大讯飞股份有限公司',
          companyType: '教育科技供应商',
          matchScore: 82,
          budget: '120万元',
          deadline: '2025-02-10',
          matchReason: '教育设备专业、技术创新能力强、服务体系完善',
          category: '教育设备',
          region: '广州'
        }
      ]
    }
  },
  methods: {
    onCategoryChange(e) {
      this.selectedCategory = e.detail.value
    },
    onRegionChange(e) {
      this.selectedRegion = e.detail.value
    },
    onMatchThresholdChange(e) {
      this.matchThreshold = e.detail.value
    },
    onSortChange(e) {
      this.selectedSort = e.detail.value
      this.sortResults()
    },
    applyCriteria() {
      uni.showToast({
        title: '应用匹配条件',
        icon: 'success'
      })
      // 根据条件重新匹配商机
    },
    startMatching() {
      uni.showToast({
        title: '开始智能匹配',
        icon: 'success'
      })
      // 调用API开始匹配流程
    },
    exportMatches() {
      uni.showToast({
        title: '导出匹配结果',
        icon: 'success'
      })
      // 导出当前匹配结果
    },
    viewMatchDetail(result) {
      uni.navigateTo({
        url: `/pages/procurement/procurement-detail?id=${result.id}`
      })
    },
    contactCompany(result) {
      uni.showModal({
        title: '联系企业',
        content: `确定要联系 ${result.companyName} 吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '已发送联系请求',
              icon: 'success'
            })
            // 调用API发送联系请求
          }
        }
      })
    },
    recommendMatch(result) {
      uni.showModal({
        title: '推荐匹配',
        content: `确定要推荐这个匹配给 ${result.companyName} 吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '推荐成功',
              icon: 'success'
            })
            // 调用API推荐匹配
          }
        }
      })
    },
    saveMatch(result) {
      uni.showModal({
        title: '保存商机',
        content: `确定要保存这个商机吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '商机已保存',
              icon: 'success'
            })
            // 调用API保存商机
          }
        }
      })
    },
    sortResults() {
      // 根据选择的排序方式重新排序结果
      const sortType = this.sortOptions[this.selectedSort]
      switch (sortType) {
        case '按匹配度排序':
          this.matchResults.sort((a, b) => b.matchScore - a.matchScore)
          break
        case '按预算金额排序':
          this.matchResults.sort((a, b) => this.parseBudget(b.budget) - this.parseBudget(a.budget))
          break
        case '按截止时间排序':
          this.matchResults.sort((a, b) => new Date(a.deadline) - new Date(b.deadline))
          break
      }
    },
    parseBudget(budgetStr) {
      // 解析预算字符串，提取数字
      const match = budgetStr.match(/(\d+)/)
      return match ? parseInt(match[1]) : 0
    },
    getScoreClass(score) {
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 70) return 'score-fair'
      return 'score-poor'
    }
  }
}
</script>

<style>
.procurement-opportunities {
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

.match-criteria {
  background: white;
  padding: 30rpx;
  border-radius: 12rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.criteria-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.criteria-form {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.form-row {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.form-label {
  font-size: 28rpx;
  color: #666;
  min-width: 120rpx;
}

.picker-field {
  background: #f8f9fa;
  padding: 16rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #333;
  min-width: 200rpx;
  border: 1rpx solid #ddd;
}

.budget-range {
  display: flex;
  align-items: center;
  gap: 10rpx;
  flex: 1;
}

.budget-input {
  background: #f8f9fa;
  padding: 16rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #333;
  border: 1rpx solid #ddd;
  width: 150rpx;
}

.range-separator {
  font-size: 24rpx;
  color: #666;
}

.match-slider {
  flex: 1;
  margin: 0 20rpx;
}

.match-value {
  font-size: 28rpx;
  font-weight: bold;
  color: #007aff;
  min-width: 60rpx;
}

.btn-apply {
  background-color: #28a745;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  align-self: flex-end;
}

.match-results {
  background: white;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.results-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.sort-picker {
  background: #f8f9fa;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #333;
  border: 1rpx solid #ddd;
}

.results-list {
  max-height: 800rpx;
}

.match-item {
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
  cursor: pointer;
}

.match-item:last-child {
  border-bottom: none;
}

.match-item:active {
  background-color: #f8f9fa;
}

.match-header {
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

.match-score {
  text-align: center;
  min-width: 100rpx;
}

.score-label {
  display: block;
  font-size: 20rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.score-value {
  font-size: 36rpx;
  font-weight: bold;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
}

.score-excellent {
  color: #28a745;
  background-color: #d4edda;
}

.score-good {
  color: #007bff;
  background-color: #cce7ff;
}

.score-fair {
  color: #ffc107;
  background-color: #fff3cd;
}

.score-poor {
  color: #dc3545;
  background-color: #f8d7da;
}

.company-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding: 20rpx;
  background-color: #f8f9fa;
  border-radius: 8rpx;
}

.company-name {
  font-size: 26rpx;
  font-weight: bold;
  color: #333;
}

.company-type {
  font-size: 24rpx;
  color: #666;
}

.match-details {
  margin-bottom: 20rpx;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.detail-label {
  font-size: 24rpx;
  color: #999;
  min-width: 120rpx;
}

.detail-value {
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

.match-actions {
  display: flex;
  gap: 20rpx;
  justify-content: flex-end;
}

.btn-contact {
  background-color: #007bff;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 6rpx;
  font-size: 22rpx;
}

.btn-recommend {
  background-color: #28a745;
  color: white;
  padding: 12rpx 24rpx;
  border-radius: 6rpx;
  font-size: 22rpx;
}

.btn-save {
  background-color: #ffc107;
  color: #212529;
  padding: 12rpx 24rpx;
  border-radius: 6rpx;
  font-size: 22rpx;
}
</style>