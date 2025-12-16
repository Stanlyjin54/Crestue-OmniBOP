<template>
  <div class="product-list">
    <div class="container">
      <!-- 搜索和筛选栏 -->
      <div class="filter-section">
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索商品..."
            size="large"
            clearable
            @keyup.enter="handleSearch"
            @clear="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch" :icon="Search" />
            </template>
          </el-input>
        </div>
        
        <!-- 分类筛选 -->
        <div class="category-filter">
          <el-cascader
            v-model="selectedCategory"
            :options="categories"
            :props="categoryProps"
            placeholder="选择分类"
            clearable
            @change="handleCategoryChange"
          />
        </div>
        
        <!-- 价格筛选 -->
        <div class="price-filter">
          <el-select v-model="priceRange" placeholder="价格区间" clearable @change="handlePriceChange">
            <el-option label="0-100元" value="0-100" />
            <el-option label="100-500元" value="100-500" />
            <el-option label="500-1000元" value="500-1000" />
            <el-option label="1000元以上" value="1000-999999" />
          </el-select>
        </div>
        
        <!-- 排序 -->
        <div class="sort-filter">
          <el-select v-model="sortBy" placeholder="排序方式" @change="handleSortChange">
            <el-option label="综合排序" value="default" />
            <el-option label="价格从低到高" value="price-asc" />
            <el-option label="价格从高到低" value="price-desc" />
            <el-option label="销量优先" value="sales-desc" />
            <el-option label="好评优先" value="rating-desc" />
          </el-select>
        </div>
      </div>

      <!-- 商品列表 -->
      <div class="products-grid" v-loading="loading">
        <div v-if="products.length === 0 && !loading" class="empty-state">
          <el-result
            icon="info"
            title="暂无商品"
            sub-title="试试调整筛选条件或搜索关键词"
          >
            <template #extra>
              <el-button type="primary" @click="resetFilters">重置筛选</el-button>
            </template>
          </el-result>
        </div>
        
        <div v-for="product in products" :key="product.id" class="product-card">
          <div class="product-image" @click="goToProduct(product.id)">
            <el-image 
              :src="product.image" 
              :alt="product.name"
              fit="cover"
              lazy
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
          </div>
          
          <div class="product-info">
            <h3 class="product-name" @click="goToProduct(product.id)">
              {{ product.name }}
            </h3>
            <p class="product-desc">{{ product.description }}</p>
            
            <div class="product-price">
              <span class="price-current">¥{{ product.price }}</span>
              <span class="price-original" v-if="product.originalPrice">
                ¥{{ product.originalPrice }}
              </span>
            </div>
            
            <div class="product-meta">
              <span class="sales">已售{{ product.sales }}件</span>
              <span class="rating" v-if="product.rating && product.rating > 0">
                <el-rate :model-value="product.rating" disabled size="small" />
                {{ product.rating }}分
              </span>
            </div>
            
            <div class="product-actions">
              <el-button 
                type="primary" 
                size="small" 
                @click="addToCart(product)"
                :disabled="product.stock === 0"
              >
                <el-icon><ShoppingCart /></el-icon>
                加购物车
              </el-button>
              <el-button 
                type="danger" 
                size="small" 
                @click="buyNow(product)"
                :disabled="product.stock === 0"
              >
                立即购买
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-section" v-if="total > 0">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[12, 24, 36, 48]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Picture, ShoppingCart } from '@element-plus/icons-vue'
// 图标已在main.ts中全局注册，无需单独导入
import { useCartStore } from '@/stores/cart'
import { getProductList, getCategoryList } from '@/api/product'
import type { Product, Category } from '@/types'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

