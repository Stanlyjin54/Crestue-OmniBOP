<template>
  <el-dialog
    v-model="visible"
    :title="title"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
      v-loading="submitLoading"
    >
      <!-- 基本信息 -->
      <el-divider content-position="left">基本信息</el-divider>
      
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="form.username"
          placeholder="请输入用户名"
          :disabled="isView"
        />
      </el-form-item>
      
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="form.phone"
          placeholder="请输入手机号"
          :disabled="isView"
        />
      </el-form-item>
      
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="form.email"
          placeholder="请输入邮箱"
          :disabled="isView"
        />
      </el-form-item>
      
      <!-- 密码（仅新增时显示） -->
      <el-form-item v-if="isCreate" label="密码" prop="password">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="请输入密码"
          show-password
        />
      </el-form-item>
      
      <!-- 角色和权限 -->
      <el-divider content-position="left">角色权限</el-divider>
      
      <el-form-item label="用户角色" prop="role">
        <el-select
          v-model="form.role"
          placeholder="请选择用户角色"
          :disabled="isView"
          style="width: 100%"
        >
          <el-option
            v-for="option in roleOptions"
            :key="option.value"
            :label="option.label"
            :value="option.value"
          >
            <div class="role-option">
              <span>{{ option.label }}</span>
              <el-tag size="small" type="info">
                {{ getRoleDescription(option.value) }}
              </el-tag>
            </div>
          </el-option>
        </el-select>
      </el-form-item>
      
      <!-- 状态设置 -->
      <el-divider content-position="left">状态设置</el-divider>
      
      <el-form-item label="账号状态" prop="status">
        <el-radio-group v-model="form.status" :disabled="isView">
          <el-radio label="active">启用</el-radio>
          <el-radio label="inactive">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <!-- 头像上传 -->
      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          action="/api/upload/avatar"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :disabled="isView"
        >
          <img v-if="form.avatar" :src="form.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      
      <!-- 备注信息 -->
      <el-form-item label="备注">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
          :disabled="isView"
        />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button
          v-if="!isView"
          type="primary"
          @click="handleSubmit"
          :loading="submitLoading"
        >
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  },
  formData: {
    type: Object,
    default: () => ({})
  },
  roleOptions: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue', 'submit'])

// 表单引用
const formRef = ref()
const submitLoading = ref(false)

// 表单数据
const form = reactive({
  id: null,
  username: '',
  phone: '',
  email: '',
  password: '',
  role: '',
  status: 'active',
  avatar: '',
  remark: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择用户角色', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择账号状态', trigger: 'change' }
  ]
}

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const isView = computed(() => props.title.includes('查看'))
const isCreate = computed(() => props.title.includes('新增'))

// 获取角色描述
const getRoleDescription = (role) => {
  const roleMap = {
    super_admin: '系统超级管理员',
    admin: '系统管理员',
    manager: '业务经理',
    sales: '销售人员',
    customer_service: '客服人员',
    warehouse: '仓库管理员',
    finance: '财务人员'
  }
  return roleMap[role] || ''
}

// 头像上传处理
const handleAvatarSuccess = (response, uploadFile) => {
  if (response.code === 200) {
    form.avatar = response.data.url
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error('头像上传失败')
  }
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

// 表单提交
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitLoading.value = true

    // 模拟提交
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 构建提交数据
    const submitData = {
      ...form,
      id: form.id || undefined
    }

    // 如果是新增，移除id字段
    if (isCreate.value) {
      delete submitData.id
    }

    emit('submit', submitData)
    ElMessage.success(isCreate.value ? '用户创建成功' : '用户更新成功')
  } catch (error) {
    if (error !== false) {
      ElMessage.error('表单验证失败')
    }
  } finally {
    submitLoading.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  visible.value = false
  // 重置表单
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 监听表单数据变化
watch(
  () => props.formData,
  (newData) => {
    if (newData && Object.keys(newData).length > 0) {
      Object.assign(form, newData)
    } else {
      // 重置为默认值
      Object.assign(form, {
        id: null,
        username: '',
        phone: '',
        email: '',
        password: '',
        role: '',
        status: 'active',
        avatar: '',
        remark: ''
      })
    }
  },
  { immediate: true }
)
</script>

<style lang="scss" scoped>
.role-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }

  :deep(.el-upload:hover) {
    border-color: var(--el-color-primary);
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
}

.el-divider {
  margin: 24px 0 16px;
}

:deep(.el-dialog__body) {
  padding-top: 10px;
}
</style>