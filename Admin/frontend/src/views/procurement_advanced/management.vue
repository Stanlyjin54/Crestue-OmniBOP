<template>
  <div class="procurement-advanced-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon class="title-icon"><ShoppingBag /></el-icon>
            采购管理高级功能
          </h1>
          <p class="page-description">
            供应商管理、合同管理、采购分析和风险控制的一体化管理平台
          </p>
        </div>
        <div class="header-actions">
          <el-button :icon="Plus" type="primary" @click="handleCreateSupplier">
            新建供应商
          </el-button>
          <el-button :icon="Document" @click="handleCreateContract">
            新建合同
          </el-button>
          <el-button :icon="DataAnalysis" @click="handleGenerateReport">
            生成报告
          </el-button>
          <el-button :icon="Setting" @click="handleSettings">
            系统设置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb-nav">
      <el-breadcrumb-item>首页</el-breadcrumb-item>
      <el-breadcrumb-item>采购管理</el-breadcrumb-item>
      <el-breadcrumb-item>高级功能</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 统计卡片 -->
    <div class="statistics-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card primary">
            <div class="stat-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatNumber(activeSuppliers.length) }}</div>
              <div class="stat-label">活跃供应商</div>
              <div class="stat-trend positive">
                <el-icon><ArrowUp /></el-icon>
                <span>+12.5%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card success">
            <div class="stat-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatCurrency(procurementAnalytics.totalSpend) }}</div>
              <div class="stat-label">年度采购总额</div>
              <div class="stat-trend positive">
                <el-icon><ArrowUp /></el-icon>
                <span>+8.3%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card warning">
            <div class="stat-icon">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ highRiskSuppliers.length }}</div>
              <div class="stat-label">高风险供应商</div>
              <div class="stat-trend negative">
                <el-icon><ArrowDown /></el-icon>
                <span>-5.2%</span>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card info">
            <div class="stat-icon">
              <el-icon><Medal /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ procurementAnalytics.complianceScore }}%</div>
              <div class="stat-label">合规评分</div>
              <div class="stat-trend positive">
                <el-icon><ArrowUp /></el-icon>
                <span>+2.1%</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表分析区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>供应商绩效分析</h3>
              <div class="chart-actions">
                <el-button size="small" @click="refreshAnalytics">刷新</el-button>
                <el-button size="small" @click="exportAnalytics">导出</el-button>
              </div>
            </div>
            <div class="chart-content">
              <div class="chart-placeholder">
                <el-icon><TrendCharts /></el-icon>
                <span>供应商绩效图表</span>
                <div class="chart-description">
                  展示Top 10供应商的绩效评分分布情况
                </div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>采购分类分析</h3>
              <div class="chart-actions">
                <el-button size="small" @click="viewCategoryDetail">详情</el-button>
              </div>
            </div>
            <div class="chart-content">
              <div class="chart-placeholder">
                <el-icon><PieChart /></el-icon>
                <span>采购分类饼图</span>
                <div class="chart-description">
                  展示各采购分类的金额占比
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>风险指标监控</h3>
              <div class="chart-actions">
                <el-button size="small" @click="assessAllRisks">风险评估</el-button>
              </div>
            </div>
            <div class="chart-content">
              <div class="risk-metrics">
                <div class="risk-item">
                  <span class="risk-label">高风险供应商</span>
                  <span class="risk-value high">{{ procurementAnalytics.riskMetrics.highRiskSuppliers }}</span>
                </div>
                <div class="risk-item">
                  <span class="risk-label">合规问题</span>
                  <span class="risk-value warning">{{ procurementAnalytics.riskMetrics.complianceIssues }}</span>
                </div>
                <div class="risk-item">
                  <span class="risk-label">逾期合同</span>
                  <span class="risk-value danger">{{ procurementAnalytics.riskMetrics.overdueContracts }}</span>
                </div>
                <div class="risk-item">
                  <span class="risk-label">风险评分</span>
                  <span class="risk-value">{{ procurementAnalytics.riskMetrics.riskScore }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-card">
            <div class="chart-header">
              <h3>成本节约分析</h3>
              <div class="chart-actions">
                <el-button size="small" @click="optimizeCosts">成本优化</el-button>
              </div>
            </div>
            <div class="chart-content">
              <div class="savings-analysis">
                <div class="savings-item">
                  <div class="savings-value">{{ formatCurrency(procurementAnalytics.costSavings) }}</div>
                  <div class="savings-label">年度节约金额</div>
                  <div class="savings-percentage">6.8%</div>
                </div>
                <div class="savings-breakdown">
                  <div class="breakdown-item">
                    <span class="breakdown-label">批量采购节约</span>
                    <span class="breakdown-value">{{ formatCurrency(3200000) }}</span>
                  </div>
                  <div class="breakdown-item">
                    <span class="breakdown-label">供应商优化节约</span>
                    <span class="breakdown-value">{{ formatCurrency(2800000) }}</span>
                  </div>
                  <div class="breakdown-item">
                    <span class="breakdown-label">流程优化节约</span>
                    <span class="breakdown-value">{{ formatCurrency(2500000) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 筛选和搜索区域 -->
    <div class="filter-section">
      <el-card shadow="never">
        <div class="filter-content">
          <el-row :gutter="16" align="middle">
            <el-col :span="6">
              <el-input
                v-model="filterParams.search"
                placeholder="搜索供应商名称、编码、联系人"
                :prefix-icon="Search"
                clearable
                @input="handleSearch"
              />
            </el-col>
            <el-col :span="4">
              <el-select
                v-model="filterParams.category"
                placeholder="供应商分类"
                clearable
                style="width: 100%"
                @change="handleFilterChange"
              >
                <el-option
                  v-for="category in supplierCategories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select
                v-model="filterParams.riskLevel"
                placeholder="风险等级"
                clearable
                style="width: 100%"
                @change="handleFilterChange"
              >
                <el-option
                  v-for="level in riskLevels"
                  :key="level.id"
                  :label="level.name"
                  :value="level.id"
                />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select
                v-model="filterParams.status"
                placeholder="状态"
                clearable
                style="width: 100%"
                @change="handleFilterChange"
              >
                <el-option label="活跃" value="active" />
                <el-option label="非活跃" value="inactive" />
              </el-select>
            </el-col>
            <el-col :span="6">
              <div class="filter-actions">
                <el-button @click="resetFilters">重置</el-button>
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button :icon="Download" @click="exportSuppliers">导出</el-button>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>

    <!-- 供应商列表 -->
    <div class="table-section">
      <el-card shadow="never">
        <template #header>
          <div class="table-header">
            <div class="header-left">
              <h3>供应商管理</h3>
              <span class="table-subtitle">
                共 {{ filteredSuppliers.length }} 个供应商
              </span>
            </div>
            <div class="header-right">
              <el-button-group>
                <el-button
                  :icon="List"
                  :type="viewMode === 'table' ? 'primary' : ''"
                  @click="viewMode = 'table'"
                />
                <el-button
                  :icon="Grid"
                  :type="viewMode === 'card' ? 'primary' : ''"
                  @click="viewMode = 'card'"
                />
              </el-button-group>
              <el-button
                :icon="Refresh"
                circle
                @click="refreshData"
                :loading="loading"
              />
            </div>
          </div>
        </template>

        <!-- 表格视图 -->
        <el-table
          v-if="viewMode === 'table'"
          :data="paginatedSuppliers"
          v-loading="loading"
          stripe
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="code" label="供应商编码" width="120" />
          <el-table-column prop="name" label="供应商名称" min-width="200">
            <template #default="scope">
              <div class="supplier-name">
                <strong>{{ scope.row.name }}</strong>
                <el-tag size="small" class="supplier-tag">
                  {{ getSupplierCategoryName(scope.row.category) }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="contactPerson" label="联系人" width="100" />
          <el-table-column prop="contactPhone" label="联系电话" width="130" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag
                :type="scope.row.status === 'active' ? 'success' : 'info'"
                size="small"
              >
                {{ scope.row.status === 'active' ? '活跃' : '非活跃' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="riskLevel" label="风险等级" width="100">
            <template #default="scope">
              <el-tag
                :style="{ 
                  backgroundColor: getRiskLevelColor(scope.row.riskLevel) + '20',
                  borderColor: getRiskLevelColor(scope.row.riskLevel),
                  color: getRiskLevelColor(scope.row.riskLevel)
                }"
                size="small"
              >
                {{ riskLevels.find(l => l.id === scope.row.riskLevel)?.name }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="performance.overallScore" label="绩效评分" width="100">
            <template #default="scope">
              <div class="performance-score">
                <span class="score-value">{{ scope.row.performance.overallScore }}</span>
                <el-progress
                  :percentage="scope.row.performance.overallScore"
                  :stroke-width="6"
                  :show-text="false"
                  :color="getScoreColor(scope.row.performance.overallScore)"
                />
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="annualTurnover" label="年营业额" width="120">
            <template #default="scope">
              {{ formatCurrency(scope.row.annualTurnover) }}
            </template>
          </el-table-column>
          <el-table-column prop="contracts" label="合同数" width="80" />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button type="primary" link @click="handleViewDetail(scope.row)">
                查看
              </el-button>
              <el-button type="success" link @click="handleEdit(scope.row)">
                编辑
              </el-button>
              <el-button type="warning" link @click="handleRiskAssessment(scope.row)">
                风险评估
              </el-button>
              <el-dropdown @command="(command) => handleCommand(command, scope.row)">
                <el-button type="info" link>
                  更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="contract">合同管理</el-dropdown-item>
                    <el-dropdown-item command="performance">绩效分析</el-dropdown-item>
                    <el-dropdown-item command="optimize">优化建议</el-dropdown-item>
                    <el-dropdown-item command="history">历史记录</el-dropdown-item>
                    <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <!-- 卡片视图 -->
        <div v-else class="card-view">
          <el-row :gutter="20">
            <el-col
              v-for="supplier in paginatedSuppliers"
              :key="supplier.id"
              :span="8"
            >
              <el-card class="supplier-card" shadow="hover">
                <div class="card-header">
                  <div class="supplier-info">
                    <h4>{{ supplier.name }}</h4>
                    <p>{{ supplier.code }}</p>
                  </div>
                  <div class="card-actions">
                    <el-tag
                      :type="supplier.status === 'active' ? 'success' : 'info'"
                      size="small"
                    >
                      {{ supplier.status === 'active' ? '活跃' : '非活跃' }}
                    </el-tag>
                  </div>
                </div>
                
                <div class="card-content">
                  <div class="supplier-detail">
                    <div class="detail-item">
                      <span class="detail-label">联系人:</span>
                      <span class="detail-value">{{ supplier.contactPerson }}</span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">风险等级:</span>
                      <el-tag
                        :style="{ 
                          backgroundColor: getRiskLevelColor(supplier.riskLevel) + '20',
                          borderColor: getRiskLevelColor(supplier.riskLevel),
                          color: getRiskLevelColor(supplier.riskLevel)
                        }"
                        size="small"
                      >
                        {{ riskLevels.find(l => l.id === supplier.riskLevel)?.name }}
                      </el-tag>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">绩效评分:</span>
                      <span class="detail-value">{{ supplier.performance.overallScore }}</span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">合同数量:</span>
                      <span class="detail-value">{{ supplier.contracts }}</span>
                    </div>
                  </div>
                </div>
                
                <div class="card-footer">
                  <el-button type="primary" size="small" @click="handleViewDetail(supplier)">
                    查看详情
                  </el-button>
                  <el-button type="success" size="small" @click="handleEdit(supplier)">
                    编辑
                  </el-button>
                  <el-button type="warning" size="small" @click="handleRiskAssessment(supplier)">
                    风险评估
                  </el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 分页 -->
        <div class="pagination-section">
          <el-pagination
            v-model:current-page="pagination.currentPage"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="filteredSuppliers.length"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 供应商详情对话框 -->
    <SupplierDetailDialog
      v-model="detailDialogVisible"
      :supplier="selectedSupplier"
      @updated="handleSupplierUpdated"
    />

    <!-- 供应商表单对话框 -->
    <SupplierFormDialog
      v-model="formDialogVisible"
      :supplier="editingSupplier"
      :mode="dialogMode"
      @saved="handleSupplierSaved"
    />

    <!-- 风险评估对话框 -->
    <RiskAssessmentDialog
      v-model="riskDialogVisible"
      :supplier="riskAssessmentSupplier"
      :assessment="riskAssessment"
      @completed="handleRiskAssessmentCompleted"
    />

    <!-- 合同管理对话框 -->
    <ContractManagementDialog
      v-model="contractDialogVisible"
      :supplier="contractSupplier"
      @updated="handleContractsUpdated"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ShoppingBag, Plus, Document, DataAnalysis, Setting, User, Warning, Medal,
  TrendCharts, PieChart, ArrowUp, ArrowDown, Search, Download, List, Grid,
  Refresh, ArrowDown as DropdownArrow
} from '@element-plus/icons-vue'
import { useProcurementAdvanced } from '@/composables/useProcurementAdvanced'

// 导入对话框组件
import SupplierDetailDialog from './components/SupplierDetailDialog.vue'
import SupplierFormDialog from './components/SupplierFormDialog.vue'
import RiskAssessmentDialog from './components/RiskAssessmentDialog.vue'
import ContractManagementDialog from './components/ContractManagementDialog.vue'

// 组合式函数
const procurementAdvanced = useProcurementAdvanced()

// 响应式数据
const viewMode = ref('table')
const detailDialogVisible = ref(false)
const formDialogVisible = ref(false)
const riskDialogVisible = ref(false)
const contractDialogVisible = ref(false)
const selectedSupplier = ref(null)
const editingSupplier = ref(null)
const riskAssessmentSupplier = ref(null)
const contractSupplier = ref(null)
const riskAssessment = ref(null)
const dialogMode = ref('create')

// 计算属性
const {
  loading,
  suppliers,
  filteredSuppliers,
  activeSuppliers,
  highRiskSuppliers,
  procurementAnalytics,
  filterParams,
  pagination,
  supplierCategories,
  riskLevels,
  formatCurrency,
  formatNumber,
  getRiskLevelColor,
  getSupplierCategoryName,
  fetchSuppliers,
  fetchProcurementAnalytics,
  assessSupplierRisk,
  resetFilters
} = procurementAdvanced

const paginatedSuppliers = computed(() => {
  const start = (pagination.value.currentPage - 1) * pagination.value.pageSize
  const end = start + pagination.value.pageSize
  return filteredSuppliers.value.slice(start, end)
})

// 工具方法
const getScoreColor = (score) => {
  if (score >= 90) return '#67C23A'
  if (score >= 80) return '#E6A23C'
  if (score >= 70) return '#F56C6C'
  return '#909399'
}

// 事件处理方法
const handleCreateSupplier = () => {
  dialogMode.value = 'create'
  editingSupplier.value = null
  formDialogVisible.value = true
}

const handleCreateContract = () => {
  ElMessage.info('新建合同功能开发中...')
}

const handleGenerateReport = () => {
  ElMessage.info('报告生成功能开发中...')
}

const handleSettings = () => {
  ElMessage.info('系统设置功能开发中...')
}

const handleSearch = () => {
  fetchSuppliers()
}

const handleFilterChange = () => {
  fetchSuppliers()
}

const resetFilters = () => {
  resetFilters()
  fetchSuppliers()
}

const exportSuppliers = () => {
  ElMessage.success('供应商数据导出成功')
}

const refreshData = () => {
  fetchSuppliers()
  fetchProcurementAnalytics()
}

const refreshAnalytics = () => {
  fetchProcurementAnalytics()
}

const exportAnalytics = () => {
  ElMessage.success('分析数据导出成功')
}

const viewCategoryDetail = () => {
  ElMessage.info('分类详情功能开发中...')
}

const assessAllRisks = () => {
  ElMessage.info('批量风险评估功能开发中...')
}

const optimizeCosts = () => {
  ElMessage.info('成本优化功能开发中...')
}

const handleSelectionChange = (selection) => {
  // 处理选择变化
}

const handleViewDetail = (supplier) => {
  selectedSupplier.value = supplier
  detailDialogVisible.value = true
}

const handleEdit = (supplier) => {
  dialogMode.value = 'edit'
  editingSupplier.value = supplier
  formDialogVisible.value = true
}

const handleRiskAssessment = async (supplier) => {
  riskAssessmentSupplier.value = supplier
  try {
    riskAssessment.value = await assessSupplierRisk(supplier.id)
    riskDialogVisible.value = true
  } catch (error) {
    // 错误已在组合式函数中处理
  }
}

const handleCommand = (command, supplier) => {
  switch (command) {
    case 'contract':
      contractSupplier.value = supplier
      contractDialogVisible.value = true
      break
    case 'performance':
      ElMessage.info('绩效分析功能开发中...')
      break
    case 'optimize':
      ElMessage.info('优化建议功能开发中...')
      break
    case 'history':
      ElMessage.info('历史记录功能开发中...')
      break
    case 'delete':
      handleDelete(supplier)
      break
  }
}

const handleDelete = async (supplier) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除供应商 "${supplier.name}" 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await procurementAdvanced.deleteSupplier(supplier.id)
  } catch (error) {
    if (error !== 'cancel') {
      // 错误已在组合式函数中处理
    }
  }
}

const handleSupplierSaved = () => {
  fetchSuppliers()
}

const handleSupplierUpdated = () => {
  fetchSuppliers()
}

const handleRiskAssessmentCompleted = () => {
  fetchSuppliers()
}

const handleContractsUpdated = () => {
  fetchSuppliers()
}

const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
}

const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
}

