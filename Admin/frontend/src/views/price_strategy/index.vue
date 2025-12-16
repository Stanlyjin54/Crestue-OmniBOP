<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">价格策略管理</h1>
          <p class="page-subtitle">管理动态定价策略，支持B2C零售和B2B批发价格规则</p>
        </div>
        <div class="header-image">
          <img src="@/assets/logo/price.png" alt="价格策略" />
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>价格策略管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon primary">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-title">活跃策略</div>
              <div class="stats-value">{{ stats.activeStrategies }}</div>
              <div class="stats-trend">
                正在生效的价格策略
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon success">
              <el-icon><ShoppingCart /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-title">今日计算</div>
              <div class="stats-value">{{ stats.todayCalculations }}</div>
              <div class="stats-trend">
                价格计算次数
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon warning">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-title">平均折扣</div>
              <div class="stats-value">{{ stats.averageDiscount }}%</div>
              <div class="stats-trend">
                当前折扣水平
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon info">
              <el-icon><DataAnalysis /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-title">策略类型</div>
              <div class="stats-value">{{ stats.strategyTypes }}</div>
              <div class="stats-trend">
                可用策略类型
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索和筛选区域 -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="策略名称">
          <el-input
            v-model="searchForm.strategyName"
            placeholder="请输入策略名称"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="策略类型">
          <el-select v-model="searchForm.strategyType" placeholder="请选择策略类型" clearable>
            <el-option
              v-for="type in STRATEGY_TYPES"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="渠道类型">
          <el-select v-model="searchForm.channelType" placeholder="请选择渠道类型" clearable>
            <el-option label="B2C零售" value="B2C" />
            <el-option label="B2B批发" value="B2B" />
            <el-option label="全渠道" value="ALL" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="激活" value="ACTIVE" />
            <el-option label="停用" value="INACTIVE" />
            <el-option label="已过期" value="EXPIRED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <div class="action-bar">
      <div class="action-left">
        <el-button type="primary" @click="handleCreate">
          <el-icon><Plus /></el-icon>
          新建策略
        </el-button>
        <el-button @click="handleBatchToggle">
          <el-icon><Switch /></el-icon>
          批量切换状态
        </el-button>
        <el-button @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>
      <div class="action-right">
        <el-button @click="handleTestCalculation">
          <el-icon><Calculator /></el-icon>
          价格计算测试
        </el-button>
        <el-button @click="handleStrategyAnalysis">
          <el-icon><DataAnalysis /></el-icon>
          策略分析
        </el-button>
      </div>
    </div>

    <!-- 策略列表 -->
    <el-card class="table-card">
      <el-table
        :data="strategyList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        :loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="strategyName" label="策略名称" width="200">
          <template #default="{ row }">
            <div class="strategy-name">
              <strong>{{ row.strategyName }}</strong>
              <el-tag :type="getStrategyTypeTagType(row.strategyType)" size="small" class="ml-2">
                {{ getStrategyTypeLabel(row.strategyType) }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="channelType" label="适用渠道" width="120">
          <template #default="{ row }">
            <el-tag :type="getChannelTypeTagType(row.channelType)" size="small">
              {{ getChannelTypeLabel(row.channelType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userType" label="用户类型" width="120">
          <template #default="{ row }">
            {{ getUserTypeLabel(row.userType) }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" />
        <el-table-column prop="minQuantity" label="最小数量" width="100">
          <template #default="{ row }">
            {{ row.minQuantity || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="discountRate" label="折扣率" width="100">
          <template #default="{ row }">
            <span v-if="row.discountRate">
              {{ (row.discountRate * 100).toFixed(1) }}折
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button type="success" link @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="warning" link @click="handleToggle(row)">
              <el-icon><Switch /></el-icon>
              {{ row.status === 'ACTIVE' ? '停用' : '启用' }}
            </el-button>
            <el-dropdown @command="(command) => handleMoreAction(command, row)">
              <el-button type="info" link>
                <el-icon><MoreFilled /></el-icon>
                更多
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="clone">
                    <el-icon><CopyDocument /></el-icon>
                    复制策略
                  </el-dropdown-item>
                  <el-dropdown-item command="calculate">
                    <el-icon><Calculator /></el-icon>
                    价格测试
                  </el-dropdown-item>
                  <el-dropdown-item command="analytics">
                    <el-icon><DataAnalysis /></el-icon>
                    效果分析
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
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 策略表单对话框 -->
    <PriceStrategyFormDialog
      v-model="dialogVisible"
      :strategy="currentStrategy"
      :mode="dialogMode"
      @submit="handleSubmit"
    />

    <!-- 价格计算测试对话框 -->
    <PriceCalculationDialog
      v-model="calculationDialogVisible"
      :strategy-list="strategyList"
    />

    <!-- 策略分析对话框 -->
    <StrategyAnalysisDialog
      v-model="analysisDialogVisible"
      :strategy="currentStrategy"
    />
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, 
  Refresh, 
  Plus, 
  Download, 
  Calculator, 
  DataAnalysis,
  Switch,
  View,
  Edit,
  MoreFilled,
  CopyDocument,
  Delete,
  Money,
  ShoppingCart,
  TrendCharts
} from '@element-plus/icons-vue'
import PriceStrategyFormDialog from './components/PriceStrategyFormDialog.vue'
import PriceCalculationDialog from './components/PriceCalculationDialog.vue'
import StrategyAnalysisDialog from './components/StrategyAnalysisDialog.vue'

// 响应式数据
const loading = ref(false)
const strategyList = ref([])
const selectedRows = ref([])
const currentStrategy = ref(null)

// 分页配置
const pagination = reactive({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 搜索表单
const searchForm = reactive({
  strategyName: '',
  strategyType: '',
  channelType: '',
  status: ''
})

// 统计数据
const stats = reactive({
  activeStrategies: 0,
  todayCalculations: 0,
  averageDiscount: 0,
  strategyTypes: 0
})

// 对话框状态
const dialogVisible = ref(false)
const dialogMode = ref('create')
const calculationDialogVisible = ref(false)
const analysisDialogVisible = ref(false)

// 策略类型配置
const STRATEGY_TYPES = [
  { value: 'DISCOUNT', label: '折扣策略' },
  { value: 'FIXED_AMOUNT', label: '固定金额' },
  { value: 'TIERED', label: '阶梯价格' },
  { value: 'BUY_X_GET_Y', label: '买X送Y' },
  { value: 'BUNDLE', label: '捆绑销售' }
]

// 方法
const fetchStrategyList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据
    strategyList.value = generateMockStrategies()
    pagination.total = strategyList.value.length
    
    // 更新统计数据
    updateStats()
  } catch (error) {
    ElMessage.error('获取策略列表失败')
  } finally {
    loading.value = false
  }
}

const generateMockStrategies = () => {
  return [
    {
      id: 1,
      strategyName: '新用户专享折扣',
      strategyType: 'DISCOUNT',
      channelType: 'B2C',
      userType: 'NEW_USER',
      priority: 1,
      minQuantity: 1,
      maxQuantity: null,
      discountRate: 0.9,
      fixedAmount: null,
      status: 'ACTIVE',
      startTime: '2024-01-01 00:00:00',
      endTime: '2024-12-31 23:59:59',
      createTime: '2024-01-01 10:00:00',
      updateTime: '2024-01-01 10:00:00'
    },
    {
      id: 2,
      strategyName: '批发阶梯价格',
      strategyType: 'TIERED',
      channelType: 'B2B',
      userType: 'ENTERPRISE',
      priority: 2,
      minQuantity: 100,
      maxQuantity: 500,
      discountRate: 0.85,
      fixedAmount: null,
      status: 'ACTIVE',
      startTime: '2024-01-01 00:00:00',
      endTime: '2024-12-31 23:59:59',
      createTime: '2024-01-01 10:00:00',
      updateTime: '2024-01-01 10:00:00'
    }
  ]
}

const updateStats = () => {
  stats.activeStrategies = strategyList.value.filter(s => s.status === 'ACTIVE').length
  stats.todayCalculations = Math.floor(Math.random() * 1000) + 500
  stats.averageDiscount = Math.round(Math.random() * 20 + 5)
  stats.strategyTypes = STRATEGY_TYPES.length
}

const handleSearch = () => {
  pagination.currentPage = 1
  fetchStrategyList()
}

const handleReset = () => {
  Object.assign(searchForm, {
    strategyName: '',
    strategyType: '',
    channelType: '',
    status: ''
  })
  handleSearch()
}

const handleCreate = () => {
  dialogMode.value = 'create'
  currentStrategy.value = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogMode.value = 'edit'
  currentStrategy.value = { ...row }
  dialogVisible.value = true
}

const handleView = (row) => {
  dialogMode.value = 'view'
  currentStrategy.value = { ...row }
  dialogVisible.value = true
}

const handleToggle = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要${row.status === 'ACTIVE' ? '停用' : '启用'}策略"${row.strategyName}"吗？`,
      '状态切换',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟API调用
    row.status = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
    ElMessage.success(`策略${row.status === 'ACTIVE' ? '启用' : '停用'}成功`)
    
    updateStats()
  } catch {
    // 用户取消操作
  }
}

const handleBatchToggle = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要操作的策略')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要批量切换选中的${selectedRows.value.length}个策略的状态吗？`,
      '批量操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟API调用
    selectedRows.value.forEach(row => {
      row.status = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
    })
    
    ElMessage.success('批量操作完成')
    updateStats()
  } catch {
    // 用户取消操作
  }
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchStrategyList()
}

const handleCurrentChange = (page) => {
  pagination.currentPage = page
  fetchStrategyList()
}

const handleMoreAction = (command, row) => {
  switch (command) {
    case 'clone':
      handleClone(row)
      break
    case 'calculate':
      handleCalculate(row)
      break
    case 'analytics':
      handleAnalytics(row)
      break
    case 'delete':
      handleDelete(row)
      break
  }
}

const handleClone = (row) => {
  dialogMode.value = 'clone'
  currentStrategy.value = { ...row, id: null, strategyName: `${row.strategyName}_副本` }
  dialogVisible.value = true
}

const handleCalculate = (row) => {
  currentStrategy.value = row
  calculationDialogVisible.value = true
}

const handleAnalytics = (row) => {
  currentStrategy.value = row
  analysisDialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除策略"${row.strategyName}"吗？此操作不可恢复！`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error'
      }
    )
    
    // 模拟API调用
    const index = strategyList.value.findIndex(s => s.id === row.id)
    if (index !== -1) {
      strategyList.value.splice(index, 1)
      pagination.total--
      ElMessage.success('删除成功')
      updateStats()
    }
  } catch {
    // 用户取消操作
  }
}

const handleSubmit = () => {
  dialogVisible.value = false
  fetchStrategyList()
}

const handleTestCalculation = () => {
  calculationDialogVisible.value = true
}

const handleStrategyAnalysis = () => {
  analysisDialogVisible.value = true
}

const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 工具函数
const getStrategyTypeLabel = (type) => {
  const strategyType = STRATEGY_TYPES.find(s => s.value === type)
  return strategyType ? strategyType.label : type
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

const getChannelTypeTagType = (type) => {
  const typeMap = {
    'B2C': 'success',
    'B2B': 'warning',
    'ALL': 'info'
  }
  return typeMap[type] || 'info'
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

const getStatusLabel = (status) => {
  const statusMap = {
    'ACTIVE': '激活',
    'INACTIVE': '停用',
    'EXPIRED': '已过期'
  }
  return statusMap[status] || status
}

const getStatusTagType = (status) => {
  const typeMap = {
    'ACTIVE': 'success',
    'INACTIVE': 'info',
    'EXPIRED': 'danger'
  }
  return typeMap[status] || 'info'
}

const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return new Date(datetime).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  fetchStrategyList()
})
</script>

<style scoped>
.page-header {
  @apply bg-white p-6 rounded-lg shadow-sm mb-6;
}

.header-content {
  @apply flex items-center justify-between;
}

.title-section h1 {
  @apply text-2xl font-bold text-gray-800 mb-2;
}

.title-section p {
  @apply text-gray-600;
}

.header-image img {
  @apply w-16 h-16;
}

.breadcrumb {
  @apply mb-6;
}

.stats-row {
  @apply mb-6;
}

.stats-card {
  @apply h-full;
}

.stats-content {
  @apply flex items-center;
}

.stats-icon {
  @apply w-12 h-12 rounded-lg flex items-center justify-center text-white text-xl mr-4;
}

.stats-icon.primary {
  @apply bg-blue-500;
}

.stats-icon.success {
  @apply bg-green-500;
}

.stats-icon.warning {
  @apply bg-yellow-500;
}

.stats-icon.info {
  @apply bg-purple-500;
}

.stats-title {
  @apply text-sm text-gray-600 mb-1;
}

.stats-value {
  @apply text-2xl font-bold text-gray-800 mb-1;
}

.stats-trend {
  @apply text-xs text-gray-500;
}

.search-card {
  @apply mb-6;
}

.search-form {
  @apply flex flex-wrap gap-4;
}

.action-bar {
  @apply flex items-center justify-between mb-6;
}

.action-left,
.action-right {
  @apply flex gap-2;
}

.table-card {
  @apply mb-6;
}

.strategy-name {
  @apply flex items-center;
}

.ml-2 {
  @apply ml-2;
}

.pagination-container {
  @apply flex justify-center mt-6;
}
</style>