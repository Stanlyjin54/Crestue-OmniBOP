<template>
  <el-dialog
    v-model="dialogVisible"
    title="编辑租户"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      class="tenant-form"
    >
      <!-- 基本信息 -->
      <el-divider content-position="left">基本信息</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="租户名称" prop="tenantName">
            <el-input
              v-model="formData.tenantName"
              placeholder="请输入租户名称"
              maxlength="50"
              show-word-limit
              :disabled="isSystemTenant"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="租户类型" prop="tenantType">
            <el-select v-model="formData.tenantType" placeholder="请选择租户类型" :disabled="isSystemTenant">
              <el-option
                v-for="(type, key) in TENANT_TYPES"
                :key="key"
                :label="type.label"
                :value="type.value"
                :disabled="key === 'SYSTEM'"
              >
                <div class="tenant-type-option">
                  <span class="type-label">{{ type.label }}</span>
                  <span class="type-description">{{ type.description }}</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系人邮箱" prop="contactEmail">
            <el-input
              v-model="formData.contactEmail"
              placeholder="请输入联系人邮箱"
              type="email"
              :disabled="isSystemTenant"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="formData.contactPhone"
              placeholder="请输入联系电话"
              :disabled="isSystemTenant"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="公司名称" prop="companyName">
            <el-input
              v-model="formData.companyName"
              placeholder="请输入公司名称"
              maxlength="100"
              :disabled="isSystemTenant"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="行业">
            <el-select v-model="formData.industry" placeholder="请选择行业" :disabled="isSystemTenant">
              <el-option label="互联网" value="互联网" />
              <el-option label="电商" value="电商" />
              <el-option label="金融" value="金融" />
              <el-option label="制造业" value="制造业" />
              <el-option label="教育" value="教育" />
              <el-option label="医疗" value="医疗" />
              <el-option label="房地产" value="房地产" />
              <el-option label="其他" value="其他" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="地址">
        <el-input
          v-model="formData.address"
          placeholder="请输入公司地址"
          maxlength="200"
          type="textarea"
          :rows="2"
          :disabled="isSystemTenant"
        />
      </el-form-item>

      <el-form-item label="描述">
        <el-input
          v-model="formData.description"
          placeholder="请输入租户描述"
          maxlength="500"
          type="textarea"
          :rows="3"
          show-word-limit
          :disabled="isSystemTenant"
        />
      </el-form-item>

      <!-- 租户状态 -->
      <el-divider content-position="left">状态信息</el-divider>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="租户状态">
            <el-tag :type="getStatusTagType(formData.status)" size="large">
              {{ TENANT_STATUS[formData.status]?.label || '未知' }}
            </el-tag>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间">
            <span class="info-text">{{ formatDateTime(formData.createdAt) }}</span>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="最后更新">
            <span class="info-text">{{ formatDateTime(formData.updatedAt) }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="租户ID">
            <span class="info-text">{{ formData.tenantId }}</span>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 使用统计 -->
      <el-divider content-position="left">使用统计</el-divider>

      <el-row :gutter="20">
        <el-col :span="8">
          <div class="usage-card">
            <div class="usage-number">{{ formData.stats?.userCount || 0 }}</div>
            <div class="usage-label">当前用户数</div>
            <div class="usage-limit">/ {{ formData.quota?.maxUsers || 0 }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="usage-card">
            <div class="usage-number">{{ formatStorage(formData.stats?.storageUsed || 0) }}</div>
            <div class="usage-label">存储使用</div>
            <div class="usage-limit">/ {{ formatStorage(formData.quota?.maxStorage || 0) }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="usage-card">
            <div class="usage-number">{{ formatNumber(formData.stats?.apiCalls || 0) }}</div>
            <div class="usage-label">API调用</div>
            <div class="usage-limit">/ {{ formatNumber(formData.quota?.maxApiCalls || 0) }}</div>
          </div>
        </el-col>
      </el-row>

      <!-- 配额设置 -->
      <el-divider content-position="left">配额设置</el-divider>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="最大用户数" prop="quota.maxUsers">
            <el-input-number
              v-model="formData.quota.maxUsers"
              :min="1"
              :max="getMaxUsersByType(formData.tenantType)"
              :step="10"
              placeholder="最大用户数"
              style="width: 100%"
              :disabled="isSystemTenant"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="存储空间(GB)" prop="quota.maxStorage">
            <el-input-number
              v-model="formData.quota.maxStorage"
              :min="1"
              :max="getMaxStorageByType(formData.tenantType)"
              :step="1"
              placeholder="最大存储空间"
              style="width: 100%"
              :disabled="isSystemTenant"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="API调用次数" prop="quota.maxApiCalls">
            <el-input-number
              v-model="formData.quota.maxApiCalls"
              :min="100"
              :max="getMaxApiCallsByType(formData.tenantType)"
              :step="1000"
              placeholder="最大API调用次数"
              style="width: 100%"
              :disabled="isSystemTenant"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最大项目数" prop="quota.maxProjects">
            <el-input-number
              v-model="formData.quota.maxProjects"
              :min="1"
              :max="getMaxProjectsByType(formData.tenantType)"
              :step="1"
              placeholder="最大项目数"
              style="width: 100%"
              :disabled="isSystemTenant"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 功能特性 -->
      <el-divider content-position="left">功能特性</el-divider>

      <el-form-item label="功能权限">
        <el-checkbox-group v-model="formData.features" :disabled="isSystemTenant">
          <el-checkbox label="full_access">完整功能访问</el-checkbox>
          <el-checkbox label="custom_branding">自定义品牌</el-checkbox>
          <el-checkbox label="api_access">API访问</el-checkbox>
          <el-checkbox label="priority_support">优先支持</el-checkbox>
          <el-checkbox label="advanced_analytics">高级分析</el-checkbox>
          <el-checkbox label="custom_integrations">自定义集成</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <!-- 结算设置 -->
      <el-divider content-position="left">结算设置</el-divider>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计费周期">
            <el-select v-model="formData.billingCycle" placeholder="请选择计费周期" :disabled="isSystemTenant">
              <el-option label="月付" value="monthly" />
              <el-option label="年付" value="yearly" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="自动续费">
            <el-switch v-model="formData.autoRenew" :disabled="isSystemTenant" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 隔离级别 -->
      <el-divider content-position="left">数据隔离</el-divider>

      <el-form-item label="隔离级别">
        <el-radio-group v-model="formData.isolationLevel" :disabled="isSystemTenant">
          <el-radio-button
            v-for="(level, key) in ISOLATION_LEVELS"
            :key="key"
            :label="level.value"
            :disabled="isSystemTenant"
          >
            <div class="isolation-option">
              <div class="option-title">{{ level.label }}</div>
              <div class="option-desc">{{ level.description }}</div>
            </div>
          </el-radio-button>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button @click="handleReset">重置</el-button>
        <el-button
          type="primary"
          :loading="loading"
          :disabled="isSystemTenant"
          @click="handleSubmit"
        >
          保存修改
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'

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

const emit = defineEmits(['update:modelValue', 'success'])

// 使用租户管理组合式函数
const { TENANT_TYPES, TENANT_STATUS, ISOLATION_LEVELS, updateTenant, getTenant } = useTenant()

// 响应式数据
const formRef = ref()
const loading = ref(false)
const originalData = ref({})

const formData = reactive({
  tenantId: '',
  tenantName: '',
  tenantType: 'BASIC',
  contactEmail: '',
  contactPhone: '',
  companyName: '',
  industry: '',
  address: '',
  description: '',
  status: 'ACTIVE',
  createdAt: '',
  updatedAt: '',
  quota: {
    maxUsers: 100,
    maxStorage: 10,
    maxApiCalls: 10000,
    maxProjects: 10
  },
  features: [],
  billingCycle: 'monthly',
  autoRenew: true,
  isolationLevel: 'TABLE',
  stats: {
    userCount: 0,
    storageUsed: 0,
    apiCalls: 0,
    projectCount: 0
  }
})

// 表单验证规则
const formRules = {
  tenantName: [
    { required: true, message: '请输入租户名称', trigger: 'blur' },
    { min: 2, max: 50, message: '租户名称长度为2-50个字符', trigger: 'blur' }
  ],
  tenantType: [
    { required: true, message: '请选择租户类型', trigger: 'change' }
  ],
  contactEmail: [
    { required: true, message: '请输入联系人邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' }
  ],
  companyName: [
    { required: true, message: '请输入公司名称', trigger: 'blur' },
    { min: 2, max: 100, message: '公司名称长度为2-100个字符', trigger: 'blur' }
  ],
  'quota.maxUsers': [
    { required: true, message: '请设置最大用户数', trigger: 'blur' },
    { type: 'number', min: 1, message: '最大用户数至少为1', trigger: 'blur' }
  ],
  'quota.maxStorage': [
    { required: true, message: '请设置存储空间', trigger: 'blur' },
    { type: 'number', min: 1, message: '存储空间至少为1GB', trigger: 'blur' }
  ],
  'quota.maxApiCalls': [
    { required: true, message: '请设置API调用次数', trigger: 'blur' },
    { type: 'number', min: 100, message: 'API调用次数至少为100', trigger: 'blur' }
  ],
  'quota.maxProjects': [
    { required: true, message: '请设置最大项目数', trigger: 'blur' },
    { type: 'number', min: 1, message: '最大项目数至少为1', trigger: 'blur' }
  ]
}

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

/**
 * 是否为系统租户（不可编辑）
 */
const isSystemTenant = computed(() => {
  return formData.tenantType === 'SYSTEM'
})

// 监听对话框显示，获取租户详情
watch(() => props.modelValue, async (visible) => {
  if (visible && props.tenantData.tenantId) {
    await loadTenantDetail(props.tenantData.tenantId)
  }
})

// 监听租户类型变化，自动设置功能特性
watch(() => formData.tenantType, (newType) => {
  const tenantType = TENANT_TYPES[newType]
  if (tenantType && tenantType.features && !isSystemTenant.value) {
    // 根据租户类型设置功能特性，但保留用户可能已修改的设置
    const recommendedFeatures = getRecommendedFeaturesByType(newType)
    // 只在没有自定义设置时自动设置
    if (formData.features.length === 0 || formData.features === recommendedFeatures) {
      formData.features = [...recommendedFeatures]
    }
  }
}, { immediate: true })

// 方法

/**
 * 加载租户详情
 */
const loadTenantDetail = async (tenantId) => {
  try {
    loading.value = true
    const result = await getTenant(tenantId)
    
    if (result.success) {
      const tenant = result.data
      // 保存原始数据
      originalData.value = { ...tenant }
      
      // 更新表单数据
      Object.assign(formData, {
        tenantId: tenant.tenantId,
        tenantName: tenant.tenantName,
        tenantType: tenant.tenantType,
        contactEmail: tenant.contactEmail,
        contactPhone: tenant.contactPhone,
        companyName: tenant.companyName,
        industry: tenant.industry,
        address: tenant.address,
        description: tenant.description,
        status: tenant.status,
        createdAt: tenant.createdAt,
        updatedAt: tenant.updatedAt,
        features: tenant.features || [],
        billingCycle: tenant.billingCycle,
        autoRenew: tenant.autoRenew,
        isolationLevel: tenant.isolationLevel,
        quota: {
          maxUsers: tenant.quota?.maxUsers || 100,
          maxStorage: tenant.quota?.maxStorage || 10,
          maxApiCalls: tenant.quota?.maxApiCalls || 10000,
          maxProjects: tenant.quota?.maxProjects || 10
        },
        stats: {
          userCount: tenant.stats?.userCount || 0,
          storageUsed: tenant.stats?.storageUsed || 0,
          apiCalls: tenant.stats?.apiCalls || 0,
          projectCount: tenant.stats?.projectCount || 0
        }
      })
    }
  } catch (error) {
    console.error('加载租户详情失败:', error)
    ElMessage.error('加载租户详情失败')
  } finally {
    loading.value = false
  }
}

/**
 * 处理关闭对话框
 */
const handleClose = () => {
  emit('update:modelValue', false)
  resetForm()
}

/**
 * 处理重置表单
 */
const handleReset = () => {
  if (originalData.value.tenantId) {
    // 重置为原始数据
    Object.assign(formData, {
      ...originalData.value,
      quota: {
        ...originalData.value.quota
      },
      stats: {
        ...originalData.value.stats
      }
    })
  } else {
    resetForm()
  }
}

/**
 * 重置表单
 */
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  // 重置为默认值
  Object.assign(formData, {
    tenantId: '',
    tenantName: '',
    tenantType: 'BASIC',
    contactEmail: '',
    contactPhone: '',
    companyName: '',
    industry: '',
    address: '',
    description: '',
    status: 'ACTIVE',
    createdAt: '',
    updatedAt: '',
    quota: {
      maxUsers: 100,
      maxStorage: 10,
      maxApiCalls: 10000,
      maxProjects: 10
    },
    features: [],
    billingCycle: 'monthly',
    autoRenew: true,
    isolationLevel: 'TABLE',
    stats: {
      userCount: 0,
      storageUsed: 0,
      apiCalls: 0,
      projectCount: 0
    }
  })
  originalData.value = {}
}

/**
 * 处理提交表单
 */
const handleSubmit = async () => {
  try {
    // 表单验证
    await formRef.value.validate()
    
    // 系统租户不允许修改
    if (isSystemTenant.value) {
      ElMessage.warning('系统租户不允许修改')
      return
    }
    
    loading.value = true
    
    // 构建更新数据
    const updateData = {
      tenantName: formData.tenantName,
      contactEmail: formData.contactEmail,
      contactPhone: formData.contactPhone,
      companyName: formData.companyName,
      industry: formData.industry,
      address: formData.address,
      description: formData.description,
      quota: {
        ...formData.quota,
        // 验证配额不能低于当前使用量
        maxUsers: Math.max(formData.quota.maxUsers, formData.stats.userCount),
        maxStorage: Math.max(formData.quota.maxStorage, formData.stats.storageUsed),
        maxApiCalls: Math.max(formData.quota.maxApiCalls, formData.stats.apiCalls),
        maxProjects: Math.max(formData.quota.maxProjects, formData.stats.projectCount)
      },
      features: formData.features,
      billingCycle: formData.billingCycle,
      autoRenew: formData.autoRenew,
      isolationLevel: formData.isolationLevel
    }
    
    // 检查是否有变更
    const hasChanges = JSON.stringify(updateData) !== JSON.stringify(originalData.value)
    
    if (!hasChanges) {
      ElMessage.info('没有变更需要保存')
      handleClose()
      return
    }
    
    // 更新租户
    const result = await updateTenant(formData.tenantId, updateData)
    
    if (result.success) {
      ElMessage.success('租户信息更新成功')
      emit('success')
      handleClose()
    }
  } catch (error) {
    console.error('更新租户失败:', error)
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    loading.value = false
  }
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
 * 根据租户类型获取推荐功能特性
 */
const getRecommendedFeaturesByType = (tenantType) => {
  const features = {
    ENTERPRISE: ['full_access', 'custom_branding', 'api_access', 'priority_support', 'advanced_analytics'],
    PROFESSIONAL: ['standard_access', 'basic_api', 'email_support', 'advanced_analytics'],
    BASIC: ['basic_access', 'limited_api'],
    TRIAL: ['trial_access', 'limited_features']
  }
  return features[tenantType] || []
}

/**
 * 根据租户类型获取最大用户数
 */
const getMaxUsersByType = (tenantType) => {
  const limits = {
    ENTERPRISE: 10000,
    PROFESSIONAL: 1000,
    BASIC: 100,
    TRIAL: 10,
    SYSTEM: 100000
  }
  return limits[tenantType] || 100
}

/**
 * 根据租户类型获取最大存储空间
 */
const getMaxStorageByType = (tenantType) => {
  const limits = {
    ENTERPRISE: 10000,
    PROFESSIONAL: 1000,
    BASIC: 100,
    TRIAL: 5,
    SYSTEM: 100000
  }
  return limits[tenantType] || 10
}

/**
 * 根据租户类型获取最大API调用次数
 */
const getMaxApiCallsByType = (tenantType) => {
  const limits = {
    ENTERPRISE: 10000000,
    PROFESSIONAL: 1000000,
    BASIC: 100000,
    TRIAL: 10000,
    SYSTEM: 100000000
  }
  return limits[tenantType] || 10000
}

/**
 * 根据租户类型获取最大项目数
 */
const getMaxProjectsByType = (tenantType) => {
  const limits = {
    ENTERPRISE: 1000,
    PROFESSIONAL: 100,
    BASIC: 10,
    TRIAL: 3,
    SYSTEM: 10000
  }
  return limits[tenantType] || 10
}
</script>

<style scoped>
.tenant-form {
  max-height: 60vh;
  overflow-y: auto;
  padding-right: 10px;
}

.tenant-type-option {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.type-label {
  font-weight: 500;
  color: #303133;
}

.type-description {
  font-size: 12px;
  color: #909399;
}

.isolation-option {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: center;
  text-align: center;
  padding: 8px;
}

.option-title {
  font-weight: 500;
  color: #303133;
}

.option-desc {
  font-size: 12px;
  color: #909399;
  line-height: 1.3;
}

.dialog-footer {
  text-align: right;
}

.info-text {
  color: #606266;
  font-size: 14px;
}

.usage-card {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.usage-number {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.usage-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
}

.usage-limit {
  font-size: 12px;
  color: #909399;
}

:deep(.el-divider__text) {
  color: #409EFF;
  font-weight: 500;
}

:deep(.el-radio-button__inner) {
  padding: 12px 20px;
  height: auto;
  white-space: normal;
}

:deep(.el-checkbox-group) {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

:deep(.el-checkbox) {
  margin-right: 0;
}
</style>