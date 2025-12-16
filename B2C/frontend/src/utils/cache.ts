import { ref, onActivated } from 'vue'
import type { Ref } from 'vue'

interface CacheOptions {
  key?: string
  expireTime?: number // 缓存过期时间（毫秒）
  immediate?: boolean // 是否立即执行
}

interface CachedData<T> {
  data: T
  timestamp: number
}

/**
 * 页面数据缓存
 */
export function usePageCache<T>(
  fetchData: () => Promise<T>,
  options: CacheOptions = {}
) {
  const {
    key = 'page-cache',
    expireTime = 5 * 60 * 1000, // 默认5分钟过期
    immediate = true
  } = options

  const data: Ref<T | null> = ref(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  // 生成缓存键
  const getCacheKey = () => `cache_${key}`

  // 检查缓存是否过期
  const isCacheExpired = (cached: CachedData<T>) => {
    return Date.now() - cached.timestamp > expireTime
  }

  // 从缓存获取数据
  const getCachedData = (): T | null => {
    try {
      const cachedStr = sessionStorage.getItem(getCacheKey())
      if (!cachedStr) return null

      const cached: CachedData<T> = JSON.parse(cachedStr)
      
      if (isCacheExpired(cached)) {
        sessionStorage.removeItem(getCacheKey())
        return null
      }

      return cached.data
    } catch (error) {
      console.error('Error reading cache:', error)
      return null
    }
  }

  // 保存数据到缓存
  const setCachedData = (value: T): void => {
    try {
      const cached: CachedData<T> = {
        data: value,
        timestamp: Date.now()
      }
      sessionStorage.setItem(getCacheKey(), JSON.stringify(cached))
    } catch (error) {
      console.error('Error saving cache:', error)
    }
  }

  // 清除缓存
  const clearCache = (): void => {
    try {
      sessionStorage.removeItem(getCacheKey())
    } catch (error) {
      console.error('Error clearing cache:', error)
    }
  }

  // 加载数据
  const loadData = async (useCache = true): Promise<void> => {
    loading.value = true
    error.value = null

    try {
      // 尝试从缓存获取
      if (useCache) {
        const cachedData = getCachedData()
        if (cachedData) {
          data.value = cachedData
          loading.value = false
          return
        }
      }

      // 获取新数据
      const result = await fetchData()
      data.value = result
      
      // 保存到缓存
      if (useCache) {
        setCachedData(result)
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : '加载失败'
      console.error('Error loading data:', err)
    } finally {
      loading.value = false
    }
  }

  // 刷新数据（不使用缓存）
  const refresh = (): Promise<void> => {
    return loadData(false)
  }

  // 如果组件被激活，重新加载数据
  onActivated(() => {
    // 检查缓存是否仍然有效
    const cachedData = getCachedData()
    if (!cachedData) {
      loadData(false)
    }
  })

  // 如果immediate为true，立即加载数据
  if (immediate) {
    loadData()
  }

  return {
    data,
    loading,
    error,
    loadData,
    refresh,
    clearCache
  }
}

/**
 * 列表数据缓存（支持分页）
 */
export function useListCache<T>(
  fetchData: (page: number, pageSize: number) => Promise<{ list: T[]; total: number }>,
  options: CacheOptions = {}
) {
  const list = ref<T[]>([])
  const total = ref(0)
  const currentPage = ref(1)
  const pageSize = ref(10)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const { key = 'list-cache', expireTime = 5 * 60 * 1000 } = options

  // 生成缓存键
  const getCacheKey = (page: number, size: number) => `cache_${key}_${page}_${size}`

  // 检查缓存是否过期
  const isCacheExpired = (cached: CachedData<{ list: T[]; total: number }>) => {
    return Date.now() - cached.timestamp > expireTime
  }

  // 从缓存获取数据
  const getCachedData = (page: number, size: number) => {
    try {
      const cachedStr = sessionStorage.getItem(getCacheKey(page, size))
      if (!cachedStr) return null

      const cached: CachedData<{ list: T[]; total: number }> = JSON.parse(cachedStr)
      
      if (isCacheExpired(cached)) {
        sessionStorage.removeItem(getCacheKey(page, size))
        return null
      }

      return cached.data
    } catch (error) {
      console.error('Error reading cache:', error)
      return null
    }
  }

  // 保存数据到缓存
  const setCachedData = (page: number, size: number, data: { list: T[]; total: number }) => {
    try {
      const cached: CachedData<{ list: T[]; total: number }> = {
        data,
        timestamp: Date.now()
      }
      sessionStorage.setItem(getCacheKey(page, size), JSON.stringify(cached))
    } catch (error) {
      console.error('Error saving cache:', error)
    }
  }

  // 加载列表数据
  const loadList = async (page = currentPage.value, size = pageSize.value, useCache = true) => {
    loading.value = true
    error.value = null

    try {
      // 尝试从缓存获取
      if (useCache) {
        const cachedData = getCachedData(page, size)
        if (cachedData) {
          list.value = cachedData.list
          total.value = cachedData.total
          currentPage.value = page
          pageSize.value = size
          loading.value = false
          return
        }
      }

      // 获取新数据
      const result = await fetchData(page, size)
      list.value = result.list
      total.value = result.total
      currentPage.value = page
      pageSize.value = size
      
      // 保存到缓存
      if (useCache) {
        setCachedData(page, size, result)
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : '加载失败'
      console.error('Error loading list:', err)
    } finally {
      loading.value = false
    }
  }

  // 刷新当前页数据
  const refresh = () => {
    return loadList(currentPage.value, pageSize.value, false)
  }

  // 改变页码
  const handlePageChange = (page: number) => {
    loadList(page, pageSize.value)
  }

  // 改变页大小
  const handleSizeChange = (size: number) => {
    loadList(1, size)
  }

  return {
    list,
    total,
    currentPage,
    pageSize,
    loading,
    error,
    loadList,
    refresh,
    handlePageChange,
    handleSizeChange
  }
}