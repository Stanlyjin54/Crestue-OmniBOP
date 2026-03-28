import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '@/types'
import { login as loginApi, getUserInfo, updateUserInfo, updatePassword as updatePasswordApi, updatePhone as updatePhoneApi, updateEmail as updateEmailApi, deleteAccount as deleteAccountApi, register as registerApi } from '@/api'
import { getToken, setToken as setAuthToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  // 状态
  const user = ref<User | null>(null)
  const token = ref<string>(getToken() || '')

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const userId = computed(() => user.value?.id || 0)
  const userName = computed(() => user.value?.username || '')
  const userAvatar = computed(() => user.value?.avatar || '')
  const userLevel = computed(() => user.value?.level || 0)
  const userInfo = computed(() => user.value)

  // 方法
  const setToken = (newToken: string) => {
    token.value = newToken
    setAuthToken(newToken)
  }

  const setUser = (userData: User | null) => {
    user.value = userData
  }

  const login = async (loginData: { username: string; password: string }) => {
    // 转换为后端需要的格式
    const loginRequest: any = {
      phone: loginData.username,
      password: loginData.password,
      relevanceTable: 'user_info' // 默认用户表
    }
    console.log('userStore.login - 请求参数:', loginRequest)
    const response = await loginApi(loginRequest)
    console.log('userStore.login - 响应:', response)
    setToken(response.data)
    console.log('userStore.login - Token已设置:', response.data)
    // 暂时跳过获取用户信息，先测试跳转
    // await fetchUserInfo()
    return response
  }

  const register = async (registerData: { username: string; password: string; phone: string; email: string }) => {
    await registerApi(registerData)
    // 注册成功，用户需要手动登录
  }

  const logout = async () => {
    setToken('')
    setUser(null)
  }

  const fetchUserInfo = async () => {
    if (!token.value) return
    
    try {
      const response = await getUserInfo()
      setUser(response)
    } catch (error) {
      // 如果获取用户信息失败，清除登录状态
      setToken('')
      setUser(null)
      throw error
    }
  }

  const updateUser = async (userData: Partial<User>) => {
    await updateUserInfo(userData)
    // 更新成功后重新获取用户信息
    await fetchUserInfo()
    return true
  }

  const initUser = async () => {
    if (token.value) {
      try {
        await fetchUserInfo()
      } catch (error) {
        // 初始化失败时清除token
        setToken('')
      }
    }
  }

  const updatePassword = async (passwordData: { oldPassword: string; newPassword: string; confirmPassword: string }) => {
    const response = await updatePasswordApi(passwordData)
    return response
  }

  const updatePhone = async (phoneData: { phone: string; code: string }) => {
    const response = await updatePhoneApi(phoneData)
    // 更新用户信息
    if (response?.user) {
      setUser(response.user)
    }
    return response
  }

  const updateEmail = async (emailData: { email: string; code: string }) => {
    const response = await updateEmailApi(emailData)
    // 更新用户信息
    if (response?.user) {
      setUser(response.user)
    }
    return response
  }

  const deleteAccount = async (password: string) => {
    await deleteAccountApi(password)
    setToken('')
    setUser(null)
  }

  return {
    // 状态
    user,
    token,
    // 计算属性
    isLoggedIn,
    userId,
    userName,
    userAvatar,
    userLevel,
    userInfo,
    // 方法
    setToken,
    setUser,
    login,
    register,
    logout,
    fetchUserInfo,
    updateUser,
    updatePassword,
    updatePhone,
    updateEmail,
    initUser,
    deleteAccount
  }
})