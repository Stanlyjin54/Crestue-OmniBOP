<template>
  <el-container class="layout-container">
    <!-- 顶部导航栏 -->
    <el-header class="layout-header">
      <div class="header-content">
        <!-- Logo -->
        <div class="logo">
          <router-link to="/">
            <img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjQwIiBoZWlnaHQ9IjQwIiByeD0iOCIgZmlsbD0iIzQwOWVmZiIvPgo8dGV4dCB4PSIyMCIgeT0iMjgiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxOCIgZm9udC13ZWlnaHQ9ImJvbGQiIGZpbGw9IndoaXRlIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIj5CMkM8L3RleHQ+Cjwvc3ZnPgo=" alt="B2C商城" />
            <span>B2C商城</span>
          </router-link>
        </div>
        
        <!-- 搜索栏 -->
        <div class="search-section">
          <SearchBar />
        </div>
        
        <!-- 用户信息 -->
        <div class="user-section">
          <div v-if="userStore.isLoggedIn" class="user-info">
            <el-dropdown @command="handleUserCommand">
              <span class="user-dropdown">
                <el-avatar :size="32" :src="userStore.userAvatar">
                  <el-icon><user /></el-icon>
                </el-avatar>
                <span class="username">{{ userStore.userName }}</span>
                <el-icon class="dropdown-icon"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                  <el-dropdown-item command="favorites">我的收藏</el-dropdown-item>
                  <el-dropdown-item command="addresses">收货地址</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div v-else class="guest-actions">
            <el-button type="primary" @click="router.push('/login')">登录</el-button>
            <el-button @click="router.push('/register')">注册</el-button>
          </div>
          
          <!-- 购物车 -->
          <div class="cart-section">
            <router-link to="/cart" class="cart-link">
              <el-badge :value="cartStore.totalCount" :max="99" class="cart-badge">
                <el-icon class="cart-icon"><shopping-cart /></el-icon>
              </el-badge>
              <span class="cart-text">购物车</span>
            </router-link>
          </div>
        </div>
      </div>
    </el-header>
    
    <!-- 主要内容区域 -->
    <el-main class="layout-main">
      <div class="main-content">
        <router-view />
      </div>
    </el-main>
    
    <!-- 底部 -->
    <el-footer class="layout-footer">
      <div class="footer-content">
        <div class="footer-section">
          <h4>帮助中心</h4>
          <ul>
            <li><a href="#">购物指南</a></li>
            <li><a href="#">支付方式</a></li>
            <li><a href="#">配送方式</a></li>
            <li><a href="#">售后服务</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>关于我们</h4>
          <ul>
            <li><a href="#">公司简介</a></li>
            <li><a href="#">联系我们</a></li>
            <li><a href="#">招聘信息</a></li>
            <li><a href="#">友情链接</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>客户服务</h4>
          <ul>
            <li><a href="#">在线客服</a></li>
            <li><a href="#">投诉建议</a></li>
            <li><a href="#">服务协议</a></li>
            <li><a href="#">隐私政策</a></li>
          </ul>
        </div>
        <div class="footer-section contact">
          <h4>联系方式</h4>
          <p>客服热线：400-123-4567</p>
          <p>工作时间：9:00-18:00</p>
          <p>客服邮箱：service@b2c.com</p>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 B2C商城. All rights reserved.</p>
      </div>
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import SearchBar from '@/components/SearchBar.vue'
// 不需要单独导入图标，main.ts中已全局注册

const userStore = useUserStore()
const cartStore = useCartStore()
const router = useRouter()

// 处理用户菜单命令
const handleUserCommand = async (command: string) => {
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
    case 'addresses':
      router.push('/user/addresses')
      break
    case 'logout':
      try {
        await userStore.logout()
        ElMessage.success('退出登录成功')
        router.push('/')
      } catch (error) {
        ElMessage.error('退出登录失败')
      }
      break
  }
}
</script>

<style scoped lang="scss">
.layout-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.layout-header {
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 80px;
  
  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
  }
  
  .logo {
    a {
      display: flex;
      align-items: center;
      text-decoration: none;
      color: #333;
      
      img {
        height: 40px;
        margin-right: 10px;
      }
      
      span {
        font-size: 24px;
        font-weight: bold;
        color: #409eff;
      }
    }
  }
  
  .search-section {
    flex: 1;
    max-width: 500px;
    margin: 0 40px;
  }
  
  .user-section {
    display: flex;
    align-items: center;
    gap: 20px;
    
    .user-info {
      .user-dropdown {
        display: flex;
        align-items: center;
        cursor: pointer;
        padding: 8px 12px;
        border-radius: 4px;
        transition: background-color 0.3s;
        
        &:hover {
          background-color: #f5f5f5;
        }
        
        .username {
          margin: 0 8px;
          font-size: 14px;
        }
        
        .dropdown-icon {
          font-size: 12px;
        }
      }
    }
    
    .guest-actions {
      display: flex;
      gap: 10px;
    }
    
    .cart-section {
      .cart-link {
        display: flex;
        align-items: center;
        text-decoration: none;
        color: #333;
        padding: 8px 12px;
        border-radius: 4px;
        transition: background-color 0.3s;
        
        &:hover {
          background-color: #f5f5f5;
        }
        
        .cart-badge {
          margin-right: 8px;
        }
        
        .cart-icon {
          font-size: 24px;
        }
        
        .cart-text {
          font-size: 14px;
        }
      }
    }
  }
}

.layout-main {
  padding: 0;
  background-color: #f5f5f5;
  
  .main-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    min-height: calc(100vh - 80px - 200px);
  }
}

.layout-footer {
  background: #2c3e50;
  color: #fff;
  padding: 40px 0 20px;
  
  .footer-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 40px;
    
    .footer-section {
      h4 {
        margin-bottom: 15px;
        font-size: 16px;
        color: #ecf0f1;
      }
      
      ul {
        list-style: none;
        
        li {
          margin-bottom: 8px;
          
          a {
            color: #bdc3c7;
            text-decoration: none;
            transition: color 0.3s;
            
            &:hover {
              color: #3498db;
            }
          }
        }
      }
      
      &.contact {
        p {
          margin-bottom: 8px;
          color: #bdc3c7;
        }
      }
    }
  }
  
  .footer-bottom {
    text-align: center;
    padding-top: 20px;
    border-top: 1px solid #34495e;
    color: #95a5a6;
    font-size: 14px;
  }
}

// 响应式设计
@media screen and (max-width: 768px) {
  .layout-header {
    .header-content {
      flex-direction: column;
      padding: 10px 20px;
      height: auto;
    }
    
    .search-section {
      width: 100%;
      margin: 10px 0;
    }
    
    .user-section {
      width: 100%;
      justify-content: space-between;
    }
  }
  
  .layout-footer {
    .footer-content {
      grid-template-columns: 1fr;
      gap: 20px;
    }
  }
}
</style>