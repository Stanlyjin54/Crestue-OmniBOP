<template>
  <el-dialog
    v-model="dialogVisible"
    title="营销活动数据分析"
    width="1000px"
    top="5vh"
  >
    <div v-loading="loading" class="analytics-container">
      <!-- 活动基本信息 -->
      <div class="campaign-info" v-if="campaign">
        <el-card shadow="never" class="info-card">
          <div class="info-header">
            <h3>{{ campaign.name }}</h3>
            <div class="info-meta">
              <el-tag :type="getStatusType(campaign.status)" size="small">
                {{ getStatusLabel(campaign.status) }}
              </el-tag>
              <el-tag size="small" style="margin-left: 8px">
                {{ getCampaignTypeLabel(campaign.type) }}
              </el-tag>
            </div>
          </div>
          <div class="info-details">
            <div class="info-item">
              <span class="label">时间范围：</span>
              <span>{{ campaign.startDate }} - {{ campaign.endDate }}</span>
            </div>
            <div class="info-item">
              <span class="label">目标平台：</span>
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
            <div class="info-item">
              <span class="label">预算：</span>
              <span>{{ formatBudget(campaign.budget) }} {{ campaign.currency }}</span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 核心指标 -->
      <div class="metrics-section">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(metric, key) in keyMetrics" :key="key">
            <el-card shadow="hover" class="metric-card">
              <div class="metric-content">
                <div class="metric-icon" :style="{ color: metric.color }">
                  <el-icon><component :is="metric.icon" /></el-icon>
                </div>
                <div class="metric-info">
                  <div class="metric-value">{{ metric.value }}</div>
                  <div class="metric-label">{{ metric.label }}</div>
                  <div class="metric-progress">
                    <el-progress
                      :percentage="metric.progress"
                      :color="metric.color"
                      :show-text="false"
                      :stroke-width="4"
                    />
                    <span class="progress-text">{{ metric.progress }}%</span>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 图表分析 -->
      <div class="charts-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="hover" class="chart-card">
              <template #header>
                <div class="chart-header">
                  <span>平台表现对比</span>
                  <el-button-group>
                    <el-button
                      v-for="period in timePeriods"
                      :key="period.value"
                      :type="selectedPeriod === period.value ? 'primary' : 'default'"
                      size="small"
                      @click="selectedPeriod = period.value"
                    >
                      {{ period.label }}
                    </el-button>
                  </el-button-group>
                </div>
              </template>
              <div ref="platformChartRef" class="chart-container"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover" class="chart-card">
              <template #header>
                <span>趋势分析</span>
              </template>
              <div ref="trendChartRef" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="12">
            <el-card shadow="hover" class="chart-card">
              <template #header>
                <span>受众分析</span>
              </template>
              <div ref="audienceChartRef" class="chart-container"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover" class="chart-card">
              <template #header>
                <span>内容表现</span>
              </template>
              <div ref="contentChartRef" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 详细数据表格 -->
      <div class="details-section">
        <el-card shadow="hover">
          <template #header>
            <div class="table-header">
              <span>平台详细数据</span>
              <el-button
                type="primary"
                size="small"
                :icon="Download"
                @click="exportDetailedData"
              >
                导出详细数据
              </el-button>
            </div>
          </template>
          
          <el-table
            :data="detailedData"
            v-loading="loading"
            class="details-table"
            stripe
          >
            <el-table-column prop="platform" label="平台" width="100">
              <template #default="{ row }">
                <el-tag
                  size="small"
                  :style="{ 
                    backgroundColor: getPlatformColor(row.platform),
                    borderColor: getPlatformColor(row.platform),
                    color: 'white'
                  }"
                >
                  {{ getPlatformName(row.platform) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reach" label="触达" width="120">
              <template #default="{ row }">
                {{ formatNumber(row.reach) }}
              </template>
            </el-table-column>
            <el-table-column prop="engagement" label="互动" width="120">
              <template #default="{ row }">
                {{ formatNumber(row.engagement) }}
              </template>
            </el-table-column>
            <el-table-column prop="clickRate" label="点击率" width="100">
              <template #default="{ row }">
                {{ row.clickRate.toFixed(2) }}%
              </template>
            </el-table-column>
            <el-table-column prop="conversionRate" label="转化率" width="100">
              <template #default="{ row }">
                {{ row.conversionRate.toFixed(2) }}%
              </template>
            </el-table-column>
            <el-table-column prop="roi" label="ROI" width="100">
              <template #default="{ row }">
                <span :class="row.roi >= 0 ? 'positive' : 'negative'">
                  {{ row.roi.toFixed(2) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="cost" label="成本" width="120">
              <template #default="{ row }">
                {{ formatBudget(row.cost) }}
              </template>
            </el-table-column>
            <el-table-column prop="posts" label="发布数" width="100" />
            <el-table-column prop="impressions" label="展示数" width="120">
              <template #default="{ row }">
                {{ formatNumber(row.impressions) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>

      <!-- 优化建议 -->
      <div class="insights-section">
        <el-card shadow="hover">
          <template #header>
            <span>AI 分析与优化建议</span>
          </template>
          <div class="insights-content">
            <div
              v-for="insight in insights"
              :key="insight.id"
              class="insight-item"
              :class="insight.type"
            >
              <div class="insight-icon">
                <el-icon>
                  <component :is="getInsightIcon(insight.type)" />
                </el-icon>
              </div>
              <div class="insight-content">
                <h4>{{ insight.title }}</h4>
                <p>{{ insight.description }}</p>
                <div class="insight-suggestions">
                  <strong>建议：</strong>
                  <ul>
                    <li v-for="suggestion in insight.suggestions" :key="suggestion">
                      {{ suggestion }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  data: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const loading = ref(false)
const selectedPeriod = ref('7d')
const platformChartRef = ref()
const trendChartRef = ref()
const audienceChartRef = ref()
const contentChartRef = ref()

// 静态配置
const timePeriods = [
  { label: '7天', value: '7d' },
  { label: '30天', value: '30d' },
  { label: '90天', value: '90d' }
]

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const campaign = computed(() => props.data)

// 核心指标计算
const keyMetrics = computed(() => {
  if (!campaign.value) return {}
  
  const metrics = campaign.value.metrics || {}
  const kpis = campaign.value.kpis || {}
  
  return {
    reach: {
      label: '总触达',
      value: formatNumber(metrics.reach || 0),
      icon: 'View',
      color: '#409EFF',
      progress: kpis.targetReach ? 
        Math.min(100, ((metrics.reach || 0) / kpis.targetReach * 100)) : 0
    },
    engagement: {
      label: '总互动',
      value: formatNumber(metrics.engagement || 0),
      icon: 'ChatDotRound',
      color: '#67C23A',
      progress: kpis.targetEngagement ? 
        Math.min(100, ((metrics.engagement || 0) / kpis.targetEngagement * 100)) : 0
    },
    conversionRate: {
      label: '转化率',
      value: `${(metrics.conversionRate || 0).toFixed(2)}%`,
      icon: 'TrendCharts',
      color: '#E6A23C',
      progress: kpis.targetConversionRate ? 
        Math.min(100, ((metrics.conversionRate || 0) / kpis.targetConversionRate * 100)) : 0
    },
    roi: {
      label: '投资回报率',
      value: (metrics.roi || 0).toFixed(2),
      icon: 'Money',
      color: '#F56C6C',
      progress: kpis.targetROI ? 
        Math.min(100, ((metrics.roi || 0) / kpis.targetROI * 100)) : 0
    }
  }
})

// 详细数据
const detailedData = computed(() => {
  // 模拟各平台详细数据
  const platforms = campaign.value?.platforms || []
  return platforms.map(platform => ({
    platform,
    reach: Math.floor(Math.random() * 50000) + 10000,
    engagement: Math.floor(Math.random() * 5000) + 1000,
    clickRate: Math.random() * 5 + 1,
    conversionRate: Math.random() * 3 + 0.5,
    roi: Math.random() * 4 - 1,
    cost: Math.floor(Math.random() * 10000) + 1000,
    posts: Math.floor(Math.random() * 20) + 5,
    impressions: Math.floor(Math.random() * 100000) + 20000
  }))
})

// AI 分析洞察
const insights = computed(() => {
  return [
    {
      id: 1,
      type: 'success',
      title: '表现优异',
      description: '您的活动在抖音平台表现优异，互动率超出预期 45%',
      suggestions: [
        '考虑增加抖音平台的预算分配',
        '分析成功内容的特点，复制到其他平台',
        '延长在抖音的发布时间段'
      ]
    },
    {
      id: 2,
      type: 'warning',
      title: '需要优化',
      description: '微博平台的转化率低于平均水平，建议调整内容策略',
      suggestions: [
        '优化微博内容的文案和视觉设计',
        '增加互动性元素，如投票、问答等',
        '调整发布时间，选择用户活跃度更高的时间段'
      ]
    },
    {
      id: 3,
      type: 'info',
      title: '增长机会',
      description: '小红书平台显示出巨大潜力，建议扩大投入',
      suggestions: [
        '增加在小红书的KOL合作',
        '制作更多适合小红书调性的内容',
        '测试不同的内容格式和话题标签'
      ]
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

const getInsightIcon = (type) => {
  const iconMap = {
    success: 'SuccessFilled',
    warning: 'WarningFilled',
    info: 'InfoFilled'
  }
  return iconMap[type] || 'InfoFilled'
}

const formatNumber = (num) => {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

const formatBudget = (budget) => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(budget)
}

const exportDetailedData = async () => {
  try {
    // 模拟导出功能
    ElMessage.success('数据导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 渲染图表（示例实现）
const renderCharts = () => {
  // 这里应该集成实际的图表库，如 ECharts
  console.log('渲染图表数据:', {
    campaign: campaign.value,
    period: selectedPeriod.value,
    metrics: keyMetrics.value
  })
}

// 监听数据变化，重新渲染图表
watch(() => props.data, async () => {
  await nextTick()
  renderCharts()
}, { deep: true })

watch(selectedPeriod, async () => {
  await nextTick()
  renderCharts()
})
</script>

<style scoped>
.analytics-container {
  max-height: 80vh;
  overflow-y: auto;
  padding: 0 8px;
}

.campaign-info {
  margin-bottom: 20px;
}

.info-card {
  border: none;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.info-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.info-meta {
  display: flex;
  align-items: center;
}

.info-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-item .label {
  color: #606266;
  font-weight: 500;
  min-width: 80px;
}

.metrics-section {
  margin-bottom: 20px;
}

.metric-card {
  border: none;
  transition: transform 0.2s;
}

.metric-card:hover {
  transform: translateY(-2px);
}

.metric-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.metric-icon {
  font-size: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #f5f5f5;
}

.metric-info {
  flex: 1;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.metric-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.metric-progress {
  display: flex;
  align-items: center;
  gap: 8px;
}

.progress-text {
  font-size: 12px;
  color: #909399;
  white-space: nowrap;
}

.charts-section {
  margin-bottom: 20px;
}

.chart-card {
  border: none;
  margin-bottom: 0;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.details-section {
  margin-bottom: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.details-table {
  width: 100%;
}

.positive {
  color: #67C23A;
  font-weight: 500;
}

.negative {
  color: #F56C6C;
  font-weight: 500;
}

.insights-section {
  margin-bottom: 20px;
}

.insights-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.insight-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-radius: 8px;
  border-left: 4px solid;
}

.insight-item.success {
  background-color: #f0f9ff;
  border-left-color: #409EFF;
}

.insight-item.warning {
  background-color: #fdf6ec;
  border-left-color: #E6A23C;
}

.insight-item.info {
  background-color: #f4f4f5;
  border-left-color: #909399;
}

.insight-icon {
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: white;
  flex-shrink: 0;
}

.insight-item.success .insight-icon {
  color: #409EFF;
}

.insight-item.warning .insight-icon {
  color: #E6A23C;
}

.insight-item.info .insight-icon {
  color: #909399;
}

.insight-content {
  flex: 1;
}

.insight-content h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.insight-content p {
  margin: 0 0 12px 0;
  color: #606266;
  line-height: 1.5;
}

.insight-suggestions {
  color: #303133;
}

.insight-suggestions ul {
  margin: 8px 0 0 0;
  padding-left: 20px;
}

.insight-suggestions li {
  margin-bottom: 4px;
  color: #606266;
}
</style>