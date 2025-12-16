<template>
  <el-dialog
    v-model="dialogVisible"
    title="策略效果分析"
    width="900px"
    :before-close="handleClose"
  >
    <div v-if="strategy" class="analysis-container">
      <!-- 策略基本信息 -->
      <el-card class="strategy-info-card mb-4">
        <template #header>
          <div class="card-header">
            <span class="title">{{ strategy.strategyName }}</span>
            <el-tag :type="getStrategyTypeTagType(strategy.strategyType)" size="small">
              {{ getStrategyTypeLabel(strategy.strategyType) }}
            </el-tag>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <div class="info-label">适用渠道</div>
              <div class="info-value">{{ getChannelTypeLabel(strategy.channelType) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <div class="info-label">用户类型</div>
              <div class="info-value">{{ getUserTypeLabel(strategy.userType) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <div class="info-label">优先级</div>
              <div class="info-value">{{ strategy.priority }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 统计指标 -->
      <el-row :gutter="20" class="stats-row mb-4">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon primary">
                <el-icon><ShoppingCart /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">应用次数</div>
                <div class="stat-value">{{ analyticsData.applicationCount }}</div>
                <div class="stat-trend">
                  本月累计应用{{ analyticsData.monthlyApplications }}次
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon success">
                <el-icon><Money /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">优惠金额</div>
                <div class="stat-value">¥{{ analyticsData.totalDiscount.toFixed(2) }}</div>
                <div class="stat-trend">
                  平均每次优惠¥{{ analyticsData.avgDiscount.toFixed(2) }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon warning">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">转化率</div>
                <div class="stat-value">{{ analyticsData.conversionRate }}%</div>
                <div class="stat-trend">
                  比上月{{ analyticsData.conversionTrend > 0 ? '上升' : '下降' }}{{ Math.abs(analyticsData.conversionTrend) }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon info">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-title">用户覆盖</div>
                <div class="stat-value">{{ analyticsData.userCoverage }}</div>
                <div class="stat-trend">
                  总用户数的{{ analyticsData.userCoveragePercentage }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 图表分析 -->
      <el-row :gutter="20" class="charts-row mb-4">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>应用趋势</span>
            </template>
            <div class="chart-container">
              <div class="chart-placeholder">
                <el-icon class="chart-icon"><TrendCharts /></el-icon>
                <div>近30天策略应用趋势图</div>
                <div class="chart-data">
                  <div class="data-item" v-for="(value, index) in analyticsData.dailyApplications" :key="index">
                    <div class="data-bar" :style="{ height: (value / Math.max(...analyticsData.dailyApplications) * 80 + 20) + 'px' }"></div>
                    <div class="data-label">第{{ index + 1 }}天</div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>优惠分布</span>
            </template>
            <div class="chart-container">
              <div class="chart-placeholder">
                <el-icon class="chart-icon"><PieChart /></el-icon>
                <div>优惠金额区间分布</div>
                <div class="distribution-chart">
                  <div class="dist-item" v-for="(item, index) in discountDistribution" :key="index">
                    <div class="dist-bar" :style="{ width: (item.percentage * 100) + '%' }"></div>
                    <div class="dist-label">{{ item.range }}</div>
                    <div class="dist-value">{{ item.percentage }}%</div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 详细分析 -->
      <el-card class="detailed-analysis">
        <template #header>
          <span>详细分析</span>
        </template>
        <el-tabs v-model="activeTab" class="analysis-tabs">
          <el-tab-pane label="效果分析" name="effectiveness">
            <div class="effectiveness-analysis">
              <div class="analysis-section">
                <h4>策略表现</h4>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <div class="metric-item">
                      <div class="metric-label">平均订单价值</div>
                      <div class="metric-value">¥{{ analyticsData.avgOrderValue.toFixed(2) }}</div>
                      <div class="metric-change" :class="analyticsData.orderValueChange >= 0 ? 'positive' : 'negative'">
                        {{ analyticsData.orderValueChange >= 0 ? '↑' : '↓' }} {{ Math.abs(analyticsData.orderValueChange) }}%
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="metric-item">
                      <div class="metric-label">策略ROI</div>
                      <div class="metric-value">{{ analyticsData.roi }}%</div>
                      <div class="metric-description">
                        投资回报率 = 收益 / 优惠成本
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>

              <div class="analysis-section">
                <h4>用户反馈</h4>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <div class="feedback-metric">
                      <div class="feedback-score">{{ analyticsData.satisfactionScore }}</div>
                      <div class="feedback-label">满意度评分</div>
                      <el-rate v-model="satisfactionRate" disabled show-score />
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="feedback-metric">
                      <div class="feedback-score">{{ analyticsData.repeatUsageRate }}%</div>
                      <div class="feedback-label">重复使用率</div>
                      <div class="feedback-description">
                        用户重复使用此策略的频率
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="feedback-metric">
                      <div class="feedback-score">{{ analyticsData.recommendationRate }}%</div>
                      <div class="feedback-label">推荐率</div>
                      <div class="feedback-description">
                        用户向他人推荐此策略的比例
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="用户分析" name="users">
            <div class="user-analysis">
              <div class="analysis-section">
                <h4>用户画像</h4>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <div class="user-profile">
                      <div class="profile-item">
                        <span class="profile-label">主要年龄段：</span>
                        <span class="profile-value">{{ analyticsData.primaryAgeGroup }}</span>
                      </div>
                      <div class="profile-item">
                        <span class="profile-label">性别分布：</span>
                        <span class="profile-value">男{{ analyticsData.malePercentage }}% / 女{{ analyticsData.femalePercentage }}%</span>
                      </div>
                      <div class="profile-item">
                        <span class="profile-label">地域分布：</span>
                        <span class="profile-value">{{ analyticsData.topRegions }}</span>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="user-behavior">
                      <div class="behavior-item">
                        <span class="behavior-label">平均使用时间：</span>
                        <span class="behavior-value">{{ analyticsData.avgUsageTime }}</span>
                      </div>
                      <div class="behavior-item">
                        <span class="behavior-label">活跃时段：</span>
                        <span class="behavior-value">{{ analyticsData.activeTimeSlot }}</span>
                      </div>
                      <div class="behavior-item">
                        <span class="behavior-label">渠道偏好：</span>
                        <span class="behavior-value">{{ analyticsData.preferredChannels }}</span>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="优化建议" name="recommendations">
            <div class="recommendations">
              <div class="recommendation-section">
                <h4>效果优化</h4>
                <div class="recommendation-item" v-for="(rec, index) in optimizationRecommendations" :key="index">
                  <div class="rec-icon" :class="rec.priority">
                    <el-icon><WarningFilled /></el-icon>
                  </div>
                  <div class="rec-content">
                    <div class="rec-title">{{ rec.title }}</div>
                    <div class="rec-description">{{ rec.description }}</div>
                    <div class="rec-impact">
                      预期影响：{{ rec.impact }}
                    </div>
                  </div>
                </div>
              </div>

              <div class="recommendation-section">
                <h4>A/B测试建议</h4>
                <div class="ab-test-suggestions">
                  <div class="ab-test-item" v-for="(test, index) in abTestSuggestions" :key="index">
                    <div class="ab-test-title">{{ test.title }}</div>
                    <div class="ab-test-description">{{ test.description }}</div>
                    <div class="ab-test-metrics">
                      <span class="metric">测试指标：{{ test.metrics }}</span>
                      <span class="duration">预计时长：{{ test.duration }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>

    <div v-else class="no-data">
      暂无策略数据
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleExportReport">
          <el-icon><Download /></el-icon>
          导出报告
        </el-button>
        <el-button type="primary" @click="handleClose">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  ShoppingCart, 
  Money, 
  TrendCharts, 
  User, 
  PieChart, 
  WarningFilled,
  Download
} from '@element-plus/icons-vue'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  strategy: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const activeTab = ref('effectiveness')
const analyticsData = reactive({
  applicationCount: 0,
  monthlyApplications: 0,
  totalDiscount: 0,
  avgDiscount: 0,
  conversionRate: 0,
  conversionTrend: 0,
  userCoverage: 0,
  userCoveragePercentage: 0,
  dailyApplications: [],
  avgOrderValue: 0,
  orderValueChange: 0,
  roi: 0,
  satisfactionScore: 0,
  repeatUsageRate: 0,
  recommendationRate: 0,
  primaryAgeGroup: '',
  malePercentage: 0,
  femalePercentage: 0,
  topRegions: '',
  avgUsageTime: '',
  activeTimeSlot: '',
  preferredChannels: ''
})

const satisfactionRate = computed(() => analyticsData.satisfactionScore / 5)

const discountDistribution = ref([
  { range: '0-10元', percentage: 15 },
  { range: '10-50元', percentage: 35 },
  { range: '50-100元', percentage: 30 },
  { range: '100-500元', percentage: 15 },
  { range: '500元以上', percentage: 5 }
])

const optimizationRecommendations = ref([
  {
    priority: 'high',
    title: '调整策略触发条件',
    description: '当前策略触发率较低，建议优化最小购买数量限制，提高策略的适用范围。',
    impact: '预计提升20-30%应用率'
  },
  {
    priority: 'medium',
    title: '优化折扣力度',
    description: '部分用户反馈折扣力度不够吸引人，建议增加阶梯折扣的最高折扣率。',
    impact: '预计提升15%转化率'
  },
  {
    priority: 'low',
    title: '扩展适用用户群体',
    description: '当前策略主要针对VIP用户，建议扩展到普通用户群体以扩大覆盖面。',
    impact: '预计增加50%潜在用户'
  }
])

const abTestSuggestions = ref([
  {
    title: '折扣率A/B测试',
    description: '测试8折和9折两种折扣率对转化率的影响',
    metrics: '转化率、平均订单价值',
    duration: '2周'
  },
  {
    title: '触发条件测试',
    description: '对比最小购买数量1件和5件的策略效果',
    metrics: '策略应用率、用户满意度',
    duration: '3周'
  }
])

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 方法
const fetchAnalyticsData = async () => {
  if (!props.strategy) return
  
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 生成模拟数据
    Object.assign(analyticsData, {
      applicationCount: Math.floor(Math.random() * 1000) + 500,
      monthlyApplications: Math.floor(Math.random() * 200) + 100,
      totalDiscount: Math.floor(Math.random() * 50000) + 10000,
      avgDiscount: Math.floor(Math.random() * 50) + 20,
      conversionRate: Math.floor(Math.random() * 30) + 10,
      conversionTrend: (Math.random() - 0.5) * 20,
      userCoverage: Math.floor(Math.random() * 500) + 100,
      userCoveragePercentage: Math.floor(Math.random() * 20) + 5,
      dailyApplications: Array.from({ length: 30 }, () => Math.floor(Math.random() * 50) + 10),
      avgOrderValue: Math.floor(Math.random() * 200) + 100,
      orderValueChange: (Math.random() - 0.5) * 30,
      roi: Math.floor(Math.random() * 200) + 100,
      satisfactionScore: Math.floor(Math.random() * 2) + 4,
      repeatUsageRate: Math.floor(Math.random() * 30) + 40,
      recommendationRate: Math.floor(Math.random() * 20) + 30,
      primaryAgeGroup: '25-35岁',
      malePercentage: Math.floor(Math.random() * 40) + 30,
      femalePercentage: 100 - (Math.floor(Math.random() * 40) + 30),
      topRegions: '北京、上海、广州',
      avgUsageTime: '15分钟',
      activeTimeSlot: '晚上19-22点',
      preferredChannels: '移动端App'
    })
  } catch (error) {
    ElMessage.error('获取分析数据失败')
  }
}

const handleExportReport = () => {
  ElMessage.info('报告导出功能开发中...')
}

const handleClose = () => {
  dialogVisible.value = false
}

const getStrategyTypeLabel = (type) => {
  const typeMap = {
    'DISCOUNT': '折扣策略',
    'FIXED_AMOUNT': '固定金额',
    'TIERED': '阶梯价格',
    'BUY_X_GET_Y': '买X送Y',
    'BUNDLE': '捆绑销售'
  }
  return typeMap[type] || type
}

const getStrategyTypeTagType = (type) => {
  const typeMap = {
    'DISCOUNT': 'success',
    'FIXED_AMOUNT': 'primary',
    'TIERED': 'warning',
    'BUY_X_GET_Y': 'info',
    'BUNDLE': 'danger'
  }
  return typeMap[type] || 'info'
}

const getChannelTypeLabel = (type) => {
  const channelMap = {
    'B2C': 'B2C零售',
    'B2B': 'B2B批发',
    'ALL': '全渠道'
  }
  return channelMap[type] || type
}

const getUserTypeLabel = (type) => {
  const userTypeMap = {
    'INDIVIDUAL': '个人用户',
    'ENTERPRISE': '企业用户',
    'NEW_USER': '新用户',
    'VIP': 'VIP用户'
  }
  return userTypeMap[type] || type
}

// 监听器
watch(() => props.strategy, (newStrategy) => {
  if (newStrategy && dialogVisible.value) {
    fetchAnalyticsData()
  }
}, { immediate: true })

watch(dialogVisible, (newValue) => {
  if (newValue && props.strategy) {
    fetchAnalyticsData()
  }
})
</script>

<style scoped>
.analysis-container {
  @apply max-h-96 overflow-y-auto;
}

.strategy-info-card {
  @apply bg-blue-50;
}

.card-header {
  @apply flex items-center justify-between;
}

.card-header .title {
  @apply font-semibold text-lg;
}

.info-item {
  @apply text-center;
}

.info-label {
  @apply text-sm text-gray-600 mb-1;
}

.info-value {
  @apply font-semibold text-gray-800;
}

.stats-row {
  @apply mb-4;
}

.stat-card {
  @apply h-full;
}

.stat-content {
  @apply flex items-center;
}

.stat-icon {
  @apply w-10 h-10 rounded-lg flex items-center justify-center text-white text-lg mr-3;
}

.stat-icon.primary {
  @apply bg-blue-500;
}

.stat-icon.success {
  @apply bg-green-500;
}

.stat-icon.warning {
  @apply bg-yellow-500;
}

.stat-icon.info {
  @apply bg-purple-500;
}

.stat-title {
  @apply text-sm text-gray-600 mb-1;
}

.stat-value {
  @apply text-lg font-bold text-gray-800 mb-1;
}

.stat-trend {
  @apply text-xs text-gray-500;
}

.charts-row {
  @apply mb-4;
}

.chart-container {
  @apply h-64;
}

.chart-placeholder {
  @apply h-full flex flex-col items-center justify-center text-gray-500;
}

.chart-icon {
  @apply text-3xl mb-2;
}

.chart-data {
  @apply flex items-end gap-1 h-32 w-full mt-4;
}

.data-item {
  @apply flex flex-col items-center flex-1;
}

.data-bar {
  @apply bg-blue-500 w-4 rounded-t;
  min-height: 20px;
}

.data-label {
  @apply text-xs text-gray-400 mt-1;
}

.distribution-chart {
  @apply space-y-2 mt-4;
}

.dist-item {
  @apply flex items-center gap-2;
}

.dist-bar {
  @apply bg-green-500 h-4 rounded;
  min-width: 20px;
}

.dist-label {
  @apply text-sm text-gray-600 w-16;
}

.dist-value {
  @apply text-sm font-semibold text-gray-800;
}

.detailed-analysis {
  @apply bg-gray-50;
}

.analysis-tabs {
  @apply bg-white;
}

.analysis-section {
  @apply mb-6;
}

.analysis-section h4 {
  @apply text-lg font-semibold text-gray-800 mb-4;
}

.metric-item {
  @apply p-4 bg-white rounded-lg;
}

.metric-label {
  @apply text-sm text-gray-600 mb-1;
}

.metric-value {
  @apply text-xl font-bold text-gray-800 mb-2;
}

.metric-change {
  @apply text-sm font-semibold;
}

.metric-change.positive {
  @apply text-green-600;
}

.metric-change.negative {
  @apply text-red-600;
}

.metric-description {
  @apply text-xs text-gray-500;
}

.feedback-metric {
  @apply text-center p-4 bg-white rounded-lg;
}

.feedback-score {
  @apply text-2xl font-bold text-blue-600 mb-2;
}

.feedback-label {
  @apply text-sm text-gray-600 mb-2;
}

.feedback-description {
  @apply text-xs text-gray-500;
}

.user-profile,
.user-behavior {
  @apply p-4 bg-white rounded-lg;
}

.profile-item,
.behavior-item {
  @apply mb-3 last:mb-0;
}

.profile-label,
.behavior-label {
  @apply text-sm text-gray-600;
}

.profile-value,
.behavior-value {
  @apply text-sm font-semibold text-gray-800 ml-2;
}

.recommendation-section {
  @apply mb-6;
}

.recommendation-item {
  @apply flex items-start gap-3 p-4 bg-white rounded-lg mb-3;
}

.rec-icon {
  @apply w-8 h-8 rounded-full flex items-center justify-center text-white text-sm;
}

.rec-icon.high {
  @apply bg-red-500;
}

.rec-icon.medium {
  @apply bg-yellow-500;
}

.rec-icon.low {
  @apply bg-green-500;
}

.rec-content {
  @apply flex-1;
}

.rec-title {
  @apply font-semibold text-gray-800 mb-1;
}

.rec-description {
  @apply text-sm text-gray-600 mb-2;
}

.rec-impact {
  @apply text-xs text-blue-600 font-medium;
}

.ab-test-suggestions {
  @apply space-y-3;
}

.ab-test-item {
  @apply p-4 bg-white rounded-lg;
}

.ab-test-title {
  @apply font-semibold text-gray-800 mb-2;
}

.ab-test-description {
  @apply text-sm text-gray-600 mb-2;
}

.ab-test-metrics {
  @apply flex items-center gap-4 text-xs text-gray-500;
}

.no-data {
  @apply text-center text-gray-500 py-8;
}

.dialog-footer {
  @apply flex justify-end gap-2;
}

.mb-4 {
  @apply mb-4;
}
</style>