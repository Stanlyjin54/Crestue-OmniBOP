<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>{{ isRegister ? '用户注册' : '用户登录' }}</h2>
        <p>{{ isRegister ? '注册账户，开启购物之旅' : '欢迎回来，请登录您的账户' }}</p>
      </div>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        class="login-form"
        @submit.prevent="handleSubmit"
      >
        <el-form-item prop="username">
          <el-input
            v-model="formData.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="user"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="lock"
            show-password
          />
        </el-form-item>

        <el-form-item prop="confirmPassword" v-if="isRegister">
          <el-input
            v-model="formData.confirmPassword"
            type="password"
            placeholder="请确认密码"
            size="large"
            prefix-icon="lock"
            show-password
          />
        </el-form-item>

        <el-form-item prop="phone" v-if="isRegister">
          <el-input
            v-model="formData.phone"
            placeholder="请输入手机号"
            size="large"
            prefix-icon="phone"
          />
        </el-form-item>

        <el-form-item prop="email" v-if="isRegister">
          <el-input
            v-model="formData.email"
            placeholder="请输入邮箱"
            size="large"
            prefix-icon="message"
          />
        </el-form-item>

        <el-form-item prop="captcha" v-if="!isRegister">
          <div class="captcha-container">
            <el-input
              v-model="formData.captcha"
              placeholder="请输入验证码"
              size="large"
              prefix-icon="key"
            />
            <div class="captcha-image" @click="refreshCaptcha">
              <img :src="captchaUrl" alt="验证码" />
            </div>
          </div>
        </el-form-item>

        <el-form-item class="form-actions">
          <el-checkbox v-model="rememberMe" v-if="!isRegister">
            记住我
          </el-checkbox>
          <el-button 
            type="text" 
            @click="handleForgotPassword"
            v-if="!isRegister"
          >
            忘记密码？
          </el-button>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="submit-button"
            :loading="loading"
            @click="handleSubmit"
          >
            {{ isRegister ? '注册' : '登录' }}
          </el-button>
        </el-form-item>

        <el-form-item class="form-footer">
          <span>{{ isRegister ? '已有账户？' : '还没有账户？' }}</span>
          <el-button type="text" @click="toggleMode">
            {{ isRegister ? '立即登录' : '立即注册' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 第三方登录 -->
      <div class="third-party-login" v-if="!isRegister">
        <div class="divider">
          <span>其他登录方式</span>
        </div>
        <div class="login-methods">
          <el-button circle @click="handleWeChatLogin">
            <el-icon><chat-dot-round /></el-icon>
          </el-button>
          <el-button circle @click="handleQQLogin">
            <span class="qq-icon">Q</span>
          </el-button>
          <el-button circle @click="handleWeiboLogin">
            <el-icon><platform /></el-icon>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
// 不需要单独导入图标，main.ts中已全局注册
import { useUserStore } from '@/stores/user'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)
const isRegister = ref(false)
const rememberMe = ref(false)
const captchaUrl = ref('/api/captcha?' + Date.now())

const formData = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  phone: '',
  email: '',
  captcha: ''
})

// 表单验证规则
const rules = computed<FormRules>(() => {
  const baseRules = {
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' },
      { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
    ]
  }

  if (isRegister.value) {
    return {
      ...baseRules,
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        {
          validator: (_rule, value, callback) => {
            if (value !== formData.password) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ],
      phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
      ]
    }
  } else {
    return {
      ...baseRules,
      captcha: [
        { required: true, message: '请输入验证码', trigger: 'blur' },
        { len: 4, message: '验证码长度为4位', trigger: 'blur' }
      ]
    }
  }
})

// 切换登录/注册模式
const toggleMode = () => {
  isRegister.value = !isRegister.value
  formRef.value?.resetFields()
}

// 刷新验证码
const refreshCaptcha = () => {
  captchaUrl.value = `/api/captcha?${Date.now()}`
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (isRegister.value) {
          // 注册
          await userStore.register({
            username: formData.username,
            password: formData.password,
            phone: formData.phone,
            email: formData.email
          })
          ElMessage.success('注册成功，请登录')
          toggleMode()
        } else {
          // 登录
          await userStore.login({
            username: formData.username,
            password: formData.password
          })
          
          ElMessage.success('登录成功')
          
          // 跳转到原来要访问的页面或首页
          const redirect = route.query.redirect as string
          router.push(redirect || '/')
        }
      } catch (error: any) {
        ElMessage.error(error.message || '操作失败')
        if (!isRegister.value) {
          refreshCaptcha()
          formData.captcha = ''
        }
      } finally {
        loading.value = false
      }
    }
  })
}

// 忘记密码
const handleForgotPassword = () => {
  ElMessageBox.prompt('请输入您的注册邮箱', '找回密码', {
    confirmButtonText: '发送',
    cancelButtonText: '取消',
    inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
    inputErrorMessage: '请输入正确的邮箱格式'
  }).then(({ value }) => {
    // 这里调用找回密码API
    ElMessage.success(`重置密码链接已发送到 ${value}`)
  }).catch(() => {
    // 取消操作
  })
}

// 第三方登录
const handleWeChatLogin = () => {
  ElMessage.info('微信登录功能开发中...')
}

const handleQQLogin = () => {
  ElMessage.info('QQ登录功能开发中...')
}

const handleWeiboLogin = () => {
  ElMessage.info('微博登录功能开发中...')
}
</script>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-box {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 420px;
  
  .login-header {
    text-align: center;
    margin-bottom: 30px;
    
    h2 {
      color: #333;
      font-size: 24px;
      font-weight: 600;
      margin-bottom: 8px;
    }
    
    p {
      color: #666;
      font-size: 14px;
    }
  }
  
  .login-form {
    .el-form-item {
      margin-bottom: 20px;
    }
    
    .captcha-container {
      display: flex;
      gap: 10px;
      
      .el-input {
        flex: 1;
      }
      
      .captcha-image {
        width: 100px;
        height: 40px;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        overflow: hidden;
        cursor: pointer;
        
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
        
        &:hover {
          border-color: #409eff;
        }
      }
    }
    
    .form-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      .el-checkbox {
        color: #666;
      }
    }
    
    .submit-button {
      width: 100%;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      
      &:hover {
        opacity: 0.9;
      }
    }
    
    .form-footer {
      text-align: center;
      margin-top: 20px;
      
      span {
        color: #666;
        font-size: 14px;
      }
    }
  }
  
  .third-party-login {
    margin-top: 30px;
    
    .divider {
      text-align: center;
      position: relative;
      margin-bottom: 20px;
      
      span {
        background: white;
        padding: 0 15px;
        color: #999;
        font-size: 12px;
      }
      
      &::before {
        content: '';
        position: absolute;
        top: 50%;
        left: 0;
        right: 0;
        height: 1px;
        background: #eee;
        z-index: -1;
      }
    }
    
    .login-methods {
      display: flex;
      justify-content: center;
      gap: 20px;
      
      .el-button {
        width: 40px;
        height: 40px;
        padding: 0;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .qq-icon {
          font-weight: bold;
          font-size: 16px;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 480px) {
  .login-box {
    padding: 30px 20px;
    margin: 0 10px;
  }
}
</style>