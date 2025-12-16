<template>
  <div class="customer-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">客户管理</h1>
          <p class="page-subtitle">B2B客户档案管理、客户关系维护、客户数据分析</p>
        </div>
        <div class="header-image">
          <img src="@/assets/logo/document.png" alt="客户管理" />
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>客户管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :xs="12" :sm="6" :lg="6">
          <div class="stat-card">
            <div class="stat-icon customers">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.totalCustomers || 0 }}</div>
              <div class="stat-label">总客户数</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :lg="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.activeCustomers || 0 }}</div>
              <div class="stat-label">活跃客户</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :lg="6">
          <div class="stat-card">
            <div class="stat-icon enterprise">
              <el-icon><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.enterpriseCustomers || 0 }}</div>
              <div class="stat-label">企业客户</div>
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :lg="6">
          <div class="stat-card">
            <div class="stat-icon new">
              <el-icon><Plus /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.newCustomersThisMonth || 0 }}</div>
              <div class="stat-label">本月新增</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <el-card>
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="关键词">
            <el-input
              v-model="searchForm.keyword"
              placeholder="客户名称/手机号/邮箱"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="客户类型">
            <el-select v-model="searchForm.customerType" placeholder="全部" clearable @change="handleSearch">
              <el-option label="个人客户" value="individual" />
              <el-option label="企业客户" value="enterprise" />
              <el-option label="经销商" value="distributor" />
              <el-option label="供应商" value="supplier" />
              <el-option label="合作伙伴" value="partner" />
            </el-select>
          </el-form-item>
          <el-form-item label="客户状态">
            <el-select v-model="searchForm.status" placeholder="全部" clearable @change="handleSearch">
              <el-option label="活跃" value="active" />
              <el-option label="非活跃" value="inactive" />
              <el-option label="暂停" value="suspended" />
              <el-option label="待审核" value="pending" />
              <el-option label="黑名单" value="blacklisted" />
            </el-select>
          </el-form-item>
          <el-form-item label="客户等级">
            <el-select v-model="searchForm.level" placeholder="全部" clearable @change="handleSearch">
              <el-option label="普通客户" value="normal" />
              <el-option label="VIP客户" value="vip" />
              <el-option label="SVIP客户" value="svip" />
              <el-option label="钻石客户" value="diamond" />
              <el-option label="黑金客户" value="black_gold" />
            </el-select>
          </el-form-item>
          <el-form-item label="注册时间">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              @change="handleSearch"
            />
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
    </div>

    <!-- 操作按钮 -->
    <div class="action-section">
      <el-row justify="space-between">
        <el-col>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            新建客户
          </el-button>
          <el-button @click="handleImport">
            <el-icon><Upload /></el-icon>
            导入
          </el-button>
          <el-button @click="handleExport" :disabled="selectedCustomers.length === 0">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
          <el-button type="danger" @click="handleBatchDelete" :disabled="selectedCustomers.length === 0">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </el-col>
        <el-col>
          <el-button-group>
            <el-button @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </div>

    <!-- 客户列表 -->
    <div class="table-section">
      <el-card>
        <el-table
          v-loading="loading"
          :data="customers"
          @selection-change="handleSelectionChange"
          stripe
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="customerName" label="客户名称" min-width="150" show-overflow-tooltip>
            <template #default="scope">
              <div class="customer-name">
                <el-link type="primary" @click="handleView(scope.row)">
                  {{ scope.row.customerName }}
                </el-link>
                <el-tag v-if="scope.row.companyName" size="small" type="info" class="company-tag">
                  {{ scope.row.companyName }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="customerType" label="客户类型" width="100">
            <template #default="scope">
              <el-tag :type="getCustomerTypeTag(scope.row.customerType)">
                {{ getCustomerTypeLabel(scope.row.customerType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="手机号" width="120" />
          <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
          <el-table-column prop="level" label="等级" width="80">
            <template #default="scope">
              <el-tag :type="getCustomerLevelTag(scope.row.level)">
                {{ getCustomerLevelLabel(scope.row.level) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag :type="getCustomerStatusTag(scope.row.status)">
                {{ getCustomerStatusLabel(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="totalOrderAmount" label="订单总额" width="120" align="right">
            <template #default="scope">
              ¥{{ formatCurrency(scope.row.totalOrderAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="orderCount" label="订单数" width="80" align="center" />
          <el-table-column prop="lastOrderDate" label="最后下单" width="120">
            <template #default="scope">
              {{ formatDate(scope.row.lastOrderDate) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button type="primary" link @click="handleView(scope.row)">
                查看
              </el-button>
              <el-button type="primary" link @click="handleEdit(scope.row)">
                编辑
              </el-button>
              <el-button 
                type="primary" 
                link 
                @click="handleUpdateStatus(scope.row)"
                :disabled="scope.row.status === 'blacklisted'"
              >
                {{ scope.row.status === 'active' ? '停用' : '启用' }}
              </el-button>
              <el-button type="danger" link @click="handleDelete(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.limit"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 客户表单对话框 -->
    <CustomerFormDialog
      v-model="formDialog.visible"
      :type="formDialog.type"
      :customer="formDialog.customer"
      @success="handleFormSuccess"
    />

    <!-- 导入对话框 -->
    <el-dialog
      v-model="importDialog.visible"
      title="导入客户"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-upload
        class="upload-demo"
        drag
        action="#"
        :before-upload="handleBeforeUpload"
        :http-request="handleImportRequest"
        :show-file-list="false"
        accept=".xlsx,.xls,.csv"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          拖拽文件到此处或 <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            支持 xlsx/xls/csv 格式，文件大小不超过 10MB
          </div>
        </template>
      </el-upload>
      <template #footer>
        <el-button @click="importDialog.visible = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useCustomer } from '@/composables/useCustomer'
import CustomerFormDialog from './components/CustomerFormDialog.vue'

const router = useRouter()
const { 
  loading, 
  customers, 
  pagination, 
  searchForm, 
  fetchCustomers, 
  deleteCustomer, 
  batchDeleteCustomers, 
  updateCustomerStatus, 
  exportCustomers, 
  importCustomers, 
  getCustomerStats,
  resetSearch,
  resetPagination
} = useCustomer()

const selectedCustomers = ref([])
const stats = ref({})

// 对话框状态
const formDialog = reactive({
  visible: false,
  type: 'create',
  customer: null
})

const importDialog = reactive({
  visible: false
})

// 获取客户统计
const loadStats = async () => {
  try {
    const data = await getCustomerStats()
    stats.value = data
  } catch (error) {
    console.error('获取客户统计失败:', error)
  }
}

// 搜索
const handleSearch = async () => {
  resetPagination()
  await fetchCustomers()
}

// 重置搜索
const handleReset = () => {
  resetSearch()
  handleSearch()
}

// 刷新
const handleRefresh = async () => {
  await fetchCustomers()
  await loadStats()
}

// 创建客户
const handleCreate = () => {
  formDialog.type = 'create'
  formDialog.customer = null
  formDialog.visible = true
}

// 查看客户
const handleView = (row) => {
  router.push(`/customer/detail/${row.id}`)
}

// 编辑客户
const handleEdit = (row) => {
  formDialog.type = 'edit'
  formDialog.customer = { ...row }
  formDialog.visible = true
}

// 删除客户
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除客户 "${row.customerName}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteCustomer(row.id)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除客户失败:', error)
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedCustomers.value.length === 0) {
    ElMessage.warning('请选择要删除的客户')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedCustomers.value.length} 个客户吗？`,
      '确认批量删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const ids = selectedCustomers.value.map(item => item.id)
    await batchDeleteCustomers(ids)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
    }
  }
}

// 更新状态
const handleUpdateStatus = async (row) => {
  try {
    const newStatus = row.status === 'active' ? 'inactive' : 'active'
    const actionText = row.status === 'active' ? '停用' : '启用'
    
    await ElMessageBox.confirm(
      `确定要${actionText}客户 "${row.customerName}" 吗？`,
      `确认${actionText}`,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await updateCustomerStatus(row.id, newStatus)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新状态失败:', error)
    }
  }
}

// 导入
const handleImport = () => {
  importDialog.visible = true
}

// 导出
const handleExport = async () => {
  if (selectedCustomers.value.length === 0) {
    ElMessage.warning('请选择要导出的客户')
    return
  }

  try {
    await exportCustomers()
  } catch (error) {
    console.error('导出失败:', error)
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedCustomers.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.limit = size
  fetchCustomers()
}

// 页码变化
const handlePageChange = (page) => {
  pagination.page = page
  fetchCustomers()
}

// 表单成功
const handleFormSuccess = () => {
  formDialog.visible = false
  fetchCustomers()
  loadStats()
}

// 文件上传前检查
const handleBeforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                   file.type === 'application/vnd.ms-excel' ||
                   file.name.endsWith('.csv')
  
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isExcel) {
    ElMessage.error('只能上传 Excel/CSV 文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  return true
}

// 导入请求
const handleImportRequest = async (options) => {
  try {
    await importCustomers(options.file)
    importDialog.visible = false
  } catch (error) {
    console.error('导入失败:', error)
  }
}

// 工具函数
const getCustomerTypeTag = (type) => {
  const tagMap = {
    individual: 'info',
    enterprise: 'primary',
    distributor: 'success',
    supplier: 'warning',
    partner: 'danger'
  }
  return tagMap[type] || 'info'
}

const getCustomerTypeLabel = (type) => {
  const labelMap = {
    individual: '个人客户',
    enterprise: '企业客户',
    distributor: '经销商',
    supplier: '供应商',
    partner: '合作伙伴'
  }
  return labelMap[type] || type
}

const getCustomerLevelTag = (level) => {
  const tagMap = {
    normal: 'info',
    vip: 'warning',
    svip: 'danger',
    diamond: 'primary',
    black_gold: 'success'
  }
  return tagMap[level] || 'info'
}

const getCustomerLevelLabel = (level) => {
  const labelMap = {
    normal: '普通客户',
    vip: 'VIP客户',
    svip: 'SVIP客户',
    diamond: '钻石客户',
    black_gold: '黑金客户'
  }
  return labelMap[level] || level
}

const getCustomerStatusTag = (status) => {
  const tagMap = {
    active: 'success',
    inactive: 'info',
    suspended: 'warning',
    pending: 'primary',
    blacklisted: 'danger'
  }
  return tagMap[status] || 'info'
}

const getCustomerStatusLabel = (status) => {
  const labelMap = {
    active: '活跃',
    inactive: '非活跃',
    suspended: '暂停',
    pending: '待审核',
    blacklisted: '黑名单'
  }
  return labelMap[status] || status
}

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(amount)
}

const formatDate = (dateString) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 初始化
onMounted(async () => {
  await Promise.all([fetchCustomers(), loadStats()])
})
</script>

<style lang="scss" scoped>
.customer-page {
  padding: 20px;
  background-color: var(--el-bg-color-page);
  min-height: calc(100vh - 84px);
}

// 页面头部
.page-header {
  background: linear-gradient(135deg, #5D5FEF 0%, #4F52CC 100%);
  border-radius: 12px;
  margin-bottom: 20px;
  overflow: hidden;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30px 40px;
    height: 140px;

    .title-section {
      .page-title {
        font-size: 28px;
        font-weight: bold;
        color: #FFFFFF;
        margin: 0 0 8px 0;
      }
      
      .page-subtitle {
        font-size: 14px;
        color: #E6E6FF;
        margin: 0;
        opacity: 0.9;
      }
    }

    .header-image {
      img {
        height: 100px;
        opacity: 0.8;
      }
    }
  }
}

// 面包屑
.breadcrumb {
  margin-bottom: 20px;
}

// 统计卡片
.stats-cards {
  margin-bottom: 20px;

  .stat-card {
    background: #FFFFFF;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.06);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.1);
    }

    .stat-icon {
      width: 56px;
      height: 56px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      font-size: 24px;

      &.customers {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
      }

      &.active {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        color: white;
      }

      &.enterprise {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        color: white;
      }

      &.new {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        color: white;
      }
    }

    .stat-content {
      .stat-number {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

// 搜索区域
.search-section {
  margin-bottom: 20px;

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }
}

// 操作区域
.action-section {
  margin-bottom: 20px;
}

// 表格区域
.table-section {
  .customer-name {
    display: flex;
    flex-direction: column;
    gap: 4px;

    .company-tag {
      margin-top: 4px;
    }
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
    padding: 20px 0;
  }
}

// 上传区域
.upload-demo {
  width: 100%;
}

// 响应式设计
@media (max-width: 768px) {
  .customer-page {
    padding: 10px;
  }

  .page-header {
    .header-content {
      padding: 20px;
      height: 120px;

      .title-section {
        .page-title {
          font-size: 24px;
        }
      }

      .header-image {
        img {
          height: 80px;
        }
      }
    }
  }

  .stats-cards {
    .stat-card {
      padding: 16px;

      .stat-icon {
        width: 48px;
        height: 48px;
        font-size: 20px;
      }

      .stat-content {
        .stat-number {
          font-size: 24px;
        }
      }
    }
  }

  .search-section {
    .search-form {
      .el-form-item {
        display: block;
        margin-bottom: 10px;
        width: 100%;

        .el-input,
        .el-select,
        .el-date-picker {
          width: 100%;
        }
      }
    }
  }

  .action-section {
    .el-row {
      flex-direction: column;
      gap: 10px;

      .el-col {
        text-align: center;
      }
    }
  }
}

// 深色主题适配
:deep(.dark) {
  .page-header {
    background: linear-gradient(135deg, #4a4cb8 0%, #3d4099 100%);
  }

  .stat-card {
    background: var(--el-bg-color);
    
    .stat-content {
      .stat-number {
        color: var(--el-text-color-primary);
      }
    }
  }
}
</style>