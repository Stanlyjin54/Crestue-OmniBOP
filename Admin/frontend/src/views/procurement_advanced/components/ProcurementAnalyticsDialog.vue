<template>
  <div class="procurement-analytics-dialog">
    <el-dialog
      v-model="dialogVisible"
      title="采购数据分析"
      width="1200px"
      :before-close="handleClose"
    >
      <!-- 筛选条件 -->
      <div class="analytics-filters">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              @change="handleDateChange"
            />
          </el-col>
          <el-col :span="4">
            <el-select v-model="analyticsType" placeholder="分析类型" @change="handleTypeChange">
              <el-option
                v-for="option in analyticsTypes"
                :key="option.value"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="supplierFilter" placeholder="供应商筛选" clearable @change="handleSupplierChange">
              <el-option
                v-for="supplier in supplierOptions"
                :key="supplier.value"
                :label="supplier.label"
                :value="supplier.value"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select v-model="categoryFilter" placeholder="产品类别" clearable @change="handleCategoryChange">
              <el-option
                v-for="category in categoryOptions"
                :key="category.value"
                :label="category.label"
                :value="category.value"
              />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-button-group>
              <el-button @click="handleRefresh">
                <el-icon><Refresh /></el-icon>
                刷新
              </el-button>
              <el-button @click="handleExport">
                <el-icon><Download /></el-icon>
                导出
              </el-button>
              <el-button @click="handlePrint">
                <el-icon><Printer /></el-icon>
                打印
              </el-button>
            </el-button-group>
          </el-col>
        </el-row>
      </div>

      <div class="analytics-content">
        <!-- 概览统计 -->
        <div class="overview-section">
          <h3>
            <el-icon><TrendCharts /></el-icon>
            数据概览
          </h3>
          <el-row :gutter="20">
            <el-col :span="6" v-for="metric in overviewMetrics" :key="metric.key">
              <div class="metric-card">
                <div class="metric-icon" :style="{ backgroundColor: metric.color }">
                  <el-icon><component :is="metric.icon" /></el-icon>
                </div>
                <div class="metric-content">
                  <div class="metric-value">{{ formatValue(metric.value, metric.type) }}</div>
                  <div class="metric-label">{{ metric.label }}</div>
                  <div class="metric-trend" :class="metric.trend > 0 ? 'positive' : 'negative'">
                    <el-icon><ArrowUp v-if="metric.trend > 0" /><ArrowDown v-else /></el-icon>
                    {{ Math.abs(metric.trend) }}%
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 图表分析区域 -->
        <div class="charts-section">
          <el-tabs v-model="activeChartTab" type="border-card">
            <!-- 成本分析 -->
            <el-tab-pane label="成本分析" name="cost">
              <div class="chart-container">
                <div class="chart-header">
                  <h4>采购成本趋势分析</h4>
                  <el-radio-group v-model="costChartType" size="small">
                    <el-radio-button label="trend">趋势图</el-radio-button>
                    <el-radio-button label="compare">对比图</el-radio-button>
                    <el-radio-button label="breakdown">成本构成</el-radio-button>
                  </el-radio-group>
                </div>
                <div class="chart-content">
                  <div v-if="costChartType === 'trend'" class="trend-chart">
                    <div class="chart-canvas">
                      <!-- 这里应该集成图表库，如ECharts -->
                      <div class="placeholder-chart">
                        <el-icon><TrendCharts /></el-icon>
                        <p>成本趋势图表</p>
                        <small>显示月度采购成本变化趋势</small>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="costChartType === 'compare'" class="compare-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><DataAnalysis /></el-icon>
                        <p>成本对比图表</p>
                        <small>对比不同供应商/类别的成本</small>
                      </div>
                    </div>
                  </div>
                  <div v-else class="breakdown-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><PieChart /></el-icon>
                        <p>成本构成图表</p>
                        <small>展示各项成本占比</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 供应商分析 -->
            <el-tab-pane label="供应商分析" name="supplier">
              <div class="chart-container">
                <div class="chart-header">
                  <h4>供应商表现分析</h4>
                  <el-radio-group v-model="supplierChartType" size="small">
                    <el-radio-button label="performance">绩效分析</el-radio-button>
                    <el-radio-button label="ranking">排名分析</el-radio-button>
                    <el-radio-button label="comparison">对比分析</el-radio-button>
                  </el-radio-group>
                </div>
                <div class="chart-content">
                  <div v-if="supplierChartType === 'performance'" class="performance-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Star /></el-icon>
                        <p>供应商绩效图表</p>
                        <small>质量、交付、服务等指标评分</small>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="supplierChartType === 'ranking'" class="ranking-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Trophy /></el-icon>
                        <p>供应商排名图表</p>
                        <small>按综合评分排名</small>
                      </div>
                    </div>
                  </div>
                  <div v-else class="comparison-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><DataLine /></el-icon>
                        <p>供应商对比图表</p>
                        <small>多维度对比分析</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 采购趋势 -->
            <el-tab-pane label="采购趋势" name="trend">
              <div class="chart-container">
                <div class="chart-header">
                  <h4>采购活动趋势分析</h4>
                  <el-radio-group v-model="trendChartType" size="small">
                    <el-radio-button label="volume">采购量趋势</el-radio-button>
                    <el-radio-button label="frequency">采购频次</el-radio-button>
                    <el-radio-button label="seasonality">季节性分析</el-radio-button>
                  </el-radio-group>
                </div>
                <div class="chart-content">
                  <div v-if="trendChartType === 'volume'" class="volume-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><TrendCharts /></el-icon>
                        <p>采购量趋势图表</p>
                        <small>按时间序列展示采购量变化</small>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="trendChartType === 'frequency'" class="frequency-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Histogram /></el-icon>
                        <p>采购频次图表</p>
                        <small>展示采购频次分布</small>
                      </div>
                    </div>
                  </div>
                  <div v-else class="seasonality-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Calendar /></el-icon>
                        <p>季节性分析图表</p>
                        <small>分析采购的季节性规律</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 风险分析 -->
            <el-tab-pane label="风险分析" name="risk">
              <div class="chart-container">
                <div class="chart-header">
                  <h4>采购风险分析</h4>
                  <el-radio-group v-model="riskChartType" size="small">
                    <el-radio-button label="risk_map">风险地图</el-radio-button>
                    <el-radio-button label="concentration">集中度分析</el-radio-button>
                    <el-radio-button label="alert">风险预警</el-radio-button>
                  </el-radio-group>
                </div>
                <div class="chart-content">
                  <div v-if="riskChartType === 'risk_map'" class="risk-map-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Warning /></el-icon>
                        <p>风险地图图表</p>
                        <small>供应商风险等级分布</small>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="riskChartType === 'concentration'" class="concentration-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><PieChart /></el-icon>
                        <p>集中度分析图表</p>
                        <small>供应商集中度风险</small>
                      </div>
                    </div>
                  </div>
                  <div v-else class="alert-chart">
                    <div class="chart-canvas">
                      <div class="placeholder-chart">
                        <el-icon><Bell /></el-icon>
                        <p>风险预警图表</p>
                        <small>潜在风险点监控</small>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 详细数据表格 -->
        <div class="data-table-section">
          <h3>
            <el-icon><DataAnalysis /></el-icon>
            详细数据
          </h3>
          <el-tabs v-model="activeTableTab">
            <el-tab-pane label="成本明细" name="cost_detail">
              <el-table :data="costDetailData" stripe style="width: 100%" :height="300">
                <el-table-column prop="date" label="日期" width="120" />
                <el-table-column prop="supplier" label="供应商" width="150" />
                <el-table-column prop="category" label="产品类别" width="120" />
                <el-table-column prop="amount" label="采购金额" width="120">
                  <template #default="{ row }">
                    ¥{{ row.amount.toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column prop="quantity" label="采购数量" width="100" />
                <el-table-column prop="unitPrice" label="单价" width="120">
                  <template #default="{ row }">
                    ¥{{ row.unitPrice.toFixed(2) }}
                  </template>
                </el-table-column>
                <el-table-column prop="savings" label="节约金额" width="120">
                  <template #default="{ row }">
                    <span class="savings-positive" v-if="row.savings > 0">
                      +¥{{ row.savings.toLocaleString() }}
                    </span>
                    <span class="savings-negative" v-else-if="row.savings < 0">
                      -¥{{ Math.abs(row.savings).toLocaleString() }}
                    </span>
                    <span v-else>-</span>
                  </template>
                </el-table-column>
                <el-table-column prop="notes" label="备注" />
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="供应商绩效" name="supplier_performance">
              <el-table :data="supplierPerformanceData" stripe style="width: 100%" :height="300">
                <el-table-column prop="supplier" label="供应商名称" width="150" />
                <el-table-column prop="totalOrders" label="订单总数" width="100" />
                <el-table-column prop="totalAmount" label="总金额" width="120">
                  <template #default="{ row }">
                    ¥{{ row.totalAmount.toLocaleString() }}
                  </template>
                </el-table-column>
                <el-table-column prop="onTimeRate" label="准时率" width="100">
                  <template #default="{ row }">
                    <el-progress :percentage="row.onTimeRate" :stroke-width="8" />
                  </template>
                </el-table-column>
                <el-table-column prop="qualityScore" label="质量评分" width="100">
                  <template #default="{ row }">
                    <el-rate v-model="row.qualityScore" disabled show-score />
                  </template>
                </el-table-column>
                <el-table-column prop="serviceScore" label="服务评分" width="100">
                  <template #default="{ row }">
                    <el-rate v-model="row.serviceScore" disabled show-score />
                  </template>
                </el-table-column>
                <el-table-column prop="overallRating" label="综合评级" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getRatingType(row.overallRating)">
                      {{ row.overallRating }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>

            <el-tab-pane label="风险监控" name="risk_monitoring">
              <el-table :data="riskMonitoringData" stripe style="width: 100%" :height="300">
                <el-table-column prop="supplier" label="供应商" width="150" />
                <el-table-column prop="riskType" label="风险类型" width="120">
                  <template #default="{ row }">
                    <el-tag :type="getRiskTagType(row.riskType)">
                      {{ row.riskType }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="riskLevel" label="风险等级" width="100">
                  <template #default="{ row }">
                    <el-tag :type="getRiskLevelType(row.riskLevel)">
                      {{ row.riskLevel }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="description" label="风险描述" />
                <el-table-column prop="probability" label="发生概率" width="100">
                  <template #default="{ row }">
                    <el-progress :percentage="row.probability" :stroke-width="8" />
                  </template>
                </el-table-column>
                <el-table-column prop="impact" label="影响程度" width="100">
                  <template #default="{ row }">
                    <el-progress :percentage="row.impact" :stroke-width="8" />
                  </template>
                </el-table-column>
                <el-table-column prop="action" label="应对措施" />
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.status === 'resolved' ? 'success' : 'warning'">
                      {{ row.status === 'resolved' ? '已解决' : '监控中' }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 智能建议 -->
        <div class="insights-section">
          <h3>
            <el-icon><Lightbulb /></el-icon>
            智能分析建议
          </h3>
          <div class="insights-grid">
            <div v-for="insight in insights" :key="insight.id" class="insight-card">
              <div class="insight-header">
                <el-icon :class="['insight-icon', insight.type]">
                  <component :is="getInsightIcon(insight.type)" />
                </el-icon>
                <span class="insight-title">{{ insight.title }}</span>
                <el-tag :type="getInsightTagType(insight.priority)" size="small">
                  {{ insight.priority }}
                </el-tag>
              </div>
              <div class="insight-content">
                <p>{{ insight.description }}</p>
                <div class="insight-actions" v-if="insight.actions">
                  <el-button
                    v-for="action in insight.actions"
                    :key="action"
                    size="small"
                    @click="handleInsightAction(action, insight)"
                  >
                    {{ action }}
                  </el-button>
                </div>
              </div>
              <div class="insight-footer">
                <span class="insight-impact">预期影响: {{ insight.impact }}</span>
                <span class="insight-confidence">可信度: {{ insight.confidence }}%</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Refresh, Download, Printer, TrendCharts, DataAnalysis, PieChart,
  Star, Trophy, DataLine, Histogram, Calendar, Warning, Bell,
  Lightbulb, ArrowUp, ArrowDown
} from '@element-plus/icons-vue'
import { useProcurementAdvanced } from '@/composables/useProcurementAdvanced'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:visible'])

// 组合式函数
const {
  getProcurementAnalytics,
  exportAnalyticsData
} = useProcurementAdvanced()

// 响应式数据
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

// 筛选条件
const dateRange = ref([])
const analyticsType = ref('comprehensive')
const supplierFilter = ref('')
const categoryFilter = ref('')

// 图表配置
const activeChartTab = ref('cost')
const costChartType = ref('trend')
const supplierChartType = ref('performance')
const trendChartType = ref('volume')
const riskChartType = ref('risk_map')

// 表格配置
const activeTableTab = ref('cost_detail')

// 配置数据
const analyticsTypes = [
  { label: '综合分析', value: 'comprehensive' },
  { label: '成本分析', value: 'cost' },
  { label: '供应商分析', value: 'supplier' },
  { label: '风险分析', value: 'risk' },
  { label: '效率分析', value: 'efficiency' }
]

const supplierOptions = ref([
  { label: '供应商A', value: 'supplier_a' },
  { label: '供应商B', value: 'supplier_b' },
  { label: '供应商C', value: 'supplier_c' }
])

const categoryOptions = ref([
  { label: '电子元器件', value: 'electronics' },
  { label: '机械配件', value: 'mechanical' },
  { label: '化工原料', value: 'chemical' },
  { label: '包装材料', value: 'packaging' }
])

// 数据
const overviewMetrics = ref([
  {
    key: 'totalCost',
    label: '总采购成本',
    value: 2580000,
    type: 'currency',
    trend: 8.5,
    color: '#409eff',
    icon: 'Money'
  },
  {
    key: 'costSaving',
    label: '成本节约',
    value: 156000,
    type: 'currency',
    trend: 12.3,
    color: '#67c23a',
    icon: 'Discount'
  },
  {
    key: 'supplierCount',
    label: '活跃供应商',
    value: 45,
    type: 'number',
    trend: -2.1,
    color: '#e6a23c',
    icon: 'OfficeBuilding'
  },
  {
    key: 'orderEfficiency',
    label: '订单效率',
    value: 92.5,
    type: 'percentage',
    trend: 5.7,
    color: '#f56c6c',
    icon: 'Clock'
  }
])

const costDetailData = ref([
  {
    date: '2024-01-15',
    supplier: '供应商A',
    category: '电子元器件',
    amount: 125000,
    quantity: 500,
    unitPrice: 250,
    savings: 8500,
    notes: '批量采购优惠'
  },
  {
    date: '2024-01-16',
    supplier: '供应商B',
    category: '机械配件',
    amount: 89000,
    quantity: 200,
    unitPrice: 445,
    savings: -3200,
    notes: '原材料价格上涨'
  }
])

const supplierPerformanceData = ref([
  {
    supplier: '供应商A',
    totalOrders: 156,
    totalAmount: 2580000,
    onTimeRate: 95,
    qualityScore: 4.5,
    serviceScore: 4.2,
    overallRating: 'A+'
  },
  {
    supplier: '供应商B',
    totalOrders: 89,
    totalAmount: 1560000,
    onTimeRate: 88,
    qualityScore: 4.0,
    serviceScore: 3.8,
    overallRating: 'A'
  }
])

const riskMonitoringData = ref([
  {
    supplier: '供应商C',
    riskType: '财务风险',
    riskLevel: '高',
    description: '供应商财务状况恶化',
    probability: 75,
    impact: 80,
    action: '寻找替代供应商',
    status: 'monitoring'
  },
  {
    supplier: '供应商A',
    riskType: '供应链风险',
    riskLevel: '中',
    description: '主要原材料供应商集中度高',
    probability: 45,
    impact: 60,
    action: '建立备用供应商',
    status: 'resolved'
  }
])

const insights = ref([
  {
    id: 1,
    type: 'cost',
    title: '成本优化建议',
    description: '通过集中采购和长期合同，预计可节约15%的采购成本',
    priority: 'high',
    impact: '节约¥387,000',
    confidence: 85,
    actions: ['查看详细方案', '联系供应商', '制定采购计划']
  },
  {
    id: 2,
    type: 'supplier',
    title: '供应商优化',
    description: '建议增加2-3家备选供应商以降低供应风险',
    priority: 'medium',
    impact: '风险降低30%',
    confidence: 78,
    actions: ['供应商评估', '建立联系']
  },
  {
    id: 3,
    type: 'risk',
    title: '风险预警',
    description: '检测到供应商C存在潜在风险，建议提前准备应对措施',
    priority: 'high',
    impact: '避免¥200,000损失',
    confidence: 92,
    actions: ['查看详情', '制定预案']
  }
])

// 工具方法
const formatValue = (value, type) => {
  switch (type) {
    case 'currency':
      return `¥${value.toLocaleString()}`
    case 'percentage':
      return `${value}%`
    case 'number':
    default:
      return value.toString()
  }
}

const getRatingType = (rating) => {
  if (rating.startsWith('A+')) return 'success'
  if (rating.startsWith('A')) return 'primary'
  if (rating.startsWith('B')) return 'warning'
  return 'danger'
}

const getRiskTagType = (riskType) => {
  const typeMap = {
    '财务风险': 'danger',
    '供应链风险': 'warning',
    '质量风险': 'primary',
    '合规风险': 'info'
  }
  return typeMap[riskType] || 'info'
}

const getRiskLevelType = (riskLevel) => {
  const levelMap = {
    '高': 'danger',
    '中': 'warning',
    '低': 'success'
  }
  return levelMap[riskLevel] || 'info'
}

const getInsightIcon = (type) => {
  const iconMap = {
    'cost': 'Money',
    'supplier': 'OfficeBuilding',
    'risk': 'Warning',
    'efficiency': 'Clock'
  }
  return iconMap[type] || 'InfoFilled'
}

const getInsightTagType = (priority) => {
  const priorityMap = {
    'high': 'danger',
    'medium': 'warning',
    'low': 'info'
  }
  return priorityMap[priority] || 'info'
}

// 事件处理
const handleClose = () => {
  dialogVisible.value = false
}

const handleDateChange = () => {
  loadAnalyticsData()
}

const handleTypeChange = () => {
  loadAnalyticsData()
}

const handleSupplierChange = () => {
  loadAnalyticsData()
}

const handleCategoryChange = () => {
  loadAnalyticsData()
}

const handleRefresh = async () => {
  try {
    await loadAnalyticsData()
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败')
  }
}

const handleExport = async () => {
  try {
    await exportAnalyticsData({
      dateRange: dateRange.value,
      analyticsType: analyticsType.value,
      supplierFilter: supplierFilter.value,
      categoryFilter: categoryFilter.value
    })
    ElMessage.success('数据导出成功')
  } catch (error) {
    ElMessage.error('数据导出失败')
  }
}

const handlePrint = () => {
  window.print()
}

const handleInsightAction = (action, insight) => {
  console.log(`执行操作: ${action} for insight: ${insight.title}`)
  ElMessage.info(`执行 ${action}`)
}

const loadAnalyticsData = async () => {
  try {
    // 这里应该调用实际的API
    const params = {
      dateRange: dateRange.value,
      analyticsType: analyticsType.value,
      supplierFilter: supplierFilter.value,
      categoryFilter: categoryFilter.value
    }
    
    // 模拟数据加载
    console.log('加载分析数据:', params)
  } catch (error) {
    console.error('加载分析数据失败:', error)
  }
}

// 初始化
onMounted(() => {
  // 设置默认日期范围（最近30天）
  const endDate = new Date()
  const startDate = new Date()
  startDate.setDate(endDate.getDate() - 30)
  
  dateRange.value = [
    startDate.toISOString().split('T')[0],
    endDate.toISOString().split('T')[0]
  ]
  
  loadAnalyticsData()
})
</script>

<style scoped>
.procurement-analytics-dialog {
  .analytics-filters {
    margin-bottom: 20px;
    padding: 16px;
    background-color: #f5f7fa;
    border-radius: 6px;
  }

  .analytics-content {
    .overview-section {
      margin-bottom: 30px;

      h3 {
        margin-bottom: 20px;
        color: #303133;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .metric-card {
        display: flex;
        align-items: center;
        padding: 20px;
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        transition: transform 0.2s;

        &:hover {
          transform: translateY(-2px);
        }

        .metric-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          color: white;
          font-size: 20px;
        }

        .metric-content {
          flex: 1;

          .metric-value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
            margin-bottom: 4px;
          }

          .metric-label {
            font-size: 14px;
            color: #606266;
            margin-bottom: 4px;
          }

          .metric-trend {
            font-size: 12px;
            display: flex;
            align-items: center;
            gap: 4px;

            &.positive {
              color: #67c23a;
            }

            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }

    .charts-section {
      margin-bottom: 30px;

      .chart-container {
        .chart-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;

          h4 {
            color: #303133;
            font-weight: 600;
          }
        }

        .chart-content {
          .chart-canvas {
            height: 400px;
            background: #f5f7fa;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 2px dashed #dcdfe6;

            .placeholder-chart {
              text-align: center;
              color: #909399;

              .el-icon {
                font-size: 48px;
                margin-bottom: 16px;
              }

              p {
                font-size: 18px;
                font-weight: 600;
                margin-bottom: 8px;
              }

              small {
                font-size: 14px;
              }
            }
          }
        }
      }
    }

    .data-table-section {
      margin-bottom: 30px;

      h3 {
        margin-bottom: 20px;
        color: #303133;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .savings-positive {
        color: #67c23a;
        font-weight: 600;
      }

      .savings-negative {
        color: #f56c6c;
        font-weight: 600;
      }
    }

    .insights-section {
      h3 {
        margin-bottom: 20px;
        color: #303133;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 8px;
      }

      .insights-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
        gap: 20px;

        .insight-card {
          background: white;
          border-radius: 8px;
          padding: 20px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
          transition: transform 0.2s;

          &:hover {
            transform: translateY(-2px);
          }

          .insight-header {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 16px;

            .insight-icon {
              font-size: 24px;
              padding: 8px;
              border-radius: 50%;

              &.cost {
                background-color: #f0f9ff;
                color: #409eff;
              }

              &.supplier {
                background-color: #f0fdf4;
                color: #67c23a;
              }

              &.risk {
                background-color: #fef2f2;
                color: #f56c6c;
              }

              &.efficiency {
                background-color: #fffbeb;
                color: #e6a23c;
              }
            }

            .insight-title {
              font-weight: 600;
              color: #303133;
              flex: 1;
            }
          }

          .insight-content {
            margin-bottom: 16px;

            p {
              margin: 0 0 12px 0;
              color: #606266;
              line-height: 1.6;
            }

            .insight-actions {
              display: flex;
              gap: 8px;
              flex-wrap: wrap;
            }
          }

          .insight-footer {
            display: flex;
            justify-content: space-between;
            font-size: 12px;
            color: #909399;
            padding-top: 12px;
            border-top: 1px solid #f0f0f0;
          }
        }
      }
    }
  }
}

:deep(.el-dialog__body) {
  padding: 20px 30px;
}

:deep(.el-tabs--border-card) {
  border: 1px solid #e4e7ed;
  border-radius: 6px;
}

:deep(.el-progress-bar__outer) {
  border-radius: 4px;
}

:deep(.el-rate) {
  .el-rate__text {
    font-size: 12px;
  }
}
</style>