import { describe, it, expect, vi } from 'vitest'
import { useListCache, usePageCache } from '@/utils/cache'
import { useAsyncState, useNetworkStatus } from '@/utils/performance'

describe('Integration Tests', () => {
  describe('Performance and Network Integration', () => {
    it('应该在使用缓存时正确处理网络状态', async () => {
      const networkStatus = useNetworkStatus()
      const cache = useListCache('integration-test', 5000)
      
      // 模拟在线状态
      Object.defineProperty(navigator, 'onLine', {
        value: true,
        writable: true
      })
      
      const testData = [{ id: 1, name: '测试商品' }]
      cache.set(testData)
      
      // 验证缓存可用
      expect(cache.get()).toEqual(testData)
      expect(networkStatus.isOnline.value).toBe(true)
    })

    it('应该在网络离线时使用缓存数据', async () => {
      const cache = useListCache('offline-test', 5000)
      
      // 先设置缓存数据
      const testData = [{ id: 1, name: '离线商品' }]
      cache.set(testData)
      
      // 模拟网络离线
      Object.defineProperty(navigator, 'onLine', {
        value: false,
        writable: true
      })
      
      // 验证缓存数据仍然可用
      const cachedData = cache.get()
      expect(cachedData).toEqual(testData)
    })

    it('应该在异步状态中正确处理缓存', async () => {
      const cache = useListCache('async-cache-test', 1000)
      const asyncData = { items: ['item1', 'item2'], total: 2 }
      
      const mockAsyncFn = vi.fn().mockImplementation(() => {
        return new Promise(resolve => {
          setTimeout(() => {
            cache.set(asyncData.items)
            resolve(asyncData)
          }, 100)
        })
      })
      
      const { execute, loading, data } = useAsyncState(mockAsyncFn)
      
      // 执行异步操作
      const promise = execute()
      expect(loading.value).toBe(true)
      
      await promise
      
      expect(loading.value).toBe(false)
      expect(data.value).toEqual(asyncData)
      expect(cache.get()).toEqual(asyncData.items)
    })
  })

  describe('Component and Store Integration', () => {
    it('应该在组件中正确使用性能工具', async () => {
      // 模拟组件使用性能工具的场景
      const cache = usePageCache('component-cache', 3000)
      const networkStatus = useNetworkStatus()
      
      // 模拟组件挂载时的行为
      const componentData = { products: [], loading: true, error: null }
      
      // 检查缓存
      const cachedData = cache.get()
      if (cachedData && networkStatus.isOnline.value) {
        componentData.products = cachedData
        componentData.loading = false
      } else {
        // 模拟API调用
        try {
          const mockProducts = [{ id: 1, name: '产品1' }, { id: 2, name: '产品2' }]
          cache.set(mockProducts)
          componentData.products = mockProducts
          componentData.loading = false
        } catch (error) {
          componentData.error = error
          componentData.loading = false
        }
      }
      
      expect(componentData.loading).toBe(false)
      expect(componentData.products).toHaveLength(2)
      expect(componentData.error).toBeNull()
    })

    it('应该处理错误状态和重试机制', async () => {
      const cache = useListCache('error-retry-test', 5000)
      let attemptCount = 0
      
      const mockFailingFn = vi.fn().mockImplementation(() => {
        attemptCount++
        if (attemptCount === 1) {
          return Promise.reject(new Error('网络错误'))
        }
        return Promise.resolve([{ id: 1, name: '重试成功' }])
      })
      
      const { execute, error, data } = useAsyncState(mockFailingFn)
      
      // 第一次执行 - 应该失败
      await execute()
      expect(error.value).toBeInstanceOf(Error)
      expect(error.value?.message).toBe('网络错误')
      
      // 第二次执行 - 应该成功
      await execute()
      expect(error.value).toBeNull()
      expect(data.value).toEqual([{ id: 1, name: '重试成功' }])
      expect(attemptCount).toBe(2)
    })
  })

  describe('Real-world Scenarios', () => {
    it('应该模拟商品列表页面的完整流程', async () => {
      // 1. 初始化缓存和网络状态
      const productsCache = useListCache('products-integration', 300000) // 5分钟
      const categoriesCache = useListCache('categories-integration', 600000) // 10分钟
      const networkStatus = useNetworkStatus()
      
      // 2. 模拟页面加载
      const pageState = {
        products: [],
        categories: [],
        loading: true,
        error: null,
        currentPage: 1,
        pageSize: 20
      }
      
      // 3. 检查缓存
      const cachedProducts = productsCache.get()
      const cachedCategories = categoriesCache.get()
      
      if (cachedProducts && cachedCategories && networkStatus.isOnline.value) {
        // 使用缓存数据
        pageState.products = cachedProducts
        pageState.categories = cachedCategories
        pageState.loading = false
      } else {
        // 4. 模拟API调用
        try {
          // 模拟并发请求
          const [productsResponse, categoriesResponse] = await Promise.all([
            Promise.resolve({
              data: [
                { id: 1, name: '商品1', price: 99.99 },
                { id: 2, name: '商品2', price: 199.99 }
              ],
              total: 2
            }),
            Promise.resolve([
              { id: 1, name: '分类1', children: [] },
              { id: 2, name: '分类2', children: [] }
            ])
          ])
          
          // 更新缓存
          productsCache.set(productsResponse.data)
          categoriesCache.set(categoriesResponse)
          
          pageState.products = productsResponse.data
          pageState.categories = categoriesResponse
          pageState.loading = false
        } catch (error) {
          pageState.error = error
          pageState.loading = false
        }
      }
      
      // 5. 验证结果
      expect(pageState.loading).toBe(false)
      expect(pageState.error).toBeNull()
      expect(pageState.products).toHaveLength(2)
      expect(pageState.categories).toHaveLength(2)
      expect(productsCache.get()).toEqual(pageState.products)
      expect(categoriesCache.get()).toEqual(pageState.categories)
    })

    it('应该模拟网络状态变化的处理', async () => {
      const cache = useListCache('network-change-test', 10000)
      const networkStatus = useNetworkStatus()
      
      // 初始在线状态
      Object.defineProperty(navigator, 'onLine', {
        value: true,
        writable: true
      })
      
      // 设置缓存数据
      const onlineData = [{ id: 1, name: '在线数据' }]
      cache.set(onlineData)
      
      // 验证在线状态
      expect(networkStatus.isOnline.value).toBe(true)
      expect(cache.get()).toEqual(onlineData)
      
      // 模拟网络离线
      Object.defineProperty(navigator, 'onLine', {
        value: false,
        writable: true
      })
      
      // 触发网络状态变化事件
      window.dispatchEvent(new Event('offline'))
      
      // 验证离线状态和数据可用性
      expect(networkStatus.isOnline.value).toBe(false)
      expect(cache.get()).toEqual(onlineData) // 缓存数据仍然可用
      
      // 模拟网络恢复
      Object.defineProperty(navigator, 'onLine', {
        value: true,
        writable: true
      })
      
      // 触发网络状态变化事件
      window.dispatchEvent(new Event('online'))
      
      // 验证网络恢复
      expect(networkStatus.isOnline.value).toBe(true)
    })
  })
})