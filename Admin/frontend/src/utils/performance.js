// 性能优化工具函数

// 防抖函数
export function debounce(func, wait, immediate = false) {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      timeout = null
      if (!immediate) func.apply(this, args)
    }
    const callNow = immediate && !timeout
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
    if (callNow) func.apply(this, args)
  }
}

// 节流函数
export function throttle(func, limit) {
  let inThrottle
  return function executedFunction(...args) {
    if (!inThrottle) {
      func.apply(this, args)
      inThrottle = true
      setTimeout(() => inThrottle = false, limit)
    }
  }
}

// 懒加载图片
export function lazyLoadImages() {
  if ('IntersectionObserver' in window) {
    const imageObserver = new IntersectionObserver((entries, observer) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          const img = entry.target
          img.src = img.dataset.src
          img.classList.remove('lazy')
          imageObserver.unobserve(img)
        }
      })
    })

    const images = document.querySelectorAll('img[data-src]')
    images.forEach(img => imageObserver.observe(img))
  }
}

// 虚拟滚动
export class VirtualScroller {
  constructor(container, itemHeight, renderItem, totalItems) {
    this.container = container
    this.itemHeight = itemHeight
    this.renderItem = renderItem
    this.totalItems = totalItems
    this.buffer = 5
    this.startIndex = 0
    this.endIndex = 0
    this.visibleItems = []
    
    this.init()
  }

  init() {
    this.container.style.position = 'relative'
    this.container.style.overflow = 'auto'
    
    this.spacer = document.createElement('div')
    this.spacer.style.height = `${this.totalItems * this.itemHeight}px`
    this.container.appendChild(this.spacer)
    
    this.content = document.createElement('div')
    this.content.style.position = 'absolute'
    this.content.style.top = '0'
    this.content.style.left = '0'
    this.content.style.right = '0'
    this.container.appendChild(this.content)
    
    this.container.addEventListener('scroll', this.handleScroll.bind(this))
    this.updateVisibleItems()
  }

  handleScroll() {
    const scrollTop = this.container.scrollTop
    const containerHeight = this.container.clientHeight
    
    this.startIndex = Math.floor(scrollTop / this.itemHeight) - this.buffer
    this.endIndex = Math.ceil((scrollTop + containerHeight) / this.itemHeight) + this.buffer
    
    this.startIndex = Math.max(0, this.startIndex)
    this.endIndex = Math.min(this.totalItems - 1, this.endIndex)
    
    this.updateVisibleItems()
  }

  updateVisibleItems() {
    const startOffset = this.startIndex * this.itemHeight
    this.content.style.transform = `translateY(${startOffset}px)`
    
    this.content.innerHTML = ''
    for (let i = this.startIndex; i <= this.endIndex; i++) {
      if (i < this.totalItems) {
        const item = this.renderItem(i)
        item.style.position = 'absolute'
        item.style.top = `${(i - this.startIndex) * this.itemHeight}px`
        item.style.height = `${this.itemHeight}px`
        item.style.width = '100%'
        this.content.appendChild(item)
      }
    }
  }

  destroy() {
    this.container.removeEventListener('scroll', this.handleScroll)
    this.container.innerHTML = ''
  }
}

// 内存缓存
export class MemoryCache {
  constructor(maxSize = 100, ttl = 5 * 60 * 1000) { // 默认5分钟过期
    this.cache = new Map()
    this.maxSize = maxSize
    this.ttl = ttl
  }

  set(key, value, ttl = this.ttl) {
    if (this.cache.size >= this.maxSize) {
      const firstKey = this.cache.keys().next().value
      this.cache.delete(firstKey)
    }

    const item = {
      value,
      expire: Date.now() + ttl
    }

    this.cache.set(key, item)
  }

  get(key) {
    const item = this.cache.get(key)
    
    if (!item) {
      return null
    }

    if (Date.now() > item.expire) {
      this.cache.delete(key)
      return null
    }

    return item.value
  }

  has(key) {
    const item = this.cache.get(key)
    
    if (!item) {
      return false
    }

    if (Date.now() > item.expire) {
      this.cache.delete(key)
      return false
    }

    return true
  }

