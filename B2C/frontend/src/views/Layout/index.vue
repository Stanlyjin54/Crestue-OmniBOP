<template>
  <div class="layout-container">
    <!-- 头部导航 -->
    <header class="header">
      <div class="header-content">
        <!-- Logo -->
        <div class="logo" @click="router.push('/home')">
          <img src="/logo.png" alt="B2C商城" />
          <span>B2C商城</span>
        </div>
        
        <!-- 搜索框 -->
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索商品"
            size="large"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
        
        <!-- 导航菜单 -->
        <nav class="nav-menu">
          <router-link to="/home" class="nav-item">首页</router-link>
          <router-link to="/category" class="nav-item">分类</router-link>
          <router-link to="/cart" class="nav-item cart-item">
            购物车
            <el-badge :value="cartStore.totalCount" :hidden="cartStore.totalCount === 0">
              <el-icon><ShoppingCart /></el-icon>
            </el-badge>
          </router-link>
          <div class="nav-item user-menu" v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleUserCommand">
              <span class="dropdown-trigger">
                <el-avatar :src="userStore.userAvatar" :size="32">
                  {{ userStore.userName?.charAt(0) }}
                </el-avatar>
                <span class="username">{{ userStore.userName }}</span>
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                  <el-dropdown-item command="favorites">我的收藏</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div class="nav-item auth-menu" v-else>
            <router-link to="/auth/login" class="auth-link">登录</router-link>
            <span class="divider">|</span>
            <router-link to="/auth/register" class="auth-link">注册</router-link>
          </div>
        </nav>
      </div>
    </header>
    
    <!-- 主要内容 -->
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <keep-alive>
          <component :is="Component" v-if="route.meta.keepAlive" />
        </keep-alive>
        <component :is="Component" v-if="!route.meta.keepAlive" />
      </router-view>
    </main>
    
    <!-- 底部 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h4>购物指南</h4>
          <ul>
            <li><a href="#">购物流程</a></li>
            <li><a href="#">会员介绍</a></li>
            <li><a href="#">生活旅行</a></li>
            <li><a href="#">常见问题</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>配送方式</h4>
          <ul>
            <li><a href="#">上门自提</a></li>
            <li><a href="#">211限时达</a></li>
            <li><a href="#">配送服务查询</a></li>
            <li><a href="#">配送费收取标准</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>支付方式</h4>
          <ul>
            <li><a href="#">货到付款</a></li>
            <li><a href="#">在线支付</a></li>
            <li><a href="#">分期付款</a></li>
            <li><a href="#">邮局汇款</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>售后服务</h4>
          <ul>
            <li><a href="#">售后政策</a></li>
            <li><a href="#">价格保护</a></li>
            <li><a href="#">退款说明</a></li>
            <li><a href="#">返修/退换货</a></li>
          </ul>
        </div>
        <div class="footer-section contact">
          <h4>联系我们</h4>
          <p>客服热线：400-123-4567</p>
          <p>工作时间：9:00-22:00</p>
          <p>客服邮箱：service@b2c.com</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 B2C商城. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
// 不需要单独导入图标，main.ts中已全局注册

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()

const searchKeyword = ref('')

// 方法
const handleSearch = () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  router.push(`/search?keyword=${encodeURIComponent(searchKeyword.value.trim())}`)
}

const handleUserCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'orders':
      router.push('/user/orders')
      break
    case 'favorites':
      router.push('/user/favorites')
      break
    case 'logout':
      userStore.logout()
      ElMessage.success('退出登录成功')
      router.push('/home')
      break
  }
}

// 初始化购物车数据
onMounted(async () => {
  if (userStore.isLoggedIn) {
    try {
      await cartStore.fetchCart()
    } catch (error) {
      console.error('获取购物车数据失败:', error)
    }
  }
})
</script>

<style scoped lang="scss">
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
  
  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 16px 20px;
    display: flex;
    align-items: center;
    gap: 40px;
  }
  
  .logo {
    display: flex;
    align-items: center;
    gap: 12px;
    cursor: pointer;
    font-size: 20px;
    font-weight: bold;
    color: #3b82f6;
    
    img {
      height: 32px;
    }
  }
  
  .search-box {
    flex: 1;
    max-width: 500px;
    
    .el-input {
      border-radius: 24px;
      overflow: hidden;
      
      :deep(.el-input__wrapper) {
        border-radius: 24px;
      }
    }
  }
  
  .nav-menu {
    display: flex;
    align-items: center;
    gap: 24px;
    
    .nav-item {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #334155;
      text-decoration: none;
      font-weight: 500;
      transition: color 0.3s;
      
      &:hover {
        color: #3b82f6;
      }
      
      &.router-link-active {
        color: #3b82f6;
      }
      
      &.cart-item {
        position: relative;
        
        .el-badge {
          :deep(.el-badge__content) {
            background-color: #ef4444;
          }
        }
      }
    }
    
    .user-menu {
      .dropdown-trigger {
        display: flex;
        align-items: center;
        gap: 8px;
        cursor: pointer;
        
        .username {
          font-size: 14px;
          color: #334155;
        }
        
        .el-icon {
          font-size: 12px;
          color: #64748b;
        }
      }
    }
    
    .auth-menu {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .auth-link {
        color: #334155;
        text-decoration: none;
        font-size: 14px;
        
        &:hover {
          color: #3b82f6;
        }
      }
      
      .divider {
        color: #cbd5e1;
      }
    }
  }
}

.main-content {
  flex: 1;
  background: #f8fafc;
}

.footer {
  background: #1e293b;
  color: white;
  padding: 40px 0 0;
  
  .footer-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 40px;
  }
  
  .footer-section {
    h4 {
      font-size: 16px;
      margin-bottom: 16px;
      color: #f1f5f9;
    }
    
    ul {
      list-style: none;
      
      li {
        margin-bottom: 8px;
        
        a {
          color: #cbd5e1;
          text-decoration: none;
          font-size: 14px;
          
          &:hover {
            color: #3b82f6;
          }
        }
      }
    }
    
    p {
      color: #cbd5e1;
      font-size: 14px;
      margin-bottom: 4px;
    }
  }
  
  .footer-bottom {
    text-align: center;
    padding: 20px;
    border-top: 1px solid #334155;
    margin-top: 40px;
    
    p {
      color: #94a3b8;
      font-size: 14px;
    }
  }
}

@media (max-width: 768px) {
  .header {
    .header-content {
      flex-direction: column;
      gap: 16px;
      padding: 12px 16px;
    }
    
    .search-box {
      width: 100%;
      max-width: none;
    }
    
    .nav-menu {
      width: 100%;
      justify-content: space-around;
      gap: 12px;
      
      .nav-item {
        font-size: 14px;
      }
    }
  }
  
  .footer {
    .footer-content {
      grid-template-columns: 1fr;
      text-align: center;
      gap: 24px;
    }
  }
}
</style>