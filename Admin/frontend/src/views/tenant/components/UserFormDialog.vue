<template>
  <el-dialog
    v-model="dialogVisible"
    :title="getDialogTitle()"
    width="600px"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="user-form-dialog">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        class="user-form"
      >
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="formData.username"
                placeholder="请输入用户名"
                :disabled="mode === 'view' || mode === 'edit'"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input
                v-model="formData.realName"
                placeholder="请输入真实姓名"
                :disabled="mode === 'view'"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="formData.email"
                placeholder="请输入邮箱"
                type="email"
                :disabled="mode === 'view'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input
                v-model="formData.phone"
                placeholder="请输入手机号"
                :disabled="mode === 'view'"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="formData.gender" placeholder="请选择性别" :disabled="mode === 'view'">
                <el-option label="男" value="MALE" />
                <el-option label="女" value="FEMALE" />
                <el-option label="未知" value="UNKNOWN" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期">
              <el-date-picker
                v-model="formData.birthDate"
                type="date"
                placeholder="选择出生日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                :disabled="mode === 'view'"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="密码" prop="password" v-if="mode === 'add'">
              <el-input
                v-model="formData.password"
                type="password"
                placeholder="请输入密码"
                show-password
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPassword" v-if="mode === 'add'">
              <el-input
                v-model="formData.confirmPassword"
                type="password"
                placeholder="请再次输入密码"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 角色权限 -->
        <el-divider content-position="left">角色权限</el-divider>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户角色" prop="role">
              <el-select v-model="formData.role" placeholder="请选择角色" :disabled="mode === 'view'">
                <el-option
                  v-for="(role, key) in USER_ROLES"
                  :key="key"
                  :label="role.label"
                  :value="role.value"
                  :disabled="role.disabled"
                >
                  <div class="role-option">
                    <span class="role-label">{{ role.label }}</span>
                    <span class="role-description">{{ role.description }}</span>
                  </div>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择状态" :disabled="mode === 'view'">
                <el-option label="启用" value="ACTIVE" />
                <el-option label="禁用" value="INACTIVE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input
            v-model="formData.remark"
            placeholder="请输入备注信息"
            type="textarea"
            :rows="3"
            maxlength="500"
            show-word-limit
            :disabled="mode === 'view'"
          />
        </el-form-item>

        <!-- 扩展信息 (只读显示) -->
        <template v-if="mode === 'view'">
          <el-divider content-position="left">扩展信息</el-divider>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">用户ID:</span>
                <span class="info-value">{{ formData.userId }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">创建时间:</span>
                <span class="info-value">{{ formatDateTime(formData.createdAt) }}</span>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">最后登录:</span>
                <span class="info-value">{{ formatDateTime(formData.lastLoginTime) }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info-item">
                <span class="info-label">更新时间:</span>
                <span class="info-value">{{ formatDateTime(formData.updatedAt) }}</span>
              </div>
            </el-col>
          </el-row>
        </template>
      </el-form>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button 
          v-if="mode === 'view'"
          type="primary" 
          @click="handleEdit"
        >
          编辑
        </el-button>
        <el-button
          v-else-if="mode === 'add'"
          type="primary"
          :loading="loading"
          @click="handleSubmit"
        >
          创建用户
        </el-button>
        <el-button
          v-else-if="mode === 'edit'"
          type="primary"
          :loading="loading"
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
  userData: {
    type: Object,
    default: () => ({})
  },
  mode: {
    type: String,
    default: 'view', // view, add, edit
    validator: (value) => ['view', 'add', 'edit'].includes(value)
  },
  tenantId: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 使用租户管理组合式函数
const { USER_ROLES, addUserToTenant, updateTenantUser } = useTenant()

// 响应式数据
const formRef = ref()
const loading = ref(false)

const formData = reactive({
  userId: '',
  username: '',
  realName: '',
  email: '',
  phone: '',
  gender: 'UNKNOWN',
  birthDate: '',
  password: '',
  confirmPassword: '',
  role: 'USER',
  status: 'ACTIVE',
  remark: '',
  createdAt: '',
  updatedAt: '',
  lastLoginTime: ''
})

// 表单验证规则
const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名为3-50个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '姓名为2-50个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== formData.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  role: [
    { required: true, message: '请选择用户角色', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择用户状态', trigger: 'change' }
  ]
}

// 计算属性
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 监听用户数据变化
watch(() => props.userData, (userData) => {
  if (userData && Object.keys(userData).length > 0) {
    Object.assign(formData, {
      userId: userData.userId || '',
      username: userData.username || '',
      realName: userData.realName || '',
      email: userData.email || '',
      phone: userData.phone || '',
      gender: userData.gender || 'UNKNOWN',
      birthDate: userData.birthDate || '',
      role: userData.role || 'USER',
      status: userData.status || 'ACTIVE',
      remark: userData.remark || '',
      createdAt: userData.createdAt || '',
      updatedAt: userData.updatedAt || '',
      lastLoginTime: userData.lastLoginTime || ''
    })
  }
}, { immediate: true })

// 监听对话框显示，重置表单
watch(() => props.modelValue, (visible) => {
  if (!visible) {
    resetForm()
  }
})

// 方法

/**
 * 获取对话框标题
 */
const getDialogTitle = () => {
  const titles = {
    view: '查看用户',
    add: '添加用户',
    edit: '编辑用户'
  }
  return titles[props.mode] || '用户管理'
}

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
  emit('success') // 触发父组件切换到编辑模式
}

/**
 * 处理表单提交
 */
const handleSubmit = async () => {
  try {
    // 表单验证
    await formRef.value.validate()
    
    loading.value = true
    
    let result
    
    if (props.mode === 'add') {
      // 添加用户
      const userData = {
        username: formData.username,
        realName: formData.realName,
        email: formData.email,
        phone: formData.phone,
        gender: formData.gender,
        birthDate: formData.birthDate,
        password: formData.password,
        role: formData.role,
        status: formData.status,
        remark: formData.remark
      }
      
      result = await addUserToTenant(props.tenantId, userData)
    } else if (props.mode === 'edit') {
      // 更新用户
      const userData = {
        realName: formData.realName,
        email: formData.email,
        phone: formData.phone,
        gender: formData.gender,
        birthDate: formData.birthDate,
        role: formData.role,
        status: formData.status,
        remark: formData.remark
      }
      
      result = await updateTenantUser(props.tenantId, formData.userId, userData)
    }
    
    if (result.success) {
      const action = props.mode === 'add' ? '创建' : '更新'
      ElMessage.success(`用户${action}成功`)
      emit('success')
      handleClose()
    }
  } catch (error) {
    console.error('用户操作失败:', error)
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    loading.value = false
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
    userId: '',
    username: '',
    realName: '',
    email: '',
    phone: '',
    gender: 'UNKNOWN',
    birthDate: '',
    password: '',
    confirmPassword: '',
    role: 'USER',
    status: 'ACTIVE',
    remark: '',
    createdAt: '',
    updatedAt: '',
    lastLoginTime: ''
  })
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
.user-form-dialog {
  max-height: 70vh;
  overflow-y: auto;
}

.user-form {
  padding-right: 10px;
}

.role-option {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.role-label {
  font-weight: 500;
  color: #303133;
}

.role-description {
  font-size: 12px;
  color: #909399;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-label {
  font-weight: 500;
  color: #606266;
  min-width: 100px;
  margin-right: 15px;
}

.info-value {
  color: #303133;
  flex: 1;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-divider__text) {
  color: #409EFF;
  font-weight: 500;
}
</style>