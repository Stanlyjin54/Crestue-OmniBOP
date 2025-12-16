<template>
  <el-dialog
    v-model="dialogVisible"
    title="租户详情"
    width="900px"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="tenant-detail">
      <!-- 租户基本信息 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><OfficeBuilding /></el-icon>
          基本信息
        </h3>
        
        <el-row :gutter="20" class="info-grid">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">租户名称:</span>
              <span class="info-value">{{ tenantData.tenantName }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">租户类型:</span>
              <el-tag :type="getTenantTypeTagType(tenantData.tenantType)" size="small">
                {{ TENANT_TYPES[tenantData.tenantType]?.label || '未知' }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">联系人邮箱:</span>
              <span class="info-value">{{ tenantData.contactEmail || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">联系电话:</span>
              <span class="info-value">{{ tenantData.contactPhone || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">公司名称:</span>
              <span class="info-value">{{ tenantData.companyName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">行业:</span>
              <span class="info-value">{{ tenantData.industry || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <span class="info-label">地址:</span>
              <span class="info-value">{{ tenantData.address || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="24">
            <div class="info-item">
              <span class="info-label">描述:</span>
              <span class="info-value">{{ tenantData.description || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 状态信息 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><StatusMessage /></el-icon>
          状态信息
        </h3>
        
        <el-row :gutter="20" class="info-grid">
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">当前状态:</span>
              <el-tag :type="getStatusTagType(tenantData.status)" size="large">
                {{ TENANT_STATUS[tenantData.status]?.label || '未知' }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">创建时间:</span>
              <span class="info-value">{{ formatDateTime(tenantData.createdAt) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <span class="info-label">最后更新:</span>
              <span class="info-value">{{ formatDateTime(tenantData.updatedAt) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">租户ID:</span>
              <span class="info-value">{{ tenantData.tenantId }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">隔离级别:</span>
              <el-tag type="info" size="small">
                {{ ISOLATION_LEVELS[tenantData.isolationLevel]?.label || tenantData.isolationLevel }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 使用统计 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><TrendCharts /></el-icon>
          使用统计
        </h3>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ tenantData.stats?.userCount || 0 }}</div>
              <div class="stat-label">当前用户数</div>
              <div class="stat-limit">/ {{ tenantData.quota?.maxUsers || 0 }}</div>
              <el-progress 
                :percentage="getUsagePercentage(tenantData.stats?.userCount || 0, tenantData.quota?.maxUsers || 0)" 
                :color="getProgressColor(getUsagePercentage(tenantData.stats?.userCount || 0, tenantData.quota?.maxUsers || 0))"
                :show-text="false"
                :stroke-width="6"
              />
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ formatStorage(tenantData.stats?.storageUsed || 0) }}</div>
              <div class="stat-label">存储使用</div>
              <div class="stat-limit">/ {{ formatStorage(tenantData.quota?.maxStorage || 0) }}</div>
              <el-progress 
                :percentage="getUsagePercentage(tenantData.stats?.storageUsed || 0, tenantData.quota?.maxStorage || 0)" 
                :color="getProgressColor(getUsagePercentage(tenantData.stats?.storageUsed || 0, tenantData.quota?.maxStorage || 0))"
                :show-text="false"
                :stroke-width="6"
              />
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ formatNumber(tenantData.stats?.apiCalls || 0) }}</div>
              <div class="stat-label">API调用</div>
              <div class="stat-limit">/ {{ formatNumber(tenantData.quota?.maxApiCalls || 0) }}</div>
              <el-progress 
                :percentage="getUsagePercentage(tenantData.stats?.apiCalls || 0, tenantData.quota?.maxApiCalls || 0)" 
                :color="getProgressColor(getUsagePercentage(tenantData.stats?.apiCalls || 0, tenantData.quota?.maxApiCalls || 0))"
                :show-text="false"
                :stroke-width="6"
              />
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-number">{{ tenantData.stats?.projectCount || 0 }}</div>
              <div class="stat-label">项目数量</div>
              <div class="stat-limit">/ {{ tenantData.quota?.maxProjects || 0 }}</div>
              <el-progress 
                :percentage="getUsagePercentage(tenantData.stats?.projectCount || 0, tenantData.quota?.maxProjects || 0)" 
                :color="getProgressColor(getUsagePercentage(tenantData.stats?.projectCount || 0, tenantData.quota?.maxProjects || 0))"
                :show-text="false"
                :stroke-width="6"
              />
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 配额设置 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><Setting /></el-icon>
          配额设置
        </h3>
        
        <el-row :gutter="20" class="info-grid">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">最大用户数:</span>
              <span class="info-value">{{ tenantData.quota?.maxUsers || 0 }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">存储空间:</span>
              <span class="info-value">{{ formatStorage(tenantData.quota?.maxStorage || 0) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">API调用次数:</span>
              <span class="info-value">{{ formatNumber(tenantData.quota?.maxApiCalls || 0) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">最大项目数:</span>
              <span class="info-value">{{ tenantData.quota?.maxProjects || 0 }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 功能特性 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><Grid /></el-icon>
          功能特性
        </h3>
        
        <div class="features-grid">
          <el-tag
            v-for="feature in tenantData.features || []"
            :key="feature"
            :type="getFeatureTagType(feature)"
            class="feature-tag"
            size="large"
          >
            {{ getFeatureLabel(feature) }}
          </el-tag>
          <el-tag v-if="!tenantData.features || tenantData.features.length === 0" type="info">
            暂无功能特性
          </el-tag>
        </div>
      </div>

      <!-- 结算设置 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><CreditCard /></el-icon>
          结算设置
        </h3>
        
        <el-row :gutter="20" class="info-grid">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">计费周期:</span>
              <span class="info-value">
                {{ tenantData.billingCycle === 'monthly' ? '月付' : tenantData.billingCycle === 'yearly' ? '年付' : '-' }}
              </span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">自动续费:</span>
              <el-switch 
                v-model="tenantData.autoRenew" 
                disabled 
                :active-text="tenantData.autoRenew ? '已启用' : '未启用'"
                :inactive-text="tenantData.autoRenew ? '已启用' : '未启用'"
              />
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button 
          v-if="canEdit"
          type="primary" 
          @click="handleEdit"
        >
          编辑
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { 
  OfficeBuilding, 
  StatusMessage, 
  TrendCharts, 
  Setting, 
  Grid, 
  CreditCard 
} from '@element-plus/icons-vue'

import { useTenant } from '@/composables/useTenant'

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

const emit = defineEmits(['update:modelValue', 'edit'])

// 使用租户管理组合式函数
const { TENANT_TYPES, TENANT_STATUS, ISOLATION_LEVELS, getTenant } = useTenant()

// 响应式数据
const loading = ref(false)
const tenantData = reactive({})

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

/**
 * 是否可以编辑
 */
const canEdit = computed(() => {
  return tenantData.tenantType !== 'SYSTEM'
})

// 监听对话框显示，更新租户数据
watch(() => props.modelValue, (visible) => {
  if (visible) {
    Object.assign(tenantData, props.tenantData)
  }
})

// 方法

/**
 * 处理关闭对话框
 */
const handleClose = () => {
  emit('update:modelValue', false)
}

/**
 * 处理编辑按钮
 */
const handleEdit = () => {
  emit('edit', tenantData)
  handleClose()
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
 * 获取状态标签类型
 */
const getStatusTagType = (status) => {
  const statusMap = {
    ACTIVE: 'success',
    INACTIVE: 'warning',
    SUSPENDED: 'danger',
    PENDING: 'info'
  }
  return statusMap[status] || 'info'
}

/**
 * 获取功能特性标签类型
 */
const getFeatureTagType = (feature) => {
  const typeMap = {
    full_access: 'success',
    custom_branding: 'warning',
    api_access: 'primary',
    priority_support: 'danger',
    advanced_analytics: 'info',
    custom_integrations: 'success',
    standard_access: 'primary',
    basic_api: 'info',
    email_support: 'warning',
    basic_access: 'info',
    limited_api: 'warning',
    trial_access: 'info',
    limited_features: 'warning'
  }
  return typeMap[feature] || 'info'
}

/**
 * 获取功能特性标签文本
 */
const getFeatureLabel = (feature) => {
  const labelMap = {
    full_access: '完整功能访问',
    custom_branding: '自定义品牌',
    api_access: 'API访问',
    priority_support: '优先支持',
    advanced_analytics: '高级分析',
    custom_integrations: '自定义集成',
    standard_access: '标准访问',
    basic_api: '基础API',
    email_support: '邮件支持',
    basic_access: '基础访问',
    limited_api: '有限API',
    trial_access: '试用访问',
    limited_features: '有限功能'
  }
  return labelMap[feature] || feature
}

/**
 * 格式化日期时间
 */
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

/**
 * 格式化存储大小
 */
const formatStorage = (bytes) => {
  if (!bytes || bytes === 0) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let size = bytes
  let unitIndex = 0
  
  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024
    unitIndex++
  }
  
  return `${size.toFixed(1)} ${units[unitIndex]}`
}

/**
 * 格式化数字
 */
const formatNumber = (num) => {
  if (!num || num === 0) return '0'
  if (num >= 1000000) {
    return `${(num / 1000000).toFixed(1)}M`
  } else if (num >= 1000) {
    return `${(num / 1000).toFixed(1)}K`
  }
  return num.toString()
}

/**
 * 计算使用率百分比
 */
const getUsagePercentage = (used, total) => {
  if (!total || total === 0) return 0
  return Math.min(Math.round((used / total) * 100), 100)
}

/**
 * 获取进度条颜色
 */
const getProgressColor = (percentage) => {
  if (percentage >= 90) return '#f56c6c'
  if (percentage >= 70) return '#e6a23c'
  if (percentage >= 50) return '#409eff'
  return '#67c23a'
}
</script>

<style scoped>
.tenant-detail {
  max-height: 70vh;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 30px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409EFF;
}

.section-title .el-icon {
  color: #409EFF;
}

.info-grid {
  gap: 15px;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-label {
  font-weight: 500;
  color: #606266;
  min-width: 120px;
  margin-right: 15px;
}

.info-value {
  color: #303133;
  flex: 1;
}

.stat-card {
  background: #f8f9fa;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
}

.stat-limit {
  font-size: 12px;
  color: #909399;
  margin-bottom: 15px;
}

.features-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.feature-tag {
  margin: 0;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-progress) {
  margin-top: 10px;
}

:deep(.el-switch.is-disabled) {
  opacity: 1;
}
</style>