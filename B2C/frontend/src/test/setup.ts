import { vi } from 'vitest'
import { config } from '@vue/test-utils'

// Mock @popperjs/core to fix CommonJS module import issue
vi.mock('@popperjs/core', () => {
  const placements = {
    auto: 'auto',
    top: 'top',
    bottom: 'bottom',
    left: 'left',
    right: 'right',
    topStart: 'top-start',
    topEnd: 'top-end',
    bottomStart: 'bottom-start',
    bottomEnd: 'bottom-end',
    leftStart: 'left-start',
    leftEnd: 'left-end',
    rightStart: 'right-start',
    rightEnd: 'right-end'
  }
  
  // For CommonJS modules, we need to export as module.exports
  return {
    placements,
    default: placements,
    __esModule: true
  }
})

// 添加 Node.js 全局类型声明
declare global {
  var localStorage: Storage
  var sessionStorage: Storage
  var IntersectionObserver: typeof IntersectionObserver
  var console: Console
  var window: Window & typeof globalThis
  var global: typeof globalThis
}

// 全局mock
vi.mock('vue-router', () => ({
  useRouter: vi.fn(() => ({
    push: vi.fn(),
    replace: vi.fn(),
    go: vi.fn(),
    back: vi.fn(),
    forward: vi.fn()
  })),
  useRoute: vi.fn(() => ({
    params: {},
    query: {},
    path: '/',
    name: 'Home'
  }))
}))

vi.mock('element-plus', () => ({
  ElMessage: {
    success: vi.fn(),
    error: vi.fn(),
    warning: vi.fn(),
    info: vi.fn()
  },
  ElMessageBox: {
    confirm: vi.fn(() => Promise.resolve()),
    alert: vi.fn(() => Promise.resolve())
  },
  ElNotification: {
    success: vi.fn(),
    error: vi.fn(),
    warning: vi.fn(),
    info: vi.fn()
  },
  ElButton: {
    install: vi.fn()
  },
  ElInput: {
    install: vi.fn()
  }
}))

// 配置Vue Test Utils (使用mock的ElementPlus)
config.global.plugins = []

// 全局测试工具
;(global as any).console = {
  ...console,
  warn: vi.fn(),
  error: vi.fn()
}

// 模拟 IntersectionObserver
class MockIntersectionObserver {
  constructor(_callback?: IntersectionObserverCallback, _options?: IntersectionObserverInit) {}
  observe() {}
  unobserve() {}
  disconnect() {}
}

// 设置全局 IntersectionObserver
;(global as any).IntersectionObserver = MockIntersectionObserver

// Mock localStorage and sessionStorage
const localStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn()
}

const sessionStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn()
}

;(global as any).localStorage = localStorageMock
;(global as any).sessionStorage = sessionStorageMock

// Mock window对象
;(global as any).window = {
  ...(global as any).window,
  location: {
    href: 'http://localhost:3000',
    origin: 'http://localhost:3000',
    pathname: '/',
    search: '',
    hash: ''
  },
  navigator: {
    onLine: true,
    userAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
  }
}