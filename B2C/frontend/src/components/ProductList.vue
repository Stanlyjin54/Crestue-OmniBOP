<template>
  <div class="product-list">
    <!-- 视图模式切换 -->
    <div class="view-controls">
      <el-radio-group v-model="viewMode" size="small">
        <el-radio-button value="grid">
          <el-icon><Grid /></el-icon>
          网格视图
        </el-radio-button>
        <el-radio-button value="list">
          <el-icon><List /></el-icon>
          列表视图
        </el-radio-button>
      </el-radio-group>
      
      <!-- 排序选项 -->
      <div class="sort-controls">
        <el-select v-model="sortBy" placeholder="排序方式" size="small" style="width: 120px;">
          <el-option label="默认排序" value="default" />
          <el-option label="价格从低到高" value="price_asc" />
          <el-option label="价格从高到低" value="price_desc" />
          <el-option label="销量从高到低" value="sales_desc" />
          <el-option label="最新上架" value="newest" />
          <el-option label="供应商评级" value="rating_desc" />
        </el-select>
      </div>
    </div>

    <!-- 商品网格视图 -->
    <div v-if="viewMode === 'grid'" class="product-grid">
      <ProductCardEnhanced
        v-for="product in sortedProducts" 
        :key="product.id"
        :product="enhanceProductData(product)"
        @add-to-cart="handleAddToCart"
        @add-to-favorites="handleAddToFavorites"
        @compare="handleCompare"
      />
    </div>

    <!-- 商品列表视图 -->
    <div v-else class="product-list-view">
      <ProductListItem
        v-for="product in sortedProducts" 
        :key="product.id"
        :product="enhanceProductData(product)"
        @add-to-cart="handleAddToCart"
        @add-to-favorites="handleAddToFavorites"
        @compare="handleCompare"
      />
    </div>

    <!-- 分页 -->
    <div v-if="total > localPageSize" class="pagination-wrapper">
      <el-pagination
        v-model:current-page="localCurrentPage"
        v-model:page-size="localPageSize"
        :page-sizes="[12, 24, 48, 96]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
// 图标已在main.ts中全局注册，无需单独导入
import ProductCardEnhanced from './ProductCardEnhanced.vue'
import ProductListItem from './ProductListItem.vue'
// import PriceDisplay from './PriceDisplay.vue'
import { useCartStore } from '@/stores/cart'
import type { Product } from '@/types'

// 扩展产品类型定义，支持供应商平台数据
interface EnhancedProduct extends Product {
  supplier?: {
    id: string | number
    name: string
    rating?: number
    location?: {
      province?: string
      city?: string
      district?: string
      address?: string
    }
  }
  minOrderQuantity?: number
  leadTime?: string
  certifications?: string[]
  mainImage?: string
  tierPricing?: Array<{
    quantity: number
    price: number
    discount?: number
  }>
}

export interface Props {
  products: Product[]
  total?: number
  pageSize?: number
  currentPage?: number
}

export interface Emits {
  (e: 'add-to-cart', product: EnhancedProduct): void
  (e: 'add-to-favorites', product: EnhancedProduct): void
  (e: 'compare', product: EnhancedProduct): void
  (e: 'page-change', page: number, pageSize: number): void
}

const props = withDefaults(defineProps<Props>(), {
  total: 0,
  pageSize: 12,
  currentPage: 1
})

const emit = defineEmits<Emits>()
const cartStore = useCartStore()

// 本地状态，避免直接修改props
const localCurrentPage = ref(props.currentPage)
const localPageSize = ref(props.pageSize)

// 监听props变化并更新本地状态
watch(() => props.currentPage, (newValue) => {
  localCurrentPage.value = newValue
})

watch(() => props.pageSize, (newValue) => {
  localPageSize.value = newValue
})
// const router = useRouter()

// 视图模式和排序
const viewMode = ref<'grid' | 'list'>('grid')
const sortBy = ref<string>('default')

// 数据增强：映射供应商平台数据到B2C标准
const enhanceProductData = (product: Product): EnhancedProduct => {
  const enhanced: EnhancedProduct = { ...product }
  
  // 处理价格数据 - 使用Product接口定义的number类型
  if (typeof product.price === 'number') {
    enhanced.price = product.price
    // 如果有原始价格，确保它也是number类型
    if (product.originalPrice && typeof product.originalPrice === 'number') {
      enhanced.originalPrice = product.originalPrice
    }
  }
  
  // 处理图片数据
  if (Array.isArray(product.images)) {
    enhanced.mainImage = product.images[0] || product.image
  } else if (product.image) {
    enhanced.mainImage = product.image
  }
  
  // 模拟供应商数据（实际应该从API获取）
  if (!enhanced.supplier && Math.random() > 0.5) {
    enhanced.supplier = {
      id: `supplier_${Math.floor(Math.random() * 1000)}`,
      name: ['京东工品汇', '工帮帮', '工业品超市', '五金商城'][Math.floor(Math.random() * 4)],
      rating: 3 + Math.random() * 2, // 3-5星评级
      location: {
        province: ['北京', '上海', '广东', '江苏'][Math.floor(Math.random() * 4)],
        city: ['朝阳区', '浦东新区', '天河区', '玄武区'][Math.floor(Math.random() * 4)]
      }
    }
    
    // 随机添加工业品特色信息
    if (Math.random() > 0.7) {
      enhanced.minOrderQuantity = [1, 10, 50, 100][Math.floor(Math.random() * 4)]
      enhanced.leadTime = ['1-3天', '3-7天', '7-15天', '15-30天'][Math.floor(Math.random() * 4)]
      enhanced.certifications = ['ISO9001', 'CE认证', '3C认证'].filter(() => Math.random() > 0.5)
    }
  }
  
  return enhanced
}

