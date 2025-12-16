<template>
  <div class="tenant-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">
            <el-icon><OfficeBuilding /></el-icon>
            多租户管理
          </h1>
          <p class="page-description">管理租户隔离、权限控制和数据隔离机制</p>
        </div>
        <div class="action-section">
          <el-button
            type="primary"
            :icon="Plus"
            @click="showCreateDialog = true"
            :loading="loading"
            v-hasPermission="TENANT_PERMISSIONS.CREATE"
          >
            创建租户
          </el-button>
        </div>
      </div>
      
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>多租户管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
            <el-icon><OfficeBuilding /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalTenants }}</div>
            <div class="stat-label">总租户数</div>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
            <el-icon><Check /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.activeTenants }}</div>
            <div class="stat-label">活跃租户</div>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
            <el-icon><UserFilled /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalUsers }}</div>
            <div class="stat-label">租户用户总数</div>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">
            <el-icon><DataAnalysis /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.resourceUtilization }}%</div>
            <div class="stat-label">资源利用率</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-card shadow="never">
        <el-form
          ref="searchFormRef"
          :model="searchForm"
          :inline="true"
          class="search-form"
        >
          <el-form-item label="租户名称">
            <el-input
              v-model="searchForm.tenantName"
              placeholder="请输入租户名称"
              :prefix-icon="Search"
              clearable
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          
          <el-form-item label="租户类型">
            <el-select v-model="searchForm.tenantType" placeholder="请选择租户类型" clearable>
              <el-option
                v-for="(type, key) in TENANT_TYPES"
                :key="key"
                :label="type.label"
                :value="type.value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
              <el-option
                v-for="(status, key) in TENANT_STATUS"
                :key="key"
                :label="status.label"
                :value="status.value"
              >
                <el-tag :type="status.tagType" size="small">{{ status.label }}</el-tag>
              </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="联系人邮箱">
            <el-input
              v-model="searchForm.contactEmail"
              placeholder="请输入联系人邮箱"
              clearable
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearchForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 租户列表 -->
    <div class="tenant-list">
      <el-card shadow="never">
        <el-table
          v-loading="loading"
          :data="filteredTenants"
          style="width: 100%"
          :default-sort="{ prop: 'createTime', order: 'descending' }"
        >
          <el-table-column type="expand">
            <template #default="{ row }">
              <div class="tenant-detail">
                <div class="detail-grid">
                  <div class="detail-item">
                    <span class="detail-label">租户类型：</span>
                    <el-tag :color="TENANT_TYPES[row.tenantType]?.color" style="color: white">
                      {{ TENANT_TYPES[row.tenantType]?.label }}
                    </el-tag>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">行业：</span>
                    <span>{{ row.industry || '未设置' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">公司名称：</span>
                    <span>{{ row.companyName }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">联系电话：</span>
                    <span>{{ row.contactPhone }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">地址：</span>
                    <span>{{ row.address || '未设置' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">过期时间：</span>
                    <span :class="{ 'text-danger': new Date(row.expireTime) < new Date() }">
                      {{ row.expireTime }}
                    </span>
                  </div>
                </div>
                <div v-if="row.description" class="detail-description">
                  <span class="detail-label">描述：</span>
                  <span>{{ row.description }}</span>
                </div>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="tenantName" label="租户名称" min-width="200">
            <template #default="{ row }">
              <div class="tenant-name">
                <el-icon class="tenant-icon"><OfficeBuilding /></el-icon>
                <span class="name-text">{{ row.tenantName }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="tenantType" label="租户类型" width="120">
            <template #default="{ row }">
              <el-tag :color="TENANT_TYPES[row.tenantType]?.color" style="color: white">
                {{ TENANT_TYPES[row.tenantType]?.label }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="TENANT_STATUS[row.status]?.tagType">
                {{ TENANT_STATUS[row.status]?.label }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="totalUsers" label="用户数" width="80">
            <template #default="{ row }">
              <span class="user-count">{{ row.totalUsers }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="totalProjects" label="项目数" width="80">
            <template #default="{ row }">
              <span class="project-count">{{ row.totalProjects }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="storageUsed" label="存储使用" width="120">
            <template #default="{ row }">
              <div class="storage-usage">
                <span>{{ row.storageUsed }}GB / {{ row.maxStorage }}GB</span>
                <el-progress
                  :percentage="Math.round((row.storageUsed / row.maxStorage) * 100)"
                  :show-text="false"
                  :stroke-width="6"
                  size="small"
                />
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="lastLoginTime" label="最后登录" width="160">
            <template #default="{ row }">
              <span>{{ row.lastLoginTime || '从未登录' }}</span>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button
                  type="primary"
                  size="small"
                  :icon="View"
                  @click="handleViewDetail(row)"
                  v-hasPermission="TENANT_PERMISSIONS.VIEW"
                >
                  查看
                </el-button>
                
                <el-button
                  type="success"
                  size="small"
                  :icon="Edit"
                  @click="handleEdit(row)"
                  v-hasPermission="TENANT_PERMISSIONS.UPDATE"
                >
                  编辑
                </el-button>
                
                <el-dropdown @command="(command) => handleAction(command, row)">
                  <el-button size="small" :icon="MoreFilled">
                    更多
                    <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item
                        command="switch"
                        :icon="Switch"
                        v-if="row.status !== 'ACTIVE'"
                      >
                        启用租户
                      </el-dropdown-item>
                      <el-dropdown-item
                        command="suspend"
                        :icon="Switch"
                        v-if="row.status === 'ACTIVE'"
                      >
                        暂停租户
                      </el-dropdown-item>
                      <el-dropdown-item command="users" :icon="UserFilled">
                        用户管理
                      </el-dropdown-item>
                      <el-dropdown-item command="resources" :icon="DataAnalysis">
                        资源监控
                      </el-dropdown-item>
                      <el-dropdown-item command="export" :icon="Download">
                        导出数据
                      </el-dropdown-item>
                      <el-dropdown-item command="analyze" :icon="DataAnalysis">
                        AI分析
                      </el-dropdown-item>
                      <el-dropdown-item
                        command="delete"
                        :icon="Delete"
                        class="text-danger"
                        v-hasPermission="TENANT_PERMISSIONS.DELETE"
                      >
                        删除租户
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination-wrapper">
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
    </div>

    <!-- 创建租户对话框 -->
    <TenantCreateDialog
      v-model="showCreateDialog"
      @success="handleCreateSuccess"
    />

    <!-- 编辑租户对话框 -->
    <TenantEditDialog
      v-model="showEditDialog"
      :tenant="currentTenant"
      @success="handleEditSuccess"
    />

    <!-- 租户详情对话框 -->
    <TenantDetailDialog
      v-model="showDetailDialog"
      :tenant="currentTenant"
    />

    <!-- 租户用户管理对话框 -->
    <TenantUsersDialog
      v-model="showUsersDialog"
      :tenant="currentTenant"
    />

    <!-- 租户资源监控对话框 -->
    <TenantResourcesDialog
      v-model="showResourcesDialog"
      :tenant="currentTenant"
    />

    <!-- AI分析对话框 -->
    <AiAnalysisDialog
      v-model="showAnalysisDialog"
      :data="analysisData"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useTenant } from '@/composables/useTenant'
import {
  OfficeBuilding,
  Plus,
  Search,
  Refresh,
  Check,
  UserFilled,
  DataAnalysis,
  View,
  Edit,
  MoreFilled,
  ArrowDown,
  Switch,
  Download,
  Delete,
  TrendCharts
} from '@element-plus/icons-vue'

import TenantCreateDialog from './components/TenantCreateDialog.vue'
import TenantEditDialog from './components/TenantEditDialog.vue'
import TenantDetailDialog from './components/TenantDetailDialog.vue'
import TenantUsersDialog from './components/TenantUsersDialog.vue'
import TenantResourcesDialog from './components/TenantResourcesDialog.vue'
import AiAnalysisDialog from '../ai_assistant/components/AiAnalysisDialog.vue'

// 使用租户管理组合式函数
const {
  tenantList,
  currentTenant,
  loading,
  stats,
  searchForm,
  pagination,
  filteredTenants,
  TENANT_TYPES,
  TENANT_STATUS,
  fetchTenantList,
  createTenant,
  updateTenant,
  deleteTenant,
  toggleTenantStatus,
  fetchTenantUsers,
  fetchTenantResources,
  resetSearchForm
} = useTenant()

// 权限常量
const TENANT_PERMISSIONS = {
  VIEW: 'tenant:view',
  CREATE: 'tenant:create',
  UPDATE: 'tenant:update',
  DELETE: 'tenant:delete',
  MANAGE_USERS: 'tenant:users',
  MANAGE_RESOURCES: 'tenant:resources'
}

// 响应式数据
const showCreateDialog = ref(false)
const showEditDialog = ref(false)
const showDetailDialog = ref(false)
const showUsersDialog = ref(false)
const showResourcesDialog = ref(false)
const showAnalysisDialog = ref(false)
const searchFormRef = ref()
const analysisData = ref({})

// 计算属性
const activeTenants = computed(() => 
  tenantList.value.filter(t => t.status === 'ACTIVE')
)

// 方法

/**
 * 处理搜索
 */
const handleSearch = () => {
  pagination.currentPage = 1
  fetchTenantList()
}

/**
 * 重置搜索表单
 */
const resetSearchForm = () => {
  resetSearchForm()
  pagination.currentPage = 1
  fetchTenantList()
}

/**
 * 处理页面大小变化
 */
const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  fetchTenantList()
}

/**
 * 处理页面变化
 */
const handleCurrentChange = (page) => {
  pagination.currentPage = page
  fetchTenantList()
}

/**
 * 处理查看详情
 */
const handleViewDetail = (tenant) => {
  currentTenant.value = tenant
  showDetailDialog.value = true
}

/**
 * 处理编辑
 */
const handleEdit = (tenant) => {
  currentTenant.value = tenant
  showEditDialog.value = true
}

/**
 * 处理下拉操作
 */
const handleAction = async (command, tenant) => {
  switch (command) {
    case 'switch':
      await toggleTenantStatus(tenant.id)
      break
    case 'suspend':
      await toggleTenantStatus(tenant.id)
      break
    case 'users':
      currentTenant.value = tenant
      showUsersDialog.value = true
      break
    case 'resources':
      currentTenant.value = tenant
      showResourcesDialog.value = true
      break
    case 'export':
      await handleExportTenant(tenant)
      break
    case 'delete':
      await handleDeleteTenant(tenant)
      break
  }
}

/**
 * 处理创建成功
 */
const handleCreateSuccess = () => {
  showCreateDialog.value = false
  fetchTenantList()
}

/**
 * 处理编辑成功
 */
const handleEditSuccess = () => {
  showEditDialog.value = false
  fetchTenantList()
}

/**
 * 处理导出租户数据
 */
const handleExportTenant = async (tenant) => {
  try {
    await ElMessageBox.confirm(
      `确定要导出租户 "${tenant.tenantName}" 的所有数据吗？`,
      '导出确认',
      {
        confirmButtonText: '确定导出',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 模拟导出
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('租户数据导出成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('导出失败')
    }
  }
}

/**
 * 处理删除租户
 */
const handleDeleteTenant = async (tenant) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除租户 "${tenant.tenantName}" 吗？此操作不可恢复！`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error'
      }
    )
    
    await deleteTenant(tenant.id)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 处理AI分析
 */
const handleAnalysis = async () => {
  try {
    // 加载分析数据
    analysisData.value = {
      totalRequests: '2,450',
      avgResponseTime: 1250,
      activeUsers: 45,
      successRate: 98.5
    }
    
    showAnalysisDialog.value = true
  } catch (error) {
    ElMessage.error('加载分析数据失败')
  }
}

/**
 * 处理导出
 */
const handleExport = () => {
  // 导出所有租户数据的逻辑
  ElMessage.success('租户数据导出成功')
}

// 生命周期
onMounted(() => {
  fetchTenantList()
})
</script>

<style scoped>
.tenant-management {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 60px);
}

.page-header {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.title-section .page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-section .page-description {
  color: #909399;
  margin: 5px 0 0 0;
  font-size: 14px;
}

.search-section {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.tenant-list {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.tenant-detail {
  padding: 20px;
  background-color: #fafafa;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 15px;
  margin-bottom: 15px;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.detail-label {
  font-weight: 500;
  color: #606266;
  min-width: 80px;
}

.detail-description {
  padding: 10px 0;
  border-top: 1px solid #ebeef5;
}

.tenant-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tenant-icon {
  color: #409EFF;
  font-size: 16px;
}

.name-text {
  font-weight: 500;
}

.user-count,
.project-count {
  font-weight: 500;
  color: #409EFF;
}

.storage-usage {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.storage-usage span {
  font-size: 12px;
  color: #606266;
}

.text-danger {
  color: #f56c6c !important;
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.pagination-wrapper {
  padding: 20px;
  display: flex;
  justify-content: center;
}

.text-danger {
  color: #f56c6c;
}

:deep(.el-table .el-table__row:hover) {
  background-color: #f5f7fa;
}

:deep(.el-dropdown-menu__item.text-danger) {
  color: #f56c6c;
}

:deep(.el-dropdown-menu__item.text-danger:hover) {
  background-color: #fef0f0;
  color: #f56c6c;
}
</style>