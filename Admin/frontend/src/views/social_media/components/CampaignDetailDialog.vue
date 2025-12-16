<template>
  <el-dialog
    v-model="dialogVisible"
    title="营销活动详情"
    width="900px"
    top="5vh"
  >
    <div v-loading="loading" class="detail-container">
      <!-- 活动概览 -->
      <div class="campaign-overview">
        <el-card shadow="never" class="overview-card">
          <div class="overview-header">
            <div class="title-section">
              <h2>{{ campaign.name }}</h2>
              <div class="meta-tags">
                <el-tag :type="getStatusType(campaign.status)" size="large">
                  {{ getStatusLabel(campaign.status) }}
                </el-tag>
                <el-tag size="large" style="margin-left: 8px">
                  {{ getCampaignTypeLabel(campaign.type) }}
                </el-tag>
              </div>
            </div>
            <div class="overview-actions">
              <el-button
                v-if="campaign.status === 'active'"
                :icon="VideoPause"
                @click="handlePauseCampaign"
              >
                暂停活动
              </el-button>
              <el-button
                v-if="campaign.status === 'paused'"
                :icon="VideoPlay"
                @click="handleResumeCampaign"
              >
                恢复活动
              </el-button>
              <el-button
                v-if="campaign.status !== 'completed' && campaign.status !== 'cancelled'"
                :icon="CircleCheck"
                @click="handleCompleteCampaign"
              >
                完成活动
              </el-button>
            </div>
          </div>

          <div class="overview-content">
            <p class="description">{{ campaign.description }}</p>
            
            <div class="info-grid">
              <div class="info-item">
                <span class="label">时间范围</span>
                <span class="value">
                  {{ campaign.startDate }} - {{ campaign.endDate }}
                </span>
              </div>
              <div class="info-item">
                <span class="label">预算金额</span>
                <span class="value">
                  {{ formatBudget(campaign.budget) }} {{ campaign.currency }}
                </span>
              </div>
              <div class="info-item">
                <span class="label">目标平台</span>
                <div class="platform-list">
                  <el-tag
                    v-for="platform in campaign.platforms"
                    :key="platform"
                    size="small"
                    :style="{ 
                      backgroundColor: getPlatformColor(platform),
                      borderColor: getPlatformColor(platform),
                      color: 'white'
                    }"
                  >
                    {{ getPlatformName(platform) }}
                  </el-tag>
                </div>
              </div>
              <div class="info-item">
                <span class="label">创建时间</span>
                <span class="value">{{ campaign.createdAt || '2024-01-15 10:30:00' }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 目标受众 -->
      <div class="audience-section">
        <el-card shadow="hover">
          <template #header>
            <div class="section-header">
              <span>目标受众</span>
              <el-icon><User /></el-icon>
            </div>
          </template>
          
          <div class="audience-grid">
            <div class="audience-item">
              <span class="label">年龄范围</span>
              <span class="value">{{ campaign.targetAudience?.ageRange || '未设置' }}</span>
            </div>
            <div class="audience-item">
              <span class="label">性别偏好</span>
              <span class="value">{{ getGenderLabel(campaign.targetAudience?.gender) }}</span>
            </div>
            <div class="audience-item">
              <span class="label">地域分布</span>
              <span class="value">{{ getLocationLabel(campaign.targetAudience?.location) }}</span>
            </div>
          </div>

          <div class="interests-section" v-if="campaign.targetAudience?.interests?.length">
            <h4>兴趣标签</h4>
            <div class="interest-tags">
              <el-tag
                v-for="interest in campaign.targetAudience.interests"
                :key="interest"
                size="small"
                style="margin: 4px"
              >
                {{ getInterestLabel(interest) }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 内容策略 -->
      <div class="content-section">
        <el-card shadow="hover">
          <template #header>
            <div class="section-header">
              <span>内容策略</span>
              <el-icon><Document /></el-icon>
            </div>
          </template>

          <div class="content-grid">
            <div class="content-item">
              <span class="label">内容类型</span>
              <div class="content-type-tags">
                <el-tag
                  v-for="type in campaign.contentStrategy?.contentType || []"
                  :key="type"
                  size="small"
                >
                  {{ getContentTypeLabel(type) }}
                </el-tag>
              </div>
            </div>
            <div class="content-item">
              <span class="label">发布频次</span>
              <span class="value">{{ getFrequencyLabel(campaign.contentStrategy?.frequency) }}</span>
            </div>
          </div>

          <div class="themes-section" v-if="campaign.contentStrategy?.themes?.length">
            <h4>内容主题</h4>
            <div class="theme-tags">
              <el-tag
                v-for="theme in campaign.contentStrategy.themes"
                :key="theme"
                style="margin: 4px"
              >
                {{ theme }}
              </el-tag>
            </div>
          </div>

          <div class="keywords-section" v-if="campaign.contentStrategy?.keywords?.length">
            <h4>关键词</h4>
            <div class="keyword-tags">
              <el-tag
                v-for="keyword in campaign.contentStrategy.keywords"
                :key="keyword"
                type="info"
                style="margin: 4px"
              >
                #{{ keyword }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </div>

      <!-- KPI 指标 -->
      <div class="kpi-section">
        <el-card shadow="hover">
          <template #header>
            <div class="section-header">
              <span>KPI 目标与完成情况</span>
              <el-icon><TrendCharts /></el-icon>
            </div>
          </template>

          <div class="kpi-grid">
            <div class="kpi-item" v-for="(kpi, key) in kpiComparison" :key="key">
              <div class="kpi-header">
                <span class="kpi-label">{{ kpi.label }}</span>
                <span class="kpi-unit">{{ kpi.unit }}</span>
              </div>
              <div class="kpi-values">
                <div class="kpi-target">
                  <span class="value-label">目标</span>
                  <span class="value">{{ kpi.target }}</span>
                </div>
                <div class="kpi-actual">
                  <span class="value-label">实际</span>
                  <span class="value" :class="getKpiStatusClass(kpi)">{{ kpi.actual }}</span>
                </div>
              </div>
              <div class="kpi-progress">
                <el-progress
                  :percentage="kpi.progress"
                  :color="getKpiProgressColor(kpi)"
                  :stroke-width="8"
                />
                <span class="progress-text">{{ kpi.progress.toFixed(1) }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 实时数据 -->
      <div class="realtime-section">
        <el-card shadow="hover">
          <template #header>
            <div class="section-header">
              <span>实时数据监控</span>
              <div class="refresh-info">
                <el-icon><Refresh /></el-icon>
                <span>最后更新: {{ lastUpdateTime }}</span>
              </div>
            </div>
          </template>

          <div class="realtime-metrics">
            <div class="metric-item" v-for="(metric, key) in realtimeMetrics" :key="key">
              <div class="metric-icon" :style="{ color: metric.color }">
                <el-icon><component :is="metric.icon" /></el-icon>
              </div>
              <div class="metric-content">
                <div class="metric-value">{{ metric.value }}</div>
                <div class="metric-label">{{ metric.label }}</div>
                <div class="metric-trend">
                  <el-icon :color="metric.trend.color">
                    <component :is="metric.trend.icon" />
                  </el-icon>
                  <span :style="{ color: metric.trend.color }">{{ metric.trend.value }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 操作日志 -->
      <div class="logs-section">
        <el-card shadow="hover">
          <template #header>
            <div class="section-header">
              <span>操作日志</span>
              <el-button size="small" :icon="Download" @click="exportLogs">
                导出日志
              </el-button>
            </div>
          </template>

          <el-timeline>
            <el-timeline-item
              v-for="log in operationLogs"
              :key="log.id"
              :timestamp="log.timestamp"
              :type="log.type"
              :color="getLogColor(log.type)"
            >
              <div class="log-content">
                <div class="log-action">{{ log.action }}</div>
                <div class="log-details">{{ log.details }}</div>
                <div class="log-operator">操作人: {{ log.operator }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import {
  User, Document, TrendCharts, Refresh, Download,
  VideoPause, VideoPlay, CircleCheck
} from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  campaign: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'updated'])

// 响应式数据
const loading = ref(false)
const lastUpdateTime = ref('2024-01-20 15:30:45')

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// KPI 对比数据
const kpiComparison = computed(() => {
  if (!props.campaign) return {}
  
  const kpis = props.campaign.kpis || {}
  const metrics = props.campaign.metrics || {}
  
  return {
    reach: {
      label: '触达人数',
      unit: '人',
      target: kpis.targetReach || 0,
      actual: metrics.reach || 0,
      progress: kpis.targetReach ? Math.min(100, (metrics.reach || 0) / kpis.targetReach * 100) : 0
    },
    engagement: {
      label: '用户互动',
      unit: '次',
      target: kpis.targetEngagement || 0,
      actual: metrics.engagement || 0,
      progress: kpis.targetEngagement ? Math.min(100, (metrics.engagement || 0) / kpis.targetEngagement * 100) : 0
    },
    conversionRate: {
      label: '转化率',
      unit: '%',
      target: kpis.targetConversionRate || 0,
      actual: metrics.conversionRate || 0,
      progress: kpis.targetConversionRate ? Math.min(100, (metrics.conversionRate || 0) / kpis.targetConversionRate * 100) : 0
    },
    ctr: {
      label: '点击率',
      unit: '%',
      target: kpis.targetCTR || 0,
      actual: metrics.ctr || 0,
      progress: kpis.targetCTR ? Math.min(100, (metrics.ctr || 0) / kpis.targetCTR * 100) : 0
    },
    roi: {
      label: '投资回报率',
      unit: '',
      target: kpis.targetROI || 0,
      actual: metrics.roi || 0,
      progress: kpis.targetROI ? Math.min(100, (metrics.roi || 0) / kpis.targetROI * 100) : 0
    }
  }
})

// 实时指标数据
const realtimeMetrics = computed(() => {
  return {
    reach: {
      label: '实时触达',
      value: '24,567',
      icon: 'View',
      color: '#409EFF',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+12.5%'
      }
    },
    engagement: {
      label: '实时互动',
      value: '1,234',
      icon: 'ChatDotRound',
      color: '#67C23A',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+8.3%'
      }
    },
    posts: {
      label: '今日发布',
      value: '5',
      icon: 'Document',
      color: '#E6A23C',
      trend: {
        icon: 'Minus',
        color: '#909399',
        value: '0%'
      }
    },
    cost: {
      label: '实时成本',
      value: '￥2,345',
      icon: 'Money',
      color: '#F56C6C',
      trend: {
        icon: 'ArrowUp',
        color: '#F56C6C',
        value: '+5.2%'
      }
    }
  }
})

// 操作日志
const operationLogs = computed(() => {
  return [
    {
      id: 1,
      timestamp: '2024-01-20 15:30:45',
      type: 'primary',
      action: '活动更新',
      details: '修改了活动预算从 ￥10,000 到 ￥15,000',
      operator: '张三'
    },
    {
      id: 2,
      timestamp: '2024-01-20 14:15:20',
      type: 'success',
      action: '内容发布',
      details: '在抖音平台发布了3条新内容',
      operator: '李四'
    },
    {
      id: 3,
      timestamp: '2024-01-20 10:45:30',
      type: 'info',
      action: '活动启动',
      details: '营销活动正式开始投放',
      operator: '王五'
    },
    {
      id: 4,
      timestamp: '2024-01-19 16:20:10',
      type: 'warning',
      action: '预算预警',
      details: '活动预算使用率达到80%，建议调整投放策略',
      operator: '系统'
    },
    {
      id: 5,
      timestamp: '2024-01-19 09:30:15',
      type: 'primary',
      action: '活动创建',
      details: '创建了新的营销活动"春季新品推广"',
      operator: '张三'
    }
  ]
})

// 方法
const getStatusType = (status) => {
  const statusMap = {
    draft: 'info',
    scheduled: 'warning',
    active: 'success',
    paused: 'warning',
    completed: 'success',
    cancelled: 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusLabel = (status) => {
  const statusMap = {
    draft: '草稿',
    scheduled: '已排期',
    active: '进行中',
    paused: '已暂停',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

const getCampaignTypeLabel = (type) => {
  const typeMap = {
    awareness: '品牌认知',
    engagement: '用户互动',
    conversion: '转化推广',
    retention: '用户留存',
    viral: '病毒营销'
  }
  return typeMap[type] || type
}

const getPlatformName = (platformId) => {
  const platformMap = {
    wechat: '微信',
    weibo: '微博',
    douyin: '抖音',
    xiaohongshu: '小红书',
    zhihu: '知乎',
    bilibili: 'B站'
  }
  return platformMap[platformId] || platformId
}

const getPlatformColor = (platformId) => {
  const colorMap = {
    wechat: '#07C160',
    weibo: '#E6162D',
    douyin: '#FE2C55',
    xiaohongshu: '#FF2442',
    zhihu: '#0066FF',
    bilibili: '#00A1D6'
  }
  return colorMap[platformId] || '#909399'
}

const getGenderLabel = (gender) => {
  const genderMap = {
    all: '不限',
    male: '男性',
    female: '女性'
  }
  return genderMap[gender] || '不限'
}

const getLocationLabel = (location) => {
  const locationMap = {
    china: '全国',
    north_china: '华北',
    east_china: '华东',
    south_china: '华南',
    overseas: '海外'
  }
  return locationMap[location] || '全国'
}

const getInterestLabel = (interest) => {
  const interestMap = {
    tech: '科技数码',
    fashion: '时尚美妆',
    food: '美食料理',
    travel: '旅游度假',
    fitness: '健身运动',
    education: '教育培训',
    finance: '金融理财',
    entertainment: '娱乐休闲'
  }
  return interestMap[interest] || interest
}

const getContentTypeLabel = (type) => {
  const typeMap = {
    image: '图片',
    video: '视频',
    text: '文本',
    carousel: '轮播'
  }
  return typeMap[type] || type
}

const getFrequencyLabel = (frequency) => {
  const frequencyMap = {
    daily: '每日',
    weekly: '每周',
    monthly: '每月'
  }
  return frequencyMap[frequency] || '每日'
}

const getKpiStatusClass = (kpi) => {
  if (kpi.actual >= kpi.target) {
    return 'success'
  } else if (kpi.actual >= kpi.target * 0.8) {
    return 'warning'
  } else {
    return 'danger'
  }
}

const getKpiProgressColor = (kpi) => {
  if (kpi.actual >= kpi.target) {
    return '#67C23A'
  } else if (kpi.actual >= kpi.target * 0.8) {
    return '#E6A23C'
  } else {
    return '#F56C6C'
  }
}

const getLogColor = (type) => {
  const colorMap = {
    primary: '#409EFF',
    success: '#67C23A',
    warning: '#E6A23C',
    danger: '#F56C6C',
    info: '#909399'
  }
  return colorMap[type] || '#909399'
}

const formatBudget = (budget) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(budget)
}

// 操作方法
const handlePauseCampaign = async () => {
  try {
    // 模拟API调用
    ElMessage.success('活动已暂停')
    emit('updated')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleResumeCampaign = async () => {
  try {
    // 模拟API调用
    ElMessage.success('活动已恢复')
    emit('updated')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCompleteCampaign = async () => {
  try {
    // 模拟API调用
    ElMessage.success('活动已完成')
    emit('updated')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const exportLogs = async () => {
  try {
    // 模拟导出功能
    ElMessage.success('日志导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}
</script>

<style scoped>
.detail-container {
  max-height: 80vh;
  overflow-y: auto;
  padding: 0 8px;
}

.campaign-overview {
  margin-bottom: 20px;
}

.overview-card {
  border: none;
}

.overview-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.title-section h2 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.meta-tags {
  display: flex;
  align-items: center;
}

.overview-actions {
  display: flex;
  gap: 12px;
}

.overview-content {
  color: #606266;
}

.description {
  margin: 0 0 16px 0;
  font-size: 14px;
  line-height: 1.6;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-item .label {
  font-size: 12px;
  color: #909399;
  font-weight: 500;
}

.info-item .value {
  font-size: 14px;
  color: #303133;
}

.platform-list {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.audience-section,
.content-section,
.kpi-section,
.realtime-section,
.logs-section {
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #303133;
}

.refresh-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #909399;
}

.audience-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.audience-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background-color: #f5f5f5;
  border-radius: 6px;
}

.audience-item .label {
  font-size: 12px;
  color: #909399;
}

.audience-item .value {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.interests-section h4,
.themes-section h4,
.keywords-section h4 {
  margin: 16px 0 8px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.interest-tags,
.theme-tags,
.keyword-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 16px;
}

.content-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.content-item .label {
  font-size: 12px;
  color: #909399;
  font-weight: 500;
}

.content-item .value {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.content-type-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.kpi-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}

.kpi-item {
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background-color: #fafafa;
}

.kpi-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.kpi-label {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.kpi-unit {
  font-size: 12px;
  color: #909399;
}

.kpi-values {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.kpi-target,
.kpi-actual {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.value-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.kpi-target .value {
  font-size: 16px;
  font-weight: 600;
  color: #606266;
}

.kpi-actual .value {
  font-size: 16px;
  font-weight: 600;
}

.kpi-actual .value.success {
  color: #67C23A;
}

.kpi-actual .value.warning {
  color: #E6A23C;
}

.kpi-actual .value.danger {
  color: #F56C6C;
}

.kpi-progress {
  display: flex;
  align-items: center;
  gap: 12px;
}

.progress-text {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
}

.realtime-metrics {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.metric-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background-color: #fafafa;
}

.metric-icon {
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: #f5f5f5;
}

.metric-content {
  flex: 1;
}

.metric-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.metric-label {
  font-size: 12px;
  color: #606266;
  margin-bottom: 4px;
}

.metric-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.log-content {
  line-height: 1.5;
}

.log-action {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.log-details {
  font-size: 13px;
  color: #606266;
  margin-bottom: 4px;
}

.log-operator {
  font-size: 12px;
  color: #909399;
}
</style>