// 响应式数据
const loading = ref(false)
const products = ref<Product[]>([])
const categories = ref<Category[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const searchKeyword = ref('')
const selectedCategory = ref<number[]>([])
const priceRange = ref('')
const sortBy = ref('default')

// 分类选择器配置
const categoryProps = {
  expandTrigger: 'hover' as const,
  checkStrictly: true,
  value: 'id',
  label: 'name',
  children: 'children'
}

// 计算属性
const queryParams = computed(() => ({
  page: currentPage.value,
  pageSize: pageSize.value,
  keyword: searchKeyword.value,
  categoryId: selectedCategory.value[selectedCategory.value.length - 1] || undefined,
  priceMin: priceRange.value ? Number(priceRange.value.split('-')[0]) : undefined,
  priceMax: priceRange.value ? Number(priceRange.value.split('-')[1]) : undefined,
  sortBy: sortBy.value
}))

// 方法
const loadProducts = async () => {
  loading.value = true
  try {
    const response = await getProductList(queryParams.value)
    products.value = response.list
    total.value = response.total
  } catch (error: any) {
    ElMessage.error(error.message || '加载商品列表失败')
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    const response = await getCategoryList()
    categories.value = response
  } catch (error: any) {
    console.error('加载分类失败:', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadProducts()
}

const handleCategoryChange = () => {
  currentPage.value = 1
  loadProducts()
}

const handlePriceChange = () => {
  currentPage.value = 1
  loadProducts()
}

const handleSortChange = () => {
  currentPage.value = 1
  loadProducts()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadProducts()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadProducts()
}

const resetFilters = () => {
  searchKeyword.value = ''
  selectedCategory.value = []
  priceRange.value = ''
  sortBy.value = 'default'
  currentPage.value = 1
  loadProducts()
}

const goToProduct = (productId: number) => {
  router.push(`/product/${productId}`)
}

const addToCart = async (product: Product) => {
  try {
    await cartStore.addItem(product.id, 1)
    ElMessage.success('已添加到购物车')
  } catch (error: any) {
    ElMessage.error(error.message || '添加购物车失败')
  }
}

const buyNow = async (product: Product) => {
  try {
    await cartStore.addItem(product.id, 1)
    router.push('/cart')
  } catch (error: any) {
    ElMessage.error(error.message || '购买失败')
  }
}

// 监听路由参数变化
watch(() => route.query, () => {
  // 从URL参数恢复筛选状态
  if (route.query.keyword) searchKeyword.value = route.query.keyword as string
  if (route.query.category) selectedCategory.value = [Number(route.query.category)]
  if (route.query.price) priceRange.value = route.query.price as string
  if (route.query.sort) sortBy.value = route.query.sort as string
  if (route.query.page) currentPage.value = Number(route.query.page)
  
  loadProducts()
}, { immediate: true })

// 生命周期
onMounted(() => {
  loadCategories()
})
</script>

<style scoped lang="scss">
.product-list {
  padding: 20px 0;
  background-color: #f5f5f5;
  min-height: calc(100vh - 120px);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.filter-section {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  align-items: center;
  flex-wrap: wrap;
}

.search-bar {
  flex: 1;
  min-width: 300px;
}

.category-filter,
.price-filter,
.sort-filter {
  min-width: 150px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  }
}

.product-image {
  position: relative;
  width: 100%;
  height: 280px;
  overflow: hidden;
  cursor: pointer;
  
  .el-image {
    width: 100%;
    height: 100%;
    transition: transform 0.3s;
    
    &:hover {
      transform: scale(1.05);
    }
  }
}

.image-error {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 48px;
}

.product-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: #ff6b6b;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.product-info {
  padding: 20px;
}

.product-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
  cursor: pointer;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  
  &:hover {
    color: #409eff;
  }
}

.product-desc {
  font-size: 14px;
  color: #909399;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-price {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.price-current {
  font-size: 20px;
  font-weight: bold;
  color: #ff6b6b;
}

.price-original {
  font-size: 14px;
  color: #c0c4cc;
  text-decoration: line-through;
}

.discount-badge {
  background: #ff6b6b;
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  font-size: 12px;
  color: #909399;
}

.sales {
  font-size: 12px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
}

.product-actions {
  display: flex;
  gap: 10px;
}

.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 60px 0;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

// 响应式设计
@media (max-width: 768px) {
  .filter-section {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-bar {
    min-width: auto;
  }
  
  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 15px;
  }
  
  .product-image {
    height: 200px;
  }
  
  .product-actions {
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
  
  .product-image {
    height: 180px;
  }
  
  .product-info {
    padding: 15px;
  }
  
  .product-name {
    font-size: 14px;
  }
  
  .price-current {
    font-size: 18px;
  }
}
</style>