import { describe, it, expect, vi, beforeEach } from 'vitest'
import { debounce, throttle, formatPrice, formatDate } from '@/utils'

describe('Utils Functions', () => {
  beforeEach(() => {
    vi.useFakeTimers()
  })

  describe('debounce', () => {
    it('应该在指定延迟后执行函数', () => {
      const fn = vi.fn()
      const debouncedFn = debounce(fn, 100)

      debouncedFn()
      expect(fn).not.toHaveBeenCalled()

      vi.advanceTimersByTime(100)
      expect(fn).toHaveBeenCalledTimes(1)
    })

    it('应该在重复调用时重置计时器', () => {
      const fn = vi.fn()
      const debouncedFn = debounce(fn, 100)

      debouncedFn()
      vi.advanceTimersByTime(50)
      debouncedFn()
      vi.advanceTimersByTime(50)
      
      expect(fn).not.toHaveBeenCalled()

      vi.advanceTimersByTime(50)
      expect(fn).toHaveBeenCalledTimes(1)
    })

    it('应该传递正确的参数', () => {
      const fn = vi.fn()
      const debouncedFn = debounce(fn, 100)

      debouncedFn('arg1', 'arg2')
      vi.advanceTimersByTime(100)

      expect(fn).toHaveBeenCalledWith('arg1', 'arg2')
    })
  })

  describe('throttle', () => {
    it('应该在指定时间内只执行一次', () => {
      const fn = vi.fn()
      const throttledFn = throttle(fn, 100)

      throttledFn()
      throttledFn()
      throttledFn()

      expect(fn).toHaveBeenCalledTimes(1)
    })

    it('应该在时间间隔后再次执行', () => {
      const fn = vi.fn()
      const throttledFn = throttle(fn, 100)

      throttledFn()
      vi.advanceTimersByTime(100)
      throttledFn()

      expect(fn).toHaveBeenCalledTimes(2)
    })
  })

  describe('formatPrice', () => {
    it('应该正确格式化价格', () => {
      expect(formatPrice(1234.56)).toBe('¥1234.56')
      expect(formatPrice(0)).toBe('¥0.00')
      expect(formatPrice(999999.99)).toBe('¥999999.99')
    })

    it('应该处理无效输入', () => {
      expect(formatPrice(NaN)).toBe('¥NaN')
      expect(formatPrice(-100)).toBe('¥-100.00')
    })
  })

  describe('formatDate', () => {
    it('应该正确格式化日期', () => {
      const date = new Date('2023-12-25T10:30:00')
      expect(formatDate(date, 'YYYY-MM-DD')).toBe('2023-12-25')
    })

    it('应该处理无效日期', () => {
      const result = formatDate(new Date('invalid'))
      expect(result).toContain('NaN')
    })
  })
})