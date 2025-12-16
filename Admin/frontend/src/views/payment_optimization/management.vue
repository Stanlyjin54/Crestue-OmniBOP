<template>
  <div class="payment-optimization">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">
            <el-icon><Money /></el-icon>
            支付系统优化
          </h1>
          <p class="page-description">
            管理和优化B2B/B2C支付场景，提升支付成功率和用户体验
          </p>
        </div>
        <div class="action-section">
          <el-button
            v-if="hasPermission('payment:create')"
            type="primary"
            :icon="Plus"
            @click="showCreateDialog = true"
          >
            创建场景
          </el-button>
          <el-button
            v-if="hasPermission('payment:analytics')"
            type="default"
            :icon="DataAnalysis"
            @click="showAnalyticsDialog = true"
          >
            数据分析
          </el-button>
          <el-button
            v-if="hasPermission('payment:optimize')"
            type="default"
            :icon="Setting"
            @click="showOptimizationDialog = true"
          >
            智能优化
          </el-button>
          <el-button
            v-if="hasPermission('payment:export')"
            type="default"
            :icon="Download"
            @click="handleExport"
          >
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <div class="breadcrumb-section">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>支付优化</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(card, key) in statisticsCards" :key="key">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon" :style="{ backgroundColor: card.color + '20', color: card.color }">
                <el-icon><component :is="card.icon" /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">
                  {{ card.value }}
                  <span class="stat-unit">{{ card.unit }}</span>
                </div>
                <div class="stat-label">{{ card.label }}</div>
                <div class="stat-trend" :style="{ color: card.trend.color }">
                  <el-icon><component :is="card.trend.icon" /></el-icon>
                  <span>{{ card.trend.value }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="chart-card" shadow="never">
            <template #header>
              <div class="card-header">
                <span>支付场景分布</span>
                <el-select v-model="selectedPeriod" size="small" style="width: 120px">
                  <el-option
                    v-for="period in timePeriods"
                    :key="period.value"
                    :label="period.label"
                    :value="period.value"
                  />
                </el-select>
              </div>
            </template>
            <div class="chart-container">
              <!-- 图表占位符 -->
              <div class="chart-placeholder">
                <el-icon><PieChart /></el-icon>
                <span>支付场景分布图表</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card" shadow="never">
            <template #header>
              <div class="card-header">
                <span>转化率趋势</span>
                <el-select v-model="selectedPeriod" size="small" style="width: 120px">
                  <el-option
                    v-for="period in timePeriods"
                    :key="period.value"
                    :label="period.label"
                    :value="period.value"
                  />
                </el-select>
              </div>
            </template>
            <div class="chart-container">
              <!-- 图表占位符 -->
              <div class="chart-placeholder">
                <el-icon><TrendCharts /></el-icon>
                <span>转化率趋势图表</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 筛选区域 -->
    <div class="filter-section">
      <el-card shadow="never">
        <el-form :model="filterForm" :inline="true" class="filter-form">
          <el-form-item label="场景类型">
            <el-select
              v-model="filterForm.scenarioType"
              placeholder="选择类型"
              clearable
              style="width: 140px"
            >
              <el-option
                v-for="type in paymentOptimization.scenarioConfig.scenarioTypes"
                :key="type.value"
                :label="type.label"
                :value="type.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="支付方式">
            <el-select
              v-model="filterForm.paymentMethod"
              placeholder="选择方式"
              clearable
              style="width: 140px"
            >
              <el-option
                v-for="method in paymentOptimization.scenarioConfig.paymentMethods"
                :key="method.id"
                :label="method.name"
                :value="method.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="业务类型">
            <el-select
              v-model="filterForm.businessType"
              placeholder="选择业务"
              clearable
              style="width: 140px"
            >
              <el-option
                v-for="business in paymentOptimization.scenarioConfig.businessTypes"
                :key="business.value"
                :label="business.label"
                :value="business.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="filterForm.status"
              placeholder="选择状态"
              clearable
              style="width: 120px"
            >
              <el-option label="草稿" value="draft" />
              <el-option label="活跃" value="active" />
              <el-option label="优化中" value="optimizing" />
              <el-option label="已优化" value="optimized" />
              <el-option label="已暂停" value="suspended" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">
              搜索
            </el-button>
            <el-button :icon="Refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 场景列表 -->
    <div class="table-section">
      <el-card shadow="never">
        <template #header>
          <div class="table-header">
            <span>支付场景列表 ({{ paymentOptimization.filteredScenarios.length }})</span>
            <div class="table-actions">
              <el-button
                v-if="hasPermission('payment:batch')"
                type="danger"
                :icon="Delete"
                :disabled="!selectedRows.length"
                @click="handleBatchDelete"
              >
                批量删除
              </el-button>
            </div>
          </div>
        </template>

        <el-table
          ref="tableRef"
          v-loading="paymentOptimization.loading"
          :data="paymentOptimization.filteredScenarios"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
          class="payment-table"
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column prop="name" label="场景名称" min-width="200" sortable="custom">
            <template #default="{ row }">
              <div class="scenario-info">
                <div class="scenario-name">{{ row.name }}</div>
                <div class="scenario-description">{{ row.description }}</div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="type" label="场景类型" width="120" sortable="custom">
            <template #default="{ row }">
              <el-tag
                :style="{ 
                  backgroundColor: paymentOptimization.getScenarioTypeColor(row.type) + '20',
                  borderColor: paymentOptimization.getScenarioTypeColor(row.type),
                  color: paymentOptimization.getScenarioTypeColor(row.type)
                }"
              >
                {{ paymentOptimization.getScenarioTypeLabel(row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="businessType" label="业务类型" width="120">
            <template #default="{ row }">
              {{ getBusinessTypeLabel(row.businessType) }}
            </template>
          </el-table-column>
          
          <el-table-column prop="supportedMethods" label="支付方式" width="200">
            <template #default="{ row }">
              <div class="method-tags">
                <el-tag
                  v-for="method in row.supportedMethods.slice(0, 2)"
                  :key="method"
                  size="small"
                  style="margin-right: 4px"
                >
                  {{ getPaymentMethodName(method) }}
                </el-tag>
                <el-dropdown v-if="row.supportedMethods.length > 2">
                  <span class="more-methods">
                    +{{ row.supportedMethods.length - 2 }}
                    <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item
                        v-for="method in row.supportedMethods.slice(2)"
                        :key="method"
                      >
                        {{ getPaymentMethodName(method) }}
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="conversionRate" label="转化率" width="100" sortable="custom">
            <template #default="{ row }">
              <span class="conversion-rate">
                {{ row.conversionRate.toFixed(1) }}%
              </span>
            </template>
          </el-table-column>
          
          <el-table-column prop="averageAmount" label="平均金额" width="120" sortable="custom">
            <template #default="{ row }">
              {{ paymentOptimization.formatAmount(row.averageAmount) }}
            </template>
          </el-table-column>
          
          <el-table-column prop="transactionVolume" label="交易量" width="100" sortable="custom">
            <template #default="{ row }">
              {{ paymentOptimization.formatNumber(row.transactionVolume) }}
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="状态" width="100" sortable="custom">
            <template #default="{ row }">
              <el-tag :type="paymentOptimization.getStatusType(row.status)">
                {{ paymentOptimization.getStatusLabel(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button size="small" :icon="View" @click="handleViewDetail(row)">
                查看
              </el-button>
              <el-button size="small" :icon="Edit" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-dropdown @command="(command) => handleCommand(command, row)">
                <el-button size="small">
                  更多
                  <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="analytics">
                      <el-icon><DataAnalysis /></el-icon>
                      数据分析
                    </el-dropdown-item>
                    <el-dropdown-item command="optimize">
                      <el-icon><Setting /></el-icon>
                      智能优化
                    </el-dropdown-item>
                    <el-dropdown-item command="duplicate" divided>
                      <el-icon><CopyDocument /></el-icon>
                      复制场景
                    </el-dropdown-item>
                    <el-dropdown-item command="delete" divided>
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-section">
          <el-pagination
            v-model:current-page="paymentOptimization.pagination.currentPage"
            v-model:page-size="paymentOptimization.pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="paymentOptimization.pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handlePageSizeChange"
            @current-page-change="handleCurrentPageChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 创建/编辑场景对话框 -->
    <ScenarioFormDialog
      v-model="showCreateDialog"
      :scenario="selectedScenario"
      @updated="handleScenarioUpdated"
    />

    <!-- 数据分析对话框 -->
    <AnalyticsDialog
      v-model="showAnalyticsDialog"
      :data="analyticsData"
    />

    <!-- 智能优化对话框 -->
    <OptimizationDialog
      v-model="showOptimizationDialog"
      :scenario="selectedScenario"
      @optimized="handleScenarioOptimized"
    />

    <!-- 场景详情对话框 -->
    <ScenarioDetailDialog
      v-model="showDetailDialog"
      :scenario="selectedScenario"
      @updated="handleScenarioUpdated"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Money, Plus, DataAnalysis, Setting, Download, Search, Refresh, Delete,
  View, Edit, ArrowDown, CopyDocument, PieChart, TrendCharts
} from '@element-plus/icons-vue'
import { usePaymentOptimization } from '@/composables/usePaymentOptimization'
import ScenarioFormDialog from './components/ScenarioFormDialog.vue'
import AnalyticsDialog from './components/AnalyticsDialog.vue'
import OptimizationDialog from './components/OptimizationDialog.vue'
import ScenarioDetailDialog from './components/ScenarioDetailDialog.vue'
import ConfigurationOptimizationDialog from './components/ConfigurationOptimizationDialog.vue'

// 组合式函数
const paymentOptimization = usePaymentOptimization()

// 响应式数据
const tableRef = ref()
const selectedRows = ref([])
const selectedScenario = ref(null)
const showCreateDialog = ref(false)
const showAnalyticsDialog = ref(false)
const showOptimizationDialog = ref(false)
const showDetailDialog = ref(false)
const analyticsData = ref({})

// 筛选表单
const filterForm = ref({
  scenarioType: '',
  paymentMethod: '',
  businessType: '',
  status: '',
  dateRange: []
})

// 时间周期选项
const timePeriods = [
  { label: '7天', value: '7d' },
  { label: '30天', value: '30d' },
  { label: '90天', value: '90d' }
]

const selectedPeriod = ref('30d')

// 统计卡片配置
const statisticsCards = computed(() => {
  const analytics = paymentOptimization.paymentAnalytics.overview || {}
  return {
    totalScenarios: {
      label: '总场景数',
      value: analytics.totalScenarios || 0,
      icon: 'Money',
      color: '#409EFF',
      unit: '个',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+8.5%'
      }
    },
    activeScenarios: {
      label: '活跃场景',
      value: analytics.activeScenarios || 0,
      icon: 'VideoPlay',
      color: '#67C23A',
      unit: '个',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+12.3%'
      }
    },
    totalTransactions: {
      label: '总交易额',
      value: paymentOptimization.formatAmount(analytics.totalTransactions || 0),
      icon: 'View',
      color: '#E6A23C',
      unit: '',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+18.7%'
      }
    },
    conversionRate: {
      label: '平均转化率',
      value: (analytics.averageConversionRate || 0).toFixed(1),
      icon: 'Percentage',
      color: '#F56C6C',
      unit: '%',
      trend: {
        icon: 'ArrowUp',
        color: '#67C23A',
        value: '+2.8%'
      }
    }
  }
})

// 权限检查函数
const hasPermission = (permission) => {
  // 模拟权限检查
  return true
}

// 工具方法
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

const getPaymentMethodName = (methodId) => {
  const method = paymentOptimization.scenarioConfig.paymentMethods.find(m => m.id === methodId)
  return method ? method.name : methodId
}

// 方法
const handleSearch = () => {
  Object.assign(paymentOptimization.filterParams, filterForm.value)
  paymentOptimization.fetchPaymentScenarios()
}

const handleReset = () => {
  Object.assign(filterForm.value, {
    scenarioType: '',
    paymentMethod: '',
    businessType: '',
    status: '',
    dateRange: []
  })
  Object.assign(paymentOptimization.filterParams, filterForm.value)
  paymentOptimization.fetchPaymentScenarios()
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSortChange = ({ prop, order }) => {
  const params = {
    sortBy: prop,
    sortOrder: order === 'ascending' ? 'asc' : 'desc'
  }
  paymentOptimization.fetchPaymentScenarios(params)
}

const handlePageSizeChange = (size) => {
  paymentOptimization.pagination.pageSize = size
  paymentOptimization.fetchPaymentScenarios()
}

const handleCurrentPageChange = (page) => {
  paymentOptimization.pagination.currentPage = page
  paymentOptimization.fetchPaymentScenarios()
}

const handleViewDetail = (scenario) => {
  selectedScenario.value = scenario
  showDetailDialog.value = true
}

const handleEdit = (scenario) => {
  selectedScenario.value = { ...scenario }
  showCreateDialog.value = true
}

const handleCommand = async (command, scenario) => {
  switch (command) {
    case 'analytics':
      analyticsData.value = scenario
      showAnalyticsDialog.value = true
      break
    case 'optimize':
      selectedScenario.value = scenario
      showOptimizationDialog.value = true
      break
    case 'duplicate':
      // 复制场景逻辑
      const duplicatedScenario = {
        ...scenario,
        id: 'scenario_' + Date.now(),
        name: `${scenario.name} (副本)`,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
      await paymentOptimization.createPaymentScenario(duplicatedScenario)
      break
    case 'delete':
      try {
        await ElMessageBox.confirm(
          `确定要删除场景"${scenario.name}"吗？`,
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        await paymentOptimization.deletePaymentScenario(scenario.id)
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
      break
  }
}

const handleBatchDelete = async () => {
  if (!selectedRows.value.length) return
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个场景吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const ids = selectedRows.value.map(row => row.id)
    // 批量删除逻辑
    for (const id of ids) {
      await paymentOptimization.deletePaymentScenario(id)
    }
    selectedRows.value = []
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

const handleExport = async () => {
  try {
    await paymentOptimization.exportPaymentData(filterForm.value)
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const handleScenarioUpdated = () => {
  paymentOptimization.fetchPaymentScenarios()
}

const handleScenarioOptimized = () => {
  paymentOptimization.fetchPaymentScenarios()
  paymentOptimization.fetchPaymentAnalytics()
}

// 加载图表数据
const loadChartData = async () => {
  try {
    await paymentOptimization.fetchPaymentAnalytics({
      period: selectedPeriod.value
    })
    await nextTick()
    renderCharts()
  } catch (error) {
    console.error('加载图表数据失败:', error)
  }
}

// 渲染图表（示例实现）
const renderCharts = () => {
  console.log('渲染图表数据:', paymentOptimization.paymentAnalytics)
}

// 生命周期
onMounted(async () => {
  try {
    await Promise.all([
      paymentOptimization.fetchPaymentScenarios(),
      paymentOptimization.fetchPaymentAnalytics()
    ])
  } catch (error) {
    console.error('初始化失败:', error)
  }
})
</script>

<style scoped>
.payment-optimization {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.title-section {
  flex: 1;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.page-description {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.action-section {
  display: flex;
  gap: 12px;
}

.breadcrumb-section {
  margin-bottom: 20px;
}

.statistics-section {
  margin-bottom: 20px;
}

.stat-card {
  border: none;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: #f5f5f5;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-unit {
  font-size: 12px;
  color: #909399;
  font-weight: normal;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 2px;
}

.stat-trend {
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

.chart-container {
  height: 300px;
  width: 100%;
}

.chart-placeholder {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 14px;
}

.chart-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
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

.payment-table {
  width: 100%;
}

.scenario-info {
  min-width: 0;
}

.scenario-name {
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.scenario-description {
  font-size: 12px;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.method-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.more-methods {
  font-size: 12px;
  color: #909399;
  cursor: pointer;
}

.conversion-rate {
  font-weight: 500;
  color: #67C23A;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>