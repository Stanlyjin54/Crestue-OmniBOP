<template>
  <div class="ai-assistant-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">
            <el-icon><ChatDotRound /></el-icon>
            AI助手管理
          </h1>
          <p class="page-description">管理和监控AI助手的使用情况、性能指标和配置参数</p>
        </div>
        <div class="action-section">
          <el-button
            v-if="hasPermission('ai_assistant:config')"
            type="primary"
            :icon="Setting"
            @click="handleConfig"
          >
            配置设置
          </el-button>
          <el-button
            v-if="hasPermission('ai_assistant:export')"
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
        <el-breadcrumb-item to="/admin">首页</el-breadcrumb-item>
        <el-breadcrumb-item>AI助手管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6" v-for="(stat, key) in statisticsCards" :key="key">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon" :style="{ color: stat.color }">
                <el-icon><component :is="stat.icon" /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stat.value }}</div>
                <div class="stat-label">{{ stat.label }}</div>
                <div class="stat-unit">{{ stat.unit }}</div>
              </div>
            </div>
            <div v-if="stat.trend" class="stat-trend">
              <el-icon :color="stat.trend.color">
                <component :is="stat.trend.icon" />
              </el-icon>
              <span :style="{ color: stat.trend.color }">{{ stat.trend.value }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>使用趋势</span>
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
            <div ref="usageChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <span>功能使用分布</span>
            </template>
            <div ref="featureChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="filter-section">
      <el-card shadow="never">
        <el-form :model="filterForm" :inline="true" class="filter-form">
          <el-form-item label="用户">
            <el-input
              v-model="filterForm.userId"
              placeholder="输入用户ID或用户名"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="功能类型">
            <el-select
              v-model="filterForm.featureType"
              placeholder="选择功能类型"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="(label, value) in featureTypes"
                :key="value"
                :label="label"
                :value="value"
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
              <el-option
                v-for="(label, value) in statusTypes"
                :key="value"
                :label="label"
                :value="value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 320px"
            />
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

    <!-- 日志列表表格 -->
    <div class="table-section">
      <el-card shadow="never">
        <template #header>
          <div class="card-header">
            <span>AI助手日志</span>
            <div class="table-actions">
              <el-button
                v-if="hasPermission('ai_assistant:delete')"
                type="danger"
                size="small"
                :icon="Delete"
                :disabled="!selectedRows.length"
                @click="handleBatchDelete"
              >
                批量删除 ({{ selectedRows.length }})
              </el-button>
            </div>
          </div>
        </template>

        <el-table
          ref="tableRef"
          v-loading="aiAssistant.loading"
          :data="aiAssistant.logs"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
          class="ai-table"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column
            prop="id"
            label="ID"
            width="80"
            sortable="custom"
          />
          <el-table-column
            prop="userId"
            label="用户ID"
            width="120"
            sortable="custom"
          />
          <el-table-column
            prop="userName"
            label="用户名"
            width="150"
            sortable="custom"
          />
          <el-table-column
            prop="featureType"
            label="功能类型"
            width="120"
            sortable="custom"
          >
            <template #default="{ row }">
              <el-tag :type="getFeatureTypeTagType(row.featureType)">
                {{ featureTypes[row.featureType] || row.featureType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="requestContent"
            label="请求内容"
            min-width="200"
          >
            <template #default="{ row }">
              <el-tooltip :content="row.requestContent" placement="top">
                <span class="content-text">
                  {{ row.requestContent ? row.requestContent.substring(0, 50) + '...' : '-' }}
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
            prop="responseContent"
            label="响应内容"
            min-width="200"
          >
            <template #default="{ row }">
              <el-tooltip :content="row.responseContent" placement="top">
                <span class="content-text">
                  {{ row.responseContent ? row.responseContent.substring(0, 50) + '...' : '-' }}
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
            prop="responseTime"
            label="响应时间"
            width="100"
            sortable="custom"
          >
            <template #default="{ row }">
              <span :class="getResponseTimeClass(row.responseTime)">
                {{ row.responseTime }}ms
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="status"
            label="状态"
            width="100"
            sortable="custom"
          >
            <template #default="{ row }">
              <el-tag :type="getStatusTagType(row.status)">
                {{ statusTypes[row.status] || row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="createdAt"
            label="创建时间"
            width="180"
            sortable="custom"
          >
            <template #default="{ row }">
              {{ formatDateTime(row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="180"
            fixed="right"
          >
            <template #default="{ row }">
              <el-button
                type="primary"
                size="small"
                :icon="View"
                @click="handleViewDetail(row)"
              >
                查看
              </el-button>
              <el-button
                v-if="hasPermission('ai_assistant:delete')"
                type="danger"
                size="small"
                :icon="Delete"
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-section">
          <el-pagination
            v-model:current-page="aiAssistant.currentPage"
            v-model:page-size="aiAssistant.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="aiAssistant.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handlePageSizeChange"
            @current-change="handleCurrentPageChange"
          />
        </div>
      </el-card>
    </div>

    <!-- AI助手日志详情对话框 -->
    <AiLogDialog
      v-model="showLogDialog"
      :log="selectedLog"
      @updated="handleLogUpdated"
    />

    <!-- AI助手分析对话框 -->
    <AiAnalysisDialog
      v-model="showAnalysisDialog"
      :data="analysisData"
    />
    
    <AiConfigDialog
      v-model="showConfigDialog"
      :config="configData"
      @updated="handleConfigUpdated"
    />

    <!-- AI助手配置对话框 -->
    <AiConfigDialog
      v-model="showConfigDialog"
      :config="aiAssistant.config"
      @updated="handleConfigUpdated"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ChatDotRound, Setting, Download, Search, Refresh, Delete, View,
  TrendCharts, Timer, User, UserFilled, Connection, SuccessFilled,
  ArrowUp, ArrowDown, OfficeBuilding, Files
} from '@element-plus/icons-vue'
import { useAiAssistantLogs, useAiAssistantStatistics, aiAssistantStatisticsConfig } from '@/composables/useAiAssistant'
import AiLogDialog from './components/AiLogDialog.vue'
import AiAnalysisDialog from './components/AiAnalysisDialog.vue'
import AiConfigDialog from './components/AiConfigDialog.vue'

// 权限检查
const hasPermission = (permission) => {
  // 这里应该从权限管理中获取当前用户的权限
  return true // 临时返回true，实际应该检查权限
}

// AI助手状态
const aiAssistant = useAiAssistantLogs()
const aiAssistantStats = useAiAssistantStatistics()

// 响应式数据
const tableRef = ref()
const usageChartRef = ref()
const featureChartRef = ref()

// 显示状态
const showLogDialog = ref(false)
const showAnalysisDialog = ref(false)
const showConfigDialog = ref(false)

// 选中数据
const selectedLog = ref(null)
const selectedRows = ref([])
const analysisData = ref({})
const showConfigDialog = ref(false)
const configData = ref({})

// 筛选表单
const filterForm = reactive({
  userId: '',
  featureType: '',
  status: '',
  dateRange: []
})

// 时间周期
const timePeriods = [
  { label: '24小时', value: '24h' },
  { label: '7天', value: '7d' },
  { label: '30天', value: '30d' }
]
const selectedPeriod = ref('24h')

// 功能类型
const featureTypes = {
  CHAT: '聊天对话',
  TEXT_GENERATION: '文本生成',
  TRANSLATION: '翻译',
  SUMMARIZATION: '总结摘要',
  ANALYSIS: '数据分析',
  RECOMMENDATION: '智能推荐',
  OCR: '文字识别',
  SENTIMENT: '情感分析'
}

// 状态类型
const statusTypes = {
  SUCCESS: '成功',
  FAILED: '失败',
  TIMEOUT: '超时',
  CANCELLED: '已取消'
}

// 统计卡片配置
const statisticsCards = computed(() => {
  const stats = aiAssistantStats.statistics.value
  return [
    {
      ...aiAssistantStatisticsConfig.totalRequests,
      value: stats.totalRequests,
      trend: {
        icon: ArrowUp,
        color: '#67C23A',
        value: '+12.5%'
      }
    },
    {
      ...aiAssistantStatisticsConfig.successRate,
      value: stats.successRate,
      trend: {
        icon: ArrowUp,
        color: '#67C23A',
        value: '+2.1%'
      }
    },
    {
      ...aiAssistantStatisticsConfig.avgResponseTime,
      value: stats.avgResponseTime,
      trend: {
        icon: ArrowDown,
        color: '#E6A23C',
        value: '-5.2%'
      }
    },
    {
      ...aiAssistantStatisticsConfig.totalUsers,
      value: stats.totalUsers,
      trend: {
        icon: ArrowUp,
        color: '#67C23A',
        value: '+8.3%'
      }
    }
  ]
})

// 方法
const handleSearch = () => {
  const params = {
    ...filterForm,
    startDate: filterForm.dateRange?.[0],
    endDate: filterForm.dateRange?.[1]
  }
  aiAssistant.fetchLogs(params)
}

const handleReset = () => {
  Object.assign(filterForm, {
    userId: '',
    featureType: '',
    status: '',
    dateRange: []
  })
  aiAssistant.fetchLogs()
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSortChange = ({ prop, order }) => {
  const params = {
    sortBy: prop,
    sortOrder: order === 'ascending' ? 'asc' : 'desc'
  }
  aiAssistant.fetchLogs(params)
}

const handlePageSizeChange = (size) => {
  aiAssistant.setPageSize(size)
  aiAssistant.fetchLogs()
}

const handleCurrentPageChange = (page) => {
  aiAssistant.setCurrentPage(page)
  aiAssistant.fetchLogs()
}

const handleViewDetail = (log) => {
  selectedLog.value = log
  showLogDialog.value = true
}

const handleDelete = async (log) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除ID为 ${log.id} 的AI助手日志吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    await aiAssistant.deleteLog(log.id)
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  if (!selectedRows.value.length) return
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 条AI助手日志吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const ids = selectedRows.value.map(row => row.id)
    await aiAssistant.batchDeleteLogs(ids)
    ElMessage.success('批量删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

const handleExport = async () => {
  try {
    await aiAssistant.exportLogs(filterForm)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const handleLogUpdated = () => {
  aiAssistant.fetchLogs()
}

const handleConfigUpdated = () => {
  // 重新获取统计数据
  aiAssistantStats.fetchStatistics()
}

const handleConfig = () => {
  configData.value = {
    model: 'gpt-3.5-turbo',
    apiVersion: 'v1',
    temperature: 0.7,
    maxTokens: 1000,
    timeout: 30,
    maxConcurrent: 10,
    enabledFeatures: ['CHAT', 'TEXT_GENERATION', 'TRANSLATION'],
    textGenerationLength: 500,
    contextWindow: '4096',
    procurementAlgorithm: 'cost_based',
    priceSensitivity: 50,
    qualityWeight: 80,
    supplierFilters: ['certified', 'high_rating'],
    analysisDimensions: ['performance', 'cost', 'quality'],
    analysisPeriod: 'weekly',
    reportTemplate: 'standard',
    marketingPlatforms: ['wechat', 'weibo'],
    contentStrategy: 'educational',
    publishFrequency: 3,
    engagementThreshold: 20,
    paymentScenarios: ['b2c', 'b2b'],
    successRateWeight: 70,
    costWeight: 30,
    paymentChannelPriority: ['alipay', 'wechat_pay'],
    monitoringMetrics: ['response_time', 'success_rate', 'error_rate'],
    monitoringFrequency: '5min',
    alertThreshold: 90,
    alertNotifications: ['email', 'webhook'],
    logLevel: 'info',
    cacheStrategy: 'redis',
    enableLoadBalancing: true,
    enableEncryption: true,
    enableAuditLog: true,
    enableRateLimit: true
  }
  showConfigDialog.value = true
}

// 工具方法
const getFeatureTypeTagType = (type) => {
  const typeMap = {
    CHAT: '',
    TEXT_GENERATION: 'primary',
    TRANSLATION: 'success',
    SUMMARIZATION: 'warning',
    ANALYSIS: 'info',
    RECOMMENDATION: 'danger',
    OCR: 'primary',
    SENTIMENT: 'success'
  }
  return typeMap[type] || ''
}

const getStatusTagType = (status) => {
  const statusMap = {
    SUCCESS: 'success',
    FAILED: 'danger',
    TIMEOUT: 'warning',
    CANCELLED: 'info'
  }
  return statusMap[status] || ''
}

const getResponseTimeClass = (time) => {
  if (time < 1000) return 'response-time-fast'
  if (time < 3000) return 'response-time-normal'
  return 'response-time-slow'
}

const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return new Date(datetime).toLocaleString('zh-CN')
}

// 监听时间周期变化
watch(selectedPeriod, () => {
  // 重新加载图表数据
  loadChartData()
})

// 加载图表数据
const loadChartData = async () => {
  try {
    // 加载使用趋势数据
    const trendData = await aiAssistantStats.fetchUsageTrend({
      period: selectedPeriod.value
    })
    
    // 加载功能分布数据
    const featureData = await aiAssistantStats.fetchPopularFeatures({
      period: selectedPeriod.value
    })
    
    // 渲染图表（这里需要根据实际图表库来实现）
    await nextTick()
    renderCharts(trendData, featureData)
  } catch (error) {
    console.error('加载图表数据失败:', error)
  }
}

// 渲染图表（示例实现）
const renderCharts = (trendData, featureData) => {
  // 这里应该使用实际的图表库（如ECharts、Chart.js等）
  console.log('渲染图表数据:', { trendData, featureData })
}

// 生命周期
onMounted(async () => {
  try {
    // 加载数据
    await Promise.all([
      aiAssistant.fetchLogs(),
      aiAssistantStats.fetchStatistics(),
      loadChartData()
    ])
  } catch (error) {
    console.error('初始化失败:', error)
  }
})
</script>

<style scoped>
.ai-assistant-management {
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

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 2px;
}

.stat-unit {
  font-size: 12px;
  color: #909399;
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

.filter-section {
  margin-bottom: 20px;
}

.table-section {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-actions {
  display: flex;
  gap: 8px;
}

.ai-table {
  width: 100%;
}

.content-text {
  cursor: pointer;
  color: #409EFF;
}

.response-time-fast {
  color: #67C23A;
  font-weight: 500;
}

.response-time-normal {
  color: #E6A23C;
  font-weight: 500;
}

.response-time-slow {
  color: #F56C6C;
  font-weight: 500;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 16px;
  }
  
  .action-section {
    width: 100%;
  }
  
  .action-section .el-button {
    flex: 1;
  }
  
  .statistics-section .el-col {
    margin-bottom: 16px;
  }
  
  .charts-section .el-col {
    margin-bottom: 16px;
  }
}
</style>