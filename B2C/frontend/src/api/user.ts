import { request } from './request'
import type { PageResponse, PageRequest, UserStats, Product } from '@/types'
import type { UserInfo, LoginRequest, LoginResponse, RegisterRequest } from '@/types'

// 用户登录
export const login = (data: LoginRequest): Promise<LoginResponse> => {
  return request.post('/user/login', data)
}

// 用户注册
export const register = (data: RegisterRequest): Promise<void> => {
  return request.post('/user/register', data)
}

// 获取用户信息
export const getUserInfo = (): Promise<UserInfo> => {
  return request.get('/user/info')
}

// 更新用户信息
export const updateUserInfo = (data: Partial<UserInfo>): Promise<void> => {
  return request.put('/user/info', data)
}

// 修改密码
export const updatePassword = (data: {
  oldPassword: string
  newPassword: string
  confirmPassword: string
}): Promise<void> => {
  return request.put('/user/password', data)
}

// 更新手机号
export const updatePhone = (data: {
  phone: string
  code: string
}): Promise<{ user: UserInfo }> => {
  return request.put('/user/phone', data)
}

// 更新邮箱
export const updateEmail = (data: {
  email: string
  code: string
}): Promise<{ user: UserInfo }> => {
  return request.put('/user/email', data)
}

// 上传头像
export const uploadAvatar = (file: File): Promise<{ url: string }> => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request.post('/user/avatar', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取用户统计信息
export const getUserStats = (): Promise<UserStats> => {
  return request.get('/user/stats')
}

// 获取用户收藏列表
export const getFavoriteList = (params?: PageRequest): Promise<PageResponse<Product>> => {
  return request.get('/user/favorites', { params })
}

// 切换收藏状态
export const toggleFavorite = (productId: string | number): Promise<{ isFavorited: boolean }> => {
  return request.post('/user/favorites/toggle', { productId })
}

// 删除账号
export const deleteAccount = (password: string): Promise<void> => {
  return request.delete('/user/delete', { data: { password } })
}