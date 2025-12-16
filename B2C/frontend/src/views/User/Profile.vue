<template>
  <div class="profile-container">
    <div class="profile-header">
      <h2>个人信息</h2>
      <el-button type="primary" @click="handleEdit" v-if="!isEditing">
        编辑信息
      </el-button>
      <div v-else>
        <el-button type="primary" @click="handleSave" :loading="loading">
          保存
        </el-button>
        <el-button @click="handleCancel">取消</el-button>
      </div>
    </div>
    
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="profile-form"
      v-loading="loading"
    >
      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          action="/api/upload/avatar"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :disabled="!isEditing"
        >
          <img v-if="formData.avatar" :src="formData.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" :disabled="!isEditing" />
      </el-form-item>
      
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="formData.nickname" :disabled="!isEditing" />
      </el-form-item>
      
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="formData.phone" :disabled="!isEditing" />
      </el-form-item>
      
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" :disabled="!isEditing" />
      </el-form-item>
      
      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="formData.gender" :disabled="!isEditing">
          <el-radio label="male">男</el-radio>
          <el-radio label="female">女</el-radio>
          <el-radio label="secret">保密</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="生日" prop="birthday">
        <el-date-picker
          v-model="formData.birthday"
          type="date"
          placeholder="选择生日"
          :disabled="!isEditing"
        />
      </el-form-item>
      
      <el-form-item label="个人简介" prop="bio">
        <el-input
          v-model="formData.bio"
          type="textarea"
          :rows="3"
          placeholder="请输入个人简介"
          :disabled="!isEditing"
        />
      </el-form-item>
    </el-form>
    
    <div class="security-section">
      <h3>账号安全</h3>
      <div class="security-items">
        <div class="security-item">
          <span>登录密码</span>
          <el-button type="text" @click="showPasswordDialog">修改</el-button>
        </div>
        <div class="security-item">
          <span>手机号</span>
          <el-button type="text" @click="showPhoneDialog">修改</el-button>
        </div>
        <div class="security-item">
          <span>邮箱</span>
          <el-button type="text" @click="showEmailDialog">修改</el-button>
        </div>
      </div>
    </div>
    
    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="passwordDialogVisible"
      title="修改密码"
      width="400px"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="80px"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChangePassword" :loading="loading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'

import { useUserStore } from '@/stores/user'
import type { User } from '@/types'

const userStore = useUserStore()

const loading = ref(false)
const isEditing = ref(false)
const formRef = ref<FormInstance>()
const passwordFormRef = ref<FormInstance>()

const formData = reactive<Partial<User>>({
  avatar: '',
  username: '',
  nickname: '',
  phone: '',
  email: '',
  gender: 0,
  birthday: '',
  bio: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordDialogVisible = ref(false)

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { max: 20, message: '昵称长度不能超过 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const passwordRules: FormRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const initFormData = () => {
  if (userStore.userInfo) {
    Object.assign(formData, {
      avatar: userStore.userInfo.avatar,
      username: userStore.userInfo.username,
      nickname: userStore.userInfo.nickname,
      phone: userStore.userInfo.phone,
      email: userStore.userInfo.email,
      gender: userStore.userInfo.gender || 'secret',
      birthday: userStore.userInfo.birthday,
      bio: userStore.userInfo.bio
    })
  }
}

const handleEdit = () => {
  isEditing.value = true
}

const handleSave = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    await userStore.updateUser(formData)
    ElMessage.success('个人信息更新成功')
    isEditing.value = false
  } catch (error) {
    ElMessage.error('个人信息更新失败')
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  isEditing.value = false
  initFormData()
}

const handleAvatarSuccess = (response: any) => {
  if (response.code === 200) {
    formData.avatar = response.data.url
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error('头像上传失败')
  }
}

const beforeAvatarUpload = (file: File) => {
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

const showPasswordDialog = () => {
  passwordDialogVisible.value = true
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

const handleChangePassword = async () => {
  if (!passwordFormRef.value) return
  
  try {
    await passwordFormRef.value.validate()
    loading.value = true
    
    await userStore.updatePassword(passwordForm)
    ElMessage.success('密码修改成功')
    passwordDialogVisible.value = false
  } catch (error) {
    ElMessage.error('密码修改失败')
  } finally {
    loading.value = false
  }
}

const showPhoneDialog = () => {
  ElMessage.info('手机号修改功能开发中...')
}

const showEmailDialog = () => {
  ElMessage.info('邮箱修改功能开发中...')
}

onMounted(() => {
  initFormData()
})
</script>

<style scoped lang="scss">
.profile-container {
  padding: 20px;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  
  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 500;
  }
}

.profile-form {
  max-width: 600px;
  margin-bottom: 40px;
}

.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
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
  width: 80px;
  height: 80px;
  text-align: center;
}

.avatar {
  width: 80px;
  height: 80px;
  display: block;
}

.security-section {
  border-top: 1px solid #eee;
  padding-top: 30px;
  
  h3 {
    margin-bottom: 20px;
    font-size: 18px;
    font-weight: 500;
  }
}

.security-items {
  max-width: 600px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
}
</style>