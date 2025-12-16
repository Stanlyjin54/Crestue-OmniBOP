import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '@/utils/auth'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 添加token
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response
    
    // 处理业务逻辑错误
    if (data.code !== 200) {
      ElMessage.error(data.message || '请求失败')
      
      // 处理特定错误码
      if (data.code === 401) {
        // token过期或无效
        removeToken()
        window.location.href = '/login'
      }
      
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    
    return data
  },
  (error) => {
    console.error('响应错误:', error)
    
    let message = '网络错误'
    
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求错误'
          break
        case 401:
          message = '未授权，请登录'
          removeToken()
          window.location.href = '/login'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        case 502:
          message = '网关错误'
          break
        case 503:
          message = '服务不可用'
          break
        case 504:
          message = '网关超时'
          break
        default:
          message = '未知错误'
      }
    } else if (error.request) {
      message = '网络错误，请检查网络连接'
    }
    
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

// 封装请求方法
export const request = {
  get: <T = any>(url: string, config?: AxiosRequestConfig) => {
    return service.get(url, config) as unknown as Promise<T>
  },
  
  post: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) => {
    return service.post(url, data, config) as unknown as Promise<T>
  },
  
  put: <T = any>(url: string, data?: any, config?: AxiosRequestConfig) => {
    return service.put(url, data, config) as unknown as Promise<T>
  },
  
  delete: <T = any>(url: string, config?: AxiosRequestConfig) => {
    return service.delete(url, config) as unknown as Promise<T>
  }
}

export default service