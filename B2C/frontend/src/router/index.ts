import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'



// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/Layout/index.vue'),
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/Home/index.vue'),
        meta: { title: '首页' }
      },
      {
        path: '/products',
        name: 'Products',
        component: () => import('@/views/Products.vue'),
        meta: { title: '商品列表' }
      },
      {
        path: '/product/:id',
        name: 'ProductDetail',
        component: () => import('@/views/Product/Detail.vue'),
        meta: { title: '商品详情' }
      },
      {
        path: '/cart',
        name: 'Cart',
        component: () => import('@/views/Cart.vue'),
        meta: { title: '购物车', requiresAuth: true }
      },
      {
        path: '/checkout',
        name: 'Checkout',
        component: () => import('@/views/Order/Confirm.vue'),
        meta: { title: '订单确认', requiresAuth: true }
      },
      {
        path: '/payment',
        name: 'Payment',
        component: () => import('@/views/Order/Payment.vue'),
        meta: { title: '订单支付', requiresAuth: true }
      },
      {
        path: '/order/success/:orderId',
        name: 'OrderSuccess',
        component: () => import('@/views/Order/Success.vue'),
        meta: { title: '支付成功', requiresAuth: true }
      },
      {
        path: '/payment/callback',
        name: 'PaymentCallback',
        component: () => import('@/views/Order/PaymentCallback.vue'),
        meta: { title: '支付回调处理', requiresAuth: true }
      },
      {
        path: '/refund',
        name: 'Refund',
        component: () => import('@/views/Order/Refund.vue'),
        meta: { title: '申请退款', requiresAuth: true }
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('@/views/User/index.vue'),
        redirect: '/user/dashboard',
        meta: { title: '用户中心', requiresAuth: true },
        children: [
          {
            path: 'dashboard',
            name: 'UserDashboard',
            component: () => import('@/views/User/Dashboard.vue'),
            meta: { title: '我的主页' }
          },
          {
            path: 'profile',
            name: 'UserProfile',
            component: () => import('@/views/User/Profile.vue'),
            meta: { title: '个人信息' }
          },
          {
            path: 'settings',
            name: 'UserSettings',
            component: () => import('@/views/User/Settings.vue'),
            meta: { title: '账号设置' }
          },
          {
            path: 'orders',
            name: 'UserOrders',
            component: () => import('@/views/User/Orders.vue'),
            meta: { title: '我的订单' }
          },
          {
            path: 'favorites',
            name: 'UserFavorites',
            component: () => import('@/views/User/Favorites.vue'),
            meta: { title: '我的收藏' }
          },
          {
            path: 'addresses',
            name: 'UserAddresses',
            component: () => import('@/views/User/Addresses.vue'),
            meta: { title: '收货地址' }
          }
        ]
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Auth/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Auth/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/Error/404.vue'),
    meta: { title: '页面不存在' }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(_to, _from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach(async (to, _from, next) => {
  // 开始进度条
  NProgress.start()
  
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - B2C商城` : 'B2C商城'
  
  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      // 游客页面限制
      if (to.meta.guest === false) {
        next('/login')
        return
      }
      
      // 需要登录的页面
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
      return
    }
  }
  
  next()
})

router.afterEach(() => {
  // 结束进度条
  NProgress.done()
})

export default router