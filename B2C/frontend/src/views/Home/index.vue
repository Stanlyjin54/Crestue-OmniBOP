<template>
  <div class="home-container">
    <!-- 轮播图 -->
    <section class="hero-section">
      <el-carousel height="400px" indicator-position="outside">
        <el-carousel-item v-for="banner in banners" :key="banner.id">
          <div class="banner-item" :style="{ backgroundImage: `url(${banner.image})` }">
            <div class="banner-content">
              <h2 class="banner-title">{{ banner.title }}</h2>
              <p class="banner-desc">{{ banner.description }}</p>
              <el-button type="primary" size="large" @click="handleBannerClick(banner)">
                立即查看
              </el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <!-- 分类导航 -->
    <section class="category-section">
      <div class="section-header">
        <h3 class="section-title">热门分类</h3>
        <el-button type="text" @click="router.push('/category')">查看更多</el-button>
      </div>
      <div class="category-grid">
        <div 
          v-for="category in categories" 
          :key="category.id"
          class="category-item"
          @click="handleCategoryClick(category)"
        >
          <div class="category-icon">
            <el-icon :size="32" :icon="category.icon" />
          </div>
          <span class="category-name">{{ category.name }}</span>
        </div>
      </div>
    </section>

    <!-- 热销商品 -->
    <section class="products-section">
      <div class="section-header">
        <h3 class="section-title">热销商品</h3>
        <el-button type="text" @click="router.push('/search?sort=sales')">查看更多</el-button>
      </div>
      <ProductGrid :products="hotProducts?.list || []" :loading="productStore.loading" />
    </section>

    <!-- 新品推荐 -->
    <section class="products-section">
      <div class="section-header">
        <h3 class="section-title">新品推荐</h3>
        <el-button type="text" @click="router.push('/search?sort=created_at')">查看更多</el-button>
      </div>
      <ProductGrid :products="newProducts?.list || []" :loading="productStore.loading" />
    </section>

    <!-- 限时折扣 -->
    <section class="products-section">
      <div class="section-header">
        <h3 class="section-title">限时折扣</h3>
        <el-button type="text" @click="router.push('/search?type=discount')">查看更多</el-button>
      </div>
      <ProductGrid :products="discountProducts?.list || []" :loading="productStore.loading" />
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useProductStore } from '@/stores/product'
import ProductGrid from '@/components/ProductGrid.vue'
import { usePageCache } from '@/utils'

const router = useRouter()
const productStore = useProductStore()
// const { isOnline } = useNetworkStatus() // 未使用的变量

// 轮播图数据
const banners = ref([
  {
    id: 1,
    title: '春季新品大促',
    description: '全场8折起，满299减50',
    image: '/images/banner1.jpg',
    link: '/search?type=new'
  },
  {
    id: 2,
    title: '数码专场',
    description: '精选数码产品，限时优惠',
    image: '/images/banner2.jpg',
    link: '/category/1'
  },
  {
    id: 3,
    title: '品质生活',
    description: '家居好物，品质生活',
    image: '/images/banner3.jpg',
    link: '/category/2'
  }
])

// 分类数据
const categories = ref([
  { id: 1, name: '数码电器', icon: 'monitor' },
  { id: 2, name: '家居生活', icon: 'home-filled' },
  { id: 3, name: '运动户外', icon: 'football' },
  { id: 4, name: '服装配饰', icon: 'shopping-bag' },
  { id: 5, name: '美妆护肤', icon: 'goods' },
  { id: 6, name: '手机通讯', icon: 'iphone' },
  { id: 7, name: '摄影摄像', icon: 'camera' },
  { id: 8, name: '影音娱乐', icon: 'headset' }
])

// 使用页面缓存获取推荐商品
const {
  data: hotProducts,
  loadData: loadHotProducts
} = usePageCache(
  () => productStore.fetchRecommendProducts('hot'),
  { key: 'home-hot-products', expireTime: 5 * 60 * 1000 }
)

const {
  data: newProducts,
  loadData: loadNewProducts
} = usePageCache(
  () => productStore.fetchRecommendProducts('new'),
  { key: 'home-new-products', expireTime: 5 * 60 * 1000 }
)

const {
  data: discountProducts,
  loadData: loadDiscountProducts
} = usePageCache(
  () => productStore.fetchRecommendProducts('discount'),
  { key: 'home-discount-products', expireTime: 5 * 60 * 1000 }
)

// 整体加载状态
// const loading = computed(() => hotLoading.value || newLoading.value || discountLoading.value) // 未使用的变量
// const error = computed(() => hotError.value || newError.value || discountError.value) // 未使用的变量

// 方法
const handleBannerClick = (banner: any) => {
  if (banner.link) {
    router.push(banner.link)
  }
}

const handleCategoryClick = (category: any) => {
  router.push(`/category/${category.id}`)
}

// 初始化数据
onMounted(async () => {
  try {
    // 并行获取推荐商品
    await Promise.all([
      loadHotProducts(),
      loadNewProducts(),
      loadDiscountProducts()
    ])
  } catch (error) {
    console.error('加载商品数据失败:', error)
  }
})
</script>

<style scoped lang="scss">
.home-container {
  padding: 0 0 40px;
}

.hero-section {
  margin-bottom: 40px;
  
  .banner-item {
    height: 400px;
    background-size: cover;
    background-position: center;
    display: flex;
    align-items: center;
    position: relative;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(45deg, rgba(0,0,0,0.7) 0%, rgba(0,0,0,0.3) 100%);
    }
    
    .banner-content {
      position: relative;
      z-index: 1;
      color: white;
      text-align: center;
      max-width: 600px;
      margin: 0 auto;
      padding: 0 20px;
    }
    
    .banner-title {
      font-size: 48px;
      font-weight: bold;
      margin-bottom: 16px;
      text-shadow: 0 2px 4px rgba(0,0,0,0.3);
    }
    
    .banner-desc {
      font-size: 20px;
      margin-bottom: 32px;
      opacity: 0.9;
    }
  }
}

.category-section {
  margin-bottom: 40px;
  padding: 0 20px;
  
  .category-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    gap: 20px;
    margin-top: 20px;
  }
  
  .category-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    background: white;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 16px rgba(0,0,0,0.15);
      
      .category-icon {
        color: #3b82f6;
        transform: scale(1.1);
      }
    }
    
    .category-icon {
      color: #64748b;
      margin-bottom: 8px;
      transition: all 0.3s ease;
    }
    
    .category-name {
      font-size: 14px;
      color: #334155;
      font-weight: 500;
    }
  }
}

.products-section {
  margin-bottom: 40px;
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .section-title {
    font-size: 24px;
    font-weight: 600;
    color: #1e293b;
  }
}

@media (max-width: 768px) {
  .hero-section {
    .banner-item {
      height: 300px;
      
      .banner-title {
        font-size: 32px;
      }
      
      .banner-desc {
        font-size: 16px;
      }
    }
  }
  
  .category-grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 12px;
  }
  
  .category-item {
    padding: 16px 8px;
    
    .category-name {
      font-size: 12px;
    }
  }
}
</style>