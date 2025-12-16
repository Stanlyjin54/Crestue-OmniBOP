<template>
  <base-wrapper>
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">角色管理</h1>
          <p class="page-subtitle">管理系统角色权限，支持灵活的权限配置</p>
        </div>
        <div class="header-image">
          <img src="@/assets/logo/document.png" alt="角色管理" />
        </div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>角色管理</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 角色列表 -->
    <div class="role-section">
      <div class="role-header">
        <h3>系统角色列表</h3>
        <el-button
          v-if="hasPermission('role:create')"
          type="primary"
          @click="handleCreate"
          :icon="Plus"
        >
          新增角色
        </el-button>
      </div>
      
      <div class="role-grid">
        <div
          v-for="role in roles"
          :key="role.code"
          class="role-card"
          :class="{ 'role-card-active': currentRole?.code === role.code }"
          @click="handleSelectRole(role)"
        >
          <div class="role-header-card">
            <div class="role-icon">
              <el-icon :size="24">
                <component :is="getRoleIcon(role.code)" />
              </el-icon>
            </div>
            <div class="role-info">
              <h4>{{ role.name }}</h4>
              <p>{{ role.description }}</p>
            </div>
          </div>
          
          <div class="role-stats">
            <div class="stat-item">
              <span class="stat-label">用户数</span>
              <span class="stat-value">{{ role.userCount }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">权限数</span>
              <span class="stat-value">{{ role.permissions?.length || 0 }}</span>
            </div>
          </div>
          
          <div class="role-actions">
            <el-button
              type="primary"
              link
              @click.stop="handleEdit(role)"
            >
              编辑
            </el-button>
            <el-button
              type="warning"
              link
              @click.stop="handlePermission(role)"
            >
              权限
            </el-button>
            <el-button
              v-if="role.code !== 'super_admin'"
              type="danger"
              link
              @click.stop="handleDelete(role)"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 权限配置区域 -->
    <div v-if="currentRole" class="permission-section">
      <div class="permission-header">
        <h3>{{ currentRole.name }} - 权限配置</h3>
        <div class="permission-actions">
          <el-button @click="handleSelectAll">全选</el-button>
          <el-button @click="handleDeselectAll">全不选</el-button>
          <el-button type="primary" @click="handleSavePermissions" :loading="saveLoading">
            保存权限
          </el-button>
        </div>
      </div>
      
      <div class="permission-tree">
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTreeData"
          show-checkbox
          node-key="id"
          :default-checked-keys="currentRole.permissions"
          :props="treeProps"
          @check-change="handlePermissionChange"
        >
          <template #default="{ node, data }">
            <div class="permission-node">
              <div class="permission-info">
                <el-icon v-if="data.icon" class="permission-icon">
                  <component :is="data.icon" />
                </el-icon>
                <span class="permission-label">{{ data.label }}</span>
                <el-tag v-if="data.type" :type="getPermissionTagType(data.type)" size="small">
                  {{ getPermissionTypeLabel(data.type) }}
                </el-tag>
              </div>
              <div class="permission-desc">{{ data.description }}</div>
            </div>
          </template>
        </el-tree>
      </div>
    </div>

    <!-- 角色表单对话框 -->
    <role-form-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      :form-data="currentRoleForm"
      @submit="handleSubmit"
    />
  </base-wrapper>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, User, Management, Setting, Shop, Document, Box, Coin, Monitor } from '@element-plus/icons-vue'
import { useAuth, PERMISSIONS } from '@/composables/useAuth'
import RoleFormDialog from './components/RoleFormDialog.vue'

// 权限钩子
const { hasPermission } = useAuth()

// 状态
const roles = ref([])
const currentRole = ref(null)
const loading = ref(false)
const saveLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const currentRoleForm = ref({})
const permissionTreeRef = ref()

// 树形配置
const treeProps = {
  children: 'children',
  label: 'label'
}

// 权限图标映射
const permissionIconMap = {
  'user': User,
  'role': Management,
  'permission': Setting,
  'product': Shop,
  'order': Document,
  'inventory': Box,
  'finance': Coin,
  'system': Monitor
}

// 角色图标映射
const roleIconMap = {
  super_admin: Setting,
  admin: Management,
  manager: User,
  sales: Shop,
  customer_service: User,
  warehouse: Box,
  finance: Coin
}

