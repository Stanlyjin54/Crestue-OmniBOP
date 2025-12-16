<template>
  <div class="product-list-page">
    <!-- 筛选条件 -->
    <div class="filter-section">
      <div class="filter-item">
        <span class="filter-label">分类：</span>
        <el-cascader
          v-model="selectedCategory"
          :options="categoryOptions"
          :props="{ checkStrictly: true }"
          clearable
          placeholder="全部分类"
          @change="handleFilterChange"
        />
      </div>
      
      <div class="filter-item">
        <span class="filter-label">价格：</span>
        <el-input-number
          v-model="priceRange[0]"
          :min="0"
          placeholder="最低价"
          @change="handleFilterChange"
        />
        <span class="price-separator">-</span>
        <el-input-number
          v-model="priceRange[1]"
          :min="0"
          placeholder="最高价"
          @change="handleFilterChange"
        />
      </div>
      
      <div class="filter-item">
        <span class="filter-label">排序：</span>
        <el-select v-model="sortBy" @change="handleFilterChange">
          <el-option label="综合排序" value="default" />
          <el-option label="价格从低到高" value="price_asc" />
          <el-option label="价格从高到低" value="price_desc" />
          <el-option label="销量优先" value="sales" />
          <el-option label="好评优先" value="rating" />
        </el-select>
      </div>
    </div>

    <!-- 商品列表 -->
    <div class="product-section">
      <div class="section-header">
        <h2>商品列表</h2>
        <span class="result-count">共 {{ total }} 件商品</span>
      </div>
      
      <LoadingSpinner v-if="loading" text="正在加载商品..." />
      
      <ErrorState 
        v-else-if="error"
        type="error"
        :message="error"
        :show-retry="true"
        @retry="handleRetry"
      />
      
      <div v-else-if="products.length === 0" class="empty-container">
        <ErrorState 
          type="empty"
          title="暂无商品"
          message="当前分类下没有商品，试试其他筛选条件"
          :show-retry="false"
          :show-back="false"
        />
      </div>
      
      <ProductList 
        v-else
        :products="products" 
        @add-to-cart="handleAddToCart"
      />
    </div>

    <!-- 分页 -->
    <div class="pagination-section">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[20, 40, 80, 120]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, onActivated } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import ProductList from '@/components/ProductList.vue'
import LoadingSpinner from '@/components/LoadingSpinner.vue'
import ErrorState from '@/components/ErrorState.vue'
import { useListCache, useAsyncState, debounce } from '@/utils'
import { getProductList, getProductCategories } from '@/api'
import type { Product, Category } from '@/types'

const route = useRoute()

// 筛选条件
const selectedCategory = ref<number[]>([])
const priceRange = ref<[number, number]>([0, 0])
const sortBy = ref('default')

// 使用列表缓存
const {
  list: products,
  total,
  currentPage,
  pageSize,
  loading,
  error,
  loadList,
  handlePageChange,
  handleSizeChange
} = useListCache<Product>(
  async (page, size) => {
    const params = {
      keyword: route.query.keyword as string,
      categoryId: selectedCategory.value[selectedCategory.value.length - 1] || undefined,
      minPrice: priceRange.value[0] || undefined,
      maxPrice: priceRange.value[1] || undefined,
      sort: sortBy.value !== 'default' ? sortBy.value : undefined,
      page,
      pageSize: size
    }
    
    const response = await getProductList(params)
    return {
      list: response.list,
      total: response.total
    }
  },
  {
    key: 'products-list',
    expireTime: 3 * 60 * 1000 // 3分钟缓存
  }
)

// 分类数据状态
const {
  execute: loadCategories
} = useAsyncState()

// 分类数据
const categoryOptions = ref<any[]>([])

// 获取分类数据
const fetchCategories = async () => {
  const response = await getProductCategories()
  categoryOptions.value = response.map((category: Category) => ({
    value: category.id,
    label: category.name,
    children: category.children?.map((child: Category) => ({
      value: child.id,
      label: child.name
    }))
  }))
}

// 筛选条件变化（防抖处理）
const handleFilterChange = debounce(() => {
  handlePageChange(1)
}, 500)

// 分页变化处理
const handleCurrentChange = (page: number) => {
  handlePageChange(page)
}

// 重试加载
const handleRetry = () => {
  loadList(currentPage.value, pageSize.value, false)
}

// 添加到购物车
const handleAddToCart = (product: Product) => {
  console.log('添加到购物车:', product.name)
  ElMessage.success(`已添加 ${product.name} 到购物车`)
}

// 监听路由参数变化
watch(() => route.query, () => {
  handlePageChange(1)
})

// 生命周期
onMounted(async () => {
  try {
    // 加载分类数据
    await loadCategories(fetchCategories)
  } catch (error) {
    console.error('加载分类数据失败', error)
  }
})

// 组件激活时刷新数据
onActivated(() => {
  // 如果网络恢复在线，刷新数据
  if (navigator.onLine) {
    loadList(currentPage.value, pageSize.value)
  }
})
</script>

<style scoped lang="scss">
.product-list-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.filter-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
  
  .filter-item {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .filter-label {
      font-weight: 500;
      color: #333;
      white-space: nowrap;
    }
    
    .price-separator {
      margin: 0 5px;
      color: #999;
    }
  }
}

.product-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h2 {
      font-size: 20px;
      font-weight: 600;
      color: #333;
      margin: 0;
    }
    
    .result-count {
      color: #999;
      font-size: 14px;
    }
  }
  
  .loading-container {
    padding: 40px 0;
  }
  
  .empty-container {
    padding: 60px 0;
  }
}

.pagination-section {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

@media (max-width: 768px) {
  .product-list-page {
    padding: 15px;
  }
  
  .filter-section {
    padding: 15px;
    gap: 15px;
    
    .filter-item {
      flex-direction: column;
      align-items: flex-start;
      gap: 8px;
      
      .filter-label {
        font-size: 14px;
      }
    }
  }
  
  .product-section {
    padding: 15px;
    
    .section-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;
      
      h2 {
        font-size: 18px;
      }
    }
  }
}
</style>