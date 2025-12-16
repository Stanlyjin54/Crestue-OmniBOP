<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">用户管理</h1>
          <p class="page-subtitle">管理系统用户信息，支持多角色权限配置</p>
        </div>
        <div class="header-image">
          <img src="@/assets/logo/document.png" alt="用户管理" />
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="用户名">
          <el-input
            v-model="queryParams.username"
            placeholder="请输入用户名"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input
            v-model="queryParams.phone"
            placeholder="请输入手机号"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select
            v-model="queryParams.role"
            placeholder="请选择角色"
            clearable
            @change="handleSearch"
          >
            <el-option
              v-for="role in roleOptions"
              :key="role.value"
              :label="role.label"
              :value="role.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择状态"
            clearable
            @change="handleSearch"
          >
            <el-option label="启用" value="active" />
            <el-option label="禁用" value="inactive" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
          <el-button @click="handleReset" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="action-buttons">
        <el-button
          v-if="hasPermission('user:create')"
          type="primary"
          @click="handleCreate"
          :icon="Plus"
        >
          新增用户
        </el-button>
        <el-button
          type="success"
          @click="handleExport"
          :icon="Download"
          :loading="exportLoading"
        >
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 用户列表 -->
    <div class="table-section">
      <el-table
        v-loading="loading"
        :data="users"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="用户ID" width="80" />
        <el-table-column label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :size="40" :src="row.avatar || defaultAvatar" />
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleTagType(row.role)">
              {{ getRoleName(row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" width="160">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="最后登录" width="160">
          <template #default="{ row }">
            {{ formatTime(row.lastLoginTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="hasPermission('user:view')"
              type="primary"
              link
              @click="handleView(row)"
            >
              查看
            </el-button>
            <el-button
              v-if="hasPermission('user:update')"
              type="primary"
              link
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="hasPermission('user:update')"
              type="warning"
              link
              @click="handleResetPassword(row)"
            >
              重置密码
            </el-button>
            <el-button
              v-if="hasPermission('user:delete')"
              type="danger"
              link
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
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 用户表单对话框 -->
    <user-form-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      :form-data="currentUser"
      :role-options="roleOptions"
      @submit="handleSubmit"
    />
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'
import { useAuth, useUserManagement, PERMISSIONS, ROLE_CONFIG } from '@/composables/useAuth'
import { formatTime } from '@/utils/format'
import UserFormDialog from './components/UserFormDialog.vue'

// 权限钩子
const { hasPermission } = useAuth()
const { users, loading, total, queryParams, fetchUsers, createUser, updateUser, deleteUser, resetUserPassword } = useUserManagement()

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// 对话框状态
const dialogVisible = ref(false)
const dialogTitle = ref('')
const currentUser = ref({})
const selectedUsers = ref([])
const exportLoading = ref(false)

// 角色选项
const roleOptions = computed(() => {
  return Object.entries(ROLE_CONFIG).map(([key, config]) => ({
    value: key,
    label: config.name
  }))
})

// 获取角色标签类型
const getRoleTagType = (role) => {
  const roleTypeMap = {
    super_admin: 'danger',
    admin: 'warning',
    manager: 'primary',
    sales: 'success',
    customer_service: 'info',
    warehouse: 'info',
    finance: 'info'
  }
  return roleTypeMap[role] || 'info'
}

// 获取角色名称
const getRoleName = (role) => {
  return ROLE_CONFIG[role]?.name || role
}

// 搜索
const handleSearch = () => {
  queryParams.page = 1
  fetchUsers()
}

// 重置搜索条件
const handleReset = () => {
  Object.assign(queryParams, {
    username: '',
    phone: '',
    role: '',
    status: '',
    page: 1
  })
  fetchUsers()
}

// 创建用户
const handleCreate = () => {
  dialogTitle.value = '新增用户'
  currentUser.value = {
    username: '',
    phone: '',
    password: '',
    role: 'sales',
    status: 'active',
    avatar: ''
  }
  dialogVisible.value = true
}

// 查看用户
const handleView = (row) => {
  dialogTitle.value = '查看用户'
  currentUser.value = { ...row }
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (row) => {
  dialogTitle.value = '编辑用户'
  currentUser.value = { ...row }
  dialogVisible.value = true
}

// 重置密码
const handleResetPassword = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要重置用户 "${row.username}" 的密码吗？`,
      '重置密码',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await resetUserPassword(row.id)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重置密码失败')
    }
  }
}

// 删除用户
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${row.username}" 吗？`,
      '删除用户',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteUser(row.id)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除用户失败')
    }
  }
}

// 提交表单
const handleSubmit = async (formData) => {
  try {
    let success = false
    
    if (formData.id) {
      // 编辑
      success = await updateUser(formData.id, formData)
    } else {
      // 新增
      success = await createUser(formData)
    }
    
    if (success) {
      dialogVisible.value = false
      fetchUsers()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 导出数据
const handleExport = async () => {
  exportLoading.value = true
  try {
    // 模拟导出
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('数据导出成功')
  } catch (error) {
    ElMessage.error('数据导出失败')
  } finally {
    exportLoading.value = false
  }
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedUsers.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  queryParams.pageSize = size
  fetchUsers()
}

// 当前页变化
const handleCurrentChange = (page) => {
  queryParams.page = page
  fetchUsers()
}

// 生命周期
onMounted(() => {
  fetchUsers()
})
</script>

<style lang="scss" scoped>
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  height: 140px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    
    .title-section {
      .page-title {
        font-size: 24px;
        font-weight: bold;
        color: #fff;
        margin: 0 0 8px 0;
      }
      
      .page-subtitle {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
        margin: 0;
      }
    }
    
    .header-image {
      img {
        height: 130px;
        opacity: 0.9;
      }
    }
  }
}

.breadcrumb {
  margin: 20px;
}

.search-section {
  background: #fff;
  padding: 20px;
  margin: 0 20px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  
  .search-form {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 20px;
  }
  
  .action-buttons {
    display: flex;
    gap: 10px;
  }
}

.table-section {
  background: #fff;
  padding: 20px;
  margin: 0 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  
  .pagination-section {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .search-section .search-form {
    flex-direction: column;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>