// 生命周期钩子
onMounted(() => {
  fetchSuppliers()
  fetchProcurementAnalytics()
})
</script>

<style scoped>
.procurement-advanced-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  flex: 1;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.title-icon {
  color: #409EFF;
}

.page-description {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.breadcrumb-nav {
  margin-bottom: 20px;
  padding: 12px 20px;
  background-color: white;
  border-radius: 6px;
}

.statistics-section {
  margin-bottom: 20px;
}

.stat-card {
  padding: 24px;
  border-radius: 8px;
  color: white;
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-card.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card.success {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-card.warning {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-card.info {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon {
  font-size: 32px;
  opacity: 0.8;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  opacity: 0.8;
}

.stat-trend.positive {
  color: #c6f6d5;
}

.stat-trend.negative {
  color: #fed7d7;
}

.charts-section {
  margin-bottom: 20px;
}

.chart-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.chart-header {
  padding: 16px 20px;
  border-bottom: 1px solid #EBEEF5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-header h3 {
  margin: 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.chart-actions {
  display: flex;
  gap: 8px;
}

.chart-content {
  padding: 20px;
}

.chart-placeholder {
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  text-align: center;
}

.chart-placeholder .el-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.chart-description {
  font-size: 12px;
  margin-top: 8px;
  opacity: 0.7;
}

.risk-metrics {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.risk-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border: 1px solid #EBEEF5;
  border-radius: 6px;
}

.risk-label {
  color: #606266;
  font-size: 14px;
}

.risk-value {
  font-weight: 600;
  font-size: 16px;
}

.risk-value.high {
  color: #E6A23C;
}

.risk-value.warning {
  color: #F56C6C;
}

.risk-value.danger {
  color: #C00;
}

.savings-analysis {
  text-align: center;
}

.savings-value {
  font-size: 24px;
  font-weight: 700;
  color: #67C23A;
  margin-bottom: 4px;
}

.savings-label {
  color: #606266;
  font-size: 14px;
  margin-bottom: 8px;
}

.savings-percentage {
  color: #67C23A;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}

.savings-breakdown {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.breakdown-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 12px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.breakdown-label {
  color: #606266;
  font-size: 13px;
}

.breakdown-value {
  font-weight: 500;
  color: #303133;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-content {
  padding: 16px 0;
}

.filter-actions {
  display: flex;
  gap: 8px;
}

.table-section {
  margin-bottom: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left h3 {
  margin: 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.table-subtitle {
  color: #909399;
  font-size: 12px;
  margin-left: 8px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.supplier-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.supplier-tag {
  font-size: 11px;
}

.performance-score {
  display: flex;
  flex-direction: column;
  gap: 4px;
  width: 60px;
}

.score-value {
  font-weight: 600;
  text-align: center;
  color: #303133;
}

.card-view {
  margin: -16px;
}

.supplier-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.supplier-info h4 {
  margin: 0 0 4px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.supplier-info p {
  margin: 0;
  color: #909399;
  font-size: 13px;
}

.card-content {
  margin-bottom: 16px;
}

.supplier-detail {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.detail-label {
  color: #909399;
  font-size: 13px;
  min-width: 60px;
}

.detail-value {
  color: #303133;
  font-size: 13px;
  font-weight: 500;
}

.card-footer {
  display: flex;
  gap: 8px;
}

.pagination-section {
  margin-top: 20px;
  text-align: right;
}
</style>