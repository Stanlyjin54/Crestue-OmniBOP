<template>
  <el-dialog
    v-model="dialogVisible"
    title="租户用户管理"
    width="1000px"
    :before-close="handleClose"
  >
    <div class="tenant-user-dialog">
      <!-- 租户基本信息 -->
      <div class="tenant-info">
        <el-card class="info-card">
          <div class="tenant-header">
            <div class="tenant-name">
              <h3>{{ currentTenant.tenantName }}</h3>
              <el-tag :type="getTenantTypeTagType(currentTenant.tenantType)" size="small">
                {{ TENANT_TYPES[currentTenant.tenantType]?.label || '未知' }}
              </el-tag>
            </div>
            <div class="tenant-stats">
              <div class="stat-item">
                <span class="stat-label">当前用户:</span>
                <span class="stat-value">{{ users.length }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">最大用户数:</span>
                <span class="stat-value">{{ currentTenant.quota?.maxUsers || 0 }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">使用率:</span>
                <span class="stat-value">{{ getUsagePercentage(users.length, currentTenant.quota?.maxUsers || 0) }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 用户列表操作栏 -->
      <div class="user-operations">
        <el-button type="primary" :icon="Plus" @click="handleAddUser">
          添加用户
        </el-button>
        <el-button :icon="Refresh" @click="loadUsers">刷新</el-button>
        
        <div class="search-bar">
          <el-input
            v-model="searchQuery"
            placeholder="搜索用户名、邮箱或手机号"
            :prefix-icon="Search"
            style="width: 300px"
            clearable
            @input="handleSearch"
          />
        </div>
      </div>

      <!-- 用户列表 -->
      <div class="user-list" v-loading="loading">
        <el-table
          :data="filteredUsers"
          style="width: 100%"
          :header-cell-style="{ backgroundColor: '#f5f7fa' }"
          empty-text="暂无用户数据"
        >
          <el-table-column prop="userId" label="用户ID" width="120" />
          <el-table-column prop="username" label="用户名" width="150" />
          <el-table-column prop="realName" label="真实姓名" width="120" />
          <el-table-column prop="email" label="邮箱" width="180" />
          <el-table-column prop="phone" label="手机号" width="130" />
          <el-table-column prop="role" label="角色" width="100">
            <template #default="{ row }">
              <el-tag :type="getRoleTagType(row.role)" size="small">
                {{ getRoleLabel(row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-switch
                v-model="row.status"
                active-value="ACTIVE"
                inactive-value="INACTIVE"
                @change="(value) => handleStatusChange(row, value)"
              />
              <span class="status-text">{{ row.status === 'ACTIVE' ? '启用' : '禁用' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="lastLoginTime" label="最后登录" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.lastLoginTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="200">
            <template #default="{ row }">
              <el-button type="primary" link @click="handleViewUser(row)">
                查看
              </el-button>
              <el-button type="warning" link @click="handleEditUser(row)">
                编辑
              </el-button>
              <el-button 
                type="danger" 
                link 
                @click="handleRemoveUser(row)"
                :disabled="row.role === 'ADMIN'"
              >
                移除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container" v-if="totalUsers > pageSize">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="totalUsers"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 用户操作对话框 -->
  <UserFormDialog
    v-model="userDialogVisible"
    :user-data="currentUser"
    :mode="userDialogMode"
    :tenant-id="currentTenant.tenantId"
    @success="handleUserDialogSuccess"
  />
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'

import { useTenant } from '@/composables/useTenant'
import UserFormDialog from './UserFormDialog.vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  tenantData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:modelValue'])

// 使用租户管理组合式函数
const { TENANT_TYPES, TENANT_STATUS, getTenantUsers, updateUserStatus, removeUserFromTenant } = useTenant()

// 响应式数据
const loading = ref(false)
const users = ref([])
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const totalUsers = ref(0)
const currentUser = ref({})
const userDialogVisible = ref(false)
const userDialogMode = ref('view') // view, add, edit

const currentTenant = reactive({
  tenantId: '',
  tenantName: '',
  tenantType: 'BASIC',
  quota: {
    maxUsers: 100
  }
})

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

/**
 * 过滤后的用户列表
 */
const filteredUsers = computed(() => {
  let result = users.value
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(user => 
      user.username?.toLowerCase().includes(query) ||
      user.realName?.toLowerCase().includes(query) ||
      user.email?.toLowerCase().includes(query) ||
      user.phone?.includes(query)
    )
  }
  
  return result
})

// 监听对话框显示，加载用户列表
watch(() => props.modelValue, (visible) => {
  if (visible && props.tenantData.tenantId) {
    Object.assign(currentTenant, props.tenantData)
    loadUsers()
  }
})

// 方法

/**
 * 加载租户用户列表
 */
const loadUsers = async () => {
  try {
    loading.value = true
    const result = await getTenantUsers(currentTenant.tenantId)
    
    if (result.success) {
      users.value = result.data || []
      totalUsers.value = users.value.length
    }
  } catch (error) {
    console.error('加载用户列表失败:', error)
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 处理搜索
 */
const handleSearch = () => {
  currentPage.value = 1
}

/**
 * 处理分页大小变化
 */
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
}

/**
 * 处理页码变化
 */
const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

/**
 * 处理添加用户
 */
const handleAddUser = () => {
  currentUser.value = {}
  userDialogMode.value = 'add'
  userDialogVisible.value = true
}

/**
 * 处理查看用户
 */
const handleViewUser = (user) => {
  currentUser.value = user
  userDialogMode.value = 'view'
  userDialogVisible.value = true
}

/**
 * 处理编辑用户
 */
const handleEditUser = (user) => {
  currentUser.value = user
  userDialogMode.value = 'edit'
  userDialogVisible.value = true
}

/**
 * 处理移除用户
 */
const handleRemoveUser = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要移除用户 "${user.realName || user.username}" 吗？`,
      '确认移除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const result = await removeUserFromTenant(currentTenant.tenantId, user.userId)
    
    if (result.success) {
      ElMessage.success('用户移除成功')
      loadUsers()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('移除用户失败:', error)
      ElMessage.error('移除用户失败')
    }
  }
}

/**
 * 处理用户状态切换
 */
const handleStatusChange = async (user, newStatus) => {
  try {
    const result = await updateUserStatus(currentTenant.tenantId, user.userId, newStatus)
    
    if (result.success) {
      ElMessage.success(`用户${newStatus === 'ACTIVE' ? '启用' : '禁用'}成功`)
      user.status = newStatus
    } else {
      // 还原状态
      user.status = user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
      ElMessage.error('状态更新失败')
    }
  } catch (error) {
    console.error('更新用户状态失败:', error)
    // 还原状态
    user.status = user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
    ElMessage.error('状态更新失败')
  }
}

/**
 * 处理用户对话框成功
 */
const handleUserDialogSuccess = () => {
  userDialogVisible.value = false
  loadUsers()
}

/**
 * 处理关闭对话框
 */
const handleClose = () => {
  emit('update:modelValue', false)
  // 重置状态
  users.value = []
  searchQuery.value = ''
  currentPage.value = 1
}

/**
 * 获取租户类型标签类型
 */
const getTenantTypeTagType = (tenantType) => {
  const typeMap = {
    ENTERPRISE: 'danger',
    PROFESSIONAL: 'warning',
    BASIC: 'primary',
    TRIAL: 'info',
    SYSTEM: 'success'
  }
  return typeMap[tenantType] || 'info'
}

/**
 * 获取角色标签类型
 */
const getRoleTagType = (role) => {
  const typeMap = {
    ADMIN: 'danger',
    MANAGER: 'warning',
    USER: 'primary',
    GUEST: 'info'
  }
  return typeMap[role] || 'info'
}

/**
 * 获取角色标签文本
 */
const getRoleLabel = (role) => {
  const labelMap = {
    ADMIN: '管理员',
    MANAGER: '经理',
    USER: '用户',
    GUEST: '访客'
  }
  return labelMap[role] || role
}

/**
 * 计算使用率百分比
 */
const getUsagePercentage = (used, total) => {
  if (!total || total === 0) return 0
  return Math.min(Math.round((used / total) * 100), 100)
}

/**
 * 格式化日期时间
 */
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}
</script>

<style scoped>
.tenant-user-dialog {
  height: 70vh;
  display: flex;
  flex-direction: column;
}

.tenant-info {
  margin-bottom: 20px;
}

.info-card {
  border: 1px solid #e4e7ed;
}

.tenant-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.tenant-name {
  display: flex;
  align-items: center;
  gap: 10px;
}

.tenant-name h3 {
  margin: 0;
  color: #303133;
}

.tenant-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.user-operations {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 6px;
}

.search-bar {
  margin-left: auto;
}

.user-list {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

:deep(.el-table) {
  flex: 1;
}

.status-text {
  margin-left: 8px;
  font-size: 12px;
  color: #606266;
}

.pagination-container {
  padding: 20px 0;
  text-align: right;
  border-top: 1px solid #e4e7ed;
  background: #fafafa;
}

.dialog-footer {
  text-align: right;
}
</style>