<template>
  <div class="plugin-page">
    <base-wrapper>
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <div class="header-title">
            <el-icon class="header-icon"><Box /></el-icon>
            <h1>插件管理</h1>
          </div>
          <p class="header-description">管理系统插件的安装、卸载、启用、禁用等操作</p>
        </div>
      </div>

      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>插件管理</el-breadcrumb-item>
      </el-breadcrumb>

      <!-- 操作按钮 -->
      <div class="action-bar">
        <el-button type="primary" @click="handleInstall" :loading="loading">
          <el-icon><Upload /></el-icon>
          安装插件
        </el-button>
        <el-button @click="handleRefresh" :loading="loading">
          <el-icon><Refresh /></el-icon>
          刷新列表
        </el-button>
      </div>

      <!-- 插件列表 -->
      <el-card class="plugin-card">
        <div class="plugin-list">
          <el-table v-loading="loading" :data="plugins" style="width: 100%">
            <el-table-column prop="name" label="插件名称" width="200" />
            <el-table-column prop="id" label="插件ID" width="180" />
            <el-table-column prop="version" label="版本" width="100" />
            <el-table-column prop="author" label="作者" width="120" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="status" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="installTime" label="安装时间" width="180" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="scope">
                <el-button
                  type="success"
                  size="small"
                  v-if="scope.row.status === PLUGIN_STATUS.INSTALLED || scope.row.status === PLUGIN_STATUS.DISABLED"
                  @click="handleEnable(scope.row)"
                  :loading="loading"
                >
                  <el-icon><Check /></el-icon>
                  启用
                </el-button>
                <el-button
                  type="warning"
                  size="small"
                  v-else-if="scope.row.status === PLUGIN_STATUS.ENABLED"
                  @click="handleDisable(scope.row)"
                  :loading="loading"
                >
                  <el-icon><Close /></el-icon>
                  禁用
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="handleUninstall(scope.row)"
                  :loading="loading"
                >
                  <el-icon><Delete /></el-icon>
                  卸载
                </el-button>
                <el-button
                  type="primary"
                  size="small"
                  @click="handleDetail(scope.row)"
                  :loading="loading"
                >
                  <el-icon><View /></el-icon>
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>

      <!-- 安装插件对话框 -->
      <el-dialog
        v-model="installDialogVisible"
        title="安装插件"
        width="500px"
        :close-on-click-modal="false"
      >
        <el-upload
          ref="uploadRef"
          class="plugin-upload"
          action=""
          :http-request="handlePluginUpload"
          :before-upload="beforePluginUpload"
          :on-success="onPluginUploadSuccess"
          :on-error="onPluginUploadError"
          accept=".jar"
          drag
        >
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">
            拖放插件包到此处，或
            <em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              仅支持 .jar 格式的插件包，大小不超过 100MB
            </div>
          </template>
        </el-upload>
        
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="installDialogVisible = false">取消</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 插件详情对话框 -->
      <el-dialog
        v-model="detailDialogVisible"
        title="插件详情"
        width="600px"
      >
        <div v-if="currentPlugin" class="plugin-detail">
          <el-descriptions border :column="2">
            <el-descriptions-item label="插件名称">{{ currentPlugin.name }}</el-descriptions-item>
            <el-descriptions-item label="插件ID">{{ currentPlugin.id }}</el-descriptions-item>
            <el-descriptions-item label="版本">{{ currentPlugin.version }}</el-descriptions-item>
            <el-descriptions-item label="作者">{{ currentPlugin.author }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ getStatusText(currentPlugin.status) }}</el-descriptions-item>
            <el-descriptions-item label="安装时间">{{ currentPlugin.installTime }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ currentPlugin.updateTime }}</el-descriptions-item>
            <el-descriptions-item label="最后启用时间">{{ currentPlugin.lastEnableTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="描述" :span="2">{{ currentPlugin.description }}</el-descriptions-item>
            <el-descriptions-item label="依赖" :span="2">
              <el-tag v-for="dep in currentPlugin.dependencies" :key="dep" size="small" style="margin-right: 5px;">{{ dep }}</el-tag>
              <span v-if="!currentPlugin.dependencies || currentPlugin.dependencies.length === 0">无</span>
            </el-descriptions-item>
          </el-descriptions>
        </div>
        
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="detailDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </base-wrapper>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

// 插件状态枚举
const PLUGIN_STATUS = {
  UNINSTALLED: 0,
  INSTALLED: 1,
  ENABLED: 2,
  DISABLED: 3
}

// 插件API（模拟数据，实际项目中应替换为真实API调用）
const plugins = ref([
  {
    id: 'test-plugin-001',
    name: '测试插件1',
    version: '1.0.0',
    description: '这是一个测试插件，用于演示插件系统功能',
    author: 'kuafuai',
    status: PLUGIN_STATUS.ENABLED,
    dependencies: [],
    installTime: '2025-12-14 10:00:00',
    updateTime: '2025-12-14 10:00:00',
    lastEnableTime: '2025-12-14 10:00:00'
  },
  {
    id: 'test-plugin-002',
    name: '测试插件2',
    version: '2.0.0',
    description: '这是第二个测试插件，用于演示插件依赖功能',
    author: 'kuafuai',
    status: PLUGIN_STATUS.DISABLED,
    dependencies: ['test-plugin-001'],
    installTime: '2025-12-14 11:00:00',
    updateTime: '2025-12-14 11:00:00',
    lastEnableTime: '2025-12-14 11:30:00'
  }
])

// 响应式数据
const loading = ref(false)
const installDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentPlugin = ref(null)
const uploadRef = ref(null)

// 方法
const getStatusText = (status) => {
  const statusMap = {
    [PLUGIN_STATUS.UNINSTALLED]: '未安装',
    [PLUGIN_STATUS.INSTALLED]: '已安装',
    [PLUGIN_STATUS.ENABLED]: '已启用',
    [PLUGIN_STATUS.DISABLED]: '已禁用'
  }
  return statusMap[status] || '未知'
}

const getStatusType = (status) => {
  const typeMap = {
    [PLUGIN_STATUS.UNINSTALLED]: '',
    [PLUGIN_STATUS.INSTALLED]: 'info',
    [PLUGIN_STATUS.ENABLED]: 'success',
    [PLUGIN_STATUS.DISABLED]: 'warning'
  }
  return typeMap[status] || ''
}

const handleEnable = (plugin) => {
  loading.value = true
  // TODO: 调用启用插件API
  setTimeout(() => {
    plugin.status = PLUGIN_STATUS.ENABLED
    plugin.lastEnableTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
    ElMessage.success('插件启用成功')
    loading.value = false
  }, 500)
}

const handleDisable = (plugin) => {
  loading.value = true
  // TODO: 调用禁用插件API
  setTimeout(() => {
    plugin.status = PLUGIN_STATUS.DISABLED
    ElMessage.success('插件禁用成功')
    loading.value = false
  }, 500)
}

const handleUninstall = (plugin) => {
  loading.value = true
  // TODO: 调用卸载插件API
  setTimeout(() => {
    const index = plugins.value.findIndex(p => p.id === plugin.id)
    if (index > -1) {
      plugins.value.splice(index, 1)
    }
    ElMessage.success('插件卸载成功')
    loading.value = false
  }, 500)
}

const handleDetail = (plugin) => {
  currentPlugin.value = plugin
  detailDialogVisible.value = true
}

const handleInstall = () => {
  installDialogVisible.value = true
}

const handleRefresh = () => {
  loading.value = true
  // TODO: 调用刷新插件列表API
  setTimeout(() => {
    ElMessage.success('插件列表已刷新')
    loading.value = false
  }, 500)
}

const handlePluginUpload = (options) => {
  loading.value = true
  // TODO: 调用插件上传API
  setTimeout(() => {
    options.onSuccess({})
  }, 1000)
}

const beforePluginUpload = (file) => {
  const isJar = file.type === 'application/java-archive' || file.name.endsWith('.jar')
  const isLt100M = file.size / 1024 / 1024 < 100

  if (!isJar) {
    ElMessage.error('只能上传 JAR 格式的插件包')
    return false
  }
  if (!isLt100M) {
    ElMessage.error('插件包大小不能超过 100MB')
    return false
  }
  return true
}

const onPluginUploadSuccess = (response) => {
  ElMessage.success('插件安装成功')
  installDialogVisible.value = false
  uploadRef.value.clearFiles()
  handleRefresh()
  loading.value = false
}

const onPluginUploadError = (error) => {
  ElMessage.error('插件安装失败')
  loading.value = false
}

// 生命周期钩子
onMounted(() => {
  // TODO: 调用获取插件列表API
})
</script>

<style scoped>
.plugin-page {
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

.plugin-card {
  margin-bottom: 20px;
}

.plugin-list {
  width: 100%;
}

.info-icon {
  margin-left: 8px;
  font-size: 16px;
  color: var(--el-color-info);
  cursor: help;
}

.plugin-upload {
  margin: 20px 0;
}

.plugin-detail {
  margin: 20px 0;
}
</style>
