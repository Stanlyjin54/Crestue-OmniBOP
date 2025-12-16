<template>
  <div class="settings-container">
    <div class="settings-header">
      <h2>账号设置</h2>
    </div>

    <div class="settings-content">
      <!-- 账号安全 -->
      <div class="settings-section">
        <h3>账号安全</h3>
        <div class="settings-list">
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">登录密码</span>
              <span class="setting-desc">定期更换密码可以保护账号安全</span>
            </div>
            <el-button type="primary" @click="showPasswordDialog">
              修改
            </el-button>
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">手机号</span>
              <span class="setting-desc">{{ userInfo?.phone || '未绑定' }}</span>
            </div>
            <el-button type="primary" @click="showPhoneDialog">
              {{ userInfo?.phone ? '修改' : '绑定' }}
            </el-button>
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">邮箱</span>
              <span class="setting-desc">{{ userInfo?.email || '未绑定' }}</span>
            </div>
            <el-button type="primary" @click="showEmailDialog">
              {{ userInfo?.email ? '修改' : '绑定' }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 隐私设置 -->
      <div class="settings-section">
        <h3>隐私设置</h3>
        <div class="settings-list">
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">个人资料可见性</span>
              <span class="setting-desc">控制其他用户是否可以查看您的个人资料</span>
            </div>
            <el-switch
              v-model="privacySettings.profileVisible"
              @change="updatePrivacySetting('profileVisible')"
            />
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">订单评价可见性</span>
              <span class="setting-desc">控制其他用户是否可以查看您的订单评价</span>
            </div>
            <el-switch
              v-model="privacySettings.reviewVisible"
              @change="updatePrivacySetting('reviewVisible')"
            />
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">个性化推荐</span>
              <span class="setting-desc">根据您的浏览和购买记录推荐相关商品</span>
            </div>
            <el-switch
              v-model="privacySettings.personalizedRecommendations"
              @change="updatePrivacySetting('personalizedRecommendations')"
            />
          </div>
        </div>
      </div>

      <!-- 通知设置 -->
      <div class="settings-section">
        <h3>通知设置</h3>
        <div class="settings-list">
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">订单状态通知</span>
              <span class="setting-desc">订单状态变更时发送通知</span>
            </div>
            <el-switch
              v-model="notificationSettings.orderStatus"
              @change="updateNotificationSetting('orderStatus')"
            />
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">促销活动通知</span>
              <span class="setting-desc">促销活动和优惠信息通知</span>
            </div>
            <el-switch
              v-model="notificationSettings.promotions"
              @change="updateNotificationSetting('promotions')"
            />
          </div>
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">系统消息通知</span>
              <span class="setting-desc">系统重要消息和公告通知</span>
            </div>
            <el-switch
              v-model="notificationSettings.systemMessages"
              @change="updateNotificationSetting('systemMessages')"
            />
          </div>
        </div>
      </div>

      <!-- 账号操作 -->
      <div class="settings-section">
        <h3>账号操作</h3>
        <div class="settings-list">
          <div class="setting-item">
            <div class="setting-info">
              <span class="setting-title">导出个人数据</span>
              <span class="setting-desc">下载您的个人数据副本</span>
            </div>
            <el-button @click="exportPersonalData">导出数据</el-button>
          </div>
          <div class="setting-item danger">
            <div class="setting-info">
              <span class="setting-title">注销账号</span>
              <span class="setting-desc">永久删除您的账号和所有相关数据</span>
            </div>
            <el-button type="danger" @click="showDeleteAccountDialog">
              注销账号
            </el-button>
          </div>
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

    <!-- 修改手机号对话框 -->
    <el-dialog
      v-model="phoneDialogVisible"
      title="修改手机号"
      width="400px"
    >
      <el-form
        ref="phoneFormRef"
        :model="phoneForm"
        :rules="phoneRules"
        label-width="80px"
      >
        <el-form-item label="新手机号" prop="phone">
          <el-input
            v-model="phoneForm.phone"
            placeholder="请输入新手机号"
          />
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <div class="code-input-group">
            <el-input
              v-model="phoneForm.code"
              placeholder="请输入验证码"
            />
            <el-button
              type="primary"
              :disabled="codeSending || codeCountdown > 0"
              @click="sendPhoneCode"
            >
              {{ codeCountdown > 0 ? `${codeCountdown}s` : '发送验证码' }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="phoneDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChangePhone" :loading="loading">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 修改邮箱对话框 -->
    <el-dialog
      v-model="emailDialogVisible"
      title="修改邮箱"
      width="400px"
    >
      <el-form
        ref="emailFormRef"
        :model="emailForm"
        :rules="emailRules"
        label-width="80px"
      >
        <el-form-item label="新邮箱" prop="email">
          <el-input
            v-model="emailForm.email"
            placeholder="请输入新邮箱"
          />
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <div class="code-input-group">
            <el-input
              v-model="emailForm.code"
              placeholder="请输入验证码"
            />
            <el-button
              type="primary"
              :disabled="codeSending || codeCountdown > 0"
              @click="sendEmailCode"
            >
              {{ codeCountdown > 0 ? `${codeCountdown}s` : '发送验证码' }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="emailDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleChangeEmail" :loading="loading">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 注销账号确认对话框 -->
    <el-dialog
      v-model="deleteAccountDialogVisible"
      title="注销账号确认"
      width="400px"
    >
      <div class="delete-account-warning">
        <el-alert
          title="警告"
          type="error"
          description="账号注销后将无法恢复，所有数据将被永久删除。请谨慎操作！"
          :closable="false"
        />
        <el-form
          ref="deleteAccountFormRef"
          :model="deleteAccountForm"
          :rules="deleteAccountRules"
          label-width="80px"
          style="margin-top: 20px"
        >
          <el-form-item label="确认密码" prop="password">
            <el-input
              v-model="deleteAccountForm.password"
              type="password"
              placeholder="请输入登录密码确认"
              show-password
            />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="deleteAccountDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="handleDeleteAccount" :loading="loading">
          确认注销
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'


const userStore = useUserStore()
const router = useRouter()

const loading = ref(false)
const passwordDialogVisible = ref(false)
const phoneDialogVisible = ref(false)
const emailDialogVisible = ref(false)
const deleteAccountDialogVisible = ref(false)
const codeSending = ref(false)
const codeCountdown = ref(0)

const passwordFormRef = ref<FormInstance>()
const phoneFormRef = ref<FormInstance>()
const emailFormRef = ref<FormInstance>()
const deleteAccountFormRef = ref<FormInstance>()

const userInfo = computed(() => userStore.userInfo)

// 隐私设置
const privacySettings = reactive({
  profileVisible: true,
  reviewVisible: true,
  personalizedRecommendations: true
})

// 通知设置
const notificationSettings = reactive({
  orderStatus: true,
  promotions: true,
  systemMessages: true
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const phoneForm = reactive({
  phone: '',
  code: ''
})

const emailForm = reactive({
  email: '',
  code: ''
})

const deleteAccountForm = reactive({
  password: ''
})

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
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const phoneRules: FormRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

const emailRules: FormRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

const deleteAccountRules: FormRules = {
  password: [
    { required: true, message: '请输入登录密码', trigger: 'blur' }
  ]
}

// 显示对话框
const showPasswordDialog = () => {
  passwordDialogVisible.value = true
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
}

const showPhoneDialog = () => {
  phoneDialogVisible.value = true
  phoneForm.phone = userInfo.value?.phone || ''
  phoneForm.code = ''
}

const showEmailDialog = () => {
  emailDialogVisible.value = true
  emailForm.email = userInfo.value?.email || ''
  emailForm.code = ''
}

const showDeleteAccountDialog = () => {
  deleteAccountDialogVisible.value = true
  deleteAccountForm.password = ''
}

// 发送验证码
const sendPhoneCode = async () => {
  try {
    await phoneFormRef.value?.validateField('phone')
    codeSending.value = true
    codeCountdown.value = 60
    
    // 模拟发送验证码
    const timer = setInterval(() => {
      codeCountdown.value--
      if (codeCountdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
    
    ElMessage.success('验证码已发送')
  } catch (error) {
    console.error('发送验证码失败:', error)
  } finally {
    codeSending.value = false
  }
}

const sendEmailCode = async () => {
  try {
    await emailFormRef.value?.validateField('email')
    codeSending.value = true
    codeCountdown.value = 60
    
    // 模拟发送验证码
    const timer = setInterval(() => {
      codeCountdown.value--
      if (codeCountdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
    
    ElMessage.success('验证码已发送')
  } catch (error) {
    console.error('发送验证码失败:', error)
  } finally {
    codeSending.value = false
  }
}

// 处理表单提交
const handleChangePassword = async () => {
  try {
    await passwordFormRef.value?.validate()
    loading.value = true
    
    // 这里调用修改密码API
    await userStore.updatePassword(passwordForm)
    
    passwordDialogVisible.value = false
    ElMessage.success('密码修改成功')
  } catch (error) {
    console.error('修改密码失败:', error)
    ElMessage.error('修改密码失败')
  } finally {
    loading.value = false
  }
}

const handleChangePhone = async () => {
  try {
    await phoneFormRef.value?.validate()
    loading.value = true
    
    // 这里调用修改手机号API
    await userStore.updatePhone(phoneForm)
    
    phoneDialogVisible.value = false
    ElMessage.success('手机号修改成功')
  } catch (error) {
    console.error('修改手机号失败:', error)
    ElMessage.error('修改手机号失败')
  } finally {
    loading.value = false
  }
}

const handleChangeEmail = async () => {
  try {
    await emailFormRef.value?.validate()
    loading.value = true
    
    // 这里调用修改邮箱API
    await userStore.updateEmail(emailForm)
    
    emailDialogVisible.value = false
    ElMessage.success('邮箱修改成功')
  } catch (error) {
    console.error('修改邮箱失败:', error)
    ElMessage.error('修改邮箱失败')
  } finally {
    loading.value = false
  }
}

// 更新设置
const updatePrivacySetting = async (_key: string) => {
  try {
    // 这里调用更新隐私设置API
    ElMessage.success('设置已更新')
  } catch (error) {
    console.error('更新设置失败:', error)
    ElMessage.error('更新设置失败')
  }
}

const updateNotificationSetting = async (_key: string) => {
  try {
    // 这里调用更新通知设置API
    ElMessage.success('设置已更新')
  } catch (error) {
    console.error('更新设置失败:', error)
    ElMessage.error('更新设置失败')
  }
}

// 导出个人数据
const exportPersonalData = async () => {
  try {
    loading.value = true
    // 这里调用导出数据API
    ElMessage.success('数据导出请求已提交，请稍后查看邮箱')
  } catch (error) {
    console.error('导出数据失败:', error)
    ElMessage.error('导出数据失败')
  } finally {
    loading.value = false
  }
}

// 注销账号
const handleDeleteAccount = async () => {
  try {
    await deleteAccountFormRef.value?.validate()
    loading.value = true
    
    // 这里调用注销账号API
    await userStore.deleteAccount(deleteAccountForm.password)
    
    deleteAccountDialogVisible.value = false
    ElMessage.success('账号注销成功')
    
    // 跳转到首页
    router.push('/')
  } catch (error) {
    console.error('注销账号失败:', error)
    ElMessage.error('注销账号失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.settings-container {
  padding: 20px;
}

.settings-header {
  margin-bottom: 30px;
  
  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 600;
    color: #303133;
  }
}

.settings-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.settings-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  h3 {
    margin: 0 0 20px;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }
}

.settings-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
  
  &.danger {
    .setting-title {
      color: #f56c6c;
    }
  }
  
  .setting-info {
    flex: 1;
    
    .setting-title {
      display: block;
      font-size: 16px;
      font-weight: 500;
      color: #303133;
      margin-bottom: 4px;
    }
    
    .setting-desc {
      display: block;
      font-size: 12px;
      color: #909399;
    }
  }
}

.code-input-group {
  display: flex;
  gap: 10px;
  
  .el-input {
    flex: 1;
  }
}

.delete-account-warning {
  .el-alert {
    margin-bottom: 20px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .setting-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    
    .setting-info {
      margin-bottom: 10px;
    }
  }
  
  .code-input-group {
    flex-direction: column;
  }
}
</style>