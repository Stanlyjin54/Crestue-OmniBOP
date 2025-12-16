<template>
  <el-dialog
    v-model="dialogVisible"
    title="创建租户"
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
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="租户类型" prop="tenantType">
            <el-select v-model="formData.tenantType" placeholder="请选择租户类型">
              <el-option
                v-for="(type, key) in TENANT_TYPES"
                :key="key"
                :label="type.label"
                :value="type.value"
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
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="formData.contactPhone"
              placeholder="请输入联系电话"
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
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="行业">
            <el-select v-model="formData.industry" placeholder="请选择行业">
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
        />
      </el-form-item>

      <!-- 配额设置 -->
      <el-divider content-position="left">配额设置</el-divider>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="最大用户数" prop="quota.maxUsers">
            <el-input-number
              v-model="formData.quota.maxUsers"
              :min="1"
              :max="10000"
              :step="10"
              placeholder="最大用户数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="存储空间(GB)" prop="quota.maxStorage">
            <el-input-number
              v-model="formData.quota.maxStorage"
              :min="1"
              :max="10000"
              :step="1"
              placeholder="最大存储空间"
              style="width: 100%"
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
              :max="10000000"
              :step="1000"
              placeholder="最大API调用次数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最大项目数" prop="quota.maxProjects">
            <el-input-number
              v-model="formData.quota.maxProjects"
              :min="1"
              :max="1000"
              :step="1"
              placeholder="最大项目数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 功能特性 -->
      <el-divider content-position="left">功能特性</el-divider>

      <el-form-item label="功能权限">
        <el-checkbox-group v-model="formData.features">
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
            <el-select v-model="formData.billingCycle" placeholder="请选择计费周期">
              <el-option label="月付" value="monthly" />
              <el-option label="年付" value="yearly" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="自动续费">
            <el-switch v-model="formData.autoRenew" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 隔离级别 -->
      <el-divider content-position="left">数据隔离</el-divider>

      <el-form-item label="隔离级别">
        <el-radio-group v-model="formData.isolationLevel">
          <el-radio-button
            v-for="(level, key) in ISOLATION_LEVELS"
            :key="key"
            :label="level.value"
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
          @click="handleSubmit"
        >
          创建租户
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
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 使用租户管理组合式函数
const { TENANT_TYPES, TENANT_STATUS, ISOLATION_LEVELS, createTenant, createForm } = useTenant()

// 响应式数据
const formRef = ref()
const loading = ref(false)

const formData = reactive({
  tenantName: '',
  tenantType: 'BASIC',
  contactEmail: '',
  contactPhone: '',
  companyName: '',
  industry: '',
  address: '',
  description: '',
  quota: {
    maxUsers: 100,
    maxStorage: 10,
    maxApiCalls: 10000,
    maxProjects: 10
  },
  features: [],
  billingCycle: 'monthly',
  autoRenew: true,
  isolationLevel: 'TABLE'
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

// 监听租户类型变化，自动设置功能特性
watch(() => formData.tenantType, (newType) => {
  const tenantType = TENANT_TYPES[newType]
  if (tenantType && tenantType.features) {
    // 根据租户类型预设功能特性
    if (newType === 'ENTERPRISE') {
      formData.features = ['full_access', 'custom_branding', 'api_access', 'priority_support', 'advanced_analytics']
    } else if (newType === 'PROFESSIONAL') {
      formData.features = ['standard_access', 'basic_api', 'email_support', 'advanced_analytics']
    } else if (newType === 'BASIC') {
      formData.features = ['basic_access', 'limited_api']
    } else if (newType === 'TRIAL') {
      formData.features = ['trial_access', 'limited_features']
    }
  }
}, { immediate: true })

// 方法

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
  resetForm()
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
    tenantName: '',
    tenantType: 'BASIC',
    contactEmail: '',
    contactPhone: '',
    companyName: '',
    industry: '',
    address: '',
    description: '',
    quota: {
      maxUsers: 100,
      maxStorage: 10,
      maxApiCalls: 10000,
      maxProjects: 10
    },
    features: [],
    billingCycle: 'monthly',
    autoRenew: true,
    isolationLevel: 'TABLE'
  })
}

/**
 * 处理提交表单
 */
const handleSubmit = async () => {
  try {
    // 表单验证
    await formRef.value.validate()
    
    loading.value = true
    
    // 构建提交数据
    const submitData = {
      ...formData,
      // 根据租户类型设置默认配额
      quota: {
        ...formData.quota,
        // 可以根据租户类型调整配额限制
        maxUsers: Math.min(formData.quota.maxUsers, getMaxUsersByType(formData.tenantType)),
        maxStorage: Math.min(formData.quota.maxStorage, getMaxStorageByType(formData.tenantType)),
        maxApiCalls: Math.min(formData.quota.maxApiCalls, getMaxApiCallsByType(formData.tenantType)),
        maxProjects: Math.min(formData.quota.maxProjects, getMaxProjectsByType(formData.tenantType))
      }
    }
    
    // 创建租户
    const result = await createTenant(submitData)
    
    if (result.success) {
      ElMessage.success('租户创建成功')
      emit('success')
      handleClose()
    }
  } catch (error) {
    console.error('创建租户失败:', error)
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    loading.value = false
  }
}

/**
 * 根据租户类型获取最大用户数
 */
const getMaxUsersByType = (tenantType) => {
  const limits = {
    ENTERPRISE: 10000,
    PROFESSIONAL: 1000,
    BASIC: 100,
    TRIAL: 10
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
    TRIAL: 5
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
    TRIAL: 10000
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
    TRIAL: 3
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