import { vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { createApp } from 'vue'
import ElementPlus from 'element-plus'

// 创建测试用的Pinia实例
export const createTestPinia = () => {
  const pinia = createPinia()
  setActivePinia(pinia)
  return pinia
}

// 创建完整的测试应用
export const createTestApp = () => {
  const app = createApp({})
  app.use(ElementPlus)
  return app
}

// 模拟API响应
export const mockApiResponse = <T>(data: T, delay = 0): Promise<T> => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(data), delay)
  })
}

// 模拟错误响应
export const mockApiError = (message = 'API Error', delay = 0): Promise<never> => {
  return new Promise((_, reject) => {
    setTimeout(() => reject(new Error(message)), delay)
  })
}

// 生成测试用的商品数据
export const generateMockProducts = (count = 10) => {
  return Array.from({ length: count }, (_, index) => ({
    id: index + 1,
    name: `测试商品${index + 1}`,
    description: `这是测试商品${index + 1}的描述`,
    price: Math.floor(Math.random() * 1000) + 50,
    originalPrice: Math.floor(Math.random() * 1500) + 100,
    image: `test-image-${index + 1}.jpg`,
    rating: Math.floor(Math.random() * 5) + 1,
    reviewCount: Math.floor(Math.random() * 1000),
    salesCount: Math.floor(Math.random() * 5000),
    specs: `规格${index + 1}`,
    tags: index % 2 === 0 ? ['热销'] : ['新品'],
    categoryId: Math.floor(Math.random() * 5) + 1
  }))
}

// 生成测试用的分类数据
export const generateMockCategories = (count = 5) => {
  return Array.from({ length: count }, (_, index) => ({
    id: index + 1,
    name: `分类${index + 1}`,
    icon: `icon-${index + 1}`,
    children: Array.from({ length: 3 }, (_, childIndex) => ({
      id: (index + 1) * 10 + childIndex + 1,
      name: `子分类${childIndex + 1}`,
      icon: `sub-icon-${childIndex + 1}`
    }))
  }))
}

// 模拟localStorage
export const mockLocalStorage = () => {
  const storage = new Map<string, string>()
  
  return {
    getItem: vi.fn((key: string) => storage.get(key) || null),
    setItem: vi.fn((key: string, value: string) => storage.set(key, value)),
    removeItem: vi.fn((key: string) => storage.delete(key)),
    clear: vi.fn(() => storage.clear()),
    storage
  }
}

// 模拟sessionStorage
export const mockSessionStorage = () => {
  const storage = new Map<string, string>()
  
  return {
    getItem: vi.fn((key: string) => storage.get(key) || null),
    setItem: vi.fn((key: string, value: string) => storage.set(key, value)),
    removeItem: vi.fn((key: string) => storage.delete(key)),
    clear: vi.fn(() => storage.clear()),
    storage
  }
}

// 等待组件更新
export const waitForComponentUpdate = async (wrapper: any, timeout = 1000) => {
  return new Promise<void>((resolve) => {
    setTimeout(() => {
      wrapper.vm.$nextTick(() => resolve())
    }, timeout)
  })
}

// 模拟路由
export const mockRouter = {
  push: vi.fn(),
  replace: vi.fn(),
  go: vi.fn(),
  back: vi.fn(),
  forward: vi.fn(),
  currentRoute: {
    value: {
      path: '/',
      name: 'Home',
      params: {},
      query: {}
    }
  }
}

// 模拟路由实例
export const createMockRouter = () => ({
  ...mockRouter,
  currentRoute: ref({
    path: '/',
    name: 'Home',
    params: {},
    query: {}
  })
})

// 测试工具函数
export const testUtils = {
  // 检查元素是否存在
  elementExists: (wrapper: any, selector: string) => {
    return wrapper.find(selector).exists()
  },
  
  // 检查元素文本
  elementText: (wrapper: any, selector: string) => {
    const element = wrapper.find(selector)
    return element.exists() ? element.text() : ''
  },
  
  // 检查元素数量
  elementCount: (wrapper: any, selector: string) => {
    return wrapper.findAll(selector).length
  },
  
  // 模拟用户输入
  simulateInput: async (wrapper: any, selector: string, value: string) => {
    const input = wrapper.find(selector)
    if (input.exists()) {
      await input.setValue(value)
    }
  },
  
  // 模拟点击事件
  simulateClick: async (wrapper: any, selector: string) => {
    const element = wrapper.find(selector)
    if (element.exists()) {
      await element.trigger('click')
    }
  }
}