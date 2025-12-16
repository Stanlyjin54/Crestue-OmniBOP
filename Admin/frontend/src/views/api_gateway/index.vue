<template>
  <div class="api-gateway-page">
    <base-wrapper>
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <div class="header-title">
            <el-icon class="header-icon"><Grid /></el-icon>
            <h1>API网关管理</h1>
          </div>
          <p class="header-description">管理API路由、认证授权、限流规则和监控数据</p>
        </div>
      </div>

      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>API网关管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 操作按钮 -->
      <div class="action-bar">
        <el-button type="primary" @click="handleCreate" :loading="loading">
          <el-icon><Plus /></el-icon>
          创建路由
        </el-button>
        <el-button @click="handleRefresh" :loading="loading">
          <el-icon><Refresh /></el-icon>
          刷新列表
        </el-button>
      </div>

      <!-- 路由列表 -->
      <el-card class="route-card">
        <div class="route-list">
          <el-table v-loading="loading" :data="routes" style="width: 100%">
            <el-table-column prop="path" label="路由路径" width="250" />
            <el-table-column prop="method" label="请求方法" width="120">
              <template #default="scope">
                <el-tag type="info">{{ scope.row.method }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="targetUrl" label="目标URL" width="300" />
            <el-table-column prop="authType" label="认证方式" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.authType === 'none' ? 'success' : 'warning'">
                  {{ getAuthTypeText(scope.row.authType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="enabled" label="状态" width="120">
              <template #default="scope">
                <el-switch
                  v-model="scope.row.enabled"
                  @change="handleEnableChange(scope.row)"
                  :loading="loading"
                />
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column prop="updateTime" label="更新时间" width="180" />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  @click="handleEdit(scope.row)"
                  :loading="loading"
                >
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="handleDelete(scope.row)"
                  :loading="loading"
                >
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
                <el-button
                  type="success"
                  size="small"
                  @click="handleRateLimit(scope.row)"
                  :loading="loading"
                >
                  <el-icon><DataAnalysis /></el-icon>
                  限流
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>

      <!-- 创建/编辑路由对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="isEdit ? '编辑路由' : '创建路由'"
        width="600px"
        :close-on-click-modal="false"
      >
        <el-form
          ref="routeFormRef"
          :model="routeForm"
          label-width="120px"
          :rules="routeRules"
          class="route-form"
        >
          <el-form-item label="路由路径" prop="path">
            <el-input
              v-model="routeForm.path"
              placeholder="请输入路由路径，如：/api/proxy/*"
              maxlength="255"
            />
          </el-form-item>

          <el-form-item label="请求方法" prop="method">
            <el-select v-model="routeForm.method" placeholder="请选择请求方法">
              <el-option label="GET" value="GET" />
              <el-option label="POST" value="POST" />
              <el-option label="PUT" value="PUT" />
              <el-option label="DELETE" value="DELETE" />
              <el-option label="*" value="*" />
            </el-select>
          </el-form-item>

          <el-form-item label="目标URL" prop="targetUrl">
            <el-input
              v-model="routeForm.targetUrl"
              placeholder="请输入目标URL，如：http://localhost:8080"
              maxlength="255"
            />
          </el-form-item>

          <el-form-item label="认证方式" prop="authType">
            <el-select v-model="routeForm.authType" placeholder="请选择认证方式">
              <el-option label="无认证" value="none" />
              <el-option label="JWT" value="jwt" />
              <el-option label="OAuth2.0" value="oauth2" />
            </el-select>
          </el-form-item>

          <el-form-item label="负载均衡策略" prop="loadBalanceStrategy">
            <el-select v-model="routeForm.loadBalanceStrategy" placeholder="请选择负载均衡策略">
              <el-option label="轮询" value="round_robin" />
              <el-option label="随机" value="random" />
              <el-option label="加权轮询" value="weighted_round_robin" />
            </el-select>
          </el-form-item>

          <el-form-item label="超时时间（毫秒）" prop="timeout">
            <el-input-number
              v-model="routeForm.timeout"
              :min="1000"
              :max="60000"
              :step="1000"
              placeholder="请输入超时时间"
            />
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="loading">
              {{ isEdit ? '更新' : '创建' }}
            </el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 限流配置对话框 -->
      <el-dialog
        v-model="rateLimitDialogVisible"
        title="限流配置"
        width="500px"
        :close-on-click-modal="false"
      >
        <el-form
          ref="rateLimitFormRef"
          :model="rateLimitForm"
          label-width="120px"
          class="rate-limit-form"
        >
          <el-form-item label="是否启用">
            <el-switch v-model="rateLimitForm.enabled" />
          </el-form-item>

          <el-form-item label="限流类型">
            <el-select v-model="rateLimitForm.type" placeholder="请选择限流类型">
              <el-option label="按API" value="api" />
              <el-option label="按IP" value="ip" />
              <el-option label="按用户" value="user" />
            </el-select>
          </el-form-item>

          <el-form-item label="每秒请求数">
            <el-input-number
              v-model="rateLimitForm.requestsPerSecond"
              :min="1"
              :max="10000"
              :step="10"
              placeholder="请输入每秒允许的请求数"
            />
          </el-form-item>

          <el-form-item label="突发请求数">
            <el-input-number
              v-model="rateLimitForm.burstRequests"
              :min="1"
              :max="10000"
              :step="10"
              placeholder="请输入突发请求数"
            />
          </el-form-item>

          <el-form-item label="限流窗口（秒）">
            <el-input-number
              v-model="rateLimitForm.windowSize"
              :min="1"
              :max="60"
              :step="1"
              placeholder="请输入限流窗口大小"
            />
          </el-form-item>
        </el-form>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="rateLimitDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleRateLimitSubmit" :loading="loading">
              保存
            </el-button>
          </span>
        </template>
      </el-dialog>
    </base-wrapper>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// API路由数据（模拟数据，实际项目中应替换为真实API调用）
const routes = ref([
  {
    routeId: 'route_001',
    path: '/api/proxy/*',
    targetUrl: 'http://localhost:8080',
    method: '*',
    authType: 'none',
    enabled: true,
    loadBalanceStrategy: 'round_robin',
    timeout: 30000,
    createTime: '2025-12-14 10:00:00',
    updateTime: '2025-12-14 10:00:00'
  },
  {
    routeId: 'route_002',
    path: '/api/test',
    targetUrl: 'http://localhost:8080/test',
    method: 'GET',
    authType: 'jwt',
    enabled: true,
    loadBalanceStrategy: 'random',
    timeout: 30000,
    createTime: '2025-12-14 11:00:00',
    updateTime: '2025-12-14 11:30:00'
  }
])

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const rateLimitDialogVisible = ref(false)
const isEdit = ref(false)
const currentRoute = ref(null)
const routeFormRef = ref(null)
const rateLimitFormRef = ref(null)

// 表单数据
const routeForm = reactive({
  routeId: '',
  path: '',
  method: '*',
  targetUrl: '',
  authType: 'none',
  loadBalanceStrategy: 'round_robin',
  timeout: 30000,
  enabled: true
})

const rateLimitForm = reactive({
  enabled: false,
  type: 'api',
  requestsPerSecond: 100,
  burstRequests: 100,
  windowSize: 1
})

// 表单验证规则
const routeRules = {
  path: [{ required: true, message: '请输入路由路径', trigger: 'blur' }],
  targetUrl: [{ required: true, message: '请输入目标URL', trigger: 'blur' }],
  method: [{ required: true, message: '请选择请求方法', trigger: 'change' }]
}

// 方法
const getAuthTypeText = (authType) => {
  const authTypeMap = {
    'none': '无认证',
    'jwt': 'JWT',
    'oauth2': 'OAuth2.0'
  }
  return authTypeMap[authType] || '未知'
}

const handleEnableChange = (route) => {
  loading.value = true
  // TODO: 调用启用/禁用路由API
  setTimeout(() => {
    ElMessage.success(`路由已${route.enabled ? '启用' : '禁用'}`)
    route.updateTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
    loading.value = false
  }, 500)
}

const handleCreate = () => {
  isEdit.value = false
  // 重置表单
  Object.assign(routeForm, {
    routeId: '',
    path: '',
    method: '*',
    targetUrl: '',
    authType: 'none',
    loadBalanceStrategy: 'round_robin',
    timeout: 30000,
    enabled: true
  })
  dialogVisible.value = true
}

const handleEdit = (route) => {
  isEdit.value = true
  currentRoute.value = route
  // 填充表单数据
  Object.assign(routeForm, route)
  dialogVisible.value = true
}

const handleDelete = (route) => {
  ElMessageBox.confirm('确定要删除这条路由吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    loading.value = true
    // TODO: 调用删除路由API
    setTimeout(() => {
      const index = routes.value.findIndex(r => r.routeId === route.routeId)
      if (index > -1) {
        routes.value.splice(index, 1)
      }
      ElMessage.success('路由删除成功')
      loading.value = false
    }, 500)
  }).catch(() => {
    // 取消删除
  })
}

const handleRateLimit = (route) => {
  currentRoute.value = route
  // 填充限流配置
  Object.assign(rateLimitForm, {
    enabled: true,
    type: 'api',
    requestsPerSecond: 100,
    burstRequests: 100,
    windowSize: 1
  })
  rateLimitDialogVisible.value = true
}

const handleSubmit = async () => {
  if (!await routeFormRef.value.validate()) {
    return
  }
  
  loading.value = true
  const now = new Date().toISOString().slice(0, 19).replace('T', ' ')
  
  if (isEdit.value) {
    // 更新路由
    // TODO: 调用更新路由API
    setTimeout(() => {
      const index = routes.value.findIndex(r => r.routeId === routeForm.routeId)
      if (index > -1) {
        routes.value[index] = { ...routeForm, updateTime: now }
      }
      ElMessage.success('路由更新成功')
      dialogVisible.value = false
      loading.value = false
    }, 500)
  } else {
    // 创建路由
    // TODO: 调用创建路由API
    setTimeout(() => {
      const newRoute = {
        ...routeForm,
        routeId: 'route_' + Date.now(),
        createTime: now,
        updateTime: now
      }
      routes.value.push(newRoute)
      ElMessage.success('路由创建成功')
      dialogVisible.value = false
      loading.value = false
    }, 500)
  }
}

const handleRateLimitSubmit = () => {
  loading.value = true
  // TODO: 调用更新限流配置API
  setTimeout(() => {
    ElMessage.success('限流配置保存成功')
    rateLimitDialogVisible.value = false
    loading.value = false
  }, 500)
}

const handleRefresh = () => {
  loading.value = true
  // TODO: 调用刷新路由列表API
  setTimeout(() => {
    ElMessage.success('路由列表已刷新')
    loading.value = false
  }, 500)
}

// 生命周期钩子
onMounted(() => {
  // TODO: 调用获取路由列表API
})
</script>

<style scoped>
.api-gateway-page {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-icon {
  font-size: 28px;
  color: var(--el-color-primary);
}

.header-title h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.header-description {
  margin: 0;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.action-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.route-card {
  margin-bottom: 20px;
}

.route-list {
  width: 100%;
}

.route-form {
  margin: 20px 0;
}

.rate-limit-form {
  margin: 20px 0;
}
</style>
