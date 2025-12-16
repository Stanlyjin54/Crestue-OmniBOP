import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi, getUserInfo, updateUserInfo as updateUserInfoApi, updatePassword as changePasswordApi, uploadAvatar as uploadAvatarApi, deleteAccount as deleteAccountApi } from '@/api/user'
import { setToken, setUserInfo, removeToken, removeUserInfo } from '@/utils/auth'
import type { LoginRequest, RegisterRequest, UserInfo } from '@/types'

export const useUserStore = () => {
  // 状态
  const token = ref<string>('')
  const userInfo = ref<UserInfo | null>(null)
  const loading = ref(false)

  // 计算属性
  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.level === 99)

  // 登录
  const login = async (loginData: LoginRequest) => {
    try {
      loading.value = true
      const response = await loginApi(loginData)
      
      token.value = response.token
      userInfo.value = response.user
      
      // 保存到本地存储
      setToken(token.value)
      setUserInfo(userInfo.value)
      
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 注册
  const register = async (registerData: RegisterRequest) => {
    try {
      loading.value = true
      const response = await registerApi(registerData)
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      if (!token.value) return
      
      loading.value = true
      const response = await getUserInfo()
      userInfo.value = response
      setUserInfo(userInfo.value)
      
      return response
    } catch (error) {
      // 如果获取用户信息失败，清除token
      logout()
      throw error
    } finally {
      loading.value = false
    }
  }

  // 退出登录
  const logout = () => {
    token.value = ''
    userInfo.value = null
    removeToken()
    removeUserInfo()
  }

  // 更新用户信息
  const updateUserInfo = async (userData: Partial<UserInfo>) => {
    try {
      loading.value = true
      const response = await updateUserInfoApi(userData)
      if (userInfo.value) {
        userInfo.value = { ...userInfo.value, ...userData }
        setUserInfo(userInfo.value)
      }
      
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 修改密码
  const changePassword = async (oldPassword: string, newPassword: string, confirmPassword: string) => {
    try {
      loading.value = true
      const response = await changePasswordApi({
        oldPassword,
        newPassword,
        confirmPassword
      })
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 上传头像
  const uploadAvatar = async (file: File) => {
    try {
      loading.value = true
      const response = await uploadAvatarApi(file)
      if (userInfo.value) {
        userInfo.value.avatar = response.url
        setUserInfo(userInfo.value)
      }
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  // 初始化用户信息
  const initUserInfo = () => {
    const savedToken = localStorage.getItem('token')
    const savedUserInfo = localStorage.getItem('userInfo')
    
    if (savedToken) {
      token.value = savedToken
    }
    
    if (savedUserInfo) {
      try {
        userInfo.value = JSON.parse(savedUserInfo)
      } catch (error) {
        console.error('解析用户信息失败:', error)
        removeUserInfo()
      }
    }
  }

  // 删除账号
  const deleteAccount = async (password: string) => {
    try {
      loading.value = true
      const response = await deleteAccountApi(password)
      return response
    } catch (error) {
      throw error
    } finally {
      loading.value = false
    }
  }

  return {
    // 状态
    token,
    userInfo,
    loading,
    
    // 计算属性
    isAuthenticated,
    isAdmin,
    
    // 方法
    login,
    register,
    fetchUserInfo,
    logout,
    updateUserInfo,
    changePassword,
    uploadAvatar,
    deleteAccount,
    initUserInfo
  }
}