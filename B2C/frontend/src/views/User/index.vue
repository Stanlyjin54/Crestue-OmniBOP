<template>
  <div class="user-container">
    <div class="user-sidebar">
      <div class="user-avatar">
        <el-avatar :size="80" :src="userInfo?.avatar || defaultAvatar" />
        <h3>{{ userInfo?.username || '用户' }}</h3>
        <p class="user-level">{{ getUserLevel() }}</p>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        class="user-menu"
        @select="handleMenuSelect"
      >
        <el-menu-item index="dashboard">
          <el-icon><House /></el-icon>
          <span>我的主页</span>
        </el-menu-item>
        <el-menu-item index="profile">
          <el-icon><User /></el-icon>
          <span>个人信息</span>
        </el-menu-item>
        <el-menu-item index="settings">
          <el-icon><Setting /></el-icon>
          <span>账号设置</span>
        </el-menu-item>
        <el-menu-item index="orders">
          <el-icon><Document /></el-icon>
          <span>我的订单</span>
        </el-menu-item>
        <el-menu-item index="favorites">
          <el-icon><Star /></el-icon>
          <span>我的收藏</span>
        </el-menu-item>
        <el-menu-item index="addresses">
          <el-icon><Location /></el-icon>
          <span>收货地址</span>
        </el-menu-item>
      </el-menu>
    </div>
    
    <div class="user-content">
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const defaultAvatar = '/images/default-avatar.png'

const userInfo = computed(() => userStore.userInfo)

const activeMenu = computed(() => {
  const path = route.path
  if (path.includes('/user/dashboard')) return 'dashboard'
  if (path.includes('/user/profile')) return 'profile'
  if (path.includes('/user/settings')) return 'settings'
  if (path.includes('/user/orders')) return 'orders'
  if (path.includes('/user/favorites')) return 'favorites'
  if (path.includes('/user/addresses')) return 'addresses'
  return 'dashboard'
})

const getUserLevel = () => {
  if (!userInfo.value) return '普通会员'
  const levels = ['普通会员', '铜牌会员', '银牌会员', '金牌会员', '钻石会员']
  const level = userInfo.value.level || 0
  return levels[level] || '普通会员'
}

const handleMenuSelect = (index: string) => {
  router.push(`/user/${index}`)
}

onMounted(async () => {
  if (!userStore.userInfo) {
    try {
      await userStore.fetchUserInfo()
    } catch (error) {
      ElMessage.error('获取用户信息失败')
    }
  }
})
</script>

<style scoped lang="scss">
.user-container {
  display: flex;
  min-height: 600px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.user-sidebar {
  width: 240px;
  padding: 20px;
  border-right: 1px solid #eee;
  background: #fafafa;
}

.user-avatar {
  text-align: center;
  margin-bottom: 30px;
  
  h3 {
    margin: 15px 0 5px;
    font-size: 18px;
    font-weight: 500;
  }
  
  .user-level {
    color: #666;
    font-size: 14px;
    margin: 0;
  }
}

.user-menu {
  border: none;
  background: transparent;
  
  .el-menu-item {
    margin: 5px 0;
    border-radius: 6px;
    
    &:hover {
      background: #e6f7ff;
    }
    
    &.is-active {
      background: #409eff;
      color: #fff;
    }
  }
}

.user-content {
  flex: 1;
  padding: 20px;
}

@media (max-width: 768px) {
  .user-container {
    flex-direction: column;
  }
  
  .user-sidebar {
    width: 100%;
    border-right: none;
    border-bottom: 1px solid #eee;
  }
  
  .user-menu {
    display: flex;
    overflow-x: auto;
    padding: 0;
    
    .el-menu-item {
      min-width: 100px;
      text-align: center;
    }
  }
}
</style>