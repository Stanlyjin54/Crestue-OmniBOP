<template>
  <el-dialog
    v-model="dialogVisible"
    title="支付数据分析"
    width="1200px"
    :before-close="handleClose"
  >
    <div class="analytics-content">
      <!-- 概览统计 -->
      <div class="overview-section">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(metric, key) in overviewMetrics" :key="key">
            <el-card class="metric-card" shadow="hover">
              <div class="metric-content">
                <div class="metric-icon" :style="{ backgroundColor: metric.color + '20', color: metric.color }">
                  <el-icon><component :is="metric.icon" /></el-icon>
                </div>
                <div class="metric-info">
                  <div class="metric-value">{{ metric.value }}</div>
                  <div class="metric-label">{{ metric.label }}</div>
                  <div class="metric-change" :style="{ color: metric.change.color }">
                    <el-icon><component :is="metric.change.icon" /></el-icon>
                    <span>{{ metric.change.value }}</span>
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
          <!-- 转化漏斗图 -->
          <el-col :span="12">
            <el-card class="chart-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span>支付转化漏斗</span>
                  <el-select v-model="selectedFunnelPeriod" size="small" style="width: 100px">
                    <el-option label="7天" value="7d" />
                    <el-option label="30天" value="30d" />
                    <el-option label="90天" value="90d" />
                  </el-select>
                </div>
              </template>
              <div class="funnel-chart">
                <div class="funnel-stage" v-for="(stage, index) in funnelData" :key="stage.name">
                  <div class="stage-bar">
                    <div 
                      class="stage-fill" 
                      :style="{ 
                        width: stage.percentage + '%',
                        backgroundColor: stage.color
                      }"
                    ></div>
                    <div class="stage-content">
                      <span class="stage-name">{{ stage.name }}</span>
                      <span class="stage-value">{{ stage.value.toLocaleString() }}</span>
                      <span class="stage-percentage">{{ stage.percentage.toFixed(1) }}%</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>

          <!-- 支付方式分布 -->
          <el-col :span="12">
            <el-card class="chart-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span>支付方式分布</span>
                  <el-select v-model="selectedMethodPeriod" size="small" style="width: 100px">
                    <el-option label="7天" value="7d" />
                    <el-option label="30天" value="30d" />
                    <el-option label="90天" value="90d" />
                  </el-select>
                </div>
              </template>
              <div class="method-distribution">
                <div class="method-item" v-for="method in paymentMethodData" :key="method.name">
                  <div class="method-info">
                    <span class="method-name">{{ method.name }}</span>
                    <span class="method-percentage">{{ method.percentage.toFixed(1) }}%</span>
                  </div>
                  <div class="method-bar">
                    <div 
                      class="method-fill"
                      :style="{ 
                        width: method.percentage + '%',
                        backgroundColor: method.color
                      }"
                    ></div>
                  </div>
                  <div class="method-stats">
                    <span>{{ method.volume.toLocaleString() }} 笔</span>
                    <span>{{ formatAmount(method.amount) }}</span>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px">
          <!-- 交易趋势 -->
          <el-col :span="16">
            <el-card class="chart-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span>交易趋势分析</span>
                  <el-select v-model="selectedTrendPeriod" size="small" style="width: 100px">
                    <el-option label="7天" value="7d" />
                    <el-option label="30天" value="30d" />
                    <el-option label="90天" value="90d" />
                  </el-select>
                </div>
              </template>
              <div class="trend-chart">
                <!-- 图表占位符 -->
                <div class="chart-placeholder">
                  <el-icon><TrendCharts /></el-icon>
                  <span>交易趋势图表</span>
                </div>
              </div>
            </el-card>
          </el-col>

          <!-- 场景对比 -->
          <el-col :span="8">
            <el-card class="chart-card" shadow="never">
              <template #header>
                <span>场景性能对比</span>
              </template>
              <div class="scenario-comparison">
                <div class="scenario-item" v-for="scenario in scenarioComparison" :key="scenario.name">
                  <div class="scenario-header">
                    <span class="scenario-name">{{ scenario.name }}</span>
                    <el-tag :type="scenario.performance > 80 ? 'success' : scenario.performance > 60 ? 'warning' : 'danger'">
                      {{ scenario.performance }}分
                    </el-tag>
                  </div>
                  <div class="scenario-metrics">
                    <div class="metric">
                      <span class="metric-label">转化率</span>
                      <span class="metric-value">{{ scenario.conversionRate }}%</span>
                    </div>
                    <div class="metric">
                      <span class="metric-label">交易量</span>
                      <span class="metric-value">{{ scenario.volume.toLocaleString() }}</span>
                    </div>
                    <div class="metric">
                      <span class="metric-label">平均金额</span>
                      <span class="metric-value">{{ formatAmount(scenario.averageAmount) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 详细数据表格 -->
      <div class="table-section">
        <el-card shadow="never">
          <template #header>
            <div class="table-header">
              <span>详细分析数据</span>
              <div class="table-actions">
                <el-button type="default" :icon="Download" @click="exportAnalytics">
                  导出报告
                </el-button>
                <el-button type="default" :icon="Refresh" @click="refreshData">
                  刷新数据
                </el-button>
              </div>
            </div>
          </template>

          <el-tabs v-model="activeTab">
            <el-tab-pane label="转化分析" name="conversion">
              <el-table :data="conversionData" stripe style="width: 100%">
                <el-table-column prop="stage" label="转化阶段" width="150" />
                <el-table-column prop="count" label="数量" width="120">
                  <template #default="{ row }">
                    {{ row.count.toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column prop="rate" label="转化率" width="100">
                  <template #default="{ row }">
                    <span class="conversion-rate">{{ row.rate }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="dropRate" label="流失率" width="100">
                  <template #default="{ row }">
                    <span class="drop-rate">{{ row.dropRate }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="avgTime" label="平均耗时" width="120">
                  <template #default="{ row }">
                    {{ formatTime(row.avgTime) }}
                  </template>
                </el-table-column>
                <el-table-column prop="notes" label="备注" min-width="200" />
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="支付分析" name="payment">
              <el-table :data="paymentAnalysisData" stripe style="width: 100%">
                <el-table-column prop="method" label="支付方式" width="150" />
                <el-table-column prop="volume" label="交易量" width="120">
                  <template #default="{ row }">
                    {{ row.volume.toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column prop="amount" label="交易金额" width="150">
                  <template #default="{ row }">
                    {{ formatAmount(row.amount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="successRate" label="成功率" width="100">
                  <template #default="{ row }">
                    <span class="success-rate">{{ row.successRate }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="avgTime" label="平均处理时间" width="120">
                  <template #default="{ row }">
                    {{ formatTime(row.avgTime) }}
                  </template>
                </el-table-column>
                <el-table-column prop="fee" label="手续费" width="100">
                  <template #default="{ row }">
                    {{ row.fee }}%
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="时间分析" name="time">
              <el-table :data="timeAnalysisData" stripe style="width: 100%">
                <el-table-column prop="hour" label="时间段" width="120">
                  <template #default="{ row }">
                    {{ row.hour }}:00 - {{ row.hour + 1 }}:00
                  </template>
                </el-table-column>
                <el-table-column prop="volume" label="交易量" width="120">
                  <template #default="{ row }">
                    {{ row.volume.toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column prop="amount" label="交易金额" width="150">
                  <template #default="{ row }">
                    {{ formatAmount(row.amount) }}
                  </template>
                </el-table-column>
                <el-table-column prop="conversionRate" label="转化率" width="100">
                  <template #default="{ row }">
                    {{ row.conversionRate }}%
                  </template>
                </el-table-column>
                <el-table-column prop="peakHour" label="高峰时段" width="100">
                  <template #default="{ row }">
                    <el-tag v-if="row.peakHour" type="danger" size="small">高峰</el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button type="primary" @click="generateReport">
          生成报告
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Money, TrendCharts, DataAnalysis, Download, Refresh, 
  ArrowUp, ArrowDown, Warning, Check, Clock
} from '@element-plus/icons-vue'
import { usePaymentOptimization } from '@/composables/usePaymentOptimization'

// 组件props
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

// 组件emit
const emit = defineEmits(['update:modelValue'])

// 组合式函数
const paymentOptimization = usePaymentOptimization()

// 响应式数据
const loading = ref(false)
const activeTab = ref('conversion')
const selectedFunnelPeriod = ref('30d')
const selectedMethodPeriod = ref('30d')
const selectedTrendPeriod = ref('30d')

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 概览指标
const overviewMetrics = computed(() => {
  const analytics = props.data || {}
  return {
    totalTransactions: {
      label: '总交易数',
      value: (analytics.totalTransactions || 0).toLocaleString(),
      icon: 'Money',
      color: '#409EFF',
      change: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+12.5%'
      }
    },
    totalAmount: {
      label: '总交易额',
      value: formatAmount(analytics.totalAmount || 0),
      icon: 'TrendCharts',
      color: '#67C23A',
      change: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+8.3%'
      }
    },
    successRate: {
      label: '成功率',
      value: (analytics.successRate || 0).toFixed(1) + '%',
      icon: 'Check',
      color: '#E6A23C',
      change: {
        icon: 'ArrowDown',
        color: '#F56C6C',
        value: '-0.8%'
      }
    },
    avgProcessingTime: {
      label: '平均处理时间',
      value: formatTime(analytics.avgProcessingTime || 0),
      icon: 'Clock',
      color: '#909399',
      change: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '-2.1s'
      }
    }
  }
})

// 转化漏斗数据
const funnelData = ref([
  { name: '页面访问', value: 10000, percentage: 100, color: '#409EFF' },
  { name: '添加购物车', value: 3500, percentage: 35, color: '#67C23A' },
  { name: '进入支付', value: 2100, percentage: 21, color: '#E6A23C' },
  { name: '选择支付方式', value: 1800, percentage: 18, color: '#F56C6C' },
  { name: '支付完成', value: 1650, percentage: 16.5, color: '#909399' }
])

// 支付方式分布数据
const paymentMethodData = ref([
  { name: '支付宝', percentage: 45.2, volume: 74640, amount: 3732000, color: '#1677FF' },
  { name: '微信支付', percentage: 38.7, volume: 63855, amount: 3192750, color: '#07C160' },
  { name: '银行卡', percentage: 12.1, volume: 19965, amount: 997950, color: '#FF6B35' },
  { name: '其他', percentage: 4.0, volume: 6600, amount: 330000, color: '#909399' }
])

// 场景对比数据
const scenarioComparison = ref([
  { 
    name: '电商购物', 
    performance: 85, 
    conversionRate: 16.5, 
    volume: 45000, 
    averageAmount: 299.5 
  },
  { 
    name: '在线教育', 
    performance: 78, 
    conversionRate: 12.8, 
    volume: 28000, 
    averageAmount: 599.0 
  },
  { 
    name: '订阅服务', 
    performance: 92, 
    conversionRate: 22.1, 
    volume: 35000, 
    averageAmount: 129.9 
  },
  { 
    name: '医疗服务', 
    performance: 71, 
    conversionRate: 8.9, 
    volume: 18000, 
    averageAmount: 899.0 
  }
])

// 详细数据
const conversionData = ref([
  { stage: '页面访问', count: 10000, rate: 100, dropRate: 0, avgTime: 45, notes: '基准数据' },
  { stage: '浏览商品', count: 8500, rate: 85, dropRate: 15, avgTime: 180, notes: '用户兴趣度较高' },
  { stage: '添加购物车', count: 3500, rate: 35, dropRate: 50, avgTime: 60, notes: '价格敏感度分析' },
  { stage: '进入支付', count: 2100, rate: 21, dropRate: 40, avgTime: 30, notes: '支付意愿强' },
  { stage: '完成支付', count: 1650, rate: 16.5, dropRate: 21.4, avgTime: 120, notes: '整体转化率良好' }
])

const paymentAnalysisData = ref([
  { method: '支付宝', volume: 74640, amount: 3732000, successRate: 98.5, avgTime: 2.8, fee: 0.6 },
  { method: '微信支付', volume: 63855, amount: 3192750, successRate: 97.8, avgTime: 3.2, fee: 0.6 },
  { method: '银行卡', volume: 19965, amount: 997950, successRate: 94.2, avgTime: 15.6, fee: 0.8 },
  { method: '其他', volume: 6600, amount: 330000, successRate: 95.1, avgTime: 8.4, fee: 1.2 }
])

const timeAnalysisData = ref([
  { hour: 0, volume: 1200, amount: 60000, conversionRate: 12.5, peakHour: false },
  { hour: 6, volume: 800, amount: 40000, conversionRate: 10.2, peakHour: false },
  { hour: 9, volume: 3500, amount: 175000, conversionRate: 18.9, peakHour: true },
  { hour: 12, volume: 2800, amount: 140000, conversionRate: 16.7, peakHour: false },
  { hour: 15, volume: 4200, amount: 210000, conversionRate: 19.2, peakHour: true },
  { hour: 18, volume: 3800, amount: 190000, conversionRate: 17.8, peakHour: true },
  { hour: 21, volume: 2500, amount: 125000, conversionRate: 15.4, peakHour: false },
  { hour: 23, volume: 1500, amount: 75000, conversionRate: 13.8, peakHour: false }
])

// 工具方法
const formatAmount = (amount) => {
  if (amount >= 10000) {
    return (amount / 10000).toFixed(1) + '万'
  }
  return '¥' + amount.toLocaleString()
}

const formatTime = (seconds) => {
  if (seconds < 60) {
    return seconds.toFixed(1) + 's'
  }
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = (seconds % 60).toFixed(1)
  return `${minutes}m ${remainingSeconds}s`
}

// 方法
const handleClose = () => {
  dialogVisible.value = false
}

const refreshData = async () => {
  loading.value = true
  try {
    // 模拟数据刷新
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败')
  } finally {
    loading.value = false
  }
}

const exportAnalytics = () => {
  ElMessage.info('正在生成分析报告...')
  // 实现导出逻辑
}

const generateReport = () => {
  ElMessage.info('正在生成详细报告...')
  // 实现报告生成逻辑
}
</script>

<style scoped>
.analytics-content {
  max-height: 600px;
  overflow-y: auto;
}

.overview-section {
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
  margin-bottom: 2px;
}

.metric-change {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  margin-top: 8px;
}

.charts-section {
  margin-bottom: 20px;
}

.chart-card {
  border: none;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.funnel-chart {
  padding: 20px 0;
}

.funnel-stage {
  margin-bottom: 16px;
}

.stage-bar {
  position: relative;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f5f5f5;
}

.stage-fill {
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  transition: width 0.3s ease;
}

.stage-content {
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 16px;
  color: white;
  font-weight: 500;
}

.stage-name {
  font-size: 14px;
}

.stage-value {
  font-size: 16px;
}

.stage-percentage {
  font-size: 12px;
  opacity: 0.9;
}

.method-distribution {
  padding: 20px 0;
}

.method-item {
  margin-bottom: 20px;
}

.method-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.method-name {
  font-weight: 500;
  color: #303133;
}

.method-percentage {
  font-weight: 600;
  color: #409EFF;
}

.method-bar {
  height: 8px;
  background-color: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.method-fill {
  height: 100%;
  transition: width 0.3s ease;
}

.method-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.trend-chart {
  height: 250px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #909399;
  font-size: 14px;
}

.chart-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.scenario-comparison {
  padding: 20px 0;
}

.scenario-item {
  margin-bottom: 24px;
  padding: 16px;
  border: 1px solid #EBEEF5;
  border-radius: 8px;
}

.scenario-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.scenario-name {
  font-weight: 500;
  color: #303133;
}

.scenario-metrics {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 16px;
}

.metric {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.metric-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.metric-value {
  font-weight: 600;
  color: #303133;
}

.table-section {
  margin-bottom: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-actions {
  display: flex;
  gap: 12px;
}

.conversion-rate {
  color: #67C23A;
  font-weight: 500;
}

.drop-rate {
  color: #F56C6C;
  font-weight: 500;
}

.success-rate {
  color: #67C23A;
  font-weight: 500;
}

.dialog-footer {
  text-align: right;
}
</style>