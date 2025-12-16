<template>
  <div class="favorites-container">
    <div class="favorites-header">
      <h2>我的收藏</h2>
    </div>
    
    <div class="favorites-content" v-loading="loading">
      <div v-if="favorites.length === 0" class="empty-favorites">
        <el-empty description="暂无收藏商品" />
      </div>
      
      <div v-else class="favorites-grid">
        <div v-for="item in favorites" :key="item.id" class="favorite-item">
          <div class="product-card">
            <div class="product-image">
              <img :src="item.product.image" :alt="item.product.name" @click="goToProduct(item.product.id)" />
              <div class="product-actions">
                <el-button 
                  type="danger" 
                  size="small" 
                  circle
                  @click="handleRemoveFavorite(item.id)"
                >
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
            
            <div class="product-info">
              <h3 class="product-name" @click="goToProduct(item.product.id)">
                {{ item.product.name }}
              </h3>
              <p class="product-desc">{{ item.product.description }}</p>
              
              <div class="product-price">
                <span class="price">￥{{ item.product.price }}</span>
                <span v-if="item.product.originalPrice" class="original-price">
                  ￥{{ item.product.originalPrice }}
                </span>
              </div>
              
              <div class="product-actions-bottom">
                <el-button type="primary" size="small" @click="addToCart(item.product)">
                  加入购物车
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div v-if="total > 0" class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[12, 24, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import { getFavoriteList, removeFavorite as removeFavoriteApi } from '@/api/favorite'
import { useCartStore } from '@/stores/cart'
import type { Favorite } from '@/types'

const router = useRouter()
const cartStore = useCartStore()

const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const favorites = ref<Favorite[]>([])

const fetchFavorites = async () => {
  try {
    loading.value = true
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value
    }
    const response = await getFavoriteList(params)
    favorites.value = response.data.items
    total.value = response.data.total
  } catch (error) {
    ElMessage.error('获取收藏列表失败')
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  fetchFavorites()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchFavorites()
}

const goToProduct = (productId: number) => {
  router.push({
    name: 'ProductDetail',
    params: { id: productId }
  })
}

const handleRemoveFavorite = async (favoriteId: number) => {
  try {
    await removeFavoriteApi(favoriteId)
    ElMessage.success('已取消收藏')
    fetchFavorites()
  } catch (error) {
    ElMessage.error('取消收藏失败')
  }
}

const addToCart = async (product: any) => {
  try {
    await cartStore.addItem(product.id, 1, product.defaultSkuId)
    ElMessage.success('已加入购物车')
  } catch (error) {
    ElMessage.error('加入购物车失败')
  }
}

onMounted(() => {
  fetchFavorites()
})
</script>

<style scoped lang="scss">
.favorites-container {
  padding: 20px;
}

.favorites-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 500;
  }
}

.empty-favorites {
  padding: 60px 0;
  text-align: center;
}

.favorites-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.favorite-item {
  .product-card {
    border: 1px solid #eee;
    border-radius: 8px;
    overflow: hidden;
    background: #fff;
    transition: all 0.3s ease;
    
    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }
  }
}

.product-image {
  position: relative;
  width: 100%;
  height: 280px;
  overflow: hidden;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    cursor: pointer;
    transition: transform 0.3s ease;
    
    &:hover {
      transform: scale(1.05);
    }
  }
  
  .product-actions {
    position: absolute;
    top: 10px;
    right: 10px;
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  
  &:hover .product-actions {
    opacity: 1;
  }
}

.product-info {
  padding: 15px;
  
  .product-name {
    margin: 0 0 8px;
    font-size: 16px;
    font-weight: 500;
    color: #333;
    cursor: pointer;
    transition: color 0.3s ease;
    
    &:hover {
      color: #409eff;
    }
  }
  
  .product-desc {
    margin: 0 0 10px;
    font-size: 14px;
    color: #999;
    line-height: 1.4;
    height: 40px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  
  .product-price {
    margin-bottom: 15px;
    
    .price {
      font-size: 18px;
      font-weight: bold;
      color: #f56c6c;
    }
    
    .original-price {
      margin-left: 8px;
      font-size: 14px;
      color: #999;
      text-decoration: line-through;
    }
  }
  
  .product-actions-bottom {
    text-align: center;
  }
}

.pagination {
  display: flex;
  justify-content: center;
}
</style>