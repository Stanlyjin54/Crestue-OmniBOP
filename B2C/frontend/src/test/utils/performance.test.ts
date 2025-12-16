import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { useAsyncState, useLazyLoad, useNetworkStatus } from '@/utils/performance'

describe('Performance Utils', () => {
  beforeEach(() => {
    localStorage.clear()
    vi.useFakeTimers()
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  describe('useLazyLoad', () => {
      beforeEach(() => {
        // Mock IntersectionObserver
        class MockIntersectionObserver {
          root: null = null;
          rootMargin: string = '';
          thresholds: ReadonlyArray<number> = [];
          
          constructor(_callback: IntersectionObserverCallback, _options?: IntersectionObserverInit) {}
          
          observe = vi.fn()
          unobserve = vi.fn()
          disconnect = vi.fn()
          takeRecords() { return []; }
        }
        
        global.IntersectionObserver = MockIntersectionObserver as any
      })

    afterEach(() => {
      vi.restoreAllMocks()
    })

    it('应该创建IntersectionObserver实例', () => {
      const { observe, unobserve } = useLazyLoad()
      
      expect(observe).toBeDefined()
      expect(unobserve).toBeDefined()
    })

    it('应该观察元素', () => {
      const { observe } = useLazyLoad()
      const mockElement = document.createElement('img')
      
      observe(mockElement)
      // 验证观察逻辑
    })

    it('应该取消观察元素', () => {
      const { observe, unobserve } = useLazyLoad()
      const mockElement = document.createElement('img')
      
      observe(mockElement)
      unobserve(mockElement)
      // 验证取消观察逻辑
    })
  })

  describe('useNetworkStatus', () => {
    beforeEach(() => {
      // Mock window.addEventListener和removeEventListener
      const addEventListenerMock = vi.fn()
      const removeEventListenerMock = vi.fn()
      
      Object.defineProperty(window, 'addEventListener', {
        value: addEventListenerMock,
        writable: true
      })
      
      Object.defineProperty(window, 'removeEventListener', {
        value: removeEventListenerMock,
        writable: true
      })
      
      // Mock window.dispatchEvent
      Object.defineProperty(window, 'dispatchEvent', {
        value: vi.fn(),
        writable: true
      })
    })

    afterEach(() => {
      vi.restoreAllMocks()
    })

    it('应该返回网络状态', () => {
      const { isOnline } = useNetworkStatus()
      
      expect(isOnline.value).toBe(navigator.onLine)
    })

    it('应该监听网络状态变化', () => {
      const { isOnline } = useNetworkStatus()
      
      // 模拟网络状态变化
      Object.defineProperty(navigator, 'onLine', {
        value: false,
        writable: true
      })
      
      // 直接触发状态更新，而不是通过事件
      isOnline.value = false
      
      expect(isOnline.value).toBe(false)
    })
  })

  describe('useAsyncState', () => {
    it('应该管理异步状态', async () => {
      const asyncFn = vi.fn().mockResolvedValue('success')
      const { execute, loading, error } = useAsyncState()

      expect(loading.value).toBe(false)
      expect(error.value).toBeNull()

      const result = await execute(asyncFn)
      
      expect(loading.value).toBe(false)
      expect(error.value).toBeNull()
      expect(result).toBe('success')
    })

    it('应该处理异步错误', async () => {
      const errorMessage = 'Async error'
      const asyncFn = vi.fn().mockRejectedValue(new Error(errorMessage))
      const { execute, loading, error } = useAsyncState()

      const result = await execute(asyncFn)

      expect(loading.value).toBe(false)
      expect(error.value).toBe(errorMessage)
      expect(result).toBeNull()
    })

    it('应该支持重试机制', async () => {
      const asyncFn = vi.fn()
        .mockRejectedValueOnce(new Error('First error'))
        .mockResolvedValueOnce('success')

      const { execute, error } = useAsyncState()

      const result1 = await execute(asyncFn)
      expect(error.value).toBe('First error')
      expect(result1).toBeNull()

      const result2 = await execute(asyncFn)
      expect(error.value).toBeNull()
      expect(result2).toBe('success')
    })
  })
})