// 权限树数据
const permissionTreeData = [
  {
    id: 'user',
    label: '用户管理',
    icon: 'user',
    type: 'module',
    description: '管理系统用户信息',
    children: [
      { id: PERMISSIONS.USER_VIEW, label: '查看用户', type: 'view', description: '查看用户列表和详情' },
      { id: PERMISSIONS.USER_CREATE, label: '创建用户', type: 'create', description: '创建新用户' },
      { id: PERMISSIONS.USER_UPDATE, label: '更新用户', type: 'update', description: '修改用户信息' },
      { id: PERMISSIONS.USER_DELETE, label: '删除用户', type: 'delete', description: '删除用户' }
    ]
  },
  {
    id: 'role',
    label: '角色管理',
    icon: 'role',
    type: 'module',
    description: '管理系统角色和权限',
    children: [
      { id: PERMISSIONS.ROLE_VIEW, label: '查看角色', type: 'view', description: '查看角色列表' },
      { id: PERMISSIONS.ROLE_CREATE, label: '创建角色', type: 'create', description: '创建新角色' },
      { id: PERMISSIONS.ROLE_UPDATE, label: '更新角色', type: 'update', description: '修改角色信息' },
      { id: PERMISSIONS.ROLE_DELETE, label: '删除角色', type: 'delete', description: '删除角色' }
    ]
  },
  {
    id: 'permission',
    label: '权限管理',
    icon: 'permission',
    type: 'module',
    description: '管理系统权限配置',
    children: [
      { id: PERMISSIONS.PERMISSION_VIEW, label: '查看权限', type: 'view', description: '查看权限列表' },
      { id: PERMISSIONS.PERMISSION_CREATE, label: '创建权限', type: 'create', description: '创建新权限' },
      { id: PERMISSIONS.PERMISSION_UPDATE, label: '更新权限', type: 'update', description: '修改权限信息' },
      { id: PERMISSIONS.PERMISSION_DELETE, label: '删除权限', type: 'delete', description: '删除权限' }
    ]
  },
  {
    id: 'product',
    label: '商品管理',
    icon: 'product',
    type: 'module',
    description: '管理系统商品信息',
    children: [
      { id: PERMISSIONS.PRODUCT_VIEW, label: '查看商品', type: 'view', description: '查看商品列表' },
      { id: PERMISSIONS.PRODUCT_CREATE, label: '创建商品', type: 'create', description: '创建新商品' },
      { id: PERMISSIONS.PRODUCT_UPDATE, label: '更新商品', type: 'update', description: '修改商品信息' },
      { id: PERMISSIONS.PRODUCT_DELETE, label: '删除商品', type: 'delete', description: '删除商品' }
    ]
  },
  {
    id: 'order',
    label: '订单管理',
    icon: 'order',
    type: 'module',
    description: '管理系统订单信息',
    children: [
      { id: PERMISSIONS.ORDER_VIEW, label: '查看订单', type: 'view', description: '查看订单列表' },
      { id: PERMISSIONS.ORDER_CREATE, label: '创建订单', type: 'create', description: '创建新订单' },
      { id: PERMISSIONS.ORDER_UPDATE, label: '更新订单', type: 'update', description: '修改订单信息' },
      { id: PERMISSIONS.ORDER_DELETE, label: '删除订单', type: 'delete', description: '删除订单' }
    ]
  },
  {
    id: 'customer',
    label: '客户管理',
    icon: 'user',
    type: 'module',
    description: '管理系统客户信息',
    children: [
      { id: PERMISSIONS.CUSTOMER_VIEW, label: '查看客户', type: 'view', description: '查看客户列表' },
      { id: PERMISSIONS.CUSTOMER_CREATE, label: '创建客户', type: 'create', description: '创建新客户' },
      { id: PERMISSIONS.CUSTOMER_UPDATE, label: '更新客户', type: 'update', description: '修改客户信息' },
      { id: PERMISSIONS.CUSTOMER_DELETE, label: '删除客户', type: 'delete', description: '删除客户' }
    ]
  },
  {
    id: 'system',
    label: '系统管理',
    icon: 'system',
    type: 'module',
    description: '管理系统配置和监控',
    children: [
      { id: PERMISSIONS.SYSTEM_CONFIG, label: '系统配置', type: 'config', description: '配置系统参数' },
      { id: PERMISSIONS.SYSTEM_LOG, label: '系统日志', type: 'log', description: '查看系统日志' },
      { id: PERMISSIONS.SYSTEM_MONITOR, label: '系统监控', type: 'monitor', description: '监控系统状态' }
    ]
  }
]

