<template>
  <view class="procurement-opportunities">
    <!-- 页面头部 -->
    <view class="page-header">
      <view class="header-left">
        <text class="page-title">采购商机匹配</text>
        <text class="page-subtitle">基于采集信息智能匹配商机</text>
      </view>
      <view class="header-actions">
        <button class="btn-primary" @click="startNewMatching">
          <text class="iconfont icon-match"></text>
          智能匹配
        </button>
      </view>
    </view>

    <!-- 统计概览 -->
    <view class="stats-overview">
      <view class="stat-card">
        <view class="stat-icon">
          <text class="iconfont icon-document"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.totalOpportunities || 0 }}</text>
          <text class="stat-label">总商机数</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon">
          <text class="iconfont icon-opportunity"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.highQualityOpportunities || 0 }}</text>
          <text class="stat-label">优质商机</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon">
          <text class="iconfont icon-success"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.convertedOpportunities || 0 }}</text>
          <text class="stat-label">已转换</text>
        </view>
      </view>
      
      <view class="stat-card">
        <view class="stat-icon">
          <text class="iconfont icon-match"></text>
        </view>
        <view class="stat-content">
          <text class="stat-number">{{ statistics.matchRate || 0 }}%</text>
          <text class="stat-label">匹配率</text>
        </view>
      </view>
    </view>

    <!-- 搜索筛选 -->
    <view class="filter-section">
      <view class="filter-row">
        <view class="filter-item">
          <text class="filter-label">关键词</text>
          <input 
            class="filter-input" 
            v-model="searchForm.keyword" 
            placeholder="请输入关键词搜索"
            placeholder-style="color: #999"
            @confirm="handleSearch"
          />
        </view>
        
        <view class="filter-item">
          <text class="filter-label">商机等级</text>
          <picker 
            mode="selector" 
            :range="gradeOptions" 
            :value="gradeIndex"
            @change="onGradeChange"
          >
            <view class="filter-picker">
              <text>{{ gradeOptions[gradeIndex] }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <view class="filter-item">
          <text class="filter-label">状态</text>
          <picker 
            mode="selector" 
            :range="statusOptions" 
            :value="statusIndex"
            @change="onStatusChange"
          >
            <view class="filter-picker">
              <text>{{ statusOptions[statusIndex] }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
      </view>
      
      <view class="filter-row">
        <view class="filter-item">
          <text class="filter-label">预算范围</text>
          <view class="budget-range">
            <input 
              class="budget-input" 
              v-model="searchForm.minBudget" 
              type="number"
              placeholder="最小值"
              placeholder-style="color: #999"
            />
            <text class="budget-separator">-</text>
            <input 
              class="budget-input" 
              v-model="searchForm.maxBudget" 
              type="number"
              placeholder="最大值"
              placeholder-style="color: #999"
            />
          </view>
        </view>
        
        <view class="filter-item">
          <text class="filter-label">时间范围</text>
          <picker 
            mode="date" 
            :value="searchForm.startDate"
            @change="onStartDateChange"
          >
            <view class="filter-picker">
              <text>{{ searchForm.startDate || '开始日期' }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <view class="filter-item">
          <text class="filter-label">至</text>
          <picker 
            mode="date" 
            :value="searchForm.endDate"
            @change="onEndDateChange"
          >
            <view class="filter-picker">
              <text>{{ searchForm.endDate || '结束日期' }}</text>
              <text class="picker-arrow">▼</text>
            </view>
          </picker>
        </view>
        
        <view class="filter-actions">
          <button class="btn-search" @click="handleSearch">
            <text class="iconfont icon-search"></text>
            搜索
          </button>
          <button class="btn-reset" @click="resetSearch">
            <text class="iconfont icon-reset"></text>
            重置
          </button>
        </view>
      </view>
    </view>

    <!-- 商机列表 -->
    <view class="opportunities-list">
      <view class="list-header">
        <text class="list-title">商机列表 ({{ total }})</text>
        <view class="list-actions">
          <button class="btn-batch" @click="batchConvertOpportunities">
            <text class="iconfont icon-convert"></text>
            批量转换
          </button>
          <button class="btn-batch" @click="exportOpportunities">
            <text class="iconfont icon-export"></text>
            导出商机
          </button>
        </view>
      </view>
      
      <view class="opportunity-cards" v-if="opportunityList.length > 0">
        <view 
          class="opportunity-card" 
          v-for="opportunity in opportunityList" 
          :key="opportunity.id"
          @click="viewOpportunityDetail(opportunity)"
        >
          <view class="card-header">
            <view class="header-left">
              <checkbox 
                :checked="selectedOpportunities.includes(opportunity.id)" 
                @click.stop="toggleOpportunitySelection(opportunity.id)"
              />
              <text class="opportunity-title">{{ opportunity.title }}</text>
              <text class="opportunity-grade" :class="getGradeClass(opportunity.grade)">
                {{ getGradeText(opportunity.grade) }}
              </text>
            </view>
            <view class="header-right">
              <text class="match-score">匹配度: {{ opportunity.matchScore }}%</text>
            </view>
          </view>
          
          <view class="card-content">
            <view class="info-section">
              <view class="info-item">
                <text class="info-label">采购单位:</text>
                <text class="info-value">{{ opportunity.procuringEntity }}</text>
              </view>
              <view class="info-item">
                <text class="info-label">项目预算:</text>
                <text class="info-value budget">{{ formatBudget(opportunity.budget) }}</text>
              </view>
              <view class="info-item">
                <text class="info-label">所属行业:</text>
                <text class="info-value">{{ opportunity.industry }}</text>
              </view>
              <view class="info-item">
                <text class="info-label">所在地区:</text>
                <text class="info-value">{{ opportunity.region }}</text>
              </view>
              <view class="info-item">
                <text class="info-label">截止时间:</text>
                <text class="info-value deadline">{{ formatDate(opportunity.deadline) }}</text>
              </view>
            </view>
            
            <view class="match-section">
              <text class="match-title">匹配详情</text>
              <view class="match-tags">
                <text 
                  class="match-tag" 
                  v-for="tag in opportunity.matchTags" 
                  :key="tag"
                >{{ tag }}</text>
              </view>
              <view class="match-reason">
                <text class="reason-label">匹配理由:</text>
                <text class="reason-text">{{ opportunity.matchReason }}</text>
              </view>
            </view>
          </view>
          
          <view class="card-footer">
            <view class="footer-left">
              <text class="create-time">创建时间: {{ formatDateTime(opportunity.createTime) }}</text>
              <text class="match-time">匹配时间: {{ formatDateTime(opportunity.matchTime) }}</text>
            </view>
            <view class="footer-actions">
              <button 
                class="action-btn primary" 
                @click.stop="convertToOpportunity(opportunity)"
                v-if="opportunity.status === 'pending'"
              >
                转为商机
              </button>
              <button 
                class="action-btn secondary" 
                @click.stop="viewMatchDetail(opportunity)"
              >
                匹配详情
              </button>
              <button class="action-btn danger" @click.stop="deleteOpportunity(opportunity)">
                删除
              </button>
            </view>
          </view>
        </view>
      </view>
      
      <view class="empty-state" v-else>
        <text class="empty-text">暂无匹配商机</text>
        <button class="empty-btn" @click="startNewMatching">开始智能匹配</button>
      </view>
    </view>

    <!-- 智能匹配弹窗 -->
    <uni-popup ref="matchingPopup" type="center">
      <view class="popup-content">
        <view class="popup-header">
          <text class="popup-title">智能匹配配置</text>
          <text class="popup-close" @click="closeMatchingPopup">×</text>
        </view>
        
        <view class="popup-body">
          <view class="form-item">
            <text class="form-label">匹配策略</text>
            <radio-group class="radio-group" v-model="matchingForm.strategy">
              <label class="radio-item">
                <radio value="keyword" :checked="matchingForm.strategy === 'keyword'" />
                <text>关键词匹配</text>
              </label>
              <label class="radio-item">
                <radio value="semantic" :checked="matchingForm.strategy === 'semantic'" />
                <text>语义匹配</text>
              </label>
              <label class="radio-item">
                <radio value="hybrid" :checked="matchingForm.strategy === 'hybrid'" />
                <text>混合匹配</text>
              </label>
            </radio-group>
          </view>
          
          <view class="form-item">
            <text class="form-label">匹配关键词</text>
            <textarea 
              class="form-textarea" 
              v-model="matchingForm.keywords" 
              placeholder="请输入匹配关键词，多个关键词用逗号分隔"
              placeholder-style="color: #999"
              :maxlength="200"
            />
          </view>
          
          <view class="form-item">
            <text class="form-label">行业筛选</text>
            <picker 
              mode="selector" 
              :range="industryOptions" 
              :value="industryIndex"
              @change="onIndustryChange"
            >
              <view class="form-picker">
                <text>{{ industryOptions[industryIndex] || '全部行业' }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="form-label">地区筛选</text>
            <picker 
              mode="selector" 
              :range="regionOptions" 
              :value="matchingRegionIndex"
              @change="onMatchingRegionChange"
            >
              <view class="form-picker">
                <text>{{ regionOptions[matchingRegionIndex] || '全部地区' }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
          </view>
          
          <view class="form-item">
            <text class="form-label">预算范围</text>
            <view class="budget-range">
              <input 
                class="budget-input" 
                v-model="matchingForm.minBudget" 
                type="number"
                placeholder="最小值"
                placeholder-style="color: #999"
              />
              <text class="budget-separator">-</text>
              <input 
                class="budget-input" 
                v-model="matchingForm.maxBudget" 
                type="number"
                placeholder="最大值"
                placeholder-style="color: #999"
              />
              <text class="budget-unit">万元</text>
            </view>
          </view>
          
          <view class="form-item">
            <text class="form-label">时间范围</text>
            <view class="date-range">
              <picker 
                mode="date" 
                :value="matchingForm.startDate"
                @change="onMatchingStartDateChange"
              >
                <view class="date-picker">
                  <text>{{ matchingForm.startDate || '开始日期' }}</text>
                  <text class="picker-arrow">▼</text>
                </view>
              </picker>
              <text class="date-separator">至</text>
              <picker 
                mode="date" 
                :value="matchingForm.endDate"
                @change="onMatchingEndDateChange"
              >
                <view class="date-picker">
                  <text>{{ matchingForm.endDate || '结束日期' }}</text>
                  <text class="picker-arrow">▼</text>
                </view>
              </picker>
            </view>
          </view>
        </view>
        
        <view class="popup-footer">
          <button class="btn-cancel" @click="closeMatchingPopup">取消</button>
          <button class="btn-confirm" @click="startMatching" :loading="matchingLoading">
            开始匹配
          </button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, onShow } from 'vue'
import { onPullDownRefresh, onReachBottom } from '@dcloudio/uni-app'
import {
  getMatchedOpportunities,
  startIntelligentMatching,
  convertToBusinessOpportunity,
  deleteMatchedOpportunity,
  getOpportunityStatistics,
  exportOpportunities
} from '@/api/procurement/procurementCrawler.js'

// 搜索表单
const searchForm = reactive({
  keyword: '',
  grade: '',
  status: '',
  minBudget: '',
  maxBudget: '',
  startDate: '',
  endDate: '',
  current: 1,
  pageSize: 10
})

// 智能匹配表单
const matchingForm = reactive({
  strategy: 'hybrid',
  keywords: '',
  industry: '',
  region: '',
  minBudget: '',
  maxBudget: '',
  startDate: '',
  endDate: ''
})

// 选项数据
const gradeOptions = ['全部等级', 'A级', 'B级', 'C级', 'D级']
const statusOptions = ['全部状态', '待处理', '已转换', '已忽略']
const industryOptions = [
  '全部行业', '信息技术', '建筑工程', '制造业', '医疗卫生', '教育培训', 
  '交通运输', '能源环保', '金融服务', '文化传媒', '农业林业', '其他'
]
const regionOptions = [
  '全部地区', '北京市', '上海市', '天津市', '重庆市', '广东省', '江苏省', '浙江省', '山东省', 
  '河南省', '四川省', '湖北省', '湖南省', '河北省', '山西省', '陕西省', '安徽省', '福建省', 
  '江西省', '辽宁省', '吉林省', '黑龙江省', '云南省', '贵州省', '广西壮族自治区', '甘肃省', 
  '青海省', '宁夏回族自治区', '新疆维吾尔自治区', '西藏自治区', '内蒙古自治区'
]

// 状态索引
const gradeIndex = ref(0)
const statusIndex = ref(0)
const industryIndex = ref(0)
const matchingRegionIndex = ref(0)

// 数据列表
const opportunityList = ref([])
const selectedOpportunities = ref([])
const statistics = ref({
  totalOpportunities: 0,
  highQualityOpportunities: 0,
  convertedOpportunities: 0,
  matchRate: 0
})

// 弹窗相关
const matchingPopup = ref(null)
const matchingLoading = ref(false)

// 分页相关
const total = ref(0)
const loading = ref(false)

// 页面加载时获取数据
onMounted(() => {
  loadOpportunities()
  loadStatistics()
})

onShow(() => {
  loadOpportunities()
  loadStatistics()
})

// 下拉刷新
onPullDownRefresh(() => {
  Promise.all([loadOpportunities(), loadStatistics()]).finally(() => {
    uni.stopPullDownRefresh()
  })
})

// 上拉加载更多
onReachBottom(() => {
  if (searchForm.current * searchForm.pageSize < total.value) {
    searchForm.current++
    loadOpportunities(true)
  }
})

// 加载商机列表
async function loadOpportunities(append = false) {
  if (loading.value) return
  loading.value = true
  
  try {
    const params = {
      ...searchForm,
      grade: gradeIndex.value > 0 ? gradeOptions[gradeIndex.value] : '',
      status: statusIndex.value > 0 ? getStatusValue(statusIndex.value) : ''
    }
    
    const result = await getMatchedOpportunities(params)
    if (result.success) {
      const newList = result.data.records || []
      if (append) {
        opportunityList.value = [...opportunityList.value, ...newList]
      } else {
        opportunityList.value = newList
      }
      total.value = result.data.total || 0
    } else {
      uni.showToast({
        title: result.message || '加载失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('加载商机列表失败:', error)
    uni.showToast({
      title: '加载失败',
      icon: 'none'
    })
  } finally {
    loading.value = false
  }
}

// 加载统计信息
async function loadStatistics() {
  try {
    const result = await getOpportunityStatistics()
    if (result.success) {
      statistics.value = result.data
    }
  } catch (error) {
    console.error('加载统计信息失败:', error)
  }
}

// 搜索处理
function handleSearch() {
  searchForm.current = 1
  loadOpportunities()
}

// 重置搜索
function resetSearch() {
  searchForm.keyword = ''
  searchForm.minBudget = ''
  searchForm.maxBudget = ''
  searchForm.startDate = ''
  searchForm.endDate = ''
  gradeIndex.value = 0
  statusIndex.value = 0
  handleSearch()
}

// 选择器变更处理
function onGradeChange(e) {
  gradeIndex.value = e.detail.value
  handleSearch()
}

function onStatusChange(e) {
  statusIndex.value = e.detail.value
  handleSearch()
}

function onStartDateChange(e) {
  searchForm.startDate = e.detail.value
  handleSearch()
}

function onEndDateChange(e) {
  searchForm.endDate = e.detail.value
  handleSearch()
}

// 商机选择处理
function toggleOpportunitySelection(opportunityId) {
  const index = selectedOpportunities.value.indexOf(opportunityId)
  if (index > -1) {
    selectedOpportunities.value.splice(index, 1)
  } else {
    selectedOpportunities.value.push(opportunityId)
  }
}

// 商机操作
function startNewMatching() {
  matchingPopup.value.open()
}

function closeMatchingPopup() {
  matchingPopup.value.close()
}

function onIndustryChange(e) {
  industryIndex.value = e.detail.value
  matchingForm.industry = industryIndex.value > 0 ? industryOptions[industryIndex.value] : ''
}

function onMatchingRegionChange(e) {
  matchingRegionIndex.value = e.detail.value
  matchingForm.region = matchingRegionIndex.value > 0 ? regionOptions[matchingRegionIndex.value] : ''
}

function onMatchingStartDateChange(e) {
  matchingForm.startDate = e.detail.value
}

function onMatchingEndDateChange(e) {
  matchingForm.endDate = e.detail.value
}

async function startMatching() {
  if (!matchingForm.keywords.trim()) {
    uni.showToast({
      title: '请输入匹配关键词',
      icon: 'none'
    })
    return
  }
  
  matchingLoading.value = true
  
  try {
    const result = await startIntelligentMatching(matchingForm)
    if (result.success) {
      uni.showToast({
        title: `成功匹配 ${result.data.matchedCount} 个商机`,
        icon: 'success'
      })
      closeMatchingPopup()
      loadOpportunities()
      loadStatistics()
    } else {
      uni.showToast({
        title: result.message || '匹配失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('智能匹配失败:', error)
    uni.showToast({
      title: '匹配失败',
      icon: 'none'
    })
  } finally {
    matchingLoading.value = false
  }
}

async function convertToOpportunity(opportunity) {
  try {
    const result = await convertToBusinessOpportunity(opportunity.id)
    if (result.success) {
      uni.showToast({
        title: '转换成功',
        icon: 'success'
      })
      loadOpportunities()
      loadStatistics()
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

async function batchConvertOpportunities() {
  if (selectedOpportunities.value.length === 0) {
    uni.showToast({
      title: '请选择要转换的商机',
      icon: 'none'
    })
    return
  }
  
  uni.showModal({
    title: '确认转换',
    content: `确定要转换 ${selectedOpportunities.value.length} 个商机吗？`,
    success: async (res) => {
      if (res.confirm) {
        try {
          const promises = selectedOpportunities.value.map(id => convertToBusinessOpportunity(id))
          const results = await Promise.allSettled(promises)
          const successCount = results.filter(r => r.status === 'fulfilled' && r.value.success).length
          
          uni.showToast({
            title: `成功转换 ${successCount} 个商机`,
            icon: 'success'
          })
          selectedOpportunities.value = []
          loadOpportunities()
          loadStatistics()
        } catch (error) {
          console.error('批量转换商机失败:', error)
          uni.showToast({
            title: '批量转换失败',
            icon: 'none'
          })
        }
      }
    }
  })
}

function viewOpportunityDetail(opportunity) {
  uni.navigateTo({
    url: `/pagesA/procurement/opportunity-detail?id=${opportunity.id}`
  })
}

function viewMatchDetail(opportunity) {
  uni.navigateTo({
    url: `/pagesA/procurement/match-detail?id=${opportunity.id}`
  })
}

async function deleteOpportunity(opportunity) {
  uni.showModal({
    title: '确认删除',
    content: '确定要删除这个商机吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          const result = await deleteMatchedOpportunity(opportunity.id)
          if (result.success) {
            uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
            loadOpportunities()
            loadStatistics()
          } else {
            uni.showToast({
              title: result.message || '删除失败',
              icon: 'none'
            })
          }
        } catch (error) {
          console.error('删除商机失败:', error)
          uni.showToast({
            title: '删除失败',
            icon: 'none'
          })
        }
      }
    }
  })
}

async function exportOpportunities() {
  try {
    const result = await exportOpportunities(searchForm)
    if (result.success) {
      uni.showToast({
        title: '导出成功',
        icon: 'success'
      })
      // 这里可以处理文件下载
    } else {
      uni.showToast({
        title: result.message || '导出失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('导出商机失败:', error)
    uni.showToast({
      title: '导出失败',
      icon: 'none'
    })
  }
}

// 工具函数
function getGradeClass(grade) {
  const gradeMap = {
    'A': 'grade-a',
    'B': 'grade-b',
    'C': 'grade-c',
    'D': 'grade-d'
  }
  return gradeMap[grade] || 'grade-d'
}

function getGradeText(grade) {
  const gradeMap = {
    'A': 'A级',
    'B': 'B级',
    'C': 'C级',
    'D': 'D级'
  }
  return gradeMap[grade] || '未知'
}

function getStatusValue(index) {
  const statusValues = ['', 'pending', 'converted', 'ignored']
  return statusValues[index]
}

function formatBudget(budget) {
  if (!budget) return '暂无'
  const num = parseFloat(budget)
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万元'
  }
  return budget + '元'
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

function formatDateTime(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.procurement-opportunities {
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
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      
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

.filter-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .filter-row {
    display: flex;
    align-items: center;
    gap: 20rpx;
    margin-bottom: 20rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .filter-item {
      flex: 1;
      
      .filter-label {
        display: block;
        font-size: 26rpx;
        color: #606266;
        margin-bottom: 12rpx;
      }
      
      .filter-input {
        width: 100%;
        height: 70rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 26rpx;
      }
      
      .filter-picker {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: 70rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 0 20rpx;
        font-size: 26rpx;
        background: #fff;
        
        .picker-arrow {
          color: #909399;
        }
      }
      
      .budget-range {
        display: flex;
        align-items: center;
        gap: 12rpx;
        
        .budget-input {
          flex: 1;
          height: 70rpx;
          border: 1rpx solid #dcdfe6;
          border-radius: 8rpx;
          padding: 0 20rpx;
          font-size: 26rpx;
        }
        
        .budget-separator {
          font-size: 26rpx;
          color: #606266;
        }
      }
    }
    
    .filter-actions {
      display: flex;
      gap: 16rpx;
      align-self: flex-end;
      
      .btn-search,
      .btn-reset {
        padding: 20rpx 30rpx;
        border-radius: 8rpx;
        font-size: 26rpx;
        border: none;
        display: flex;
        align-items: center;
        gap: 8rpx;
        
        .iconfont {
          font-size: 28rpx;
        }
      }
      
      .btn-search {
        background: #409eff;
        color: #fff;
      }
      
      .btn-reset {
        background: #f4f4f5;
        color: #606266;
      }
    }
  }
}

.opportunities-list {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .list-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
    
    .list-actions {
      display: flex;
      gap: 16rpx;
      
      .btn-batch {
        padding: 16rpx 24rpx;
        border-radius: 8rpx;
        font-size: 24rpx;
        border: none;
        background: #f4f4f5;
        color: #606266;
        display: flex;
        align-items: center;
        gap: 8rpx;
        
        .iconfont {
          font-size: 24rpx;
        }
      }
    }
  }
  
  .opportunity-cards {
    .opportunity-card {
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
      
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
        
        .header-left {
          display: flex;
          align-items: center;
          gap: 16rpx;
          flex: 1;
          
          .opportunity-title {
            font-size: 30rpx;
            font-weight: bold;
            color: #303133;
            flex: 1;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          
          .opportunity-grade {
            padding: 8rpx 16rpx;
            border-radius: 20rpx;
            font-size: 22rpx;
            font-weight: bold;
            
            &.grade-a {
              background: #f6ffed;
              color: #52c41a;
            }
            
            &.grade-b {
              background: #f0f9ff;
              color: #409eff;
            }
            
            &.grade-c {
              background: #fdf6ec;
              color: #e6a23c;
            }
            
            &.grade-d {
              background: #fef0f0;
              color: #f56c6c;
            }
          }
        }
        
        .header-right {
          .match-score {
            font-size: 26rpx;
            color: #409eff;
            font-weight: bold;
          }
        }
      }
      
      .card-content {
        margin-bottom: 20rpx;
        
        .info-section {
          margin-bottom: 20rpx;
          
          .info-item {
            display: flex;
            align-items: center;
            margin-bottom: 12rpx;
            
            &:last-child {
              margin-bottom: 0;
            }
            
            .info-label {
              font-size: 26rpx;
              color: #909399;
              width: 120rpx;
              margin-right: 20rpx;
            }
            
            .info-value {
              font-size: 26rpx;
              color: #606266;
              flex: 1;
              
              &.budget {
                color: #f56c6c;
                font-weight: bold;
              }
              
              &.deadline {
                color: #e6a23c;
                font-weight: bold;
              }
            }
          }
        }
        
        .match-section {
          background: #fff;
          border-radius: 8rpx;
          padding: 20rpx;
          
          .match-title {
            display: block;
            font-size: 28rpx;
            font-weight: bold;
            color: #303133;
            margin-bottom: 16rpx;
          }
          
          .match-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 12rpx;
            margin-bottom: 16rpx;
            
            .match-tag {
              background: #f0f9ff;
              color: #409eff;
              padding: 8rpx 16rpx;
              border-radius: 20rpx;
              font-size: 22rpx;
            }
          }
          
          .match-reason {
            display: flex;
            align-items: flex-start;
            
            .reason-label {
              font-size: 26rpx;
              color: #909399;
              margin-right: 16rpx;
              white-space: nowrap;
            }
            
            .reason-text {
              font-size: 26rpx;
              color: #606266;
              flex: 1;
            }
          }
        }
      }
      
      .card-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .footer-left {
          display: flex;
          flex-direction: column;
          gap: 8rpx;
          
          .create-time,
          .match-time {
            font-size: 24rpx;
            color: #909399;
          }
        }
        
        .footer-actions {
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
            
            &.danger {
              background: #fef0f0;
              color: #f56c6c;
              border: 1rpx solid #f56c6c;
            }
          }
        }
      }
    }
  }
  
  .empty-state {
    text-align: center;
    padding: 80rpx 0;
    
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

// 弹窗样式
.popup-content {
  background: #fff;
  border-radius: 16rpx;
  width: 700rpx;
  max-width: 90vw;
  max-height: 80vh;
  overflow-y: auto;
  
  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #ebeef5;
    position: sticky;
    top: 0;
    background: #fff;
    z-index: 10;
    
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
      
      .radio-group {
        display: flex;
        flex-wrap: wrap;
        gap: 30rpx;
        
        .radio-item {
          display: flex;
          align-items: center;
          gap: 12rpx;
          font-size: 28rpx;
          color: #606266;
        }
      }
      
      .form-textarea {
        width: 100%;
        height: 120rpx;
        border: 1rpx solid #dcdfe6;
        border-radius: 8rpx;
        padding: 20rpx;
        font-size: 28rpx;
        resize: none;
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
        background: #fff;
        
        .picker-arrow {
          color: #909399;
        }
      }
      
      .budget-range {
        display: flex;
        align-items: center;
        gap: 12rpx;
        
        .budget-input {
          flex: 1;
          height: 80rpx;
          border: 1rpx solid #dcdfe6;
          border-radius: 8rpx;
          padding: 0 20rpx;
          font-size: 28rpx;
        }
        
        .budget-separator {
          font-size: 28rpx;
          color: #606266;
        }
        
        .budget-unit {
          font-size: 28rpx;
          color: #909399;
          white-space: nowrap;
        }
      }
      
      .date-range {
        display: flex;
        align-items: center;
        gap: 20rpx;
        
        .date-picker {
          flex: 1;
          display: flex;
          justify-content: space-between;
          align-items: center;
          height: 80rpx;
          border: 1rpx solid #dcdfe6;
          border-radius: 8rpx;
          padding: 0 20rpx;
          font-size: 28rpx;
          background: #fff;
          
          .picker-arrow {
            color: #909399;
          }
        }
        
        .date-separator {
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
    position: sticky;
    bottom: 0;
    background: #fff;
    z-index: 10;
    
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