// 排序逻辑
const sortedProducts = computed(() => {
  let products = [...props.products]
  
  switch (sortBy.value) {
    case 'price_asc':
      return products.sort((a, b) => a.price - b.price)
    case 'price_desc':
      return products.sort((a, b) => b.price - a.price)
    case 'sales_desc':
      return products.sort((a, b) => (b.sales || 0) - (a.sales || 0))
    case 'newest':
      return products.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    case 'rating_desc':
      return products.sort((a, b) => {
        const ratingA = (a as any).supplier?.rating || 0
        const ratingB = (b as any).supplier?.rating || 0
        return ratingB - ratingA
      })
    default:
      return products
  }
})

// 事件处理
const handleAddToCart = async (product: EnhancedProduct) => {
  try {
    await cartStore.addItem(product.id, 1)
    ElMessage.success('已添加到购物车')
    emit('add-to-cart', product)
  } catch (error) {
    console.error('添加到购物车失败:', error)
    ElMessage.error('添加失败')
  }
}

const handleAddToFavorites = (product: EnhancedProduct) => {
  // TODO: 实现收藏功能
  ElMessage.success('已添加到收藏夹')
  emit('add-to-favorites', product)
}

const handleCompare = (product: EnhancedProduct) => {
  // TODO: 实现对比功能
  ElMessage.info('已添加到对比列表')
  emit('compare', product)
}

const handleSizeChange = (size: number) => {
  localPageSize.value = size
  emit('page-change', localCurrentPage.value, size)
}

const handleCurrentChange = (page: number) => {
  localCurrentPage.value = page
  emit('page-change', page, localPageSize.value)
}
</script>

<style scoped lang="scss">
.product-list {
  padding: 20px 0;
}

// 视图控制区域
.view-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

  .el-radio-group {
    margin-right: 20px;
  }

  .sort-controls {
    display: flex;
    gap: 12px;
    align-items: center;

    .el-select {
      width: 150px;
    }
  }
}

// 商品网格视图
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

// 商品列表视图
.product-list-view {
  .product-list-item {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }

    .list-item-content {
      display: flex;
      gap: 20px;
      align-items: center;

      .product-image {
        flex-shrink: 0;
        width: 120px;
        height: 120px;
        border-radius: 8px;
        overflow: hidden;
        
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }

      .product-info {
        flex: 1;
        min-width: 0;

        .product-name {
          font-size: 18px;
          font-weight: 600;
          color: #333;
          margin: 0 0 8px;
          line-height: 1.4;
        }

        .product-desc {
          font-size: 14px;
          color: #666;
          margin: 0 0 12px;
          line-height: 1.5;
        }

        .product-meta {
          display: flex;
          gap: 20px;
          align-items: center;
          flex-wrap: wrap;
        }
      }

      .product-actions {
        flex-shrink: 0;
        display: flex;
        flex-direction: column;
        gap: 12px;
        align-items: flex-end;

        .el-button {
          min-width: 120px;
        }
      }
    }
  }
}

// 分页
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding: 20px 0;
}

@media (max-width: 768px) {
  .view-controls {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;

    .el-radio-group {
      margin-right: 0;
      justify-content: center;
    }

    .sort-controls {
      justify-content: center;

      .el-select {
        width: 100%;
        max-width: 200px;
      }
    }
  }

  .product-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 12px;
  }

  .product-list-view {
    .product-list-item {
      padding: 16px;

      .list-item-content {
        flex-direction: column;
        gap: 16px;

        .product-image {
          width: 100%;
          height: 200px;
          align-self: center;
        }

        .product-actions {
          width: 100%;
          flex-direction: row;

          .el-button {
            flex: 1;
            min-width: auto;
          }
        }
      }
    }
  }
}

@media (max-width: 480px) {
  .product-list {
    padding: 12px 0;
  }

  .view-controls {
    padding: 12px;
  }

  .product-grid {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 8px;
  }

  .product-list-view {
    .product-list-item {
      padding: 12px;

      .list-item-content {
        .product-image {
          height: 160px;
        }

        .product-info {
          .product-name {
            font-size: 16px;
          }

          .product-desc {
            font-size: 13px;
          }
        }
      }
    }
  }
}
</style>