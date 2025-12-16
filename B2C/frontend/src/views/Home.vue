<template>
  <div class="home-page">
    <!-- 轮播图 -->
    <div class="banner-section">
      <el-carousel height="400px" indicator-position="outside">
        <el-carousel-item v-for="banner in banners" :key="banner.id">
          <img :src="banner.image" :alt="banner.title" class="banner-image" />
          <div class="banner-content">
            <h2>{{ banner.title }}</h2>
            <p>{{ banner.description }}</p>
            <el-button type="primary" @click="goToProduct(banner.productId)">
              立即购买
            </el-button>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 分类导航 -->
    <div class="category-section">
      <div class="section-header">
        <h2>商品分类</h2>
        <el-button text @click="goToCategory">查看全部</el-button>
      </div>
      <CategoryNav 
        :categories="categories" 
        :active-category="activeCategory"
        @select-category="handleCategorySelect"
      />
    </div>

    <!-- 搜索栏 -->
    <div class="search-section">
      <SearchBar @search="handleSearch" />
    </div>

    <!-- 推荐商品 -->
    <div class="recommend-section">
      <div class="section-header">
        <h2>推荐商品</h2>
        <el-button text @click="loadMoreProducts">加载更多</el-button>
      </div>
      <ProductList 
        :products="recommendProducts" 
        @add-to-cart="handleAddToCart"
      />
    </div>

    <!-- 购物车汇总 -->
    <CartSummary />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import SearchBar from '@/components/SearchBar.vue'
import ProductList from '@/components/ProductList.vue'
import CartSummary from '@/components/CartSummary.vue'
// import { useCartStore } from '@/stores/cart'
import { getProductList, getProductCategories } from '@/api'
import type { Product, Category } from '@/types'

const router = useRouter()
// const cartStore = useCartStore()

// 数据
const banners = ref([
  {
    id: 1,
    title: '新品上市',
    description: '最新科技产品，限时优惠',
    image: 'https://via.placeholder.com/1200x400/409eff/fff?text=新品上市',
    productId: 1
  },
  {
    id: 2,
    title: '限时特惠',
    description: '精选商品，低至5折',
    image: 'https://via.placeholder.com/1200x400/ff6b6b/fff?text=限时特惠',
    productId: 2
  }
])

const categories = ref<Category[]>([])
const activeCategory = ref<number>()
const recommendProducts = ref<Product[]>([])

// 方法
const loadData = async () => {
  try {
    // 获取分类
    const categoryResponse = await getProductCategories()
    categories.value = categoryResponse
    
    // 获取推荐商品
    const productResponse = await getProductList({ page: 1, pageSize: 8 })
    recommendProducts.value = productResponse.list
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

const handleCategorySelect = (categoryId: number) => {
  activeCategory.value = categoryId
  router.push({
    path: '/products',
    query: { categoryId }
  })
}

const handleSearch = (keyword: string) => {
  router.push({
    path: '/products',
    query: { keyword }
  })
}

const handleAddToCart = (product: Product) => {
  console.log('添加到购物车:', product.name)
}

const goToProduct = (productId: number) => {
  router.push(`/product/${productId}`)
}

const goToCategory = () => {
  router.push('/products')
}

const loadMoreProducts = async () => {
  try {
    const response = await getProductList({ 
      page: Math.floor(recommendProducts.value.length / 8) + 1, 
      pageSize: 8 
    })
    recommendProducts.value.push(...response.list)
  } catch (error) {
    ElMessage.error('加载更多商品失败')
  }
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.home-page {
  padding-bottom: 80px;
}

.banner-section {
  margin-bottom: 30px;
  
  .banner-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .banner-content {
    position: absolute;
    top: 50%;
    left: 50px;
    transform: translateY(-50%);
    color: #fff;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    
    h2 {
      font-size: 36px;
      margin: 0 0 10px;
    }
    
    p {
      font-size: 18px;
      margin: 0 0 20px;
    }
  }
}

.category-section,
.search-section,
.recommend-section {
  margin-bottom: 30px;
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  h2 {
    font-size: 24px;
    font-weight: 600;
    color: #333;
    margin: 0;
  }
}

@media (max-width: 768px) {
  .banner-section {
    .banner-content {
      left: 20px;
      
      h2 {
        font-size: 24px;
      }
      
      p {
        font-size: 14px;
      }
    }
  }
  
  .category-section,
  .search-section,
  .recommend-section {
    padding: 0 15px;
    margin-bottom: 20px;
  }
  
  .section-header {
    h2 {
      font-size: 18px;
    }
  }
}
</style>