  delete(key) {
    return this.cache.delete(key)
  }

  clear() {
    this.cache.clear()
  }

  size() {
    return this.cache.size
  }
}

// 本地存储缓存
export class LocalStorageCache {
  constructor(prefix = 'cache_', ttl = 24 * 60 * 60 * 1000) { // 默认24小时过期
    this.prefix = prefix
    this.ttl = ttl
  }

  _getKey(key) {
    return this.prefix + key
  }

  set(key, value, ttl = this.ttl) {
    try {
      const item = {
        value,
        expire: Date.now() + ttl
      }
      localStorage.setItem(this._getKey(key), JSON.stringify(item))
    } catch (error) {
      console.error('LocalStorage set error:', error)
    }
  }

  get(key) {
    try {
      const itemStr = localStorage.getItem(this._getKey(key))
      
      if (!itemStr) {
        return null
      }

      const item = JSON.parse(itemStr)

      if (Date.now() > item.expire) {
        localStorage.removeItem(this._getKey(key))
        return null
      }

      return item.value
    } catch (error) {
      console.error('LocalStorage get error:', error)
      return null
    }
  }

  has(key) {
    try {
      const itemStr = localStorage.getItem(this._getKey(key))
      
      if (!itemStr) {
        return false
      }

      const item = JSON.parse(itemStr)

      if (Date.now() > item.expire) {
        localStorage.removeItem(this._getKey(key))
        return false
      }

      return true
    } catch (error) {
      console.error('LocalStorage has error:', error)
      return false
    }
  }

  delete(key) {
    try {
      localStorage.removeItem(this._getKey(key))
      return true
    } catch (error) {
      console.error('LocalStorage delete error:', error)
      return false
    }
  }

  clear() {
    try {
      const keys = []
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i)
        if (key && key.startsWith(this.prefix)) {
          keys.push(key)
        }
      }
      keys.forEach(key => localStorage.removeItem(key))
    } catch (error) {
      console.error('LocalStorage clear error:', error)
    }
  }
}

// 分页工具
export class PaginationHelper {
  constructor(pageSize = 20) {
    this.pageSize = pageSize
    this.currentPage = 1
    this.total = 0
    this.data = []
  }

  setData(data, total = null) {
    this.data = data
    this.total = total !== null ? total : data.length
  }

  getPageData(page = this.currentPage) {
    this.currentPage = page
    const start = (page - 1) * this.pageSize
    const end = start + this.pageSize
    return this.data.slice(start, end)
  }

  getTotalPages() {
    return Math.ceil(this.total / this.pageSize)
  }

  hasNextPage() {
    return this.currentPage < this.getTotalPages()
  }

  hasPrevPage() {
    return this.currentPage > 1
  }

  nextPage() {
    if (this.hasNextPage()) {
      this.currentPage++
      return this.getPageData()
    }
    return []
  }

  prevPage() {
    if (this.hasPrevPage()) {
      this.currentPage--
      return this.getPageData()
    }
    return []
  }

  goToPage(page) {
    if (page >= 1 && page <= this.getTotalPages()) {
      return this.getPageData(page)
    }
    return []
  }
}

// 数据预加载
export class DataPreloader {
  constructor() {
    this.preloadQueue = []
    this.isLoading = false
  }

  addToQueue(dataLoader, priority = 0) {
    this.preloadQueue.push({ dataLoader, priority })
    this.preloadQueue.sort((a, b) => b.priority - a.priority)
    this.processQueue()
  }

  async processQueue() {
    if (this.isLoading || this.preloadQueue.length === 0) {
      return
    }

    this.isLoading = true
    const { dataLoader } = this.preloadQueue.shift()

    try {
      await dataLoader()
    } catch (error) {
      console.error('Data preloading failed:', error)
    } finally {
      this.isLoading = false
      this.processQueue()
    }
  }

  clearQueue() {
    this.preloadQueue = []
  }
}

// 组件懒加载
export function lazyLoadComponent(loader) {
  return {
    component: loader,
    loading: () => import('@/components/LoadingComponent.vue'),
    error: () => import('@/components/ErrorComponent.vue'),
    delay: 200,
    timeout: 3000
  }
}