// 获取权限标签类型
const getPermissionTagType = (type) => {
  const typeMap = {
    view: 'info',
    create: 'success',
    update: 'warning',
    delete: 'danger',
    module: 'primary',
    config: 'primary',
    log: 'info',
    monitor: 'warning'
  }
  return typeMap[type] || 'info'
}

// 获取权限类型标签
const getPermissionTypeLabel = (type) => {
  const labelMap = {
    view: '查看',
    create: '创建',
    update: '更新',
    delete: '删除',
    module: '模块',
    config: '配置',
    log: '日志',
    monitor: '监控'
  }
  return labelMap[type] || type
}

// 获取角色图标
const getRoleIcon = (role) => {
  return roleIconMap[role] || User
}

// 选择角色
const handleSelectRole = (role) => {
  currentRole.value = role
}

// 创建角色
const handleCreate = () => {
  dialogTitle.value = '新增角色'
  currentRoleForm.value = {
    name: '',
    code: '',
    description: '',
    permissions: []
  }
  dialogVisible.value = true
}

// 编辑角色
const handleEdit = (role) => {
  dialogTitle.value = '编辑角色'
  currentRoleForm.value = { ...role }
  dialogVisible.value = true
}

// 权限配置
const handlePermission = (role) => {
  currentRole.value = role
}

