<template>
  <el-dialog
    v-model="dialogVisible"
    title="支付场景详情"
    width="800px"
    :before-close="handleClose"
  >
    <div class="scenario-detail-content">
      <el-tabs v-model="activeTab" class="detail-tabs">
        <el-tab-pane label="基本信息" name="basic">
          <div class="basic-info">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="场景名称">
                {{ scenarioData.name }}
              </el-descriptions-item>
              <el-descriptions-item label="场景类型">
                <el-tag :style="{ 
                  backgroundColor: getScenarioTypeColor(scenarioData.type) + '20',
                  borderColor: getScenarioTypeColor(scenarioData.type),
                  color: getScenarioTypeColor(scenarioData.type)
                }">
                  {{ getScenarioTypeLabel(scenarioData.type) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="业务类型">
                {{ getBusinessTypeLabel(scenarioData.businessType) }}
              </el-descriptions-item>
              <el-descriptions-item label="目标市场">
                {{ getTargetMarketLabel(scenarioData.targetMarket) }}
              </el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStatusType(scenarioData.status)">
                  {{ getStatusLabel(scenarioData.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="创建时间">
                {{ formatDateTime(scenarioData.createdAt) }}
              </el-descriptions-item>
              <el-descriptions-item label="更新时间">
                {{ formatDateTime(scenarioData.updatedAt) }}
              </el-descriptions-item>
              <el-descriptions-item label="负责人">
                {{ scenarioData.responsibleUser || '未分配' }}
              </el-descriptions-item>
            </el-descriptions>

            <div class="description-section">
              <h4>场景描述</h4>
              <p>{{ scenarioData.description || '暂无描述' }}</p>
            </div>

            <div class="metrics-section">
              <h4>关键指标</h4>
              <el-row :gutter="20">
                <el-col :span="6">
                  <div class="metric-card">
                    <div class="metric-value">{{ scenarioData.conversionRate || 0 }}%</div>
                    <div class="metric-label">当前转化率</div>
                    <div class="metric-progress">
                      <el-progress 
                        :percentage="scenarioData.conversionRate || 0" 
                        :stroke-width="6"
                        :color="getProgressColor((scenarioData.conversionRate || 0) / (scenarioData.targetConversionRate || 100))"
                      />
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="metric-card">
                    <div class="metric-value">{{ formatAmount(scenarioData.averageOrderValue || 0) }}</div>
                    <div class="metric-label">平均订单金额</div>
                    <div class="metric-trend positive">
                      <el-icon><ArrowUp /></el-icon>
                      <span>+8.5%</span>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="metric-card">
                    <div class="metric-value">{{ formatNumber(scenarioData.transactionVolume || 0) }}</div>
                    <div class="metric-label">交易量</div>
                    <div class="metric-trend positive">
                      <el-icon><ArrowUp /></el-icon>
                      <span>+12.3%</span>
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="metric-card">
                    <div class="metric-value">{{ scenarioData.targetConversionRate || 0 }}%</div>
                    <div class="metric-label">目标转化率</div>
                    <div class="metric-progress">
                      <el-progress 
                        :percentage="100" 
                        :stroke-width="6"
                        color="#67C23A"
                        :show-text="false"
                      />
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="支付配置" name="payment">
          <div class="payment-config">
            <div class="config-section">
              <h4>支持的支付方式</h4>
              <div class="payment-methods">
                <div 
                  v-for="methodId in scenarioData.supportedMethods || []" 
                  :key="methodId"
                  class="payment-method-item"
                >
                  <div class="method-info">
                    <div class="method-name">{{ getPaymentMethodName(methodId) }}</div>
                    <div class="method-fee">费率: {{ getPaymentMethodFee(methodId) }}%</div>
                  </div>
                  <div class="method-stats">
                    <div class="stat-item">
                      <span class="stat-label">使用率</span>
                      <span class="stat-value">{{ getMethodUsageRate(methodId) }}%</span>
                    </div>
                    <div class="stat-item">
                      <span class="stat-label">成功率</span>
                      <span class="stat-value">{{ getMethodSuccessRate(methodId) }}%</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="config-section">
              <h4>支付流程配置</h4>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="支付页面优化">
                  <el-tag :type="scenarioData.config?.paymentPageOptimization ? 'success' : 'info'">
                    {{ scenarioData.config?.paymentPageOptimization ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="一键支付">
                  <el-tag :type="scenarioData.config?.oneClickPayment ? 'success' : 'info'">
                    {{ scenarioData.config?.oneClickPayment ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="自动填充">
                  <el-tag :type="scenarioData.config?.autoFill ? 'success' : 'info'">
                    {{ scenarioData.config?.autoFill ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="支付提醒">
                  <el-tag :type="scenarioData.config?.paymentReminder ? 'success' : 'info'">
                    {{ scenarioData.config?.paymentReminder ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </div>

            <div class="config-section">
              <h4>风控配置</h4>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="风控级别">
                  <el-tag :type="getRiskLevelType(scenarioData.config?.riskLevel)">
                    {{ getRiskLevelLabel(scenarioData.config?.riskLevel) }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="验证码">
                  <el-tag :type="scenarioData.config?.verificationCode ? 'success' : 'info'">
                    {{ scenarioData.config?.verificationCode ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="交易限额">
                  {{ formatAmount(scenarioData.config?.transactionLimit || 0) }}
                </el-descriptions-item>
                <el-descriptions-item label="白名单检查">
                  <el-tag :type="scenarioData.config?.whitelistCheck ? 'success' : 'info'">
                    {{ scenarioData.config?.whitelistCheck ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="优化策略" name="optimization">
          <div class="optimization-config">
            <div class="config-section">
              <h4>优化功能</h4>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="A/B测试">
                  <el-tag :type="scenarioData.config?.abTesting ? 'success' : 'info'">
                    {{ scenarioData.config?.abTesting ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="智能推荐">
                  <el-tag :type="scenarioData.config?.smartRecommendation ? 'success' : 'info'">
                    {{ scenarioData.config?.smartRecommendation ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="个性化定价">
                  <el-tag :type="scenarioData.config?.personalizedPricing ? 'success' : 'info'">
                    {{ scenarioData.config?.personalizedPricing ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="优惠推荐">
                  <el-tag :type="scenarioData.config?.offerRecommendation ? 'success' : 'info'">
                    {{ scenarioData.config?.offerRecommendation ? '开启' : '关闭' }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </div>

            <div class="config-section">
              <h4>业务规则</h4>
              <div class="business-rules">
                {{ scenarioData.businessRules || '暂无业务规则' }}
              </div>
            </div>

            <div class="config-section">
              <h4>优化历史</h4>
              <el-timeline>
                <el-timeline-item
                  v-for="(record, index) in optimizationHistory"
                  :key="index"
                  :timestamp="record.date"
                  :type="record.type"
                  :hollow="true"
                >
                  <div class="timeline-content">
                    <div class="timeline-title">{{ record.title }}</div>
                    <div class="timeline-description">{{ record.description }}</div>
                    <div class="timeline-result" v-if="record.result">
                      <span class="result-label">优化结果:</span>
                      <span class="result-value">{{ record.result }}</span>
                    </div>
                  </div>
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="数据分析" name="analytics">
          <div class="analytics-data">
            <div class="chart-section">
              <h4>转化率趋势</h4>
              <div class="chart-placeholder">
                <el-icon><TrendCharts /></el-icon>
                <span>转化率趋势图表</span>
              </div>
            </div>

            <div class="metrics-comparison">
              <h4>性能对比</h4>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="comparison-card">
                    <h5>当前30天</h5>
                    <div class="comparison-metrics">
                      <div class="comparison-metric">
                        <span class="metric-label">转化率</span>
                        <span class="metric-value">{{ scenarioData.conversionRate || 0 }}%</span>
                      </div>
                      <div class="comparison-metric">
                        <span class="metric-label">交易量</span>
                        <span class="metric-value">{{ formatNumber(scenarioData.transactionVolume || 0) }}</span>
                      </div>
                      <div class="comparison-metric">
                        <span class="metric-label">成功率</span>
                        <span class="metric-value">{{ scenarioData.successRate || 0 }}%</span>
                      </div>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="comparison-card">
                    <h5>行业平均</h5>
                    <div class="comparison-metrics">
                      <div class="comparison-metric">
                        <span class="metric-label">转化率</span>
                        <span class="metric-value">12.5%</span>
                      </div>
                      <div class="comparison-metric">
                        <span class="metric-label">交易量</span>
                        <span class="metric-value">25,000</span>
                      </div>
                      <div class="comparison-metric">
                        <span class="metric-label">成功率</span>
                        <span class="metric-value">96.8%</span>
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button :icon="Edit" @click="handleEdit">
          编辑
        </el-button>
        <el-button :icon="DataAnalysis" @click="handleAnalytics">
          数据分析
        </el-button>
        <el-button :icon="Setting" @click="handleOptimize">
          智能优化
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Edit, DataAnalysis, Setting, TrendCharts, ArrowUp, ArrowDown,
  Clock, Check, Warning
} from '@element-plus/icons-vue'
import { usePaymentOptimization } from '@/composables/usePaymentOptimization'

// 组件props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  scenario: {
    type: Object,
    default: null
  }
})

// 组件emit
const emit = defineEmits(['update:modelValue', 'updated'])

// 组合式函数
const paymentOptimization = usePaymentOptimization()

// 响应式数据
const loading = ref(false)
const activeTab = ref('basic')

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const scenarioData = computed(() => props.scenario || {})

// 优化历史数据
const optimizationHistory = ref([
  {
    date: '2024-01-15 14:30',
    title: '支付方式优化',
    description: '调整支付宝和微信支付的显示顺序',
    type: 'success',
    result: '转化率提升 2.3%'
  },
  {
    date: '2024-01-10 09:15',
    title: '表单简化',
    description: '减少必填字段，启用自动填充',
    type: 'success',
    result: '支付完成率提升 1.8%'
  },
  {
    date: '2024-01-05 16:45',
    title: '风控策略调整',
    description: '降低中低风险交易的风控级别',
    type: 'warning',
    result: '处理时间减少 15%'
  },
  {
    date: '2024-01-01 10:00',
    title: '场景创建',
    description: '创建电商购物支付场景',
    type: 'primary',
    result: '场景配置完成'
  }
])

// 工具方法
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

const formatAmount = (amount) => {
  if (amount >= 10000) {
    return (amount / 10000).toFixed(1) + '万'
  }
  return '¥' + amount.toLocaleString()
}

const formatNumber = (num) => {
  return num.toLocaleString()
}

const getProgressColor = (ratio) => {
  if (ratio >= 1) return '#67C23A'
  if (ratio >= 0.8) return '#E6A23C'
  return '#F56C6C'
}

const getScenarioTypeColor = (type) => {
  const colorMap = {
    checkout: '#409EFF',
    subscription: '#67C23A',
    donation: '#E6A23C',
    marketplace: '#F56C6C',
    onepay: '#909399'
  }
  return colorMap[type] || '#909399'
}

const getScenarioTypeLabel = (type) => {
  const labelMap = {
    checkout: '结账支付',
    subscription: '订阅支付',
    donation: '捐赠支付',
    marketplace: '市场支付',
    onepay: '一键支付'
  }
  return labelMap[type] || type
}

const getBusinessTypeLabel = (type) => {
  const typeMap = {
    ecommerce: '电商购物',
    subscription: '订阅服务',
    education: '在线教育',
    healthcare: '医疗服务',
    travel: '旅游出行',
    entertainment: '娱乐服务',
    real_estate: '房地产',
    automotive: '汽车服务'
  }
  return typeMap[type] || type
}

const getTargetMarketLabel = (value) => {
  const marketMap = {
    domestic: '国内',
    overseas: '海外',
    global: '全球'
  }
  return marketMap[value] || value
}

const getStatusType = (status) => {
  const typeMap = {
    draft: 'info',
    active: 'success',
    optimizing: 'warning',
    optimized: 'success',
    suspended: 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusLabel = (status) => {
  const labelMap = {
    draft: '草稿',
    active: '活跃',
    optimizing: '优化中',
    optimized: '已优化',
    suspended: '已暂停'
  }
  return labelMap[status] || status
}

const getRiskLevelType = (level) => {
  const typeMap = {
    low: 'success',
    medium: 'warning',
    high: 'danger'
  }
  return typeMap[level] || 'info'
}

const getRiskLevelLabel = (level) => {
  const labelMap = {
    low: '低',
    medium: '中',
    high: '高'
  }
  return labelMap[level] || level
}

const getPaymentMethodName = (methodId) => {
  const method = paymentOptimization.scenarioConfig.paymentMethods.find(m => m.id === methodId)
  return method ? method.name : methodId
}

const getPaymentMethodFee = (methodId) => {
  const method = paymentOptimization.scenarioConfig.paymentMethods.find(m => m.id === methodId)
  return method ? method.fee : 0
}

const getMethodUsageRate = (methodId) => {
  const usageRates = {
    alipay: 45.2,
    wechat: 38.7,
    bank_card: 12.1,
    credit_card: 3.5,
    other: 0.5
  }
  return usageRates[methodId] || 0
}

const getMethodSuccessRate = (methodId) => {
  const successRates = {
    alipay: 98.5,
    wechat: 97.8,
    bank_card: 94.2,
    credit_card: 96.1,
    other: 95.3
  }
  return successRates[methodId] || 0
}

// 方法
const handleClose = () => {
  dialogVisible.value = false
  activeTab.value = 'basic'
}

const handleEdit = () => {
  emit('updated', { action: 'edit', scenario: scenarioData.value })
  dialogVisible.value = false
}

const handleAnalytics = () => {
  emit('updated', { action: 'analytics', scenario: scenarioData.value })
  dialogVisible.value = false
}

const handleOptimize = () => {
  emit('updated', { action: 'optimize', scenario: scenarioData.value })
  dialogVisible.value = false
}
</script>

<style scoped>
.scenario-detail-content {
  max-height: 500px;
  overflow-y: auto;
}

.detail-tabs {
  margin-top: -20px;
}

.basic-info {
  padding: 16px 0;
}

.description-section {
  margin: 20px 0;
}

.description-section h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.description-section p {
  margin: 0;
  color: #606266;
  line-height: 1.6;
}

.metrics-section {
  margin-top: 20px;
}

.metrics-section h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.metric-card {
  text-align: center;
  padding: 16px;
  border: 1px solid #EBEEF5;
  border-radius: 8px;
}

.metric-value {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.metric-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 12px;
}

.metric-progress {
  margin-top: 8px;
}

.metric-trend {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 12px;
  margin-top: 8px;
}

.metric-trend.positive {
  color: #67C23A;
}

.payment-config {
  padding: 16px 0;
}

.config-section {
  margin-bottom: 24px;
}

.config-section h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.payment-methods {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.payment-method-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border: 1px solid #EBEEF5;
  border-radius: 8px;
}

.method-info {
  flex: 1;
}

.method-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.method-fee {
  font-size: 12px;
  color: #909399;
}

.method-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.business-rules {
  padding: 16px;
  background-color: #f5f5f5;
  border-radius: 8px;
  color: #606266;
  line-height: 1.6;
}

.optimization-config {
  padding: 16px 0;
}

.timeline-content {
  margin-bottom: 8px;
}

.timeline-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.timeline-description {
  font-size: 13px;
  color: #606266;
  margin-bottom: 8px;
}

.timeline-result {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.result-label {
  color: #909399;
}

.result-value {
  color: #67C23A;
  font-weight: 500;
}

.analytics-data {
  padding: 16px 0;
}

.chart-section {
  margin-bottom: 24px;
}

.chart-section h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.chart-placeholder {
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 14px;
  border: 1px dashed #EBEEF5;
  border-radius: 8px;
}

.chart-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.metrics-comparison h4 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 500;
}

.comparison-card {
  padding: 16px;
  border: 1px solid #EBEEF5;
  border-radius: 8px;
  margin-bottom: 12px;
}

.comparison-card h5 {
  margin: 0 0 12px 0;
  color: #303133;
  font-size: 14px;
  font-weight: 500;
}

.comparison-metrics {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.comparison-metric {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comparison-metric .metric-label {
  font-size: 13px;
  color: #606266;
}

.comparison-metric .metric-value {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.dialog-footer {
  text-align: right;
}
</style>