// 路由懒加载
export function lazyLoadRoute(path) {
  return () => import(/* webpackChunkName: "[request]" */ `@/views/${path}`)
}

// 图片懒加载
export function lazyLoadImage(src, placeholder = '/images/placeholder.png') {
  return {
    src: placeholder,
    'data-src': src,
    class: 'lazy-image',
    onload: function() {
      if (this.dataset.src) {
        this.src = this.dataset.src
        this.classList.add('loaded')
      }
    }
  }
}

// 计算密集型任务分片
export function chunkedTask(items, processItem, chunkSize = 100, delay = 0) {
  return new Promise((resolve, reject) => {
    let index = 0
    
    function processChunk() {
      const endIndex = Math.min(index + chunkSize, items.length)
      
      for (let i = index; i < endIndex; i++) {
        try {
          processItem(items[i], i)
        } catch (error) {
          reject(error)
          return
        }
      }
      
      index = endIndex
      
      if (index < items.length) {
        setTimeout(processChunk, delay)
      } else {
        resolve()
      }
    }
    
    processChunk()
  })
}

// 请求去重
export class RequestDeduper {
  constructor() {
    this.pendingRequests = new Map()
  }

  async request(key, requestFn) {
    if (this.pendingRequests.has(key)) {
      return this.pendingRequests.get(key)
    }

    const promise = requestFn()
    this.pendingRequests.set(key, promise)

    try {
      const result = await promise
      return result
    } finally {
      this.pendingRequests.delete(key)
    }
  }

  clear() {
    this.pendingRequests.clear()
  }
}

// 资源加载优化
export class ResourceOptimizer {
  constructor() {
    this.loadedResources = new Set()
    this.loadingResources = new Map()
  }

  async loadResource(url, type = 'script') {
    if (this.loadedResources.has(url)) {
      return Promise.resolve()
    }

    if (this.loadingResources.has(url)) {
      return this.loadingResources.get(url)
    }

    const loadPromise = new Promise((resolve, reject) => {
      let element

      if (type === 'script') {
        element = document.createElement('script')
        element.src = url
        element.async = true
      } else if (type === 'style') {
        element = document.createElement('link')
        element.rel = 'stylesheet'
        element.href = url
      }

      element.onload = () => {
        this.loadedResources.add(url)
        this.loadingResources.delete(url)
        resolve()
      }

      element.onerror = () => {
        this.loadingResources.delete(url)
        reject(new Error(`Failed to load ${type}: ${url}`))
      }

      document.head.appendChild(element)
    })

    this.loadingResources.set(url, loadPromise)
    return loadPromise
  }

  isLoaded(url) {
    return this.loadedResources.has(url)
  }
}

// 性能监控
export class PerformanceMonitor {
  constructor() {
    this.metrics = {}
    this.observers = []
  }

  mark(name) {
    performance.mark(name)
  }

  measure(name, startMark, endMark) {
    performance.measure(name, startMark, endMark)
    const measures = performance.getEntriesByName(name)
    const duration = measures[measures.length - 1].duration
    this.metrics[name] = duration
    return duration
  }

  observe(element, callback) {
    const observer = new PerformanceObserver((list) => {
      for (const entry of list.getEntries()) {
        callback(entry)
      }
    })

    observer.observe({ entryTypes: ['element'] })
    this.observers.push(observer)
  }

  getMetrics() {
    return { ...this.metrics }
  }

  clear() {
    this.metrics = {}
    this.observers.forEach(observer => observer.disconnect())
    this.observers = []
    performance.clearMarks()
    performance.clearMeasures()
  }
}

// 使用示例
export const memoryCache = new MemoryCache(100, 5 * 60 * 1000)
export const localStorageCache = new LocalStorageCache('app_cache_', 24 * 60 * 60 * 1000)
export const dataPreloader = new DataPreloader()
export const requestDeduper = new RequestDeduper()
export const resourceOptimizer = new ResourceOptimizer()
export const performanceMonitor = new PerformanceMonitor()