// 删除角色
const handleDelete = async (role) => {
  if (role.code === 'super_admin') {
    ElMessage.error('超级管理员角色不能删除')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除角色 "${role.name}" 吗？`,
      '删除角色',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    roles.value = roles.value.filter(r => r.code !== role.code)
    if (currentRole.value?.code === role.code) {
      currentRole.value = null
    }
    ElMessage.success('角色删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除角色失败')
    }
  }
}

// 全选权限
const handleSelectAll = () => {
  const allPermissionIds = getAllPermissionIds(permissionTreeData)
  nextTick(() => {
    permissionTreeRef.value?.setCheckedKeys(allPermissionIds)
  })
}

// 全不选权限
const handleDeselectAll = () => {
  permissionTreeRef.value?.setCheckedKeys([])
}

// 获取所有权限ID
const getAllPermissionIds = (nodes) => {
  let ids = []
  nodes.forEach(node => {
    ids.push(node.id)
    if (node.children) {
      ids = ids.concat(getAllPermissionIds(node.children))
    }
  })
  return ids
}

// 权限变化处理
const handlePermissionChange = () => {
  // 可以在这里添加权限变化的逻辑
}

// 保存权限
const handleSavePermissions = async () => {
  if (!currentRole.value) return

  saveLoading.value = true
  try {
    const checkedKeys = permissionTreeRef.value?.getCheckedKeys() || []
    const halfCheckedKeys = permissionTreeRef.value?.getHalfCheckedKeys() || []
    
    // 合并选中的权限
    const allPermissions = [...checkedKeys, ...halfCheckedKeys]
    
    // 更新角色权限
    const roleIndex = roles.value.findIndex(r => r.code === currentRole.value.code)
    if (roleIndex !== -1) {
      roles.value[roleIndex].permissions = allPermissions
    }
    
    ElMessage.success('权限保存成功')
  } catch (error) {
    ElMessage.error('权限保存失败')
  } finally {
    saveLoading.value = false
  }
}

// 提交角色表单
const handleSubmit = async (formData) => {
  try {
    if (formData.id) {
      // 编辑
      const index = roles.value.findIndex(r => r.id === formData.id)
      if (index !== -1) {
        roles.value[index] = { ...roles.value[index], ...formData }
      }
    } else {
      // 新增
      const newRole = {
        id: Date.now(),
        ...formData,
        code: formData.code.toLowerCase().replace(/\s+/g, '_'),
        userCount: 0,
        permissions: []
      }
      roles.value.push(newRole)
    }
    
    dialogVisible.value = false
    ElMessage.success(formData.id ? '角色更新成功' : '角色创建成功')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 加载角色数据
const loadRoles = async () => {
  loading.value = true
  try {
    // 模拟角色数据
    roles.value = [
      {
        id: 1,
        name: '超级管理员',
        code: 'super_admin',
        description: '拥有系统所有权限',
        userCount: 2,
        permissions: Object.values(PERMISSIONS),
        level: 1
      },
      {
        id: 2,
        name: '管理员',
        code: 'admin',
        description: '系统管理员，除系统配置外所有权限',
        userCount: 5,
        permissions: [
          PERMISSIONS.USER_VIEW, PERMISSIONS.USER_CREATE, PERMISSIONS.USER_UPDATE,
          PERMISSIONS.ROLE_VIEW, PERMISSIONS.ROLE_CREATE, PERMISSIONS.ROLE_UPDATE,
          PERMISSIONS.PRODUCT_VIEW, PERMISSIONS.PRODUCT_CREATE, PERMISSIONS.PRODUCT_UPDATE,
          PERMISSIONS.ORDER_VIEW, PERMISSIONS.ORDER_CREATE, PERMISSIONS.ORDER_UPDATE,
          PERMISSIONS.CUSTOMER_VIEW, PERMISSIONS.CUSTOMER_CREATE, PERMISSIONS.CUSTOMER_UPDATE
        ],
        level: 2
      },
      {
        id: 3,
        name: '业务经理',
        code: 'manager',
        description: '业务部门经理，管理销售团队',
        userCount: 8,
        permissions: [
          PERMISSIONS.USER_VIEW,
          PERMISSIONS.PRODUCT_VIEW,
          PERMISSIONS.ORDER_VIEW, PERMISSIONS.ORDER_CREATE, PERMISSIONS.ORDER_UPDATE,
          PERMISSIONS.CUSTOMER_VIEW, PERMISSIONS.CUSTOMER_CREATE, PERMISSIONS.CUSTOMER_UPDATE
        ],
        level: 3
      },
      {
        id: 4,
        name: '销售人员',
        code: 'sales',
        description: '销售业务人员',
        userCount: 15,
        permissions: [
          PERMISSIONS.PRODUCT_VIEW,
          PERMISSIONS.ORDER_VIEW, PERMISSIONS.ORDER_CREATE,
          PERMISSIONS.CUSTOMER_VIEW, PERMISSIONS.CUSTOMER_CREATE
        ],
        level: 4
      }
    ]
  } catch (error) {
    ElMessage.error('加载角色数据失败')
  } finally {
    loading.value = false
  }
}

// 生命周期
onMounted(() => {
  loadRoles()
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

.role-section {
  background: #fff;
  padding: 20px;
  margin: 0 20px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  
  .role-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      font-size: 18px;
      color: #303133;
    }
  }
  
  .role-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
    
    .role-card {
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      padding: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      
      &:hover {
        border-color: #409eff;
        box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.2);
      }
      
      &.role-card-active {
        border-color: #409eff;
        background-color: #f0f9ff;
      }
      
      .role-header-card {
        display: flex;
        align-items: center;
        margin-bottom: 16px;
        
        .role-icon {
          width: 48px;
          height: 48px;
          background: #409eff;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          color: #fff;
        }
        
        .role-info {
          flex: 1;
          
          h4 {
            margin: 0 0 4px 0;
            font-size: 16px;
            color: #303133;
          }
          
          p {
            margin: 0;
            font-size: 12px;
            color: #909399;
          }
        }
      }
      
      .role-stats {
        display: flex;
        justify-content: space-between;
        margin-bottom: 16px;
        
        .stat-item {
          text-align: center;
          
          .stat-label {
            display: block;
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }
          
          .stat-value {
            display: block;
            font-size: 18px;
            font-weight: bold;
            color: #303133;
          }
        }
      }
      
      .role-actions {
        display: flex;
        justify-content: space-around;
        border-top: 1px solid #e4e7ed;
        padding-top: 12px;
      }
    }
  }
}

.permission-section {
  background: #fff;
  padding: 20px;
  margin: 0 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  
  .permission-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      font-size: 18px;
      color: #303133;
    }
  }
  
  .permission-tree {
    :deep(.el-tree-node__content) {
      height: 48px;
      padding: 8px 0;
    }
    
    .permission-node {
      flex: 1;
      
      .permission-info {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 4px;
        
        .permission-icon {
          color: #409eff;
        }
        
        .permission-label {
          font-weight: 500;
          color: #303133;
        }
      }
      
      .permission-desc {
        font-size: 12px;
        color: #909399;
        margin-left: 28px;
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .role-grid {
    grid-template-columns: 1fr !important;
  }
  
  .permission-header {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start !important;
  }
}
</style>