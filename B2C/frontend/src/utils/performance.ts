import { ref } from 'vue'
import { ElMessage } from 'element-plus'

/**
 * 异步操作状态管理
 */
export function useAsyncState() {
  const loading = ref(false)
  const error = ref<string | null>(null)

  const execute = async <T>(asyncFunction: () => Promise<T>): Promise<T | null> => {
    loading.value = true
    error.value = null
    
    try {
      const result = await asyncFunction()
      return result
    } catch (err) {
      const message = err instanceof Error ? err.message : '操作失败'
      error.value = message
      ElMessage.error(message)
      return null
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    error,
    execute
  }
}

/**
 * 防抖函数
 */
export function debounce<T extends (...args: any[]) => any>(
  func: T,
  wait: number
): (...args: Parameters<T>) => void {
  let timeout: number | null = null
  
  return function (...args: Parameters<T>) {
    if (timeout) clearTimeout(timeout)
    timeout = setTimeout(() => {
      func(...args)
    }, wait)
  }
}

/**
 * 节流函数
 */
export function throttle<T extends (...args: any[]) => any>(
  func: T,
  limit: number
): (...args: Parameters<T>) => void {
  let inThrottle = false
  
  return function (...args: Parameters<T>) {
    if (!inThrottle) {
      func(...args)
      inThrottle = true
      setTimeout(() => {
        inThrottle = false
      }, limit)
    }
  }
}

/**
 * 图片懒加载
 */
export function useLazyLoad() {
  // 确保 IntersectionObserver 存在
  if (typeof window !== 'undefined' && !window.IntersectionObserver) {
    (window as any).IntersectionObserver = class IntersectionObserver {
      constructor() {}
      observe() {}
      unobserve() {}
      disconnect() {}
    }
  }

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const img = entry.target as HTMLImageElement
          const src = img.dataset.src
          
          if (src) {
            img.src = src
            img.removeAttribute('data-src')
            observer.unobserve(img)
          }
        }
      })
    },
    {
      rootMargin: '50px 0px',
      threshold: 0.01
    }
  )

  const observe = (element: HTMLImageElement) => {
    observer.observe(element)
  }

  const unobserve = (element: HTMLImageElement) => {
    observer.unobserve(element)
  }

  return {
    observe,
    unobserve
  }
}

/**
 * 本地存储管理
 */
export function useLocalStorage<T>(key: string, defaultValue: T) {
  const get = (): T => {
    try {
      const item = localStorage.getItem(key)
      return item ? JSON.parse(item) : defaultValue
    } catch (error) {
      console.error(`Error reading localStorage key "${key}":`, error)
      return defaultValue
    }
  }

  const set = (value: T): void => {
    try {
      localStorage.setItem(key, JSON.stringify(value))
    } catch (error) {
      console.error(`Error setting localStorage key "${key}":`, error)
    }
  }

  const remove = (): void => {
    try {
      localStorage.removeItem(key)
    } catch (error) {
      console.error(`Error removing localStorage key "${key}":`, error)
    }
  }

  return {
    get,
    set,
    remove
  }
}

/**
 * 页面可见性变化监听
 */
export function usePageVisibility() {
  const isVisible = ref(!document.hidden)

  const handleVisibilityChange = () => {
    isVisible.value = !document.hidden
  }

  document.addEventListener('visibilitychange', handleVisibilityChange)

  const cleanup = () => {
    document.removeEventListener('visibilitychange', handleVisibilityChange)
  }

  return {
    isVisible,
    cleanup
  }
}

/**
 * 网络状态监听
 */
export function useNetworkStatus() {
  // 检查是否在浏览器环境中且有必要的API
  const isBrowser = typeof window !== 'undefined' && 
                   typeof navigator !== 'undefined' &&
                   typeof window.addEventListener === 'function' &&
                   typeof window.removeEventListener === 'function'
  const isOnline = ref(isBrowser ? navigator.onLine : true)

  const updateNetworkStatus = () => {
    if (isBrowser) {
      isOnline.value = navigator.onLine
    }
  }

  // 仅在浏览器环境中添加事件监听器
  if (isBrowser) {
    window.addEventListener('online', updateNetworkStatus)
    window.addEventListener('offline', updateNetworkStatus)
  }

  const cleanup = () => {
    // 仅在浏览器环境中移除事件监听器
    if (isBrowser) {
      window.removeEventListener('online', updateNetworkStatus)
      window.removeEventListener('offline', updateNetworkStatus)
    }
  }

  return {
    isOnline,
    cleanup
  }
}

/**
 * 性能监控
 */
export function usePerformanceMonitor() {
  const measureTime = (name: string, fn: () => void) => {
    const start = performance.now()
    fn()
    const end = performance.now()
    console.log(`${name} took ${end - start} milliseconds`)
    return end - start
  }

  const measureAsyncTime = async <T>(name: string, fn: () => Promise<T>): Promise<T> => {
    const start = performance.now()
    const result = await fn()
    const end = performance.now()
    console.log(`${name} took ${end - start} milliseconds`)
    return result
  }

  return {
    measureTime,
    measureAsyncTime
  }
}