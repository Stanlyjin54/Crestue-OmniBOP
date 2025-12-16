<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <h2>我的主页</h2>
      <p class="welcome-text">欢迎回来，{{ userInfo?.nickname || userInfo?.username || '用户' }}！</p>
    </div>

    <div class="dashboard-content">
      <!-- 快捷操作 -->
      <div class="quick-actions">
        <h3>快捷操作</h3>
        <div class="action-grid">
          <div class="action-item" @click="$router.push('/user/orders')">
            <div class="action-icon">
              <el-icon size="24"><Document /></el-icon>
            </div>
            <div class="action-info">
              <span class="action-title">我的订单</span>
              <span class="action-desc">查看订单状态</span>
            </div>
          </div>
          <div class="action-item" @click="$router.push('/user/favorites')">
            <div class="action-icon">
              <el-icon size="24"><Star /></el-icon>
            </div>
            <div class="action-info">
              <span class="action-title">我的收藏</span>
              <span class="action-desc">{{ userStats.favoriteCount }} 个商品</span>
            </div>
          </div>
          <div class="action-item" @click="$router.push('/user/addresses')">
            <div class="action-icon">
              <el-icon size="24"><Location /></el-icon>
            </div>
            <div class="action-info">
              <span class="action-title">收货地址</span>
              <span class="action-desc">管理地址信息</span>
            </div>
          </div>
          <div class="action-item" @click="$router.push('/cart')">
            <div class="action-icon">
              <el-icon size="24"><ShoppingCart /></el-icon>
            </div>
            <div class="action-info">
              <span class="action-title">购物车</span>
              <span class="action-desc">{{ cartItemCount }} 件商品</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 订单统计 -->
      <div class="stats-section">
        <h3>订单统计</h3>
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-icon">
              <el-icon size="32"><Document /></el-icon>
            </div>
            <div class="stat-content">
              <span class="stat-number">{{ userStats.orderCount }}</span>
              <span class="stat-label">总订单数</span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <el-icon size="32"><Money /></el-icon>
            </div>
            <div class="stat-content">
              <span class="stat-number">¥{{ formatAmount(userStats.totalSpent) }}</span>
              <span class="stat-label">累计消费</span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <el-icon size="32"><Star /></el-icon>
            </div>
            <div class="stat-content">
              <span class="stat-number">{{ userStats.favoriteCount }}</span>
              <span class="stat-label">收藏商品</span>
            </div>
          </div>
          <div class="stat-card">
            <div class="stat-icon">
              <el-icon size="32"><Ticket /></el-icon>
            </div>
            <div class="stat-content">
              <span class="stat-number">{{ userStats.couponCount || 0 }}</span>
              <span class="stat-label">可用优惠券</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 最近订单 -->
      <div class="recent-orders" v-if="recentOrders.length">
        <div class="section-header">
          <h3>最近订单</h3>
          <el-button type="text" @click="$router.push('/user/orders')">
            查看全部
          </el-button>
        </div>
        <div class="order-list">
          <div 
            v-for="order in recentOrders" 
            :key="order.id"
            class="order-item"
            @click="$router.push(`/user/orders/${order.id}`)"
          >
            <div class="order-header">
              <span class="order-no">订单号：{{ order.orderNo }}</span>
              <el-tag :type="getOrderStatusType(order.status)" size="small">
                {{ getOrderStatusText(order.status) }}
              </el-tag>
            </div>
            <div class="order-info">
              <div class="order-products">
                <span>共 {{ order.items.length }} 件商品</span>
                <span class="order-amount">¥{{ order.totalAmount }}</span>
              </div>
              <div class="order-date">
                {{ formatDate(order.createdAt) }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 最近收藏 -->
      <div class="recent-favorites" v-if="recentFavorites.length">
        <div class="section-header">
          <h3>最近收藏</h3>
          <el-button type="text" @click="$router.push('/user/favorites')">
            查看全部
          </el-button>
        </div>
        <div class="favorite-grid">
          <div 
            v-for="item in recentFavorites" 
            :key="item.id"
            class="favorite-item"
            @click="$router.push(`/product/${item.id}`)"
          >
            <img :src="item.images[0]" :alt="item.name" class="favorite-image" />
            <div class="favorite-info">
              <h4 class="favorite-name">{{ item.name }}</h4>
              <p class="favorite-price">¥{{ item.price }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
// 图标已在main.ts中全局注册，无需单独导入
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { getUserStats } from '@/api/user'
import { getOrderList } from '@/api/order'
import { getFavoriteList } from '@/api/user'
import type { UserStats, Order, Product } from '@/types'

const userStore = useUserStore()
const cartStore = useCartStore()

const userStats = ref<UserStats>({
  orderCount: 0,
  totalSpent: 0,
  favoriteCount: 0,
  couponCount: 0,
  points: 0
})

const recentOrders = ref<Order[]>([])
const recentFavorites = ref<Product[]>([])

const userInfo = computed(() => userStore.userInfo)
const cartItemCount = computed(() => cartStore.cartItems.length)

// 格式化金额
const formatAmount = (amount: number | undefined) => {
  return (amount || 0).toFixed(2)
}

// 格式化日期
const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString()
}

// 获取订单状态类型
const getOrderStatusType = (status?: string): "primary" | "success" | "warning" | "info" | "danger" => {
  const statusMap: Record<string, "primary" | "success" | "warning" | "info" | "danger"> = {
    'pending': 'warning',
    'paid': 'success',
    'shipped': 'info',
    'completed': 'success',
    'cancelled': 'danger'
  }
  return statusMap[status || ''] || 'info'
}

// 获取订单状态文本
const getOrderStatusText = (status?: string): string => {
  const statusMap: Record<string, string> = {
    'pending': '待付款',
    'paid': '已付款',
    'shipped': '已发货',
    'completed': '已完成',
    'cancelled': '已取消'
  }
  return statusMap[status || ''] || '未知'
}

// 加载用户统计
const loadUserStats = async () => {
  try {
    const res = await getUserStats()
    if (res) {
      userStats.value = res
    }
  } catch (error) {
    console.error('加载用户统计失败:', error)
  }
}

// 加载最近订单
const loadRecentOrders = async () => {
  try {
    const res = await getOrderList({ page: 1, pageSize: 5 })
    if (res) {
      recentOrders.value = res.list
    }
  } catch (error) {
    console.error('加载最近订单失败:', error)
  }
}

// 加载最近收藏
const loadRecentFavorites = async () => {
  try {
    const res = await getFavoriteList({ page: 1, pageSize: 6 })
    if (res) {
      recentFavorites.value = res.list
    }
  } catch (error) {
    console.error('加载最近收藏失败:', error)
  }
}

onMounted(() => {
  loadUserStats()
  loadRecentOrders()
  loadRecentFavorites()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
}

.dashboard-header {
  margin-bottom: 30px;
  
  h2 {
    margin: 0 0 10px;
    font-size: 24px;
    font-weight: 600;
    color: #303133;
  }
  
  .welcome-text {
    margin: 0;
    color: #909399;
    font-size: 14px;
  }
}

.dashboard-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.quick-actions {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  h3 {
    margin: 0 0 20px;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }
  
  .action-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
  }
  
  .action-item {
    display: flex;
    align-items: center;
    gap: 15px;
    padding: 20px;
    background: #f5f7fa;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      background: #e6f7ff;
      transform: translateY(-2px);
    }
    
    .action-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 48px;
      height: 48px;
      background: #409eff;
      color: #fff;
      border-radius: 8px;
    }
    
    .action-info {
      flex: 1;
      
      .action-title {
        display: block;
        font-size: 16px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .action-desc {
        display: block;
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.stats-section {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  h3 {
    margin: 0 0 20px;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }
  
  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
  }
  
  .stat-card {
    display: flex;
    align-items: center;
    gap: 15px;
    padding: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    border-radius: 8px;
    
    .stat-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 48px;
      height: 48px;
      background: rgba(255, 255, 255, 0.2);
      border-radius: 8px;
    }
    
    .stat-content {
      flex: 1;
      
      .stat-number {
        display: block;
        font-size: 24px;
        font-weight: 600;
        margin-bottom: 4px;
      }
      
      .stat-label {
        display: block;
        font-size: 12px;
        opacity: 0.8;
      }
    }
  }
}

.recent-orders,
.recent-favorites {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
  }
  
  .order-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
  }
  
  .order-item {
    padding: 15px;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      border-color: #409eff;
      background: #f5f7fa;
    }
    
    .order-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
      
      .order-no {
        font-size: 14px;
        color: #606266;
      }
    }
    
    .order-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .order-products {
        display: flex;
        align-items: center;
        gap: 10px;
        
        .order-amount {
          font-size: 16px;
          font-weight: 600;
          color: #f56c6c;
        }
      }
      
      .order-date {
        font-size: 12px;
        color: #909399;
      }
    }
  }
  
  .favorite-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }
  
  .favorite-item {
    padding: 10px;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      border-color: #409eff;
      transform: translateY(-2px);
    }
    
    .favorite-image {
      width: 100%;
      height: 120px;
      object-fit: cover;
      border-radius: 4px;
      margin-bottom: 8px;
    }
    
    .favorite-info {
      .favorite-name {
        margin: 0 0 4px;
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .favorite-price {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
        color: #f56c6c;
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .dashboard-content {
    .quick-actions .action-grid,
    .stats-section .stats-grid {
      grid-template-columns: repeat(2, 1fr);
    }
    
    .recent-favorites .favorite-grid {
      grid-template-columns: repeat(2, 1fr);
    }
  }
}

@media (max-width: 480px) {
  .dashboard-content {
    .quick-actions .action-grid,
    .stats-section .stats-grid {
      grid-template-columns: 1fr;
    }
    
    .recent-favorites .favorite-grid {
      grid-template-columns: repeat(2, 1fr);
    }
  }
}
</style>